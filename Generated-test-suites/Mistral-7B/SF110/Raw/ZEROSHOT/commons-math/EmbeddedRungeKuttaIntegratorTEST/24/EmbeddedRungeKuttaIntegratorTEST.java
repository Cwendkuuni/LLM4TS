import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.RungeKuttaStepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double DELTA = 1e-9;
 private static final double ABS_TOL = 1e-8;
 private static final double REL_TOL = 1e-8;
 private static final double MIN_STEP = 1.0e-8;
 private static final double MAX_STEP = 1.0e2;

 @Test
 public void testEmbeddedRungeKuttaIntegrator() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", true, new double[]{0.3, 0.3, 0.3, 0.3, 0.3}, new double[][]{{0.0, 0.43619, 0.0, 0.43619, 0.0}, {0.0, 0, 0.83239, 0.0, 0.83239}, {0.0, 0, 0.0, 1.26478, 0.0}, {0.0, 0, 0.0, 0.0, 1.69811}, {0.0, 0, 0.0, 0.0, 0.0}}, new RungeKuttaStepInterpolator(new EmbeddedRungeKuttaMethod()), MIN_STEP, MAX_STEP, ABS_TOL, REL_TOL);

 // Test getOrder method
 assertEquals("getOrder should return the correct order", 5, integrator.getOrder());

 // Test getSafety method
 assertEquals("getSafety should return the default safety value", 0.9, integrator.getSafety(), 0.01);

 // Test integration of a simple ODE
 double[] initialValues = new double[]{0.0, 1.0};
 double startTime = 0.0;
 double endTime = 10.0;
 ExpandableStatefulODE equations = new DummyODE(initialValues);
 integrator.integrate(equations, startTime, endTime);
 double[] expectedValues = new double[]{0.426447318726833, 2.718281828459045};
 assertArrayEquals("Integration result should match the expected values", expectedValues, equations.getCompleteState());

 // Test integration with absolute and relative tolerances
 integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", true, new double[]{0.3, 0.3, 0.3, 0.3, 0.3}, new double[][]{{0.0, 0.43619, 0.0, 0.43619, 0.0}, {0.0, 0, 0.83239, 0.0, 0.83239}, {0.0, 0, 0.0, 1.26478, 0.0}, {0.0, 0, 0.0, 0.0, 1.69811}, {0.0, 0, 0.0, 0.0, 0.0}}, new RungeKuttaStepInterpolator(new EmbeddedRungeKuttaMethod()), MIN_STEP, MAX_STEP, new double[]{ABS_TOL, REL_TOL}, new double[]{ABS_TOL, REL_TOL});
 integrator.integrate(equations, startTime, endTime);
 expectedValues = new double[]{0.426447318726833, 2.718281828459045};
 assertArrayEquals("Integration result should match the expected values with tolerances", expectedValues, equations.getCompleteState(), DELTA);

 // Test integration with minReduction and maxGrowth
 integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", true, new double[]{0.3, 0.3, 0.3, 0.3, 0.3}, new double[][]{{0.0, 0.43619, 0.0, 0.43619, 0.0}, {0.0, 0, 0.83239, 0.0, 0.83239}, {0.0, 0, 0.0, 1.26478, 0.0}, {0.0, 0, 0.0, 0.0, 1.69811}, {0.0, 0, 0.0, 0.0, 0.0}}, new RungeKuttaStepInterpolator(new EmbeddedRungeKuttaMethod()), MIN_STEP, MAX_STEP, null, null);
 integrator.setMinReduction(0.1);
 integrator.setMaxGrowth(2.0);
 integrator.integrate(equations, startTime, endTime);
 expectedValues = new double[]{0.426447318726833, 2.718281828459045};
 assertArrayEquals("Integration result should match the expected values with minReduction and maxGrowth", expectedValues, equations.getCompleteState(), DELTA);
 }

 private static class DummyODE implements ExpandableStatefulODE {
 private double[] state;

 public DummyODE(double[] initialValues) {
 this.state = initialValues;
 }

 @Override
 public int getDimension() {
 return state.length;
 }

 @Override
 public void setTime(double time) {
 // Empty implementation
 }

 @Override
 public void setCompleteState(double[] state) {
 this.state = state;
 }

 @Override
 public double[] getCompleteState() {
 return state;
 }

 @Override
 public ExpandableVectorFunction evaluateDerivatives(double time, double[] state) throws NumberIsTooSmallException, SingularMatrixException {
 throw new UnsupportedOperationException("Not used in this test");
 }
 }
}