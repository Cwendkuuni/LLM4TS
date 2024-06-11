import org.apache.commons.math3.optim.initializer.Weight;
import org.apache.commons.math3.optim.initializer.WeightInterface;
import org.apache.commons.math3.optim.initializer.RealMatrixWeight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

 @Test
 public void testWeightDiagonalConstruction() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}
 });
 RealMatrix actualWeight = weight.getWeight();
 assertEquals("Weight matrix should be equal", expectedWeight, actualWeight);
 }

 @Test
 public void testWeightMatrixConstruction() {
 RealMatrix weight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.5}, {0.5, 2.0}
 });
 Weight weightObj = new Weight(weight);
 RealMatrix expectedWeight = weight.copy();
 RealMatrix actualWeight = weightObj.getWeight();
 assertEquals("Weight matrix should be equal", expectedWeight, actualWeight);
 }

 @Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
 public void testWeightMatrixNonSquareConstruction() {
 RealMatrix weight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.5}, {0.5}
 });
 Weight weightObj = new Weight(weight);
 fail("Should throw NonSquareMatrixException");
 }

 @Test
 public void testGetWeight() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}
 });
 RealMatrix actualWeight = weight.getWeight();
 assertEquals("Weight matrix should be equal", expectedWeight, actualWeight);
 }

 @Test
 public void testWeightInterface() {
 Weight weight = new Weight(new double[] {1.0, 2.0, 3.0});
 WeightInterface weightInterface = weight;
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}
 });
 RealMatrix actualWeight = ((RealMatrixWeight) weightInterface).getWeight();
 assertEquals("Weight matrix should be equal", expectedWeight, actualWeight);
 }

 @Test
 public void testRealMatrixWeight() {
 Weight weight = new Weight(new RealMatrixWeight(new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.5}, {0.5, 2.0}
 })));
 RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.5}, {0.5, 2.0}
 });
 RealMatrix actualWeight = weight.getWeight();
 assertEquals("Weight matrix should be equal", expectedWeight, actualWeight);
 }
}