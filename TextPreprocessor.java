import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TextPreprocessor {

    public static Set<String> extractUniqueTokens(File file) throws FileNotFoundException {
        Set<String> tokens = new HashSet<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String token = normalizeToken(scanner.next());
                if (!token.isEmpty()) {
                    tokens.add(token);
                }
            }
        }

        return tokens;
    }

    public static String normalizeToken(String token) {
        return token.toLowerCase().replaceAll("[^a-z]", "");
    }
}