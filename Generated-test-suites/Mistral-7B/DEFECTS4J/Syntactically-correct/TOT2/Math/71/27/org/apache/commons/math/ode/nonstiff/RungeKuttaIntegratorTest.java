import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        double[] c = {0.5, 0.5, 1.0};
        double[][] a = {
            {0.0, 0.0, 0.0},
            {0.25, 0.0, 0.5},
            {0.0, 0.5, 0.5},
            {0.0, 1.0, 1.0},
        };
        double[] b = {0.5, 0.5, 1.0};
        integrator = new ClassicalRungeKuttaIntegrator(2, c, a, b, new DummyStepInterpolator(), 0.1);
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = Math.sin(y[0]);
            }
        };
    }

    @Test
    public void testIntegration() {
        double initialTime = 0.0;
        double[] initialState = {0.0};
        double finalTime = 3.0 * Math.PI;
        double[] finalState = {Math.sin(finalTime)};

        double result = integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
        assertEquals(finalTime, result, 1e-9);
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() {
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new DerivativeException("Test DerivativeException", new RuntimeException("Test DerivativeException"));
            }
        };
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                throw new IntegratorException("Test IntegratorException", new RuntimeException("Test IntegratorException"));
            }
        };
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                int i = 0;
                while (true) {
                    yPrime[0] = i++;
                }
            }
        };
        integrator.setMaxStep(1e-10);
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }

    @Test(expected = NoBracketException.class)
    public void testNoBracketException() {
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                double x = Math.sqrt(y[0]);
                yPrime[0] = 1.0 / x;
            }
        };
        integrator.integrate(equations, 0.0, new double[]{-1.0}, 1.0, new double[0]);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        equations = new FirstOrderDifferentialEquations() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yPrime) {
                yPrime[0] = -Math.sin(y[0]);
            }
        };
        integrator.integrate(equations, 0.0, new double[]{Math.PI}, 1.0, new double[0]);
    }

    @Test
    public void testEvents() {
        double initialTime = 0.0;
        double[] initialState = {0.0};
        double finalTime = 2.0 * Math.PI;
        double[] finalState = {Math.sin(finalTime)};

        StepHandler eventHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, int stepNumber) {
                if (stepNumber % 5 == 0) {
                    assertEquals(finalState[0], integrator.getY(), 1e-9);
                }
            }
        };
        integrator.addStepHandler(eventHandler);

        double result = integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
        assertEquals(finalTime, result, 1e-9);
    }

    @Test
    public void testStepSize() {
        double initialTime = 0.0;
        double[] initialState = {0.0};
        double finalTime = Math.PI;
        double[] finalState = {Math.sin(finalTime)};

        StepHandler stepSizeHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractIntegrator integrator, int stepNumber) {
                if (stepNumber % 2 == 0) {
                    integrator.setStepSize(integrator.getStepSize() * 2);
                }
            }
        };
        integrator.addStepHandler(stepSizeHandler);

        double result = integrator.integrate(equations, initialTime, initialState, finalTime, finalState);
        assertTrue(finalTime > result);
        assertEquals(Math.round(Math.log10(integrator.getStepSize())), Math.round(Math.log10(2.0)), 1);
    }

    @Test
    public void testEmptyStepHandlersManager() {
        integrator = new ClassicalRungeKuttaIntegrator(2, new double[0], new double[0][], new double[0], new DummyStepInterpolator(), 0.1);
        integrator.integrate(equations, 0.0, new double[0], 1.0, new double[0]);
    }
}