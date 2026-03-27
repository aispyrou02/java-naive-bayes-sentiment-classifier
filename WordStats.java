public class WordStats implements Comparable<WordStats> {
    private final String token;
    private int positiveCount;
    private int negativeCount;

    public WordStats(String token) {
        this.token = token;
        this.positiveCount = 0;
        this.negativeCount = 0;
    }

    public String getToken() {
        return token;
    }

    public int getPositiveCount() {
        return positiveCount;
    }

    public int getNegativeCount() {
        return negativeCount;
    }

    public void incrementPositive() {
        positiveCount++;
    }

    public void incrementNegative() {
        negativeCount++;
    }

    public int getTotalCount() {
        return positiveCount + negativeCount;
    }

    @Override
    public int compareTo(WordStats other) {
        return Integer.compare(other.getTotalCount(), this.getTotalCount());
    }
}