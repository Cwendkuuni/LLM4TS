import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private ExpandableStatefulODE equations;
 private double[] initialState = new double[]{1.0, 1.0};

 @Before
 public void setUp() {
 integrator = new MyRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.5, 0.3, 0.2, 0.1}, new double[][]{{0.5, 0}, {0, 0.5}, {0.25, 0.25}, {0, 0.5}}, new double[]{0.2, 0.3, 0.1, 0.1}, new RungeKuttaStepInterpolator(new RungeKuttaFehlberg78Method()));
 equations = new DummyODE("DummyODE", integrator);
 equations.setTime(0.0);
 equations.setCompleteState(initialState);
 }

 @After
 public void tearDown() {
 integrator = null;
 equations = null;
 }

 @Test
 public void testIntegrate() throws Exception {
 double time = 2.0;
 integrator.integrate(equations, time);
 double[] expectedState = new double[]{3.7337239044238295, 2.4606424481348466};
 assertEquals(expectedState, equations.getCompleteState(), 0.001);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateWithSmallStep() throws Exception {
 double time = 0.1;
 integrator.setMinStep(0.001);
 integrator.integrate(equations, time);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithMismatchedState() throws Exception {
 double time = 2.0;
 double[] initialState2 = new double[]{1.0, 1.0, 1.0};
 equations.setCompleteState(initialState2);
 integrator.integrate(equations, time);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxCountExceeded() throws Exception {
 double time = 1000.0;
 integrator.setMaxStep(0.1);
 integrator.integrate(equations, time);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithNoBracketing() throws Exception {
 double time = -1.0;
 integrator.integrate(equations, time);
 }

 @Test
 public void testGetOrder() {
 int order = integrator.getOrder();
 assertEquals(4, order);
 }

 @Test
 public void testGetSafety() {
 double safety = integrator.getSafety();
 assertEquals(0.9, safety, 0.01);
 }

 @Test
 public void testSetSafety() {
 integrator.setSafety(0.8);
 double newSafety = integrator.getSafety();
 assertEquals(0.8, newSafety, 0.01);
 }

 @Test
 public void testGetMinReduction() {
 double minReduction = integrator.getMinReduction();
 assertEquals(0.2, minReduction, 0.01);
 }

 @Test
 public void testSetMinReduction() {
 integrator.setMinReduction(0.3);
 double newMinReduction = integrator.getMinReduction();
 assertEquals(0.3, newMinReduction, 0.01);
 }

 @Test
 public void testGetMaxGrowth() {
 double maxGrowth = integrator.getMaxGrowth();
 assertEquals(10.0, maxGrowth, 0.01);
 }

 @Test
 public void testSetMaxGrowth() {
 integrator.setMaxGrowth(5.0);
 double newMaxGrowth = integrator.getMaxGrowth();
 assertEquals(5.0, newMaxGrowth, 0.01);
 }
}

class DummyODE extends ExpandableStatefulODE {
 private static final long serialVersionUID = 1L;

 public DummyODE(String name, ODEIntegrator integrator) {
 super(name, integrator);
 }

 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yPrime) {
 yPrime[0] = y[1];
 yPrime[1] = -y[0];
 }
}

class MyRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
 private static final long serialVersionUID = 1L;

 public MyRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
 super(name, fsal, c, a, b, prototype, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
 }

 @Override
 protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
 return 0.01;
 }
}