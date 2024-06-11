import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounter() throws NotStrictlyPositiveException, OutOfRangeException, DimensionMismatchException {
        int[] size = {2, 3, 5};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        // Test basic functionality
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 1, 0));
        // Test edge cases and exceptions
        try {
            counter.getCount(-1, 0, 0);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        try {
            counter.getCount(3, 0, 0);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            counter.getCount(0, -1, 0);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        try {
            counter.getCount(0, 3, 5);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            counter.getCount(0, 3, 6);
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected
        }

        try {
            counter.getCount(-1, 0, 0);
            fail("Expected NegativeArraySizeException");
        } catch (OutOfRangeException e) {
            // Expected exception is OutOfRangeException, but JUnit also throws NegativeArraySizeException
            // We cannot test for NegativeArraySizeException directly, so we test for the exception message
            assertTrue(e.getMessage().contains("Negative array size: -1"));
        }

        try {
            new MultidimensionalCounter(new int[]{-1, 1, 1});
            fail("Expected NegativeArraySizeException");
        } catch (NotStrictlyPositiveException e) {
            // Expected, but JUnit also throws NegativeArraySizeException
            assertTrue(e.getMessage().contains("Negative array size: -1"));
        }

        // Test iterator functionality
        assertEquals(0, iterator.getCount());
        assertFalse(iterator.hasNext());
        iterator.next();
        assertEquals(1, iterator.getCount());
        assertTrue(iterator.hasNext());
        iterator.next();
        assertEquals(2, iterator.getCount());
        assertTrue(iterator.hasNext());
        iterator.next();
        assertEquals(3, iterator.getCount());
        assertFalse(iterator.hasNext());

        // Test resetting the counter
        iterator.next();
        assertEquals(1, counter.getCount(0, 0, 0));
        iterator.next();
        assertEquals(2, counter.getCount(0, 0, 0));
        iterator.next();
        assertEquals(3, counter.getCount(0, 0, 0));
        iterator.next();
        assertEquals(0, counter.getCount(0, 0, 0));
        iterator.next();
        assertEquals(1, counter.getCount(0, 0, 0));

        // Test conversion to unidimensional counter
        int[] counts = counter.getCounts(counter.getSize() - 1);
        assertEquals(0, counter.getCount(counts));
        iterator.next();
        counts = counter.getCounts(counter.getSize() - 1);
        assertEquals(1, counter.getCount(counts));
    }
}