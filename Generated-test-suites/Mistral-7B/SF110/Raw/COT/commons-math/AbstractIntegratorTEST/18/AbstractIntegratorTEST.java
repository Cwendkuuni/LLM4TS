package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.event.Event;
import org.apache.commons.math3.ode.event.EventState;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.util.*;
import org.apache.commons.math3.analysis.solvers.*;
import java.util.*;
import java.lang.reflect.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;
 private ExpandableStatefulODE expandableODE;
 private StepInterpolator interpolator;
 private AbstractStepInterpolator stepInterpolator;
 private StepHandler stepHandler;
 private EventHandler eventHandler;
 private Event event;
 private List<EventState> eventStates;

 @Before
 public void setUp() throws NoBracketingException, MaxCountExceededException, NumberIsTooSmallException, DimensionMismatchException, InstantiationException, IllegalAccessException, InvocationTargetException {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // empty implementation
 }
 };
 equations = new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 yDot[0] = y[0] * 2;
 }
 };
 expandableODE = new ExpandableStatefulODE(equations);
 interpolator = new StepInterpolator(new RungeKutta78Method(), 1.0e-12, 1.0e-12);
 stepInterpolator = new RungeKutta78Interpolator(interpolator);
 eventHandler = new EventHandler() {
 @Override
 public void init(double t0, double[] y0, double t) {
 // empty implementation
 }

 @Override
 public boolean evaluateStep(AbstractStepInterpolator interpolator) {
 return false;
 }

 @Override
 public void stepAccepted(double t, double[] y) {
 // empty implementation
 }

 @Override
 public void reset(double t, double[] y) {
 // empty implementation
 }

 @Override
 public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
 // empty implementation
 }
 };
 event = new Event() {
 @Override
 public double getEventTime() {
 return 1.0;
 }

 @Override
 public double[] getValues(double time, double[] state) {
 return new double[]{1.0};
 }
 };
 eventStates = new ArrayList<>();
 }

 @After
 public void tearDown() {
 integrator = null;
 equations = null;
 expandableODE = null;
 interpolator = null;
 stepInterpolator = null;
 stepHandler = null;
 eventHandler = null;
 event = null;
 eventStates.clear();
 }

 @Test
 public void testConstructor() {
 AbstractIntegrator actual = new AbstractIntegrator("TestIntegrator");
 assertEquals("TestIntegrator", actual.getName());
 }

 @Test
 public void testAddStepHandler() {
 integrator.addStepHandler(stepHandler);
 assertTrue(integrator.getStepHandlers().contains(stepHandler));
 }

 @Test
 public void testGetStepHandlers() {
 List<StepHandler> expected = Arrays.asList(stepHandler);
 assertEquals(expected, integrator.getStepHandlers());
 }

 @Test
 public void testClearStepHandlers() {
 integrator.clearStepHandlers();
 assertTrue(integrator.getStepHandlers().isEmpty());
 }

 @Test
 public void testAddEventHandler() {
 integrator.addEventHandler(eventHandler, 0.0, 0.0, 1);
 assertTrue(integrator.getEventHandlers().contains(eventHandler));
 }

 @Test
 public void testGetEventHandlers() {
 List<EventHandler> expected = Arrays.asList(eventHandler);
 assertEquals(expected, integrator.getEventHandlers());
 }

 @Test
 public void testClearEventHandlers() {
 integrator.clearEventHandlers();
 assertTrue(integrator.getEventHandlers().isEmpty());
 }

 @Test
 public void testGetCurrentStepStart() {
 assertNaN(integrator.getCurrentStepStart());
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 assertNaN(integrator.getCurrentSignedStepsize());
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
 integrator.evaluateTest(); // to initialize integrator
 assertEquals(0, integrator.getEvaluations());
 }

 @Test
 public void testInitIntegration() {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 assertTrue(integrator.getExpandable() != null);
 }

 @Test
 public void testSetEquations() {
 integrator.setEquations(expandableODE);
 assertEquals(expandableODE, integrator.getExpandable());
 }

 @Test
 public void testGetExpandable() {
 assertEquals(expandableODE, integrator.getExpandable());
 }

 @Test
 public void testGetEvaluationsCounter() {
 assertEquals(integrator.getEvaluationsCounter(), integrator.getEvaluationsCounter());
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.setEquations(expandableODE);
 double t = integrator.integrate(expandableODE, 0.0);
 assertEquals(1.0, t, 0.001);
 }

 @Test
 public void testIntegrateWithCustomStepSize() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.setEquations(expandableODE);
 double t = integrator.integrate(expandableODE, 0.0, 0.2);
 assertEquals(0.2, t, 0.001);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithIncorrectDimensions() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.setEquations(expandableODE);
 expandableODE.setPrimaryState(new double[]{0.0, 0.0});
 integrator.integrate(expandableODE, 0.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxEvaluationsReached() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.setEquations(expandableODE);
 integrator.setMaxEvaluations(1);
 integrator.integrate(expandableODE, 0.0);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithRootBracketingFailed() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.setEquations(new ExpandableStatefulODE(new BracketingRootFinderTestEquations()) {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 yDot[0] = -y[0];
 }
 });
 integrator.setMaxEvaluations(1);
 integrator.integrate(expandableODE, 0.0);
 }

 @Test
 public void testComputeDerivatives() throws MaxCountExceededException, DimensionMismatchException {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.setEquations(expandableODE);
 double[] y = new double[]{0.0};
 double[] yDot = new double[1];
 integrator.computeDerivatives(1.0, y, yDot);
 assertEquals(0.0, yDot[0], 0.001);
 }

 @Test
 public void testAcceptStep() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.setEquations(expandableODE);
 integrator.addEventHandler(eventHandler, 0.0, 0.0, 1);
 integrator.setMaxEvaluations(1);
 interpolator.setInterpolatedTime(0.0);
 double t = integrator.acceptStep(stepInterpolator, y, 1.0);
 assertEquals(1.0, t, 0.001);
 }

 @Test
 public void testAcceptStepWithEventReached() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.setEquations(expandableODE);
 integrator.addEventHandler(eventHandler, 0.0, 0.0, 1);
 integrator.addEventHandler(new EventHandler() {
 @Override
 public void init(double t0, double[] y0, double t) {
 // empty implementation
 }

 @Override
 public boolean evaluateStep(AbstractStepInterpolator interpolator) {
 return false;
 }

 @Override
 public void stepAccepted(double t, double[] y) {
 // empty implementation
 }

 @Override
 public void reset(double t, double[] y) {
 // empty implementation
 }

 @Override
 public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
 // empty implementation
 }
 }, 0.5, 0.0, 1);
 interpolator.setInterpolatedTime(0.0);
 integrator.setStateInitialized(false);
 double[] y0 = new double[]{0.0};
 double[] yDot = new double[1];
 double tEnd = 1.5;
 try {
 t = integrator.acceptStep(stepInterpolator, y0, tEnd);
 fail("An exception should have been thrown.");
 } catch (NoBracketingException e) {
 // expected
 }
 assertEquals(1.0, integrator.getEventHandlers().get(0).getEventHandler().getLastT(), 0.001);
 }

 @Test
 public void testAcceptStepWithResetOccured() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.setEquations(expandableODE);
 integrator.addEventHandler(eventHandler, 0.0, 0.0, 1);
 integrator.addEventHandler(new EventHandler() {
 @Override
 public void init(double t0, double[] y0, double t) {
 // empty implementation
 }

 @Override
 public boolean evaluateStep(AbstractStepInterpolator interpolator) {
 return false;
 }

 @Override
 public void stepAccepted(double t, double[] y) {
 // empty implementation
 }

 @Override
 public void reset(double t, double[] y) {
 // empty implementation
 }

 @Override
 public void handleStep(AbstractStepInterpolator interpolator, boolean isLastStep) {
 // empty implementation
 }
 }, 0.5, 0.0, 1);
 integrator.setStateInitialized(false);
 interpolator.setInterpolatedTime(0.0);
 integrator.resetOccurred = false;
 double[] y1 = new double[]{0.0};
 double[] yDot1 = new double[1];
 double tEnd1 = 1.5;
 try {
 t = integrator.acceptStep(stepInterpolator, y1, tEnd1);
 fail("An exception should have been thrown.");
 } catch (NoBracketingException e) {
 // expected
 }
 assertTrue(integrator.resetOccurred);
 }
}