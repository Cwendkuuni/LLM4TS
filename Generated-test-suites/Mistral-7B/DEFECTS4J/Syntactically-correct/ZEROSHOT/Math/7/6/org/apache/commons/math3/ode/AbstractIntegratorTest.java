import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.Precision;
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
                // implementation not needed in this test
            }
        };
    }

    @Test
    public void testAddStepHandler() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // implementation not needed in this test
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // implementation not needed in this test
            }
        });
        Assert.assertEquals(1, integrator.getStepHandlers().size());
    }

    @Test
    public void testClearStepHandlers() {
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                // implementation not needed in this test
            }

            @Override
            public void init(double t0, double[] y0, double t) {
                // implementation not needed in this test
            }
        });
        integrator.clearStepHandlers();
        Assert.assertEquals(0, integrator.getStepHandlers().size());
    }

    @Test
    public void testAddEventHandler() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // implementation not needed in this test
            }

            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double eventTime, double[] eventY) {
                // implementation not needed in this test
            }

            @Override
            public double getEventTime() {
                return 0;
            }

            @Override
            public boolean stop() {
                return false;
            }

            @Override
            public void reset(double eventTime, double[] eventY) {
                // implementation not needed in this test
            }
        }, 0, 0.001, 10, new BracketingNthOrderBrentSolver(0.001, 5));
        Assert.assertEquals(1, integrator.getEventHandlers().size());
    }

    @Test
    public void testClearEventHandler() {
        integrator.addEventHandler(new EventHandler() {
            @Override
            public void init(double t0, double[] y0, double t) {
                // implementation not needed in this test
            }

            @Override
            public boolean evaluateStep(AbstractStepInterpolator interpolator) {
                return false;
            }

            @Override
            public void stepAccepted(double eventTime, double[] eventY) {
                // implementation not needed in this test
            }

            @Override
            public double getEventTime() {
                return 0;
            }

            @Override
            public boolean stop() {
                return false;
            }

            @Override
            public void reset(double eventTime, double[] eventY) {
                // implementation not needed in this test
            }
        }, 0, 0.001, 10, new BracketingNthOrderBrentSolver(0.001, 5));
        integrator.clearEventHandlers();
        Assert.assertEquals(0, integrator.getEventHandlers().size());
    }

    @Test
    public void testGetCurrentSignedStepsize() {
        Assert.assertFalse(Precision.isClose(integrator.getCurrentSignedStepsize(), 0.0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCurrentStepStart() {
        integrator.getCurrentStepStart();
    }

    @Test
    public void testSetMaxEvaluations() {
        integrator.setMaxEvaluations(100);
        Assert.assertEquals(100, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetMaxEvaluations() {
        integrator.setMaxEvaluations(100);
        Assert.assertEquals(100, integrator.getMaxEvaluations());
        integrator.setMaxEvaluations(-1);
        Assert.assertEquals(-1, integrator.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        integrator.evaluations.incrementCount();
        integrator.evaluations.incrementCount();
        Assert.assertEquals(2, integrator.getEvaluations());
    }

    @Test
    public void testIntegrate()
            throws DimensionMismatchException, NumberIsTooSmallException,
                   MaxCountExceededException, NoBracketingException {
        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] + 1;
            }

            @Override
            public void setTime(double time) {
                // implementation not needed in this test
            }

            @Override
            public void setPrimaryState(double[] state) {
                // implementation not needed in this test
            }

            @Override
            public double[] getPrimaryState() {
                return new double[] { 0 };
            }
        };
        integrator.setEquations(equations);
        integrator.initIntegration(0, new double[] { 0 }, 1);
        integrator.integrate(equations, 1);
        Assert.assertEquals(1, integrator.getEvaluations());
        Assert.assertArrayEquals(new double[] { 1 }, equations.getPrimaryState());
    }

}