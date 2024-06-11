import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

 @Test
 public void testOpenMapRealMatrixConstruction() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 assertEquals(2, matrix.getRowDimension());
 assertEquals(3, matrix.getColumnDimension());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOpenMapRealMatrixNegativeDimension() {
 new OpenMapRealMatrix(-2, 3);
 }

 @Test
 public void testOpenMapRealMatrixCopy() {
 OpenMapRealMatrix original = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix copy = original.copy();
 assertEquals(original, copy);
 }

 @Test
 public void testOpenMapRealMatrixAddition() {
 OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix sum = matrix1.add(matrix2);
 assertEquals(matrix1, sum);
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 3; j++) {
 sum.setEntry(i, j, 0.0);
 }
 }
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 3; j++) {
 matrix1.setEntry(i, j, i + j);
 matrix2.setEntry(i, j, i - j);
 }
 }
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 3; j++) {
 assertEquals(i + j - i + j, sum.getEntry(i, j), 1e-14);
 }
 }
 }

 @Test
 public void testOpenMapRealMatrixSubtraction() {
 OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix difference = matrix1.subtract(matrix2);
 assertEquals(matrix1, difference);
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 3; j++) {
 difference.setEntry(i, j, 2 * (i + j));
 matrix2.setEntry(i, j, i - j);
 }
 }
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 3; j++) {
 assertEquals(2 * (i + j) - (i - j), difference.getEntry(i, j), 1e-14);
 }
 }
 }

 @Test
 public void testOpenMapRealMatrixMultiplication() {
 OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
 RealMatrix product = matrix1.multiply(matrix2);
 assertEquals(2, product.getRowDimension());
 assertEquals(2, product.getColumnDimension());
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 2; j++) {
 assertEquals(0.0, product.getEntry(i, j), 1e-14);
 }
 }
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 3; j++) {
 matrix1.setEntry(i, j, j);
 }
 }
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 2; j++) {
 for (int k = 0; k < 3; k++) {
 matrix2.setEntry(k, j, i);
 }
 }
 }
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 2; j++) {
 assertEquals(j, product.getEntry(i, j), 1e-14);
 }
 }
 }

 @Test
 public void testOpenMapRealMatrixMultiplyEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 matrix.multiplyEntry(0, 0, 3.0);
 assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testOpenMapRealMatrixNumberIsTooLargeException() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 matrix.multiplyEntry(0, 0, Double.POSITIVE_INFINITY);
 }

 @Test
 public void testOpenMapRealMatrixAddToEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 matrix.addToEntry(0, 0, 3.0);
 assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
 }

 @Test(expected = NumberIsTooLargeException.class)
 public void testOpenMapRealMatrixNumberIsTooLargeExceptionAddToEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 matrix.addToEntry(0, 0, Double.POSITIVE_INFINITY);
 }

 @Test
 public void testOpenMapRealMatrixSetEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 matrix.setEntry(0, 0, 3.0);
 assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOpenMapRealMatrixNegativeEntry() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 matrix.setEntry(0, 0, -1.0);
 }

 @Test
 public void testOpenMapRealMatrixBlockMultiplication() {
 OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 3);
 OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 2);
 BlockRealMatrix product = new BlockRealMatrix(2, 2);
 matrix1.multiply(matrix2, product);
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 2; j++) {
 assertEquals(0.0, product.getEntry(i, j), 1e-14);
 }
 }
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 3; j++) {
 matrix1.setEntry(i, j, j);
 }
 }
 for (int i = 0; i < 3; i++) {
 for (int j = 0; j < 2; j++) {
 matrix2.setEntry(i, j, i);
 }
 }
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 2; j++) {
 for (int k = 0; k < 3; k++) {
 assertEquals(i * j + i, product.getEntry(i, j), 1e-14);
 }
 }
 }
 }

 @Test
 public void testOpenMapRealMatrixComputeKey() {
 OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
 int key = matrix.computeKey(0, 0);
 assertEquals(0, key % columns);
 assertEquals(0, key / columns);
 key = matrix.computeKey(1, 1);
 assertEquals(1, key % columns);
 assertEquals(1, key / columns);
 }

}