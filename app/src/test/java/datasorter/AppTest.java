package datasorter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void testEmptyArgs() {
        int exitCode = (new App()).run(new String[] {});
        assertEquals(1, exitCode);
    }

    @Test
    void testMissingFile() {
        int exitCode = (new App()).run(new String[] { "-o", "outdir", "-p", "prefix", "-a", "nonexistentfile.txt" });
        assertEquals(1, exitCode);
    }

    @Test
    void testOnlyInts() {
        Path dir = null, in = null, intOut = null, floatOut = null, stringOut = null;
        try {
            dir = Files.createTempDirectory("datasorter_test");
            in = dir.resolve("in.txt");
            intOut = dir.resolve("out_integers.txt");
            floatOut = dir.resolve("out_floats.txt");
            stringOut = dir.resolve("out_strings.txt");
            Files.write(in, "1\n-1\n9999".getBytes(StandardCharsets.UTF_8));
            int exitCode = (new App()).run(new String[] { "-o", dir.toString(), "-p", "out_", "-s", in.toString() });
            assertEquals(0, exitCode);

            assertEquals("1\n-1\n9999\n", Files.readString(intOut, StandardCharsets.UTF_8).replaceAll("\r", ""));
            assertFalse(Files.exists(floatOut));
            assertFalse(Files.exists(stringOut));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Files.deleteIfExists(in);
                Files.deleteIfExists(intOut);
                Files.deleteIfExists(floatOut);
                Files.deleteIfExists(stringOut);
                Files.deleteIfExists(dir);
            } catch (Exception cleanupEx) {
                // Игнорируем
            }
        }
    }

    @Test
    void testOnlyFloats() {
        Path dir = null, in = null, intOut = null, floatOut = null, stringOut = null;
        try {
            dir = Files.createTempDirectory("datasorter_test");
            in = dir.resolve("in.txt");
            intOut = dir.resolve("out_integers.txt");
            floatOut = dir.resolve("out_floats.txt");
            stringOut = dir.resolve("out_strings.txt");
            Files.write(in, "1.0\n-1.\n-9999e-16".getBytes(StandardCharsets.UTF_8));

            int exitCode = (new App()).run(new String[] { "-o", dir.toString(), "-p", "out_", "-s", in.toString() });
            assertEquals(0, exitCode);

            assertFalse(Files.exists(intOut));
            assertEquals("1.0\n-1.\n-9999e-16\n",
                    Files.readString(floatOut, StandardCharsets.UTF_8).replaceAll("\r", ""));
            assertFalse(Files.exists(stringOut));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Files.deleteIfExists(in);
                Files.deleteIfExists(intOut);
                Files.deleteIfExists(floatOut);
                Files.deleteIfExists(stringOut);
                Files.deleteIfExists(dir);
            } catch (Exception cleanupEx) {
                // Игнорируем
            }
        }
    }

    @Test
    void testOnlyStrings() {
        Path dir = null, in = null, intOut = null, floatOut = null, stringOut = null;
        try {
            dir = Files.createTempDirectory("datasorter_test");
            in = dir.resolve("in.txt");
            intOut = dir.resolve("out_integers.txt");
            floatOut = dir.resolve("out_floats.txt");
            stringOut = dir.resolve("out_strings.txt");
            Files.write(in, ".\n1.2.3\na\ntext".getBytes(StandardCharsets.UTF_8));

            int exitCode = (new App()).run(new String[] { "-o", dir.toString(), "-p", "out_", "-s", in.toString() });
            assertEquals(0, exitCode);

            assertFalse(Files.exists(intOut));
            assertFalse(Files.exists(floatOut));
            assertEquals(".\n1.2.3\na\ntext\n",
                    Files.readString(stringOut, StandardCharsets.UTF_8).replaceAll("\r", ""));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Files.deleteIfExists(in);
                Files.deleteIfExists(intOut);
                Files.deleteIfExists(floatOut);
                Files.deleteIfExists(stringOut);
                Files.deleteIfExists(dir);
            } catch (Exception cleanupEx) {
                // Игнорируем
            }
        }
    }

    @Test
    void testMultipleCategories() {
        Path dir = null, in = null, intOut = null, floatOut = null, stringOut = null;
        try {
            dir = Files.createTempDirectory("datasorter_test");
            in = dir.resolve("in.txt");
            intOut = dir.resolve("out_integers.txt");
            floatOut = dir.resolve("out_floats.txt");
            stringOut = dir.resolve("out_strings.txt");
            Files.write(in, "1\n1.0\nabc".getBytes(StandardCharsets.UTF_8));

            int exitCode = (new App()).run(new String[] { "-o", dir.toString(), "-p", "out_", "-f", in.toString() });
            assertEquals(0, exitCode);

            assertEquals("1\n", Files.readString(intOut, StandardCharsets.UTF_8).replaceAll("\r", ""));
            assertEquals("1.0\n", Files.readString(floatOut, StandardCharsets.UTF_8).replaceAll("\r", ""));
            assertEquals("abc\n", Files.readString(stringOut, StandardCharsets.UTF_8).replaceAll("\r", ""));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Files.deleteIfExists(in);
                Files.deleteIfExists(intOut);
                Files.deleteIfExists(floatOut);
                Files.deleteIfExists(stringOut);
                Files.deleteIfExists(dir);
            } catch (Exception cleanupEx) {
                // Игнорируем
            }
        }
    }
}
