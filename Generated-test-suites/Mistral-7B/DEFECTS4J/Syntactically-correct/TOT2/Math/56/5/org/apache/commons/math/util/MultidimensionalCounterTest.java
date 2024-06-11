import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 /**
 * Test constructor with valid dimensions.
 */
 @Test
 public void testConstructorValidDimensions() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 assertEquals(3, counter.getDimension());
 int[] sizes = counter.getSizes();
 assertEquals(2, sizes[0]);
 assertEquals(3, sizes[1]);
 assertEquals(4, sizes[2]);
 }

 /**
 * Test constructor with invalid dimension.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorInvalidDimension() {
 new MultidimensionalCounter(new int[] { 2, 3, -1 });
 }

 /**
 * Test getCounts method with valid index.
 */
 @Test
 public void testGetCountsValidIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 int[] counts = counter.getCounts(12);
 assertEquals(0, counts[0]);
 assertEquals(1, counts[1]);
 assertEquals(2, counts[2]);
 }

 /**
 * Test getCounts method with out of range index.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetCountsOutOfRangeIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 counter.getCounts(15);
 }

 /**
 * Test getCount method with valid index.
 */
 @Test
 public void testGetCountValidIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 int index = counter.getCount(0, 0, 0);
 assertEquals(0, index);
 }

 /**
 * Test getCount method with invalid index.
 */
 @Test(expected = OutOfRangeException.class)
 public void testGetCountInvalidIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 int index = counter.getCount(-1, 0, 0);
 }

 /**
 * Test getCount method with invalid index in last dimension.
 */
 @Test(expected = OutOfRangeException.class)
 public void testGetCountInvalidIndexLastDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 int index = counter.getCount(0, 0, 4);
 }

 /**
 * Test getCount method with invalid index in other dimensions.
 */
 @Test(expected = DimensionMismatchException.class)
 public void testGetCountInvalidIndexOtherDimensions() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 int index = counter.getCount(3, 0, 0);
 }

 /**
 * Test iterator hasNext method with empty counter.
 */
 @Test
 public void testIteratorHasNextEmptyCounter() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 Iterator iterator = counter.iterator();
 assertFalse(iterator.hasNext());
 }

 /**
 * Test iterator hasNext method with non-empty counter.
 */
 @Test
 public void testIteratorHasNextNonEmptyCounter() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 Iterator iterator = counter.iterator();
 iterator.next();
 assertTrue(iterator.hasNext());
 }

 /**
 * Test iterator next method with empty counter.
 */
 @Test
 public void testIteratorNextEmptyCounter() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 Iterator iterator = counter.iterator();
 assertEquals(-1, iterator.next());
 }

 /**
 * Test iterator next method with non-empty counter.
 */
 @Test
 public void testIteratorNextNonEmptyCounter() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 Iterator iterator = counter.iterator();
 assertEquals(0, iterator.next());
 }

 /**
 * Test iterator getCount method with empty counter.
 */
 @Test
 public void testIteratorGetCountEmptyCounter() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 Iterator iterator = counter.iterator();
 iterator.next();
 assertEquals(-1, iterator.getCount());
 }

 /**
 * Test iterator getCount method with non-empty counter.
 */
 @Test
 public void testIteratorGetCountNonEmptyCounter() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 Iterator iterator = counter.iterator();
 iterator.next();
 iterator.next();
 int[] counts = iterator.getCounts();
 assertEquals(1, counts[0]);
 assertEquals(1, counts[1]);
 assertEquals(1, counts[2]);
 }

 /**
 * Test iterator getCount method with negative index.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorGetCountNegativeIndex() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 Iterator iterator = counter.iterator();
 iterator.next();
 iterator.next();
 assertEquals(-1, iterator.getCount(-1));
 }

 /**
 * Test iterator getCount method with index out of range.
 */
 @Test(expected = IndexOutOfBoundsException.class)
 public void testIteratorGetCountIndexOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 Iterator iterator = counter.iterator();
 assertEquals(-1, iterator.getCount(3, 0, 0));
 }

 /**
 * Test iterator getCount method with dimension out of range.
 */
 @Test(expected = DimensionMismatchException.class)
 public void testIteratorGetCountDimensionOutOfRange() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 Iterator iterator = counter.iterator();
 assertEquals(-1, iterator.getCount(0, 3, 0));
 }

 /**
 * Test toString method with empty counter.
 */
 @Test
 public void testToStringEmptyCounter() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 assertEquals("[][][]", counter.toString());
 }

 /**
 * Test toString method with non-empty counter.
 */
 @Test
 public void testToStringNonEmptyCounter() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 3, 4 });
 counter.next();
 counter.next();
 assertEquals("[1][1][1]", counter.toString());
 }
}