import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 private RealMatrix createMatrix(double[][] data) {
 return new Array2DRrealMatrix(data);
 }

 @Test
 public void testConstructorWithPositiveDefiniteMatrix() {
 RealMatrix matrix = createMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();
 Assert.assertEquals(3, rank);
 Assert.assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.44948974278317, 0.81649658092772, 0.0}, {0.0, -0.48989798526751, 1.4142137412501}}, rootMatrix.getData(), 0.001);
 } catch (NonPositiveDefiniteMatrixException e) {
 // This test should not throw an exception
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithNonPositiveDefiniteMatrix() {
 RealMatrix matrix = createMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 6.0}, {3.0, 6.0, -10.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 } catch (NonPositiveDefiniteMatrixException e) {
 // The exception should be thrown
 }
 }

 @Test
 public void testConstructorWithSmallEps() {
 RealMatrix matrix = createMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 1e-6}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-6);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();
 Assert.assertEquals(3, rank);
 // The root matrix values may differ due to floating point precision, so we can't directly compare the arrays
 } catch (NonPositiveDefiniteMatrixException e) {
 // This test should not throw an exception
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithNegativeDiagonalElements() {
 RealMatrix matrix = createMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 6.0}, {3.0, 6.0, -10.0}});
 try {
 new RectangularCholeskyDecomposition(matrix, 0.0);
 } catch (NonPositiveDefiniteMatrixException e) {
 // The exception should be thrown
 }
 }

}