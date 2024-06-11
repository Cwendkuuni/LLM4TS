import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

 @Test
 public void testDiagonalWeightCreation() {
 Weight weight = new Weight(new double[]{-1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{-1.0, 0.0, 0.0,
 0.0, 2.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0});

 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightCreationException() {
 Weight weight = new Weight(new double[]{1.0, 2.0, 3.0, 4.0});
 }

 @Test
 public void testSingularValueDecomposition() {
 Weight weight = new Weight(new RealMatrix(new double[][]{
 {1.0, 2.0},
 {2.0, 1.0}
 }));
 RealMatrix expectedWeight = weight.getWeight();
 SingularValueDecomposition svd = new SingularValueDecomposition(expectedWeight);
 RealMatrix actualWeight = svd.getV().transpose().multiply(svd.getSigma().transpose());

 assertEquals(expectedWeight, actualWeight);
 }

 @Test
 public void testGetWeight() {
 Weight weight = new Weight(new double[]{-1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{-1.0, 0.0, 0.0,
 0.0, 2.0, 0.0, 0.0, 0.0, 3.0, 0.0, 0.0, 0.0});
 RealMatrix actualWeight = weight.getWeight();

 assertEquals(expectedWeight, actualWeight);
 }
}