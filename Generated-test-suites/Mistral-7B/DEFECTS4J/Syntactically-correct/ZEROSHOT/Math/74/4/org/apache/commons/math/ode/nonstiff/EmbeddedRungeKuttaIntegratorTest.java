package org.apache.commons.math.ode.nonstiff.embedded;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.test.TestODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorFactory;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;

public class EmbeddedRungeKuttaIntegratorTest {

    @SuppressWarnings("unchecked")
    private static class TestIntegrator extends EmbeddedRungeKuttaIntegrator {

        public TestIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b,
                RungeKuttaStepInterpolator prototype, double minStep, double maxStep,
                double scalAbsoluteTolerance, double scalRelativeTolerance) {
            super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance,
                 scalRelativeTolerance);
        }

        @Override
        protected int getOrder() {
            return 5;
        }

        @Override
        protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
            return 1.01;
        }

        @Override
        protected double filterStep(double h, boolean forward, boolean isLast) {
            return h;
        }
    }

    @SuppressWarnings("unchecked")
    private static class TestFactory implements EmbeddedRungeKuttaIntegratorFactory {

        @Override
        public EmbeddedRungeKuttaIntegrator createIntegrator(String name, boolean fsal,
                double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype,
                double minStep, double maxStep, double scalAbsoluteTolerance,
                double scalRelativeTolerance) {
            return new TestIntegrator(name, fsal, c, a, b, prototype, minStep, maxStep,
                    scalAbsoluteTolerance, scalRelativeTolerance);
        }

        @Override
        public RungeKuttaMethod getMethod() {
            return RungeKuttaMethod.RK54;
        }

        @Override
        public boolean isAutomaticStepSize() {
            return true;
        }

        @Override
        public boolean requiresDenseOutput() {
            return false;
        }

        @Override
        public String getName() {
            return "TestIntegrator";
        }
    }

    @SuppressWarnings("unchecked")
    private static class TestStepInterpolator extends RungeKuttaStepInterpolator {

        public TestStepInterpolator(EmbeddedRungeKuttaIntegrator integrator,
                double[] yTmp, double[][] yDotK, boolean forward) {
            super(integrator, yTmp, yDotK, forward);
        }

        @Override
        public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] yTmp,
                double[][] yDotK, boolean forward) {
            super.reinitialize(integrator, yTmp, yDotK, forward);
        }
    }

    private static class TestStepHandler implements StepHandler {

        private int count;

        @Override
        public void handleStep(StepHandlerContext context, int step) {
            count++;
        }

        public int getCount() {
            return count;
        }

        public void reset() {
            count = 0;
        }
    }

    @org.junit.Test
    public void testIntegration() throws Exception {
        final double[] x = new double[] { 0.0, 1.0 };
        final double[] y0 = new double[] { 1.0, 1.0 };
        final double[] y1 = new double[] { 2.0, 2.0 };
        final double[] y2 = new double[] { 3.0, 3.0 };
        final double[] y3 = new double[] { 4.0, 4.0 };
        final double  t0 = 0.0;
        final double  t1 = 1.0;

        final double[] c = { 0.23269956, 0.23269956, 0.23269956, 0.23269956, 0.1944 };
        final double[][] a = { { 0.0,   0.0,   0.0,   0.0,   0.0 },
                               { 0.23269956, 0.13839473, 0.0,   0.0,   0.0 },
                               { 0.37366583, 0.37366583, 0.23269956, 0.0,   0.0 },
                               { 0.45322711, 0.54057870, 0.45322711, 0.23269956, 0.0 },
                               { 0.51121827, 0.61271368, 0.51121827, 0.51121827, 0.23269956 } };
        final double[] b = { 0.23269956, 0.39238283, 0.43665827, 0.45322711, 0.47223421 };
        final RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator();

        final EmbeddedRungeKuttaIntegrator integrator =
                new TestFactory().createIntegrator("TestIntegrator", false, c, a, b, prototype,
                        0.001, 0.01, 1.0e-10, 1.0e-10);

        final TestStepInterpolator interpolator = new TestStepInterpolator(integrator, new double[2],
                new double[2][5], true);

        final TestStepHandler stepHandler = new TestStepHandler();
        integrator.setStepHandler(stepHandler);

        final FirstOrderDifferentialEquations equations = new TestODE() {
            @Override
            public int getDimension() {
                return 2;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = y[1];
                yPrime[1] = y[0];
            }
        }.reverse();

        final double t = integrator.integrate(equations, t0, y0, t1, y1);
        assertNotNull(t);
        assertTrue(t >= t1);

        assertEquals(2, stepHandler.getCount());
        stepHandler.reset();

        integrator.integrate(equations, t, y1, t1, y2);
        assertEquals(4, stepHandler.getCount());

        integrator.integrate(equations, t, y2, t1, y3);
        assertEquals(6, stepHandler.getCount());

        integrator.integrate(equations, t, y3, t1, y3);
        assertEquals(8, stepHandler.getCount());

        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(10.0);
        integrator.setSafety(0.8);

        final double[] x2 = new double[] { -1.0, 0.0, 1.0 };
        final double[] y02 = new double[] { 1.0, 0.0 };
        final double[] y12 = new double[] { 0.5, 1.0 };
        final double[] y22 = new double[] { 1.0, 1.0 };
        final double[] y32 = new double[] { 1.5, 1.5 };
        final double[] y42 = new double[] { 2.0, 2.0 };

        final double[] c2 = { 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3 };
        final double[][] a2 = { { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 },
                               { 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3 },
                               { 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4 },
                               { 0.36, 0.36, 0.36, 0.36, 0.36, 0.36, 0.36, 0.36 },
                               { 0.277, 0.277, 0.277, 0.277, 0.277, 0.277, 0.277, 0.277 },
                               { 0.168, 0.168, 0.168, 0.168, 0.168, 0.168, 0.168, 0.168 },
                               { 0.11, 0.11, 0.11, 0.11, 0.11, 0.11, 0.11, 0.11 },
                               { 0.055556, 0.055556, 0.055556, 0.055556, 0.055556, 0.055556, 0.055556, 0.055556 } };
        final double[] b2 = { 0.3, 0.3, 0.34724138, 0.3826834, 0.41132454, 0.41615241, 0.41132454, 0.3826834 };

        final EmbeddedRungeKuttaIntegrator integrator2 =
                new TestFactory().createIntegrator("TestIntegrator", true, c2, a2, b2, prototype,
                        0.001, 0.01, null, null);

        final TestStepInterpolator interpolator2 = new TestStepInterpolator(integrator2, new double[2],
                new double[2][8], false);

        final TestStepHandler stepHandler2 = new TestStepHandler();
        integrator2.setStepHandler(stepHandler2);

        final double step = 1.0 / 10.0;
        integrator2.reset();
        integrator2.setStep(step);

        final double[] y = new double[2];
        y[0] = 1.0;
        y[1] = 0.0;

        integrator2.integrate(new TestEvent(1.0), equations, y, y, step);
        assertEquals(1, stepHandler2.getCount());

        integrator2.integrate(new TestEvent(1.5), equations, y, y, step);
        assertEquals(2, stepHandler2.getCount());

        integrator2.integrate(equations, y, y, 2.0, y);
        assertEquals(6, stepHandler2.getCount());

        integrator2.reset();
        integrator2.setStepSize(step);

        integrator2.integrate(new TestEvent(1.0), equations, y, y, step);
        assertEquals(1, stepHandler2.getCount());

        integrator2.integrate(equations, y, y, 1.5, y);
        assertEquals(2, stepHandler2.getCount());

        integrator2.integrate(equations, y, y, 2.0, y);
        assertEquals(6, stepHandler2.getCount());

        assertFalse(integrator2.hasMoreSteps());

        integrator2.reset();
        integrator2.setStepSize(2.0);

        integrator2.integrate(new TestEvent(1.0), equations, y, y, 2.0);
        assertEquals(1, stepHandler2.getCount());

        integrator2.integrate(equations, y, y, 1.5, y);
        assertEquals(1, stepHandler2.getCount());

        integrator2.integrate(equations, y, y, 2.0, y);
        assertEquals(1, stepHandler2.getCount());

        assertFalse(integrator2.hasMoreSteps());

        integrator2.reset();
        integrator2.setStepSize(0.5);

        integrator2.integrate(new TestEvent(1.0), equations, y, y, 0.5);
        assertEquals(1, stepHandler2.getCount());

        integrator2.integrate(equations, y, y, 1.5, y);
        assertEquals(3, stepHandler2.getCount());

        integrator2.integrate(equations, y, y, 2.0, y);
        assertEquals(6, stepHandler2.getCount());

        integrator2.integrate(equations, y, y, 2.0, y);
        assertEquals(6, stepHandler2.getCount());

        assertFalse(integrator2.hasMoreSteps());

    }

    private static class TestEvent extends Event {

        public TestEvent(double t) {
            super(t, null);
        }

        @Override
        public boolean isRegular() {
            return true;
        }

        @Override
        public boolean isAbsolute() {
            return true;
        }
    }

}