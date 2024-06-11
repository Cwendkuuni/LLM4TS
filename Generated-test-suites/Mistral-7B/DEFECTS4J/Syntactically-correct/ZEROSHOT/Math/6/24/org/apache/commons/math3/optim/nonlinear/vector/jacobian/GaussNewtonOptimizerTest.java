import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplePointValuePair;
import org.apache.commons.math3.optim.unconstrained.GoalType;
import org.apache.commons.math3.optim.unconstrained.ObjectiveFunction;
import org.apache.commons.math3.optim.unconstrained.linear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.unconstrained.nonlinear.LinearConstraint;
import org.apache.commons.math3.optim.unconstrained.nonlinear.LinearConstraintSet;
import org.apache.commons.math3.optim.unconstrained.nonlinear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.unconstrained.nonlinear.linear.GaussNewtonOptimizerTest;
import org.apache.commons.math3.optim.unconstrained.linear.LSEObjectiveFunction;
import org.apache.commons.math3.optim.unconstrained.linear.LSEObjectiveFunctionTestData;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {
    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());

        ObjectiveFunction objectiveFunction = new LSEObjectiveFunction(
                new LSEObjectiveFunctionTestData(3, 3, 1.0, 1000.0));
        double[] startPoint = new double[]{1.0, 1.0, 1.0};
        PointValuePair solution = optimizer.optimize(objectiveFunction, startPoint, GoalType.MINIMIZE, null);

        assertNotNull(solution);
        assertEquals(GoalType.MINIMIZE, solution.getGoalType(), 0);
        assertEquals(3, solution.getPoint().getDimension(), 0);
        assertEquals(1.0, solution.getValue(), 1e-14);

        RealMatrix jacobian = optimizer.getFunctionValue(solution.getPoint(), objectiveFunction).getJacobian();
        assertEquals(3, jacobian.getRowDimension(), 0);
        assertEquals(3, jacobian.getColumnDimension(), 0);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullObjectiveFunction() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        optimizer.optimize(null, new double[]{0.0}, GoalType.MINIMIZE, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        optimizer.optimize(new LSEObjectiveFunction(new LSEObjectiveFunctionTestData(3, 3, 1.0, 1000.0)), null, GoalType.MINIMIZE, null);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullCheckPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        optimizer.optimize(new LSEObjectiveFunction(new LSEObjectiveFunctionTestData(3, 3, 1.0, 1000.0)), new double[]{0.0}, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGoalType() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        optimizer.optimize(new LSEObjectiveFunction(new LSEObjectiveFunctionTestData(3, 3, 1.0, 1000.0)), new double[]{0.0}, GoalType.MAXIMIZE, null);
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(new SimpleVectorValueChecker());
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new ArrayRealVector(new double[]{1.0, 1.0}), 2.0, null));
        OptimizationProblem problem = new OptimizationProblem(new LinearObjectiveFunction(new double[][]{new double[]{1.0, 1.0}, new double[]{1.0, 1.0}}, new double[]{2.0, 2.0}), null, constraints);
        optimizer.optimize(problem, new double[]{0.0}, GoalType.MINIMIZE, null);
    }
}