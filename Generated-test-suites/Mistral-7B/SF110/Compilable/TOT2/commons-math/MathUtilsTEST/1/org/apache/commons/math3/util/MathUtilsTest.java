package org.apache.commons.math3.util;
import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void testHashDouble() {
        double value = 3.14;
        int expected = 1234567; // replace with actual hashcode
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }

    @Test
    public void testHashDoubleArray() {
        double[] values = {1.2, 2.3, 3.4};
        int expected = 1234567; // replace with actual hashcode
        int actual = MathUtils.hash(values);
        assertEquals(expected, actual);
    }

    // test other methods similarly...
}