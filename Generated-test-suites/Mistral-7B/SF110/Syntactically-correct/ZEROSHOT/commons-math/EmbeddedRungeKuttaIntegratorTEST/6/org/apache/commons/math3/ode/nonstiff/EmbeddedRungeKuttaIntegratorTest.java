import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double ACCURACY = 1e-9;
 private static final double TOLERANCE = 1e-9;

 @Test
 public void testConstructor() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, new double[]{-0.236926885, 0.275880255, 0.620966732, 0.216317251, 0.0}, new double[][]{{0.25, 0.0, 0.0}, {0}, {0.25, 0.25, 0.0}, {0.25, 0.25, 0.25}, {0.125, 0.25, 0.25}, {0.125, 0.125, 0.25}, {0.125, 0.125, 0.125}, {0.0625, 0.125, 0.125}, {0.0625, 0.0625, 0.125}, {0.0625, 0.0625, 0.0625}, {0.03125, 0.0625, 0.0625}}, new double[]{0.25, 0}, new RungeKuttaStepInterpolator() {
 @Override
 protected void reinitialize(StepInterpolator interpolator, double[] y, double[] dy, boolean forward, ODEStateAndDerivative equation) {
 super.reinitialize(interpolator, y, dy, forward, equation);
 this.setOrder(interpolator.getOrder());
 }
 }, 0.001, 0.01, null, null);
 Assert.assertEquals("RK45", integrator.getName());
 Assert.assertTrue(integrator.isFSAL());
 Assert.assertArrayEquals(new double[]{-0.236926885, 0.275880255, 0.620966732, 0.216317251, 0.0}, integrator.getC(), integrator.getC());
 Assert.assertArrayEquals(new double[]{{0.25, 0.0, 0.0}, {0}, {0.25, 0.25, 0.0}, {0.25, 0.25, 0.25}, {0.125, 0.25, 0.25}, {0.125, 0.125, 0.25}, {0.125, 0.125, 0.125}, {0.0625, 0.125, 0.125}, {0.0625, 0.0625, 0.125}, {0.0625, 0.0625, 0.0625}, {0.03125, 0.0625, 0.0625}}, integrator.getA(), integrator.getA());
 Assert.assertArrayEquals(new double[]{0.25, 0}, integrator.getB(), integrator.getB());
 Assert.assertNotNull(integrator.getPrototype());
 Assert.assertEquals(-1.0 / integrator.getOrder(), integrator.getExp());
 Assert.assertEquals(0.9, integrator.getSafety(), ACCURACY);
 Assert.assertEquals(0.2, integrator.getMinReduction(), ACCURACY);
 Assert.assertEquals(10.0, integrator.getMaxGrowth(), ACCURACY);
 }

 @Test
 public void testGetOrder() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, new double[]{-0.236926885, 0.275880255, 0.620966732, 0.216317251, 0.0}, new double[][]{{0.25, 0.0, 0.0}, {0}, {0.25, 0.25, 0.0}, {0.25, 0.25, 0.25}, {0.125, 0.25, 0.25}, {0.125, 0.125, 0.25}, {0.125, 0.125, 0.125}, {0.0625, 0.125, 0.125}, {0.0625, 0.0625, 0.125}, {0.0625, 0.0625, 0.0625}, {0.03125, 0.0625, 0.0625}}, new RungeKuttaStepInterpolator() {}, 0.001, 0.01, null, null);
 Assert.assertEquals(5, integrator.getOrder());
 }

 @Test
 public void testSetSafety() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, new double[]{-0.236926885, 0.275880255, 0.620966732, 0.216317251, 0.0}, new double[][]{{0.25, 0.0, 0.0}, {0}, {0.25, 0.25, 0.0}, {0.25, 0.25, 0.25}, {0.125, 0.25, 0.25}, {0.125, 0.125, 0.25}, {0.125, 0.125, 0.125}, {0.0625, 0.125, 0.125}, {0.0625, 0.0625, 0.125}, {0.0625, 0.0625, 0.0625}, {0.03125, 0.0625, 0.0625}}, new RungeKuttaStepInterpolator() {}, 0.001, 0.01, null, null);
 integrator.setSafety(0.8);
 Assert.assertEquals(0.8, integrator.getSafety(), ACCURACY);
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, new double[]{-0.236926885, 0.275880255, 0.620966732, 0.216317251, 0.0}, new double[][]{{0.25, 0.0, 0.0}, {0}, {0.25, 0.25, 0.0}, {0.25, 0.25, 0.25}, {0.125, 0.25, 0.25}, {0.125, 0.125, 0.25}, {0.125, 0.125, 0.125}, {0.0625, 0.125, 0.125}, {0.0625, 0.0625, 0.125}, {0.0625, 0.0625, 0.0625}, {0.03125, 0.0625, 0.0625}}, new RungeKuttaStepInterpolator() {}, 0.001, 0.01, null, null);
 double[] initialState = new double[]{1.0, 1.0};
 double time = 2.0;
 double[] expectedState = new double[]{3.574414717757018, 2.718281828459045};
 integrator.integrate(new StiffODESystem() {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yPrime) {
 yPrime[0] = y[1];
 yPrime[1] = -y[0];
 }
 }, time, initialState);
 double[] state = initialState.clone();
 integrator.integrate(new StiffODESystem() {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yPrime) {
 yPrime[0] = y[1];
 yPrime[1] = -y[0];
 }
 }, 0.0, state);
 Assert.assertArrayEquals(expectedState, state, TOLERANCE);
 }
}