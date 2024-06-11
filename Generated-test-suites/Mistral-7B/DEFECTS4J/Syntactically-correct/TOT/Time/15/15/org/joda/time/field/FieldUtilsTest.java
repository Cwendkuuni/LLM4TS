package org.joda.time.field.util; // make sure the package is correct for imports in the test file

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.IllegalFieldValueException;

/**
 * Test class for the FieldUtils class.
 */
public class FieldUtilsTest {

    /**
     * Tests the safeNegate method.
     */
    @org.junit.Test
    public void testSafeNegate() {
        // Positive test: int
        int input = 5;
        int expectedOutput = -input;
        int output = FieldUtils.safeNegate(input);
        assertEquals(expectedOutput, output);

        // Positive test: long
        long inputLong = 10;
        long expectedOutputLong = -inputLong;
        long outputLong = FieldUtils.safeNegate((int) inputLong);
        assertEquals(expectedOutputLong, outputLong);
        outputLong = FieldUtils.safeNegate(inputLong);
        assertEquals(expectedOutputLong, outputLong);

        // Negative test: int
        int inputNegative = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(inputNegative));

        // Negative test: long
        long inputNegativeLong = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeNegate(inputNegativeLong));
    }

    /**
     * Tests the safeAdd method.
     */
    @org.junit.Test
    public void testSafeAdd() {
        // Positive test: int
        int a = 3;
        int b = 4;
        int sumExpected = a + b;
        int sum = FieldUtils.safeAdd(a, b);
        assertEquals(sumExpected, sum);

        // Positive test: long
        long aLong = 10;
        long bLong = 5;
        long sumExpectedLong = aLong + bLong;
        long sumLong = FieldUtils.safeAdd(aLong, bLong);
        assertEquals(sumExpectedLong, sumLong);

        // Test overflow
        int input = Integer.MIN_VALUE;
        int addend = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(input, addend));

        // Test underflow
        int input2 = Integer.MAX_VALUE;
        int addend2 = Integer.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeAdd(input2, addend2));
    }

    /**
     * Tests the safeSubtract method.
     */
    @org.junit.Test
    public void testSafeSubtract() {
        // Positive test: int
        int a = 10;
        int b = 3;
        int differenceExpected = a - b;
        int difference = FieldUtils.safeSubtract(a, b);
        assertEquals(differenceExpected, difference);

        // Positive test: long
        long aLong = 10;
        long bLong = 3;
        long differenceExpectedLong = aLong - bLong;
        long differenceLong = FieldUtils.safeSubtract(aLong, bLong);
        assertEquals(differenceExpectedLong, differenceLong);

        // Test overflow
        long input = Long.MIN_VALUE;
        long subtrahend = Long.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(input, subtrahend));

        // Test underflow
        long input2 = Long.MAX_VALUE;
        long subtrahend2 = Long.MIN_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeSubtract(input2, subtrahend2));
    }

    /**
     * Tests the safeMultiply method.
     */
    @org.junit.Test
    public void testSafeMultiply() {
        // Positive test: int
        int a = 3;
        int b = 4;
        int productExpected = a * b;
        int product = FieldUtils.safeMultiply(a, b);
        assertEquals(productExpected, product);

        // Positive test: long
        long aLong = 10;
        int bLong = 5;
        long productExpectedLong = aLong * bLong;
        long productLong = FieldUtils.safeMultiply(aLong, bLong);
        assertEquals(productExpectedLong, productLong);

        // Test overflow for int
        int input = Integer.MIN_VALUE;
        int multiplicand = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(input, multiplicand));

        // Test overflow for long
        long inputLong = Long.MIN_VALUE;
        long multiplicandLong = Long.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(inputLong, multiplicandLong));

        // Test division by zero
        int input2 = 1;
        int divisor = 0;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(input2, divisor));
    }

    /**
     * Tests the safeMultiply method for long with int.
     */
    @org.junit.Test
    public void testSafeMultiplyLongWithInt() {
        // Test positive
        long a = 5;
        int b = 3;
        long productExpected = a * b;
        long product = FieldUtils.safeMultiply(a, b);
        assertEquals(productExpected, product);

        // Test negative
        long aNegative = -5;
        int bNegative = -3;
        long productExpectedNegative = aNegative * bNegative;
        long productNegative = FieldUtils.safeMultiply(aNegative, bNegative);
        assertEquals(productExpectedNegative, productNegative);

        // Test zero
        long aZero = 0;
        int bZero = 0;
        long productExpectedZero = aZero * bZero;
        long productZero = FieldUtils.safeMultiply(aZero, bZero);
        assertEquals(productExpectedZero, productZero);

        // Test overflow
        long aOverflow = Long.MIN_VALUE;
        int bOverflow = Integer.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(aOverflow, bOverflow));
    }

    /**
     * Tests the safeMultiply method for long with long.
     */
    @org.junit.Test
    public void testSafeMultiplyLongWithLong() {
        // Test positive
        long a = 5;
        long b = 3;
        long productExpected = a * b;
        long product = FieldUtils.safeMultiply(a, b);
        assertEquals(productExpected, product);

        // Test negative
        long aNegative = -5;
        long bNegative = -3;
        long productExpectedNegative = aNegative * bNegative;
        long productNegative = FieldUtils.safeMultiply(aNegative, bNegative);
        assertEquals(productExpectedNegative, productNegative);

        // Test zero
        long aZero = 0;
        long bZero = 0;
        long productExpectedZero = aZero * bZero;
        long productZero = FieldUtils.safeMultiply(aZero, bZero);
        assertEquals(productExpectedZero, productZero);

        // Test overflow
        long aOverflow = Long.MIN_VALUE;
        long bOverflow = Long.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiply(aOverflow, bOverflow));
    }

    /**
     * Tests the safeToInt method.
     */
    @org.junit.Test
    public void testSafeToInt() {
        // Test positive
        long input = 5;
        int expectedOutput = (int) input;
        int output = FieldUtils.safeToInt((long) input);
        assertEquals(expectedOutput, output);

        // Test negative
        long inputNegative = -5;
        int expectedOutputNegative = (int) inputNegative;
        int outputNegative = FieldUtils.safeToInt(inputNegative);
        assertEquals(expectedOutputNegative, outputNegative);

        // Test overflow
        long inputOverflow = Long.MIN_VALUE;
        int expectedOutputOverflow = Integer.MIN_VALUE;
        int outputOverflow = FieldUtils.safeToInt(inputOverflow);
        assertEquals(expectedOutputOverflow, outputOverflow);

        long inputOverflow2 = Long.MAX_VALUE;
        int expectedOutputOverflow2 = Integer.MAX_VALUE;
        int outputOverflow2 = FieldUtils.safeToInt(inputOverflow2);
        assertEquals(expectedOutputOverflow2, outputOverflow2);
    }

    /**
     * Tests the safeMultiplyToInt method.
     */
    @org.junit.Test
    public void testSafeMultiplyToInt() {
        // Test positive
        long a = 5;
        long b = 3;
        int productExpected = (int) (a * b);
        int product = FieldUtils.safeMultiplyToInt(a, b);
        assertEquals(productExpected, product);

        // Test negative
        long aNegative = -5;
        long bNegative = -3;
        int productExpectedNegative = (int) (aNegative * bNegative);
        int productNegative = FieldUtils.safeMultiplyToInt(aNegative, bNegative);
        assertEquals(productExpectedNegative, productNegative);

        // Test overflow
        long aOverflow = Long.MIN_VALUE;
        long bOverflow = Long.MAX_VALUE;
        assertThrows(ArithmeticException.class, () -> FieldUtils.safeMultiplyToInt(aOverflow, bOverflow));
    }

    /**
     * Tests the verifyValueBounds method.
     */
    @org.junit.Test
    public void testVerifyValueBounds() {
        // Test int bounds with DateTimeField
        DateTimeField field = new DateTimeField(DateTimeFieldType.dayOfMonth(), 2018, 1, 1);
        int value = 31;
        int lowerBound = 1;
        int upperBound = 31;

        FieldUtils.verifyValueBounds(field, value, lowerBound, upperBound);

        // Test int bounds with DateTimeFieldType
        DateTimeFieldType fieldType = DateTimeFieldType.dayOfMonth();
        int value2 = 31;
        int lowerBound2 = 1;
        int upperBound2 = 31;

        FieldUtils.verifyValueBounds(fieldType, value2, lowerBound2, upperBound2);

        // Test string bounds
        String fieldName = "dayOfWeek";
        int value3 = 7;
        int lowerBound3 = 1;
        int upperBound3 = 7;

        FieldUtils.verifyValueBounds(fieldName, value3, lowerBound3, upperBound3);

        // Test int bounds with wrong bounds
        int lowerBound4 = 32;
        int upperBound4 = 31;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(field, value, lowerBound4, upperBound4));

        // Test int bounds with wrong bounds for DateTimeFieldType
        int lowerBound5 = 32;
        int upperBound5 = 31;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldType, value, lowerBound5, upperBound5));

        // Test int bounds with wrong bounds for String
        String fieldName2 = "dayOfWeek";
        int lowerBound6 = 32;
        int upperBound6 = 31;
        assertThrows(IllegalArgumentException.class, () -> FieldUtils.verifyValueBounds(fieldName2, value, lowerBound6, upperBound6));
    }

    /**
     * Tests the getWrappedValue method.
     */
    @org.junit.Test
    public void testGetWrappedValue() {
        // Test int
        int value = 6;
        int minValue = 1;
        int maxValue = 5;

        int wrappedValue = FieldUtils.getWrappedValue(value, 1, minValue, maxValue);
        assertEquals(value, wrappedValue);

        int wrappedValueMin = FieldUtils.getWrappedValue(minValue - 1, 1, minValue, maxValue);
        assertEquals(minValue, wrappedValueMin);

        int wrappedValueMax = FieldUtils.getWrappedValue(maxValue + 1, 1, minValue, maxValue);
        assertEquals(minValue, wrappedValueMax);

        int wrappedValue2 = FieldUtils.getWrappedValue(minValue - 1, -1, minValue, maxValue);
        assertEquals(maxValue, wrappedValue2);

        // Test long
        long valueLong = 11;
        long minValueLong = 1;
        long maxValueLong = 5;

        long wrappedValueLong = FieldUtils.getWrappedValue((int) valueLong, 1, (int) minValueLong, (int) maxValueLong);
        assertEquals((int) valueLong, wrappedValueLong);

        long wrappedValueMinLong = FieldUtils.getWrappedValue((int) (minValueLong - 1), 1, (int) minValueLong, (int) maxValueLong);
        assertEquals((int) minValueLong, wrappedValueMinLong);

        long wrappedValueMaxLong = FieldUtils.getWrappedValue((int) (maxValueLong + 1), 1, (int) minValueLong, (int) maxValueLong);
        assertEquals((int) minValueLong, wrappedValueMaxLong);

        long wrappedValue3Long = FieldUtils.getWrappedValue((int) (minValueLong - 1), -1, (int) minValueLong, (int) maxValueLong);
        assertEquals((int) maxValueLong, wrappedValue3Long);
    }
}