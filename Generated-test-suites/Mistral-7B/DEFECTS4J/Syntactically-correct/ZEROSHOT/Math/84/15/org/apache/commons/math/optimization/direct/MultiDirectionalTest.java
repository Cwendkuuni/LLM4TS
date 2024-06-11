package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.function.LinearFunction;
import org.apache.commons.math3.optim.nonlinear.function.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.function.SimpleFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.point.GoalTypePointValuePair;
import org.junit.Test;

/**
 * Test class for MultiDirectional class.
 */
public class MultiDirectionalTest {

    /** Multi-directional optimizer with default coefficients. */
    private MultiDirectional defaultMultiDirectional;

    /** Multi-directional optimizer with custom coefficients. */
    private MultiDirectional customMultiDirectional;

    /**
     * Set up tests.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        System.setProperty("org.apache.commons.math3.optim.MaxEval", "100000");
    }

    /**
     * Initialize tests.
     */
    @Before
    public void setUp() {
        defaultMultiDirectional = new MultiDirectional();
        customMultiDirectional = new MultiDirectional(3.0, 0.8);
    }

    /**
     * Test default constructor.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testDefaultConstructor() throws Exception {
        final ObjectiveFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1.0);
        final SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        final GoalType goalType = GoalType.MINIMIZE;

        final MultiDirectional optimizer = new MultiDirectional();
        final PointValuePair result = optimizer.optimize(function, bounds, goalType);

        assertNotNull(result);
        assertEquals(GoalTypePointValuePair.class, result.getClass());
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertArrayEquals(new double[]{0.0}, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test custom constructor.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testCustomConstructor() throws Exception {
        final ObjectiveFunction function = new LinearFunction(new double[]{-1.0}, 1.0);
        final SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        final GoalType goalType = GoalType.MINIMIZE;

        final MultiDirectional optimizer = new MultiDirectional(3.0, 0.8);
        final PointValuePair result = optimizer.optimize(function, bounds, goalType);

        assertNotNull(result);
        assertEquals(GoalTypePointValuePair.class, result.getClass());
        assertEquals(GoalType.MINIMIZE, result.getGoalType());
        assertArrayEquals(new double[]{0.0}, result.getPoint(), 0.0);
        assertEquals(0.0, result.getValue(), 0.0);
    }

    /**
     * Test reflection step.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testReflectionStep() throws Exception {
        final ObjectiveFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1.0);
        final SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        final GoalType goalType = GoalType.MINIMIZE;

        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{-5.0, 0.0}, 15.0, false);
        original[1] = new RealPointValuePair(new double[]{-4.0, 1.0}, 12.0, false);
        original[2] = new RealPointValuePair(new double[]{5.0, 1.0}, 25.0, false);

        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, null);

        assertEquals(-4.0, reflected.getPointRef()[0], 0.01);
        assertEquals(-3.0, reflected.getPointRef()[1], 0.01);
        assertEquals(10.0, reflected.getValue(), 0.01);
    }

    /**
     * Test expansion step.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testExpansionStep() throws Exception {
        final ObjectiveFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1.0);
        final SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        final GoalType goalType = GoalType.MINIMIZE;

        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{-5.0, 0.0}, 15.0, false);
        original[1] = new RealPointValuePair(new double[]{-4.0, 1.0}, 12.0, false);
        original[2] = new RealPointValuePair(new double[]{5.0, 1.0}, 25.0, false);

        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair expanded = optimizer.evaluateNewSimplex(original, 2.0, null);

        assertEquals(-3.6, expanded.getPointRef()[0], 0.01);
        assertEquals(1.2, expanded.getPointRef()[1], 0.01);
        assertEquals(11.55, expanded.getValue(), 0.01);
    }

    /**
     * Test contraction step.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testContractionStep() throws Exception {
        final ObjectiveFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 1.0);
        final SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        final GoalType goalType = GoalType.MINIMIZE;

        RealPointValuePair[] original = new RealPointValuePair[3];
        original[0] = new RealPointValuePair(new double[]{-5.0, 0.0}, 15.0, false);
        original[1] = new RealPointValuePair(new double[]{-4.0, 1.0}, 12.0, false);
        original[2] = new RealPointValuePair(new double[]{5.0, 1.0}, 25.0, false);

        final MultiDirectional optimizer = new MultiDirectional();
        final RealPointValuePair contracted = optimizer.evaluateNewSimplex(original, 0.5, null);

        assertEquals(-4.75, contracted.getPointRef()[0], 0.01);
        assertEquals(-3.5, contracted.getPointRef()[1], 0.01);
        assertEquals(7.75, contracted.getValue(), 0.01);
    }

}