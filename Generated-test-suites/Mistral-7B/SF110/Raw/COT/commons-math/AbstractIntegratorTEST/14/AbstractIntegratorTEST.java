package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.events.Event;
import org.apache.commons.math3.ode.stephandlers.StepHandler;
import org.apache.commons.math3.ode.stephandlers.PrintStepHandler;
import org.apache.commons.math3.ode.stephandlers.StopIntegrationAtTimeStepHandler;
import org.apache.commons.math3.ode.stephandlers.StopIntegrationAtLimitReachedStepHandler;
import org.apache.commons.math3.ode.stephandlers.StepHandlerAdapter;
import org.apache.commons.math3.ode.util.ExpandableStatefulODEs;
import org.junit.Test;
import org.junit.Before;
import java.util.*;
import java.lang.reflect.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;
 private ExpandableStatefulODE expandableODE;
 private double initialTime, initialY[];
 private double finalTime;

 @Before
 public void setUp() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(final ExpandableStatefulODE p0, final double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Override this method for test purposes.
 System.out.println("Integrating from t=" + p0.getTime() + " to t=" + p1);
 try {
 Thread.sleep(10);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 super.integrate(p0, p1);
 }
 };
 equations = new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(final double t, final double[] y, final double[] yDot) {
 yDot[0] = y[0] * 0.01;
 }
 };
 expandableODE = new ExpandableStatefulODE(equations);
 initialTime = 0.0;
 initialY = new double[]{1.0};
 finalTime = 2.0;
 }

 @Test
 public void testConstructor() {
 assertEquals("name", "TestIntegrator", integrator.getName());
 assertEquals("size of stepHandlers", 0, integrator.getStepHandlers().size());
 assertEquals("size of eventHandlers", 0, integrator.getEventHandlers().size());
 assertEquals("maxEvaluations", -1, integrator.getMaxEvaluations());
 assertEquals("evaluations", 0, integrator.getEvaluations());
 }

 @Test
 public void testAddStepHandler() {
 integrator.addStepHandler(new PrintStepHandler());
 assertEquals("size of stepHandlers", 1, integrator.getStepHandlers().size());
 integrator.removeStepHandler(integrator.getStepHandlers().iterator().next());
 assertEquals("size of stepHandlers", 0, integrator.getStepHandlers().size());
 }

 @Test
 public void testAddEventHandler() {
 integrator.addEventHandler(new When(1.5, new Action() {
 public void execute(final double t, final double[] y) {
 System.out.println("Event triggered at t=" + t + ", y=" + Arrays.toString(y));
 }
 }), 0.1, 1.0e-5, 10);
 integrator.addEventHandler(new When(2.0, new Action() {
 public void execute(final double t, final double[] y) {
 System.out.println("Final event triggered at t=" + t + ", y=" + Arrays.toString(y));
 }
 }), 0.1, 1.0e-5, 10);
 assertEquals("size of eventHandlers", 2, integrator.getEventHandlers().size());
 integrator.clearEventHandlers();
 assertEquals("size of eventHandlers", 0, integrator.getEventHandlers().size());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateDimensionMismatch() {
 integrator.integrate(expandableODE, initialTime, initialY, finalTime, new double[2]);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateNumberIsTooSmall() {
 integrator.setMaxEvaluations(0);
 integrator.integrate(expandableODE, initialTime, initialY, finalTime, new double[1]);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateMaxCountExceeded() {
 integrator.setMaxEvaluations(1);
 integrator.integrate(expandableODE, initialTime, initialY, finalTime, new double[1]);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateNoBracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException {
 integrator.setExpandable(expandableODE);
 integrator.initIntegration(initialTime, initialY, finalTime);
 Method method = AbstractIntegrator.class.getDeclaredMethod("acceptStep", AbstractStepInterpolator.class, double[].class, double.class, double[].class);
 method.setAccessible(true);
 try {
 method.invoke(integrator, new AbstractStepInterpolator() {}, initialY, initialTime, new double[1]);
 fail("acceptStep should have thrown NoBracketingException");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.setExpandable(expandableODE);
 integrator.initIntegration(initialTime, initialY, finalTime);
 integrator.integrate(expandableODE, finalTime);
 double[] y = new double[1];
 integrator.getEvaluationsCounter().getValue(y);
 assertEquals("evaluations", 2, y[0]);
 }

 @Test(expected = IllegalStateException.class)
 public void testIntegrateMultipleInitializations() {
 integrator.initIntegration(initialTime, initialY, finalTime);
 integrator.integrate(expandableODE, finalTime);
 integrator.initIntegration(initialTime, initialY, finalTime);
 integrator.integrate(expandableODE, finalTime);
 }

 @Test
 public void testResetOccurred() {
 integrator.addEventHandler(new When(1.0, new StopIntegrationAtLimitReachedStepHandler(0.5)), 0.1, 1.0e-5, 10);
 integrator.addEventHandler(new When(1.5, new PrintStepHandler()), 0.1, 1.0e-5, 10);
 integrator.addEventHandler(new When(2.0, new StopIntegrationAtTimeStepHandler(finalTime)), 0.1, 1.0e-5, 10);
 integrator.integrate(expandableODE, initialTime, initialY, finalTime);
 assertTrue("resetOccurred should be true", integrator.isResetOccurred());
 integrator.reset();
 assertFalse("resetOccurred should be false", integrator.isResetOccurred());
 }

 @Test
 public void testSanityChecks() {
 integrator.sanityChecks(expandableODE, finalTime);
 }
}