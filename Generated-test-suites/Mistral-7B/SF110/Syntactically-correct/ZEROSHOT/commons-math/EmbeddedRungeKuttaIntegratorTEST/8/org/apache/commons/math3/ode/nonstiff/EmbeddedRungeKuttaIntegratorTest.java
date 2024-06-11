import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double ABSOLUTE_TOLERANCE = 1e-10;
 private static final double RELATIVE_TOLERANCE = 1e-10;
 private static final double INITIAL_VALUE = 1.0;
 private static final double FINAL_VALUE = 2.0;
 private static final double INITIAL_TIME = 0.0;
 private static final double FINAL_TIME = 2.0;

 @Test
 public void testEmbeddedRungeKuttaIntegrator() throws Exception {
 // Test constructor with absolute and relative tolerances
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", true, new double[]{0.3, 0.4, 0.1, 0.2}, new double[][]{{0.3377459661924648, 0.325393934244138}, {0.3088056217462456, 0.315133739525234}, {0.2057617330612519, 0.235619835323679}, {0.1786321656529687, 0.1786321656529687}}, new double[]{0.0, 0.065, 0.0, 0.0}, new RungeKuttaStepInterpolator(), INITIAL_TIME, INITIAL_TIME + 1.0, ABSOLUTE_TOLERANCE, RELATIVE_TOLERANCE);

 // Test getOrder method
 assertEquals("getOrder should return the correct order", 4, integrator.getOrder());

 // Test integration
 StepHandler stepHandler = new StepHandler() {
 @Override
 public void handleStep(StepContext context) {
 double[] y = context.getY();
 double t = context.getTime();
 System.out.println("t = " + t + ", y = " + Precision.formatDouble(y[0], 10));
 }
 };
 integrator.integrate(new ExpandableODE("TestEquation") {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = y[0] * 0.1;
 }
 }, INITIAL_TIME, FINAL_TIME, 0.01, 100, stepHandler);

 // Test getSafety, getMinReduction, getMaxGrowth methods
 assertEquals("safety should be set to 0.9", 0.9, integrator.getSafety(), 1e-6);
 assertEquals("minReduction should be set to 0.2", 0.2, integrator.getMinReduction(), 1e-6);
 assertEquals("maxGrowth should be set to 10.0", 10.0, integrator.getMaxGrowth(), 1e-6);

 // Test setSafety, setMinReduction, setMaxGrowth methods
 integrator.setSafety(0.8);
 assertEquals("safety should be set to 0.8", 0.8, integrator.getSafety(), 1e-6);
 integrator.setMinReduction(0.3);
 assertEquals("minReduction should be set to 0.3", 0.3, integrator.getMinReduction(), 1e-6);
 integrator.setMaxGrowth(5.0);
 assertEquals("maxGrowth should be set to 5.0", 5.0, integrator.getMaxGrowth(), 1e-6);

 // Test integrate method with new tolerances
 integrator = new EmbeddedRungeKuttaIntegrator("EmbeddedRungeKuttaIntegrator", true, new double[]{0.3, 0.4, 0.1, 0.2}, new double[][]{{0.3377459661924648, 0.325393934244138}, {0.3088056217462456, 0.315133739525234}, {0.2057617330612519, 0.235619835323679}, {0.1786321656529687, 0.1786321656529687}}, new double[]{0.0, 0.065, 0.0, 0.0}, new RungeKuttaStepInterpolator(), INITIAL_TIME, INITIAL_TIME + 1.0, null, new double[]{1e-12, 1e-12});
 integrator.integrate(new ExpandableODE("TestEquation") {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] dy) {
 dy[0] = y[0] * 0.1;
 }
 }, INITIAL_TIME, FINAL_TIME, 0.01, 100, stepHandler);
 }
}