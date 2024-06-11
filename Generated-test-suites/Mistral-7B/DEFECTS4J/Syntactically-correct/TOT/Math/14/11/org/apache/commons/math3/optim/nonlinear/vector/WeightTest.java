import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.initializer.AroundMinimum;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.AbstractScalarFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.LinearConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.NonNegativityConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.BoundedValueFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.objective.WeighedSum;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet.Constraint;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction.ConstraintValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearObjectiveFunction.LinearConstraintSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LQR;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.QPOases;
import org.apache.commons.math3.optim.nonlinear.scalar.linear.LinearConstraintSet.DimensionMismatchException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix weightExpected = new Array2DRowRealMatrix(3, 3);
        weightExpected.setEntry(0, 0, weight[0]);
        weightExpected.setEntry(1, 1, weight[1]);
        weightExpected.setEntry(2, 2, weight[2]);

        RealMatrix weightActual = weightObj.getWeight();
        assertEquals(weightExpected, weightActual);
    }

    @Test
    public void testWeightSquareMatrixConstruction() {
        double[][] weight = {{1.0, 1.0}, {1.0, 1.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
        RealMatrix weightExpected = new Array2DRowRealMatrix(2, 2);
        weightExpected.setEntry(0, 0, weight[0][0]);
        weightExpected.setEntry(0, 1, weight[0][1]);
        weightExpected.setEntry(1, 0, weight[1][0]);
        weightExpected.setEntry(1, 1, weight[1][1]);

        RealMatrix weightActual = weightObj.getWeight();
        assertEquals(weightExpected, weightActual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixConstruction() {
        double[][] weight = {{1.0, 1.0}, {1.0}};
        new Weight(new Array2DRowRealMatrix(weight));
    }

    @Test
    public void testWeightGetWeight() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix weightExpected = new Array2DRowRealMatrix(3, 3);
        weightExpected.setEntry(0, 0, weight[0]);
        weightExpected.setEntry(1, 1, weight[1]);
        weightExpected.setEntry(2, 2, weight[2]);

        RealMatrix weightActual = weightObj.getWeight();
        assertEquals(weightExpected, weightActual);
    }

    @Test
    public void testWeightClone() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        Weight weightClone = weightObj.getWeight().clone();

        RealMatrix weightExpected = new Array2DRowRealMatrix(3, 3);
        weightExpected.setEntry(0, 0, weight[0]);
        weightExpected.setEntry(1, 1, weight[1]);
        weightExpected.setEntry(2, 2, weight[2]);

        RealMatrix weightActual = weightClone.getWeight();
        assertEquals(weightExpected, weightActual);
    }
}