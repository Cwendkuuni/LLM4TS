import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.NonstiffIntegrator;
import org.apache.commons.math3.ode.events.RelativeEvent;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;
    private double[] initialValues = {0.0, 1.0};

    @Before
    public void setup() {
        equations = new SimpleDifferentialEquations();
        integrator = new RungeKutta2Integrator("RK2", new double[]{0.5, 0.5},
                new double[][]{{0.25, 0.25}, {0.25, 0.0}}, new double[]{0.5, 0.5},
                new RungeKuttaStepInterpolator(), 0.01);
    }

    @Test
    public void testBasicFunctionality() {
        double[] finalValues = integrator.integrate(equations, 0.0, initialValues, 1.0, new double[initialValues.length]);
        Assert.assertArrayEquals(new double[]{1.26, 1.13}, finalValues, 0.001);
    }

    @Test(expected = DeriveException.class)
    public void testDerivativeException() {
        equations = new ThrowsDerivativeExceptionDifferentialEquations();
        initialValues = new double[]{0.0, 1.0};
        integrator.integrate(equations, 0.0, initialValues, 1.0, new double[initialValues.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() {
        equations = new ThrowsIntegratorExceptionDifferentialEquations();
        initialValues = new double[]{0.0, 1.0};
        integrator.integrate(equations, 0.0, initialValues, 1.0, new double[initialValues.length]);
    }

    @Test
    public void testRelativeEvent() {
        double[][] eventValues = {{1.0, 1.0}, {2.0, 2.0}};
        RelativeEvent event = new RelativeEvent(1.5, 1.0, 0.01);
        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(StepInterpolator interpolator, int stepNumber) {
                if (stepNumber == 1) {
                    integrator.addEventHandler(event);
                }
            }
        };

        integrator.addStepHandler(stepHandler);
        double[] finalValues = integrator.integrate(equations, 0.0, initialValues, 1.5, new double[initialValues.length]);
        Assert.assertArrayEquals(eventValues[0], finalValues, 0.001);
    }

}

class SimpleDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        yDot[0] = y[1];
        yDot[1] = -y[0];
    }
}

class ThrowsDerivativeExceptionDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        throw new DerivativeException("Derivative exception");
    }
}

class ThrowsIntegratorExceptionDifferentialEquations implements FirstOrderDifferentialEquations {
    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        throw new IntegratorException("Integrator exception");
    }
}