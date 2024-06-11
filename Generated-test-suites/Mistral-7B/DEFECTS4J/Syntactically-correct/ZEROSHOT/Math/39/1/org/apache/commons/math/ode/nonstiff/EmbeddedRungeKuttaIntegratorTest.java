import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.Constants;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for EmbeddedRungeKuttaIntegrator class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test method for {@link EmbedgedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Create a test equation
        class TestEquation implements ExpandableStatefulODE {
            private double t;
            private double y;

            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public void initialize(double[] y, double t) {
                this.t = t;
                this.y = y[0];
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(t) * (Constants.E + y);
            }

            @Override
            public double getTime() {
                return t;
            }

            @Override
            public double[] getCompleteState() {
                return new double[]{y};
            }

            @Override
            public void setTime(double time) {
                t = time;
            }
        }

        // Create a test integrator
        double[] c = { 1.0 / 5.0, 3.0 / 10.0, 4.0 / 5.0, 8.0 / 9.0, 1.0 };
        double[][] a = {
            { 1.0 / 25.0, 0.0, 0.0, 0.0, 1.0 / 5.0 },
            { 3.0 / 40.0, 9.0 / 40.0, 3.0 / 10.0, 0.0, 3.0 / 10.0 },
            { 44.0 / 45.0, 120.0 / 15.0 - 28.0 / 15.0, 216.0 / 175.0 - 6.0 / 5.0, 128.0 / 175.0, 16.0 / 15.0 },
            { 720.0 / 63.0, -1215.0 / 216.0 + 1152.0 / 216.0, -1792.0 / 221.0 + 728.0 / 21.0, 1280.0 / 21.0, 640.0 / 21.0 },
            { 120.0, -120.0, 120.0, -120.0, 120.0 }
        };
        double[] b = { 1.0 / 5.0, 3.0 / 4.0, 44.0 / 45.0, 720.0 / 63.0, 120.0 };
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(5);

        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", false, c, a, b, prototype, 0.01, 0.05, null, null);

        // Test integration
        double tStart = 0.0;
        double tEnd   = 2.0;
        double[] yStart = new double[]{0.0};
        double yEndExpected = 1.6230192158469111;

        Assert.assertArrayEquals(yStart, integrator.getInitialState(), 0.0);

        integrator.integrate(new TestEquation(), tStart);

        Assert.assertEquals(tEnd, integrator.getTime(), 0.0);
        Assert.assertEquals(yEndExpected, integrator.getCompleteState()[0], 0.0);
    }

    /**
     * Test method for {@link EmbeddedRungeKuttaIntegrator#EmbeddedRungeKuttaIntegrator(String, boolean, double[], double[][], double[], RungeKuttaStepInterpolator, double, double[], double[])}.
     */
    @Test
    public void testEmbeddedRungeKuttaIntegratorWithVectorTolerances() {
        // Create a test equation
        class TestEquation implements ExpandableStatefulODE {
            private double t;
            private double[] y;

            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public void initialize(double[] y, double t) {
                this.t = t;
                this.y = y;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.exp(t) * (Math.cos(y[1]) + y[0]);
                yPrime[1] = Math.exp(t) * (Math.sin(y[1]) + y[0]);
            }

            @Override
            public double getTime() {
                return t;
            }

            @Override
            public double[] getCompleteState() {
                return y;
            }

            @Override
            public void setTime(double time) {
                t = time;
            }
        }

        // Create a test integrator
        double[] c = { 1.0 / 5.0, 3.0 / 10.0, 4.0 / 5.0, 8.0 / 9.0, 1.0 };
        double[][] a = {
            { 1.0 / 25.0, 0.0, 0.0, 0.0, 1.0 / 5.0 },
            { 3.0 / 40.0, 9.0 / 40.0, 3.0 / 10.0, 0.0, 3.0 / 10.0 },
            { 44.0 / 45.0, 120.0 / 15.0 - 28.0 / 15.0, 216.0 / 175.0 - 6.0 / 5.0, 128.0 / 175.0, 16.0 / 15.0 },
            { 720.0 / 63.0, -1215.0 / 216.0 + 1152.0 / 216.0, -1792.0 / 221.0 + 728.0 / 21.0, 1280.0 / 21.0, 640.0 / 21.0 },
            { 120.0, -120.0, 120.0, -120.0, 120.0 }
        };
        double[] b = { 1.0 / 5.0, 3.0 / 4.0, 44.0 / 45.0, 720.0 / 63.0, 120.0 };
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(5);

        double[] vecAbsoluteTolerance = new double[]{1.0e-5, 1.0e-5};
        double[] vecRelativeTolerance = new double[]{1.0e-5, 1.0e-5};

        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", false, c, a, b, prototype, 0.01, 0.05, vecAbsoluteTolerance, vecRelativeTolerance);

        // Test integration
        double[] yStart = new double[]{0.0, 0.0};
        double[] yEndExpected = new double[]{0.8308588030473352, 0.824298716518617};

        Assert.assertArrayEquals(yStart, integrator.getInitialState(), 0.0);

        integrator.integrate(new TestEquation(), 0.0);

        integrator.integrate(new TestEquation(), 0.2);

        Assert.assertArrayEquals(yEndExpected, integrator.getCompleteState(), 0.0);
    }
}