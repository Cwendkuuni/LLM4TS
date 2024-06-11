import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.SamplingPoints;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.util.ODETestUtils;
import org.apache.commons.math3.special.TaylorSeries;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private static class TestEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
     private TestEmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
         super(name, fsal, c, a, b, prototype, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
     }

     @Override
     protected int getOrder() {
         return 5;
     }

     @Override
     protected double estimateError(double[][] p0, double[] p1, double[] p2, double p3) {
         return 0.01;
     }
 }

 @Test
 public void testEmbeddedRungeKuttaIntegrator() {
     EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator("Test", false, new double[]{0.3, 0.385, 0.5, 0.833, 1.0}, new double[][]{{0.0, 0.3, 0.3, 0.3732, 0.468}, {0.3, 0.65, 0.32, 0.51, 0.2}, {0.385, 0.833, 0.32, 0.51, 0.143, 0.0}, {0.5, 1.0, 0.32, 0.51, 0.143, 0.0, 0.0}, {0.833, 1.0, 0.51, 0.51, 0.143, 0.0, 0.0, 0.0}}, new double[]{0.0, 0.025, 0.05, 0.1, 0.1}, new StepInterpolator() {
         @Override
         public double[] interpolate(double time, double[] y) {
             return y;
         }

         @Override
         public int getSize() {
             return y.length;
         }
     });

     ExpandableStatefulODE equations = new ExpandableStatefulODE() {
         @Override
         public int getDimension() {
             return 1;
         }

         @Override
         public void computeDerivatives(double time, double[] y, double[] yPrime) {
             double x = y[0];
             yPrime[0] = Math.sin(x);
         }

         @Override
         public String getName() {
             return "TestEquations";
         }
     };

     double[] initialState = new double[]{0.0};
     double time = 2.0;
     SamplingPoints points = new SamplingPoints(new double[]{0.0, 1.0, 2.0});

     integrator.integrate(equations, initialState, time, points);

     double[] expected = new double[]{0.842701, 0.951222, 0.997258};
     assertArrayEquals(expected, equations.getCompleteState(), 0.001);
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorStepSize() {
     EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator("Test", false, new double[]{0.3, 0.385, 0.5, 0.833, 1.0}, new double[][]{{0.0, 0.3, 0.3, 0.3732, 0.468}, {0.3, 0.65, 0.32, 0.51, 0.2}, {0.385, 0.833, 0.32, 0.51, 0.143, 0.0}, {0.5, 1.0, 0.32, 0.51, 0.143, 0.0, 0.0}, {0.833, 1.0, 0.51, 0.51, 0.143, 0.0, 0.0, 0.0}}, new double[]{0.0, 0.025, 0.05, 0.1, 0.1}, new StepInterpolator() {
         @Override
         public double[] interpolate(double time, double[] y) {
             return y;
         }

         @Override
         public int getSize() {
             return y.length;
         }
     });

     ExpandableStatefulODE equations = new ExpandableStatefulODE() {
         @Override
         public int getDimension() {
             return 1;
         }

         @Override
         public void computeDerivatives(double time, double[] y, double[] yPrime) {
             yPrime[0] = Math.exp(-time) * Math.sin(time);
         }

         @Override
         public String getName() {
             return "TestEquations";
         }
     };

     double[] initialState = new double[]{0.0};
     double time = 2.0;
     SamplingPoints points = new SamplingPoints(new double[]{0.0, 1.0, 2.0});

     assertEquals(0.1, integrator.getStepSize(), 0.001);
     integrator.setStepSize(0.05);
     assertEquals(0.05, integrator.getStepSize(), 0.001);
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorSafety() {
     EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator("Test", false, new double[]{0.3, 0.385, 0.5, 0.833, 1.0}, new double[][]{{0.0, 0.3, 0.3, 0.3732, 0.468}, {0.3, 0.65, 0.32, 0.51, 0.2}, {0.385, 0.833, 0.32, 0.51, 0.143, 0.0}, {0.5, 1.0, 0.32, 0.51, 0.143, 0.0, 0.0}, {0.833, 1.0, 0.51, 0.51, 0.143, 0.0, 0.0, 0.0}}, new double[]{0.0, 0.025, 0.05, 0.1, 0.1}, new StepInterpolator() {
         @Override
         public double[] interpolate(double time, double[] y) {
             return y;
         }

         @Override
         public int getSize() {
             return y.length;
         }
     });

     integrator.setSafety(0.7);
     assertEquals(0.7, integrator.getSafety(), 0.001);
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorMinReduction() {
     EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator("Test", false, new double[]{0.3, 0.385, 0.5, 0.833, 1.0}, new double[][]{{0.0, 0.3, 0.3, 0.3732, 0.468}, {0.3, 0.65, 0.32, 0.51, 0.2}, {0.385, 0.833, 0.32, 0.51, 0.143, 0.0}, {0.5, 1.0, 0.32, 0.51, 0.143, 0.0, 0.0}, {0.833, 1.0, 0.51, 0.51, 0.143, 0.0, 0.0, 0.0}}, new double[]{0.0, 0.025, 0.05, 0.1, 0.1}, new StepInterpolator() {
         @Override
         public double[] interpolate(double time, double[] y) {
             return y;
         }

         @Override
         public int getSize() {
             return y.length;
         }
     });

     integrator.setMinReduction(0.5);
     assertEquals(0.5, integrator.getMinReduction(), 0.001);
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorMaxGrowth() {
     EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator("Test", false, new double[]{0.3, 0.385, 0.5, 0.833, 1.0}, new double[][]{{0.0, 0.3, 0.3, 0.3732, 0.468}, {0.3, 0.65, 0.32, 0.51, 0.2}, {0.385, 0.833, 0.32, 0.51, 0.143, 0.0}, {0.5, 1.0, 0.32, 0.51, 0.143, 0.0, 0.0}, {0.833, 1.0, 0.51, 0.51, 0.143, 0.0, 0.0, 0.0}}, new double[]{0.0, 0.025, 0.05, 0.1, 0.1}, new StepInterpolator() {
         @Override
         public double[] interpolate(double time, double[] y) {
             return y;
         }

         @Override
         public int getSize() {
             return y.length;
         }
     });

     integrator.setMaxGrowth(2.0);
     assertEquals(2.0, integrator.getMaxGrowth(), 0.001);
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorOrder() {
     EmbeddedRungeKuttaIntegrator integrator = new TestEmbeddedRungeKuttaIntegrator("Test", false, new double[]{0.3, 0.385, 0.5, 0.833, 1.0}, new double[][]{{0.0, 0.3, 0.3, 0.3732, 0.468}, {0.3, 0.65, 0.32, 0.51, 0.2}, {0.385, 0.833, 0.32, 0.51, 0.143, 0.0}, {0.5, 1.0, 0.32, 0.51, 0.143, 0.0, 0.0}, {0.833, 1.0, 0.51, 0.51, 0.143, 0.0, 0.0, 0.0}}, new double[]{0.0, 0.025, 0.05, 0.1, 0.1}, new StepInterpolator() {
         @Override
         public double[] interpolate(double time, double[] y) {
             return y;
         }

         @Override
         public int getSize() {
             return y.length;
         }
     });

     assertEquals(5, integrator.getOrder(), 0.001);
 }
}