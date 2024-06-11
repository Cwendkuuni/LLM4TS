import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.samples.Rk4SampleFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testSimpleIntegration() {
        final double[] a = {0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 8.0};
        final double[] b = {0.0, 1.0 / 4.0, 1. / 3.0, 1. / 8.0, 1. / 12.0};
        EmbeddedRungeKuttaIntegrator testIntegrator = new Rk4Integrator(
                "Rk4", true, a, b, new RungeKuttaStepInterpolator() {
            @Override
            public double integrate(ExpandableStatefulODE equations, double t) {
                return new Rk4SampleFunction().integrate(equations, t);
            }
        }, 0.01, 0.01, null, null);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void initialize(double time) {
                setCompleteState(new double[]{3.0});
            }

            @Override
            public void computeStateDerivatives(double time, double[] state) {
                state[0] += 1.0;
            }
        };

        testIntegrator.integrate(equations, 10.0);
        double[] expected = {3.0, 13.0, 23.0, 33.0, 43.0};
        assertArrayEquals(expected, equations.getCompleteState(), 0.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeStepSize() {
        final double[] a = {0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 8.0};
        final double[] b = {0.0, 1.0 / 4.0, 1. / 3.0, 1. / 8.0, 1. / 12.0};
        EmbeddedRungeKuttaIntegrator testIntegrator = new Rk4Integrator(
                "Rk4", true, a, b, new RungeKuttaStepInterpolator() {
            @Override
            public double integrate(ExpandableStatefulODE equations, double t) {
                return new Rk4SampleFunction().integrate(equations, t);
            }
        }, 0.01, -0.01, null, null);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void initialize(double time) {
                setCompleteState(new double[]{3.0});
            }

            @Override
            public void computeStateDerivatives(double time, double[] state) {
                state[0] += 1.0;
            }
        };

        testIntegrator.integrate(equations, 10.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testZeroStepSize() {
        final double[] a = {0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 8.0};
        final double[] b = {0.0, 1.0 / 4.0, 1. / 3.0, 1. / 8.0, 1. / 12.0};
        EmbeddedRungeKuttaIntegrator testIntegrator = new Rk4Integrator(
                "Rk4", true, a, b, new RungeKuttaStepInterpolator() {
            @Override
            public double integrate(ExpandableStatefulODE equations, double t) {
                return new Rk4SampleFunction().integrate(equations, t);
            }
        }, 0.01, 0.0, null, null);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void initialize(double time) {
                setCompleteState(new double[]{3.0});
            }

            @Override
            public void computeStateDerivatives(double time, double[] state) {
                state[0] += 1.0;
            }
        };

        testIntegrator.integrate(equations, 10.0);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMinStep() {
        final double[] a = {0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 8.0};
        final double[] b = {0.0, 1.0 / 4.0, 1. / 3.0, 1. / 8.0, 1. / 12.0};
        EmbeddedRungeKuttaIntegrator testIntegrator = new Rk4Integrator(
                "Rk4", true, a, b, new RungeKuttaStepInterpolator() {
            @Override
            public double integrate(ExpandableStatefulODE equations, double t) {
                return new Rk4SampleFunction().integrate(equations, t);
            }
        }, 0.01, 0.01, new double[]{3.0}, new double[]{-3.0});
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testZeroMinStep() {
        final double[] a = {0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 8.0};
        final double[] b = {0.0, 1.0 / 4.0, 1. / 3.0, 1. / 8.0, 1. / 12.0};
        EmbeddedRungeKuttaIntegrator testIntegrator = new Rk4Integrator(
                "Rk4", true, a, b, new RungeKuttaStepInterpolator() {
            @Override
            public double integrate(ExpandableStatefulODE equations, double t) {
                return new Rk4SampleFunction().integrate(equations, t);
            }
        }, 0.01, 0.01, new double[]{3.0}, new double[]{0.0});
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testNegativeMaxStep() {
        final double[] a = {0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 8.0};
        final double[] b = {0.0, 1.0 / 4.0, 1. / 3.0, 1. / 8.0, 1. / 12.0};
        EmbeddedRungeKuttaIntegrator testIntegrator = new Rk4Integrator(
                "Rk4", true, a, b, new RungeKuttaStepInterpolator() {
            @Override
            public double integrate(ExpandableStatefulODE equations, double t) {
                return new Rk4SampleFunction().integrate(equations, t);
            }
        }, 0.01, 0.01, null, null);

        testIntegrator.setMaxStep(-1.0);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testRejectedStep() {
        final double[] a = {0.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 8.0};
        final double[] b = {0.0, 1.0 / 4.0, 1. / 3.0, 1. / 8.0, 1. / 12.0};
        EmbeddedRungeKuttaIntegrator testIntegrator = new Rk4Integrator(
                "Rk4", true, a, b, new RungeKuttaStepInterpolator() {
            @Override
            public double integrate(ExpandableStatefulODE equations, double t) {
                return new Rk4SampleFunction().integrate(equations, t);
            }
        }, 0.01, 0.01, null, null);

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public void initialize(double time) {
                setCompleteState(new double[]{3.0});
            }

            @Override
            public void computeStateDerivatives(double time, double[] state) {
                state[0] += 1.0;
            }
        };

        testIntegrator.integrate(equations, 10.0);

        testIntegrator.setMaxGrowth(1.0);

        testIntegrator.integrate(equations, 10.0);
    }

}