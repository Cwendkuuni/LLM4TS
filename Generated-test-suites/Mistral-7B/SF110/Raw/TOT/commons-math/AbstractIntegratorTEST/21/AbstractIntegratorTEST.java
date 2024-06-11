package org.apache.commons.math3.ode.test;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.EventRate;
import org.apache.commons.math3.ode.events.NoninstantaneousEvent;
import org.apache.commons.math3.ode.events.StoppingEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.util.ExpandableStatefulODE;
import org.apache.commons.math3.testing.testfunctions.TestFunctions;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractIntegratorTest {

    @Test
    public void testDummy() {
        AbstractIntegrator integrator = new DummyIntegrator("DummyIntegrator");
        FirstOrderDifferentialEquations equations = TestFunctions.DLN15_2();
        double[] y0 = {1.0, 2.0};
        double t = 10.0;
        double[] y = new double[equations.getDimension()];
        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            assertEquals("Integration result is incorrect.", 11.315312235442344, y[0], 1e-5);
            assertEquals("Integration result is incorrect.", 11.46331653011855, y[1], 1e-5);
        } catch (Exception e) {
            fail("Integration failed with unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testEventHandlers() {
        AbstractIntegrator integrator = new DummyIntegrator("DummyIntegrator");
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 2.0;
            }
        };

        StoppingEvent event = new StoppingEvent(1.0, 1.0);
        integrator.addEventHandler(event, 0.1, 0.01, 5);
        double[] y0 = {0.1};
        double tStart = 0.0;
        double[] yStart = y0.clone();
        double tEnd = 2.0;

        try {
            integrator.integrate(equations, tStart, y0, tEnd, null);
            fail("Expected stopping event to be triggered.");
        } catch (MaxCountExceededException e) {
            // Expected exception
        }

        assertEquals("Event handler was not triggered.", 1.0, integrator.getCurrentStepStart(), 1e-9);
    }

    @Test
    public void testStepHandlers() {
        AbstractIntegrator integrator = new DummyIntegrator("DummyIntegrator");
        FirstOrderDifferentialEquations equations = TestFunctions.DLN15_2();
        double[] y0 = {1.0, 2.0};
        double t = 10.0;
        double[] y = new double[equations.getDimension()];

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
                if (isLastStep) {
                    assertEquals("Step handler was not called on the last step.", 10.0, interpolator.getInterpolatedTime(), 1e-5);
                    fail("Step handler should have stopped the integration.");
                }
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // Empty implementation
            }
        };

        integrator.addStepHandler(stepHandler);

        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("Integration should have been stopped by the step handler.");
        } catch (StoppedException e) {
            // Expected exception
        }
    }

    @Test
    public void testMaxEvaluations() {
        AbstractIntegrator integrator = new DummyIntegrator("DummyIntegrator");
        FirstOrderDifferentialEquations equations = TestFunctions.DLN15_2();
        double[] y0 = {1.0, 2.0};
        double t = 10.0;
        double[] y = new double[equations.getDimension()];

        integrator.setMaxEvaluations(5);

        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("Integration should have been stopped due to max evaluations.");
        } catch (MaxCountExceededException e) {
            // Expected exception
        }
    }

    @Test
    public void testNoBracketing() {
        AbstractIntegrator integrator = new DummyIntegrator("DummyIntegrator");
        FirstOrderDifferentialEquations equations = (FirstOrderDifferentialEquations) TestFunctions.A2();
        double[] y0 = {1.0};
        double t = 1.0;
        double[] y = new double[equations.getDimension()];

        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("Integration should have failed due to no bracketing.");
        } catch (NoBracketingException e) {
            // Expected exception
        }
    }

    @Test
    public void testDimensionMismatch() {
        AbstractIntegrator integrator = new DummyIntegrator("DummyIntegrator");
        FirstOrderDifferentialEquations equations = TestFunctions.DLN15_2();
        double[] y0 = {1.0, 2.0, 3.0};
        double t = 10.0;

        try {
            integrator.integrate(equations, 0.0, y0, t, null);
            fail("Integration should have failed due to dimension mismatch.");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test
    public void testNumberIsTooSmall() {
        AbstractIntegrator integrator = new DummyIntegrator("DummyIntegrator");
        FirstOrderDifferentialEquations equations = TestFunctions.DLN15_2();
        double[] y0 = {1.0, 2.0};
        double t = 1e-10;
        double[] y = new double[equations.getDimension()];

        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("Integration should have failed due to number being too small.");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }
    }

    @Test
    public void testResetOccurred() {
        AbstractIntegrator integrator = new DummyIntegrator("DummyIntegrator");
        FirstOrderDifferentialEquations equations = TestFunctions.DLN15_2();
        double[] y0 = {1.0, 2.0};
        double t = 10.0;
        double[] y = new double[equations.getDimension()];

        integrator.setMaxEvaluations(1);
        integrator.addEventHandler(new NoninstantaneousEvent(1.5, new EventRate() {
            @Override
            public double evaluateRate(double t, double[] y) {
                return 0.0;
            }
        }), 0.1);

        try {
            integrator.integrate(equations, 0.0, y0, t, y);
            fail("Integration should have been stopped due to reset occurred.");
        } catch (ResetOccurredException e) {
            // Expected exception
        }
    }
}

class DummyIntegrator extends AbstractIntegrator {

    public DummyIntegrator(String name) {
        super(name);
    }

    @Override
    protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        // Empty implementation
    }
}