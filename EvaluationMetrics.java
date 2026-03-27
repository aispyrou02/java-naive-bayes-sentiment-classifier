public class EvaluationMetrics {
    private int truePositive = 0;
    private int trueNegative = 0;
    private int falsePositive = 0;
    private int falseNegative = 0;

    public void addPrediction(boolean predictedPositive, boolean actualPositive) {
        if (predictedPositive && actualPositive) {
            truePositive++;
        } else if (!predictedPositive && !actualPositive) {
            trueNegative++;
        } else if (predictedPositive) {
            falsePositive++;
        } else {
            falseNegative++;
        }
    }

    public double getAccuracy() {
        int total = truePositive + trueNegative + falsePositive + falseNegative;
        return total == 0 ? 0.0 : (double) (truePositive + trueNegative) / total;
    }

    public double getPrecision() {
        int denom = truePositive + falsePositive;
        return denom == 0 ? 0.0 : (double) truePositive / denom;
    }

    public double getRecall() {
        int denom = truePositive + falseNegative;
        return denom == 0 ? 0.0 : (double) truePositive / denom;
    }

    public double getF1Score() {
        double precision = getPrecision();
        double recall = getRecall();
        return (precision + recall) == 0 ? 0.0 : 2 * precision * recall / (precision + recall);
    }

    public void printReport() {
        System.out.println("Confusion Matrix:");
        System.out.println("TP: " + truePositive);
        System.out.println("TN: " + trueNegative);
        System.out.println("FP: " + falsePositive);
        System.out.println("FN: " + falseNegative);
        System.out.println();

        System.out.printf("Accuracy : %.2f%%%n", getAccuracy() * 100);
        System.out.printf("Precision: %.4f%n", getPrecision());
        System.out.printf("Recall   : %.4f%n", getRecall());
        System.out.printf("F1 Score : %.4f%n", getF1Score());
    }
}