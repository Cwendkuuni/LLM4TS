import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class EmbeddedRungeKuttaIntegratorTest {

    @Test
    public void testEmbeddedRungeKuttaIntegrator() {
        double[][] coefs = new double[][]{{0.0, 0.3, 0.6, 0.1},
                {0.0, 0.5555555555555555, 0.75, 0.2222222222222222},
                {0.0, 0.5, 0.5, 0.5}};
        double[] timeSteps = new double[]{0.3, 0.5555555555555555, 0.5};
        double[] propagationWeights = new double[]{0.0, 0.4, 0.6};
        double[] absoluteTolerances = new double[]{1e-10, 1e-13, 1e-12};
        double[] relativeTolerances = new double[]{1e-8, 1e-9, 1e-8};

        for (int i = 0; i < coefs.length; i++) {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                    "embeddedRungeKuttaIntegrator" + i,
                    false,
                    timeSteps,
                    coefs[i],
                    propagationWeights,
                    new RungeKuttaStepInterpolator(coefs[i][coefs[i].length - 1]),
                    1.0e-10,
                    1.0e10,
                    absoluteTolerances[i],
                    relativeTolerances[i]) {
                @Override
                protected double estimateError(double[][] yDotK,
                                               double[] y0, double[] y1,
                                               double h) {
                    double sum = 0.0;
                    for (int j = 0; j < y0.length; j++) {
                        sum += Math.pow(y1[j] - y0[j], 2);
                    }
                    return Math.sqrt(sum);
                }
            };

            ExpandableStatefulODE equations = new ExpandableStatefulODE() {
                @Override
                public int getDimension() {
                    return y0.length;
                }

                @Override
                public int getStateDimension() {
                    return y0.length;
                }

                @Override
                public int getOrder() {
                    return coefs[0].length;
                }

                @Override
                public void initialize(double t, double[] y, double[] yDot) {
                    System.arraycopy(new double[]{1.0, 1.0, 1.0}, 0, y, 0, y.length);
                    System.arraycopy(new double[]{0.0, 0.0, 0.0}, 0, yDot, 0, y.length);
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yDot) {
                    for (int j = 0; j < y.length; j++) {
                        yDot[j] = y[j] * (j + 1);
                    }
                }
            };

            integrator.integrate(equations, 2.0);
            double[] expected = new double[]{2.25, 3.75, 5.5};
            assertArrayEquals(expected, equations.getCompleteState(), 0.001);
        }

        for (int i = 0; i < coefs.length; i++) {
            EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator(
                    "embeddedRungeKuttaIntegrator" + i,
                    true,
                    timeSteps,
                    coefs[i],
                    propagationWeights,
                    new RungeKuttaStepInterpolator(coefs[i][coefs[i].length - 1]),
                    1.0e-10,
                    1.0e10,
                    absoluteTolerances[i],
                    relativeTolerances[i]) {
                @Override
                protected double estimateError(double[][] yDotK,
                                               double[] y0, double[] y1,
                                               double h) {
                    double sum = 0.0;
                    for (int j = 0; j < y0.length; j++) {
                        sum += Math.pow(y1[j] - y0[j], 2);
                    }
                    return Math.sqrt(sum);
                }
            };

            ExpandableStatefulODE equations = new ExpandableStatefulODE() {
                @Override
                public int getDimension() {
                    return y0.length;
                }

                @Override
                public int getStateDimension() {
                    return y0.length;
                }

                @Override
                public int getOrder() {
                    return coefs[0].length;
                }

                @Override
                public void initialize(double t, double[] y, double[] yDot) {
                    System.arraycopy(new double[]{1.0, 1.0, 1.0}, 0, y, 0, y.length);
                    System.arraycopy(new double[]{0.0, 0.0, 0.0}, 0, yDot, 0, y.length);
                }

                @Override
                public void computeDerivatives(double t, double[] y, double[] yDot) {
                    for (int j = 0; j < y.length; j++) {
                        yDot[j] = y[j] * (j + 1);
                    }
                }
            };

            integrator.integrate(equations, 2.0);
            double[] expected = new double[]{2.25, 3.75, 5.5};
            assertArrayEquals(expected, equations.getCompleteState(), 0.001);
        }

    }

}