import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

@RunWith(Parameterized.class)
public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;
 private double[] initialState;
 private double endTime;

 public AbstractIntegratorTest(String integratorName, FirstOrderDifferentialEquations equations, double[] initialState, double endTime) {
 this.integrator = (AbstractIntegrator) (integratorName.equals("StepIntegrator") ? new StepIntegrator(integratorName) : new Dopri5Integrator(integratorName));
 this.equations = equations;
 this.initialState = initialState;
 this.endTime = endTime;
 }

 @Parameterized.Parameters(name = "{0} integrator with equation: {1} initial state: {2} end time: {3}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 {"StepIntegrator", new RK4IntegratorTestEquations(), new double[]{1.0, 1.0}, 3.0},
 {"Dopri5Integrator", new Dopri5IntegratorTestEquations(), new double[]{1.0, 1.0}, 3.0},
 {"DopriFehlberg78Integrator", new DopriFehlberg78IntegratorTestEquations(), new double[]{1.0, 1.0}, 3.0},
 {"Dopri54Integrator", new Dopri54IntegratorTestEquations(), new double[]{1.0, 1.0}, 3.0},
 {"Dopri853Integrator", new Dopri853IntegratorTestEquations(), new double[]{1.0, 1.0}, 3.0}
 });
 }

 @Test
 public void testIntegration() throws Exception {
 this.integrator.initIntegration(0.0, this.initialState, this.endTime);
 this.integrator.setExpandable(this.equations);
 double[] y = new double[this.equations.getDimension()];
 double t = this.integrator.integrate(this.equations, 0.0, this.initialState, this.endTime, y);
 Assert.assertEquals(this.endTime, t, 1e-9);
 Assert.assertArrayEquals(this.initialState, y, 1e-9);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatchException() throws Exception {
 this.integrator = new AbstractIntegrator("InvalidIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 throw new DimensionMismatchException(equations.getDimension(), 2);
 }
 };
 this.integrator.initIntegration(0.0, new double[]{1.0, 1.0, 1.0}, 3.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() throws Exception {
 this.integrator.setMaxEvaluations(0);
 this.integrator.initIntegration(0.0, this.initialState, this.endTime);
 this.integrator.setExpandable(this.equations);
 this.integrator.integrate(this.equations, 0.0, this.initialState, this.endTime, new double[this.equations.getDimension()]);
 }

 @Test(expected = NoBracketingException.class)
 public void testNoBracketingException() throws Exception {
 this.integrator.setMaxEvaluations(100_000);
 this.integrator.initIntegration(0.0, this.initialState, this.endTime);
 this.integrator.setExpandable(new RK4IntegratorTestEquations());
 this.integrator.integrate(this.equations, 0.0, this.initialState, 0.0, new double[this.equations.getDimension()]);
 }
}