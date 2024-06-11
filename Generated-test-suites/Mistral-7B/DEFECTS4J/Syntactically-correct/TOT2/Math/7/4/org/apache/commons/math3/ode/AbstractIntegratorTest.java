import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;

/**
 * Test for the AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setup() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation not used in this test
            }
        };
    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(FirstOrderDifferentialEquations, double, double[], double[], double[])}.
     */
    @Test
    public void testIntegrate() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        double[] y0 = {1.0};
        double t = 10.0;
        double[] yExpect = new double[1];

        integrator.initIntegration(0.0, y0, t);
        integrator.integrate(equations, t);
        System.arraycopy(integrator.expandable.getPrimaryState(), 0, yExpect, 0, yExpect.length);
        Assert.assertArrayEquals(yExpect, integrator.expandable.getPrimaryState(), 0.001);
    }

    /**
     * Test method for {@link AbstractIntegrator#acceptStep(AbstractStepInterpolator, double[], double[], double)}.
     */
    @Test
    public void testAcceptStep() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation not used in this test
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        double[] y0 = {1.0};
        double t0 = 0.0;

        integrator.initIntegration(t0, y0, 5.0);

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return integrator.stepStart;
            }

            @Override
            public double getGlobalCurrentTime() {
                return integrator.getCurrentSignedStepsize() > 0 ? integrator.stepStart + integrator.getCurrentSignedStepsize() : integrator.stepStart - integrator.getCurrentSignedStepsize();
            }

            @Override
            public void setInterpolatedTime(double time) {
                integrator.stepStart = time;
            }

            @Override
            public double[] getInterpolatedState() {
                return integrator.expandable.getPrimaryState();
            }

            @Override
            public void setSoftPreviousTime(double previousTime) {
                integrator.stepStart = previousTime;
            }

            @Override
            public void setSoftCurrentTime(double currentTime) {
                integrator.stepStart = currentTime;
            }
        };

        double[] y = new double[1];
        double[] yDot = new double[1];
        double tEnd = 2.0;

        integrator.acceptStep(interpolator, y, yDot, tEnd);

        Assert.assertArrayEquals(y, integrator.expandable.getPrimaryState(), 0.001);

        // test for an event that stops integration
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation not used in this test
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        integrator.addEventHandler(new AbstractEvent() {
            @Override
            public double getEventTime() {
                return 1.5;
            }

            @Override
            public void evaluateStep(AbstractStepInterpolator interpolator) {
                return true;
            }

            @Override
            public void stepAccepted(double eventTime, double[] eventY) {
                integrator.stepStart = eventTime;
                integrator.resetOccurred = true;
            }

            @Override
            public boolean stop() {
                return integrator.resetOccurred;
            }
        }, 1.0, 0.0, 10);

        integrator.initIntegration(0.0, y0, 5.0);

        interpolator = new AbstractStepInterpolator() {
            @Override
            public double getGlobalPreviousTime() {
                return integrator.stepStart;
            }

            @Override
            public double getGlobalCurrentTime() {
                return integrator.getCurrentSignedStepsize() > 0 ? integrator.stepStart + integrator.getCurrentSignedStepsize() : integrator.stepStart - integrator.getCurrentSignedStepsize();
            }

            @Override
            public void setInterpolatedTime(double time) {
                integrator.stepStart = time;
            }

            @Override
            public double[] getInterpolatedState() {
                return integrator.expandable.getPrimaryState();
            }

            @Override
            public void setSoftPreviousTime(double previousTime) {
                integrator.stepStart = previousTime;
            }

            @Override
            public void setSoftCurrentTime(double currentTime) {
                integrator.stepStart = currentTime;
            }
        };

        integrator.acceptStep(interpolator, y, yDot, tEnd);

        Assert.assertArrayEquals(y, integrator.expandable.getPrimaryState(), 0.001);
        Assert.assertTrue(integrator.isLastStep);
    }

    /**
     * Test method for {@link AbstractIntegrator#setStateInitialized()}.
     */
    @Test
    public void testSetStateInitialized() {
        AbstractIntegrator integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation not used in this test
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
            }
        };

        integrator.setStateInitialized(false);
        Assert.assertFalse(integrator.statesInitialized);

        integrator.setStateInitialized(true);
        Assert.assertTrue(integrator.statesInitialized);
    }

}