package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFactory;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AmoebaOptimizer;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.optimization.Weight.WeightConstructor.*;

public class WeightTest {

    @Test
    public void testDiagonalWeightMatrix() {
        double[] weight = {1.0, 1.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = RealMatrixFactory.identity(2);
        RealMatrix actualWeight = weightObj.getWeight();

        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testNonDiagonalWeightMatrix() {
        double[] values = {1.0, 0.5, 0.5, 1.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(2, 2, values);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix expectedWeight = weightMatrix.copy();
        RealMatrix actualWeight = weightObj.getWeight();

        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNullWeightMatrix() {
        Weight weightObj = new Weight(null);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testRectangularWeightMatrix() {
        double[] values = {1.0, 0.5, 0.5, 1.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(2, 3, values);
        Weight weightObj = new Weight(weightMatrix);
    }

    @Test
    public void testIdentityWeightMatrix() {
        Weight weightObj = new Weight(DIAGONAL_IDENTITY);
        RealMatrix expectedWeight = RealMatrixFactory.identity(3);
        RealMatrix actualWeight = weightObj.getWeight();

        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testSingularValueDecomposition() {
        double[] values = {1.0, 0.5, 0.5, 1.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(2, 2, values);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix actualWeight = weightObj.getWeight();

        SingularValueDecomposition svd = new SingularValueDecomposition(actualWeight);
        RealMatrix actualSingularValues = svd.getS();
        RealMatrix expectedSingularValues = RealMatrixFactory.identity(2);

        assertEquals(expectedSingularValues, actualSingularValues);
    }

    @Test
    public void testObjectiveFunctionWithIdentityWeight() {
        double[] initialGuess = {1.0, 1.0};
        SimpleBounds bounds = new SimpleBounds(new double[]{-10.0, 10.0}, new double[]{10.0, 10.0});
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(RealVector x) {
                return Math.pow(Math.sin(Math.PI * x.getEntry(0)) + Math.sin(Math.PI * x.getEntry(1)), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        Weight weightObj = new Weight(DIAGONAL_IDENTITY);
        AmoebaOptimizer optimizer = new AmoebaOptimizer();
        PointValuePair solution = optimizer.optimize(objectiveFunction, new InitialGuess(initialGuess), bounds, new MaxEval(500));

        assertEquals(0.0, solution.getPoint().getEntry(0), 0.001);
        assertEquals(0.0, solution.getPoint().getEntry(1), 0.001);
        assertEquals(0.0, solution.getValue(), 0.001);
    }

    @Test
    public void testObjectiveFunctionWithDiagonalWeight() {
        double[] weight = {1.0, 0.1};
        Weight weightObj = new Weight(weight);
        double[] initialGuess = {1.0, 1.0};
        SimpleBounds bounds = new SimpleBounds(new double[]{-10.0, 10.0}, new double[]{10.0, 10.0});
        ObjectiveFunction objectiveFunction = new ObjectiveFunction() {
            @Override
            public double value(RealVector x) {
                return Math.pow(Math.sin(Math.PI * x.getEntry(0)) + Math.sin(Math.PI * x.getEntry(1)), 2);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        AmoebaOptimizer optimizer = new AmoebaOptimizer();
        PointValuePair solution = optimizer.optimize(objectiveFunction, new InitialGuess(initialGuess), bounds, new MaxEval(500), weightObj);

        assertEquals(0.0, solution.getPoint().getEntry(0), 0.001);
        assertEquals(0.0, solution.getPoint().getEntry(1), 0.001);
        assertEquals(0.0, solution.getValue(), 0.001);
    }

    // Add more test cases as needed
}