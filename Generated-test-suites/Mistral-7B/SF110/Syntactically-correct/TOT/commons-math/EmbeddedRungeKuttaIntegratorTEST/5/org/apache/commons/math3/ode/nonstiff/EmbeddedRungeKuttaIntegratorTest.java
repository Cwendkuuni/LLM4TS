import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double ABS_TOL = 1e-9;
 private static final double REL_TOL = 1e-6;
 private static final double INITIAL_TIME = 0.0;
 private static final double FINAL_TIME = 10.0;
 private static final double[] INITIAL_STATE = {1.0, 1.0};

 @Test
 public void testEmbeddedRungeKuttaIntegrator() throws Exception {
  EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4, 0.1}, new double[][]{{0.6, 0.2, 0.1}, {0.1, 0.2, 0.2}, {0.0, 0.3, 0.3}}, new double[]{0.1, 0.2}, new StepInterpolator() {
    @Override
    public double integrate(ExpandableStatefulODE equations, double t) throws Exception {
      equations.setTime(t);
      double y[] = equations.getCompleteState();
      double dy[] = new double[y.length];
      for (int i = 0; i < y.length; i++) {
        dy[i] = y[i] * y[i];
      }
      equations.setDerivatives(dy);
      return 0.1;
    }
  }, 0.001, 0.01, ABS_TOL, REL_TOL);

  ExpandableStatefulODE equations = new AbstractODEFunction() {
    @Override
    public int getDimension() {
      return INITIAL_STATE.length;
    }

    @Override
    public double value(double t, double[] y) {
      return 0.0;
    }

    @Override
    public void deriveValues(double t, double[] y, double[] yPrime) {
      for (int i = 0; i < INITIAL_STATE.length; i++) {
        yPrime[i] = y[i] * y[i];
      }
    }
  };

  integrator.integrate(equations, FINAL_TIME);

  double[] expectedFinalState = {Math.exp(1.0), Math.exp(1.0)};
  assertArrayEquals(expectedFinalState, equations.getCompleteState());
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testSmallStep() throws Exception {
  EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4, 0.1}, new double[][]{{0.6, 0.2, 0.1}, {0.1, 0.2, 0.2}, {0.0, 0.3, 0.3}}, new double[]{0.1, 0.2}, new StepInterpolator() {
    @Override
    public double integrate(ExpandableStatefulODE equations, double t) throws Exception {
      equations.setTime(t);
      throw new NumberIsTooSmallException();
    }
  }, 0.0001, 0.01, ABS_TOL, REL_TOL);

  ExpandableStatefulODE equations = new AbstractODEFunction() {
    @Override
    public int getDimension() {
      return INITIAL_STATE.length;
    }

    @Override
    public double value(double t, double[] y) {
      return 0.0;
    }

    @Override
    public void deriveValues(double t, double[] y, double[] yPrime) {
      for (int i = 0; i < INITIAL_STATE.length; i++) {
        yPrime[i] = y[i] * y[i];
      }
    }
  };

  integrator.integrate(equations, FINAL_TIME);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testMismatchedDimensions() throws Exception {
  EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4}, new double[][]{{0.6, 0.2}, {0.1, 0.2}, {0.0, 0.3}}, new double[]{0.1, 0.2}, new StepInterpolator() {
    @Override
    public double integrate(ExpandableStatefulODE equations, double t) throws Exception {
      equations.setTime(t);
      return 0.1;
    }
  }, 0.001, 0.01, ABS_TOL, REL_TOL);

  ExpandableStatefulODE equations = new AbstractODEFunction() {
    @Override
    public int getDimension() {
      return 3; // different than the expected dimension
    }

    @Override
    public double value(double t, double[] y) {
      return 0.0;
    }

    @Override
    public void deriveValues(double t, double[] y, double[] yPrime) {
      for (int i = 0; i < INITIAL_STATE.length; i++) {
        yPrime[i] = y[i] * y[i];
      }
    }
  };

  integrator.integrate(equations, FINAL_TIME);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceeded() throws Exception {
  EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4, 0.1}, new double[][]{{0.6, 0.2, 0.1}, {0.1, 0.2, 0.2}, {0.0, 0.3, 0.3}}, new double[]{0.1, 0.2}, new StepInterpolator() {
    @Override
    public double integrate(ExpandableStatefulODE equations, double t) throws Exception {
      throw new MaxCountExceededException();
    }
  }, 0.001, 0.01, ABS_TOL, REL_TOL);

  ExpandableStatefulODE equations = new AbstractODEFunction() {
    @Override
    public int getDimension() {
      return INITIAL_STATE.length;
    }

    @Override
    public double value(double t, double[] y) {
      return 0.0;
    }

    @Override
    public void deriveValues(double t, double[] y, double[] yPrime) {
      for (int i = 0; i < INITIAL_STATE.length; i++) {
        yPrime[i] = y[i] * y[i];
      }
    }
  };

  integrator.integrate(equations, FINAL_TIME);
 }

 @Test(expected = NoBracketingException.class)
 public void testNoBracketing() throws Exception {
  EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", false, new double[]{0.3, 0.4, 0.1}, new double[][]{{0.6, 0.2, 0.1}, {0.1, 0.2, 0.2}, {0.0, 0.3, 0.3}}, new double[]{0.1, 0.2}, new StepInterpolator() {
    @Override
    public double integrate(ExpandableStatefulODE equations, double t) throws Exception {
      equations.setTime(t);
      if (t > 5.0) {
        throw new NoBracketingException();
      }
      return 0.1;
    }
  }, 0.001, 0.01, ABS_TOL, REL_TOL);

  ExpandableStatefulODE equations = new AbstractODEFunction() {
    @Override
    public int getDimension() {
      return INITIAL_STATE.length;
    }

    @Override
    public double value(double t, double[] y) {
      return 0.0;
    }

    @Override
    public void deriveValues(double t, double[] y, double[] yPrime) {
      for (int i = 0; i < INITIAL_STATE.length; i++) {
        yPrime[i] = y[i] * y[i];
      }
    }
  };

  integrator.integrate(equations, FINAL_TIME);
 }
}