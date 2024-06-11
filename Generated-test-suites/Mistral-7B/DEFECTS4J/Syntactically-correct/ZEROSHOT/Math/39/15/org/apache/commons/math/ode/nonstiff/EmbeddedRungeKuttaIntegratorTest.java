package org.apache.commons.math.ode.nonstiff.EmbeddedRungeKuttaIntegrator;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.PrecisionRequiredException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode. eqn.AbstractODETest;
import org.apache.commons.math3.ode.sample.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests the EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends AbstractODETest {

    private static final double EPSILON = 1e-12;
    private static final double ACCURACY = 1e-5;
    private static final double TOLERANCE = 1e-12;
    private static final double INITIAL_TIME = 0.0;
    private static final double FINAL_TIME = 5.0;
    private static final int INTEGRATOR_ORDER = 5;
    private static final double[] INITIAL_STATE = {1.0, 1.0};
    private static final double[][] COEFFICIENTS = {
        {0.0, 0.0, 0.3, 0.48, 0.05555555555555555, 0.01},
        {0.0, 0.235, 0.4, 0.6, 0.3649259259259259, 0.0496},
        {0.0, 0.2, 0.3275, 0.68, 0.613203059901923, 0.0831},
        {0.0, 0.1428571428571429, 0.49, 0.8333333333333333, 0.9047619047619048, 0.1102},
        {0.0, 0.1, 0.511276379731334, 0.9, 0.9702884537033786, 0.1166},
        {0.0, 0.054, 0.5555555555555556, 0.95, 0.9952380952380952, 0.1224}
    };
    private EmbeddedRungeKuttaIntegrator integrator;
    private RungeKuttaStepInterpolator prototype;

    @Before
    public void setUp() {
        prototype = new RungeKuttaStepInterpolator(INTEGRATOR_ORDER);
        integrator = new EmbeddedRungeKuttaIntegrator("Classical Runge-Kutta method", false,
                COEFFICIENTS[0], COEFFICIENTS, COEFFICIENTS, prototype,
                INITIAL_TIME, FINAL_TIME, null, null) {
            @Override
            protected double estimateError(double[][] yDotK,
                                           double[] y0, double[] y1,
                                           double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(Math.abs(y1[i] - y0[i]), 2);
                }
                return Math.sqrt(error);
            }
        };
        integrator.setSafety(1.0);
        integrator.setMinReduction(0.5);
        integrator.setMaxGrowth(2.0);
    }

    @Test
    public void testIntegration() throws Exception {
        ExpandableStatefulODE equations = createEquation(new RK4Function(), INITIAL_STATE, INITIAL_TIME);
        double[] result = new double[2];
        integrator.integrate(equations, FINAL_TIME);
        assertArrayEquals(INITIAL_STATE, equations.getCompleteState(), TOLERANCE);
        integrator.getEquations().setTime(FINAL_TIME);
        integrator.integrate(equations, FINAL_TIME + 1.0);
        equations.getDerivatives(result, FINAL_TIME + 1.0);
        double error = 0;
        for (int i = 0; i < result.length; i++) {
            error += Math.pow(Math.abs(result[i] - equations.getCompleteState()[i]), 2);
        }
        assertEquals("Step rejected.", MaxCountExceededException.class,
                integrator.getLastException());
        integrator.resetLastException();
        integrator.setMinReduction(0.1);
        integrator.integrate(equations, FINAL_TIME + 1.0);
        result = new double[2];
        integrator.getEquations().setTime(FINAL_TIME + 1.0);
        integrator.integrate(equations, FINAL_TIME + 1.1);
        equations.getDerivatives(result, FINAL_TIME + 1.1);
        error = 0;
        for (int i = 0; i < result.length; i++) {
            error += Math.pow(Math.abs(result[i] - equations.getCompleteState()[i]), 2);
        }
        assertFalse("Step accepted without checking error.",
                integrator.getLastException() instanceof MathIllegalArgumentException);
        assertTrue("Error is too large.", error > ACCURACY);
        integrator.resetLastException();
        integrator.setMinReduction(0.9);
        integrator.integrate(equations, FINAL_TIME + 1.0);
        result = new double[2];
        integrator.getEquations().setTime(FINAL_TIME + 1.0);
        integrator.integrate(equations, FINAL_TIME + 1.1);
        equations.getDerivatives(result, FINAL_TIME + 1.1);
        error = 0;
        for (int i = 0; i < result.length; i++) {
            error += Math.pow(Math.abs(result[i] - equations.getCompleteState()[i]), 2);
        }
        assertTrue("Step accepted without checking error.",
                integrator.getLastException() == null);
        assertTrue("Error is too small.", error < EPSILON);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallStepSize() throws Exception {
        ExpandableStatefulODE equations = createEquation(new RK4Function(), INITIAL_STATE, INITIAL_TIME);
        integrator.integrate(equations, INITIAL_TIME + 0.001);
    }

    @Test(expected = PrecisionRequiredException.class)
    public void testLargeStepSize() throws Exception {
        ExpandableStatefulODE equations = createEquation(new RK4Function(), INITIAL_STATE, INITIAL_TIME);
        integrator.integrate(equations, FINAL_TIME + 100.0);
    }

}

class RK4Function implements ExpandableStatefulODE {

    @Override
    public void getDerivatives(double[] y, double t) {
        double x = y[0];
        double yp = y[1];
        y[0] = x;
        y[1] = 4.0 * x - x * x * yp;
    }

    @Override
    public void setTime(double time) {}

    @Override
    public double getTime() {
        return 0.0;
    }

}