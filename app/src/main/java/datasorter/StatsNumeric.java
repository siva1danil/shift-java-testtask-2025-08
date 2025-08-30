package datasorter;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class StatsNumeric {
    private long count = 0;
    private BigDecimal min = null;
    private BigDecimal max = null;
    private BigDecimal sum = BigDecimal.ZERO;

    void add(BigDecimal value) {
        if (value == null)
            return;

        count++;
        if (min == null || value.compareTo(min) == -1)
            min = value;
        if (max == null || value.compareTo(max) == 1)
            max = value;
        sum = sum.add(value);
    }

    String toShortString() {
        return String.format("количество=%d", count);
    }

    String toFullString() {
        if (count == 0)
            return String.format("количество=%d", count);
        BigDecimal avg = sum.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP);
        return String.format("количество=%d, мин=%s, макс=%s, сумма=%s, сред=%s", count, min.toPlainString(),
                max.toPlainString(), sum.toPlainString(), avg.toPlainString());
    }
}