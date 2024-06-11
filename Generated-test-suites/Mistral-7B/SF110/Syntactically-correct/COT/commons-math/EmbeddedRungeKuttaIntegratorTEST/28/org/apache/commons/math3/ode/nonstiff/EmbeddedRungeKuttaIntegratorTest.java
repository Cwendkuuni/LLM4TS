import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double TOLERANCE = 1e-9;

 @Test
 public void testGetOrder() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 Assert.assertEquals(5, integrator.getOrder());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetOrderUnsupported() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator() {
 @Override
 public int getOrder() {
 throw new UnsupportedOperationException();
 }
 };
 integrator.getOrder();
 }

 @Test
 public void testGetSafety() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 Assert.assertEquals(0.9, integrator.getSafety(), TOLERANCE);
 }

 @Test
 public void testSetSafety() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.setSafety(0.8);
 Assert.assertEquals(0.8, integrator.getSafety(), TOLERANCE);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSafetyNegative() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.setSafety(-0.1);
 }

 @Test
 public void testIntegrateBasic() throws Exception {
 double[] y0 = {0.0, 0.0};
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.0);
 Assert.assertEquals(Precision.doubleEquals(1.0, y0[0]), true);
 Assert.assertEquals(Precision.doubleEquals(1.0, y0[1]), true);
 }

 @Test(expected = IllegalStateException.class)
 public void testIntegrateWithNullEquations() throws Exception {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.integrate(null, new double[]{0.0, 0.0}, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegrateWithNullTime() throws Exception {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, null, 1.0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegrateWithNullInitialState() throws Exception {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, null, 1.0);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateWithSmallStepSize() throws Exception {
 double[] y0 = {0.0, 0.0};
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.setMinStep(1e-15);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxCountExceeded() throws Exception {
 double[] y0 = {0.0, 0.0};
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.setMaxStep(1e-9);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.0, 100000);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithNoBracketing() throws Exception {
 double[] y0 = {0.0, 0.0};
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.0, 0.5);
 }

 @Test
 public void testIntegrateWithStepSizeChanged() throws Exception {
 ExpandableStatefulODE equations = new DummyEquations();
 double[] y0 = {0.0, 0.0};
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.0);
 integrator.setMaxStep(2.0);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 2.0);
 integrator.setMinStep(0.1);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.1);
 Assert.assertEquals(Precision.doubleEquals(2.0, equations.getTime()), true);
 }

 @Test
 public void testIntegrateWithStepSizeFilter() throws Exception {
 ExpandableStatefulODE equations = new DummyEquations();
 double[] y0 = {0.0, 0.0};
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.0);
 integrator.setMaxGrowth(2.0);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.5);
 integrator.setMaxGrowth(5.0);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 2.0);
 integrator.setMaxGrowth(10.0);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 3.0);
 Assert.assertEquals(Precision.doubleEquals(3.0, equations.getTime()), true);
 }

 @Test
 public void testIntegrateWithMinReductionChanged() throws Exception {
 ExpandableStatefulODE equations = new DummyEquations();
 double[] y0 = {0.0, 0.0};
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.0);
 integrator.setMinReduction(0.1);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 0.9);
 integrator.setMinReduction(0.5);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 0.5);
 Assert.assertEquals(Precision.doubleEquals(0.5, equations.getTime()), true);
 }

 @Test
 public void testIntegrateWithMinReductionChangedDuringIntegration() throws Exception {
 ExpandableStatefulODE equations = new DummyEquations();
 double[] y0 = {0.0, 0.0};
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.0);
 integrator.setMinReduction(0.1);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 0.9);
 integrator.setMinReduction(0.5);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 0.5);
 integrator.setMinReduction(0.2);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 0.2);
 Assert.assertEquals(Precision.doubleEquals(0.2, equations.getTime()), true);
 }

 @Test
 public void testIntegrateWithMaxGrowthChangedDuringIntegration() throws Exception {
 ExpandableStatefulODE equations = new DummyEquations();
 double[] y0 = {0.0, 0.0};
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator();
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.0);
 integrator.setMaxGrowth(2.0);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 1.5);
 integrator.setMaxGrowth(5.0);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 2.0);
 integrator.setMaxGrowth(10.0);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 3.0);
 integrator.setMaxGrowth(5.0);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 2.5);
 integrator.integrate(new StepHandler() {
 @Override
 public void handleStep(double time, ExpandableStatefulODE equations) {
 // No operation
 }
 }, y0, 2.1);
 Assert.assertEquals(Precision.doubleEquals(2.1, equations.getTime()), true);
 }

}

 class DummyEquations implements ExpandableStatefulODE {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public int getStateDimension() {
 return 2;
 }

 @Override
 public void setTime(double time) {
 // No operation
 }

 @Override
 public double getTime() {
 return 0.0;
 }

 @Override
 public double[] getCompleteState() {
 return new double[]{0.0, 0.0};
 }

 @Override
 public void setCompleteState(double[] state) {
 // No operation
 }

 @Override
 public void setEquations(ExpandableVectorFunction equations) {
 // No operation
 }

 @Override
 public void setParameters(double[] parameters) {
 // No operation
 }

 @Override
 public void setLinearSolver(LinearSolver solver) {
 // No operation
 }

 @Override
 public void setJacobianFunction(ExpandableVectorFunction jacobian) {
 // No operation
 }

 @Override
 public void setJacobianHessianFunction(ExpandableMatrixFunction hessian) {
 // No operation
 }

 @Override
 public void setJacobianFunction(ExpandableMatrixFunction jacobian) {
 // No operation
 }

 @Override
 public void setJacobianHessianFunction(ExpandableMatrixFunction hessian) {
 // No operation
 }

 @Override
 public void setMappers(Map<String, ExpandableVectorFunction> mappers) {
 // No operation
 }
}

 class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
 @Override
 protected double estimateError(final double[][] p0, final double[] p1, final double[] p2, final double p3) {
 return 0.0;
 }
}