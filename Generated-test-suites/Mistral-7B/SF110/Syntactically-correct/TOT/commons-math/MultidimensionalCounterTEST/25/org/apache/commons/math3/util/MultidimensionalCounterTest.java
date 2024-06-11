package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class MultidimensionalCounterTest
{
    private final int[] sizes;
    private MultidimensionalCounter counter;

    public MultidimensionalCounterTest(int... sizes) {
        this.sizes = sizes;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                new int[] { 1, 2, 3 },
                new int[] { 2, 2, 2 },
                new int[] { 3, 3 }
        });
    }

    @Test
    public void testConstructor() {
        try {
            counter = new MultidimensionalCounter(sizes);
            Assert.assertEquals(sizes.length, counter.getDimension());
            Assert.assertArrayEquals(sizes, counter.getSizes());
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Expected positive sizes");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        new MultidimensionalCounter(-1, sizes);
    }

    @Test
    public void testGetCount() {
        try {
            counter = new MultidimensionalCounter(sizes);
            int expected = 0;
            for (int i = 0; i < sizes.length; ++i) {
                expected += sizes[i] - 1;
                expected *= (sizes[i] - 1);
            }
            expected++;
            int actual = counter.getCount();
            Assert.assertEquals(expected, actual);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Expected positive sizes");
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        try {
            counter = new MultidimensionalCounter(sizes);
            counter.getCount(-1);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Expected positive sizes");
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongDimension() {
        try {
            counter = new MultidimensionalCounter(new int[] { 1, 2, 3 });
            counter.getCount(4);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Expected positive sizes");
        }
    }

    @Test
    public void testIterator() {
        try {
            counter = new MultidimensionalCounter(sizes);
            Iterator iterator = counter.iterator();
            List<Integer> expected = Arrays.asList(0);
            for (int i = 0; i < sizes.length; ++i) {
                for (int j = 0; j < sizes[i]; ++j) {
                    iterator.next();
                    expected.add(j);
                }
            }
            List<Integer> actual = new ArrayList<>();
            while (iterator.hasNext()) {
                actual.add(iterator.next());
            }
            Assert.assertEquals(expected, actual);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Expected positive sizes");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        try {
            counter = new MultidimensionalCounter(sizes);
            Iterator iterator = counter.iterator();
            iterator.remove();
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Expected positive sizes");
        }
    }

    @Test
    public void testGetCounts() {
        try {
            counter = new MultidimensionalCounter(sizes);
            int index = 0;
            int[] expected = new int[sizes.length];
            for (int i = 0; i < sizes.length; ++i) {
                expected[i] = i;
            }
            int[] actual = counter.getCounts(index);
            Assert.assertArrayEquals(expected, actual);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Expected positive sizes");
        } catch (OutOfRangeException e) {
            Assert.fail("Expected index within range");
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        try {
            counter = new MultidimensionalCounter(sizes);
            counter.getCounts(-1);
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Expected positive sizes");
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsLargerIndex() {
        try {
            counter = new MultidimensionalCounter(sizes);
            counter.getCounts(counter.getTotalSize());
        } catch (NotStrictlyPositiveException e) {
            Assert.fail("Expected positive sizes");
        }
    }
}