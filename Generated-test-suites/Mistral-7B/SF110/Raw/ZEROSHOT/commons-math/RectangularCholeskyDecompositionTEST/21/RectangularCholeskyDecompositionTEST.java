import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testValidDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 0.0}, {2.0, 2.0, 1.0}, {0.0, 1.0, 2.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 // Check the root matrix is correct
 Assert.assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 0.4226183547595517, 0.0}, {0.0, -0.4226183547595517, 1.4142135623730955}}, rootMatrix.getData(), 0.001);

 // Check the rank is correct
 Assert.assertEquals(3, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 // This exception should not be thrown
 Assert.fail("NonPositiveDefiniteMatrixException should not be thrown");
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testInvalidDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 0.0}, {2.0, 2.0, 1.0}, {0.0, 1.0, -1.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 Assert.fail("NonPositiveDefiniteMatrixException should be thrown");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }

 @Test
 public void testRankLessThanExpected() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 0.0}, {2.0, 2.0, 1.0}, {0.0, 1.0, 0.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 // Check the root matrix is correct
 Assert.assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 0.4226183547595517, 0.0}}, rootMatrix.getData(), 0.001);

 // Check the rank is less than expected
 Assert.assertEquals(2, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 // This exception should not be thrown
 Assert.fail("NonPositiveDefiniteMatrixException should not be thrown");
 }
 }
}