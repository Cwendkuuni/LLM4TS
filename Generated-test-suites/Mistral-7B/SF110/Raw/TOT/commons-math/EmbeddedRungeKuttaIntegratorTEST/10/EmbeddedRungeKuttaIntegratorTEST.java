package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private StepInterpolator interpolator;
 private ExpandableStatefulODE equations;

 @Before
 public void setUp() {
 integrator = new RungeKuttaFehlberg78MethodIntegrator("RKF78", false, new double[]{-0.023, 0.275, 0.467, 0.688, 0.917, 1.0}, new double[][]{{0.3488, -0.4329, 0.9324, 0.7729, 0.2381, 0.6745}, {0.3194, 0.3345, 0.2124, -0.5282, 0.7228, 0.6391}, {0.6261, 0.1845, -0.1736, -0.1706, 0.0768, 0.1613}, {0.8361, -0.0666, 0.6505, 0.0563, -0.1056, 0.0458}, {0.3983, 0.6575, -0.0147, 0.2151, 0.0, 0.0}, {-0.1232, 0.9182, 0.9821, 0.1479, 0.0, 0.0}}, new RungeKuttaStepInterpolator(new RungeKuttaMethod(integrator.getOrder(), integrator.getC(), integrator.getA(), integrator.getB())));
 interpolator = integrator.prototype.copy();
 equations = new DummyODE();
 }

 @Test
 public void testIntegration() {
 double[] initialState = {1.0, 1.0};
 double time = 2.0;
 integrator.integrate(equations, initialState, time);
 double[] expectedState = {2.718281828, 2.718281828};
 assertEquals(expectedState[0], equations.getCompleteState()[0], Precision.EPSILON);
 assertEquals(expectedState[1], equations.getCompleteState()[1], Precision.EPSILON);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrationWithSmallStep() {
 double[] initialState = {1.0, 1.0};
 double time = 2.0e-12;
 integrator.integrate(equations, initialState, time);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrationWithMismatchedDimensions() {
 ExpandableStatefulODE equationsMismatched = new DummyODE(3);
 integrator.integrate(equationsMismatched, new double[]{1.0, 1.0, 1.0}, 2.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrationWithMaxCountExceeded() {
 ExpandableStatefulODE equationsMaxCountExceeded = new DummyODE();
 integrator.setMaxSteps(1);
 integrator.integrate(equationsMaxCountExceeded, new double[]{1.0}, 2.0);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrationWithNoBracketing() {
 ExpandableStatefulODE equationsNoBracketing = new DummyODE();
 integrator.setMinStep(Double.POSITIVE_INFINITY);
 integrator.integrate(equationsNoBracketing, new double[]{1.0}, 0.0);
 }
}