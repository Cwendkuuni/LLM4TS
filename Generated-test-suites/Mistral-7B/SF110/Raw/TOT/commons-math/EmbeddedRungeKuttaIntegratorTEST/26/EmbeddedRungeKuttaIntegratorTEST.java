import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double ABSOLUTE_TOLERANCE = 1e-10;
 private static final double RELATIVE_TOLERANCE = 1e-6;

 @Test
 public void testRK4Integrator() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
  EmbeddedRungeKuttaIntegrator rk4Integrator = new RK4Integrator("RK4", false, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{ {0.5, 0.5}, {0.5, 0}, {0, 0.5}, {0, 0.5} }, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator() {});

  ExpandableStatefulODE equations = new ExpandableODE() {
   @Override
   public int getDimension() {
    return 2;
   }

   @Override
   public void computeDerivatives(double t, double[] y, double[] dy) {
    dy[0] = y[1];
    dy[1] = -y[0];
   }

   @Override
   public double getTime() {
    return 0.0;
   }

   @Override
   public double getPrimaryMapper() {
    return 0.0;
   }

   @Override
   public double[] getSecondaryMappers() {
    return new double[0];
   }
  };

  double[] initialState = new double[]{1.0, 1.0};
  double[] expectedState = new double[]{3.0, 1.5};

  rk4Integrator.integrate(equations, 2.0);
  double[] resultState = equations.getCompleteState();

  assertEquals(expectedState[0], resultState[0], ABSOLUTE_TOLERANCE);
  assertEquals(expectedState[1], resultState[1], ABSOLUTE_TOLERANCE);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testNumberIsTooSmallException() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
  EmbeddedRungeKuttaIntegrator rk4Integrator = new RK4Integrator("RK4", false, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{ {0.5, 0.5}, {0.5, 0}, {0, 0.5}, {0, 0.5} }, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator() {});

  ExpandableStatefulODE equations = new ExpandableODE() {
   @Override
   public int getDimension() {
    return 2;
   }

   @Override
   public void computeDerivatives(double t, double[] y, double[] dy) {
    dy[0] = y[1];
    dy[1] = -y[0];
   }

   @Override
   public double getTime() {
    return 0.0;
   }

   @Override
   public double getPrimaryMapper() {
    return 0.0;
   }

   @Override
   public double[] getSecondaryMappers() {
    return new double[0];
   }
  };

  double[] initialState = new double[]{0.0, 1.0};
  double targetTime = 2.0;

  rk4Integrator.integrate(equations, targetTime);
 }
}