package datasorter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class App {
    public static void main(String[] args) {
        System.exit(new App().run(args));
    }

    public int run(String[] args) {
        // Парсим аргументы командной строки
        CliOptions opt;
        try {
            opt = CliOptions.parse(args);
        } catch (IllegalArgumentException ex) {
            System.err.println("Ошибка: " + ex.getMessage());
            CliOptions.printHelp();
            return 1;
        }

        // Необходимые переменные
        OpenOption[] opts = opt.append
                ? new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.APPEND }
                : new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE };

        Path intPath = opt.output.resolve(opt.prefix + "integers.txt");
        Path floatPath = opt.output.resolve(opt.prefix + "floats.txt");
        Path stringPath = opt.output.resolve(opt.prefix + "strings.txt");

        BufferedWriter intWriter = null, floatWriter = null, stringWriter = null;

        StatsNumeric statsInt = new StatsNumeric(), statsFloat = new StatsNumeric();
        StatsString statsString = new StatsString();

        int errors = 0;

        // Основной цикл
        for (Path file : opt.input) {
            try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                for (String line; (line = reader.readLine()) != null;) {
                    LineClassifier.Type type = LineClassifier.classify(line);
                    try {
                        switch (type) {
                            case INTEGER -> {
                                statsInt.add(new BigDecimal(line));
                                intWriter = getOrCreateWriter(intWriter, intPath, opts);
                                intWriter.write(line);
                                intWriter.newLine();
                            }
                            case FLOAT -> {
                                statsFloat.add(new BigDecimal(line));
                                floatWriter = getOrCreateWriter(floatWriter, floatPath, opts);
                                floatWriter.write(line);
                                floatWriter.newLine();
                            }
                            case STRING -> {
                                statsString.add(line);
                                stringWriter = getOrCreateWriter(stringWriter, stringPath, opts);
                                stringWriter.write(line);
                                stringWriter.newLine();
                            }
                        }
                    } catch (NumberFormatException ex) {
                        System.err.println(file + ": " + line
                                + ": ошибка преобразования строки в число, сохраняю в strings: "
                                + ex.getMessage());
                        errors++;

                        statsString.add(line);
                        stringWriter = getOrCreateWriter(stringWriter, stringPath, opts);
                        stringWriter.write(line);
                        stringWriter.newLine();
                    }
                }
                reader.close();
            } catch (Exception ex) {
                printError(ex, file.toString());
                errors++;
            }
        }

        // Завершение
        System.out.println(errors > 0 ? "Завершено с ошибками." : "Завершено успешно.");
        closeAll(intWriter, floatWriter, stringWriter);
        printStats(opt, statsInt, statsFloat, statsString);
        return errors > 0 ? 1 : 0;
    }

    private static BufferedWriter getOrCreateWriter(BufferedWriter writer, Path path, OpenOption... opts)
            throws Exception {
        if (writer == null)
            writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, opts);
        return writer;
    }

    private static void closeAll(Closeable... resources) {
        for (Closeable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception ex) {
                    // Игнорируем
                }
            }
        }
    }

    private static void printStats(CliOptions opt, StatsNumeric ints, StatsNumeric floats, StatsString strings) {
        System.out.println();
        if (opt.statsShort) {
            System.out.println("Целые числа:        " + ints.toShortString());
            System.out.println("Вещественные числа: " + floats.toShortString());
            System.out.println("Строки:             " + strings.toShortString());
        } else if (opt.statsFull) {
            System.out.println("Целые числа:        " + ints.toFullString());
            System.out.println("Вещественные числа: " + floats.toFullString());
            System.out.println("Строки:             " + strings.toFullString());
        }
        System.out.println();
    }

    private static void printError(Exception ex, String context) {
        String msg;
        if (ex instanceof NoSuchFileException) {
            msg = "файл не найден";
        } else if (ex instanceof AccessDeniedException) {
            msg = "нет доступа к файлу";
        } else if (ex instanceof IOException) {
            String detail = ex.getMessage();
            if (detail == null || detail.isBlank()) {
                msg = "ошибка ввода-вывода";
            } else {
                msg = "ошибка чтения (" + detail + ")";
            }
        } else {
            msg = ex.getClass().getSimpleName() + (ex.getMessage() != null ? " (" + ex.getMessage() + ")" : "");
        }
        System.err.println(context + ": " + msg);
    }
}