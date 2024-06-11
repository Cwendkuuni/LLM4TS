import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeightTest {

 @Test
 public void testWeightDiagonalConstruction() {
 double[] weights = new double[]{1.0, 2.0, 3.0};
 Weight weightObject = new Weight(weights);
 RealMatrix expectedWeightMatrix = RealMatrixUtils.diagonalMatrix(weights);
 RealMatrix actualWeightMatrix = weightObject.getWeight();
 assertEquals(expectedWeightMatrix, actualWeightMatrix);
 }

 @Test
 public void testWeightSquareMatrixConstruction() {
 RealMatrix weightMatrix = RealMatrixUtils.random(3, 3);
 Weight weightObject = new Weight(weightMatrix);
 RealMatrix expectedWeightMatrix = weightMatrix.copy();
 RealMatrix actualWeightMatrix = weightObject.getWeight();
 assertEquals(expectedWeightMatrix, actualWeightMatrix);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testWeightNonSquareMatrixConstruction() {
 RealMatrix weightMatrix = RealMatrixUtils.random(3, 2);
 Weight weightObject = new Weight(weightMatrix);
 }

 @Test
 public void testWeightGetters() {
 Weight weightObject = new Weight(new double[]{1.0, 2.0, 3.0});
 RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(new double[][]{
         {1.0, 0.0, 0.0},
         {0.0, 2.0, 0.0},
         {0.0, 0.0, 3.0}
 });
 RealMatrix actualWeightMatrix = weightObject.getWeight();
 assertEquals(expectedWeightMatrix, actualWeightMatrix);
 }
}