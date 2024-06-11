import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private StepHandler stepHandler;
 private ExpandableODE equations;

 @BeforeAll
 public void setup() {
 integrator = new EmbeddedRungeKuttaMethod(
 "RKF45",
 true,
 new double[]{0.23692688505667133, 0.2758620005672427, 0.04640668139645971, 0.040386757514717225, 0.0, 0.0},
 new double[][]{{0.3568782836596421, 0.0, 0.0, 0.0, 0.0, 0.0}, {0, 0.3554334835345816, 0.3526945425251873, 0.0, 0.0, 0.0}, {0, 0, 0.3482234698872353, 0.3437566123413271, 0.0, 0.0}, {0, 0, 0, 0.3345872169942335, 0.3246142979168871, 0.0}, {0, 0, 0, 0, 0.3162277659757217, 0.3060234390720346}, {0, 0, 0, 0, 0, 0.2977253322306963}},
 new double[]{0, 0.25, 0.25, 0.25, 0.25, 0.25},
 new RungeKuttaStepInterpolator() {
 @Override
 public void initialize(double[] y, double[] ydot, boolean isAutodidactic, double t, double[] dydt, double[] dydt2) {}

 @Override
 public void reinitialize(EmbeddedRungeKuttaIntegrator integrator, double[] y, double[] ydot, boolean isAutodidactic, double t, double[] dydt, double[] dydt2) {
 this.integrator = integrator;
 }

 @Override
 public void storeTime(double t) {
 this.integrator.storeTime(t);
 }

 @Override
 public void storeDydt(double[] ydot) {
 this.integrator.storeDydt(ydot);
 }

 @Override
 public void storeDydt2(double[] ydot2) {}

 @Override
 public void storeStep(double h) {}

 @Override
 public void storeDydts(double[][] ydots) {}

 @Override
 public void storeState(double[] y) {
 this.integrator.setEquations(new ExpandableODE() {
 @Override
 public int getDimension() {
 return 3;
 }

 @Override
 public double getTime() {
 return this.integrator.getStepStart();
 }

 @Override
 public void setTime(double time) {
 this.integrator.setStepStart(time);
 }

 @Override
 public void setCompleteState(double[] state) {
 this.integrator.setCompleteState(state);
 }

 @Override
 public double[] getCompleteState() {
 return this.integrator.getCompleteState();
 }

 @Override
 public double[] getDerivatives(double[] state) {
 return this.integrator.getDerivatives(state);
 }

 @Override
 public void setDerivatives(double[] derivatives) {
 this.integrator.setDerivatives(derivatives);
 }

 @Override
 public double[] getPrimaryMapper() {
 return new double[]{0, 1, 2};
 }

 @Override
 public double[][] getSecondaryMappers() {
 return new double[][]{{0, 1}, {1, 2}, {2, 0}};
 }
 });
 }

 @Override
 public void integrate(double t, double[] y, double[] ydot) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 this.integrator.integrate(this, t);
 }

 @Override
 public void handleStep(double[] y, double[] ydot) {}
 });
 stepHandler = new StepHandler(100);
 equations = new ExpandableODE() {
 @Override
 public int getDimension() {
 return 3;
 }

 @Override
 public double getTime() {
 return 0.0;
 }

 @Override
 public void setTime(double time) {}

 @Override
 public void setCompleteState(double[] state) {}

 @Override
 public double[] getCompleteState() {
 return new double[]{0.0, 0.0, 0.0};
 }

 @Override
 public double[] getDerivatives(double[] state) {
 return new double[]{state[0], state[1], state[2]};
 }

 @Override
 public void setDerivatives(double[] derivatives) {}

 @Override
 public double[] getPrimaryMapper() {
 return new double[]{0, 1, 2};
 }

 @Override
 public double[][] getSecondaryMappers() {
 return new double[][]{{0, 1}, {1, 2}, {2, 0}};
 }
 };
 }

 @Test
 public void testIntegration() throws Exception {
 integrator.integrate(equations, 1.0);
 double[] expectedState = {1.0, 1.0, 1.0};
 assertArrayEquals(expectedState, equations.getCompleteState());
 }

 @Test
 public void testIntegrationWithInitialError() throws Exception {
 double[] initialState = {0.1, 0.1, 0.1};
 integrator.integrate(equations, 1.0, initialState);
 double[] expectedState = {1.0, 1.0, 1.0};
 assertArrayEquals(expectedState, equations.getCompleteState());
 }

 @Test
 public void testIntegrationWithInitialErrorAndTolerance() throws Exception {
 double[] initialState = {0.1, 0.1, 0.1};
 integrator.setAbsoluteTolerance(1.0);
 integrator.integrate(equations, 1.0, initialState);
 double[] expectedState = {1.0, 1.0, 1.0};
 double[] actualState = equations.getCompleteState();
 assertTrue(Arrays.stream(actualState).mapToDouble(d -> Math.abs(expectedState[0] - d) < 1.0).allMatch(b -> b));
 }

 @Test
 public void testIntegrationWithRelativeTolerance() throws Exception {
 double[] initialState = {0.1, 0.1, 0.1};
 integrator.setRelativeTolerance(0.1);
 integrator.integrate(equations, 1.0, initialState);
 double[] expectedState = {1.0, 1.0, 1.0};
 double[] actualState = equations.getCompleteState();
 assertTrue(Arrays.stream(actualState).mapToDouble(d -> Math.abs(expectedState[0] - d) / Math.abs(expectedState[0]) < 0.1).allMatch(b -> b));
 }

 @Test
 public void testIntegrationWithToleranceAndInitialError() throws Exception {
 double[] initialState = {0.1, 0.1, 0.1};
 integrator.setAbsoluteTolerance(1.0);
 integrator.integrate(equations, 1.0, initialState);
 integrator.setRelativeTolerance(0.1);
 integrator.resetInternalState();
 integrator.integrate(equations, 1.0, initialState);
 double[] expectedState = {1.0, 1.0, 1.0};
 double[] actualState = equations.getCompleteState();
 assertTrue(Arrays.stream(actualState).mapToDouble(d -> Math.abs(expectedState[0] - d) < 0.1).allMatch(b -> b) || Math.abs(expectedState[0] - d) < 1.0);
 }
}