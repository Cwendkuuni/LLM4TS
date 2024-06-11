package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventException;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.util.ExpandableStatefulODE;
import org.apache.commons.math3.ode.util.EquationsMapper;
import org.apache.commons.math3.ode.util.ExpandableState;
import org.apache.commons.math3.ode.exception.DimensionMismatchException;
import org.apache.commons.math3.ode.exception.InitialValueException;
import org.apache.commons.math3.ode.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.exception.NoBracketingException;
import org.apache.commons.math3.ode.exception.NonlinearSolverException;
import org.apache.commons.math3.ode.exception.SingularMatrixException;
import org.apache.commons.math3.ode.exception.UnsupportedStepControlException;
import org.junit.Test;

public class AbstractIntegratorTest {

    @Test
    public void testSimpleIntegrator() throws Exception {
        final String name = "SimpleIntegrator";
        final double[] y0 = {0.0, 1.0};
        final double[] yDot = new double[y0.length];

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                System.arraycopy(y, 0, yDot, 0, y.length);
                yDot[1] = y[0];
            }
        };

        final AbstractIntegrator integrator = new AbstractIntegrator(name) {
            @Override
            public void integrate(ExpandableStatefulODE ode, double t) throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                ode.setTime(t);
                ode.computeDerivatives(t, ode.getPrimaryState(), yDot);
            }
        };

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean last) {
                if (last) {
                    System.out.println("Last step reached.");
                }
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // no-op
            }
        });

        integrator.initIntegration(0.0, y0, 10.0);
        integrator.setExpandable(new ExpandableStatefulODE(equations));

        integrator.integrate(equations, 0.0, y0, 10.0, yDot);

        double t = 0.0;
        for (int i = 0; i < 10; i++) {
            t += 0.1;
            integrator.acceptStep(new StepInterpolator(t, yDot), yDot, yDot, t + 0.1);
            assertEquals(y0[0] + y0[1] * 0.1, yDot[0], 1e-6);
            assertEquals(y0[1], yDot[1], 1e-6);
        }

        // Test exceeding maximum evaluation limit
        integrator.setMaxEvaluations(1);
        try {
            integrator.integrate(equations, 0.0, y0, 10.0, yDot);
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        // Test with an event
        final Event event = new When(1.5).on(new PolynomialFunction(t -> Math.sin(t))).y(2.0);
        integrator.addEventHandler(event, 0.05, 1e-6, 10);

        integrator.initIntegration(0.0, y0, 2.0);
        integrator.integrate(equations, 0.0, y0, 2.0, yDot);
        assertEquals(2.0, yDot[1], 1e-6);
        assertFalse(integrator.isLastStep());

        integrator.acceptStep(new StepInterpolator(1.5, yDot), yDot, yDot, 1.6);
        assertTrue(integrator.isLastStep());
        assertEquals(1.5, integrator.getCurrentStepStart(), 1e-6);
    }

    @Test
    public void testIntegratorWithErrorHandlers() throws Exception {
        final String name = "ErrorIntegrator";
        final double[] y0 = {0.0, 1.0};
        final double[] yDot = new double[y0.length];

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * y[1];
                yDot[1] = -y[0];
            }
        };

        final AbstractIntegrator integrator = new AbstractIntegrator(name) {
            @Override
            public void integrate(ExpandableStatefulODE ode, double t) throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                ode.setTime(t);
                ode.computeDerivatives(t, ode.getPrimaryState(), yDot);
            }
        };

        integrator.setMaxEvaluations(1);
        integrator.initIntegration(0.0, y0, 5.0);
        integrator.setExpandable(new ExpandableStatefulODE(equations));

        // Test with an invalid step size
        try {
            integrator.acceptStep(new StepInterpolator(0.0, yDot), yDot, yDot, 0.5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with a nonlinear solver exception
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean last) throws MaxCountExceededException, DimensionMismatchException {
                try {
                    integrator.acceptStep(interpolator, yDot, yDot, interpolator.getGlobalCurrentTime() + 0.1);
                    fail("Expected NonlinearSolverException");
                } catch (NonlinearSolverException e) {
                    // Expected
                }
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // no-op
            }
        });

        integrator.addEventHandler(new When(3.0).on(new PolynomialFunction(t -> Math.sin(t))).y(2.0), 0.1, 1e-6, 10);

        integrator.integrate(equations, 0.0, y0, 5.0, yDot);

        double t = 0.0;
        for (int i = 0; i < 3; i++) {
            t += 1.0;
            integrator.acceptStep(new StepInterpolator(t, yDot), yDot, yDot, t + 1.0);
            assertEquals(y0[0] * Math.exp(-1.0), yDot[0], 1e-6);
            assertEquals(y0[1], yDot[1], 1e-6);
        }

        integrator.acceptStep(new StepInterpolator(3.0, yDot), yDot, yDot, 3.5);
        assertEquals(2.0, yDot[1], 1e-6);
        assertFalse(integrator.isLastStep());

        integrator.acceptStep(new StepInterpolator(3.5, yDot), yDot, yDot, 3.6);
        assertTrue(integrator.isLastStep());
        assertEquals(3.5, integrator.getCurrentStepStart(), 1e-6);
    }

    @Test
    public void testIntegratorWithSingularMatrixException() throws Exception {
        final String name = "SingularMatrixIntegrator";
        final double[] y0 = {0.0, 1.0};
        final double[] yDot = new double[y0.length];

        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * y[1];
                yDot[1] = 0.0;
            }
        };

        final AbstractIntegrator integrator = new AbstractIntegrator(name) {
            @Override
            public void integrate(ExpandableStatefulODE ode, double t) throws DimensionMismatchException, MaxCountExceededException, NoBracketingException {
                ode.setTime(t);
                ode.computeDerivatives(t, ode.getPrimaryState(), yDot);
            }
        };

        integrator.initIntegration(0.0, y0, 2.0);
        integrator.setExpandable(new ExpandableStatefulODE(equations));

        try {
            integrator.integrate(equations, 0.0, y0, 2.0, yDot);
            fail("Expected SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }

        // Test with an invalid step size
        try {
            integrator.acceptStep(new StepInterpolator(0.0, yDot), yDot, yDot, 0.5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with an UnsupportedStepControlException
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean last) {
                try {
                    integrator.acceptStep(interpolator, yDot, yDot, interpolator.getGlobalCurrentTime() + 0.1);
                    fail("Expected UnsupportedStepControlException");
                } catch (UnsupportedStepControlException e) {
                    // Expected
                }
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // no-op
            }
        });

        integrator.acceptStep(new StepInterpolator(0.0, yDot), yDot, yDot, 0.1);
        assertEquals(y0[0] * Math.exp(-0.1), yDot[0], 1e-6);
        assertEquals(y0[1], yDot[1], 1e-6);

        integrator.acceptStep(new StepInterpolator(0.1, yDot), yDot, yDot, 0.2);
        assertEquals(y0[0] * Math.exp(-0.2), yDot[0], 1e-6);
        assertEquals(y0[1], yDot[1], 1e-6);

        integrator.acceptStep(new StepInterpolator(0.2, yDot), yDot, yDot, 0.3);
        assertEquals(y0[0] * Math.exp(-0.3), yDot[0], 1e-6);
        assertEquals(y0[1], yDot[1], 1e-6);

        integrator.acceptStep(new StepInterpolator(0.3, yDot), yDot, yDot, 2.0);
        assertEquals(Double.NaN, yDot[0], 1e-6);
        assertTrue(integrator.isLastStep());
    }
}