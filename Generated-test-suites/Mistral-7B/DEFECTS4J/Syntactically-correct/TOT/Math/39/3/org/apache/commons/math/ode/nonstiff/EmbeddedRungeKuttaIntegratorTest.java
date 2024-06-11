package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends AbstractODETest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;
    private double[] initialState = {1.0, 0.0};

    @Before
    public void setUp() {
        equations = new ODETestEquations();
        integrator = new RungeKuttaFehlberg78MethodIntegrator(4, RungeKuttaStepInterpolator.ADAPTIVE, 0.001, 0.01);
    }

    /**
     * Test the Runge-Kutta Fehlberg 78 method with some test equations.
     * @throws MathIllegalArgumentException if the initial state is invalid
     */
    @Test
    public void testRungeKuttaFehlberg78Method() throws MathIllegalArgumentException {
        integrator.initIntegrator(equations, initialState, 0.0, 10.0);
        integrator.integrate(equations, 1.0);
        double[] state = equations.getCompleteState();
        assertEquals(initialState[0] + 7.0 * FastMath.exp(0.1), state[0], 1e-8);
        assertEquals(initialState[1] + 7.0 * FastMath.exp(0.1) * 0.1, state[1], 1e-8);
    }

    /**
     * Test the Runge-Kutta Fehlberg 78 method with non-adaptive step size.
     * @throws MathIllegalArgumentException if the initial state is invalid
     */
    @Test
    public void testRungeKuttaFehlberg78MethodFixedStep() throws MathIllegalArgumentException {
        integrator = new RungeKuttaFehlberg78MethodIntegrator(4, RungeKuttaStepInterpolator.CONSTANT, 0.01, 0.01);
        integrator.initIntegrator(equations, initialState, 0.0, 10.0);
        integrator.integrate(equations, 1.0);
        double[] state = equations.getCompleteState();
        assertEquals(initialState[0] + 7.0 * FastMath.exp(0.1), state[0], 1e-8);
        assertEquals(initialState[1] + 7.0 * FastMath.exp(0.1) * 0.1, state[1], 1e-8);
    }

    /**
     * Test the Runge-Kutta Fehlberg 78 method with error handling.
     * @throws MathIllegalArgumentException if the initial state is invalid
     */
    @Test(expected = MathIllegalStateException.class)
    public void testRungeKuttaFehlberg78MethodError() throws MathIllegalArgumentException {
        integrator = new RungeKuttaFehlberg78MethodIntegrator(4, RungeKuttaStepInterpolator.ADAPTIVE, 0.001, -0.01);
        integrator.initIntegrator(equations, initialState, 0.0, 10.0);
        integrator.integrate(equations, 1.0);
    }

    /**
     * Test the Runge-Kutta Fehlberg 78 method with step handlers.
     * @throws MathIllegalArgumentException if the initial state is invalid
     */
    @Test
    public void testRungeKuttaFehlberg78MethodStepHandlers()
            throws MathIllegalArgumentException {

        int count = 0;
        StepHandler handler = new StepHandler() {
            public void handleStep(double time, double[] y) {
                ++count;
            }
        };
        integrator.setStepHandler(handler);

        integrator.initIntegrator(equations, initialState, 0.0, 10.0);
        integrator.integrate(equations, 10.0);
        assertEquals(11, count); // 11 steps including the initial and final one
    }
}