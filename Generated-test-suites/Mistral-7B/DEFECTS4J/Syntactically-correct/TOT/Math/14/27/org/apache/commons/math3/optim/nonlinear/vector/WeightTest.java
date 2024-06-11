import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.nonlinear.LinearConstraints;
import org.apache.commons.math3.optim.nonlinear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.LinearRegressionData;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.optim.PointValuePair.Pair;
import static org.apache.commons.math3.optim.linear.LinearObjectiveFunction.FunctionType.QUADRATIC;

/**
 * Tests for the Weight class.
 */
public class WeightTest {

    @Test
    public void testWeightDiagonal() {
        // Given
        double[] weight = {1.0, 1.0};
        Weight w = new Weight(weight);

        // When
        RealMatrix actual = w.getWeight();

        // Then
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightSquare() {
        // Given
        double[][] weight = {{1.0, 0.5}, {0.5, 1.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight w = new Weight(weightMatrix);

        // When
        RealMatrix actual = w.getWeight();

        // Then
        RealMatrix expected = weightMatrix;
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightNonSquare() {
        // Given
        double[][] weight = {{1.0, 0.5}, {0.5, 1.0}, {0.5, 1.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);

        // When
        Weight w = new Weight(weightMatrix);

        // Then
        // The exception should be thrown here
    }

    @Test
    public void testWeightGetCopy() {
        // Given
        double[] weight = {1.0, 1.0};
        Weight w = new Weight(weight);

        // When
        RealMatrix actual = w.getWeight();
        RealMatrix copy = actual.copy();

        // Then
        assertEquals(actual, copy);
    }
}