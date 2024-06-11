import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    private EmbeddedRungeKuttaIntegrator integrator;
    private RungeKuttaStepInterpolator interpolator;
    private ExpandableStatefulODE equations;

    @Before
    public void setUp() {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.0, 0.5, 0.5}, {0.0, 0.25, 0.45, 0.6}, {0.0, 0.0, 0.5, 0.5}, {0.0, 0.0, 0.0, 1.0}};
        double[] b = {0.23692688505667133, 0.27582549862658228, 0.3251135492915177, 0.20000000000000001};
        integrator = new EmbeddedRungeKuttaIntegrator("RKF45", false, c, a, b, new RungeKuttaStepInterpolator(1), 1e-10, 1e10, null, null) {
            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                double error = 0;
                for (int i = 0; i < y0.length; i++) {
                    error += Math.pow(y1[i] - y0[i], 2);
                }
                return Math.sqrt(error);
            }
        };
        interpolator = new RungeKuttaStepInterpolator(1);
        equations = new TestODEFunction();
    }

    @Test
    public void testIntegration() {
        double initialTime = 0.0;
        double finalTime = 1.0;
        double[] initialState = {0.0, 1.0};

        integrator.integrate(equations, initialTime);
        Assert.assertEquals(initialState[0], equations.getCompleteState()[0], 1e-6);
        Assert.assertEquals(initialState[1], equations.getCompleteState()[1], 1e-6);

        integrator.integrate(equations, finalTime);
        Assert.assertEquals(1.0, equations.getCompleteState()[0], 1e-6);
        Assert.assertEquals(1.718281828, equations.getCompleteState()[1], 1e-6);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testInitialStepOutOfRange() {
        integrator.integrate(equations, -1.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testLastStepOutOfRange() {
        double[] initialState = {0.0, 1.0};
        integrator.integrate(equations, 1.0);
        integrator.integrate(equations, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testSafetyFactor() {
        integrator.setSafety(0.1);
        integrator.integrate(equations, 1.0);
        double[] initialState = {0.0, 1.0};
        double expectedError = 0.2;
        double[] state = equations.getCompleteState();
        for (int i = 0; i < state.length; i++) {
            Assert.assertEquals(initialState[i], state[i], expectedError);
        }
        assertEquals(expectedError, integrator.getStepSize(), 1e-6);
    }

    @Test
    public void testMinReduction() {
        integrator.setMinReduction(0.05);
        integrator.integrate(equations, 1.0);
        double[] initialState = {0.0, 1.0};
        double expectedError = 0.2;
        double[] state = equations.getCompleteState();
        for (int i = 0; i < state.length; i++) {
            Assert.assertEquals(initialState[i], state[i], expectedError);
        }
        assertEquals(expectedError * 0.05, integrator.getStepSize(), 1e-6);
    }

    @Test
    public void testMaxGrowth() {
        integrator.setMaxGrowth(2.0);
        integrator.integrate(equations, 1.0);
        double[] initialState = {0.0, 1.0};
        double expectedError = 0.2;
        double[] state = equations.getCompleteState();
        for (int i = 0; i < state.length; i++) {
            Assert.assertEquals(initialState[i], state[i], expectedError);
        }
        assertEquals(expectedError * 2.0, integrator.getStepSize(), 1e-6);
    }

}

class TestODEFunction extends ExpandableStatefulODE {

    @Override
    public int getDimension() {
        return 2;
    }

    @Override
    public int getStateDimension() {
        return 2;
    }

    @Override
    public int getPrimaryDimension() {
        return 2;
    }

    @Override
    public void derive(double t, double[] y, double[] dydt) {
        for (int i = 0; i < y.length; i++) {
            dydt[i] = y[i] * (1.0 - y[i]);
        }
    }

    @Override
    public void map(double t, double[] y, double[] yPrime) {
        System.arraycopy(y, 0, yPrime, 0, y.length);
    }

}