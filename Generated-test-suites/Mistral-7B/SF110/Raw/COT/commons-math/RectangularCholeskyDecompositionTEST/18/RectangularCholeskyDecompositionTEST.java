import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 private RealMatrix inputMatrix;
 private RealMatrix expectedRootMatrix;
 private RealMatrix actualRootMatrix;

 @Before
 public void setup() {
 inputMatrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 7.0}});
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNonPositiveDefiniteMatrixException() {
 try {
 new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, -1.0}}));
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected exception
 }
 }

 @Test
 public void testRectangularCholeskyDecomposition() {
 try {
 actualRootMatrix = new RectangularCholeskyDecomposition(inputMatrix).getRootMatrix();
 expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{0.57735026856556, 0.0, 0.0}, {-0.816496580927727, 0.57735026856556, 0.0}, {0.4161468365471423, -1.2212453302755076, 0.57735026856556}});
 Assert.assertArrayEquals(expectedRootMatrix.getData(), actualRootMatrix.getData(), 0.00001);
 } catch (NonPositiveDefiniteMatrixException e) {
 // test should not reach here
 }
 }

 @Test
 public void testGetRank() {
 try {
 actualRootMatrix = new RectangularCholeskyDecomposition(inputMatrix).getRootMatrix();
 } catch (NonPositiveDefiniteMatrixException e) {
 // test should not reach here
 }
 Assert.assertEquals(3, new RectangularCholeskyDecomposition(inputMatrix).getRank());
 }

 @Test
 public void testSmallMatrix() {
 try {
 final double small = 1e-10;
 actualRootMatrix = new RectangularCholeskyDecomposition(inputMatrix, small).getRootMatrix();
 expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{0.57735026856556, 0.0, 0.0}, {-0.816496580927727, 0.57735026856556, 0.0}, {0.4161468365471423, -1.2212453302755076, 0.57735026856556}});
 Assert.assertArrayEquals(expectedRootMatrix.getData(), actualRootMatrix.getData(), 0.00001);
 Assert.assertEquals(3, new RectangularCholeskyDecomposition(inputMatrix, small).getRank());
 } catch (NonPositiveDefiniteMatrixException e) {
 // test should not reach here
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNegativeElementOnDiagonal() {
 try {
 new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, -2.0}, {1.0, 2.0, 7.0}}));
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected exception
 }
 }
}