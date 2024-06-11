import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

 private MultidimensionalCounter multidimensionalCounter;

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructor_NegativeSize_ShouldThrowNotStrictlyPositiveException() {
 int[] negativeSize =new int[]{-1};
 try {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(negativeSize);
 } catch (NotStrictlyPositiveException e) {
 //expected exception
 }
 }

 @Test
 public void testConstructor_ValidSizes_ShouldInitializeCorrectly() {
 int[] validSizes =new int[]{3, 2, 1};
 try {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(validSizes);
 assertEquals(3, multidimensionalCounter.getDimension());
 int[] sizes = multidimensionalCounter.getSizes();
 assertArrayEquals(validSizes, sizes);
 int totalSize = multidimensionalCounter.getSize();
 assertEquals(validSizes[0], sizes[0]);
 for (int i = 1; i < sizes.length; i++) {
 assertEquals(validSizes[i], sizes[i] * sizes[i - 1]);
 }
 assertEquals(totalSize, validSizes[0] * validSizes[1] * validSizes[2]);
 } catch (NotStrictlyPositiveException e) {
 fail("Unexpected exception");
 }
 }

 @Test(expected = OutOfRangeException.class)
 public void testGetCounts_InvalidIndex_ShouldThrowOutOfRangeException() {
 try {
 int[] invalidIndex =new int[]{-1};
 multidimensionalCounter.getCounts(invalidIndex[0]);
 } catch (OutOfRangeException e) {
 //expected exception
 }
 }

 @Test
 public void testGetCounts_ValidIndex_ShouldReturnCorrectCounts() {
 int[] validCounts =new int[]{2, 3};
 int expectedTotalSize = validSizes[0] * validSizes[1];
 int index = validCounts[0] + validCounts[1] * validSizes[1];
 try {
 int[] counts = multidimensionalCounter.getCounts(index);
 assertEquals(validCounts.length, counts.length);
 assertArrayEquals(validCounts, counts);
 } catch (OutOfRangeException e) {
 fail("Unexpected exception");
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testGetCount_InvalidDimension_ShouldThrowDimensionMismatchException() {
 int invalidDimension =3;
 try {
 int count = multidimensionalCounter.getCount(new int[]{invalidDimension});
 } catch (DimensionMismatchException e) {
 //expected exception
 }
 }

 @Test
 public void testGetCount_ValidDimension_ShouldReturnCorrectCount() {
 int validDimension =2;
 int validIndex = validSizes[validDimension] * (validSizes[validDimension - 1] - 1) + validSizes[validDimension - 1];
 int expectedCount = validSizes[0] * validSizes[1] * (validSizes[validDimension] - 1) + validSizes[validDimension] * validIndex;
 try {
 int count = multidimensionalCounter.getCount(new int[]{validDimension});
 assertEquals(expectedCount, count);
 } catch (OutOfRangeException | DimensionMismatchException e) {
 fail("Unexpected exception");
 }
 }

 @Test
 public void testIterator_HasNext_ShouldReturnTrueWhenThereAreMoreElements() {
 int[] validSizes =new int[]{3, 2, 1};
 try {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(validSizes);
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 for (int i = 0; i < multidimensionalCounter.getSize(); i++) {
 assertTrue(iterator.hasNext());
 }
 } catch (NotStrictlyPositiveException e) {
 fail("Unexpected exception");
 }
 }

 @Test
 public void testIterator_Next_ShouldReturnCorrectValues() {
 int[] validSizes =new int[]{3, 2, 1};
 try {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(validSizes);
 MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
 int count = -1;
 while (iterator.hasNext()) {
 count = iterator.next();
 }
 assertEquals(multidimensionalCounter.getSize(), count);
 } catch (NotStrictlyPositiveException e) {
 fail("Unexpected exception");
 }
 }

 @Test
 public void testToString_ShouldReturnCorrectStringRepresentation() {
 int[] validSizes =new int[]{3, 2, 1};
 try {
 MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(validSizes);
 assertEquals("[0][0][0]", multidimensionalCounter.toString());
 } catch (NotStrictlyPositiveException e) {
 fail("Unexpected exception");
 }
 }

}