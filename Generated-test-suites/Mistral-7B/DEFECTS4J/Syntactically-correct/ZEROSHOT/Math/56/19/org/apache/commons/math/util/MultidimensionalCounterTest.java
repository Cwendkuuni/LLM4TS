import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        assertEquals(3, mdCounter.getDimension());
        assertEquals(2 * 4 * 3, mdCounter.getSize());
        assertEquals(sizes, mdCounter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterNegativeSizesThrowsException() {
        int[] sizes = new int[]{-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testMultidimensionalCounterIteration() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        Iterator it = mdCounter.iterator();

        assertTrue(it.hasNext());
        Integer next = it.next();
        assertEquals(0, next.intValue());
        int[] counts = it.getCounts();
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        assertTrue(it.hasNext());
        next = it.next();
        assertEquals(1, next.intValue());
        counts = it.getCounts();
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(1, counts[2]);

        for (int i = 0; i < 2 * 4 * 3; i++) {
            next = it.next();
            counts = it.getCounts();
            assertNotEquals(i, next.intValue());
            for (int j = 0; j < 3; j++) {
                assertEquals(i / (2 * 4) % 2, counts[j]);
            }
        }
        assertFalse(it.hasNext());
    }

    @Test(expected = OutOfRangeException.class)
    public void testMultidimensionalCounterOutOfRangeException() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);
        Iterator it = mdCounter.iterator();

        it.next();
        it.next();

        int outOfRangeIndex = 2 * 4 * 3 + 1;
        it.next(); // should throw OutOfRangeException
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultidimensionalCounterDimensionMismatchException() {
        int[] sizes = new int[]{2, 4};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes, 3);
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter mdCounter = new MultidimensionalCounter(sizes);

        int[] counts = new int[]{0, 0, 0};

        for (int i = 0; i < 2 * 4 * 3; i++) {
            counts = mdCounter.getCounts(mdCounter.getCount(counts));
        }

        for (int i = 0; i < 3; i++) {
            assertEquals(2 * 4, counts[i]);
        }
    }
}