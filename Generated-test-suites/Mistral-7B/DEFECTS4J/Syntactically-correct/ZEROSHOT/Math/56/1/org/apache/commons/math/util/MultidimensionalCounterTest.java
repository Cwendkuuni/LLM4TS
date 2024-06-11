import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter counter;

 @Test
 public void testConstructor() {
 counter = new MultidimensionalCounter(new int[]{2, 3, 4});
 assertEquals(3, counter.getDimension());
 assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeConstructorParam() {
 counter = new MultidimensionalCounter(new int[]{-1, 3, 4});
 }

 @Test
 public void testSize() {
 assertEquals(23 * 3 * 2, counter.getSize());
 }

 @Test
 public void testGetCount() {
 int[] counts = new int[counter.getDimension()];
 int index = 0;
 int expected = 0;
 while (counter.hasNext()) {
 counts = counter.next().getCounts();
 assertEquals(expected++, counter.getCount(counts));
 }
 }

 @Test
 public void testGetCounts() {
 int i = 0;
 while (counter.hasNext()) {
 int[] counts = counter.next().getCounts();
 for (int j = 0; j < counter.getDimension(); j++) {
 assertEquals(i++, counts[j]);
 }
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCountsWithWrongDim() {
 int[] counts = counter.getCounts(5);
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCountOutOfRange() {
 counter.next();
 counter.next();
 int index = counter.getSize();
 int count = counter.getCount(index);
 }

 @Test
 public void testIteratorHasNext() {
 assertFalse(counter.iterator().hasNext());
 counter.next();
 assertTrue(counter.iterator().hasNext());
 }

 @Test
 public void testIteratorGetCount() {
 int[] counts = counter.iterator().next().getCounts();
 assertEquals(counter.getCount(counts), counter.iterator().next().getCount());
 }

 @Test
 public void testIteratorGetCounts() {
 int[] counts1 = counter.iterator().next().getCounts();
 int[] counts2 = counter.iterator().next().getCounts();
 assertArrayEquals(counts1, counts2);
 }

 @Test
 public void testToString() {
 String toString = counter.toString();
 String expected = "[0][0][0]";
 for (int i = 0; i < counter.getSize(); i++) {
 assertEquals(expected, toString);
 index++;
 next();
 expected = Integer.toString(index);
 for (int j = 0; j < counter.getDimension(); j++) {
 expected += "[";
 expected += Integer.toString(getCount(j));
 expected += "]";
 }
 }
 }
}