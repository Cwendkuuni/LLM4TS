import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testNonPositiveDefiniteException() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }

 RealMatrix matrixSmall = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1}, {0.1, 1.0}});
 try {
 new RectangularCholeskyDecomposition(matrixSmall, 0.0);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 1.0, 1.0}, {1.0, 2.0, 1.0}, {1.0, 1.0, 3.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 Assert.assertEquals(3, rank);

 RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0 / Precision.SAFE_EPSILON, 0.0, 0.0},
 {-0.7142857142857143, 0.408248290463863, 0.0},
 {-0.3535533905945456, -0.2316616393659775, 1.0}});
 MatrixUtils.assertEquals(expected, rootMatrix);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not reach here
 }
}

 @Test
 public void testDecompositionIdentity() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 Assert.assertEquals(2, rank);

 RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
 MatrixUtils.assertEquals(expected, rootMatrix);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not reach here
 }
}

 @Test
 public void testDecompositionDiagonal() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 Assert.assertEquals(2, rank);

 RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.543080634815281}});
 MatrixUtils.assertEquals(expected, rootMatrix);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not reach here
 }
}

 @Test
 public void testSwapRows() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 Assert.assertEquals(2, rank);

 RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
 MatrixUtils.assertEquals(expected, rootMatrix);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not reach here
 }
}

 @Test
 public void testNegativeDiagonal() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
}
}