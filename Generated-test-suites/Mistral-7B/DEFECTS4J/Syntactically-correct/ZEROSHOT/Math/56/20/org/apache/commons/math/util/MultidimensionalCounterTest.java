import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultidimensionalCounterTest {

 @Test
 public void testMultidimensionalCounterConstruction() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 assertEquals(3, counter.getDimension());
 assertArrayEquals(new int[] { 2, 4, 3 }, counter.getSizes());
 assertFalse(counter.hasNext());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeSizes() {
 new MultidimensionalCounter(new int[] { -1, 4, 3 });
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testZeroSizes() {
 new MultidimensionalCounter(new int[] { 0, 4, 3 });
 }

 @Test
 public void testGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 assertEquals(0, counter.getCount());
 counter.next();
 assertEquals(1, counter.getCount());
 }

 @Test
 public void testGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 Iterator iterator = counter.iterator();
 int[] counts = iterator.getCounts();
 assertEquals(3, counts.length);
 assertEquals(0, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(0, counts[2]);
 iterator.next();
 counts = iterator.getCounts();
 assertEquals(0, counts[0]);
 assertEquals(0, counts[1]);
 assertEquals(1, counts[2]);
 }

 @Test
 public void testGetCountAtLastDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 Iterator iterator = counter.iterator();
 int count = 0;
 for (int i = 0; i < 13; i++) {
 iterator.next();
 count++;
 }
 assertEquals(12, counter.getCount(2, 2, 1));
 assertEquals(count, counter.getCount());
 }

 @Test
 public void testGetCountAtFirstDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 Iterator iterator = counter.iterator();
 int count = 0;
 for (int i = 0; i < 3; i++) {
 iterator.next();
 count++;
 }
 assertEquals(count, counter.getCount(0));
 }

 @Test
 public void testGetCountAtMiddleDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 Iterator iterator = counter.iterator();
 int count = 0;
 for (int i = 0; i < 3; i++) {
 iterator.next();
 count += 4;
 }
 for (int i = 0; i < 3; i++) {
 iterator.next();
 count += 3;
 }
 assertEquals(count, counter.getCount(1));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testInvalidNumberOfDimensions() {
 new MultidimensionalCounter(new int[] { 2, 4 });
 }

 @Test(expected = OutOfRangeException.class)
 public void testInvalidCountAtLastDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 counter.next();
 counter.next();
 counter.next();
 counter.next();
 counter.next();
 counter.next();
 try {
 counter.getCount(3, 2, 1);
 } catch (OutOfRangeException oor) {
 // expected
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testInvalidCountAtFirstDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 try {
 counter.getCount(-1, 2, 1);
 } catch (OutOfRangeException oor) {
 // expected
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testInvalidCountAtMiddleDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 try {
 counter.getCount(2, -1, 1);
 } catch (OutOfRangeException oor) {
 // expected
 }
 try {
 counter.getCount(2, 5, 1);
 } catch (OutOfRangeException oor) {
 // expected
 }
 }

 @Test
 public void testToString() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 assertEquals("[[0], [0], [0]]", counter.toString());
 Iterator iterator = counter.iterator();
 for (int i = 0; i < 11; i++) {
 iterator.next();
 }
 assertEquals("[1, 1, 1]", counter.toString());
 }

 @Test
 public void testIteratorGetCount() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 Iterator iterator = counter.iterator();
 int[] counts = new int[3];
 int count = 0;
 for (int i = 0; i < 13; i++) {
 iterator.next();
 counts = iterator.getCounts();
 assertEquals(i % 3, counts[2]);
 assertEquals(i / 3 % 2, counts[1]);
 assertEquals(i / 3 / 2 % 2, counts[0]);
 count++;
 }
 assertEquals(12, counter.getCount());
 }

 @Test
 public void testIteratorGetCounts() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 Iterator iterator = counter.iterator();
 int[] counts = new int[3];
 for (int i = 0; i < 13; i++) {
 iterator.next();
 counts = iterator.getCounts();
 assertArrayEquals(new int[] { i % 3, i / 3 % 2, i / 3 / 2 % 2 }, counts);
 }
 }

 @Test
 public void testIteratorGetCountAtLastDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 Iterator iterator = counter.iterator();
 int[] counts = new int[3];
 int lastDimCount = 0;
 for (int i = 0; i < 13; i++) {
 iterator.next();
 counts = iterator.getCounts();
 lastDimCount = counts[2];
 }
 assertEquals(3, lastDimCount);
 }

 @Test
 public void testIteratorGetCountAtFirstDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 Iterator iterator = counter.iterator();
 int[] counts = new int[3];
 int firstDimCount = 0;
 for (int i = 0; i < 12; i++) {
 iterator.next();
 counts = iterator.getCounts();
 firstDimCount += counts[0];
 }
 assertEquals(9, firstDimCount);
 }

 @Test
 public void testIteratorGetCountAtMiddleDimension() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 Iterator iterator = counter.iterator();
 int[] counts = new int[3];
 int middleDimCount = 0;
 for (int i = 0; i < 12; i++) {
 iterator.next();
 counts = iterator.getCounts();
 middleDimCount += counts[1];
 }
 assertEquals(12, middleDimCount);
 }

 @Test
 public void testIteratorGetCountAggregated() {
 MultidimensionalCounter counter = new MultidimensionalCounter(new int[] { 2, 4, 3 });
 Iterator iterator = counter.iterator();
 int[] counts = new int[3];
 int[] sum = new int[3];
 int count = 0;
 for (int i = 0; i < 13; i++) {
 iterator.next();
 counts = iterator.getCounts();
 sum[count] = counts[count];
 ++count;
 }
 assertEquals(11, sum[0]);
 assertEquals(10, sum[1]);
 assertEquals(12, sum[2]);
 }
}