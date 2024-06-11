import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

 @Test
 public void testConstructor() throws NumberIsTooLargeException {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 assertEquals(2, matrix.getRowDimension());
 assertEquals(3, matrix.getColumnDimension());
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorNegativeSize() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(-1, 3);
 fail("Expected NumberIsTooLargeException but it didn't occur.");
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testConstructorInvalidColumns() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, -1);
 fail("Expected NumberIsTooLargeException but it didn't occur.");
 }

 @Test
 public void testCopy() {
 OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix copy = original.copy();
 assertEquals(original, copy);
 }

 @Test
 public void testCreateMatrix() {
 OpenMapRealMatrix matrix = OpenMapRealMatrix.createMatrix(2, 3);
 assertEquals(2, matrix.getRowDimension());
 assertEquals(3, matrix.getColumnDimension());
 }

 @Test
 public void testGetColumnDimension() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 assertEquals(3, matrix.getColumnDimension());
 }

 @Test
 public void testAdd() {
 OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity()), 2, 2);
 OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity()), 2, 2);
 OpenMapRealMatrix result = left.add(right);
 assertArrayEquals(new double[][]{
 new double[]{1.0, 1.0},
 new double[]{1.0, 1.0}
 }, result.getData());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAddDimensionMismatch() {
 OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity()), 2, 2);
 OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3).identity()), 2, 2);
 OpenMapRealMatrix result = left.add(right);
 fail("Expected DimensionMismatchException but it didn't occur.");
 }

 @Test
 public void testSubtract() {
 OpenMapRealMatrix left = new OpenMapRealMatrix(new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity()), 2, 2);
 OpenMapRealMatrix right = new OpenMapRealMatrix(new OpenMapRealMatrix(new OpenMapRealMatrix(2, 2).identity()), 2, 2);
 OpenMapRealMatrix result = left.subtract(right);
 assertArrayEquals(new double[][]{
 new double[]{-1.0, -1.0},
 new double[]{-1.0, -1.0}
 }, result.getData());
 }

 @Test
 public void testMultiply() {
 OpenMapRealMatrix left = new OpenMapRealMatrix(
 new OpenMapRealMatrix(new double[][]{
 new double[]{1.0, 2.0},
 new double[]{3.0, 4.0}
 }, 2, 2), 2, 3);
 OpenMapRealMatrix right = new OpenMapRealMatrix(
 new OpenMapRealMatrix(new double[][]{
 new double[]{5.0, 6.0},
 new double[]{7.0, 8.0}
 }, 3, 2), 3, 2);
 OpenMapRealMatrix result = left.multiply(right);
 assertArrayEquals(new double[][]{
 new double[]{11.0, 12.0, 13.0},
 new double[]{23.0, 24.0, 25.0}
 }, result.getData());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMultiplyDimensionMismatch() {
 OpenMapRealMatrix left = new OpenMapRealMatrix(
 new OpenMapRealMatrix(new double[][]{
 new double[]{1.0, 2.0},
 new double[]{3.0, 4.0}
 }, 2, 2), 2, 3);
 OpenMapRealMatrix right = new OpenMapRealMatrix(
 new OpenMapRealMatrix(new double[][]{
 new double[]{1.0},
 new double[]{2.0}
 }, 2, 1), 2, 1);
 OpenMapRealMatrix result = left.multiply(right);
 fail("Expected DimensionMismatchException but it didn't occur.");
 }

 @Test
 public void testGetEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
 new double[]{1.0, 2.0},
 new double[]{3.0, 4.0}
 }, 2, 2), 2, 2);
 assertEquals(1.0, matrix.getEntry(0, 0), 0.01);
 assertEquals(3.0, matrix.getEntry(1, 1), 0.01);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetEntryInvalidIndex() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
 new double[]{1.0, 2.0},
 new double[]{3.0, 4.0}
 }, 2, 2), 2, 2);
 matrix.getEntry(3, 3);
 fail("Expected IndexOutOfBoundsException but it didn't occur.");
 }

 @Test
 public void testSetEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
 new double[]{1.0, 2.0},
 new double[]{3.0, 4.0}
 }, 2, 2), 2, 2);
 matrix.setEntry(0, 0, 5.0);
 assertEquals(5.0, matrix.getEntry(0, 0), 0.01);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetEntryInvalidIndex() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
 new double[]{1.0, 2.0},
 new double[]{3.0, 4.0}
 }, 2, 2), 2, 2);
 matrix.setEntry(3, 3, 5.0);
 fail("Expected IndexOutOfBoundsException but it didn't occur.");
 }

 @Test
 public void testAddToEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
 new double[]{1.0, 2.0},
 new double[]{3.0, 4.0}
 }, 2, 2), 2, 2);
 matrix.addToEntry(0, 0, 5.0);
 assertEquals(6.0, matrix.getEntry(0, 0), 0.01);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddToEntryInvalidIndex() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
 new double[]{1.0, 2.0},
 new double[]{3.0, 4.0}
 }, 2, 2), 2, 2);
 matrix.addToEntry(3, 3, 5.0);
 fail("Expected IndexOutOfBoundsException but it didn't occur.");
 }

 @Test
 public void testMultiplyEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
 new double[]{1.0, 2.0},
 new double[]{3.0, 4.0}
 }, 2, 2), 2, 2);
 matrix.multiplyEntry(0, 0, 5.0);
 assertEquals(5.0, matrix.getEntry(0, 0), 0.01);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testMultiplyEntryInvalidIndex() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(new double[][]{
 new double[]{1.0, 2.0},
 new double[]{3.0, 4.0}
 }, 2, 2), 2, 2);
 matrix.multiplyEntry(3, 3, 5.0);
 fail("Expected IndexOutOfBoundsException but it didn't occur.");
 }

}