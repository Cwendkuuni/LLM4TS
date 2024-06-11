import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest {

    private final int[] size;
    private MultidimensionalCounter counter;

    public MultidimensionalCounterTest(int... size) {
        this.size = size;
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{new int[]{2, 2}},
                new Object[]{new int[]{3, 3, 3}},
                new Object[]{new int[]{1, 1, 1, 1}},
        });
    }

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testConstructor() throws NotStrictlyPositiveException {
        Assert.assertEquals(counter.getDimension(), size.length);
    }

    @Test
    public void testGetDimension() {
        Assert.assertEquals(counter.getDimension(), size.length);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizeThrowsException() {
        new MultidimensionalCounter(new int[]{-1, 2});
    }

    @Test
    public void testGetSizes() {
        Assert.assertArrayEquals(counter.getSizes(), size);
    }

    @Test
    public void testGetSize() {
        Assert.assertEquals(counter.getSize(), getTotalSize());
    }

    @Test
    public void testToString() {
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < size.length; ++i) {
            expected.append("[").append(counter.getCount(i)).append("]");
        }
        Assert.assertEquals(counter.toString(), expected.toString());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndexThrowsException() {
        counter.getCount(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBoundIndexThrowsException() {
        counter.getCount(size.length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWithDifferentDimensionThrowsException() {
        int[] wrongSize = new int[]{3, 3, 2};
        counter = new MultidimensionalCounter(size);
        counter.getCount(wrongSize);
    }

    @Test
    public void testGetCount() {
        int[] index = new int[size.length];
        for (int i = 0; i < size.length; ++i) {
            index[i] = counter.getCount(i);
        }
        Assert.assertArrayEquals(index, counter.getCounts());
    }

    @Test
    public void testGetCounts() {
        Assert.assertArrayEquals(counter.getCounts(), new int[0]);
    }

    @Test
    public void testIteratorHasNext() {
        int[] expected = new int[size.length];
        Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        for (int i = 0; i < size.length; ++i) {
            int next = iterator.next();
            expected[i] = next;
            Assert.assertTrue(iterator.hasNext());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCount() {
        Iterator iterator = counter.iterator();
        Assert.assertEquals(iterator.getCount(), -1);
        for (int i = 0; i < size.length; ++i) {
            iterator.next();
            Assert.assertEquals(iterator.getCount(), i);
        }
        Assert.assertEquals(iterator.getCount(), size.length - 1);
    }

    @Test
    public void testIteratorGetCounts() {
        Iterator iterator = counter.iterator();
        Assert.assertArrayEquals(iterator.getCounts(), new int[0]);
        for (int i = 0; i < size.length; ++i) {
            iterator.next();
            int[] counts = iterator.getCounts();
            Assert.assertEquals(counts.length, size.length);
            Assert.assertEquals(counts[i], i);
        }
        int[] counts = iterator.getCounts();
        Assert.assertEquals(counts.length, size.length);
        Assert.assertEquals(counts[size.length - 1], size.length - 1);
    }

    private int getTotalSize() {
        int totalSize = 1;
        for (int i = 0; i < size.length; ++i) {
            totalSize *= size[i];
        }
        return totalSize;
    }

}