import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double ACCURACY = 1e-8;
 private static final double INITIAL_TIME = 0.0;
 private static final double FINAL_TIME = 10.0;
 private static final double[] INITIAL_STATE = {1.0, 1.0};
 private static final double[][] FUNCTION = { {-0.1, 0.0}, {0.3, -0.05} };

 @Test
 public void testRungeKutta23() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RungeKutta23", true, new double[] { 0.3, 0.47366, 0.07231, 0.0 }, new double[][] { { 0.34666, 0.33767, 0.20138, 0.07231 }, { 0.29566, 0.17239, 0.03472, 0.07231 }, { 0.1, 0.0, 0.0, 0.72984 }, { 0.0, 0.0, 0.0, 0.72984 } }, new double[] { 0.3, 0.0 }, new RungeKuttaStepInterpolator() {
 @Override
 public double integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Empty implementation, since the integrator under test does the actual integration.
 return 0.0;
 }

 @Override
 public void reinitialize(ExpandableStatefulODE equations, double[] y, double[] yDot, boolean forward, FunctionMapper primaryMapper, FunctionMapper[] secondaryMappers) {
 // Empty implementation.
 }

 @Override
 public void storeTime(double time) {
 // Empty implementation.
 }
 }, 1.0e-10, 1.0e-6, ACCURACY, ACCURACY);

 ExpandableODE equations = new ExpandableODE() {
 @Override
 public int getDimension() {
 return INITIAL_STATE.length;
 }

 @Override
 public void computeDerivatives(double time, double[] state, double[] derivatives) {
 System.arraycopy(FUNCTION[0], 0, derivatives, 0, derivatives.length);
 System.arraycopy(state, 0, stateAdd, 0, state.length);
 System.arraycopy(stateAdd, 0, state, 0, state.length);
 }
 };

 integrator.integrate(equations, INITIAL_TIME);
 double[] expectedState = new double[] { 1.0 + 0.3 * FUNCTION[0][0] * INITIAL_TIME, 1.0 + 0.3 * FUNCTION[0][1] * INITIAL_TIME };
 assertArrayEquals("Initial state not correct.", expectedState, equations.getCompleteState());

 integrator.integrate(equations, FINAL_TIME);
 expectedState = new double[] { 1.0 + 0.3 * FUNCTION[0][0] * FINAL_TIME + 0.3 * FUNCTION[0][0] * INITIAL_TIME * FUNCTION[0][1], 1.0 + 0.3 * FUNCTION[0][1] * FINAL_TIME + 0.3 * FUNCTION[0][1] * INITIAL_TIME * FUNCTION[0][0] };
 assertArrayEquals("Final state not correct.", expectedState, equations.getCompleteState());
 }

 @Test
 public void testRungeKutta23Error() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RungeKutta23", true, new double[] { 0.3, 0.47366, 0.07231, 0.0 }, new double[][] { { 0.34666, 0.33767, 0.20138, 0.07231 }, { 0.29566, 0.17239, 0.03472, 0.07231 }, { 0.1, 0.0, 0.0, 0.72984 }, { 0.0, 0.0, 0.0, 0.72984 } }, new double[] { 0.3, 0.0 }, new RungeKuttaStepInterpolator() {
 // Implementation identical to the testRungeKutta23 test, except for the name.
 }, 1.0e-10, 1.0e-6, ACCURACY, ACCURACY);

 ExpandableODE equations = new ExpandableODE() {
 @Override
 public int getDimension() {
 return INITIAL_STATE.length;
 }

 @Override
 public void computeDerivatives(double time, double[] state, double[] derivatives) {
 // Implement a test case where the derivatives cause an error.
 derivatives[0] = Double.NaN;
 }
 };

 try {
 integrator.integrate(equations, INITIAL_TIME);
 fail("Expected NumberIsTooSmallException.");
 } catch (NumberIsTooSmallException e) {
 // Expected.
 }
 }
}