import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WeightTest {

 @Test
 public void testWeightDiagonalConstruction() {
 Weight weight = new Weight(new double[]{1.0, 2.0, 3.0});
 RealMatrix expected = new Array2DRowRealMatrix(3, 3, new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
 assertEquals(expected, weight.getWeight());
 }

 @Test
 public void testWeightMatrixConstruction() {
 Weight weight = new Weight(MatrixUtils.denseMatrix(new double[][]{{1.0, 0.2}, {0.2, 3.0}}));
 RealMatrix expected = new Array2DRowRealMatrix(2, 2, new double[][]{{1.0, 0.2}, {0.2, 3.0}});
 assertEquals(expected, weight.getWeight());
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testWeightMatrixNonSquareConstruction() {
 Weight weight = new Weight(MatrixUtils.denseMatrix(new double[][]{{1.0, 0.2}, {0.2, 3.0}, {0.3, 4.0}}));
 }

 @Test
 public void testGetWeight() {
 Weight weight = new Weight(MatrixUtils.denseMatrix(new double[][]{{1.0, 0.2}, {0.2, 3.0}}));
 RealMatrix expected = new Array2DRowRealMatrix(2, 2, new double[][]{{1.0, 0.2}, {0.2, 3.0}});
 RealMatrix actual = weight.getWeight();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetWeightEmpty() {
 Weight weight = new Weight(new Weight(MatrixUtils.createRealMatrix(1, 1, 0.0)));
 RealMatrix actual = weight.getWeight();
 assertNull(actual);
 }

 @Test
 public void testSingularValueDecomposition() {
 Weight weight = new Weight(MatrixUtils.denseMatrix(new double[][]{{1.0, 0.2}, {0.2, 3.0}}));
 SingularValueDecomposition svd = new SingularValueDecomposition(weight.getWeight());
 RealMatrix expectedDiagonal = new Array2DRowRealMatrix(2, 2, new double[][]{{1.0, 0.0}, {0.0, 3.0}});
 RealMatrix actualDiagonal = svd.getDiagonal();
 assertEquals(expectedDiagonal, actualDiagonal);
 }

 @Test
 public void testSingularValueDecompositionDiagonal() {
 Weight weight = new Weight(new double[]{1.0, 3.0});
 RealMatrix expectedDiagonal = new Array2DRowRealMatrix(2, 2, new double[][]{{1.0, 0.0}, {0.0, 3.0}});
 SingularValueDecomposition svd = new SingularValueDecomposition(weight.getWeight());
 RealMatrix actualDiagonal = svd.getDiagonal();
 assertEquals(expectedDiagonal, actualDiagonal);
 }
}