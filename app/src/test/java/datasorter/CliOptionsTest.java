package datasorter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

class CliOptionsTest {
    @Test
    void testParseWithAllOptions() {
        String[] args = { "-a", "-f", "-s", "-o", "outdir", "-p", "prefix", "input1.txt", "input2.txt" };
        CliOptions opt = CliOptions.parse(args);
        assertTrue(opt.append);
        assertTrue(opt.statsFull);
        assertFalse(opt.statsShort);
        assertEquals(Path.of("outdir"), opt.output);
        assertEquals("prefix", opt.prefix);
        assertEquals(List.of(Path.of("input1.txt"), Path.of("input2.txt")), opt.input);
    }

    @Test
    void testParseNoStatsDefault() {
        String[] args = { "input.txt" };
        CliOptions opt = CliOptions.parse(args);
        assertFalse(opt.statsShort);
        assertFalse(opt.statsFull);
    }

    @Test
    void testParseThrowsOnMissingOutputDir() {
        String[] args = { "-o" };
        assertThrows(IllegalArgumentException.class, () -> CliOptions.parse(args));
    }

    @Test
    void testParseThrowsOnMissingPrefix() {
        String[] args = { "-p" };
        assertThrows(IllegalArgumentException.class, () -> CliOptions.parse(args));
    }

    @Test
    void testParseThrowsOnNoInputFiles() {
        String[] args = { "-a", "-o", "outdir" };
        assertThrows(IllegalArgumentException.class, () -> CliOptions.parse(args));
    }

    @Test
    void testParseMultipleInputs() {
        String[] args = { "input1.txt", "input2.txt", "input3.txt" };
        CliOptions opt = CliOptions.parse(args);
        assertEquals(3, opt.input.size());
        assertEquals(Path.of("input1.txt"), opt.input.get(0));
        assertEquals(Path.of("input2.txt"), opt.input.get(1));
        assertEquals(Path.of("input3.txt"), opt.input.get(2));
    }
}