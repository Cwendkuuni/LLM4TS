import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
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
                // implementation not needed for testing
            }
        };
    }

    /**
     * Test basic functionality of the integrator.
     */
    @Test
    public void testBasicFunctionality() {
        final double[] initialValues = new double[] {1.0, 0.0};
        final double targetTime = 10.0;

        integrator.initIntegration(0.0, initialValues, targetTime);
        integrator.integrate(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return initialValues.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
                yDot[1] = y[0];
            }
        }, targetTime);

        final double[] expectedFinalValues = new double[] {exp(10.0), exp(1.0)};
        Assert.assertArrayEquals(expectedFinalValues, integrator.getState(), 0.001);
    }

    /**
     * Test handling of step handlers.
     */
    @Test
    public void testStepHandlers() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation not needed for testing
            }
        };

        integrator.addStepHandler((interpolator, isLastStep) -> {
            if (isLastStep) {
                System.out.println("Last step");
            }
        });

        integrator.initIntegration(0.0, new double[] {1.0}, 10.0);
        integrator.integrate(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        }, 10.0);

        integrator.clearStepHandlers();
    }

    /**
     * Test handling of events.
     */
    @Test
    public void testEvents() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                // implementation not needed for testing
            }
        };

        integrator.addEventHandler((event, state, stepHandler) -> {
            if (event.getEventType() == AbstractIntegrator.EventType.END_OF_INTERVAL) {
                throw new NoBracketingException("Test event");
            }
        }, 1.0, 0.1, 10, new BracketingNthOrderBrentSolver(0.1, 5));

        integrator.initIntegration(0.0, new double[] {1.0}, 10.0);

        try {
            integrator.integrate(new ExpandableStatefulODE() {
                @Override
                public int getDimension() {
                    return 1;
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yDot) {
                    yDot[0] = 1.0;
                }
            }, 10.0);
            Assert.fail();
        } catch (NoBracketingException e) {
            // expected
        }
    }

    /**
     * Test exception handling.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testExceptionHandling() {
        integrator = new AbstractIntegrator() {
            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException,
                           MaxCountExceededException, NoBracketingException {
                sanityChecks(equations, t);
                // implementation not needed for testing
            }

            private void sanityChecks(ExpandableStatefulODE equations, double t)
                    throws NumberIsTooSmallException, DimensionMismatchException {
                final double threshold = 1.0e-10;
                final double dt = FastMath.abs(equations.getTime() - t);
                if (dt <= threshold) {
                    throw new NumberIsTooSmallException(LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL,
                            dt, threshold, false);
                }
            }
        };

        integrator.initIntegration(0.0, new double[] {1.0}, 1.0e-15);
        integrator.integrate(new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = 1.0;
            }
        }, 1.0);
    }

}