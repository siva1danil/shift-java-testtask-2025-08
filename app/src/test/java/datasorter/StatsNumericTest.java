package datasorter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class StatsNumericTest {
    @Test
    public void testAddNothing() {
        StatsNumeric stats = new StatsNumeric();
        assertEquals("количество=0", stats.toShortString());
        assertEquals("количество=0", stats.toFullString());
    }

    @Test
    public void testAddZeroValue() {
        StatsNumeric stats = new StatsNumeric();
        stats.add(BigDecimal.ZERO);
        assertEquals("количество=1", stats.toShortString());
        assertEquals("количество=1, мин=0, макс=0, сумма=0, сред=0", stats.toFullString());
    }

    @Test
    public void testAddSingleValue() {
        StatsNumeric stats = new StatsNumeric();
        stats.add(new BigDecimal("10"));
        assertEquals("количество=1", stats.toShortString());
        assertEquals("количество=1, мин=10, макс=10, сумма=10, сред=10", stats.toFullString());
    }

    @Test
    public void testAddMultipleValues() {
        StatsNumeric stats = new StatsNumeric();
        stats.add(new BigDecimal("5"));
        stats.add(new BigDecimal("25"));
        stats.add(new BigDecimal("10"));
        stats.add(new BigDecimal("0"));
        assertEquals("количество=4", stats.toShortString());
        assertEquals("количество=4, мин=0, макс=25, сумма=40, сред=10", stats.toFullString());
    }

    @Test
    public void testAddNullValue() {
        StatsNumeric stats = new StatsNumeric();
        stats.add(null);
        assertEquals("количество=0", stats.toShortString());
        assertEquals("количество=0", stats.toFullString());
    }

    @Test
    public void testAddNegativeValues() {
        StatsNumeric stats = new StatsNumeric();
        stats.add(new BigDecimal("-6"));
        stats.add(new BigDecimal("0"));
        stats.add(new BigDecimal("6"));
        assertEquals("количество=3", stats.toShortString());
        assertEquals("количество=3, мин=-6, макс=6, сумма=0, сред=0", stats.toFullString());
    }

    @Test
    public void testAddSameValueMultipleTimes() {
        StatsNumeric stats = new StatsNumeric();
        stats.add(new BigDecimal("7"));
        stats.add(new BigDecimal("7"));
        stats.add(new BigDecimal("7"));
        assertEquals("количество=3", stats.toShortString());
        assertEquals("количество=3, мин=7, макс=7, сумма=21, сред=7", stats.toFullString());
    }
}