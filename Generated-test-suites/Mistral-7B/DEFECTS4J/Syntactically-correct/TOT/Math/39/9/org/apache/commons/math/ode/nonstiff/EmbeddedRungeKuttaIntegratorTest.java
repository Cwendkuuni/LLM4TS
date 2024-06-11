package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sample.StepHandlerAdapter;
import org.apache.commons.math3.ode.test.AbstractODETest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link EmbeddedRungeKuttaIntegrator}.
 */
public class EmbeddedRungeKuttaIntegratorTest extends AbstractODETest {

    /** Integrator under test. */
    private EmbeddedRungeKuttaIntegrator integrator;

    /**
     * Sets up the integrator for testing.
     */
    @Before
    public void setUp() {
        integrator = new RungeKuttaFehlberg78Method();
    }

    /**
     * Tests integration of simple differential equation.
     *
     * @throws MathIllegalArgumentException if the initial conditions are invalid
     * @throws MathIllegalStateException if the integrator is not initialized properly
     */
    @Test
    public void testSimpleIntegration()
        throws MathIllegalArgumentException, MathIllegalStateException {
        final double[] y0 = new double[] { 1.0 };
        final double t0 = 0.0;
        final double tEnd = 1.0;
        final double[] yEnd = new double[] { 2.0 };
        final double relTol = 1.0e-6;
        final double absTol = 1.0e-6;
        final ExpandableStatefulODE ode = new ODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(final double t, final double[] y, final double[] yPrime) {
                yPrime[0] = y[0] * 0.1;
            }
        };
        integrator.setMinStep(1.0e-6);
        integrator.setMaxStep(1.0e2);
        integrator.setStepSize(1.0);
        integrator.setAbsTol(absTol);
        integrator.setRelTol(relTol);
        integrator.integrate(ode, t0, y0, tEnd, yEnd, new StepHandlerAdapter() {
        });
        final double[] y = ode.getCompleteState();
        assertEquals(yEnd[0], y[0], relTol * FastMath.max(1.0, FastMath.abs(yEnd[0])));
        assertEquals(yEnd[0], y[0], absTol);
    }

    /**
     * Tests integration of differential equation with discontinuity.
     *
     * @throws MathIllegalArgumentException if the initial conditions are invalid
     * @throws MathIllegalStateException if the integrator is not initialized properly
     */
    @Test
    public void testDiscontinuousIntegration()
        throws MathIllegalArgumentException, MathIllegalStateException {
        final double[] y0 = new double[] { 0.0 };
        final double t0 = 0.0;
        final double tEnd = 1.0;
        final double[] yEnd = new double[] { 1.0 };
        final ExpandableStatefulODE ode = new ODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(final double t, final double[] y, final double[] yPrime) {
                if (t < 0.5) {
                    yPrime[0] = 1.0;
                } else {
                    yPrime[0] = -1.0;
                }
            }
        };
        integrator.setMinStep(1.0e-6);
        integrator.setMaxStep(1.0e2);
        integrator.setStepSize(1.0);
        integrator.setAbsTol(1.0e-6);
        integrator.setRelTol(1.0e-6);
        integrator.integrate(ode, t0, y0, tEnd, yEnd, new StepHandlerAdapter() {
        });
        final double[] y = ode.getCompleteState();
        assertEquals(yEnd[0], y[0], relTol * FastMath.max(1.0, FastMath.abs(yEnd[0])));
        assertEquals(yEnd[0], y[0], absTol);
    }

    /**
     * Tests integration of differential equation with singularity.
     *
     * @throws MathIllegalArgumentException if the initial conditions are invalid
     * @throws MathIllegalStateException if the integrator is not initialized properly
     */
    @Test
    public void testSingularIntegration()
        throws MathIllegalArgumentException, MathIllegalStateException {
        final double[] y0 = new double[] { 1.0 };
        final double t0 = 0.0;
        final double tEnd = 1.0;
        final double[] yEnd = new double[] { 1.0 / FastMath.SQRT_2 };
        final ExpandableStatefulODE ode = new ODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(final double t, final double[] y, final double[] yPrime) {
                yPrime[0] = 1.0 / (1.0 + t);
            }
        };
        integrator.setMinStep(1.0e-6);
        integrator.setMaxStep(1.0e2);
        integrator.setStepSize(1.0);
        integrator.setAbsTol(1.0e-6);
        integrator.setRelTol(1.0e-6);
        integrator.integrate(ode, t0, y0, tEnd, yEnd, new StepHandlerAdapter() {
        });
        final double[] y = ode.getCompleteState();
        assertEquals(yEnd[0], y[0], relTol * FastMath.max(1.0, FastMath.abs(yEnd[0])));
        assertEquals(yEnd[0], y[0], absTol);
    }

    /**
     * Tests the behavior when the initial conditions are invalid.
     *
     * @throws MathIllegalArgumentException if the expected exception is not thrown
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testInvalidInitialConditions() {
        final double[] y0 = new double[] { 0.0, 1.0 };
        final double t0 = 0.0;
        final double tEnd = 1.0;
        final ExpandableStatefulODE ode = new ODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(final double t, final double[] y, final double[] yPrime) {
                yPrime[0] = 1.0;
            }
        };
        integrator.setMinStep(1.0e-6);
        integrator.setMaxStep(1.0e2);
        integrator.integrate(ode, t0, y0, tEnd, null, new StepHandlerAdapter() {
        });
    }

    /**
     * Tests the behavior when the integrator is not initialized properly.
     *
     * @throws MathIllegalStateException if the expected exception is not thrown
     */
    @Test(expected = MathIllegalStateException.class)
    public void testUninitializedIntegrator() {
        final double[] y0 = new double[] { 1.0 };
        final double t0 = 0.0;
        final double tEnd = 1.0;
        final ExpandableStatefulODE ode = new ODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(final double t, final double[] y, final double[] yPrime) {
                yPrime[0] = 1.0;
            }
        };
        integrator = null;
        integrator.integrate(ode, t0, y0, tEnd, null, new StepHandlerAdapter() {
        });
    }

}

/**
 * Helper class for testing the integrator.
 */
class ODE implements ExpandableStatefulODE {
    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void initialize(final double[] y, final double t0) {
    }

    @Override
    public void computeDerivatives(final double t, final double[] y, final double[] yPrime) {
    }
}