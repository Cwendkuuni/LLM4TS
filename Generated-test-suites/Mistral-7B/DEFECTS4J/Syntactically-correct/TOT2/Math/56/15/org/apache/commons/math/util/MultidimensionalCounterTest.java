import org.apache.commons.math.util.MultidimensionalCounter;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorValidSize() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Assert.assertEquals(2 * 4 * 3, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        int[] size = new int[]{-1, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            Assert.assertTrue(iterator.hasNext());
            iterator.next();
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int expectedCount = -1;
        int[] expectedCounts = new int[size.length];
        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            int actualCount = iterator.next();
            Assert.assertEquals(expectedCount + 1, actualCount);
            MathUtils.copy(iterator.getCounts(), expectedCounts);
            for (int j = 0; j < size.length; j++) {
                Assert.assertEquals(expectedCounts[j] + 1, iterator.getCount(j));
            }
            expectedCount = actualCount;
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIteratorGetCountNegativeIndex() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        iterator.getCount(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIteratorGetCountOutOfRange() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.getCount(2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(size[0] * size[1] * size[2]);
    }

    @Test
    public void testGetCountsValidIndex() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expectedCounts = new int[]{1, 1, 1};
        int[] actualCounts = counter.getCounts(counter.getSize() - 1);
        MathUtils.assertEquals(expectedCounts, actualCounts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(counter.getSize());
    }

    @Test
    public void testToString() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        String expectedString = "[1][1][1]";
        String actualString = counter.toString();
        Assert.assertEquals(expectedString, actualString);

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            counter.next();
            expectedString = "[2][2][2]";
            actualString = counter.toString();
            Assert.assertEquals(expectedString, actualString);
        }
    }
}