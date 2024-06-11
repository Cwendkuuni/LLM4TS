import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testConstructWithPositiveDefiniteMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.4472135625, 1.0, 0.0}, {0.1414213741, 0.7313150551, 1.0}});
 RealMatrix root = decomposition.getRootMatrix();
 assertEquals(expectedRoot, root);
 assertEquals(3, decomposition.getRank());
 }

 @Test(expected = org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException.class)
 public void testConstructWithNonPositiveDefiniteMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 0.0}, {3.0, 0.0, -1.0}});
 new RectangularCholeskyDecomposition(matrix);
 }

 @Test
 public void testConstructWithSmallEigenvalues() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 0.5, 4.0}, {3.0, 4.0, 0.1}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
 RealMatrix root = decomposition.getRootMatrix();
 assertEquals(3, root.getRowDimension());
 assertEquals(3, root.getColumnDimension());
 for (int i = 0; i < 3; i++) {
 for (int j = 0; j < 3; j++) {
 double expected = (i <= j) ? Math.sqrt(matrix.getEntry(i, i)) : 0.0;
 assertEquals(Precision.isClose(expected, root.getEntry(i, j), 1e-6), true);
 }
 }
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Should not throw exception with small eigenvalues.");
 }
 }

 @Test
 public void testConstructWithSingularMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 0.0}, {3.0, 0.0, 0.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 fail("Should throw exception with singular matrix.");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

}