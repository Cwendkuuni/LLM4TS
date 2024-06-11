package org.apache.commons.math3.util;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MathUtilsTest {
    @Test
    public void testHashDouble() {
        double value = 1.23;
        int expected = 12345; // Assuming a reasonable hashCode for the given double
        int actual = MathUtils.hash(value);
        assertEquals(expected, actual);
    }
}