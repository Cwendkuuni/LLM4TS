import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private ExpandableStatefulODE equations;

 @Before
 public void setUp() {
 integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.3, 0.4}, new double[][]{{0.0, 0.5, 0.5}, {0.0, 0.5, 0.5}}, new double[]{0.0, 0.5}, new RungeKuttaStepInterpolator(new RungeKuttaMethod(new RK4Method(), 4)), 0.001, 0.01, null, null);
 equations = new DummyODE();
 }

 @Test
 public void testGetOrder() {
 Assert.assertEquals(4, integrator.getOrder());
 }

 @Test
 public void testGetSafety() {
 Assert.assertEquals(0.9, integrator.getSafety(), 0.001);
 }

 @Test
 public void testSetSafety() {
 integrator.setSafety(0.8);
 Assert.assertEquals(0.8, integrator.getSafety(), 0.001);
 }

 @Test
 public void testIntegrateWithinBounds() throws Exception {
 integrator.integrate(equations, 0.0);
 Assert.assertEquals(0.0, equations.getTime(), 0.001);
 Assert.assertArrayEquals(new double[]{0.0, 0.0}, equations.getCompleteState(), 0.001);
 integrator.integrate(equations, 1.0);
 Assert.assertEquals(1.0, equations.getTime(), 0.001);
 Assert.assertArrayEquals(new double[]{1.0, 1.0}, equations.getCompleteState(), 0.001);
 }

 @Test
 public void testIntegrateOutsideBoundsForward() throws Exception {
 integrator.integrate(equations, 1.5);
 Assert.assertEquals(1.5, equations.getTime(), 0.001);
 Assert.assertArrayEquals(new double[]{1.5, 1.5}, equations.getCompleteState(), 0.001);
 }

 @Test
 public void testIntegrateOutsideBoundsBackward() throws Exception {
 integrator.integrate(equations, -0.5);
 Assert.assertEquals(-0.5, equations.getTime(), 0.001);
 Assert.assertArrayEquals(new double[]{-0.5, -0.5}, equations.getCompleteState(), 0.001);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testNumberIsTooSmallException() throws Exception {
 integrator.integrate(equations, -1.0);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatchException() {
 equations = new DummyODE(2);
 integrator.integrate(equations, 0.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() {
 equations = new DummyODE();
 integrator.setMaxStep(0.001);
 integrator.integrate(equations, 10.0);
 }

 @Test(expected = NoBracketingException.class)
 public void testNoBracketingException() {
 equations = new DummyODE();
 integrator.setMaxStep(1.0);
 integrator.integrate(equations, 0.0);
 }
}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {

 protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
 return Precision.abs(Precision.sub(p1, p2));
 }
}

class DummyODE implements ExpandableStatefulODE {

 private double time;
 private double[] state;

 public DummyODE() {
 this(new double[2]);
 }

 public DummyODE(double[] initialState) {
 this.state = initialState;
 }

 public String getName() {
 return "DummyODE";
 }

 public int getDimension() {
 return state.length;
 }

 public int getStateDimension() {
 return state.length;
 }

 public int getOrder() {
 return 0;
 }

 public double[] getCompleteState() {
 return state.clone();
 }

 public double getTime() {
 return time;
 }

 public void setTime(double newTime) {
 time = newTime;
 }

 public void setCompleteState(double[] newState) {
 state = newState.clone();
 }

 public double[] getDerivatives(double time, double[] state) {
 return state.clone();
 }
}