package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.SamplingPoints;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private ExpandableStatefulODE equations;
    private double[] initialState;

    @Before
    public void setup() {
        integrator = new MyEmbeddedRungeKuttaIntegrator("My Integrator", true, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.5, 0.0}, {0.25, 0.25, 0.25}, {0.5, 0.0, 0.5}}, new double[]{0.0, 0.5}, new RungeKuttaStepInterpolator(new RK4Method()));
        equations = new MyODESystem();
        initialState = new double[]{1.0, 1.0};
    }

    @Test
    public void testGetOrder() {
        Assert.assertEquals(4, integrator.getOrder());
    }

    @Test
    public void testIntegrateNormalCase() {
        integrator.integrate(equations, 10.0, initialState);
        double[] expectedFinalState = new double[]{2.449542230325843, 1.921263102529536};
        Assert.assertArrayEquals(expectedFinalState, equations.getCompleteState(), 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIntegrateWithSmallStepSize() {
        integrator.setMinStep(1e-12);
        integrator.integrate(equations, 10.0, initialState);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testIntegrateWithMismatchedDimensions() {
        ExpandableStatefulODE equationsWithMismatchedDimensions = new MyODESystemWithMismatchedDimensions();
        integrator.integrate(equationsWithMismatchedDimensions, 10.0, initialState);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testIntegrateWithMaxCountExceeded() {
        integrator.setMaxStep(0.01);
        equations.setTime(100.0);
        integrator.integrate(equations, 200.0, initialState);
    }

    @Test(expected = NoBracketingException.class)
    public void testIntegrateWithNoBracketing() {
        integrator.setMinStep(10.0);
        integrator.setMaxStep(1.0);
        integrator.integrate(equations, 5.0, initialState);
    }

    @Test
    public void testSetSafety() {
        integrator.setSafety(0.8);
        Assert.assertEquals(0.8, integrator.getSafety(), 1e-8);
    }

    @Test
    public void testSetMinReduction() {
        integrator.setMinReduction(0.15);
        Assert.assertEquals(0.15, integrator.getMinReduction(), 1e-8);
    }

    @Test
    public void testSetMaxGrowth() {
        integrator.setMaxGrowth(5.0);
        Assert.assertEquals(5.0, integrator.getMaxGrowth(), 1e-8);
    }

    @Test
    public void testIntegrateEdgeCases() {
        integrator.setMinStep(0.001);
        integrator.integrate(equations, 10.0, initialState);
        double[] expectedFinalState = new double[]{2.449542230325843, 1.921263102529536};
        Assert.assertArrayEquals(expectedFinalState, equations.getCompleteState(), 1e-8);

        integrator.setMaxStep(10.0);
        integrator.integrate(equations, 10.0, initialState);
        expectedFinalState = new double[]{2.449542230325843, 1.921263102529536};
        Assert.assertArrayEquals(expectedFinalState, equations.getCompleteState(), 1e-8);

        integrator.setMinReduction(0.0);
        integrator.integrate(equations, 10.0, initialState);
        expectedFinalState = new double[]{2.449542230325843, 1.921263102529536};
        Assert.assertArrayEquals(expectedFinalState, equations.getCompleteState(), 1e-8);

        integrator.setMaxGrowth(0.0);
        integrator.integrate(equations, 10.0, initialState);
        expectedFinalState = new double[]{2.449542230325843, 1.921263102529536};
        Assert.assertArrayEquals(expectedFinalState, equations.getCompleteState(), 1e-8);
    }

    private static class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
        public MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        }

        @Override
        protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
            double error = 0.0;
            for (int i = 0; i < p1.length; i++) {
                error += FastMath.pow(FastMath.abs(p2[i] - p1[i]), 2);
            }
            return Math.sqrt(error);
        }
    }

    private static class MyODESystem implements ExpandableStatefulODE {
        public double getTime() {
            return 0.0;
        }

        public void setTime(double time) {
        }

        public String getName() {
            return "My ODE System";
        }

        public int getDimension() {
            return 2;
        }

        public int getStateDimension() {
            return 2;
        }

        public int getParameterDimension() {
            return 0;
        }

        public double[] getCompleteState() {
            return new double[]{1.0, 1.0};
        }

        public void setCompleteState(double[] state) {
        }

        public double[] getState() {
            return new double[]{1.0, 1.0};
        }

        public void setState(double[] state) {
        }

        public double[] getParameters() {
            return new double[0];
        }

        public void setParameters(double[] parameters) {
        }

        public double[] getDerivatives(double t, double[] y) {
            double[] derivatives = new double[2];
            derivatives[0] = y[1];
            derivatives[1] = -y[0];
            return derivatives;
        }

        public double[] getPrimaryVariables() {
            return new double[]{0.0};
        }

        public int getPrimaryVariableDimension() {
            return 1;
        }

        public double[] getSecondaryVariables() {
            return new double[]{1.0};
        }

        public int getSecondaryVariableDimension() {
            return 1;
        }

        public int getEquationsDimension() {
            return 2;
        }

        public int getOrder() {
            return 4;
        }

        public SamplingPoints getSamplingPoints(int numberOfPoints) {
            return new DefaultSamplingPoints(numberOfPoints);
        }
    }

    private static class MyODESystemWithMismatchedDimensions implements ExpandableStatefulODE {
        public double getTime() {
            return 0.0;
        }

        public void setTime(double time) {
        }

        public String getName() {
            return "My ODE System with Mismatched Dimensions";
        }

        public int getDimension() {
            return 3;
        }

        public int getStateDimension() {
            return 2;
        }

        public int getParameterDimension() {
            return 0;
        }

        public double[] getCompleteState() {
            return new double[]{1.0, 1.0, 1.0};
        }

        public void setCompleteState(double[] state) {
        }

        public double[] getState() {
            return new double[]{1.0, 1.0};
        }

        public void setState(double[] state) {
        }

        public double[] getParameters() {
            return new double[0];
        }

        public void setParameters(double[] parameters) {
        }

        public double[] getDerivatives(double t, double[] y) {
            throw new RuntimeException("Not implemented");
        }

        public double[] getPrimaryVariables() {
            return new double[]{0.0};
        }

        public int getPrimaryVariableDimension() {
            return 1;
        }

        public double[] getSecondaryVariables() {
            return new double[]{1.0};
        }

        public int getSecondaryVariableDimension() {
            return 1;
        }

        public int getEquationsDimension() {
            return 2;
        }

        public int getOrder() {
            return 4;
        }

        public SamplingPoints getSamplingPoints(int numberOfPoints) {
            return new DefaultSamplingPoints(numberOfPoints);
        }
    }
}