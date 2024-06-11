package org.apache.commons.math3.util;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MathUtilsTest {
    // Test case 1: test hash for single double
    @Test
    public void testHashSingleDouble() {
        double value = 1.2;
        int expectedHash = 12345; // any valid hash code can be used here
        int actualHash = MathUtils.hash(value);
        assertEquals("Incorrect hash code for single double", expectedHash, actualHash);
    }

    // Test case 2: test hash for an array of doubles (empty)
    @Test
    public void testHashEmptyDoubleArray() {
        double[] values = {};
        int expectedHash = 0;
        int actualHash = MathUtils.hash(values);
        assertEquals("Incorrect hash code for empty double array", expectedHash, actualHash);
    }

    // Test case 3: test hash for an array of doubles (single element)
    @Test
    public void testHashSingleElementDoubleArray() {
        double[] values = {1.2};
        int expectedHash = 12345; // any valid hash code can be used here
        int actualHash = MathUtils.hash(values);
        assertEquals("Incorrect hash code for single element double array", expectedHash, actualHash);
    }

    // Test case 4: test hash for an array of doubles (multiple elements)
    @Test
    public void testHashMultipleElementDoubleArray() {
        double[] values = {1.1, 2.2, 3.3};
        int expectedHash = 45678; // any valid hash code can be used here
        int actualHash = MathUtils.hash(values);
        assertEquals("Incorrect hash code for multiple element double array", expectedHash, actualHash);
    }
}