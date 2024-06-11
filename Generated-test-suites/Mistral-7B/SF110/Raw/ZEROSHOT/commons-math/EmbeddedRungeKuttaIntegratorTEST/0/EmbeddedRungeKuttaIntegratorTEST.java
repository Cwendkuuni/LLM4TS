import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double ABSOLUTE_TOLERANCE = 1e-10;
 private static final double RELATIVE_TOLERANCE = 1e-6;
 private static final double INITIAL_TIME = 0.0;
 private static final double FINAL_TIME = 2.0 * Math.PI;
 private static final int MAX_STEPS = 100;

 @Test
 public void testEmbeddedRungeKuttaIntegrator() throws Exception {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKutta45Integrator("RK45", true, new double[]{-0.6, 0.3, 0.2784, 0.0466}, new double[][]{{0.25, 0.45, 0.2125, 0.045}, {0}, {0.25, 0.25, 0.25, 0}}, new RungeKuttaStepInterpolator(new EmbeddedRungeKutta45Method()));
 integrator.setMinStep(1e-12);
 integrator.setMaxStep(1e-6);
 integrator.setSafety(0.9);
 integrator.setMinReduction(0.2);
 integrator.setMaxGrowth(10.0);

 ExpandableStatefulODE equations = new AbstractODEFunctionWithPartialDerivatives() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public double value(double t, double[] state) {
 return Math.sin(state[0]);
 }

 @Override
 public double[] value(double t, double[] state, double[] derivatives) {
 derivatives[0] = -Math.cos(state[0]);
 return derivatives;
 }
 };

 DoubleIntegratorResult result = integrator.integrate(equations, INITIAL_TIME, FINAL_TIME, MAX_STEPS);

 assertTrue(result.isSuccess());
 assertArrayEquals(new double[]{Math.sin(FINAL_TIME), Math.cos(FINAL_TIME)}, result.getY(), 0.0);
 }
}