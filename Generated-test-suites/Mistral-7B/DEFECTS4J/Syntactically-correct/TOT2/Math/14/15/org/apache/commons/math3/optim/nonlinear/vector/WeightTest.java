import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}, {1.0}});
        RealMatrix result = w.getWeight();
        assertEquals(expected, result);
    }

    @Test
    public void testWeightMatrix() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight w = new Weight(weightMatrix);
        RealMatrix expected = weightMatrix.copy();
        RealMatrix result = w.getWeight();
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testWeightNullArray() {
        Weight w = new Weight(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightDuplicateValues() {
        double[] weight = {1.0, 2.0, 1.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {1.0, 0.0}});
        RealMatrix result = w.getWeight();
        assertEquals(expected, result);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrix() {
        double[][] weight = {{1.0, 0.0}, {0.0, 1.0}, {1.0, 1.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight w = new Weight(weightMatrix);
    }
}