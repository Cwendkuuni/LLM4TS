package org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator; // Make sure to include the package name in your test case

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.RungeKuttaStepInterpolator;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.EmbeddedRungeKuttaMethod;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.EmbeddedRungeKuttaIntegratorImpl;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.EmbeddedRungeKuttaIntegratorImpl123;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator.EmbeddedRungeKuttaIntegratorImpl45;

@RunWith(Parameterized.class)
public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private ExpandableStatefulODE equations;
 private double initialTime;
 private double[] initialState;
 private double finalTime;

 @Parameters
 public static Object[][] data() {
 return new Object[][] {
 // Test case 1: EmbeddedRungeKuttaMethod.RK23
 new Object[] { new EmbeddedRungeKuttaIntegratorImpl123(), new SimpleValueODE("y' = 2 * y", 1.0, 1.0), 1.0, 1.0, 2.0 },
 // Test case 2: EmbeddedRungeKuttaMethod.RK45
 new Object[] { new EmbeddedRungeKuttaIntegratorImpl45(), new SimpleValueODE("y' = x * y", 0.0, 1.0), 0.0, 1.0, 2.0 }
 };
 }

 public EmbeddedRungeKuttaIntegratorTest(EmbeddedRungeKuttaIntegrator integrator, ExpandableStatefulODE equations, double initialTime, double[] initialState, double finalTime) {
 this.integrator = integrator;
 this.equations = equations;
 this.initialTime = initialTime;
 this.initialState = initialState;
 this.finalTime = finalTime;
 }

 @Test
 public void testIntegration() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Test the integration method of the integrator
 integrator.integrate(equations, initialTime, initialState);
 double[] resultState = equations.getCompleteState();
 double resultTime = equations.getTime();

 // Check if the integration was successful
 assertEquals(finalTime, resultTime, 0.001);
 assertArrayEquals(new double[]{2.0}, resultState, 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeStepSize() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Test if the integrator throws an IllegalArgumentException with a negative step size
 integrator.setStepSize(-1.0);
 integrator.integrate(equations, initialTime, initialState);
 }

 @Test
 public void testGetOrder() {
 // Test the getOrder() method of the integrator
 assertEquals(2, integrator.getOrder());
 }

 @Test
 public void testGetMinReduction() {
 // Test the getMinReduction() method of the integrator
 assertEquals(0.2, integrator.getMinReduction(), 0.01);
 }

 @Test
 public void testSetMinReduction() {
 // Test the setMinReduction() method of the integrator
 integrator.setMinReduction(0.3);
 assertEquals(0.3, integrator.getMinReduction(), 0.01);
 }

 @Test
 public void testGetMaxGrowth() {
 // Test the getMaxGrowth() method of the integrator
 assertEquals(10.0, integrator.getMaxGrowth(), 0.01);
 }

 @Test
 public void testSetMaxGrowth() {
 // Test the setMaxGrowth() method of the integrator
 integrator.setMaxGrowth(15.0);
 assertEquals(15.0, integrator.getMaxGrowth(), 0.01);
 }

 @Test(expected = IllegalStateException.class)
 public void testBeforeIntegrateCallToResetInternalState() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Test if the integrator throws an IllegalStateException when the internal state is not reset before integrate() is called
 integrator.resetInternalState(); // This should be before integrator.integrate()
 integrator.integrate(equations, initialTime, initialState);
 integrator.resetInternalState(); // This should be after integrator.integrate() (illegal state)
 integrator.integrate(equations, initialTime, initialState);
 }

 @Test
 public void testStepStartAndStepSize() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Test the stepStart and stepSize after integration
 double stepStart = integrator.getStepStart();
 double stepSize = integrator.getStepSize();
 assertEquals(initialTime, stepStart, 0.001);
 assertEquals(integrator.getStepSize(), stepSize, 0.01);
 }

 @Test
 public void testAcceptStepAndFilterStep() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Test the acceptStep() and filterStep() methods of the integrator
 RungeKuttaStepInterpolator interpolator = integrator.getInterpolator();
 interpolator.reinitialize(integrator, initialState, null, true, equations.getPrimaryMapper(), equations.getSecondaryMappers());
 double stepStart = equations.getTime();
 double stepSize = integrator.getStepSize();

 double acceptedStep = integrator.acceptStep(interpolator, initialState, null, finalTime);
 assertEquals(finalTime - initialTime, acceptedStep, 0.01);

 double filterFactor = integrator.getFilterFactor();
 double filteredStep = integrator.filterStep(stepSize, true, false);
 assertEquals(filteredStep, stepSize * filterFactor, 0.01);
 }

 @Test(expected = IllegalStateException.class)
 public void testNoLastStep() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Test if the integrator throws an IllegalStateException when there is no last step
 integrator.isLastStep = false;
 integrator.integrate(equations, initialTime, initialState);
 }
}