import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 private static final double EPSILON = 1e-12;

 @Test
 public void testConstructorWithPositiveDefiniteMatrix() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix expectedResult = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.4142135625, 0.7071067812}, {0.7071067812, 1.4142135625}});
 Assert.assertEquals("Decomposition matrix is not correct.", expectedResult, decomposition.getRootMatrix());
 Assert.assertEquals("Decomposition rank is not correct.", 2, decomposition.getRank());
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithNonPositiveDefiniteMatrix() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
 new RectangularCholeskyDecomposition(matrix);
 }

 @Test
 public void testConstructorWithSmallEigenvalues() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-15);
 RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
 Assert.assertEquals("Decomposition matrix is not identity.", expectedResult, decomposition.getRootMatrix());
 Assert.assertEquals("Decomposition rank is not correct.", 2, decomposition.getRank());
 }

 @Test
 public void testGetRank() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 Assert.assertEquals("Incorrect decomposition rank.", 2, decomposition.getRank());
 }

 @Test
 public void testGetRootMatrix() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix expectedResult = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.4142135625, 0.7071067812}, {0.7071067812, 1.4142135625}});
 RealMatrix result = decomposition.getRootMatrix();
 Assert.assertEquals("Decomposition matrix is not correct.", expectedResult, result);
 }
}