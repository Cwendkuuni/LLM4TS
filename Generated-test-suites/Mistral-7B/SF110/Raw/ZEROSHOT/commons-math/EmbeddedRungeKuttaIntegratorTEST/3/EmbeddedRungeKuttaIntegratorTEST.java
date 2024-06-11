import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.util.Precision.absTolerance;
import static org.apache.commons.math3.util.Precision.relativeTolerance;

public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private ExpandableStatefulODE ode;

 @Before
 public void setup() {
 // Initialize the ODE system for testing
 ode = new MyODESystem();
 // Initialize the integrator with default tolerance and step size
 integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, new double[]{}, new double[][]{{0.25, 0.25, 0.25, 0.25, 0.25, 0}, {0, 0.25, 0.25, 0.25, 0.25, 0.25}, {0, 0, 0.25, 0.25, 0.25, 0.25}, {0, 0, 0, 0.25, 0.25, 0.25}, {0, 0, 0, 0, 0.25, 0.25}, {0, 0, 0, 0, 0, 1}}, new RungeKuttaStepInterpolator(StepInterpolator.Type.RUNGE_KUTTA_FEHLBERG_78D), 0.01, 0.01, 1.0e-12, 1.0e-12);
 }

 @Test
 public void testIntegrate() throws Exception {
 // Test integration with a simple ODE system
 double[] y0 = {1.0, 1.0};
 double tStart = 0.0;
 double tEnd = 2.0;
 integrator.integrate(ode, tStart);
 assertArrayEquals(new double[]{2.718281828459045, 2.718281828459045}, ode.getCompleteState(), 0.0001);
 integrator.integrate(ode, tEnd);
 // Test integration with a larger time step
 integrator.setStepSize(0.2);
 integrator.integrate(ode, tStart);
 integrator.integrate(ode, tEnd);
 assertArrayEquals(new double[]{6.184811586226491, 6.184811586226491}, ode.getCompleteState(), 0.001);
 // Test integration with relative tolerance
 integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, new double[]{}, new double[][]{{0.25, 0.25, 0.25, 0.25, 0.25, 0}, {0, 0.25, 0.25, 0.25, 0.25, 0.25}, {0, 0, 0.25, 0.25, 0.25, 0.25}, {0, 0, 0, 0.25, 0.25, 0.25}, {0, 0, 0, 0, 0.25, 0.25}, {0, 0, 0, 0, 0, 1}}, new RungeKuttaStepInterpolator(StepInterpolator.Type.RUNGE_KUTTA_FEHLBERG_78D), 0.01, 0.05, new double[]{1.0e-5, 1.0e-5}, new double[]{1.0e-5, 1.0e-5});
 integrator.integrate(ode, tStart);
 assertArrayEquals(new double[]{2.718281828459045, 2.718281828459045}, ode.getCompleteState(), relativeTolerance(1.0e-5));
 integrator.integrate(ode, tEnd);
 // Test integration with absolute tolerance
 integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, new double[]{}, new double[][]{{0.25, 0.25, 0.25, 0.25, 0.25, 0}, {0, 0.25, 0.25, 0.25, 0.25, 0.25}, {0, 0, 0.25, 0.25, 0.25, 0.25}, {0, 0, 0, 0.25, 0.25, 0.25}, {0, 0, 0, 0, 0.25, 0.25}, {0, 0, 0, 0, 0, 1}}, new RungeKuttaStepInterpolator(StepInterpolator.Type.RUNGE_KUTTA_FEHLBERG_78D), 0.01, 0.01, null, new double[]{1.0e-5, 1.0e-5});
 integrator.integrate(ode, tStart);
 assertArrayEquals(new double[]{2.718281828459045, 2.718281828459045}, ode.getCompleteState(), absTolerance(1.0e-5));
 integrator.integrate(ode, tEnd);
 }

 @Test (expected = NumberIsTooSmallException.class)
 public void testIntegrateWithTooSmallStepSize() throws Exception {
 integrator.setStepSize(0.001);
 integrator.integrate(ode, tStart);
 integrator.integrate(ode, tEnd);
 fail("Expected NumberIsTooSmallException but integration was completed.");
 }

 @Test (expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxCountExceeded() throws Exception {
 integrator.setMaxSteps(10);
 integrator.integrate(ode, tStart, tEnd);
 fail("Expected MaxCountExceededException but integration was completed.");
 }

 @Test (expected = DimensionMismatchException.class)
 public void testIntegrateWithMismatchedDimension() throws Exception {
 integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, new double[]{}, new double[][]{{0.25, 0.25, 0.25, 0.25}, {0, 0.25, 0.25, 0.25}, {0, 0, 0.25, 0.25}, {0, 0, 0, 0.25}, {0, 0, 0, 0, 1}}, new RungeKuttaStepInterpolator(StepInterpolator.Type.RUNGE_KUTTA_FEHLBERG_78D), 0.01, 0.01, 1.0e-12, 1.0e-12);
 ode = new MyODESystem(3);
 integrator.integrate(ode, tStart);
 fail("Expected DimensionMismatchException but integration was completed.");
 }

 @Test
 public void testGetOrder() {
 assertEquals(5, integrator.getOrder());
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

 @Test
 public void testGetMinReduction() {
 assertEquals(0.2, integrator.getMinReduction(), 0.01);
 }

 @Test
 public void testSetMinReduction() {
 integrator.setMinReduction(0.3);
 assertEquals(0.3, integrator.getMinReduction(), 0.01);
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

class MyODESystem extends ExpandableStatefulODE {
 private double[] state;

 public MyODESystem() {
 this(new MyODEFunction(), 2);
 }

 public MyODESystem(MyODEFunction function, int dimension) {
 super(function, dimension);
 state = new double[dimension];
 }

 @Override
 public void setTime(double time) {
 super.setTime(time);
 }

 @Override
 public void setCompleteState(double[] state) {
 this.state = state;
 super.setCompleteState(state);
 }

 @Override
 public void computeDerivatives(double time, double[] y, double[] yPrime) {
 yPrime[0] = y[1];
 yPrime[1] = -y[0];
 }
}

class MyODEFunction implements ODEFunction {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void value(double time, double[] y, double[] yPrime) {
 yPrime[0] = y[1];
 yPrime[1] = -y[0];
 }
}