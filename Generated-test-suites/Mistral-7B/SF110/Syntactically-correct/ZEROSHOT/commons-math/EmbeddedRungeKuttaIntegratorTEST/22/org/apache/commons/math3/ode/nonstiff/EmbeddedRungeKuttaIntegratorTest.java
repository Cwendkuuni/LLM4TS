import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Arrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private ExpandableStatefulODE equations;

 @Before
 public void setUp() {
 double[] c = {0.3, 0.6, 1.0, 1.0 / 3.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0};
 double[][] a = {
 {1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0, 1.0 / 6.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0},
 {1.0 / 3.0, 1.0 / 2.0, 2.0 / 3.0, 1.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0},
 {1.0 / 2.0, 2.0 / 3.0, 1.0, 1.0, 1.0 / 5.0, 1.0 / 6.0, 1.0 / 6.0},
 {1.0 / 6.0, 1.0, 1.0, 1.0, 1.0 / 6.0, 1.0 / 4.0, 1.0 / 3.0},
 {1.0 / 4.0, 1.0 / 5.0, 1.0 / 5.0, 1.0 / 6.0, 1.0 / 2.0, 1.0 / 3.0, 1.0 / 4.0},
 {1.0 / 5.0, 1.0 / 6.0, 1.0 / 6.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 2.0, 1.0 / 6.0},
 {1.0 / 6.0, 1.0 / 3.0, 1.0 / 4.0, 1.0 / 3.0, 1.0 / 6.0, 1.0 / 2.0, 1.0 / 6.0}
 };
 double[] b = {1.0 / 6.0, 1.0 / 3.0, 1.0 / 2.0, 1.0 / 6.0, 1.0 / 4.0, 1.0 / 5.0, 1.0 / 6.0};
 RungeKuttaStepInterpolator prototype = new RungeKuttaStepInterpolator(6, new AdamsBashforthIntegrator());
 integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", true, c, a, b, prototype, 0.001, 0.01, 1.0e-12, 1.0e-12) {
 @Override
 protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
 double error = 0.0;
 for (int i = 0; i < p0.length; i++) {
 error += Precision.pow(Precision.abs(p1[i] - p2[i]), 2);
 }
 return Math.sqrt(error);
 }
 };
 equations = new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 7;
 }

 @Override
 public void calculateStateDerivatives(double t, double[] y, double[] yPrime) {
 for (int i = 0; i < y.length; i++) {
 yPrime[i] = 10.0 * Math.exp(t) * Math.sin(t + i / 10.0);
 }
 }
 };
 }

 @Test
 public void testEmbeddedRungeKuttaIntegrator() throws Exception {
 double[] initialState = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0};
 integrator.integrate(equations, 10.0, initialState);
 double[] expectedState = {1362.361553542, 2803.883806923, 4294.122866336, 6786.228439701, 10368.185251627, 16012.917577129, 22951.466384695};
 Arrays.assertEquals(expectedState, equations.getCompleteState());
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testNumberIsTooSmallException() throws Exception {
 integrator.integrate(equations, 0.0, initialState);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatchException() throws Exception {
 equations = new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 8;
 }
 };
 integrator.integrate(equations, 10.0, initialState);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() throws Exception {
 integrator.setMaxSteps(1);
 integrator.integrate(equations, 10.0, initialState);
 }

 @Test(expected = NoBracketingException.class)
 public void testNoBracketingException() throws Exception {
 integrator.integrate(equations, 1.0, initialState);
 integrator.setMinStep(1.1);
 integrator.integrate(equations, 1.0, initialState);
 }
}