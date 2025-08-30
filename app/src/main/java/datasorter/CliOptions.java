package datasorter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final class CliOptions {
    boolean append = false; // -a; дополнять файлы, если существуют
    boolean statsShort = false; // -s; показать краткую статистику
    boolean statsFull = false; // -f; показать полную статистику

    Path output = Path.of("."); // -o <путь>; директория для выходных файлов
    String prefix = ""; // -p <префикс>; префикс для имен выходных файлов
    List<Path> input = new ArrayList<>(); // имена входных файлов

    static CliOptions parse(String[] args) throws IllegalArgumentException {
        CliOptions opt = new CliOptions();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-a" -> {
                    opt.append = true;
                }
                case "-s" -> {
                    opt.statsShort = true;
                }
                case "-f" -> {
                    opt.statsFull = true;
                }
                case "-o" -> {
                    if (i + 1 >= args.length)
                        throw new IllegalArgumentException("Не указана директория для опции -o.");
                    opt.output = Path.of(args[++i]);
                }
                case "-p" -> {
                    if (i + 1 >= args.length)
                        throw new IllegalArgumentException("Не указан префикс для опции -p.");
                    opt.prefix = args[++i];
                }
                default -> {
                    opt.input.add(Path.of(arg));
                }
            }
        }

        if (opt.statsFull)
            opt.statsShort = false;

        if (opt.input.isEmpty())
            throw new IllegalArgumentException("Не указаны входные файлы.");

        return opt;
    }

    static void printHelp() {
        System.out.println("Использование: java -jar app.jar [опции] <входные файлы>");
        System.out.println("Опции:");
        System.out.println("  -a              дополнять выходные файлы, если они существуют (вместо перезаписи)");
        System.out.println("  -s              показать краткую статистику по обработанным данным");
        System.out.println("  -f              показать полную статистику по обработанным данным");
        System.out.println("  -o <путь>       директория для выходных файлов (по умолчанию - текущая директория)");
        System.out.println("  -p <префикс>    префикс для имен выходных файлов (по умолчанию - пустой)");
        System.out.println("  <входные файлы> имена входных текстовых файлов для обработки");
    }
}
