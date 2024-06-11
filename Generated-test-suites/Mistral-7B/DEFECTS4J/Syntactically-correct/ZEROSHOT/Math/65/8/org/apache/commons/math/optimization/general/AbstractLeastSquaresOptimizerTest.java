import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.vector.linearregression.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractLeastSquaresOptimizerTest {

    @Test
    public void testAbstractLeastSquaresOptimizer() throws Exception {
        // Test default constructor
        AbstractLeastSquaresOptimizer optimizer = new AbstractLeastSquaresOptimizer();
        assertEquals(AbstractLeastSquaresOptimizer.DEFAULT_MAX_ITERATIONS, optimizer.getMaxIterations());
        assertNull(optimizer.getConvergenceChecker());

        // Test custom constructor
        VectorialConvergenceChecker checker = new SimpleVectorialValueChecker();
        optimizer = new AbstractLeastSquaresOptimizer() {{
            setConvergenceChecker(checker);
            setMaxIterations(5);
        }};
        assertEquals(5, optimizer.getMaxIterations());
        assertEquals(checker.getClass(), optimizer.getConvergenceChecker().getClass());

        // Test optimization with a custom optimizer (Levenberg-Marquardt)
        DifferentiableMultivariateVectorFunction testFunction = (f, x) -> new PointValuePair(0.1 * x[0] * x[1],
                new double[]{x[0] * x[1], x[0] * x[1] + 1.0});
        double[] startPoint = new double[]{1.0, 1.0};
        double[] target = new double[]{2.0, 3.0};
        double[] weights = new double[]{0.1, 0.1};

        OptimizationProblem problem = new OptimizationProblem(testFunction, startPoint);
        Optimizer optimizer1 = new LevenbergMarquardtOptimizer();
        optimizer1.setInitialStep(0.1);
        optimizer1.setMaxIterations(5);
        PointValuePair result1 = optimizer1.optimize(problem);

        AbstractLeastSquaresOptimizer abstractOptimizer = new AbstractLeastSquaresOptimizer() {{
            setConvergenceChecker(checker);
            setMaxIterations(5);
        }};
        AbstractFunctionOptimizer customOptimizer = new AbstractLeastSquaresOptimizer(testFunction, startPoint, target, weights, abstractOptimizer) {};
        PointValuePair result2 = customOptimizer.optimize();

        assertEquals(result1.getPoint(), result2.getPoint(), 0.001);
        assertEquals(result1.getValue(), result2.getValue(), 0.001);

        // Test optimization with given starting point
        double[] startPoint2 = new double[]{1.5, 1.5};
        AbstractLeastSquaresOptimizer optimizer2 = new AbstractLeastSquaresOptimizer() {{
            setConvergenceChecker(checker);
            setMaxIterations(5);
        }};
        PointValuePair result3 = optimizer2.optimize(testFunction, startPoint2, target, weights);
        assertTrue(result3.getValue() < 0.0);

        // Test optimization with goal type
        GoalType goalType = GoalType.MINIMIZE;
        AbstractLeastSquaresOptimizer optimizer3 = new AbstractLeastSquaresOptimizer() {{
            setConvergenceChecker(checker);
            setMaxIterations(5);
            setGoalType(goalType);
        }};
        PointValuePair result4 = optimizer3.optimize(testFunction, startPoint2, target, weights);
        assertTrue(result4.getValue() >= 0.0);

        // Test optimization with error checking
        try {
            AbstractLeastSquaresOptimizer optimizer4 = new AbstractLeastSquaresOptimizer() {{
                setConvergenceChecker(checker);
                setMaxIterations(-1);
            }};
            optimizer4.optimize(testFunction, startPoint, target, weights);
            fail("Exception expected");
        } catch (OptimizationException e) {
            // Expected
        }

        try {
            AbstractLeastSquaresOptimizer optimizer5 = new AbstractLeastSquaresOptimizer() {{
                setConvergenceChecker(checker);
                setMaxIterations(Integer.MIN_VALUE);
            }};
            optimizer5.optimize(testFunction, startPoint, target, weights);
            fail("Exception expected");
        } catch (OptimizationException e) {
            // Expected
        }

        try {
            AbstractLeastSquaresOptimizer optimizer6 = new AbstractLeastSquaresOptimizer() {{
                setConvergenceChecker(checker);
                setMaxEvaluations(-1);
            }};
            optimizer6.optimize(testFunction, startPoint, target, weights);
            fail("Exception expected");
        } catch (OptimizationException e) {
            // Expected
        }

        try {
            AbstractLeastSquaresOptimizer optimizer7 = new AbstractLeastSquaresOptimizer() {{
                setConvergenceChecker(checker);
                setMaxEvaluations(Integer.MIN_VALUE);
            }};
            optimizer7.optimize(testFunction, startPoint, target, weights);
            fail("Exception expected");
        } catch (OptimizationException e) {
            // Expected
        }

        try {
            AbstractLeastSquaresOptimizer optimizer8 = new AbstractLeastSquaresOptimizer() {{
                setConvergenceChecker(null);
            }};
            optimizer8.optimize(testFunction, startPoint, target, weights);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            AbstractLeastSquaresOptimizer optimizer9 = new AbstractLeastSquaresOptimizer() {{
                setConvergenceChecker(checker);
                setMaxIterations(1);
                setMaxEvaluations(1);
            }};
            optimizer9.optimize(testFunction, startPoint, target, weights);
            fail("Exception expected");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }

        try {
            AbstractLeastSquaresOptimizer optimizer10 = new AbstractLeastSquaresOptimizer() {{
                setConvergenceChecker(checker);
                setMaxIterations(1);
                setMaxEvaluations(Integer.MAX_VALUE);
            }};
            optimizer10.optimize(testFunction, startPoint, target, weights);
            fail("Exception expected");
        } catch (MaxEvaluationsExceededException e) {
            // Expected
        }

        try {
            AbstractLeastSquaresOptimizer optimizer11 = new AbstractLeastSquaresOptimizer() {{
                setConvergenceChecker(checker);
                setMaxIterations(1);
                setMaxEvaluations(1);
            }};
            optimizer11.optimize(testFunction, startPoint, null, weights);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            AbstractLeastSquaresOptimizer optimizer12 = new AbstractLeastSquaresOptimizer() {{
                setConvergenceChecker(checker);
                setMaxIterations(1);
                setMaxEvaluations(1);
            }};
            optimizer12.optimize(testFunction, startPoint, target, null);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            AbstractLeastSquaresOptimizer optimizer13 = new AbstractLeastSquaresOptimizer() {{
                setConvergenceChecker(checker);
                setMaxIterations(1);
                setMaxEvaluations(1);
            }};
            optimizer13.optimize(testFunction, null, target, weights);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            AbstractLeastSquaresOptimizer optimizer14 = new AbstractLeastSquaresOptimizer() {{
                setConvergenceChecker(checker);
                setMaxIterations(1);
                setMaxEvaluations(1);
            }};
            optimizer14.optimize(testFunction, startPoint, target, null);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

}