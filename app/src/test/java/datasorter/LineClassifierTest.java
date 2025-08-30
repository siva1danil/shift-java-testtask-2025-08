
package datasorter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LineClassifierTest {
    @Test
    void testNull() {
        assertEquals(LineClassifier.Type.STRING, LineClassifier.classify(null));
    }

    @Test
    void testEmptyString() {
        assertEquals(LineClassifier.Type.STRING, LineClassifier.classify(""));
        assertEquals(LineClassifier.Type.STRING, LineClassifier.classify("   "));
    }

    @Test
    void testIntegerClassification() {
        assertEquals(LineClassifier.Type.INTEGER, LineClassifier.classify("123"));
        assertEquals(LineClassifier.Type.INTEGER, LineClassifier.classify("-456"));
        assertEquals(LineClassifier.Type.INTEGER, LineClassifier.classify("+789"));
        assertEquals(LineClassifier.Type.INTEGER, LineClassifier.classify("  42  "));
    }

    @Test
    void testFloatClassification() {
        assertEquals(LineClassifier.Type.FLOAT, LineClassifier.classify("3.14"));
        assertEquals(LineClassifier.Type.FLOAT, LineClassifier.classify("-0.001"));
        assertEquals(LineClassifier.Type.FLOAT, LineClassifier.classify("+2.718"));
        assertEquals(LineClassifier.Type.FLOAT, LineClassifier.classify("6.022e23"));
        assertEquals(LineClassifier.Type.FLOAT, LineClassifier.classify("-1.6E-19"));
        assertEquals(LineClassifier.Type.FLOAT, LineClassifier.classify(".5"));
        assertEquals(LineClassifier.Type.FLOAT, LineClassifier.classify("5."));
        assertEquals(LineClassifier.Type.FLOAT, LineClassifier.classify("  0.0  "));
    }

    @Test
    void testStringClassification() {
        assertEquals(LineClassifier.Type.STRING, LineClassifier.classify("hello"));
        assertEquals(LineClassifier.Type.STRING, LineClassifier.classify("123abc"));
        assertEquals(LineClassifier.Type.STRING, LineClassifier.classify("3.14.15"));
        assertEquals(LineClassifier.Type.STRING, LineClassifier.classify("e23"));
        assertEquals(LineClassifier.Type.STRING, LineClassifier.classify("1e"));
        assertEquals(LineClassifier.Type.STRING, LineClassifier.classify("1.2e3.4"));
        assertEquals(LineClassifier.Type.STRING, LineClassifier.classify("+-123"));
    }
}
