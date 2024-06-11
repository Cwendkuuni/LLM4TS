package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class EmbeddedRungeKuttaIntegratorTest {

    private final double[] c;
    private final double[][] a;
    private final double[] b;
    private final String name;
    private final int order;
    private final boolean fsal;

    public EmbeddedRungeKuttaIntegratorTest(double[] c, double[][] a, double[] b, String name, int order, boolean fsal) {
        this.c = c;
        this.a = a;
        this.b = b;
        this.name = name;
        this.order = order;
        this.fsal = fsal;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new double[]{0.3, 0.4, 0.1, 0.2}, new double[][]{{0.0, 0.5, 0.5, 0.5}, {0.1, 0.1, 0.2, 0.2}},
                        new double[]{0.2, 0.1, 0.1, 0.1}, "RK23", 2, false},
                {new double[]{0.3, 0.4, 0.2, 0.1, 0.1}, new double[][]{{0.0, 0.4, 0.2, 0.2, 0.2}, {0.0, 0.2, 0.1, 0.2, 0.1}},
                        new double[]{0.3, 0.3, 0.2, 0.2, 0.1}, "RK45", 4, false},
                {new double[]{0.3, 0.4, 0.3}, new double[][]{{0.0, 0.3, 0.4}, {0.0, 0.2, 0.4, 0.2}, {0.0, 0.1, 0.4, 0.3, 0.1}},
                        new double[]{0.1, 0.1, 0.1}, "RK34", 3, true}
        });
    }

    @Test
    public void testIntegration() throws PrecisionException, MaxCountExceededException, IntegratorException {
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(c.length, a, b);
        EmbeddedRungeKuttaIntegrator integrator = null;
        switch (order) {
            case 2:
                integrator = new RK23Integrator(name, fsal, c, a, b, prototype, 0.01, 0.01, null, null);
                break;
            case 4:
                integrator = new RK45Integrator(name, fsal, c, a, b, prototype, 0.01, 0.01, null, null);
                break;
            case 3:
                integrator = new RK34Integrator(name, fsal, c, a, b, prototype, 0.01, 0.01, null, null);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + order);
        }

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return a[0].length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * Math.exp(t);
                }
            }
        };

        double[] y0 = new double[a[0].length];
        Arrays.fill(y0, 1.0);

        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[a[0].length];
        Arrays.fill(y, 1.0);
        integrator.integrate(equations, t0, y0, t, y);

        double[] expected = new double[a[0].length];
        Arrays.fill(expected, Math.exp(t));
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Mismatch at component " + i, expected[i], y[i], 1e-12);
        }

        // test event handling
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepHandlerContext context, int stepNumber) {
                if (stepNumber % 2 == 0) {
                    context.setStopRequested(true);
                }
            }
        };
        integrator.addStepHandler(stepHandler);

        List<Event> events = Arrays.asList(
                new When(1.5, new Action() {
                    @Override
                    public void evaluate(double t, double[] y, double[] yPrime, int stepNumber, boolean eventOccured, int level) {
                        if (eventOccured) {
                            throw new IntegratorException("Event occurred.");
                        }
                        assertEquals("Unexpected value before event", 1.2, y[0], 1e-12);
                        assertEquals("Unexpected value before event", 2.5, y[1], 1e-12);
                    }
                }));
        integrator.setEventsManager(new CombinedEventsManager(events));

        double tLast = integrator.integrate(equations, t0, y0, t, y);
        assertTrue("Integration should have reached the event", stepHandler.isStopRequested());
        assertEquals("Unexpected last time", 1.5, tLast, 1e-12);
    }

    @Test
    public void testIntegrationWithAbsoluteAndRelativeTolerances()
            throws PrecisionException, MaxCountExceededException, IntegratorException {
        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(c.length, a, b);
        EmbeddedRungeKuttaIntegrator integrator = null;
        switch (order) {
            case 2:
                integrator = new RK23Integrator(name, fsal, c, a, b, prototype, 0.01, 0.01, new double[]{1e-3, 1e-3}, new double[]{1e-4, 1e-3});
                break;
            case 4:
                integrator = new RK45Integrator(name, fsal, c, a, b, prototype, 0.01, 0.01, new double[]{1e-3, 1e-3}, new double[]{1e-4, 1e-3});
                break;
            case 3:
                integrator = new RK34Integrator(name, fsal, c, a, b, prototype, 0.01, 0.01, new double[]{1e-3, 1e-3}, new double[]{1e-4, 1e-3});
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + order);
        }

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return a[0].length;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                for (int i = 0; i < y.length; ++i) {
                    yPrime[i] = y[i] * Math.exp(t);
                }
            }
        };

        double[] y0 = new double[a[0].length];
        Arrays.fill(y0, 1.0);

        double t0 = 0.0;
        double t = 1.0;
        double[] y = new double[a[0].length];
        Arrays.fill(y, 1.0);
        integrator.integrate(equations, t0, y0, t, y);

        double[] expected = new double[a[0].length];
        Arrays.fill(expected, Math.exp(t));
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Mismatch at component " + i, expected[i], y[i], 1e-12);
        }

        // check absolute tolerance
        double[] y1 = new double[a[0].length];
        Arrays.fill(y1, 1.01);
        integrator.integrate(equations, t0, y1, t, y);
        double errorAbs = 0.0;
        for (int i = 0; i < y.length; ++i) {
            errorAbs += Math.abs(y[i] - y1[i]);
        }
        assertTrue("Absolute tolerance too small", errorAbs > 1e-3);

        // check relative tolerance
        double[] y2 = new double[a[0].length];
        Arrays.fill(y2, 2.0);
        integrator.integrate(equations, t0, y2, t, y);
        double errorRel = 0.0;
        for (int i = 0; i < y.length; ++i) {
            errorRel += Math.abs(y[i] - y2[i]) / Math.max(1.0, Math.abs(y[i]));
        }
        assertTrue("Relative tolerance too small", errorRel > 1e-4);
    }

}