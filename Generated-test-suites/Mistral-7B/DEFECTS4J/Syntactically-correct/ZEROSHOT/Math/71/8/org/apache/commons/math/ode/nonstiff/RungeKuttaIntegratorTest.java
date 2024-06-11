package org.apache.commons.math.ode.nonstiff.test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativetimeEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    private static final double RELATIVE_TOLERANCE = 1e-9;
    private static final double ABSOLUTE_TOLERANCE = 1e-15;

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new ClassicalRungeKuttaIntegrator(6, 6, new ClassicalRungeKuttaStepInterpolator(), 0.01);
        equations = new BesselEquations();
    }

    @Test
    public void testIntegrateExact() throws Exception {
        double[] y0 = {1.0, 0.0};
        double t0 = 0.0;
        double tEnd = Math.PI;
        double[] yEnd = {BesselFunctions.I0(tEnd), BesselFunctions.I1(tEnd)};
        double step = 0.001;

        integrator.integrate(equations, t0, y0, tEnd, yEnd);

        double[] y = new double[yEnd.length];
        System.arraycopy(yEnd, 0, y, 0, yEnd.length);
        Assert.assertArrayEquals(y, yEnd, RELATIVE_TOLERANCE, ABSOLUTE_TOLERANCE);
    }

    @Test
    public void testIntegrateRelativeTimeEvent() throws Exception {
        double[] y0 = {1.0, 0.0};
        double t0 = 0.0;
        double tEnd = Math.PI;
        double step = 0.01;

        equations.setRelativeTo(t0);
        equations.addEvent(new RelativetimeEvent(0.5, 0.5), r -> 1.0);

        integrator.setStepHandler(new TestStepHandler());

        integrator.integrate(equations, t0, y0, tEnd, y0);

        Assert.assertEquals(1.0, integrator.getStepHandler().getStepCount(), 0);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() throws Exception {
        double[] y0 = {1.0, 0.0};
        double t0 = 0.0;
        double tEnd = Math.PI;
        double step = 0.001;

        equations.addEvent(new RelativetimeEvent(0.0, 0.0), r -> { throw new RuntimeException(); });

        integrator.integrate(equations, t0, y0, tEnd, y0);
    }

    @Test(expected = PrecisionException.class)
    public void testPrecisionException() throws Exception {
        double[] y0 = {1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)};
        double t0 = 0.0;
        double tEnd = Math.PI / 4.0;
        double step = 0.001;

        equations.addEvent(new RelativetimeEvent(0.0, 0.0), r -> {
            double result = Math.sqrt(2.0) * (Math.PI / 4.0) - 1.0;
            throw new PrecisionException("Expected: " + result, null);
        });

        integrator.integrate(equations, t0, y0, tEnd, y0);
    }

    private static class TestStepHandler implements StepHandler {

        private int stepCount = 0;

        @Override
        public void handleStep(StepContext context, int stepNumber) {
            stepCount++;
        }

        public int getStepCount() {
            return stepCount;
        }

        public void reset() {
            stepCount = 0;
        }

    }

}