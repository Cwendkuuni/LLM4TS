import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter multidimensionalCounter;

 @Test
 public void testMultidimensionalCounterConstruction() {
 int[] size = {2, 3, 1};
 try {
 multidimensionalCounter = new MultidimensionalCounter(size);
 assertEquals(3, multidimensionalCounter.getDimension());
 assertArrayEquals(new int[]{2, 3, 1}, multidimensionalCounter.getSizes());
 assertEquals(2 * 3 * 1, multidimensionalCounter.getSize());
 } catch (NotStrictlyPositiveException e) {
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testGetDimension() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 assertEquals(3, multidimensionalCounter.getDimension());
 }

 @Test
 public void testGetSizes() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 assertArrayEquals(new int[]{2, 3, 1}, multidimensionalCounter.getSizes());
 }

 @Test
 public void testGetSize() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 assertEquals(2 * 3 * 1, multidimensionalCounter.getSize());
 }

 @Test
 public void testIteratorHasNext() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 assertEquals(false, iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 assertEquals(-1, iterator.next());
 iterator.next();
 assertEquals(0, iterator.next());
 }

 @Test
 public void testGetCounts() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 iterator.next();
 iterator.next();
 int[] expected = new int[]{0, 0, 1};
 assertArrayEquals(expected, iterator.getCounts());
 }

 @Test
 public void testGetCount() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 int[] expected = new int[]{0, 1, 1};
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 iterator.next();
 iterator.next();
 assertArrayEquals(expected, multidimensionalCounter.getCounts(iterator.getCount()));
 }

 @Test
 public void testGetCountOutOfRange() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 assertThrows(OutOfRangeException.class, () -> multidimensionalCounter.getCounts(-1));
 }

 @Test
 public void testGetCountDimensionMismatch() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 assertThrows(DimensionMismatchException.class, () -> multidimensionalCounter.getCounts(new int[]{2, 4}));
 }

 @Test
 public void testToString() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 iterator.next();
 iterator.next();
 String expected = "[0][0][1]";
 assertEquals(expected, multidimensionalCounter.toString());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testCounterConstructionNegativeSizes() {
 int[] size = {-2, 3, 1};
 new MultidimensionalCounter(size);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testCounterConstructionZeroSizes() {
 int[] size = {0, 3, 1};
 new MultidimensionalCounter(size);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 assertThrows(OutOfRangeException.class, () -> multidimensionalCounter.getCounts(Integer.MAX_VALUE));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongDimension() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 assertThrows(DimensionMismatchException.class, () -> multidimensionalCounter.getCounts(new int[]{2, 3, 2}));
 }

}