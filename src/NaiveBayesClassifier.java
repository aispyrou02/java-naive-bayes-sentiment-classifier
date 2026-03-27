import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class NaiveBayesClassifier {

    private static final int DEFAULT_MAX_VOCAB_SIZE = 5500;
    private static final int DEFAULT_SKIP_TOP_WORDS = 15;

    private final Map<String, WordStats> vocabularyStats = new HashMap<>();
    private final List<String> selectedVocabulary = new ArrayList<>();

    private int positiveDocumentCount = 0;
    private int negativeDocumentCount = 0;

    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage:");
            System.out.println("java NaiveBayesClassifier <trainPosDir> <trainNegDir> <testPosDir> <testNegDir> [maxVocabSize] [skipTopWords]");
            return;
        }

        String trainPositiveDir = args[0];
        String trainNegativeDir = args[1];
        String testPositiveDir = args[2];
        String testNegativeDir = args[3];

        int maxVocabSize = args.length >= 5 ? Integer.parseInt(args[4]) : DEFAULT_MAX_VOCAB_SIZE;
        int skipTopWords = args.length >= 6 ? Integer.parseInt(args[5]) : DEFAULT_SKIP_TOP_WORDS;

        NaiveBayesClassifier classifier = new NaiveBayesClassifier();

        try {
            classifier.train(trainPositiveDir, trainNegativeDir);
            classifier.buildVocabulary(maxVocabSize, skipTopWords);

            EvaluationMetrics metrics = classifier.evaluate(testPositiveDir, testNegativeDir);

            System.out.println("Training complete.");
            System.out.println("Positive training documents: " + classifier.positiveDocumentCount);
            System.out.println("Negative training documents: " + classifier.negativeDocumentCount);
            System.out.println("Selected vocabulary size   : " + classifier.selectedVocabulary.size());
            System.out.println();

            metrics.printReport();

        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find one or more files/directories.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void train(String positiveTrainPath, String negativeTrainPath) throws FileNotFoundException {
        File positiveDir = new File(positiveTrainPath);
        File negativeDir = new File(negativeTrainPath);

        File[] positiveFiles = positiveDir.listFiles();
        File[] negativeFiles = negativeDir.listFiles();

        if (positiveFiles == null || negativeFiles == null) {
            throw new FileNotFoundException("Training directories are invalid.");
        }

        processTrainingFiles(positiveFiles, true);
        processTrainingFiles(negativeFiles, false);
    }

    private void processTrainingFiles(File[] files, boolean positiveClass) throws FileNotFoundException {
        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }

            if (positiveClass) {
                positiveDocumentCount++;
                if (positiveDocumentCount % 100 == 0) {
                    System.out.println(positiveDocumentCount + " positive training files processed.");
                }
            } else {
                negativeDocumentCount++;
                if (negativeDocumentCount % 100 == 0) {
                    System.out.println(negativeDocumentCount + " negative training files processed.");
                }
            }

            HashSet<String> uniqueTokensInFile = new HashSet<>(TextPreprocessor.extractUniqueTokens(file));

            for (String token : uniqueTokensInFile) {
                WordStats stats = vocabularyStats.computeIfAbsent(token, WordStats::new);
                if (positiveClass) {
                    stats.incrementPositive();
                } else {
                    stats.incrementNegative();
                }
            }
        }
    }

    public void buildVocabulary(int maxVocabSize, int skipTopWords) {
        List<WordStats> allWords = new ArrayList<>(vocabularyStats.values());
        Collections.sort(allWords);

        int start = Math.min(skipTopWords, allWords.size());
        int end = Math.min(maxVocabSize, allWords.size());

        for (int i = start; i < end; i++) {
            selectedVocabulary.add(allWords.get(i).getToken());
        }

        if (selectedVocabulary.isEmpty()) {
            throw new IllegalArgumentException("Selected vocabulary is empty. Adjust vocabulary parameters.");
        }
    }

    public boolean predict(File file) throws FileNotFoundException {
        HashSet<String> fileTokens = new HashSet<>(TextPreprocessor.extractUniqueTokens(file));

        double logProbPositive = Math.log((double) positiveDocumentCount / (positiveDocumentCount + negativeDocumentCount));
        double logProbNegative = Math.log((double) negativeDocumentCount / (positiveDocumentCount + negativeDocumentCount));

        for (String token : selectedVocabulary) {
            WordStats stats = vocabularyStats.get(token);

            int positivePresence = stats != null ? stats.getPositiveCount() : 0;
            int negativePresence = stats != null ? stats.getNegativeCount() : 0;

            double pWordGivenPositive = (positivePresence + 1.0) / (positiveDocumentCount + 2.0);
            double pWordGivenNegative = (negativePresence + 1.0) / (negativeDocumentCount + 2.0);

            if (fileTokens.contains(token)) {
                logProbPositive += Math.log(pWordGivenPositive);
                logProbNegative += Math.log(pWordGivenNegative);
            } else {
                logProbPositive += Math.log(1.0 - pWordGivenPositive);
                logProbNegative += Math.log(1.0 - pWordGivenNegative);
            }
        }

        return logProbPositive > logProbNegative;
    }

    public EvaluationMetrics evaluate(String positiveTestPath, String negativeTestPath) throws FileNotFoundException {
        EvaluationMetrics metrics = new EvaluationMetrics();

        File positiveDir = new File(positiveTestPath);
        File negativeDir = new File(negativeTestPath);

        File[] positiveFiles = positiveDir.listFiles();
        File[] negativeFiles = negativeDir.listFiles();

        if (positiveFiles == null || negativeFiles == null) {
            throw new FileNotFoundException("Testing directories are invalid.");
        }

        for (File file : positiveFiles) {
            if (!file.isFile()) {
                continue;
            }
            boolean prediction = predict(file);
            metrics.addPrediction(prediction, true);
        }

        for (File file : negativeFiles) {
            if (!file.isFile()) {
                continue;
            }
            boolean prediction = predict(file);
            metrics.addPrediction(prediction, false);
        }

        return metrics;
    }
}