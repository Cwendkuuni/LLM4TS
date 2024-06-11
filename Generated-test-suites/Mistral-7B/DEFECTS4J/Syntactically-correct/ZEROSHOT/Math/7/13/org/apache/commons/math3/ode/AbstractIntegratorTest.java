import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation not used in this test
            }
        };
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // implementation not used in this test
            }
        });
        Assert.assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // implementation not used in this test
            }
        });
        integrator.clearStepHandlers();
        Assert.assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double tB, double[] yB, double[] yDotB, double[] constantValues) {
                // implementation not used in this test
            }

            @Override
            public void init(double t, double[] y, double tB) {
                // implementation not used in this test
            }
        }, 0.1, 1e-6, 10, new BracketingNthOrderBrentSolver(1e-6, 5));
        Assert.assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandler() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double tB, double[] yB, double[] yDotB, double[] constantValues) {
                // implementation not used in this test
            }

            @Override
            public void init(double t, double[] y, double tB) {
                // implementation not used in this test
            }
        }, 0.1, 1e-6, 10, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.clearEventHandlers();
        Assert.assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testResetOccurred() {
        integrator.setMaxEvaluations(1);
        integrator.addEventHandler(new EventHandler() {
            @Override
            public int getOrder() {
                return 1;
            }

            @Override
            public void evaluateState(double t, double[] y, double[] yDot, double tB, double[] yB, double[] yDotB, double[] constantValues) {
                y[0] = 1;
            }

            @Override
            public void init(double t, double[] y, double tB) {
                // implementation not used in this test
            }
        }, 0.1, 1e-6, 10, new BracketingNthOrderBrentSolver(1e-6, 5));
        integrator.initIntegration(0, new double[0], 1);
        integrator.computeDerivatives(0, new double[0], new double[0]);
        Assert.assertFalse(integrator.resetOccurred);
        integrator.acceptStep(new AbstractStepInterpolator() {
            @Override
            public double[] getInterpolatedState() {
                return new double[0];
            }

            @Override
            public double getGlobalCurrentTime() {
                return 0;
            }

            @Override
            public double getGlobalPreviousTime() {
                return -1;
            }
        }, new double[0], new double[0], 0.1);
        Assert.assertTrue(integrator.resetOccurred);
    }

}