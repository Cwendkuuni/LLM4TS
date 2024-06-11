import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testValidDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 7.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.0 / Precision.sqrt(3.0), 0.0, -1.0 / Precision.sqrt(2.0)},
 {-1.0 / (2.0 * Precision.sqrt(5.0)), 1.0 / Precision.sqrt(5.0), -0.5 / Precision.sqrt(5.0)},
 {0.0, -0.5 / Precision.sqrt(7.0), 1.0 / Precision.sqrt(7.0)}});

 RealMatrix result = decomposition.getRootMatrix();
 RealMatrixUtils.configurePrint(System.out, 3, 3);
 Assert.assertEquals("Unexpected Cholesky decomposition result.", expectedResult, result);
 }

 @Test
 public void testInvalidDecompositionSmall() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 0.5}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
 Assert.fail("A NonPositiveDefiniteMatrixException should be thrown.");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }

 @Test
 public void testInvalidDecompositionNegative() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, -7.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 Assert.fail("A NonPositiveDefiniteMatrixException should be thrown.");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }

 @Test
 public void testRank() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 7.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 Assert.assertEquals("Unexpected rank.", 3, decomposition.getRank());
 }

 @Test
 public void testRankWithNegativeElements() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, -7.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 Assert.fail("A NonPositiveDefiniteMatrixException should be thrown.");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }

 @Test
 public void testRankWithZeroDiagonalElement() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 0.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
 Assert.fail("A NonPositiveDefiniteMatrixException should be thrown.");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }

 @Test
 public void testRankWithSmallDiagonalElement() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 0.5}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
 Assert.assertEquals("Unexpected rank.", 3, decomposition.getRank());
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }
}