import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Assert.assertEquals(2 * 3 * 4, mdc.getSize());
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        Assert.assertEquals(3, mdc.getDimension());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        int[] expected = {0, 0, 0};
        int[] actual = mdc.getCounts(0);
        Assert.assertArrayEquals(expected, actual);

        expected = new int[]{1, 0, 0};
        actual = mdc.getCounts(1);
        Assert.assertArrayEquals(expected, actual);

        expected = new int[]{1, 1, 0};
        actual = mdc.getCounts(2);
        Assert.assertArrayEquals(expected, actual);

        expected = new int[]{1, 1, 1};
        actual = mdc.getCounts(3);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfBounds() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        mdc.getCounts(4);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        Assert.assertEquals(0, mdc.getCount(0, 0, 0));
        Assert.assertEquals(1, mdc.getCount(0, 0, 1));
        Assert.assertEquals(2, mdc.getCount(0, 0, 2));
        Assert.assertEquals(3, mdc.getCount(0, 1, 0));
        //... and so on

        Assert.assertEquals(2 * 3 * 4 - 1, mdc.getCount(2, 2, 2));
        Assert.assertEquals(2 * 3 * 4 - 1, mdc.getCount(1, 2, 2));
        Assert.assertEquals(2 * 3 * 4 - 1, mdc.getCount(1, 1, 2));
        Assert.assertEquals(2 * 3 * 4 - 1, mdc.getCount(1, 0, 2));
        Assert.assertEquals(2 * 3 * 4 - 1, mdc.getCount(0, 2, 2));
        Assert.assertEquals(2 * 3 * 4 - 1, mdc.getCount(0, 1, 2));
        Assert.assertEquals(2 * 3 * 4 - 1, mdc.getCount(0, 0, 2));

        Assert.assertEquals(2 * 3 * 4 - 1, mdc.getCount(2, 2, 3));
        Assert.assertEquals(2 * 3 * 4 * 2 - 1, mdc.getCount(1, 2, 2));
        //... and so on

        Assert.assertEquals(2 * 3 * 4 * 2 * 3 * 4 - 1, mdc.getCount(0, 2, 2, 2, 2, 2));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBounds() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        mdc.getCount(-1, 1, 1);
        mdc.getCount(1, -1, 1);
        mdc.getCount(1, 1, -1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        mdc.getCount(new int[]{5});
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        MultidimensionalCounter.Iterator it = mdc.iterator();
        int expected = 0;
        while (it.hasNext()) {
            int actual = it.next();
            Assert.assertEquals(expected++, actual);
        }
    }
}