import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class RectangularCholeskyDecompositionTest {

 private static final double EPSILON = 1e-9;

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithNonPositiveDefiniteMatrix() {
  RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2, -1}, {-1, 2}});
  new RectangularCholeskyDecomposition(matrix);
 }

 @Test
 public void testConstructorWithPositiveDefiniteMatrix() {
  RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3, 2}, {2, 3}});
  RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
  RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.7320508075688772, 0.0}, {0.0, 1.4142137412643376}});
  assertEquals(expected, decomposition.getRootMatrix());
  assertEquals(2, decomposition.getRank());
 }

 @Test
 public void testConstructorWithSmallMatrix() {
  RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1e-10}});
  RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-9);
  RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
  assertEquals(expected, decomposition.getRootMatrix());
  assertEquals(2, decomposition.getRank());
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithNonPositiveDefiniteMatrixAndSmall() {
  RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2, -1}, {-1, 2}});
  new RectangularCholeskyDecomposition(matrix, 1e-9);
 }

 @Test
 public void testGetRank() {
  RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3, 2}, {2, 3}});
  RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
  assertEquals(2, decomposition.getRank());
 }

 @Test
 public void testGetRootMatrix() {
  RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3, 2}, {2, 3}});
  RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
  RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.7320508075688772, 0.0}, {0.0, 1.4142137412643376}});
  assertEquals(expected, decomposition.getRootMatrix());
 }
}