package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.EventState;
import org.apache.commons.math3.ode.EventHandler;
import org.apache.commons.math3.ode.ExpandableState;
import org.apache.commons.math3.ode.AbstractStepInterpolator;
import org.apache.commons.math3.ode.events.WaypointEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.*;

@RunWith(JUnit4.class)
public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;
 private double initialTime;
 private double[] initialState;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 integrator = new AbstractIntegrator() {
 @Override
 protected void integrate(ExpandableStatefulODE equations, double time)
 throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 super.integrate(equations, time);
 // Empty implementation for testing
 }
 };
 integrator.setName("TestIntegrator");
 equations = new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double time, double[] state, double[] derivatives) {
 derivatives[0] = state[0] * 0.1;
 }
 };
 initialTime = 0.0;
 initialState = new double[]{1.0};
 }

 @After
 public void tearDown() {
 integrator = null;
 equations = null;
 initialState = null;
 initialTime = 0.0;
 }

 @Test
 public void testAddStepHandlers() {
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(StepContext context, boolean last) {
 assertEquals("Last Step?", last, context.isLastStep());
 }
 });
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(StepContext context, boolean last) {
 fail("Should not reach this step handler");
 }
 });
 StepContext context = new StepContext(1.0, new double[]{2.0}, new double[]{1.5}, 2.0, integrator);
 integrator.acceptStep(new AbstractStepInterpolator() {}, initialState, null, initialTime + 1.0);
 integrator.acceptStep(new AbstractStepInterpolator() {}, initialState, null, initialTime + 1.0);
 }

 @Test
 public void testAddEventHandler() {
 WaypointEvent event = new WaypointEvent(1.5, new double[]{2.0});
 integrator.addEventHandler(event, 0.05, 1.0e-5, 10);
 integrator.addEventHandler(event, 0.05, 1.0e-5, 10, new BracketingNthOrderBrentSolver(1.0e-5, 5));

 assertEquals(2, integrator.getEventHandlers().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEventHandlerWithNullEventHandler() {
 integrator.addEventHandler(null, 0.05, 1.0e-5, 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEventHandlerWithNegativeMaxIterationCount() {
 integrator.addEventHandler(event, 0.05, 1.0e-5, -5);
 }

 @Test(expected = IllegalStateException.class)
 public void testIntegrateWithNoEventHandler() {
 try {
 integrator.integrate(equations, initialTime, initialState, 2.0, new double[1]);
 fail("Should throw exception");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 double expectedTime = 2.0;
 double[] expectedState = new double[]{2.61}; // Approximate solution using numerical integration
 integrator.integrate(equations, initialTime, initialState, expectedTime, new double[1]);
 assertEquals("Expected time not reached", expectedTime, integrator.getCurrentStepStart(), 1e-7);
 assertArrayEquals("Expected state not reached", expectedState, integrator.getPrimaryState(), 1e-7);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegrateWithNullEquations() {
 integrator.integrate(null, initialTime, initialState, 2.0, new double[1]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIntegrateWithWrongNumberOfStates() {
 integrator.integrate(equations, initialTime, initialState, 2.0, new double[2]);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithDifferentNumberOfStates() {
 integrator.integrator = new AbstractIntegrator() {
 @Override
 protected void integrate(ExpandableStatefulODE equations, double time)
 throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 super.integrate(equations, time);
 // Empty implementation for testing
 }
 };
 integrator.integrate(equations, initialTime, new double[]{1.0, 2.0}, 2.0, new double[]{1.0});
 }

 @Test
 public void testGetEventHandlers() {
 List<EventHandler> handlers = integrator.getEventHandlers();
 assertEquals("Unexpected number of event handlers", 0, handlers.size());
 integrator.addEventHandler(new WaypointEvent(1.5, new double[]{2.0}), 0.05, 1.0e-5, 10);
 handlers = integrator.getEventHandlers();
 assertEquals("Unexpected number of event handlers", 1, handlers.size());
 }

 @Test
 public void testClearEventHandlers() {
 integrator.addEventHandler(new WaypointEvent(1.5, new double[]{2.0}), 0.05, 1.0e-5, 10);
 integrator.clearEventHandlers();
 List<EventHandler> handlers = integrator.getEventHandlers();
 assertEquals("Unexpected number of event handlers", 0, handlers.size());
 }

 @Test
 public void testGetEvaluations() {
 assertEquals("Unexpected number of evaluations", 0, integrator.getEvaluations());
 integrator.integrate(equations, initialTime, initialState, 2.0, new double[1]);
 assertEquals("Unexpected number of evaluations", 2, integrator.getEvaluations());
 }

 @Test
 public void testSetMaxEvaluations() {
 integrator.setMaxEvaluations(10);
 assertEquals("Unexpected maximum evaluations", 10, integrator.getMaxEvaluations());
 integrator.setMaxEvaluations(-1);
 assertEquals("Unexpected maximum evaluations", Integer.MAX_VALUE, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetName() {
 assertEquals("Unexpected integrator name", "TestIntegrator", integrator.getName());
 }

 @Test
 public void testAcceptStep() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 double expectedTime = 1.1;
 StepContext context = new StepContext(1.0, initialState, null, 1.2, integrator);
 integrator.acceptStep(context, false);
 assertArrayEquals("Expected state not reached", initialState, context.getPrimaryState(), 1e-7);
 assertEquals("Unexpected time not reached", expectedTime, context.getGlobalCurrentTime(), 1e-7);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAcceptStepWithNullContext() {
 integrator.acceptStep(null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAcceptStepWithWrongNumberOfStates() {
 StepContext context = new StepContext(1.0, initialState, null, 1.2, integrator);
 integrator.acceptStep(context, false);
 integrator.acceptStep(new StepContext(1.0, new double[]{1.0, 2.0}, null, 1.2, integrator), false);
 }

 @Test(expected = IllegalStateException.class)
 public void testAcceptStepWithLastStepSet() {
 integrator.isLastStep = true;
 integrator.acceptStep(new StepContext(1.0, initialState, null, 1.2, integrator), false);
 }

 @Test(expected = IllegalStateException.class)
 public void testAcceptStepWithEvaluationsCounterExceeded() {
 integrator.setMaxEvaluations(1);
 integrator.integrate(equations, initialTime, initialState, 2.0, new double[1]);
 integrator.acceptStep(new StepContext(1.0, initialState, null, 1.2, integrator), false);
 }

 @Test
 public void testAcceptStepWithEventHandlers() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 WaypointEvent event = new WaypointEvent(1.5, new double[]{2.0});
 integrator.addEventHandler(event, 0.05, 1.0e-5, 10);
 integrator.setExpandable(new ExpandableStatefulODE(equations));
 integrator.setEvaluationsCounter(new Incrementor());
 integrator.acceptStep(new StepContext(0.0, initialState, null, 2.0, integrator), false);
 assertEquals("Unexpected number of event handlers", 0, integrator.getEventHandlers().size());
 integrator.acceptStep(new StepContext(0.5, initialState, null, 1.5, integrator), false);
 assertEquals("Unexpected number of event handlers", 1, integrator.getEventHandlers().size());
 integrator.acceptStep(new StepContext(1.0, initialState, null, 1.0, integrator), false);
 assertEquals("Unexpected number of event handlers", 1, integrator.getEventHandlers().size());
 integrator.acceptStep(new StepContext(1.5, initialState, null, 0.5, integrator), false);
 assertEquals("Unexpected number of event handlers", 0, integrator.getEventHandlers().size());
 }

 @Test
 public void testResetOccurred() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 WaypointEvent event = new WaypointEvent(1.5, new double[]{2.0});
 integrator.addEventHandler(event, 0.05, 1.0e-5, 10);
 integrator.integrate(equations, initialTime, initialState, 2.0, new double[1]);
 boolean resetOccurred = integrator.isResetOccurred();
 assertTrue("Reset not occurred", resetOccurred);
 integrator.reset();
 resetOccurred = integrator.isResetOccurred();
 assertFalse("Reset still occurred", resetOccurred);
 }

}