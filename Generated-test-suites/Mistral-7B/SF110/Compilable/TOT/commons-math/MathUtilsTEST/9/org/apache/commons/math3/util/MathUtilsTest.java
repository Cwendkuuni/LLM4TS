package org.apache.commons.math3.util;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*; // for assertions

public class MathUtilsTest {
    @Test
    public void testHashDouble() {
        double value = 1.2;
        int expectedHash = 12345; // arbitrary value for testing
        int actualHash = MathUtils.hash(value);
        assertEquals(expectedHash, actualHash);
        
        // Testing edge cases
        double negativeValue = -1.2;
        int negativeExpectedHash = -12345; // negative hash for negative number
        int negativeActualHash = MathUtils.hash(negativeValue);
        assertEquals(negativeExpectedHash, negativeActualHash);
        
        double zeroValue = 0.0;
        int zeroExpectedHash = 0;
        int zeroActualHash = MathUtils.hash(zeroValue);
        assertEquals(zeroExpectedHash, zeroActualHash);
    }
}