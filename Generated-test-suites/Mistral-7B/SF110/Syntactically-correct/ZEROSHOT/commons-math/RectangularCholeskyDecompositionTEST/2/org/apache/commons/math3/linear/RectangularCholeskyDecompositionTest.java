import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testValidDecomposition() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 0.0}, {2.0, 3.0, 1.0}, {0.0, 1.0, 2.0}});
 try {
 RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = decomp.getRootMatrix();
 int rank = decomp.getRank();
 assertEquals(3, rank);
 MatrixUtils.print(System.out, root);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not be thrown for valid matrix
 assertTrue(false);
 }
 }

 @Test
 public void testInvalidDecomposition() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, -1.0}, {2.0, 3.0, 1.0}, {0.0, 1.0, 2.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 // Should throw NonPositiveDefiniteMatrixException
 assertTrue(false);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected for invalid matrix
 }
 }

 @Test
 public void testIdentityMatrixDecomposition() {
 RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
 try {
 RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = decomp.getRootMatrix();
 int rank = decomp.getRank();
 assertEquals(3, rank);
 MatrixUtils.print(System.out, root);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not be thrown for identity matrix
 assertTrue(false);
 }
 }

 @Test
 public void testZeroMatrixDecomposition() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{0.0}, {0.0}});
 try {
 RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = decomp.getRootMatrix();
 int rank = decomp.getRank();
 assertEquals(0, rank);
 MatrixUtils.print(System.out, root);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not be thrown for zero matrix
 assertTrue(false);
 }
 }

 @Test
 public void testEmptyMatrixDecomposition() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 // Should throw NonPositiveDefiniteMatrixException
 assertTrue(false);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected for empty matrix
 }
 }

}