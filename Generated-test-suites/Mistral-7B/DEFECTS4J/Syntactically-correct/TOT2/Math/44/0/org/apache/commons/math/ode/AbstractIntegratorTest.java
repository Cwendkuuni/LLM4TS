package org.apache.commons.math.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunctions;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.StoppingEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomDataGenerator;
import org.junit.Test;

import org.apache.commons.math.ode.AbstractIntegrator;
import org.apache.commons.math.ode.AbstractStepInterpolator;
import org.apache.commons.math.ode.FirstOrderIntegrator;
import org.apache.commons.math.ode.events.EventHandler;
import org.apache.commons.math.ode.events.NullEventHandler;
import org.apache.commons.math.ode.sampling.DefaultStepHandler;
import org.apache.commons.math.ode.stepper.ClassicalRungeKutta5Stepper;

/**
 * Test for AbstractIntegrator class.
 */
public class AbstractIntegratorTest {

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     * using a simple ODE with known solution.
     */
    @Test
    public void testSimpleODE() {
        final double[] y0 = { 2.0, 1.0 };
        final double t = 5.0;
        final double[] y = new double[y0.length];

        // create ODE
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                for (int i = 0; i < y.length; i++) {
                    yDot[i] = y[i] * (1.0 - y[i]);
                }
            }
        };

        // create integrator
        final AbstractIntegrator integrator = new AbstractIntegrator() {

            @Override
            public FirstOrderIntegrator newIntegrator() {
                return new ClassicalRungeKutta5Stepper();
            }

            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                ODETestUtils.testIntegrator(equations, t, y0, y);
            }

        };

        // integrate
        integrator.integrate(new ExpandableStatefulODE(equations), t);

        // check result
        assertArrayEquals(y, new double[] { 0.36787944116, 0.63212055884 });

    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     * using an ODE with a stopping event.
     */
    @Test
    public void testStoppingEvent() {

        // create ODE
        final double[] y0 = { 1.0 };
        final double tFinal = 2.0;
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * y[0] * (1.0 - y[0]);
            }

            @Override
            public EventState getEventState(double t, double[] y) {
                if (y[0] > 1.5) {
                    final StoppingEvent event = new StoppingEvent() {
                        @Override
                        public double getEventTime() {
                            return t;
                        }
                    };
                    return new EventState(event, Double.POSITIVE_INFINITY, 1.0,
                                           Integer.MAX_VALUE, new NullEventHandler());
                }
                return null;
            }

        };

        // create integrator
        final AbstractIntegrator integrator = new AbstractIntegrator() {

            @Override
            public FirstOrderIntegrator newIntegrator() {
                return new ClassicalRungeKutta5Stepper();
            }

            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }

            @Override
            public double acceptStep(AbstractStepInterpolator interpolator, double[] y,
                                     double[] yDot, double tEnd)
                    throws MathIllegalStateException {
                // no implementation needed
                return tEnd;
            }

            @Override
            public void step(double t, double[] y, double[] yDot, double[] yNew,
                             double stepSize) {
                // no implementation needed
            }

            @Override
            public void handleStateEvent(AbstractEvent event, double t, double[] y) {
                // no implementation needed
            }

        };

        // create step interpolator
        final AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {

            @Override
            public double getGlobalCurrentTime() {
                return 0.0;
            }

            @Override
            public void setGlobalCurrentTime(double time) {
                // no implementation needed
            }

            @Override
            public void setInterpolatedTime(double time) {
                // no implementation needed
            }

            @Override
            public double getInterpolatedState(int index) {
                return 0.0;
            }

            @Override
            public void setInterpolatedState(int index, double value) {
                // no implementation needed
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[0];
            }

            @Override
            public void setInterpolatedState(double[] values) {
                // no implementation needed
            }

            @Override
            public double getGlobalPreviousTime() {
                return -1.0;
            }

            @Override
            public void setGlobalPreviousTime(double time) {
                // no implementation needed
            }

            @Override
            public double[] getInterpolatedDerivatives() {
                return new double[0];
            }

            @Override
            public void setInterpolatedDerivatives(double[] values) {
                // no implementation needed
            }

            @Override
            public double getStepSize() {
                return 0.1;
            }

            @Override
            public void setStepSize(double stepSize) {
                // no implementation needed
            }

        };

        // create step handlers
        final List<StepHandler> stepHandlers = new ArrayList<>();
        stepHandlers.add(new DefaultStepHandler());
        stepHandlers.add(new AbstractStepHandler() {

            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                assertEquals(y0[0], interpolator.getInterpolatedState(0), 1.e-12);
                if (isLastStep) {
                    assertTrue(Double.isNaN(interpolator.getInterpolatedTime()));
                }
            }

        });

        // integrate
        integrator.setEquations(equations);
        integrator.setStepHandlers(stepHandlers);
        integrator.setMaxEvaluations(1000);

        integrator.integrate(new ExpandableStatefulODE(equations), tFinal);

        // check stop event
        assertTrue(integrator.isLastStep());

    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     * using an ODE with a reset event.
     */
    @Test
    public void testResetEvent() {

        // create ODE
        final double[] y0 = { 1.0 };
        final double tFinal = 3.0;
        final double[] yReset = { 2.0 };
        final FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {

            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = y[0] * y[0] * (1.0 - y[0]);
            }

            @Override
            public EventState getEventState(double t, double[] y) {
                if (y[0] >= 1.5) {
                    final EventState event = new EventState(new AbstractEventHandler() {

                        @Override
                        public void handleEvent(double t, double[] y, double[] yDot) {
                            System.arraycopy(yReset, 0, y, 0, yReset.length);
                        }

                    }, Double.POSITIVE_INFINITY, 1.0, Integer.MAX_VALUE,
                           new NullEventHandler());
                    return event;
                }
                return null;
            }

        };

        // create integrator
        final AbstractIntegrator integrator = new AbstractIntegrator() {

            @Override
            public FirstOrderIntegrator newIntegrator() {
                return new ClassicalRungeKutta5Stepper();
            }

            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }

            @Override
            public double acceptStep(AbstractStepInterpolator interpolator, double[] y,
                                     double[] yDot, double tEnd)
                    throws MathIllegalStateException {
                // no implementation needed
                return tEnd;
            }

            @Override
            public void step(double t, double[] y, double[] yDot, double[] yNew,
                             double stepSize) {
                // no implementation needed
            }

            @Override
            public void handleStateEvent(AbstractEvent event, double t, double[] y) {
                // no implementation needed
            }

            @Override
            public void reset() {
                System.arraycopy(yReset, 0, getPrimaryState(), 0, yReset.length);
            }

        };

        // create step interpolator
        final AbstractStepInterpolator interpolator = new AbstractStepInterpolator() {

            @Override
            public double getGlobalCurrentTime() {
                return 0.0;
            }

            @Override
            public void setGlobalCurrentTime(double time) {
                // no implementation needed
            }

            @Override
            public double getInterpolatedTime() {
                return 0.0;
            }

            @Override
            public void setInterpolatedTime(double time) {
                // no implementation needed
            }

            @Override
            public double[] getInterpolatedState() {
                return new double[0];
            }

            @Override
            public void setInterpolatedState(double[] values) {
                // no implementation needed
            }

            @Override
            public double getStepSize() {
                return 0.1;
            }

            @Override
            public void setStepSize(double stepSize) {
                // no implementation needed
            }

        };

        // create step handlers
        final List<StepHandler> stepHandlers = new ArrayList<>();
        stepHandlers.add(new DefaultStepHandler());
        stepHandlers.add(new AbstractStepHandler() {

            @Override
            public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
                assertEquals(y0[0], interpolator.getInterpolatedState(0), 1.e-12);
                if (isLastStep) {
                    assertTrue(Double.isNaN(interpolator.getInterpolatedTime()));
                }
            }

            @Override
            public void handleStepReset(AbstractStepInterpolator interpolator) {
                assertEquals(yReset[0], interpolator.getInterpolatedState(0), 1.e-12);
            }

        });

        // integrate
        integrator.setEquations(equations);
        integrator.setStepHandlers(stepHandlers);
        integrator.setMaxEvaluations(1000);

        integrator.integrate(new ExpandableStatefulODE(equations), tFinal);

        // check reset event
        assertTrue(integrator.isLastStep());
        assertTrue(integrator.resetOccurred());

    }

    /**
     * Test method for {@link AbstractIntegrator#integrate(ExpandableStatefulODE, double)}
     * using a random ODE.
     */
    @Test
    public void testRandomODE() {

        final double[] y0 = new double[RandomDataGenerator.nextInt(10)];
        final double tFinal = RandomDataGenerator.nextDouble(10.0);
        final ExpandableStatefulODE equations = new ExpandableStatefulODE(new PolynomialFunctions.SixthOrderPolynomial(y0.length)) {

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                for (int i = 0; i < y.length; i++) {
                    final double coeff = RandomDataGenerator.nextGaussian();
                    yDot[i] = coeff * y[i] * y[i] * (1.0 - y[i]);
                }
            }

            @Override
            public int getDimension() {
                return y0.length;
            }

            @Override
            public double getTime() {
                return 0.0;
            }

            @Override
            public double[] getPrimaryState() {
                return y0.clone();
            }

        };

        // create integrator
        final AbstractIntegrator integrator = new AbstractIntegrator() {

            @Override
            public FirstOrderIntegrator newIntegrator() {
                return new ClassicalRungeKutta5Stepper();
            }

            @Override
            public void integrate(ExpandableStatefulODE equations, double t)
                    throws MathIllegalStateException, MathIllegalArgumentException {
                // no implementation needed
            }

        };

        // integrate
        integrator.setEquations(equations);
        integrator.setMaxEvaluations(1000);
        integrator.integrate(equations, tFinal);

        // check result
        assertArrayEquals(equations.getPrimaryState(), integrator.getPrimaryState(), 1.e-12);

    }

}