import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testValidDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 4.0}});
 try {
 RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = rcd.getRootMatrix();
 int rank = rcd.getRank();

 // Check that the decomposition was successful and the root matrix is correct
 Assert.assertEquals(3, rank);
 Assert.assertEquals(3, root.getRowDimension());
 Assert.assertEquals(rank, root.getColumnDimension());

 // Check that the Cholesky matrix is symmetric
 for (int i = 0; i < rank; i++) {
 for (int j = 0; j <= i; j++) {
 Assert.assertEquals(Precision.DEFAULT_EPSILON, root.getEntry(i, j), root.getEntry(j, i), 0.0);
 }
 }
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not reach this point
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testInvalidDecomposition_NegativeValueOnDiagonal() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, -2.0}, {1.0, 2.0, 4.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 Assert.fail();
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testInvalidDecomposition_ZeroOnDiagonal() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 0.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 Assert.fail();
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testInvalidDecomposition_NegativeDeterminant() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, -4.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 Assert.fail();
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testGetRank() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 4.0}});
 try {
 RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
 int rank = rcd.getRank();
 Assert.assertEquals(3, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not reach this point
 }
 }

 @Test
 public void testGetRootMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 4.0}});
 try {
 RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = rcd.getRootMatrix();

 // Check that the root matrix has the correct size
 Assert.assertEquals(3, root.getRowDimension());
 Assert.assertEquals(3, root.getColumnDimension());
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not reach this point
 }
 }

 @Test
 public void testEquals() {
 RealMatrix matrix1 = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 4.0}});
 RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 4.0}});
 try {
 RectangularCholeskyDecomposition rcd1 = new RectangularCholeskyDecomposition(matrix1);
 RectangularCholeskyDecomposition rcd2 = new RectangularCholeskyDecomposition(matrix2);

 // Check that the decompositions are equal
 Assert.assertEquals(rcd1, rcd2);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not reach this point
 }
 }

 @Test
 public void testNotEquals() {
 RealMatrix matrix1 = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 4.0}});
 RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, -2.0}, {1.0, 2.0, 4.0}});
 try {
 RectangularCholeskyDecomposition rcd1 = new RectangularCholeskyDecomposition(matrix1);
 RectangularCholeskyDecomposition rcd2 = new RectangularCholeskyDecomposition(matrix2);

 // Check that the decompositions are not equal
 Assert.assertNotEquals(rcd1, rcd2);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not reach this point
 }
 }

}