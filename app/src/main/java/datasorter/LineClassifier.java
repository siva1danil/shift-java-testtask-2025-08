package datasorter;

final class LineClassifier {
    private static final String REGEX_INT = "^[+-]?\\d+$";
    private static final String REGEX_FLOAT = "^[+-]?(?:\\d+\\.\\d*|\\.\\d+|\\d+)(?:[eE][+-]?\\d+)?$";

    enum Type {
        INTEGER, FLOAT, STRING
    }

    static Type classify(String line) {
        if (line == null)
            return Type.STRING;
        line = line.trim();

        if (line.isEmpty())
            return Type.STRING;
        else if (line.matches(REGEX_INT))
            return Type.INTEGER;
        else if (line.matches(REGEX_FLOAT))
            return Type.FLOAT;
        return Type.STRING;
    }
}
