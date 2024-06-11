import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Assert.assertEquals(3, mdc.getDimension());
        Assert.assertArrayEquals(sizes, mdc.getSizes());
        Assert.assertEquals(32, mdc.getTotalSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() throws NotStrictlyPositiveException {
        int[] sizes = {-1, 2, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        Assert.assertEquals(3, mdc.getDimension());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        mdc.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfTotalSize() throws OutOfRangeException {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        mdc.getCounts(mdc.getTotalSize());
    }

    @Test
    public void testGetCountsNormal() throws OutOfRangeException {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        int[] expected = new int[]{0, 0, 0};
        int[] counts = mdc.getCounts(10);
        Assert.assertArrayEquals(expected, counts);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithWrongDimension() throws DimensionMismatchException {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        mdc.getCounts(new int[]{1, 2, 5});
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        int[] expected = new int[]{1, 2, 3};
        int[] c = new int[]{1, 2, 3};
        int count = mdc.getCount(c);
        Assert.assertEquals(count, 9);
        Assert.assertArrayEquals(expected, c);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() throws OutOfRangeException {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        mdc.getCount(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBounds() throws OutOfRangeException {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        mdc.getCount(new int[]{2, 3, 5});
    }

    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        Iterator it = mdc.iterator();
        List<Integer> expected = new ArrayList<>();
        int i = 0;
        while (it.hasNext()) {
            expected.add(it.next());
            i++;
        }
        Assert.assertEquals(13 * 13 * 13, i);
        Assert.assertEquals(expected, Arrays.asList(Collections.nCopies(13 * 13 * 13, 0)));
    }

    @Test
    public void testIteratorNext() {
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 3, 4});
        Iterator it = mdc.iterator();
        Assert.assertEquals(0, it.next());
        Assert.assertEquals(0, it.getCount());
        Assert.assertEquals(1, it.next());
        Assert.assertEquals(1, it.getCount());
        Assert.assertEquals(2, it.next());
        Assert.assertEquals(2, it.getCount());
        Assert.assertEquals(3, it.next());
        Assert.assertEquals(3, it.getCount());
        Assert.assertFalse(it.hasNext());
    }

}