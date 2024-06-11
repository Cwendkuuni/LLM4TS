import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setup() {
        integrator = new AbstractIntegrator() {
            @Override
            protected void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // no-op
            }
        };
    }

    @Test
    public void testAddStepHandler() throws Exception {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                Assert.assertTrue(isLastStep);
            }
        });
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                Assert.assertFalse(isLastStep);
            }
        });
        List<StepHandler> handlers = integrator.getStepHandlers();
        Assert.assertEquals(2, handlers.size());
    }

    @Test
    public void testClearStepHandlers() throws Exception {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                Assert.assertTrue(isLastStep);
            }
        });
        integrator.clearStepHandlers();
        List<StepHandler> handlers = integrator.getStepHandlers();
        Assert.assertEquals(0, handlers.size());
    }

    @Test
    public void testAddEventHandler() throws Exception {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot) {
                Assert.assertArrayEquals(new double[]{1.0}, y);
            }

            @Override
            public double eventTime(double t, double[] y) {
                return t + 1.0;
            }

            @Override
            public int getOrder() {
                return 1;
            }
        }, 0.0, 1.0e-12, 10, new BracketingNthOrderBrentSolver(1.0e-12, 5));
        List<EventHandler> handlers = integrator.getEventHandlers();
        Assert.assertEquals(1, handlers.size());
    }

    @Test
    public void testClearEventHandler() throws Exception {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot) {
                Assert.assertArrayEquals(new double[]{1.0}, y);
            }

            @Override
            public double eventTime(double t, double[] y) {
                return t + 1.0;
            }

            @Override
            public int getOrder() {
                return 1;
            }
        }, 0.0, 1.0e-12, 10, new BracketingNthOrderBrentSolver(1.0e-12, 5));
        integrator.clearEventHandlers();
        List<EventHandler> handlers = integrator.getEventHandlers();
        Assert.assertEquals(0, handlers.size());
    }

    @Test
    public void testIntegrate() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1.0;
            }
        };
        double[] y = new double[]{0.0};
        double t = 1.0;
        double[] yExpected = new double[]{1.0};
        integrator.integrate(equations, 0.0, y, t, y);
        Assert.assertArrayEquals(yExpected, y);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithStateMismatch() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // no-op
            }
        };
        double[] y = new double[]{0.0, 0.0};
        double t = 1.0;
        integrator.integrate(equations, 0.0, y, t, new double[1]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxEvaluationsReached() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                // no-op
            }
        };
        double[] y = new double[]{0.0};
        double t = 1.0;
        integrator.setMaxEvaluations(0);
        integrator.integrate(equations, 0.0, y, t, y);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketingException() throws Exception {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1.0;
            }
        };
        double[] y = new double[]{0.0};
        double t = 0.0;
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void evaluateState(double t, double[] y, double[] yDot) {
                // no-op
            }

            @Override
            public double eventTime(double t, double[] y) {
                return t;
            }

            @Override
            public int getOrder() {
                return 2;
            }
        }, 0.0, 1.0e-12, 10, new BracketingNthOrderBrentSolver(1.0e-12, 5));
        integrator.integrate(equations, 0.0, y, Double.POSITIVE_INFINITY, y);
    }

}