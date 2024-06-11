import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertEquals(sizes.length, mdc.getSizes().length);
        for (int i = 0; i < sizes.length; i++) {
            assertEquals(sizes[i], mdc.getSizes()[i]);
        }
        assertEquals(MathUtils.mult(sizes), mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = new int[]{-3, 4, 5};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSizes() {
        int[] sizes = new int[]{0, 4, 5};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIteratorBasicUseCase() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        int count = 0;
        while (it.hasNext()) {
            Integer next = it.next();
            assertEquals(next, count);
            count++;
        }
    }

    @Test
    public void testGetCountsBasicUseCase() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        int[] counter = it.next().getCounts();
        assertEquals(counter.length, mdc.getDimension());
        for (int i = 0; i < counter.length; i++) {
            assertEquals(counter[i], it.getCount(i));
        }
    }

    @Test
    public void testGetCountOutOfBounds() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        try {
            it.getCount(-1);
            fail();
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testGetCountsDimensionMismatch() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        try {
            it.getCounts(sizes.length);
            fail();
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testGetCountsDimensionNegative() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        try {
            it.getCounts(-1);
            fail();
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testGetCountsDimensionOutOfBounds() {
        int[] sizes = new int[]{3, 4, 5};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator it = mdc.iterator();
        try {
            it.getCounts(sizes.length + 1);
            fail();
        } catch (OutOfRangeException e) {
            // expected
        }
    }
}