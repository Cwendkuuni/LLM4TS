import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 @Test
 public void testConstructor() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 assertEquals(3, mdc.getDimension());
 int[] expected = {0, 0, 0};
 int[] actual = mdc.getCounts(0);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeSizes() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(-1, 2, 3);
 fail("This constructor should throw NotStrictlyPositiveException");
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testZeroSizes() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(0, 2, 3);
 fail("This constructor should throw NotStrictlyPositiveException");
 }

 @Test
 public void testGetDimension() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 assertEquals(3, mdc.getDimension());
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 int[] expected = {0, 0, 0};
 int[] actual = mdc.getCounts(0);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongDimensionSize() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 int[] wrongSize = {1, 2, 4};
 mdc.getCounts(wrongSize);
 fail("This method should throw DimensionMismatchException");
 }

 @Test
 public void testGetCountsWithMaxIndex() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 int[] expected = {2, 2, 2};
 int[] actual = mdc.getCounts(mdc.getSize() - 1);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsWithNegativeIndex() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 int[] wrongIndex = {-1, 0, 0};
 mdc.getCounts(wrongIndex);
 fail("This method should throw OutOfRangeException");
 }

 @Test
 public void testIteratorHasNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 Iterator it = mdc.iterator();
 assertTrue(it.hasNext());
 }

 @Test
 public void testIteratorNext() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 Iterator it = mdc.iterator();
 int expected = 0;
 int actual = it.next();
 assertEquals(expected, actual);
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 Iterator it = mdc.iterator();
 int expected = 0;
 int actual = it.getCount();
 assertEquals(expected, actual);
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 Iterator it = mdc.iterator();
 int[] expected = {0, 0, 0};
 int[] actual = it.getCounts();
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testIteratorGetCountsLastDimension() {
 MultidimensionalCounter mdc = new MultidimensionalCounter(1, 2, 3);
 Iterator it = mdc.iterator();
 int[] expected = {2, 2, 2};
 int[] actual = it.getCounts(mdc.getSize() - 1);
 assertArrayEquals(expected, actual);
 }
}