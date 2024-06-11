package org.apache.commons.math3.ode.tests;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.util.ExpandableState;
import org.apache.commons.math3.ode.util.EquationsMapper;
import org.apache.commons.math3.ode.util.ExpandableStatefulODE.ExpandableStatefulODEBuilder;
import org.apache.commons.math3.ode.util.StepInterpolator;
import org.apache.commons.math3.util.*;
import java.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;
 private DoubleStepInterpolator interpolator;
 private ExpandableStatefulODE expandableODE;
 private EventState eventState;
 private EventHandler eventHandler;
 private StepHandler stepHandler;

 @Before
 public void setUp() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Empty implementation
 }
 };
 equations = new MockDifferentialEquations(1);
 interpolator = new DoubleStepInterpolator();
 eventState = new EventState(new MockEventHandler(), 0.0, 0.01, 5, new BracketingNthOrderBrentSolver(0.01, 5));
 eventHandler = new MockEventHandler();
 stepHandler = new MockStepHandler();
 integrator.addEventHandler(eventHandler, 0.0, 0.01, 5, new BracketingNthOrderBrentSolver(0.01, 5));
 integrator.addStepHandler(stepHandler);
 }

 @Test
 public void testConstructor() {
 assertEquals("TestIntegrator", integrator.getName());
 assertNotNull(integrator.getStepHandlers());
 assertFalse(integrator.isLastStep());
 assertFalse(integrator.resetOccurred());
 assertEquals(0, integrator.getEvaluations());
 assertNull(integrator.getExpandable());
 }

 @Test
 public void testAddStepHandler() {
 integrator.addStepHandler(stepHandler);
 assertTrue(integrator.getStepHandlers().contains(stepHandler));
 }

 @Test
 public void testGetStepHandlers() {
 Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
 assertEquals(1, stepHandlers.size());
 assertTrue(stepHandlers.contains(stepHandler));
 }

 @Test
 public void testClearStepHandlers() {
 integrator.clearStepHandlers();
 assertTrue(integrator.getStepHandlers().isEmpty());
 }

 @Test
 public void testAddEventHandler() {
 integrator.addEventHandler(eventHandler, 0.0, 0.01, 5, new BracketingNthOrderBrentSolver(0.01, 5));
 assertTrue(integrator.getEventHandlers().contains(eventHandler));
 }

 @Test
 public void testGetEventHandlers() {
 Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
 assertEquals(1, eventHandlers.size());
 assertTrue(eventHandlers.contains(eventHandler));
 }

 @Test
 public void testClearEventHandlers() {
 integrator.clearEventHandlers();
 assertTrue(integrator.getEventHandlers().isEmpty());
 }

 @Test
 public void testGetCurrentStepStart() {
 integrator.initIntegration(0.0, new double[1], 1.0);
 assertEquals(0.0, integrator.getCurrentStepStart(), 1e-12);
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 integrator.initIntegration(0.0, new double[1], 1.0);
 assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 1e-12);
 }

 @Test
 public void testSetMaxEvaluations() {
 integrator.setMaxEvaluations(10);
 assertEquals(10, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetMaxEvaluations() {
 assertEquals(-1, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 integrator.computeDerivatives(0.0, new double[1], new double[1]);
 assertEquals(1, integrator.getEvaluations());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithInvalidPrimaryStateLength() throws Exception {
 integrator.integrate(expandableODE, 1.0);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithInvalidSecondaryStateLength() throws Exception {
 ExpandableStatefulODEBuilder builder = new ExpandableStatefulODEBuilder();
 ExpandableStatefulODE expandableODE = builder.setDimension(1).setPrimaryMapper(new IdentityMapper()).setSecondaryMappers(Collections.singletonList(new IdentityMapper())).build();
 integrator.setExpandable(expandableODE);
 integrator.integrate(expandableODE, 1.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxEvaluationsReached() throws Exception {
 integrator.setMaxEvaluations(1);
 integrator.integrate(expandableODE, 1.0);
 integrator.computeDerivatives(0.0, new double[1], new double[1]); // Trigger the first evaluation
 integrator.integrate(expandableODE, 1.0); // Trigger the second evaluation, causing MaxCountExceededException
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithNoBracketing() throws Exception {
 integrator.setMaxEvaluations(1000);
 integrator.integrate(expandableODE, 0.0);
 }

 @Test
 public void testIntegrate() throws Exception {
 integrator.setMaxEvaluations(100);
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(expandableODE);
 integrator.integrate(expandableODE, 1.0);
 // Verify that the integration was successful and the state was updated
 double[] expectedState = {0.718281828};
 assertArrayEquals(expectedState, expandableODE.getPrimaryState(), 1e-12);
 assertEquals(1.0, expandableODE.getTime(), 1e-12);
 }

 @Test
 public void testComputeDerivatives() throws Exception {
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(expandableODE);
 integrator.computeDerivatives(0.0, new double[1], new double[1]);
 // Verify that the derivatives were computed correctly
 double[] expectedDerivatives = {1.0};
 assertArrayEquals(expectedDerivatives, expandableODE.getPrimaryDerivative(), 1e-12);
 }

 @Test
 public void testAcceptStep() throws Exception {
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(expandableODE);
 integrator.addEventHandler(eventHandler, 0.0, 0.01, 5, new BracketingNthOrderBrentSolver(0.01, 5));
 integrator.acceptStep(interpolator, new double[1], new double[1], 0.5);
 // Verify that the event was detected and handled
 assertTrue(eventHandler.isEventDetected());
 // Verify that the step was accepted and the state was updated
 double[] expectedState = {0.367879441};
 assertArrayEquals(expectedState, expandableODE.getPrimaryState(), 1e-12);
 assertEquals(0.5, expandableODE.getTime(), 1e-12);
 }

 @Test
 public void testSanityChecks() throws Exception {
 ExpandableStatefulODE expandableODE = new ExpandableStatefulODE(equations);
 integrator.sanityChecks(expandableODE, 0.0);
 }

}

class MockDifferentialEquations implements FirstOrderDifferentialEquations {
 private static final double EPSILON = 1e-15;

 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public double[] evaluateDerivative(double t, double[] y) {
 return new double[] {y[0] * 2};
 }
}

class MockEventHandler implements EventHandler {
 private boolean eventDetected = false;

 @Override
 public void init(double t, double[] y, double tStep) {
 // Empty implementation
 }

 @Override
 public boolean evaluateStep(AbstractStepInterpolator interpolator) {
 eventDetected = true;
 return false;
 }

 public boolean isEventDetected() {
 return eventDetected;
 }
}

class MockStepHandler implements StepHandler {
 @Override
 public void init(double t, double[] y, double tStep) {
 // Empty implementation
 }

 @Override
 public void handleStep(AbstractStepInterpolator interpolator, boolean lastStep) {
 // Empty implementation
 }
}