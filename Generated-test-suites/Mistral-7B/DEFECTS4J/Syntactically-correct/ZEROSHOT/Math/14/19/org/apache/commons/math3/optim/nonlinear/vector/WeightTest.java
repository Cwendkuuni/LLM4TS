import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeightTest {

 @Test
 public void testWeightDiagonalConstruction() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}
 });

 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 @Test
 public void testWeightMatrixConstruction() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.5},
 {0.5, 1.5}
 });
 Weight weight = new Weight(weightMatrix);
 RealMatrix expectedWeight = weightMatrix.copy();

 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testWeightMatrixNonSquareConstruction() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.5},
 {0.5, 1.5},
 {0.0, 2.0}
 });
 Weight weight = new Weight(weightMatrix);
 }

 @Test
 public void testGetWeight() {
 Weight weight = new Weight(new double[] {0.1, 0.2, 0.3});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
 {0.1, 0.0, 0.0},
 {0.0, 0.2, 0.0},
 {0.0, 0.0, 0.3}
 });
 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);

 assertNotNull(actualWeight);
 }
}