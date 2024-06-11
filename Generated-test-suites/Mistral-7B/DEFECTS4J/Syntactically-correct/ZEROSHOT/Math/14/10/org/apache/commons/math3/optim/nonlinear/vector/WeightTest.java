import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixTestCase;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WeightTest {

 @Test
 public void testDiagonalWeightMatrixCreation() {
 RealMatrix weightMatrix;
 double[] weight = {1.0, 2.0, 3.0};
 Weight weightObj = new Weight(weight);
 weightMatrix = weightObj.getWeight();

 assertEquals(3, weightMatrix.getRowDimension());
 assertEquals(3, weightMatrix.getColumnDimension());

 for (int i = 0; i < weight.length; i++) {
 assertEquals(weight[i], weightMatrix.getEntry(i, i), 0.0);
 assertNull(weightMatrix.getEntry(i, i - 1));
 assertNull(weightMatrix.getEntry(i, i + 1));
 }
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightMatrixCreation() {
 Weight weightObj = new Weight(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
 }

 @Test
 public void testSquareWeightMatrixCreation() {
 RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3);
 weightMatrix.setEntry(0, 0, 1.0);
 weightMatrix.setEntry(1, 1, 2.0);
 weightMatrix.setEntry(2, 2, 3.0);
 Weight weightObj = new Weight(weightMatrix);

 assertEquals(weightMatrix, weightObj.getWeight());
 }

 @Test
 public void testGetWeightNull() {
 Weight weightObj = new Weight(null);
 assertNull(weightObj.getWeight());
 }
}