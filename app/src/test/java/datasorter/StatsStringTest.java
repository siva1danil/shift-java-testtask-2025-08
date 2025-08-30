package datasorter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StatsStringTest {
    @Test
    void testAddNothing() {
        StatsString stats = new StatsString();
        assertEquals("количество=0", stats.toShortString());
        assertEquals("количество=0", stats.toFullString());
    }

    @Test
    void testAddNullValue() {
        StatsString stats = new StatsString();
        stats.add(null);
        assertEquals("количество=0", stats.toShortString());
        assertEquals("количество=0", stats.toFullString());
    }

    @Test
    void testAddSingleValue() {
        StatsString stats = new StatsString();
        stats.add("abc");
        assertEquals("количество=1", stats.toShortString());
        assertEquals("количество=1, мин.длина=3, макс.длина=3", stats.toFullString());
    }

    @Test
    void testAddMultipleStringsDifferentLengths() {
        StatsString stats = new StatsString();
        stats.add("a");
        stats.add("abcd");
        stats.add("abc");
        assertEquals("количество=3", stats.toShortString());
        assertEquals("количество=3, мин.длина=1, макс.длина=4", stats.toFullString());
    }

    @Test
    void testAddEmptyString() {
        StatsString stats = new StatsString();
        stats.add("");
        assertEquals("количество=1", stats.toShortString());
        assertEquals("количество=1, мин.длина=0, макс.длина=0", stats.toFullString());
    }

    @Test
    void testAddMultipleEmptyStrings() {
        StatsString stats = new StatsString();
        stats.add("");
        stats.add("");
        assertEquals("количество=2", stats.toShortString());
        assertEquals("количество=2, мин.длина=0, макс.длина=0", stats.toFullString());
    }

    @Test
    void testAddNullAndNonNullStrings() {
        StatsString stats = new StatsString();
        stats.add(null);
        stats.add("xyz");
        stats.add(null);
        stats.add("x");
        assertEquals("количество=2", stats.toShortString());
        assertEquals("количество=2, мин.длина=1, макс.длина=3", stats.toFullString());
    }
}
