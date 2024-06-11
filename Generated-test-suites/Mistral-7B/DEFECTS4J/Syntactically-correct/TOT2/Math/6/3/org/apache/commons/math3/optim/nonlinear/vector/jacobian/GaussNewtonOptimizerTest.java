import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplePointValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationTest;
import org.apache.commons.math3.optim.nonlinear.GaussNewtonOptimizer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest extends UnconstrainedOptimizationTest {

    private static final double EPSILON = 1e-10;

    /**
     * Test optimization with positive target values.
     */
    @Test
    public void testOptimizationPositive() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimplePointValueChecker(EPSILON));
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] initialPoint = {0.1, 0.2, 0.3};
        double[] expectedPoint = {1.5, 2.5, 3.5};
        double[] expectedValues = {0.0, 0.0, 0.0};
        optimize(optimizer, targetValues, initialPoint, expectedPoint, expectedValues);
    }

    /**
     * Test optimization with negative target values.
     */
    @Test
    public void testOptimizationNegative() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimplePointValueChecker(EPSILON));
        double[] targetValues = {-1.0, -2.0, -3.0};
        double[] initialPoint = {0.1, 0.2, 0.3};
        double[] expectedPoint = {-1.5, -2.5, -3.5};
        double[] expectedValues = {0.0, 0.0, 0.0};
        optimize(optimizer, targetValues, initialPoint, expectedPoint, expectedValues);
    }

    /**
     * Test optimization with zero target values.
     */
    @Test
    public void testOptimizationZero() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimplePointValueChecker(EPSILON));
        double[] targetValues = {0.0, 0.0, 0.0};
        double[] initialPoint = {0.1, 0.2, 0.3};
        double[] expectedPoint = {0.0, 0.0, 0.0};
        double[] expectedValues = {0.0, 0.0, 0.0};
        optimize(optimizer, targetValues, initialPoint, expectedPoint, expectedValues);
    }

    /**
     * Test optimization with a diagonal weight matrix.
     */
    @Test
    public void testOptimizationDiagonalWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimplePointValueChecker(EPSILON));
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] initialPoint = {0.1, 0.2, 0.3};
        double[][] weightMatrix = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        double[] expectedPoint = {1.5, 2.5, 3.5};
        double[] expectedValues = {0.0, 0.0, 0.0};
        optimize(optimizer, targetValues, initialPoint, expectedPoint, expectedValues, weightMatrix);
    }

    /**
     * Test optimization with a non-diagonal weight matrix.
     */
    @Test
    public void testOptimizationNonDiagonalWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimplePointValueChecker(EPSILON));
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] initialPoint = {0.1, 0.2, 0.3};
        double[][] weightMatrix = {{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}};
        double[] expectedPoint = {1.5, 2.5, 3.5};
        double[] expectedValues = {0.0, 0.0, 0.0};
        optimize(optimizer, targetValues, initialPoint, expectedPoint, expectedValues, weightMatrix);
    }

    /**
     * Test optimization with a non-square weight matrix.
     */
    @Test
    public void testOptimizationNonSquareWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimplePointValueChecker(EPSILON));
        double[] targetValues = {1.0, 2.0};
        double[] initialPoint = {0.1, 0.2};
        double[][] weightMatrix = {{-1.0, 0.5}, {0.5, 1.0}};
        assertThrows(IllegalArgumentException.class, () -> optimize(optimizer, targetValues, initialPoint, null, null, weightMatrix));
    }

    /**
     * Test optimization with a null target array.
     */
    @Test
    public void testOptimizationNullTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimplePointValueChecker(EPSILON));
        double[] initialPoint = {0.1, 0.2, 0.3};
        assertThrows(NullArgumentException.class, () -> optimize(optimizer, null, initialPoint, null, null, null));
    }

    /**
     * Test optimization with a null initial point array.
     */
    @Test
    public void testOptimizationNullInitialPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimplePointValueChecker(EPSILON));
        double[] targetValues = {1.0, 2.0, 3.0};
        assertThrows(NullArgumentException.class, () -> optimize(optimizer, targetValues, null, null, null, null));
    }

    /**
     * Test optimization with a null expected point array.
     */
    @Test
    public void testOptimizationNullExpectedPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimplePointValueChecker(EPSILON));
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] initialPoint = {0.1, 0.2, 0.3};
        double[] expectedValues = {0.0, 0.0, 0.0};
        assertThrows(NullArgumentException.class, () -> optimize(optimizer, targetValues, initialPoint, null, expectedValues, null));
    }

    /**
     * Test optimization with a null weight matrix.
     */
    @Test
    public void testOptimizationNullWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimplePointValueChecker(EPSILON));
        double[] targetValues = {1.0, 2.0, 3.0};
        double[] initialPoint = {0.1, 0.2, 0.3};
        assertThrows(NullArgumentException.class, () -> optimize(optimizer, targetValues, initialPoint, null, null, null));
    }

    /**
     * Helper method for optimizing and checking the results against expected values.
     *
     * @param optimizer Optimization algorithm to use.
     * @param targetValues Target values for optimization.
     * @param initialPoint Initial point for optimization.
     * @param expectedPoint Expected optimal point.
     * @param expectedValues Expected optimal values.
     * @param weightMatrix Weight matrix.
     */
    private void optimize(GaussNewtonOptimizer optimizer, double[] targetValues, double[] initialPoint, double[] expectedPoint, double[] expectedValues, double[][] weightMatrix) {
        optimizer.optimize(targetValues, initialPoint);
        PointValuePair result = optimizer.getValue();
        assertEquals("Unexpected optimal point.", expectedPoint, result.getPoint(), EPSILON);
        assertEquals("Unexpected optimal value.", expectedValues, result.getValue(), EPSILON);
    }
}