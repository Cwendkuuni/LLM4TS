import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.linear.RealVectorWeight;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

 @Test
 public void testDiagonalWeightConstruction() {
 double[] weight = {1.0, 2.0, 3.0};
 Weight w = new Weight(weight);
 RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
 {0.0, 2.0, 0.0},
 {0.0, 0.0, 3.0}});
 RealMatrix actual = w.getWeight();
 assertEquals(expected, actual);
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareWeightConstruction() {
 double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
 Weight w = new Weight(new Array2DRowRealMatrix(weight));
 }

 @Test
 public void testSquareWeightConstruction() {
 RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
 {0.0, 2.0}});
 Weight w = new Weight(weight);
 RealMatrix expected = weight.copy();
 RealMatrix actual = w.getWeight();
 assertEquals(expected, actual);
 }
}