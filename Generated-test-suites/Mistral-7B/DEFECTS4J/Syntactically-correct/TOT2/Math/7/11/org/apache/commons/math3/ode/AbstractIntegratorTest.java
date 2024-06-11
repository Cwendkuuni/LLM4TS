import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link AbstractIntegrator} class.
 */
public class AbstractIntegratorTest {

    private AbstractIntegrator integrator;

    @Before
    public void setUp() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // empty implementation
            }
        };
    }

    /**
     * Test integration of simple differential equation.
     */
    @Test
    public void testSimpleIntegration() {
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

        integrator.initIntegration(0.0, new double[]{0.0}, 1.0);

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            protected double interpolate(double time, double[] state) {
                return 0.0;
            }
        };

        integrator.acceptStep(interpolator, new double[]{0.0}, new double[]{0.0}, 0.1);

        Assert.assertEquals(0.1, integrator.getCurrentSignedStepsize(), 1e-10);
        Assert.assertArrayEquals(new double[]{0.1}, integrator.getCurrentState(), 1e-10);
    }

    /**
     * Test integration with event handling.
     */
    @Test
    public void testIntegrationWithEventHandling() {
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

        integrator.addEventHandler(new EventHandler() {
            @Override
            public void handleEvent(double t, double[] y) {
                Assert.assertEquals(1.0, t, 1e-10);
                Assert.assertEquals(1.0, y[0], 1e-10);
                integrator.setStateInitialized(true);
            }
        }, 1.0, 1e-10, 1, new BracketingNthOrderBrentSolver(1e-10, 5));

        integrator.initIntegration(0.0, new double[]{0.0}, 1.1);

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            protected double interpolate(double time, double[] state) {
                return 0.0;
            }
        };

        integrator.acceptStep(interpolator, new double[]{0.0}, new double[]{0.0}, 0.1);
        integrator.acceptStep(interpolator, new double[]{0.1}, new double[]{0.1}, 0.1);
        integrator.acceptStep(interpolator, new double[]{0.2}, new double[]{0.2}, 0.1);

        Assert.assertTrue(integrator.isStateInitialized());
    }

    /**
     * Test integration with step handlers.
     */
    @Test
    public void testIntegrationWithStepHandlers() {
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

        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
                if (lastStep) {
                    integrator.setStateInitialized(true);
                }
            }
        });

        integrator.initIntegration(0.0, new double[]{0.0}, 1.0);

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            protected double interpolate(double time, double[] state) {
                return 0.0;
            }
        };

        integrator.acceptStep(interpolator, new double[]{0.0}, new double[]{0.0}, 0.1);
        integrator.acceptStep(interpolator, new double[]{0.1}, new double[]{0.1}, 0.1);

        Assert.assertTrue(integrator.isStateInitialized());
    }

    /**
     * Test integration with maximum evaluations.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testIntegrationWithMaxEvaluations() {
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

        integrator.setMaxEvaluations(0);

        integrator.initIntegration(0.0, new double[]{0.0}, 1.0);

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            protected double interpolate(double time, double[] state) {
                return 0.0;
            }
        };

        integrator.acceptStep(interpolator, new double[]{0.0}, new double[]{0.0}, 0.1);
        integrator.acceptStep(interpolator, new double[]{0.1}, new double[]{0.1}, 0.1);
        integrator.acceptStep(interpolator, new double[]{0.2}, new double[]{0.2}, 0.1);
        integrator.acceptStep(interpolator, new double[]{0.3}, new double[]{0.3}, 0.1);
        integrator.acceptStep(interpolator, new double[]{0.4}, new double[]{0.4}, 0.1);
        integrator.acceptStep(interpolator, new double[]{0.5}, new double[]{0.5}, 0.1);
    }

    /**
     * Test integration with dimension mismatch.
     */
    @Test(expected = DimensionMismatchException.class)
    public void testIntegrationWithDimensionMismatch() {
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * 0.1;
                yDot[1] = y[1] * 0.1;
            }
        };

        integrator.initIntegration(0.0, new double[]{0.0, 0.0}, 1.0);

        AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {
            @Override
            protected double interpolate(double time, double[] state) {
                return 0.0;
            }
        };

        integrator.acceptStep(interpolator, new double[]{0.0}, new double[]{0.0, 0.0}, 0.1);
    }
}