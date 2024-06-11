package org.example;

import org.junit.Test;
import static org.junit.Assert.*;

public class FieldUtilsTest {

    @Test
    public void testSafeNegate() {
        int value = 5;
        int result = FieldUtils.safeNegate(value);
        int expected = -5;
        assertEquals(expected, result);
    }

    @Test(expected = ArithmeticException.class)
    public void testSafeNegateMinValue() {
        int value = Integer.MIN_VALUE;
        FieldUtils.safeNegate(value);
    }

    // Test other methods in the class with similar test cases, edge cases, and exception handling scenarios
}