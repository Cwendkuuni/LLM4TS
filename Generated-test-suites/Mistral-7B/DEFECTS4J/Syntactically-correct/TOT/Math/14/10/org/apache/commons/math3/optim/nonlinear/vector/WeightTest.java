import org.apache.commons.math3.optim.initializer.Weight;
import org.apache.commons.math3.optim.initializer.WeightImpl;
import org.apache.commons.math3.optim.nonlinear.function.Add;
import org.apache.commons.math3.optim.nonlinear.function.Constant;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunctionFcn;
import org.apache.commons.math3.optim.nonlinear.function.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.function.PolynomialFunctionFcn;
import org.apache.commons.math3.optim.nonlinear.functions.UnivariateFunction;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.point.RealPointValue;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.optim.InitialValueType.MULTIPLE_X;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        // Test case for the constructor that receives a double array
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(weights.length, 1);
        for (int i = 0; i < weights.length; i++) {
            expectedWeight.setEntry(i, 0, weights[i]);
        }

        RealMatrix actualWeight = weight.getWeight();

        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testWeightMatrixConstruction() {
        // Test case for the constructor that receives a RealMatrix
        double[][] weights = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(weights);

        RealMatrix actualWeight = weight.getWeight();

        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightMatrixNonSquareConstruction() {
        // Test case for the constructor that throws NonSquareMatrixException
        double[][] weights = {{1.0, 0.0}, {0.0, 2.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);
    }

    @Test
    public void testWeightGet() {
        // Test case for the getWeight() method
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(weights.length, 1);
        for (int i = 0; i < weights.length; i++) {
            expectedWeight.setEntry(i, 0, weights[i]);
        }

        RealMatrix actualWeight = weight.getWeight();

        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testWeightMultiX() {
        // Test case for Weight with multiple initial points
        Weight weight = new Weight(new double[]{1.0, 1.0});
        UnivariateFunction f = new Add(new PolynomialFunctionFcn(new LinearFunctionFcn(1.0), new LinearFunctionFcn(-2.0)), new Constant(3.0));
        PointValuePair initialGuess = new RealPointValue(new double[]{4.0, 4.0});
        PointValuePair result = new OptimizationProblem(f, MULTIPLE_X, null, new WeightImpl(weight)).optimize();

        RealMatrix expectedWeight = weight.getWeight();
        RealMatrix actualWeight = result.getPoint().getMatrix();

        assertEquals(expectedWeight, actualWeight);
    }
}