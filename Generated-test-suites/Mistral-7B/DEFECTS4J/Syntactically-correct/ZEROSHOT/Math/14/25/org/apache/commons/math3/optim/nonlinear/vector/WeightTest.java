import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertArrayEquals;

public class WeightTest {

 @Test
 public void testDiagonalWeightConstruction() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}
 });

 assertEquals(expectedWeight, weight.getWeight());
 }

 @Test
 public void testDiagonalWeightEquals() {
 Weight weight1 = new Weight(new double[] {1.0, 2.0, 3.0});
 Weight weight2 = new Weight(new double[] {1.0, 2.0, 3.0});

 assertEquals(weight1, weight2);
 }

 @Test
 public void testDiagonalWeightNotEquals() {
 Weight weight1 = new Weight(new double[] {1.0, 2.0, 3.0});
 Weight weight2 = new Weight(new double[] {1.0, 3.0, 2.0});

 assertNotEquals(weight1, weight2);
 }

 @Test
 public void testDiagonalWeightDeterminant() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});

 assertEquals(1.0 * 2.0 * 3.0, weight.getWeight().determine(), 1e-14);
 }

 @Test
 public void testDiagonalWeightInverse() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedInverse = new Array2DRowRealMatrix(new double[][] {
 {2.0, -2.0, 1.0/3.0},
 {-2.0, 1.0, -1.0/2.0},
 {1.0/3.0, -1.0/2.0, 1.0/6.0}
 });

 RealMatrix inverse = weight.getWeight().getInverse();
 assertArrayEquals(expectedInverse.getData(), inverse.getData(), 1e-14);
 }

 @Test
 public void testDiagonalWeightMultiplyVector() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealVector vector = MatrixUtils.createRealVector(new double[] {1.0, 2.0, 3.0});
 RealVector result = weight.getWeight().operate(vector);

 assertArrayEquals(new double[] {1.0, 2.0, 3.0}, result.toArray(), 1e-14);
 }

 @Test
 public void testNonDiagonalWeightConstruction() {
 double[][] weights = {{1.0, 0.5}, {0.5, 2.0}};
 Weight weight = new Weight(new Array2DRowRealMatrix(weights));
 RealMatrix expectedWeight = new Array2DRowRealMatrix(weights);

 assertEquals(expectedWeight, weight.getWeight());
 }

 @Test
 public void testNonDiagonalWeightEquals() {
 Weight weight1 = new Weight(new Array2DRowRealMatrix(new double[][] {{1.0, 0.5}, {0.5, 2.0}}));
 Weight weight2 = new Weight(new Array2DRowRealMatrix(new double[][] {{1.0, 0.5}, {0.5, 2.0}}));

 assertEquals(weight1, weight2);
 }

 @Test
 public void testNonDiagonalWeightNotEquals() {
 Weight weight1 = new Weight(new Array2DRowRealMatrix(new double[][] {{1.0, 0.5}, {0.5, 2.0}}));
 Weight weight2 = new Weight(new Array2DRowRealMatrix(new double[][] {{1.0, 0.6}, {0.6, 2.0}}));

 assertNotEquals(weight1, weight2);
 }

 @Test
 public void testNonDiagonalWeightDeterminant() {
 Weight weight = new Weight(new Array2DRowRealMatrix(new double[][] {{1.0, 0.5}, {0.5, 2.0}}));
 SingularValueDecomposition svd = weight.getWeight().decomposeSVD();
 assertEquals(1.0/Math.sqrt(2.0), svd.getSingularValues()[0], 1e-14);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightConstruction() {
 new Weight(new Array2DRowRealMatrix(new double[][] {{1.0, 0.5}, {0.5, 2.0}, {0.0, 3.0}}));
 }
}