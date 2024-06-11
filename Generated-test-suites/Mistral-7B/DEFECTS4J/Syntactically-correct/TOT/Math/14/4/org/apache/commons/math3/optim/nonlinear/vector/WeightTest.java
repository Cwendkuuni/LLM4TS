import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;
import org.junit.Assert.*;

public class WeightTest {

 @Test
 public void testDiagonalWeightMatrix() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}});

 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 @Test
 public void testZeroDiagonalWeightMatrix() {
 Weight weight = new Weight(new double[] {0.0, 0.0, 0.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{{0.0, 0.0, 0.0},
 {0.0, 0.0, 0.0},
 {0.0, 0.0, 0.0}});

 RealMatrix actualWeight = weight.getWeight();
 assertEquals(expectedWeight, actualWeight);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightMatrix() {
 Weight weight = new Weight(new double[] {1.0, 2.0});
 }

 @Test
 public void testCopyWeightMatrix() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}});

 RealMatrix actualWeight = weight.getWeight();
 RealMatrix copyWeight = weight.getWeight().copy();

 assertEquals(expectedWeight, actualWeight);
 assertEquals(expectedWeight, copyWeight);
 }

 @Test
 public void testSingularValueDecomposition() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix weightMatrix = weight.getWeight();
 SingularValueDecomposition svd = new SingularValueDecomposition(weightMatrix);
 RealMatrix expectedS = svd.getS();

 RealMatrix actualS = weightMatrix.copy();
 svd.transform(actualS);

 assertEquals(expectedS, actualS);
 }
}