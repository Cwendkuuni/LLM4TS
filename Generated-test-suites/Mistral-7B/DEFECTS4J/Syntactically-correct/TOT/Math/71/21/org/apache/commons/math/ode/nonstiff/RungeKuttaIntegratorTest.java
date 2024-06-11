import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.ContinuousIntervalEvent;
import org.apache.commons.math3.ode.events.EndEvent;
import org.apache.commons.math3.ode.events.RelativesSizeEvent;
import org.apache.commons.math3.ode.events.StepHandler;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the {@link RungeKuttaIntegrator} class.
 */
public class RungeKuttaIntegratorTest {

    /**
     * Test a simple differential equation with no events.
     * @throws MathException if any error occurs during integration
     */
    @Test
    public void testSimpleDifferentialEquation() throws MathException {
        // create the differential equations
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        // create the integrator
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", new double[] {0.25, 0.25, 0.25, 0.25},
                new double[][] {{0.0, 0.25, 0.25, 0.5, 0.875, 1.0}, {0.25, 0, 0.25, 0.25, 0.5, 0.875},
                        {0.25, 0.25, 0, 0.25, 0.25, 0.5}, {0.5, 0.25, 0.25, 0, 0.25, 0.25}},
                new double[] {0.5, 0.5, 0.5, 0.5});

        // set up the step handler
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int stepNumber) {
                double y = interpolator.getY()[0];
                Assert.assertEquals(Math.sin(y), interpolator.getYDot()[0], 1e-8);
            }
        };

        integrator.addStepHandler(stepHandler);

        // integrate the equations
        double tStart = 0.0;
        double yStart = 0.0;
        double tEnd   = 5.0;
        double[] yEnd = new double[] {Math.sin(Math.PI / 4.0)};
        double step  = 0.1;
        double result = integrator.integrate(equations, tStart, new double[] {yStart}, tEnd, new double[yEnd.length]);

        // check the results
        Assert.assertEquals(result, tEnd, 1e-8);
        Assert.assertArrayEquals(yEnd, integrator.getSolution(), 1e-8);
    }

    /**
     * Test a differential equation with a continuous event.
     * @throws MathException if any error occurs during integration
     */
    @Test
    public void testContinuousEvent() throws MathException {
        // create the differential equations
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        // create the integrator
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", new double[] {0.25, 0.25, 0.25, 0.25},
                new double[][] {{0.0, 0.25, 0.25, 0.5, 0.875, 1.0}, {0.25, 0, 0.25, 0.25, 0.5, 0.875},
                        {0.25, 0.25, 0, 0.25, 0.25, 0.5}, {0.5, 0.25, 0.25, 0, 0.25, 0.25}},
                new double[] {0.5, 0.5, 0.5, 0.5});

        // create the event handler
        StepHandler eventHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int stepNumber) {
                if (interpolator.getTime() > 2.0) {
                    throw new ContinuousIntervalEvent(interpolator.getTime());
                }
            }
        };
        integrator.addStepHandler(eventHandler);

        // integrate the equations
        double tStart = 0.0;
        double yStart = 0.0;
        double tEnd   = 5.0;
        double[] yEnd = new double[] {Math.sin(Math.PI / 4.0)};
        double step  = 0.1;
        try {
            integrator.integrate(equations, tStart, new double[] {yStart}, tEnd, new double[yEnd.length]);
            Assert.fail();
        } catch (IntegratorException e) {
            // expected
        }

        // reset the event handler
        integrator.removeStepHandler(eventHandler);

        // integrate the equations without the event
        integrator.reset();
        integrator.integrate(equations, tStart, new double[] {yStart}, tEnd, new double[yEnd.length]);

        // check the results
        Assert.assertEquals(integrator.getSolution()[0], Math.sin(Math.PI / 4.0), 1e-8);
    }

    /**
     * Test a differential equation with a relative size event.
     * @throws MathException if any error occurs during integration
     */
    @Test
    public void testRelativeSizeEvent() throws MathException {
        // create the differential equations
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        // create the integrator
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", new double[] {0.25, 0.25, 0.25, 0.25},
                new double[][] {{0.0, 0.25, 0.25, 0.5, 0.875, 1.0}, {0.25, 0, 0.25, 0.25, 0.5, 0.875},
                        {0.25, 0.25, 0, 0.25, 0.25, 0.5}, {0.5, 0.25, 0.25, 0, 0.25, 0.25}},
                new double[] {0.5, 0.5, 0.5, 0.5});

        // create the event handler
        StepHandler eventHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int stepNumber) {
                if (Math.abs(interpolator.getY()[0] - 1.0) > 1e-8) {
                    throw new RelativesSizeEvent(1.0);
                }
            }
        };
        integrator.addStepHandler(eventHandler);

        // integrate the equations
        double tStart = 0.0;
        double yStart = 0.0;
        double tEnd   = 5.0;
        double[] yEnd = new double[] {1.0};
        double step  = 0.1;
        try {
            integrator.integrate(equations, tStart, new double[] {yStart}, tEnd, new double[yEnd.length]);
            Assert.fail();
        } catch (IntegratorException e) {
            // expected
        }

        // reset the event handler
        integrator.removeStepHandler(eventHandler);

        // integrate the equations without the event
        integrator.reset();
        integrator.integrate(equations, tStart, new double[] {yStart}, tEnd, new double[yEnd.length]);

        // check the results
        Assert.assertEquals(integrator.getSolution()[0], 1.0, 1e-8);
    }

    /**
     * Test a differential equation with an end event.
     * @throws MathException if any error occurs during integration
     */
    @Test
    public void testEndEvent() throws MathException {
        // create the differential equations
        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };

        // create the integrator
        RungeKuttaIntegrator integrator = new RungeKuttaIntegrator("RK4", new double[] {0.25, 0.25, 0.25, 0.25},
                new double[][] {{0.0, 0.25, 0.25, 0.5, 0.875, 1.0}, {0.25, 0, 0.25, 0.25, 0.5, 0.875},
                        {0.25, 0.25, 0, 0.25, 0.25, 0.5}, {0.5, 0.25, 0.25, 0, 0.25, 0.25}},
                new double[] {0.5, 0.5, 0.5, 0.5});

        // create the event handler
        StepHandler eventHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator, int stepNumber) {
                if (interpolator.getTime() > 3.0) {
                    throw new EndEvent();
                }
            }
        };
        integrator.addStepHandler(eventHandler);

        // integrate the equations
        double tStart = 0.0;
        double yStart = 0.0;
        double tEnd   = 5.0;
        double[] yEnd = new double[] {Math.sin(Math.PI / 4.0)};
        double step  = 0.1;
        try {
            integrator.integrate(equations, tStart, new double[] {yStart}, tEnd, new double[yEnd.length]);
            Assert.fail();
        } catch (IntegratorException e) {
            // expected
        }

        // reset the event handler
        integrator.removeStepHandler(eventHandler);

        // integrate the equations without the event
        integrator.reset();
        integrator.integrate(equations, tStart, new double[] {yStart}, tEnd, new double[yEnd.length]);

        // check the results
        Assert.assertEquals(integrator.getSolution()[0], Math.sin(Math.PI / 4.0), 1e-8);
    }

}