package org.apache.commons.math.ode.nonstiff;

import junit.framework.TestCase;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RK4Integrator;
import org.apache.commons.math3.ode.noise.RandomNoiseFunction;
import org.apache.commons.math3.special.BesselFunctions;

/**
 * Test cases for {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    /**
     * Test with a simple ODE.
     * @throws MathIllegalStateException
     * @throws MathIllegalArgumentException
     */
    public void testSimpleODE() throws MathIllegalStateException, MathIllegalArgumentException {
        final double[] y0 = {0.0, 1.0};
        final double  t0 = 0.0;
        final double  t1 = 1.0;
        final double  dt = 0.1;
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * (i + 1.0) + dt;
                }
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator("RK4", true, new double[] {0.25, 0.25, 0.25, 0.25},
                                                                  new double[][] {{1.0/6.0, 1.0/2.0, 1.0/2.0, 1.0},
                                                                                      {1.0/6.0, 1.0/2.0, 1.0/2.0, 1.0},
                                                                                      {1.0/6.0, 1.0/2.0, 1.0/2.0, 1.0},
                                                                                      {1.0,    1.0,    1.0,     1.0}},
                                                                  new RungeKuttaStepInterpolator() {
                                                                      @Override
                                                                      public double interpolate(double[] yDot, int step, double t) {
                                                                          return yDot[step];
                                                                      }
                                                                  },
                                                                  0.001, 0.01, null, null);
        integrator.integrate(equations, t1, y0);

        double[] yExpected = {1.5, 2.105};
        assertEquals(yExpected[0], equations.getCompleteState()[0], 1e-12);
        assertEquals(yExpected[1], equations.getCompleteState()[1], 1e-12);

        // Integrate backward
        integrator.integrate(equations, t0, yExpected);
        assertEquals(y0[0], equations.getCompleteState()[0], 1e-12);
        assertEquals(y0[1], equations.getCompleteState()[1], 1e-12);
    }

    /**
     * Test with a simple ODE with random noise.
     * @throws MathIllegalStateException
     * @throws MathIllegalArgumentException
     */
    public void testSimpleODEWithNoise() throws MathIllegalStateException, MathIllegalArgumentException {
        final double[] y0 = {0.0, 1.0};
        final double  t0 = 0.0;
        final double  t1 = 1.0;
        final double  dt = 0.1;
        final ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * (i + 1.0) + dt + new RandomNoiseFunction(0.1).value();
                }
            }
        };

        EmbeddedRungeKuttaIntegrator integrator = new RK4Integrator("RK4", true, new double[] {0.25, 0.25, 0.25, 0.25},
                                                                  new double[][] {{1.0/6.0, 1.0/2.0, 1.0/2.0, 1.0},
                                                                                      {1.0/6.0, 1.0/2.0, 1.0/2.0, 1.0},
                                                                                      {1.0/6.0, 1.0/2.0, 1.0/2.0, 1.0},
                                                                                      {1.0,    1.0,    1.0,     1.0}},
                                                                  new RungeKuttaStepInterpolator() {
                                                                      @Override
                                                                      public double interpolate(double[] yDot, int step, double t) {
                                                                          return yDot[step];
                                                                      }
                                                                  },
                                                                  0.001, 0.01, null, null);
        integrator.integrate(equations, t1, y0);

        double[] yExpected = {1.6, 2.135};
        double[] y = equations.getCompleteState();
        assertEquals(yExpected[0], y[0], 1e-12);
        assertEquals(yExpected[1], y[1], 1e-12);

        // Integrate backward
        integrator.integrate(equations, t0, yExpected);
        y = equations.getCompleteState();
        assertEquals(y0[0], y[0], 1e-12);
        assertEquals(y0[1], y[1], 1e-12);

        // Test with a stiff ODE
        final double[] y0Stiff = {1.0, 1.0};
        final double  t0Stiff  = 0.0;
        final double  t1Stiff  = 1.0;
        final double  dtStiff  = 0.1;
        final ExpandableStatefulODE equationsStiff = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return y0Stiff.length;
            }

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * (i + 1.0) * Math.exp(-10.0 * t);
                }
            }
        };

        // First integration
        integrator.integrate(equationsStiff, t1Stiff, y0Stiff);
        double[] yExpectedStiff = {1.4499996505433823E-5, 6.327983345976268E-5};
        double[] yStiff = equationsStiff.getCompleteState();
        assertEquals(yExpectedStiff[0], yStiff[0], 1e-13);
        assertEquals(yExpectedStiff[1], yStiff[1], 1e-13);

        // Integrate backward
        integrator.integrate(equationsStiff, t0Stiff, yExpectedStiff);
        yStiff = equationsStiff.getCompleteState();
        assertEquals(y0Stiff[0], yStiff[0], 1e-13);
        assertEquals(y0Stiff[1], yStiff[1], 1e-13);

        // Test with a stiff ODE with random noise
        final double[] y0StiffNoise = {1.0, 1.0};
        final double  t0StiffNoise  = 0.0;
        final double  t1StiffNoise  = 1.0;
        final double  dtStiffNoise  = 0.1;
        final ExpandableStatefulODE equationsStiffNoise = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return y0StiffNoise.length;
            }

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * (i + 1.0) * Math.exp(-10.0 * t) + new RandomNoiseFunction(0.1).value();
                }
            }
        };

        // First integration
        integrator.integrate(equationsStiffNoise, t1StiffNoise, y0StiffNoise);
        double[] yExpectedStiffNoise = {4.975653872417785E-10, 2.396176112632772E-9};
        double[] yStiffNoise = equationsStiffNoise.getCompleteState();
        assertEquals(yExpectedStiffNoise[0], yStiffNoise[0], 1e-13);
        assertEquals(yExpectedStiffNoise[1], yStiffNoise[1], 1e-13);

        // Integrate backward
        integrator.integrate(equationsStiffNoise, t0StiffNoise, yExpectedStiffNoise);
        yStiffNoise = equationsStiffNoise.getCompleteState();
        assertEquals(y0StiffNoise[0], yStiffNoise[0], 1e-13);
        assertEquals(y0StiffNoise[1], yStiffNoise[1], 1e-13);

        // Test with a stiff ODE with Bessel functions
        final double[] y0StiffBessel = {0.0, 1.0};
        final double  t0StiffBessel  = 0.0;
        final double  t1StiffBessel  = 1.0;
        final double  dtStiffBessel  = 0.1;
        final ExpandableStatefulODE equationsStiffBessel = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return y0StiffBessel.length;
            }

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * BesselFunctions.firstKind(i+1.0, FastMath.sqrt(-10.0 * t));
                }
            }
        };

        // First integration
        integrator.integrate(equationsStiffBessel, t1StiffBessel, y0StiffBessel);
        double[] yExpectedStiffBessel = {-1.0273632222438827E-5, 3.805358866151322E-4};
        double[] yStiffBessel = equationsStiffBessel.getCompleteState();
        assertEquals(yExpectedStiffBessel[0], yStiffBessel[0], 1e-13);
        assertEquals(yExpectedStiffBessel[1], yStiffBessel[1], 1e-13);

        // Integrate backward
        integrator.integrate(equationsStiffBessel, t0StiffBessel, yExpectedStiffBessel);
        yStiffBessel = equationsStiffBessel.getCompleteState();
        assertEquals(y0StiffBessel[0], yStiffBessel[0], 1e-13);
        assertEquals(y0StiffBessel[1], yStiffBessel[1], 1e-13);
    }

}