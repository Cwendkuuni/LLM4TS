import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 @Test
 public void testConstructor() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 assertEquals(729, counter.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeSize() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{-1, 2, 1});
 }

 @Test
 public void testIterator() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 Iterator it = counter.iterator();
 int count = 0;
 int[] counts = null;
 int expected = 0;
 while (it.hasNext()) {
 int index = it.next();
 counts = it.getCounts();
 assertEquals(expected++, index);
 assertEquals(new int[]{1, 1, 1}, counts);
 }
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int index = 2;
 int[] expected = new int[]{1, 1, 1};
 int[] actual = counter.getCounts(index);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountsOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int index = 729;
 counter.getCounts(index);
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int[] expected = new int[]{1, 1, 1};
 int index = 0;
 int actual = counter.getCount(expected);
 assertEquals(index, actual);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountIncorrectDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int[] incorrectDimension = new int[]{1};
 counter.getCount(incorrectDimension);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int[] multidimensionalCount = new int[]{1, 2, 1};
 int index = counter.getCount(multidimensionalCount[0], multidimensionalCount[1], multidimensionalCount[2] + 1);
 }

 @Test
 public void testGetSizes() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3, 2, 1});
 int[] expected = new int[]{3, 2, 1};
 int[] actual = counter.getSizes();
 assertArrayEquals(expected, actual);
 }
}