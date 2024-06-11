import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private StepHandler stepHandler;

 @Before
 public void setup() {
 integrator = new MyEmbeddedRungeKuttaIntegrator("TestIntegrator", false, new double[]{0.3, 0.2, 0.2, 0.1}, new double[][]{{0.3453, 0.0, 0.4367, 0.1382}, {0, 0.4687, 0.3174, 0.1231}, {0, 0, 0.5323, 0.0749}, {0, 0, 0, 0.6221}}, new double[]{0.0, 0.1, 0.2, 0.3}, new RungeKuttaStepInterpolator(4), 0.001, 0.01, 1e-6, 1e-6);
 stepHandler = new StepHandler();
 integrator.addStepHandler(stepHandler);
 }

 @Test
 public void testGetOrder() {
 assertEquals(4, integrator.getOrder());
 }

 @Test
 public void testGetSafety() {
 assertEquals(0.9, integrator.getSafety(), 0.01);
 }

 @Test
 public void testSetSafety() {
 integrator.setSafety(0.8);
 assertEquals(0.8, integrator.getSafety(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSafetyNegative() {
 integrator.setSafety(-1);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testIntegrate() throws Exception {
 double[] initialValues = {1.0, 1.0};
 double[] expectedValues = {1.5685083322352393, 1.4806352351416732};
 double[] state = new double[initialValues.length];
 System.arraycopy(initialValues, 0, state, 0, initialValues.length);
 integrator.integrate(new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = y[1];
 dy[1] = -y[0];
 }
 }, 0.0, state);
 assertArrayEquals(expectedValues, state, 0.0001);
 }

 @Test
 public void testIntegrateWithTol() throws Exception {
 double[] initialValues = {1.0, 1.0};
 double[] expectedValues = {1.5685083322352393, 1.4806352351416732};
 double tolerance = 1e-7;
 double[] state = new double[initialValues.length];
 System.arraycopy(initialValues, 0, state, 0, initialValues.length);
 integrator.integrate(new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = y[1];
 dy[1] = -y[0];
 }
 }, 0.0, state, tolerance);
 assertArrayEquals(expectedValues, state, tolerance);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateWithTolTooSmall() throws Exception {
 double[] initialValues = {1.0, 1.0};
 double tolerance = 0;
 double[] state = new double[initialValues.length];
 System.arraycopy(initialValues, 0, state, 0, initialValues.length);
 integrator.integrate(new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = y[1];
 dy[1] = -y[0];
 }
 }, 0.0, state, tolerance);
 fail("Should throw NumberIsTooSmallException");
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithWrongDimension() throws Exception {
 integrator.integrate(new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 3;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 // empty
 }
 }, 0.0, new double[2]);
 fail("Should throw DimensionMismatchException");
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxCount() throws Exception {
 integrator.setMaxSteps(0);
 double[] initialValues = {1.0, 1.0};
 double[] state = new double[initialValues.length];
 System.arraycopy(initialValues, 0, state, 0, initialValues.length);
 integrator.integrate(new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = y[1];
 dy[1] = -y[0];
 }
 }, 0.0, state);
 fail("Should throw MaxCountExceededException");
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithNoBracketing() throws Exception {
 integrator.setMinStep(Double.POSITIVE_INFINITY);
 double[] initialValues = {1.0, 1.0};
 double[] state = new double[initialValues.length];
 System.arraycopy(initialValues, 0, state, 0, initialValues.length);
 integrator.integrate(new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = y[1];
 dy[1] = -y[0];
 }
 }, 0.0, state);
 fail("Should throw NoBracketingException");
 }

 @Test
 public void testGetMinReduction() {
 assertEquals(0.2, integrator.getMinReduction(), 0.01);
 }

 @Test
 public void testSetMinReduction() {
 integrator.setMinReduction(0.15);
 assertEquals(0.15, integrator.getMinReduction(), 0.01);
 }

 @Test
 public void testGetMaxGrowth() {
 assertEquals(10.0, integrator.getMaxGrowth(), 0.01);
 }

 @Test
 public void testSetMaxGrowth() {
 integrator.setMaxGrowth(5.0);
 assertEquals(5.0, integrator.getMaxGrowth(), 0.01);
 }
}