import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegratorTest;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {

        // Test constructor
        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
            new double[]{0.25, 0.25, 0.5, 0.5}, new double[][]{{0.25, 0.25}, {0.25, 0}, {0, 0.25}, {0, 0.25}},
            new double[]{0.25, 0.25}, new RungeKuttaStepInterpolator(4), 1e-10, 1e-10);
        Assert.assertEquals("RK4", integrator.getName());
        Assert.assertFalse(integrator.isFsal());
        Assert.assertArrayEquals(new double[]{0.25, 0.25, 0.5, 0.5}, integrator.getC());
        Assert.assertArrayEquals(new double[][]{{0.25, 0.25}, {0.25, 0}, {0, 0.25}, {0, 0.25}}, integrator.getA());
        Assert.assertArrayEquals(new double[]{0.25, 0.25}, integrator.getB());
        Assert.assertEquals(RungeKuttaStepInterpolator.class, integrator.getPrototype().getClass());
        Assert.assertEquals(1e-10, integrator.getMinStep(), 0.0);
        Assert.assertEquals(1e-10, integrator.getMaxStep(), 0.0);
        Assert.assertArrayEquals(new double[]{1e-10, 1e-10}, integrator.getScaledAbsoluteTolerance(), 0.0);
        Assert.assertArrayEquals(new double[]{1e-10, 1e-10}, integrator.getScaledRelativeTolerance(), 0.0);
        Assert.assertEquals(0.9, integrator.getSafety(), 0.0);
        Assert.assertEquals(0.2, integrator.getMinReduction(), 0.0);
        Assert.assertEquals(10.0, integrator.getMaxGrowth(), 0.0);

        // Test getOrder
        Assert.assertEquals(4, integrator.getOrder());

        // Test setSafety
        integrator.setSafety(0.8);
        Assert.assertEquals(0.8, integrator.getSafety(), 0.0);

        // Test setMinReduction
        integrator.setMinReduction(0.15);
        Assert.assertEquals(0.15, integrator.getMinReduction(), 0.0);

        // Test setMaxGrowth
        integrator.setMaxGrowth(5.0);
        Assert.assertEquals(5.0, integrator.getMaxGrowth(), 0.0);

        // Test integrate
        FirstOrderDifferentialEquations equations = new DummyDifferentialEquations();
        double y0[] = {1.0, 1.0};
        double t0 = 0.0;
        double t1 = 1.0;
        double[] result = integrator.integrate(equations, t0, y0, t1, new double[y0.length]);
        Assert.assertEquals(t1, result, 0.0);

        // Test integrate with events
        integrator = new EmbeddedRungeKuttaIntegrator("RK4", false,
            new double[]{0.25, 0.25, 0.5, 0.5}, new double[][]{{0.25, 0.25}, {0.25, 0}, {0, 0.25}, {0, 0.25}},
            new double[]{0.25, 0.25}, new RungeKuttaStepInterpolator(4), 1e-10, 1e-10,
            new double[]{1e-10, 1e-10}, new double[]{1e-10, 1e-10});

        equations = new DummyDifferentialEquationsWithEvent();
        y0 = new double[]{1.0, 1.0};
        t0 = 0.0;
        t1 = 2.5;
        double[] resultEvents = new double[2];
        result = integrator.integrate(equations, t0, y0, t1, new double[y0.length], resultEvents);
        Assert.assertEquals(t1, result, 0.0);
        Assert.assertEquals(1.5, resultEvents[0], 0.0);
        Assert.assertEquals(2.0, resultEvents[1], 0.0);

    }

    private static class DummyDifferentialEquations implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] dy) {
            dy[0] = 1.0;
        }

    }

    private static class DummyDifferentialEquationsWithEvent implements FirstOrderDifferentialEquations {

        @Override
        public int getDimension() {
            return 1;
        }

        @Override
        public void computeDerivatives(double t, double[] y, double[] dy) {
            dy[0] = 1.0;
        }

        @Override
        public Event[] getEvents(double t) {
            if (t > 1.5) {
                return new Event[]{new When(t, Evt -> 2.0)};
            } else {
                return new Event[0];
            }
        }

    }

}