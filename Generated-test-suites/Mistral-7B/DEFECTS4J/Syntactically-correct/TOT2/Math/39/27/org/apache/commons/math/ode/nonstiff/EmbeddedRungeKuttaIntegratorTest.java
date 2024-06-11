import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.sample.StepHandler;
import org.apache.commons.math3.ode.verifier.DifferentialEquationsVerifier;
import org.apache.commons.math3.special.BesselFunctions;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[] a = {0.0, 0.5, 0.5, 1.0};
        double[] b = {0.0, 0.25, 0.25, 0.75, 1.0};
        double[] c = {0.0, 0.5, 0.5};

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(5, a, b);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, c, null, b, prototype, 0.0, 1.0, null, null);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double[] getDerivatives(double t, double[] y) {
                double result = -y[0] * Math.exp(-1 * t);
                double[] resultArray = new double[y.length];
                resultArray[0] = result;
                return resultArray;
            }
        };

        double initialValue = 1.0;
        double time = 2.0;

        integrator.integrate(equations, initialValue, time);

        double expectedValue = Math.exp(-2.0);
        assertEquals(expectedValue, equations.getCompleteState()[0], 1e-6);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorWithStepHandlers() {
        double[] a = {0.0, 0.5, 0.5, 1.0};
        double[] b = {0.0, 0.25, 0.25, 0.75, 1.0};
        double[] c = {0.0, 0.5, 0.5};

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(5, a, b);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, c, null, b, prototype, 0.0, 1.0, null, null);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double[] getDerivatives(double t, double[] y) {
                double result = -y[0] * Math.exp(-1 * t);
                double[] resultArray = new double[y.length];
                resultArray[0] = result;
                return resultArray;
            }

            @Override
            public void addStepHandler(StepHandler handler) {
                // Custom step handler implementation
            }
        };

        double initialValue = 1.0;
        double time = 2.0;

        integrator.integrate(equations, initialValue, time);

        double expectedValue = Math.exp(-2.0);
        assertEquals(expectedValue, equations.getCompleteState()[0], 1e-6);

        // Add a custom step handler to verify the step size
        integrator.addStepHandler(new StepHandler() {
            @Override
            public int getId() {
                return 0;
            }

            @Override
            public void handleStep(double t, double[] y, double[] yDot, int stageNumber) {
                System.out.println("Step size: " + integrator.getStepSize());
            }
        });

        integrator.integrate(equations, initialValue, time);

        integrator.resetInternalState();
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorWithNonFSALMethods() {
        double[] a = {0.0, 0.5, 0.5, 1.0, 1.0};
        double[] b = {0.0, 0.25, 0.25, 0.75, 0.5, 1.0};
        double[] c = {0.0, 0.5, 0.5};

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(6, a, b);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF6(2,3)", false, c, null, b, prototype, 0.0, 1.0, null, null);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 6;
            }

            @Override
            public double[] getDerivatives(double t, double[] y) {
                double result = -2 * Math.sin(t) * Math.exp(-t);
                double[] resultArray = new double[y.length];
                resultArray[0] = result;
                return resultArray;
            }
        };

        double initialValue = 0.1;
        double time = Math.PI;

        integrator.integrate(equations, initialValue, time);

        double expectedValue = Math.sin(time) * Math.exp(-time);
        assertEquals(expectedValue, equations.getCompleteState()[0], 1e-6);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorWithExceptions() {
        double[] a = {0.0, 0.5, 0.5, 1.0};
        double[] b = {0.0, 0.25, 0.25, 0.75, 1.0};
        double[] c = {0.0, 0.5, 0.5};

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4, a, b);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, c, null, b, prototype, 0.0, 1.0, null, null) {
            @Override
            public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                throw new MathIllegalStateException("Test error");
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double[] getDerivatives(double t, double[] y) {
                throw new MathIllegalArgumentException("Test exception");
            }
        };

        try {
            integrator.integrate(equations, 0.0, 1.0);
            fail("Exception expected");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }

        try {
            integrator.integrate(equations, 1.0, 0.0);
            fail("Exception expected");
        } catch (MathIllegalStateException e) {
            // Expected exception
        }

        try {
            integrator.integrate(equations, Double.NaN, 1.0);
            fail("Exception expected");
        } catch (MathIllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorWithDifferentialEquationsVerifier() {
        double[] a = {0.0, 0.5, 0.5, 1.0};
        double[] b = {0.0, 0.25, 0.25, 0.75, 1.0};
        double[] c = {0.0, 0.5, 0.5};

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4, a, b);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, c, null, b, prototype, 0.0, 1.0, null, null);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double[] getDerivatives(double t, double[] y) {
                double result = -2 * Math.sin(t) * Math.exp(-t);
                double[] resultArray = new double[y.length];
                resultArray[0] = result;
                return resultArray;
            }
        };

        double initialValue = 0.1;
        double time = Math.PI;

        integrator.integrate(equations, initialValue, time);

        double expectedValue = Math.sin(time) * Math.exp(-time);
        assertEquals(expectedValue, equations.getCompleteState()[0], 1e-6);

        DifferentialEquationsVerifier verifier = new DifferentialEquationsVerifier(equations);
        verifier.verifyState(initialValue, time, equations.getCompleteState(), 1e-6);
    }

    @Test
    public void testEmbeddedRungeKuttaIntegratorWithBesselFunctions() {
        double[] a = {0.0, 0.5, 0.5, 1.0};
        double[] b = {0.0, 0.25, 0.25, 0.75, 1.0};
        double[] c = {0.0, 0.5, 0.5};

        RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(4, a, b);

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false, c, null, b, prototype, 0.0, 1.0, null, null);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 4;
            }

            @Override
            public double[] getDerivatives(double t, double[] y) {
                double result = BesselFunctions.secondKind(0, t);
                double[] resultArray = new double[y.length];
                resultArray[0] = result;
                return resultArray;
            }
        };

        double initialValue = 0.1;
        double time = 2.0;

        integrator.integrate(equations, initialValue, time);

        double expectedValue = BesselFunctions.secondKind(0, time);
        assertEquals(expectedValue, equations.getCompleteState()[0], 1e-6);
    }

}