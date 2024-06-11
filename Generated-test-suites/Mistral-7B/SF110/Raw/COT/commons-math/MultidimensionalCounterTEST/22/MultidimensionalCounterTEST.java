import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter multidimensionalCounter;

 @Test
 public void testMultidimensionalCounterConstructor() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 assertEquals(3, multidimensionalCounter.getDimension());
 assertEquals(2, multidimensionalCounter.getSizes()[0]);
 assertEquals(3, multidimensionalCounter.getSizes()[1]);
 assertEquals(1, multidimensionalCounter.getSizes()[2]);
 assertEquals(6, multidimensionalCounter.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testMultidimensionalCounterConstructorWithNegativeSizes() {
 try {
 new MultidimensionalCounter(new int[]{-1, 3, 1});
 fail();
 } catch (NotStrictlyPositiveException e) {
 // pass
 }
 }

 @Test
 public void testIteratorHasNext() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 assertTrue(iterator.hasNext());
 assertTrue(iterator.hasNext());
 assertTrue(iterator.hasNext());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 assertEquals(0, iterator.next());
 assertEquals(1, iterator.next());
 assertEquals(2, iterator.next());
 assertEquals(3, iterator.next());
 assertEquals(4, iterator.next());
 assertEquals(5, iterator.next());
 assertEquals(6, iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testGetCounts() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 int index = 0;
 int[] counts = multidimensionalCounter.getCounts(index);
 assertEquals(0, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(0, counts[2]);
 index = multidimensionalCounter.getSize() - 1;
 counts = multidimensionalCounter.getCounts(index);
 assertEquals(2, counts[0]);
 assertEquals(2, counts[1]);
 assertEquals(0, counts[2]);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 try {
 multidimensionalCounter.getCounts(-1);
 fail();
 } catch (OutOfRangeException e) {
 // pass
 }
 }

 @Test
 public void testGetCount() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 int[] coordinates = new int[]{0, 0, 0};
 int count = multidimensionalCounter.getCount(coordinates);
 assertEquals(0, count);
 coordinates[0] = 1;
 count = multidimensionalCounter.getCount(coordinates);
 assertEquals(1, count);
 coordinates[1] = 1;
 count = multidimensionalCounter.getCount(coordinates);
 assertEquals(2, count);
 coordinates[2] = 0;
 count = multidimensionalCounter.getCount(coordinates);
 assertEquals(1, count);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 final int[] coordinates = new int[]{2, 2, 1};
 try {
 multidimensionalCounter.getCount(coordinates);
 fail();
 } catch (OutOfRangeException e) {
 // pass
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountDimensionMismatch() {
 multidimensionalCounter = new MultidimensionalCounter(new int[]{2, 3, 1});
 final int[] coordinates = new int[]{2, 4, 1};
 try {
 multidimensionalCounter.getCount(coordinates);
 fail();
 } catch (DimensionMismatchException e) {
 // pass
 }
 }

}