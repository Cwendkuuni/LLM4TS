
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.apache.commons.math3.util.MathArrays.*;

import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

    private int dimension;
    private int[] sizes;
    private MultidimensionalCounter multidimensionalCounter;

    @Parameters
    public static Iterable<Object[]> data() {
        return asList(new Object[][] {
                {2, new int[] {2, 2}},
                {3, new int[] {2, 2, 2}},
                {4, new int[] {2, 2, 2, 2}},
                {5, new int[] {2, 2, 2, 2, 2}}
        });
    }

    @Parameters(name = "{index}: dimension={0} sizes={1}")
    public Object[] getParameters() {
        return new Object[] {dimension, sizes};
    }

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        multidimensionalCounter = new MultidimensionalCounter(sizes);
    }

    @Test
    public void testConstructor() {
        // Expert 1's test case
    }

    @Test
    public void testIteratorHasNext() {
        // Expert 1's test case
    }

    @Test
    public void testIteratorNext() {
        // Expert 1's test case
    }

    @Test
    public void testGetDimension() {
        // Expert 1's test case
    }

    @Test
    public void testGetCountsOutOfRange() {
        // Expert 1's test case
    }

    @Test
    public void testGetCountsWithinRange() {
        // Expert 1's test case
    }

    @Test
    public void testGetCountOutOfRange() {
        // Expert 1's test case
    }

    @Test
    public void testGetCountWithinRange() {
        // Expert 1's test case
    }

    @Test
    public void testGetSize() {
        // Expert 1's test case
    }

    @Test
    public void testGetSizes() {
        // Expert 1's test case
    }

    @Test
    public void testToString() {
        // Expert 1's test case
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        // Expert 2's test case
    }

    // Test for edge cases and exceptions

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = {-2, 2};
        try {
            new MultidimensionalCounter(sizes);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegativeIndex() {
        int[] indices = new int[] {-1};
        try {
            multidimensionalCounter.getCounts(indices[0]);
            fail();
        } catch (OutOfRangeException e) {
            // Expected
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimensions() {
        int[] dimensions = new int[] {3};
        try {
            multidimensionalCounter.getCount(dimensions);
            fail();
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBounds() {
        int[] indices = new int[] {dimension - 1, dimension - 1};
        try {
            multidimensionalCounter.getCount(indices);
            fail();
        } catch (OutOfRangeException e) {
            // Expected
        }
    }
}
