import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testPositiveDefiniteMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 4.0}, {1.0, 4.0, 6.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 assertEquals(3, rank);
 assertEquals(3, root.getRowDimension());
 assertEquals(rank, root.getColumnDimension());

 // Check that the lower triangular matrix is correct
 for (int i = 0; i < rank; i++) {
 for (int j = 0; j < i; j++) {
 assertEquals(0, root.getEntry(i, j), Precision.SAFE_EPSILON);
 }
 for (int j = i; j < rank; j++) {
 double expected = MathUtils.sqrt(matrix.getEntry(i, i));
 assertEquals(expected, root.getEntry(i, j), Precision.SAFE_EPSILON);
 }
 }
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not be thrown
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNonPositiveDefiniteMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0, 1.0}, {2.0, -5.0, 4.0}, {1.0, 4.0, -6.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 assertEquals(-1.0, e.getValue(), Precision.SAFE_EPSILON);
 assertEquals(0, e.getRow());
 assertEquals(0.0, e.getThreshold(), Precision.SAFE_EPSILON);
 }
 }

 @Test
 public void testIdentityMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 assertEquals(2, rank);
 assertEquals(2, root.getRowDimension());
 assertEquals(rank, root.getColumnDimension());

 // Check that the lower triangular matrix is identity
 for (int i = 0; i < rank; i++) {
 for (int j = 0; j < rank; j++) {
 if (i == j) {
 assertEquals(1.0, root.getEntry(i, j), Precision.SAFE_EPSILON);
 } else {
 assertEquals(0.0, root.getEntry(i, j), Precision.SAFE_EPSILON);
 }
 }
 }
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not be thrown
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testRankOneMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 assertEquals(1, rank);
 assertEquals(2, root.getRowDimension());
 assertEquals(rank, root.getColumnDimension());

 // Check that the lower triangular matrix is correct
 assertEquals(1.0, root.getEntry(0, 0), Precision.SAFE_EPSILON);
 assertEquals(1.0 / Math.sqrt(2.0), root.getEntry(0, 1), Precision.SAFE_EPSILON);
 assertEquals(1.0 / Math.sqrt(2.0), root.getEntry(1, 0), Precision.SAFE_EPSILON);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not be thrown
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSingularMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 assertEquals(0.0, e.getValue(), Precision.SAFE_EPSILON);
 assertEquals(0, e.getRow());
 assertEquals(0.0, e.getThreshold(), Precision.SAFE_EPSILON);
 }
 }
}