import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import java.util.Arrays;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterValidConstruction() {
        // Test valid construction
        int[] validSizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(validSizes);
        int expectedDimension = 2;
        int expectedTotalSize = 6;
        assertEquals(expectedDimension, counter.getDimension());
        assertEquals(expectedTotalSize, counter.getSize());
    }

    // Add other basic test cases here

    @Test
    public void testMultidimensionalCounterEmptyConstruction() {
        // Test empty array construction
        int[] emptySizes = {};
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(emptySizes);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Test passed
        }
    }

    // Add other edge case and exception handling test cases here

    @Test
    public void testMultidimensionalCounterSingleDimensionalConstruction() {
        // Test single-dimensional array construction
        int[] singleDimensionalSizes = {5};
        MultidimensionalCounter counter = new MultidimensionalCounter(singleDimensionalSizes);
        int expectedDimension = 1;
        int expectedTotalSize = 5;
        assertEquals(expectedDimension, counter.getDimension());
        assertEquals(expectedTotalSize, counter.getSize());
    }

    // Add other test cases here

    @Test
    public void testNotStrictlyPositiveException() {
        // Test NotStrictlyPositiveException
        try {
            int[] negativeSizes = {-1};
            MultidimensionalCounter counter = new MultidimensionalCounter(negativeSizes);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Test passed
        }
    }

    // Add other test cases here

    @Test
    public void testDimensionMismatchException() {
        // Test DimensionMismatchException
        int[] validSizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(validSizes);
        int[] invalidDimensions = {4, 3};
        try {
            int[] counts = counter.getCounts(10);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Test passed
        }
    }

    // Add other test cases here

    @Test
    public void testOutOfRangeException() {
        // Test OutOfRangeException
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3});
        try {
            int[] counts = counter.getCounts(100);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Test passed
        }
    }

    @Test
    public void testToString() {
        // Test toString()
        int[] validSizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(validSizes);
        String expectedString = "[2][3]";
        assertEquals(expectedString, counter.toString());
    }

    // Add other test cases here
}