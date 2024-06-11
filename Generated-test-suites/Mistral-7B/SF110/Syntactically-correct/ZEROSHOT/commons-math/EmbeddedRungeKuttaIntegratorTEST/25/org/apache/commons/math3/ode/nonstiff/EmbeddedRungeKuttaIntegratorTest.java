import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double ABSOLUTE_TOLERANCE = 1.0e-6;
 private static final double RELATIVE_TOLERANCE = 1.0e-6;
 private static final double MIN_STEP = 0.01;
 private static final double MAX_STEP = 1.0;
 private static final int ORDER = 5; // assume the integrator has a fifth-order method
 private static final double SAFETY = 0.9;
 private static final double MIN_REDUCTION = 0.2;
 private static final double MAX_GROWTH = 10.0;

 @Test
 public void testEmbeddedRungeKuttaIntegrator() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{0.3, 0.1, 0.1, 0.1, 0.1}, new double[][]{{0.34662697236611973, 0.0, 0.22360679774997904, 0.10282872188526699, 0.04929150251612238}, {0.0, 0.34662697236611973, 0.30633296486586423, 0.13247140195735634, 0.05551153953097561}, {0.0, 0.0, 0.34662697236611973, 0.3226264820257479, 0.12181582275822584}, {0.0, 0.0, 0.0, 0.34662697236611973, 0.31368121617347106}, {0.0, 0.0, 0.0, 0.0, 0.34662697236611973}}, new double[]{0.0, 0.135625}, new RungeKuttaStepInterpolator(new RungeKuttaMethod(ORDER)) {
 @Override
 protected double getOrder() {
 return ORDER;
 }
 });
 integrator.setSafety(SAFETY);
 integrator.setMinReduction(MIN_REDUCTION);
 integrator.setMaxGrowth(MAX_GROWTH);
 ExpandableStatefulODE equations = new ExpandableODE() {
 @Override
 public int getDimension() {
 return 5;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yPrime) {
 yPrime[0] = y[1];
 yPrime[1] = y[2];
 yPrime[2] = y[3];
 yPrime[3] = y[4];
 yPrime[4] = -2.0 * y[0] + 3.0 * y[1];
 }
 };

 try {
 integrator.integrate(equations, 5.0);
 assertEquals("Final state is incorrect.", new double[]{3.75, 3.5, 3.0, 2.5, 2.0}, equations.getCompleteState(), ABSOLUTE_TOLERANCE);
 } catch (Exception e) {
 fail("An exception was thrown during integration: " + e.getMessage());
 }
 }

 @Test
 public void testMinStep() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{0.3, 0.1, 0.1, 0.1, 0.1}, new double[][]{{0.34662697236611973, 0.0, 0.22360679774997904, 0.10282872188526699, 0.04929150251612238}, {0.0, 0.34662697236611973, 0.30633296486586423, 0.13247140195735634, 0.05551153953097561}, {0.0, 0.0, 0.34662697236611973, 0.3226264820257479, 0.12181582275822584}, {0.0, 0.0, 0.0, 0.34662697236611973, 0.31368121617347106}, {0.0, 0.0, 0.0, 0.0, 0.34662697236611973}}, new double[]{0.0, 0.135625}, new RungeKuttaStepInterpolator(new RungeKuttaMethod(ORDER)) {
 @Override
 protected double getOrder() {
 return ORDER;
 }
 });
 integrator.setSafety(SAFETY);
 integrator.setMinReduction(MIN_REDUCTION);
 integrator.setMaxGrowth(MAX_GROWTH);
 integrator.setMinStep(MIN_STEP);
 integrator.setMaxStep(MAX_STEP);
 ExpandableStatefulODE equations = new ExpandableODE() {
 @Override
 public int getDimension() {
 return 5;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yPrime) {
 yPrime[0] = y[1];
 yPrime[1] = y[2];
 yPrime[2] = y[3];
 yPrime[3] = y[4];
 yPrime[4] = -2.0 * y[0] + 3.0 * y[1];
 }
 };

 try {
 integrator.integrate(equations, 5.0);
 fail("Integration should have failed due to a small step size.");
 } catch (MaxCountExceededException e) {
 // Expected
 }
 }

 @Test
 public void testMaxStep() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{0.3, 0.1, 0.1, 0.1, 0.1}, new double[][]{{0.34662697236611973, 0.0, 0.22360679774997904, 0.10282872188526699, 0.04929150251612238}, {0.0, 0.34662697236611973, 0.30633296486586423, 0.13247140195735634, 0.05551153953097561}, {0.0, 0.0, 0.34662697236611973, 0.3226264820257479, 0.12181582275822584}, {0.0, 0.0, 0.0, 0.34662697236611973, 0.31368121617347106}, {0.0, 0.0, 0.0, 0.0, 0.34662697236611973}}, new double[]{0.0, 0.135625}, new RungeKuttaStepInterpolator(new RungeKuttaMethod(ORDER)) {
 @Override
 protected double getOrder() {
 return ORDER;
 }
 });
 integrator.setSafety(SAFETY);
 integrator.setMinReduction(MIN_REDUCTION);
 integrator.setMaxGrowth(MAX_GROWTH);
 integrator.setMinStep(MIN_STEP);
 integrator.setMaxStep(MAX_STEP * 10);
 ExpandableStatefulODE equations = new ExpandableODE() {
 @Override
 public int getDimension() {
 return 5;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yPrime) {
 yPrime[0] = y[1];
 yPrime[1] = y[2];
 yPrime[2] = y[3];
 yPrime[3] = y[4];
 yPrime[4] = -2.0 * y[0] + 3.0 * y[1];
 }
 };

 try {
 integrator.integrate(equations, 5.0);
 fail("Integration should have failed due to a large step size.");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }
 }

 @Test
 public void testEstimateError() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("TestIntegrator", true, new double[]{0.3, 0.1, 0.1, 0.1, 0.1}, new double[][]{{0.34662697236611973, 0.0, 0.22360679774997904, 0.10282872188526699, 0.04929150251612238}, {0.0, 0.34662697236611973, 0.30633296486586423, 0.13247140195735634, 0.05551153953097561}, {0.0, 0.0, 0.34662697236611973, 0.3226264820257479, 0.12181582275822584}, {0.0, 0.0, 0.0, 0.34662697236611973, 0.31368121617347106}, {0.0, 0.0, 0.0, 0.0, 0.34662697236611973}}, new double[]{0.0, 0.135625}, new RungeKuttaStepInterpolator(new RungeKuttaMethod(ORDER)) {
 @Override
 protected double getOrder() {
 return ORDER;
 }
 });
 integrator.setSafety(SAFETY);
 integrator.setMinReduction(MIN_REDUCTION);
 integrator.setMaxGrowth(MAX_GROWTH);
 ExpandableStatefulODE equations = new ExpandableODE() {
 @Override
 public int getDimension() {
 return 5;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yPrime) {
 yPrime[0] = y[1];
 yPrime[1] = y[2];
 yPrime[2] = y[3];
 yPrime[3] = y[4];
 yPrime[4] = -2.0 * y[0] + 3.0 * y[1];
 }
 };
 double[] state = new double[]{1.0, 0.1, 0.2, 0.3, 0.4};
 integrator.integrate(equations, 1.5);
 double[] result = equations.getCompleteState();
 double error = integrator.estimateError(equations.getStateInterpolator().getValues(), state, result, integrator.getStepSize());
 assertEquals("The estimated error is incorrect.", 0.001, error, 0.0001);
 }

 private static class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
 protected MyEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
 super(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
 }

 @Override
 protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
 double error = 0.0;
 for (int i = 0; i < p1.length; i++) {
 double diff = FastMath.abs(p2[i] - p1[i]);
 error += diff * diff;
 }
 error = FastMath.sqrt(error);
 return error;
 }
 }
}