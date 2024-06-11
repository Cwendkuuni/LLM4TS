package org.apache.commons.math3.ode.nonstiff.test;

import junit.framework.TestCase;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.space.*;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.util.Precision;

public class EmbeddedRungeKuttaIntegratorTest extends TestCase {

    public void testEmbeddedRungeKuttaIntegrator() throws Exception {
        // Test case for a simple ODE: y' = 2*y
        double[] initialCondition = {1.0};
        ExpandableStatefulODE equations = new SimpleODE(initialCondition);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK42Integrator("RK42", false, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{
                {0.25, 0.25, 0.25, 0.25},
                {0, 0.5, 0.5, 0},
                {0.5, 0, 0.5, 0},
                {0, 0, 0.5, 0.5},
        }, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator() {
            @Override
            public StepInterpolator copy() {
                return new RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(ODEIntegrator odeIntegrator, double[] yInitial, double[][] yDotInitial, boolean forward, ODEFunctionMapper primaryMapper, ODEFunctionMapper[] secondaryMappers) {
                        super.reinitialize(odeIntegrator, yInitial, yDotInitial, forward, primaryMapper, secondaryMappers);
                    }
                };
            }
        }, 0.01, 0.01, new double[]{0.001}, new double[]{0.001});

        integrator.integrate(equations, 2.0);
        double expectedResult = Precision.round(Math.exp(2.0), 4);
        double[] result = equations.getCompleteState();

        assertEquals("Expected y(2.0) = " + expectedResult, expectedResult, result[0], 0.0001);
    }

    public void testEmbeddedRungeKuttaIntegratorWithErrorHandling() throws Exception {
        // Test case for an ODE with an error (singularity)
        double[] initialCondition = {1.0 / 0.0};
        ExpandableStatefulODE equations = new DivideByZeroODE(initialCondition);

        try {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK42Integrator("RK42", false, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{
                    {0.25, 0.25, 0.25, 0.25},
                    {0, 0.5, 0.5, 0},
                    {0.5, 0, 0.5, 0},
                    {0, 0, 0.5, 0.5},
            }, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator() {
                @Override
                public StepInterpolator copy() {
                    return new RungeKuttaStepInterpolator() {
                        @Override
                        public void reinitialize(ODEIntegrator odeIntegrator, double[] yInitial, double[][] yDotInitial, boolean forward, ODEFunctionMapper primaryMapper, ODEFunctionMapper[] secondaryMappers) {
                            super.reinitialize(odeIntegrator, yInitial, yDotInitial, forward, primaryMapper, secondaryMappers);
                        }
                    };
                }
            }, 0.01, 0.01, new double[]{0.001}, new double[]{0.001});

            integrator.integrate(equations, 1.0);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }
    }

    public void testEmbeddedRungeKuttaIntegratorWithStepSizeControl() throws Exception {
        // Test case for an ODE with adaptive step size control
        double[] initialCondition = {0.1};
        ExpandableStatefulODE equations = new RiddersFunctionODE(initialCondition);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRK45Integrator("RK45", true, new double[]{0.3, 0.3, 0.3, 0.3, 0.3, 0.3}, new double[][]{
                {0.3, 0.3, 0.3, 0.3, 0.3, 0.3},
                {0, 0.3, 0.3, 0.3, 0.3, 0},
                {0.3, 0, 0.3, 0.3, 0.3, 0},
                {0.3, 0.3, 0, 0.3, 0.3, 0.3},
                {0.3, 0.3, 0.3, 0, 0.3, 0.3},
                {0.3, 0.3, 0.3, 0.3, 0, 0.3},
        }, new double[]{0.3, 0.3, 0.3, 0.3, 0.3, 0.3}, new RungeKuttaStepInterpolator() {
            @Override
            public StepInterpolator copy() {
                return new RungeKuttaStepInterpolator() {
                    @Override
                    public void reinitialize(ODEIntegrator odeIntegrator, double[] yInitial, double[][] yDotInitial, boolean forward, ODEFunctionMapper primaryMapper, ODEFunctionMapper[] secondaryMappers) {
                        super.reinitialize(odeIntegrator, yInitial, yDotInitial, forward, primaryMapper, secondaryMappers);
                    }
                };
            }
        }, 0.001, 0.01, null, null);

        integrator.integrate(equations, 2.0);
        double expectedResult = 1.316196310285245;
        double[] result = equations.getCompleteState();

        assertEquals("Expected y(2.0) = " + expectedResult, expectedResult, result[0], 0.0001);
    }
}

class SimpleODE implements ExpandableStatefulODE {
    private double[] state;

    public SimpleODE(double[] initialCondition) {
        this.state = initialCondition;
    }

    @Override
    public int getDimension() {
        return state.length;
    }

    @Override
    public void setState(double[] y) {
        System.arraycopy(y, 0, state, 0, state.length);
    }

    @Override
    public double[] getState() {
        return state.clone();
    }

    @Override
    public double[] getDerivatives(double t, double[] y) {
        double[] yPrime = new double[y.length];

        for (int i = 0; i < y.length; ++i) {
            yPrime[i] = 2.0 * y[i];
        }

        return yPrime;
    }
}

class DivideByZeroODE implements ExpandableStatefulODE {
    private double[] state;

    public DivideByZeroODE(double[] initialCondition) {
        this.state = initialCondition;
    }

    @Override
    public int getDimension() {
        return state.length;
    }

    @Override
    public void setState(double[] y) {
        System.arraycopy(y, 0, state, 0, state.length);
    }

    @Override
    public double[] getState() {
        return state.clone();
    }

    @Override
    public double[] getDerivatives(double t, double[] y) {
        throw new SingularityException();
    }
}

class RiddersFunctionODE implements ExpandableStatefulODE {
    private double[] state;

    public RiddersFunctionODE(double[] initialCondition) {
        this.state = initialCondition;
    }

    @Override
    public int getDimension() {
        return state.length;
    }

    @Override
    public void setState(double[] y) {
        System.arraycopy(y, 0, state, 0, state.length);
    }

    @Override
    public double[] getState() {
        return state.clone();
    }

    @Override
    public double[] getDerivatives(double t, double[] y) {
        double[] yPrime = new double[y.length];

        for (int i = 0; i < y.length; ++i) {
            yPrime[i] = Math.tan(y[i]) / (1 + Math.pow(y[i], 2));
        }

        return yPrime;
    }
}