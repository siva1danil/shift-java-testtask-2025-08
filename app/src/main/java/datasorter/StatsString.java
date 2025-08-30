package datasorter;

final class StatsString {
    private long count = 0;
    private int minLength = Integer.MAX_VALUE;
    private int maxLength = Integer.MIN_VALUE;

    void add(String value) {
        if (value == null)
            return;

        count++;
        int length = value.length();
        if (length < minLength)
            minLength = length;
        if (length > maxLength)
            maxLength = length;
    }

    String toShortString() {
        return String.format("количество=%d", count);
    }

    String toFullString() {
        if (count == 0)
            return String.format("количество=%d", count);
        return String.format("количество=%d, мин.длина=%d, макс.длина=%d", count, minLength, maxLength);
    }
}
