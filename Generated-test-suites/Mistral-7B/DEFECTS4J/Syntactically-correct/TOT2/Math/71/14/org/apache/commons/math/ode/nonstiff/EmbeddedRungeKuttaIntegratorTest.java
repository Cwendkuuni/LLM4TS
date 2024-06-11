import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.ContinuousIntegratorEvent;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    /**
     * Test the EmbeddedRungeKuttaIntegrator with a simple function.
     * @throws Exception if integration or test fail
     */
    @Test
    public void testSimpleFunction() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("RK4", false,
                        new double[]{-0.25, 0.25, 0.5, 0.875},
                        new double[][]{ {0.25, 0, 0, 0.25}, {0, 0.25, 0.25, 0}},
                        new double[]{ 0.25, 0.25 },
                        new RungeKuttaStepInterpolator(),
                        0.01, 10.0,
                        1.0e-6, 1.0e-6) {
                    @Override
                    protected double estimateError(double[][] yDotK,
                                                   double[] y0, double[] y1,
                                                   double h) {
                        return Math.abs(y1[0] - y0[0]) / (2.0 * h);
                    }
                };

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

        double[] initialValues = new double[]{ 0.0 };
        double  start          = 0.0;
        double  end            = 10.0;

        // integrate the first step
        double  result1       = integrator.integrate(equations, start, initialValues, start, initialValues);

        // check the result
        assertEquals(Math.sin(0.0), initialValues[0], 1.0e-6);
        assertEquals(Math.sin(start), initialValues[0], 1.0e-6);

        // integrate the next steps
        for (int i = 1; i < 10; ++i) {
            initialValues[0] = result1;
            result1 = integrator.integrate(equations, start + 0.1, initialValues,
                                          start + 0.1, initialValues);
            assertEquals(Math.sin(start + 0.1), initialValues[0], 1.0e-6);
        }
    }

    /**
     * Test the EmbeddedRungeKuttaIntegrator with a discontinuous function.
     * @throws Exception if integration or test fail
     */
    @Test
    public void testDiscontinuousFunction() throws Exception {
        EmbeddedRungeKuttaIntegrator integrator =
                new EmbeddedRungeKuttaIntegrator("RKF45", true,
                        new double[]{-0.25, 0.25, 0.5, 0.875, 1.0, 1.0},
                        new double[][]{ {0.25, 0, 0, 0.25, 0, 0},
                                       {0, 0.25, 0.25, 0, 0.25, 0},
                                       {0, 0, 0, 0.25, 0.25, 0},
                                       {0, 0, 0, 0, 1.0, 1.0},
                                       {0.5, 0.5, 0.5, 0.5, 0.5, 0.5},
                                       {1.0, 1.0, 1.0, 1.0, 1.0, 1.0} },
                        new double[]{ 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 },
                        new RungeKuttaStepInterpolator(),
                        0.01, 10.0,
                        1.0e-6, 1.0e-6) {
                    @Override
                    protected double estimateError(double[][] yDotK,
                                                   double[] y0, double[] y1,
                                                   double h) {
                        return Math.abs(y1[0] - y0[0]) / (2.0 * h);
                    }
                };

        FirstOrderDifferentialEquations equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = (t > 2.0) ? y[0] * y[0] : 1.0;
            }
        };

        double[] initialValues = new double[]{ 1.0 };
        double  start          = 0.0;
        double  end            = 10.0;

        // integrate the first step
        double  result1       = integrator.integrate(equations, start, initialValues, start, initialValues);

        // check the result
        assertEquals(1.0, initialValues[0], 1.0e-6);
        assertEquals(1.0, initialValues[0], 1.0e-6);

        // integrate the next steps
        for (int i = 1; i < 10; ++i) {
            initialValues[0] = result1;
            result1 = integrator.integrate(equations, start + 0.1, initialValues,
                                          start + 0.1, initialValues);
            if (start + 0.1 > 2.0) {
                assertEquals(Math.pow(initialValues[0], 2.0), initialValues[0], 1.0e-6);
            } else {
                assertEquals(1.0, initialValues[0], 1.0e-6);
            }
        }

        // test discontinuous event
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerEvent event) {
                if (event.getY()[0] > 2.0 && event.getStepNumber() > 0) {
                    throw new ContinuousIntegratorEvent(new When(2.5), null);
                }
            }
        });

        // integrate the first step
        try {
            integrator.integrate(equations, start, initialValues, start, initialValues);
            fail();
        } catch (Exception e) {
            // expected
        }

        // integrate the next steps
        initialValues[0] = 2.5;
        integrator.resetEvaluations();
        integrator.setEquations(equations);
        integrator.addStepHandler(new StepHandler() {
            @Override
            public void handleStep(StepHandlerEvent event) {
                if (event.getY()[0] > 2.5 && event.getStepNumber() > 0) {
                    throw new ContinuousIntegratorEvent(new When(3.0), null);
                }
            }
        });
        integrator.integrate(equations, start, initialValues, start, initialValues);
        assertEquals(Math.pow(2.5, 2.0), initialValues[0], 1.0e-6);
        initialValues[0] += 0.1;
        integrator.integrate(equations, start + 0.1, initialValues, start + 0.1, initialValues);
        assertEquals(Math.pow(2.625, 2.0), initialValues[0], 1.0e-6);
    }

}