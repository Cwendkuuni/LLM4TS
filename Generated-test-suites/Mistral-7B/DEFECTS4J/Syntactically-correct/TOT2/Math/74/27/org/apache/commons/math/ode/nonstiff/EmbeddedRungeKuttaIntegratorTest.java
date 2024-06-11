package org.apache.commons.math.ode.nonstiff;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberFormatException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.MockEvent;
import org.apache.commons.math3.ode.events.RelValueEvent;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator.Order;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the {@link EmbeddedRungeKuttaIntegrator} class.
 */
public class EmbeddedRungeKuttaIntegratorTest {

    /** The integrator under test. */
    private EmbeddedRungeKuttaIntegrator integrator;

    /** The equations to integrate. */
    private final FirstOrderDifferentialEquations equations = new MockDifferentialEquations();

    /** The initial condition. */
    private double[] y0 = new double[]{ 1.0, 0.0 };

    /** The final time. */
    private double t = 10.0;

    @Before
    public void setUp() {
        integrator = new Fehlberg78Integrator(Order.SEVENTH_ORDER, 0.0, 0.0,
                new double[]{}, new double[]{},
                new RungeKuttaStepInterpolator(), 0.001, 10.0, null, null);
    }

    /**
     * Test integration with no events.
     * @throws Exception if an error occurs during integration
     */
    @Test
    public void testIntegration() throws Exception {
        double result = integrator.integrate(equations, 0.0, y0, t, y0);
        Assert.assertEquals(t, result, 1e-14);
    }

    /**
     * Test integration with an event at the final time.
     * @throws Exception if an error occurs during integration
     */
    @Test
    public void testIntegrationWithEvent() throws Exception {
        integrator.addEvent(new RelValueEvent(t, 1.0, new MockEventHandler()));
        integrator.integrate(equations, 0.0, y0, t, y0);
    }

    /**
     * Test singular value decomposition of the Butcher array.
     * @throws SingularMatrixException if the Butcher array is singular
     * @throws NumberFormatException if an error occurs parsing the Butcher
     * array as a matrix
     */
    @Test(expected = SingularMatrixException.class)
    public void testSingularButcherArray() throws SingularMatrixException, NumberFormatException {
        double[][] butcherArray = { { 0.0, 0.0 }, { 0.0, 0.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(butcherArray);
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        svd.getS();
    }

    /**
     * Test error estimation.
     * @throws MaxCountExceededException if the number of iterations exceeds
     * the maximum number allowed by the integrator
     * @throws DerivativeException if an error occurs during derivative
     * evaluation
     * @throws IntegratorException if an error occurs during integration
     */
    @Test
    public void testErrorEstimation() throws MaxCountExceededException, DerivativeException, IntegratorException {
        double[] y1 = new double[]{ 1.0 + 1e-13, 0.0 };
        double[] y2 = new double[]{ 1.0 + 1e-14, 0.0 };
        double h = 0.1;
        integrator.computeDerivatives(0.0, y0, yDotK0);
        double error = integrator.estimateError(yDotK, y0, y1, h);
        Assert.assertTrue(error > 1.0);

        integrator.resetEvaluations();
        integrator.computeDerivatives(0.0, y0, yDotK0);
        error = integrator.estimateError(yDotK, y0, y2, h);
        Assert.assertTrue(error > 1.0);
    }

    /**
     * Mock differential equations for testing.
     */
    private static class MockDifferentialEquations implements FirstOrderDifferentialEquations {

        /** {@inheritDoc} */
        public void computeDerivatives(double t, double[] y, double[] yPrime) {
            yPrime[0] = y[1];
            yPrime[1] = -y[0];
        }

    }

    /**
     * Mock event handler for testing.
     */
    private static class MockEventHandler implements StepHandler {

        /** {@inheritDoc} */
        public void handleStep(StepHandlerContext context, int stepNumber) {
            throw new RuntimeException("Mock event handler exception");
        }

    }

}