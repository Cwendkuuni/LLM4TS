import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeightTest {

 @Test
 public void testDiagonalWeightMatrix() {
 double[] weight = {1.0, 0.5, 2.0};
 Weight w = new Weight(weight);
 RealMatrix weightMatrix = w.getWeight();
 assertEquals(3, weightMatrix.getRowDimension());
 assertEquals(3, weightMatrix.getColumnDimension());
 for (int i = 0; i < weight.length; i++) {
 assertEquals(weight[i], weightMatrix.getEntry(i, i), 1e-10);
 }
 }

 @Test
 public void testNonDiagonalWeightMatrix() {
 double[][] weight = {{1.0, 0.5}, {0.5, 2.0}};
 Weight w = new Weight(new Array2DRowRealMatrix(weight));
 RealMatrix weightMatrix = w.getWeight();
 assertEquals(weight.length, weightMatrix.getRowDimension());
 assertEquals(weight[0].length, weightMatrix.getColumnDimension());
 for (int i = 0; i < weight.length; i++) {
 for (int j = 0; j < weight[i].length; j++) {
 assertEquals(weight[i][j], weightMatrix.getEntry(i, j), 1e-10);
 }
 }
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightMatrix() {
 double[][] weight = {{1.0, 0.5}, {0.5, 2.0}, {1.0, 1.0}};
 Weight w = new Weight(new Array2DRowRealMatrix(weight));
 }

 @Test
 public void testGetWeight() {
 Weight w = new Weight(new MaxEval(new SimpleGoalType(), new LinearConstraint(), new PointValuePair(new double[]{1.0, 1.0}, 1.0)));
 RealMatrix weightMatrix = w.getWeight();
 assertNotNull(weightMatrix);
 }
}