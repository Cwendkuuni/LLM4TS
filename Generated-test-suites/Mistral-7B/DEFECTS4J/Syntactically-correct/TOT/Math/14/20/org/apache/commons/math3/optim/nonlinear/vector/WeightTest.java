import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonRidderOptimizer;
import org.apache.commons.math3.optim.nonlinear.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.linear.RealMatrix;
import org.apache.commons.math3.optim.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    /**
     * Test constructor with a diagonal weight matrix.
     */
    @Test
    public void testDiagonalWeightMatrixConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);

        RealMatrix actualWeight = weight.getWeight();

        assertTrue(actualWeight.equals(expectedWeight));
    }

    /**
     * Test constructor with a non-diagonal weight matrix.
     */
    @Test
    public void testNonDiagonalWeightMatrixConstruction() {
        double[][] weight = {{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix inputWeight = new Array2DRowRealMatrix(weight);
        Weight weight = new Weight(inputWeight);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(2, 2);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(0, 1, 0.5);
        expectedWeight.setEntry(1, 0, 0.5);
        expectedWeight.setEntry(1, 1, 2.0);

        RealMatrix actualWeight = weight.getWeight();

        assertTrue(actualWeight.equals(expectedWeight));
    }

    /**
     * Test exception when trying to create a weight matrix with a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] weight = {{1.0, 0.5}, {0.5, 2.0}, {3.0, 4.0}};
        RealMatrix inputWeight = new Array2DRowRealMatrix(weight);
        new Weight(inputWeight);
    }
}