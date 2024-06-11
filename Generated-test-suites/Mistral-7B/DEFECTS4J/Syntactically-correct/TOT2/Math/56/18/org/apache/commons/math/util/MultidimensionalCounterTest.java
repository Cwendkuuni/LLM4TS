import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Assert.assertEquals(6 * 4 * 3 * 2, counter.getSize());
        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(size, counter.getSizes());
    }

    @Test
    public void testIteratorBasicUsage() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        IntStream.iterate(0, i -> iterator.hasNext() ? iterator.next() : iterator.next())
                .forEachOrdered(index -> Assert.assertEquals(expected.get(index), index));
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        int[] multidimensionalCount = new int[3];
        int unidimensionalCount = 0;

        while (iterator.hasNext()) {
            multidimensionalCount = iterator.getCounts();
            int[] expected = new int[]{iterator.getCount(), iterator.getCount(1), iterator.getCount(2)};
            Assert.assertArrayEquals(expected, multidimensionalCount);
            unidimensionalCount = iterator.getCount();
        }

        int[] lastMultiDimensionalCount = iterator.getCounts();
        int lastUnidimensionalCount = iterator.getCount();

        Assert.assertArrayEquals(new int[]{size[0], size[1], size[2]}, lastMultiDimensionalCount);
        Assert.assertEquals(totalSize - 1, lastUnidimensionalCount);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetCountNegativeIndexThrowsException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimThrowsException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(5);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeIndexThrowsException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(totalSize);
    }

    @Test
    public void testGetCountInvalidDimensionSize() {
        int[] size = {2, 3, -1};
        try {
            new MultidimensionalCounter(size);
            Assert.fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testGetCountValidIndex() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Assert.assertEquals(0, counter.getCount());
    }

    @Test
    public void testGetCountLastIndex() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int lastUnidimensionalCount = 0;

        while (iterator.hasNext()) {
            lastUnidimensionalCount = iterator.getCount();
        }

        Assert.assertEquals(totalSize - 1, lastUnidimensionalCount);
    }

    @Test
    public void testSize() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Assert.assertEquals(6 * 4 * 3 * 2, counter.getSize());
    }

    @Test
    public void testToString() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        String expected = "[0][0][0]" +
                "[0][0][1]" +
                "[0][0][2]" +
                "[0][1][0]" +
                "[0][1][1]" +
                "[0][1][2]" +
                "[1][0][0]" +
                "[1][0][1]" +
                "[1][0][2]" +
                "[1][1][0]" +
                "[1][1][1]" +
                "[1][1][2]" +
                "[1][2][0]" +
                "[1][2][1]" +
                "[1][2][2]" +
                "[2][0][0]";

        IntStream.iterate(0, i -> iterator.hasNext() ? iterator.next() : iterator.next())
                .forEachOrdered(index -> {
                    String currentCounter = counter.toString();
                    String expectedCounter = "[";
                    for (int j = 0; j < dimension; j++) {
                        expectedCounter += "[" + iterator.getCount(j) + "]";
                        if (j != dimension - 1) {
                            expectedCounter += ",";
                        }
                    }
                    expectedCounter += "]";
                    Assert.assertEquals(expectedCounter, currentCounter);
                });
    }

    @Test
    public void testToStringLastElement() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        String expected = "[";
        for (int j = 0; j < dimension - 1; j++) {
            expected += "[" + size[j] + "]";
            if (j != dimension - 2) {
                expected += ",";
            }
        }
        expected += "[" + (size[dimension - 1] - 1) + "]";

        Iterator lastIterator = counter.iterator();
        String currentCounter = counter.toString();

        while (lastIterator.hasNext()) {
            lastIterator.next();
        }

        Assert.assertEquals(expected, currentCounter);
    }
}