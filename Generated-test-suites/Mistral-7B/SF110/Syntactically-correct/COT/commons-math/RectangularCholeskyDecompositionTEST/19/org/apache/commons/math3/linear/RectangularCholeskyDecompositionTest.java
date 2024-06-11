import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

 private static final double DELTA = 1e-12;

 @Test
 public void testConstructorWithPositiveDefiniteMatrix() {
 RectangularCholeskyDecomposition decomposition;
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 9.0}});
 try {
 decomposition = new RectangularCholeskyDecomposition(matrix);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected NonPositiveDefiniteMatrixException");
 }
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();
 assertEquals("Unexpected rank", 3, rank);
 assertArrayEquals("Unexpected root matrix", new double[][]{{1.0, 1.0, 1.0}, {1.0, 2.0, 2.0}, {1.0, 2.0, 3.0}}, rootMatrix.getData(), 0.0);
 }

 @Test
 public void testConstructorWithPositiveDefiniteMatrixSmallValue() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 9.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected NonPositiveDefiniteMatrixException");
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithNonPositiveDefiniteMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithNonPositiveDefiniteMatrixSmallValue() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
 try {
 new RectangularCholeskyDecomposition(matrix, 0.0);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testGetRank() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 9.0}});
 RectangularCholeskyDecomposition decomposition;
 try {
 decomposition = new RectangularCholeskyDecomposition(matrix);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected NonPositiveDefiniteMatrixException");
 }
 int rank = decomposition.getRank();
 assertEquals("Unexpected rank", 3, rank);
 }

 @Test
 public void testGetRootMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 9.0}});
 RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 2.0, 2.0}, {1.0, 2.0, 3.0}});
 RectangularCholeskyDecomposition decomposition;
 try {
 decomposition = new RectangularCholeskyDecomposition(matrix);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected NonPositiveDefiniteMatrixException");
 }
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 assertArrayEquals("Unexpected root matrix", expectedRootMatrix.getData(), rootMatrix.getData(), 0.0);
 }

 @Test
 public void testGetRootMatrixWithSmallValue() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 9.0}});
 RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 2.0, 2.0}, {1.0, 2.0, 3.0}});
 RectangularCholeskyDecomposition decomposition;
 try {
 decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected NonPositiveDefiniteMatrixException");
 }
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 assertArrayEquals("Unexpected root matrix", expectedRootMatrix.getData(), rootMatrix.getData(), Precision.EPSILON);
 }
}