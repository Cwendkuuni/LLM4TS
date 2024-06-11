import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationTest;
import org.apache.commons.math3.optimization.general.GaussNewtonOptimizer;
import org.apache.commons.math3.optimization.linear.*;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1e-5, 1e-5));

        RealMatrix weightMatrix = new IdentityMatrix(3);
        LinearConstraint[] constraints = new LinearConstraint[0];
        double[] initialValues = {1.0, 2.0, 3.0};
        LinearConstraint[] bounds = new LinearConstraint[0];

        OptimizationProblem problem = new LeastSquaresProblem(1, 3,
                new QuadraticObjectiveFunction(-1, new QuadraticForm(new DiagonalMatrix(new double[]{1.0, 2.0, 3.0}))),
                initialValues, constraints, bounds, weightMatrix);

        PointValuePair solution = optimizer.optimize(problem);

        RealVector expectedSolution = new ArrayRealVector(new double[]{0.0, 0.0, 0.0});
        double expectedCost = 0.0;
        assertEquals(expectedSolution, solution.getPoint());
        assertEquals(expectedCost, solution.getValue(), 1e-5);

        // Test with different initial values
        optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1e-5, 1e-5));
        double[] initialValues2 = {4.0, 5.0, 6.0};
        problem = new LeastSquaresProblem(1, 3,
                new QuadraticObjectiveFunction(-1, new QuadraticForm(new DiagonalMatrix(new double[]{1.0, 2.0, 3.0}))),
                initialValues2, constraints, bounds, weightMatrix);

        PointValuePair solution2 = optimizer.optimize(problem);

        expectedSolution = new ArrayRealVector(new double[]{-2.1216246259455537E-2, -9.4868373876181344E-3, 1.1837010973134322E-1});
        expectedCost = 3.3333333333333335E-3;
        assertArrayEquals(expectedSolution.toArray(), solution2.getPoint(), 1e-5);
        assertEquals(expectedCost, solution2.getValue(), 1e-5);

        // Test singular matrix exception
        optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1e-5, 1e-5));
        LinearConstraint[] constraintsSingular = new LinearConstraint[]{new LinearConstraint(new Equality(0.0), 1)};
        double[] initialValuesSingular = {1.0, 2.0};
        problem = new LeastSquaresProblem(1, 2,
                new QuadraticObjectiveFunction(-1, new QuadraticForm(new DiagonalMatrix(new double[]{1.0, 1.0}))),
                initialValuesSingular, constraintsSingular, bounds, weightMatrix);

        try {
            optimizer.optimize(problem);
            assertTrue(false); // Should throw an exception
        } catch (ConvergenceException e) {
            // Expected
        }
    }
}