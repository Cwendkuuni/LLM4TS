import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Assert.*; // For assertArrays equality assertion

public class MultidimensionalCounterTest {

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructor_NegativeSizes() {
 try {
 new MultidimensionalCounter(new int[] {-1, 2, 3});
 fail();
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 }

 @Test
 public void testConstructor_ValidSizes() {
 int[] sizes = new int[] {2, 3, 4};
 MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
 assertEquals(6 * 3 * 4, counter.getTotalSize());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCounts_OutOfBounds() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 try {
 counter.getCounts(-1);
 fail();
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetCounts_ValidIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 int[] indices = counter.getCounts(5);
 assertArrayEquals(new int[] {1, 2, 3}, indices);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCount_OutOfBounds() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 try {
 counter.getCount(new int[] {-1, 2, 3});
 fail();
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCount_InvalidDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 try {
 counter.getCount(new int[] {1, 5, 3});
 fail();
 } catch (DimensionMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetCount_ValidCoordinates() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 int count = counter.getCount(new int[] {1, 1, 1});
 assertEquals(1, count);
 }

 @Test
 public void testIterator() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] {2, 3, 4});
 MultidimensionalCounter.Iterator iterator = counter.iterator();
 int expectedTotal = 0;
 while (iterator.hasNext()) {
 int next = iterator.next();
 assertEquals(next, iterator.getCount());
 expectedTotal++;
 }
 assertEquals(counter.getTotalSize(), expectedTotal);
 }

}