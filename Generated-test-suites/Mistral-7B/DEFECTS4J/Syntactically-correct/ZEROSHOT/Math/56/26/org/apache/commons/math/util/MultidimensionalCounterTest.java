import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultidimensionalCounterTest {

 @Test
 public void testConstructor() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {3, 2, 1});
 assertEquals(3, mdc.getDimension());
 assertEquals(3 * 2 * 1, mdc.getSize());
 assertArrayEquals(new int[] {0, 0, 0}, mdc.getCounts(0));
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorNegativeSize() {
 new MultidimensionalCounter(new int[] {-1, 2, 1});
 }

 @Test
 public void testSizeAndGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {3, 2, 1});
 assertEquals(7, mdc.getSize());
 assertArrayEquals(new int[] {2, 1}, mdc.getCounts(6));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsInvalidDimension() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {3, 2, 1});
 mdc.getCounts(4);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountInvalidIndex() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {3, 2, 1});
 mdc.getCount(-1);
 }

 @Test
 public void testIterator() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {3, 2, 1});
 Iterator it = mdc.iterator();
 int count = 0;
 int counter[] = new int[3];
 while (it.hasNext()) {
 int i = it.next();
 assertEquals(i, count++);
 it.getCounts(counter);
 assertArrayEquals(new int[] {counter[0], counter[1], counter[2]}, new int[] {0, 0, 0});
 }
 assertFalse(it.hasNext());
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {3, 2, 1});
 assertEquals(0, mdc.getCount(new int[] {0, 0, 0}));
 assertEquals(1, mdc.getCount(new int[] {1, 0, 0}));
 assertEquals(2, mdc.getCount(new int[] {1, 1, 0}));
 assertEquals(3, mdc.getCount(new int[] {0, 1, 0}));
 assertEquals(4, mdc.getCount(new int[] {1, 1, 1}));
 assertEquals(6, mdc.getCount(new int[] {1, 2, 0}));
 assertEquals(7, mdc.getCount(new int[] {1, 2, 1}));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountInvalidDimension2() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {3, 2, 1});
 mdc.getCount(new int[] {4, 3, 2});
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountInvalidIndex2() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(new int[] {3, 2, 1});
 mdc.getCount(new int[] {3, 3, 1});
 }
}