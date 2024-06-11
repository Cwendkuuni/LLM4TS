import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithNegativeSizes() {
 try {
 new MultidimensionalCounter(new int[]{-1, 2, 3});
 fail("Expected NotStrictlyPositiveException");
 } catch (NotStrictlyPositiveException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 assertEquals(3, counter.getDimension());
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 try {
 counter.getCounts(10);
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int[] counts = counter.getCounts(0);
 assertArrayEquals(new int[]{0, 0, 0}, counts);
 counts = counter.getCounts(3);
 assertArrayEquals(new int[]{2, 2, 0}, counts);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 try {
 counter.getCount(new int[]{-1, 2, 1});
 fail("Expected OutOfRangeException");
 } catch (OutOfRangeException e) {
 // Expected exception
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountWithWrongDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 try {
 counter.getCount(new int[]{3, 2});
 fail("Expected DimensionMismatchException");
 } catch (DimensionMismatchException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int count = counter.getCount(new int[]{0, 0, 0});
 assertEquals(0, count);
 count = counter.getCount(new int[]{2, 1, 1});
 assertEquals(6, count);
 }

 @Test
 public void testGetSize() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 assertEquals(6, counter.getSize());
 }

 @Test
 public void testGetSizes() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int[] sizes = counter.getSizes();
 assertArrayEquals(new int[]{3, 2, 1}, sizes);
 }

 @Test
 public void testToString() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 String toString = counter.toString();
 assertEquals("[0][0][0]", toString);
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 Iterator iterator = counter.iterator();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 Iterator iterator = counter.iterator();
 iterator.next();
 assertTrue(iterator.hasNext());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 Iterator iterator = counter.iterator();
 iterator.next();
 assertEquals(0, iterator.getCount());
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 Iterator iterator = counter.iterator();
 iterator.next();
 int[] counts = iterator.getCounts();
 assertArrayEquals(new int[]{0, 0, 0}, counts);
 }

 @Test
 public void testIteratorGetCountsLastDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 Iterator iterator = counter.iterator();
 iterator.next();
 iterator.next();
 int[] counts = iterator.getCounts();
 assertArrayEquals(new int[]{0, 1, 0}, counts);
 }

 @Test
 public void testIteratorNextMultipleTimes() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 Iterator iterator = counter.iterator();
 for (int i = 0; i < 7; ++i) {
 iterator.next();
 }
 assertEquals(7, iterator.getCount());
 }
}