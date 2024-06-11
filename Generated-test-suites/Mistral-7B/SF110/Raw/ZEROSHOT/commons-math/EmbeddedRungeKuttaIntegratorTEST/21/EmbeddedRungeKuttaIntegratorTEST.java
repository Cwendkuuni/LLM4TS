import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 @Test
 public void testEmbeddedRungeKuttaIntegratorConstruction() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.3, 0.4, 0.1}, new double[][]{{0.238903926762647, 0.164333735264629}, {-0.049059370191404, 0.121202152124332}, {0.049059370191404, 0.052359891396209}, {-0.02236133864057, 0.035355339059544}}, new double[]{0.0, 0.5}, new RungeKuttaStepInterpolator(new RungeKuttaMethod(new RungeKuttaMethod.Order(3))), 0.001, 0.01, null, null);
 assertEquals("MyIntegrator", integrator.getName());
 assertEquals(true, integrator.isFullyStateFeedback());
 assertEquals(3, integrator.getOrder());
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorAbstractMethods() {
 try {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("MyIntegrator", true, null, null, null, null, 0.0, 0.0, null, null);
 integrator.integrate(new MockExpandableStatefulODE(), 1.0);
 fail("Integrator should not be able to be instantiated without required parameters");
 }
 catch (IllegalArgumentException e) {
 // expected
 }

 try {
 integrator = new EmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.3, 0.4, 0.1}, new double[][]{{0.238903926762647, 0.164333735264629}, {-0.049059370191404, 0.121202152124332}, {0.049059370191404, 0.052359891396209}, {-0.02236133864057, 0.035355339059544}}, null, null, 0.0, 0.0, null, null);
 integrator.integrate(new MockExpandableStatefulODE(), 1.0);
 fail("Integrator should not be able to be instantiated without a RungeKuttaStepInterpolator");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorIntegration() {
 double[] initialState = new double[]{1.0, 1.0};
 double time = 10.0;
 double[] expectedState = new double[]{3.737724223083217, 2.216238554956625};

 MockExpandableStatefulODE equations = new MockExpandableStatefulODE();
 equations.setFunction(new MyODEFunction(initialState));

 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.3, 0.4, 0.1}, new double[][]{{0.238903926762647, 0.164333735264629}, {-0.049059370191404, 0.121202152124332}, {0.049059370191404, 0.052359891396209}, {-0.02236133864057, 0.035355339059544}}, new RungeKuttaStepInterpolator(new RungeKuttaMethod(new RungeKuttaMethod.Order(3))), 0.001, 0.01, null, null);
 integrator.integrate(equations, time);

 double[] actualState = equations.getCompleteState();
 assertArrayEquals(expectedState, actualState);
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorSafety() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.3, 0.4, 0.1}, new double[][]{{0.238903926762647, 0.164333735264629}, {-0.049059370191404, 0.121202152124332}, {0.049059370191404, 0.052359891396209}, {-0.02236133864057, 0.035355339059544}}, new RungeKuttaStepInterpolator(new RungeKuttaMethod(new RungeKuttaMethod.Order(3))), 0.001, 0.01, 0.5, 0.05);
 integrator.setSafety(0.8);
 assertEquals(0.8, integrator.getSafety(), 1e-5);
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorMinReduction() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.3, 0.4, 0.1}, new double[][]{{0.238903926762647, 0.164333735264629}, {-0.049059370191404, 0.121202152124332}, {0.049059370191404, 0.052359891396209}, {-0.02236133864057, 0.035355339059544}}, new RungeKuttaStepInterpolator(new RungeKuttaMethod(new RungeKuttaMethod.Order(3))), 0.001, 0.01, null, null);
 integrator.setMinReduction(0.1);
 assertEquals(0.1, integrator.getMinReduction(), 1e-5);
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorMaxGrowth() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{0.3, 0.4, 0.1}, new double[][]{{0.238903926762647, 0.164333735264629}, {-0.049059370191404, 0.121202152124332}, {0.049059370191404, 0.052359891396209}, {-0.02236133864057, 0.035355339059544}}, new RungeKuttaStepInterpolator(new RungeKuttaMethod(new RungeKuttaMethod.Order(3))), 0.001, 0.01, null, null);
 integrator.setMaxGrowth(2.0);
 assertEquals(2.0, integrator.getMaxGrowth(), 1e-5);
 }

}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
 @Override
 public int getOrder() {
 return 3;
 }

 @Override
 protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
 double error = 0.0;
 for (int i = 0; i < p1.length; i++) {
 error += FastMath.pow(FastMath.abs(p2[i] - p1[i]), 2.0);
 }
 return error;
 }
}

class MyODEFunction implements ODEFunction {
 private double[] state;

 public MyODEFunction(double[] state) {
 this.state = state;
 }

 @Override
 public int getNumberOfStateVariables() {
 return state.length;
 }

 @Override
 public int getNumberOfParameters() {
 return 0;
 }

 @Override
 public void value(double t, double[] y, double[] dy) {
 dy[0] = 1.0;
 dy[1] = 1.0 + y[0];
 }
}

class MockExpandableStatefulODE implements ExpandableStatefulODE {
 private ODEFunction function;
 private double time;
 private double[] completeState;

 public MockExpandableStatefulODE() {
 }

 public void setFunction(ODEFunction function) {
 this.function = function;
 }

 @Override
 public int getNumberOfStateVariables() {
 return function.getNumberOfStateVariables();
 }

 @Override
 public int getNumberOfParameters() {
 return function.getNumberOfParameters();
 }

 @Override
 public double getTime() {
 return time;
 }

 @Override
 public void setTime(double time) {
 this.time = time;
 }

 @Override
 public double[] getCompleteState() {
 return completeState;
 }

 @Override
 public void setCompleteState(double[] state) {
 this.completeState = state;
 }

 @Override
 public double[] getState() {
 return new double[]{completeState[0], completeState[1]};
 }

 @Override
 public double[] getDerivatives(double t, double[] y) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 function.value(t, y, null);
 return y;
 }
}