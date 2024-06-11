import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketException;
import org.apache.commons.math3.exception.RootFindException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.RelativisticEvent;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link RungeKuttaIntegrator}.
 */
public class RungeKuttaIntegratorTest {

    private RungeKuttaIntegrator integrator;
    private FirstOrderDifferentialEquations equations;

    @Before
    public void setUp() {
        integrator = new RungeKuttaIntegrator("RK4",
            new double[] { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 },
            new double[][] { { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 / 6.0 },
                             { 0.0, 0.0, 1.0 / 2.0, 1.0 / 2.0 },
                             { 0.0, 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0 },
                             { 0.0, 0.0, 1.0 / 2.0, 1.0 } },
            new double[] { 1.0 / 6.0, 1.0 / 2.0, 1.0 / 2.0, 1.0 },
            new RungeKuttaStepInterpolator() {});
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
    public void testStandardUseCase() throws Exception {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double tEnd = Math.PI;
        double stepSize = 0.1;

        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);

        Assert.assertEquals(Math.sin(tEnd), y0[0], 1e-9);
    }

    @Test
    public void testZeroStepSize() throws Exception {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double tEnd = Math.PI;
        double stepSize = 0.0;

        try {
            integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testStepHandler() throws Exception {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double tEnd = Math.PI / 2.0;
        double stepSize = 0.1;

        StepHandler stepHandler = new StepHandler() {
            @Override
            public void handleStep(AbstractStepInterpolator interpolator,
                                   boolean last) {
                if (last) {
                    Assert.assertEquals(Math.PI / 2.0, interpolator.getTime(), 1e-9);
                }
            }
        };

        integrator.addStepHandler(stepHandler);
        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);
    }

    @Test
    public void testEvents() throws Exception {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double tEnd = 1.0;
        double stepSize = 0.1;

        CombinedEventsManager manager = new CombinedEventsManager();
        manager.addEvent(new RelativisticEvent(0.5, new double[] { 1.0 }));

        integrator.setEquations(equations);
        integrator.setEventsHandlersManager(manager);
        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);

        Assert.assertEquals(1, manager.getEventCount());
    }

    @Test(expected = DerivativeException.class)
    public void testDerivativeException() throws Exception {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double tEnd = Math.PI / 2.0;
        double stepSize = 0.1;

        equations.setExceptionThrowsOnEvaluateDerivatives(true);

        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);
    }

    @Test(expected = IntegratorException.class)
    public void testIntegratorException() throws Exception {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double tEnd = Math.PI / 2.0;
        double stepSize = 0.1;

        integrator.setMaxStepCount(2);

        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceeded() throws Exception {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double tEnd = Math.PI / 2.0;
        double stepSize = 0.1;

        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);
        integrator.integrate(equations, tEnd, y0, tEnd, new double[y0.length]);
    }

    @Test(expected = NoBracketException.class)
    public void testNoBracketException() throws Exception {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double tEnd = Math.PI / 2.0;
        double stepSize = 0.1;

        equations.setExceptionThrowsOnEvaluateDerivatives(true);
        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);
    }

    @Test(expected = RootFindException.class)
    public void testRootFindException() throws Exception {
        double[] y0 = { 0.0 };
        double t0 = 0.0;
        double tEnd = Math.PI / 2.0;
        double stepSize = 0.1;

        equations.setExceptionThrowsOnEvaluateDerivatives(true);
        integrator.integratorFactory.setRootFinder(new BrentRootFinder());

        integrator.integrate(equations, t0, y0, tEnd, new double[y0.length]);
    }

}