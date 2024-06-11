import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testValidDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 0.0}, {2.0, 4.0, 2.0}, {0.0, 2.0, 3.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();
 assertEquals(3, rank);
 assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {-1.0, 1.0, 0.0}, {-0.5, -1.0, 1.0}}, rootMatrix.getData(), 0.01);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSmallMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 2.0}, {2.0, 2.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-15);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();
 assertEquals(2, rank);
 assertArrayEquals(new double[][]{{1.0, 0.0}, {-1.0, 1.0}}, rootMatrix.getData(), 0.01);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testNonPositiveDefiniteMatrixException() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testNonPositiveDefiniteMatrixRank() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, -1.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 int rank = decomposition.getRank();
 assertEquals(1, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testSwapRows() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 0.0}, {2.0, 4.0, 2.0}, {0.0, 2.0, 3.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();
 // Swap the first and third rows
 double[][] data = rootMatrix.getData();
 double temp = data[0];
 data[0] = data[2];
 data[2] = temp;
 RealMatrix swappedRootMatrix = MatrixUtils.createRealMatrix(3, 3, data);
 assertArrayEquals(swappedRootMatrix.getData(), rootMatrix.getData(), 0.01);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testRowWithZeroDiagonalElement() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 0.0}, {2.0, 4.0, 2.0}, {0.0, 2.0, 3.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testRowWithNegativeDiagonalElement() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 0.0}, {2.0, 4.0, 2.0}, {0.0, 2.0, -3.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }
}