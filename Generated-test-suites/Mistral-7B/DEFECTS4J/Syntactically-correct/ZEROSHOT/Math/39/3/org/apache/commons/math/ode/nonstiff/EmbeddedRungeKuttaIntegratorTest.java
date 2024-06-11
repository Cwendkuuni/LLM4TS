import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[] c = {0.0, 0.3, 0.6, 1.0};
        double[][] a = {{0.0, 0.0, 0.0, 0.5}, {0.0, 0.5, 0.5, 0.5}, {0.5, 0.5, 0.0, 0.0}, {1.0, 0.0, 0.0, 0.0}};
        double[] b = {0.2369268850561891, 0.6797473813195274, 0.6797473813195274, 0.2369268850561891};

        EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("Test RK4", true, c, a, b,
                new RungeKuttaStepInterpolator(), 1e-10, 1e10, null, null) {

            @Override
            protected double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
                return FastMath.abs(E.EULER_POLYNOMIAL(5, h)) / 5.0;
            }
        };

        ExpandableStatefulODE equations = new ExpandableStatefulODE() {
            @Override
            public int getDimension() {
                return 1;
            }

            @Override
            public int getOrder() {
                return 5;
            }

            @Override
            public void computeDerivatives(double t, double[] y, double[] yDot) {
                yDot[0] = E.EULER_POLYNOMIAL(5, y[0]) / 5.0;
            }
        };

        double[] initialState = new double[]{1.0};
        double time = 1.0;

        integrator.integrate(equations, time);
        double[] finalState = equations.getCompleteState();

        Assert.assertEquals(initialState, finalState);

        integrator.resetInternalState();

        time = 2.0;
        integrator.integrate(equations, time);
        finalState = equations.getCompleteState();

        Assert.assertEquals(new double[]{2.1213203435596425}, finalState);

        integrator.resetInternalState();

        time = -1.0;
        integrator.integrate(equations, time);
        finalState = equations.getCompleteState();

        Assert.assertEquals(new double[]{-1.1213203435596425}, finalState);

        integrator.setMinReduction(0.1);
        integrator.setMaxGrowth(1.5);

        integrator.integrate(equations, time);
        finalState = equations.getCompleteState();

        Assert.assertEquals(new double[]{1.0}, finalState);

    }

}