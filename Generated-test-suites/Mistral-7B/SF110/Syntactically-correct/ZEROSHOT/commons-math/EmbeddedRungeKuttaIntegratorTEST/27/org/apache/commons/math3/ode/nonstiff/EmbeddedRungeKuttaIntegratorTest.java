import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private ExpandableStatefulODE equations;

 @Before
 public void setup() {
 // Initialize the equations for testing
 equations = new SimpleODE("y' = x * y") {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yPrime) {
 yPrime[0] = y[0] * t;
 }
 };

 // Initialize the integrator with default values
 integrator = new EmbeddedRungeKuttaIntegrator("RK45", true, new double[]{-0.236926885056488, 0.275898428571453, 0.046795288466623, -0.049834093191523, 0.129665027370496, 0.472111410136018, 0.218214451612574, 0.0129665027370496},
 new double[][]{{-0.25982952360221, 0.25982952360221, 0.50985896716443, 0.50985896716443, 0.5, 0.5, 0.5, 0.5},
 {-0.12679337130324, -0.12679337130324, 0.12679337130324, 0.12679337130324, 0.5, 0.5, 0.5, 0.5},
 new double[]{-0.01332489516182, -0.01332489516182, 0.01332489516182, 0.01332489516182, 0.5, 0.5, 0.5, 0.5},
 new double[]{0.129665027370496, 0.129665027370496, 0.0, 0.0, 0.5, 0.5, 0.5, 0.5},
 new double[]{0.0, 0.0, 0.0, 0.0, 0.5, 0.5, 0.5, 0.5},
 new double[]{0.0, 0.0, 0.0, 0.0, 0.5, 0.5, 0.5, 0.5},
 new double[]{0.0, 0.0, 0.0, 0.0, 0.5, 0.5, 0.5, 0.5},
 new double[]{0.0, 0.0, 0.0, 0.0, 0.5, 0.5, 0.5, 0.5}},
 new double[]{-0.01332489516182, -0.01332489516182, 0.01332489516182, 0.01332489516182, 0.5, 0.5, 0.5, 0.5},
 new RungeKuttaStepInterpolator() {});
 integrator.setMinStep(0.0001);
 integrator.setMaxStep(0.01);
 integrator.setScalAbsoluteTolerance(1e-8);
 integrator.setScalRelativeTolerance(1e-5);
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Test integration over a given interval
 double[] initialState = new double[]{1.0};
 double startTime = 0.0;
 double endTime = 10.0;
 integrator.integrate(equations, startTime, initialState, endTime);
 double[] expectedState = new double[]{196.48656752255692};
 assertArrayEquals(expectedState, equations.getCompleteState());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegrateWithNullEquations() {
 integrator.integrate(null, 0.0, new double[]{1.0}, 10.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegrateWithNegativeStepSize() {
 integrator.setMinStep(-0.01);
 integrator.integrate(equations, 0.0, new double[]{1.0}, 10.0);
 }

 @Test(expected = IllegalStateException.class)
 public void testIntegrateWithEmptyStepSize() {
 integrator.setMinStep(0.0);
 integrator.integrate(equations, 0.0, new double[]{1.0}, 10.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegrateWithInvalidTolerances() {
 integrator.setMinStep(0.001);
 integrator.setMaxStep(0.001);
 integrator.setScalAbsoluteTolerance(1.0);
 integrator.setScalRelativeTolerance(0.5);
 integrator.integrate(equations, 0.0, new double[]{1.0}, 10.0);
 }

 @Test
 public void testIntegrateWithZeroTolerances() {
 integrator.setMinStep(0.001);
 integrator.setMaxStep(0.001);
 integrator.setScalAbsoluteTolerance(0.0);
 integrator.setScalRelativeTolerance(0.0);
 integrator.integrate(equations, 0.0, new double[]{1.0}, 10.0);
 }

 @Test
 public void testIntegrateWithZeroInitialCondition() {
 integrator.integrate(equations, 0.0, new double[]{0.0}, 10.0);
 }

 @Test
 public void testIntegrateWithNonMonotonicStepSize() {
 integrator.integrator = new NonMonotonicStepSizeIntegrator();
 integrator.integrate(equations, 0.0, new double[]{1.0}, 10.0);
 }
}

class SimpleODE extends AbstractStatefulODE {
 // Empty implementation
}

class NonMonotonicStepSizeIntegrator extends EmbeddedRungeKuttaIntegrator {
 // Override the filterStep method to always return a non-monotonic step size
 @Override
 protected double filterStep(double h, boolean forward, boolean last) {
 return Math.max(h / 2.0, -h / 2.0);
 }
}