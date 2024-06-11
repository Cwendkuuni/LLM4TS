import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        // Test with a simple differential equation
        FirstOrderDifferentialEquations equations = new SimpleDifferentialEquation();
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta23(
                1.0, new RungeKuttaStepInterpolator(4), 0.001, 0.01,
                new double[] {0.0, 0.0}, new double[] {0.05, 0.05},
                new double[] {1.0e-8, 1.0e-8});

        double[] result = integrator.integrate(equations, 0.0, new double[] {1.0},
                10.0, new double[equations.getStateDimension()]);

        // Check the result
        double[] expected = new double[] {1.7466124462333323, 1.7595332585535275};
        assertArrayEquals(expected, result, 1e-8);

        // Test with an event
        equations = new SimpleDifferentialEquationWithEvent();
        integrator = new EmbeddedRungeKuttaFehlberg78(
                7, new RungeKuttaStepInterpolator(7), 0.001, 0.01,
                new double[] {0.0, 0.0}, new double[] {0.05, 0.05},
                new double[] {1.0e-8, 1.0e-8});

        StepHandler eventHandler = new StepEventHandler();

        integrator.addStepHandler(eventHandler);

        double startTime = 0.0;
        double endTime = 2.0;

        integrator.integrate(equations, startTime, new double[] {1.0}, endTime,
                new double[equations.getStateDimension()]);

        // Check the event was triggered
        assertEquals(1, eventHandler.getEventCount());
        Event event = eventHandler.getLastEvent();
        assertEquals(1.0, event.getTime(), 1e-8);

        // Test with an exception
        equations = new SimpleDifferentialEquationWithException();
        integrator = new EmbeddedRungeKuttaFehlberg78(
                7, new RungeKuttaStepInterpolator(7), 0.001, 0.01,
                new double[] {0.0, 0.0}, new double[] {0.05, 0.05},
                new double[] {1.0e-8, 1.0e-8});

        try {
            integrator.integrate(equations, 0.0, new double[] {1.0}, 1.0,
                    new double[equations.getStateDimension()]);
            fail("Expected IntegratorException");
        } catch (IntegratorException e) {
            // Expected
        }

        // Test with edge cases
        equations = new SimpleDifferentialEquationEdgeCases();
        integrator = new EmbeddedRungeKutta23(
                1.0, new RungeKuttaStepInterpolator(4), 0.001, 0.01,
                new double[] {0.0, 0.0}, new double[] {0.05, 0.05},
                new double[] {1.0e-8, 1.0e-8});

        result = integrator.integrate(equations, -1.0, new double[] {1.0},
                1.0, new double[equations.getStateDimension()]);
        assertArrayEquals(new double[] {1.0, 1.0}, result, 1e-8);

        result = integrator.integrate(equations, 1.0, new double[] {1.0},
                -1.0, new double[equations.getStateDimension()]);
        assertArrayEquals(new double[] {1.0, 1.0}, result, 1e-8);

        result = integrator.integrate(equations, 1.0, new double[] {1.0},
                1.0, new double[equations.getStateDimension()]);
        assertArrayEquals(new double[] {1.0, 1.0}, result, 1e-8);

        // Test with large steps
        integrator = new EmbeddedRungeKutta23(
                1.0, new RungeKuttaStepInterpolator(4), 10.0, 10.0,
                new double[] {0.0, 0.0}, new double[] {0.05, 0.05},
                new double[] {1.0e-8, 1.0e-8});

        result = integrator.integrate(equations, 0.0, new double[] {1.0},
                10.0, new double[equations.getStateDimension()]);
        assertArrayEquals(new double[] {1.5707963267948966, 1.6277636028225073},
                result, 1e-8);

        // Test with small steps
        integrator = new EmbeddedRungeKutta23(
                1.0, new RungeKuttaStepInterpolator(4), 0.0001, 0.0001,
                new double[] {0.0, 0.0}, new double[] {0.05, 0.05},
                new double[] {1.0e-8, 1.0e-8});

        result = integrator.integrate(equations, 0.0, new double[] {1.0},
                10.0, new double[equations.getStateDimension()]);
        assertTrue(result.length > 5000);

        // Test with a large number of dimensions
        equations = new SimpleDifferentialEquationManyDimensions();
        integrator = new EmbeddedRungeKuttaFehlberg78(
                7, new RungeKuttaStepInterpolator(7), 0.001, 0.01,
                new double[] {0.0}, new double[] {0.05},
                new double[] {1.0e-8, 1.0e-8});

        result = integrator.integrate(equations, 0.0, new double[equations.getStateDimension()],
                10.0, new double[equations.getStateDimension()]);
        assertEquals(equations.getStateDimension(), result.length);
        for (int i = 0; i < equations.getStateDimension(); ++i) {
            assertEquals(Math.exp(i), result[i], 1e-8);
        }
    }

}

class SimpleDifferentialEquation implements FirstOrderDifferentialEquations {
    public int getStateDimension() {
        return 2;
    }

    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -y[0];
    }
}

class SimpleDifferentialEquationWithEvent implements FirstOrderDifferentialEquations {
    private int eventCount;

    public int getStateDimension() {
        return 1;
    }

    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = 1.0;
        if (t >= 1.0) {
            eventCount++;
            yDot[0] = Double.NaN;
        }
    }

    public int getEventCount() {
        return eventCount;
    }

    public Event getLastEvent() {
        if (eventCount > 0) {
            eventCount--;
            return new Event() {
                @Override
                public double getTime() {
                    return 1.0;
                }
            };
        } else {
            throw new IllegalStateException();
        }
    }
}

class SimpleDifferentialEquationWithException implements FirstOrderDifferentialEquations {
    public int getStateDimension() {
        return 1;
    }

    public void computeDerivatives(double t, double[] y, double[] yDot) {
        throw new RuntimeException("Exception in computeDerivatives");
    }
}

class SimpleDifferentialEquationEdgeCases implements FirstOrderDifferentialEquations {
    public int getStateDimension() {
        return 2;
    }

    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -y[0];
    }
}

class SimpleDifferentialEquationManyDimensions implements FirstOrderDifferentialEquations {
    public int getStateDimension() {
        return 10;
    }

    public void computeDerivatives(double t, double[] y, double[] yDot) {
        for (int i = 0; i < y.length; ++i) {
            yDot[i] = Math.exp(i);
        }
    }
}

class StepEventHandler implements StepHandler {
    private int eventCount;

    public void handleStep(StepHandler handler, boolean lastStep) {
        eventCount++;
    }

    public int getEventCount() {
        return eventCount;
    }

    public Event getLastEvent() {
        if (eventCount > 0) {
            eventCount--;
            return new Event() {
                @Override
                public double getTime() {
                    return handler.getStepTime();
                }
            };
        } else {
            throw new IllegalStateException();
        }
    }
}