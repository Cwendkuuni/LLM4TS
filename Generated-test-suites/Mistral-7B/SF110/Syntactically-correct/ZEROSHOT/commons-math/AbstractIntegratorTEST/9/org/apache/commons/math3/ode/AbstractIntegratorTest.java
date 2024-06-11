package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

import java.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 integrator = new MyAbstractIntegrator("MyIntegrator");
 equations = new DummyDifferentialEquations(1);
 }

 @Test
 public void testConstructor() {
 AbstractIntegrator abstractIntegrator = new AbstractIntegrator();
 assertNull(abstractIntegrator.getName());
 assertNotNull(abstractIntegrator.getStepHandlers());
 assertEquals(0, abstractIntegrator.getStepHandlers().size());
 }

 @Test
 public void testConstructorWithName() {
 AbstractIntegrator abstractIntegrator = new AbstractIntegrator("MyIntegrator");
 assertEquals("MyIntegrator", abstractIntegrator.getName());
 }

 @Test
 public void testAddStepHandler() {
 integrator.addStepHandler(new DummyStepHandler());
 assertEquals(1, integrator.getStepHandlers().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullStepHandler() {
 integrator.addStepHandler(null);
 }

 @Test
 public void testGetStepHandlers() {
 Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
 assertEquals(0, stepHandlers.size());
 integrator.addStepHandler(new DummyStepHandler());
 stepHandlers = integrator.getStepHandlers();
 assertEquals(1, stepHandlers.size());
 }

 @Test
 public void testClearStepHandlers() {
 Collection<StepHandler> stepHandlers = integrator.getStepHandlers();
 integrator.clearStepHandlers();
 stepHandlers = integrator.getStepHandlers();
 assertEquals(0, stepHandlers.size());
 }

 @Test
 public void testAddEventHandler() {
 integrator.addEventHandler(new DummyEventHandler(0.1), 0.01, 1e-6, 10);
 assertEquals(1, integrator.getEventHandlers().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullEventHandler() {
 integrator.addEventHandler(null, 0.1, 1e-6, 10, new BracketingNthOrderBrentSolver(1e-6, 5));
 }

 @Test
 public void testGetEventHandlers() {
 Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
 assertEquals(0, eventHandlers.size());
 integrator.addEventHandler(new DummyEventHandler(0.1), 0.01, 1e-6, 10);
 eventHandlers = integrator.getEventHandlers();
 assertEquals(1, eventHandlers.size());
 }

 @Test
 public void testClearEventHandlers() {
 Collection<EventHandler> eventHandlers = integrator.getEventHandlers();
 integrator.clearEventHandlers();
 eventHandlers = integrator.getEventHandlers();
 assertEquals(0, eventHandlers.size());
 }

 @Test
 public void testGetCurrentStepStart() {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 assertNaN(integrator.getCurrentStepStart());
 integrator.acceptStep(new StepInterpolator(0.0, 0.1), new double[]{0.0}, new double[]{0.1}, 0.1);
 assertEquals(0.0, integrator.getCurrentStepStart(), 1e-6);
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 assertNaN(integrator.getCurrentSignedStepsize());
 integrator.acceptStep(new StepInterpolator(0.0, 0.1), new double[]{0.0}, new double[]{0.1}, 0.1);
 assertEquals(0.1, integrator.getCurrentSignedStepsize(), 1e-6);
 }

 @Test
 public void testSetMaxEvaluations() {
 integrator.setMaxEvaluations(10);
 assertEquals(10, integrator.getMaxEvaluations());
 integrator.setMaxEvaluations(-1);
 assertEquals(-1, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetMaxEvaluations() {
 integrator.setMaxEvaluations(-1);
 assertEquals(-1, integrator.getMaxEvaluations());
 integrator.setMaxEvaluations(10);
 assertEquals(10, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 integrator.evaluations.resetCount();
 integrator.acceptStep(new StepInterpolator(0.0, 0.1), new double[]{0.0}, new double[]{0.1}, 0.1);
 assertEquals(1, integrator.getEvaluations());
 }

 @Test
 public void testIntegrate() throws Exception {
 double[] y = new double[equations.getDimension()];
 double t = 1.0;
 integrator.initIntegration(0.0, new double[]{0.0}, t);
 double result = integrator.integrate(equations, 0.0, new double[]{0.0}, t, y);
 assertEquals(t, result, 1e-6);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithWrongYSize() throws Exception {
 double[] y = new double[equations.getDimension() + 1];
 integrator.integrate(equations, 0.0, y, 1.0);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateWithTooSmallStepSize() throws Exception {
 integrator.setStepSize(0.001);
 integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[equations.getDimension()]);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxEvaluationsReached() throws Exception {
 integrator.setMaxEvaluations(5);
 integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[equations.getDimension()]);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithBracketingFailure() throws Exception {
 integrator.setStepSize(0.1);
 integrator.integrate(new DummyDifferentialEquationsWithEvent(0.0, 1.0, 0.5), 0.0, new double[]{0.0}, 1.0, new double[equations.getDimension()]);
 }

 @Test
 public void testAcceptStep() throws Exception {
 double[] y = new double[equations.getDimension()];
 double t = 1.0;
 integrator.initIntegration(0.0, new double[]{0.0}, t);
 integrator.setExpandable(new DummyExpandableODE(equations));
 integrator.acceptStep(new StepInterpolator(0.0, 0.1), new double[]{0.0}, new double[]{0.1}, 0.1);
 assertEquals(0.1, integrator.getCurrentStepStart(), 1e-6);
 assertEquals(0.1, integrator.getCurrentSignedStepsize(), 1e-6);
 assertEquals(1, integrator.getEvaluations());
 }

 @Test
 public void testAcceptStepWithEvent() throws Exception {
 double[] y = new double[equations.getDimension()];
 double t = 1.0;
 integrator.initIntegration(0.0, new double[]{0.0}, t);
 integrator.addEventHandler(new DummyEventHandler(0.5), 0.01, 1e-6, 10);
 integrator.setExpandable(new DummyExpandableODEWithEvent(equations));
 integrator.acceptStep(new StepInterpolator(0.0, 0.1), new double[]{0.0}, new double[]{0.1}, 0.1);
 assertEquals(0.1, integrator.getCurrentStepStart(), 1e-6);
 assertEquals(0.1, integrator.getCurrentSignedStepsize(), 1e-6);
 assertEquals(1, integrator.getEvaluations());
 integrator.acceptStep(new StepInterpolator(0.1, 0.1), new double[]{0.1}, new double[]{0.2}, 0.1);
 assertEquals(0.2, integrator.getCurrentStepStart(), 1e-6);
 assertEquals(0.1, integrator.getCurrentSignedStepsize(), 1e-6);
 assertEquals(2, integrator.getEvaluations());
 }

 @Test
 public void testAcceptStepWithEventStop() throws Exception {
 double[] y = new double[equations.getDimension()];
 double t = 1.0;
 integrator.initIntegration(0.0, new double[]{0.0}, t);
 integrator.addEventHandler(new DummyEventHandlerWithStop(0.5), 0.01, 1e-6, 10);
 integrator.setExpandable(new DummyExpandableODEWithEvent(equations));
 integrator.acceptStep(new StepInterpolator(0.0, 0.1), new double[]{0.0}, new double[]{0.1}, 0.1);
 assertEquals(0.1, integrator.getCurrentStepStart(), 1e-6);
 assertEquals(0.1, integrator.getCurrentSignedStepsize(), 1e-6);
 assertEquals(1, integrator.getEvaluations());
 integrator.acceptStep(new StepInterpolator(0.1, 0.1), new double[]{0.1}, new double[]{0.2}, 0.1);
 assertFalse(integrator.isLastStep());
 integrator.acceptStep(new StepInterpolator(0.2, 0.1), new double[]{0.2}, new double[]{0.3}, 0.1);
 assertTrue(integrator.isLastStep());
 }

 @Test
 public void testResetOccurred() throws Exception {
 double[] y = new double[equations.getDimension()];
 double t = 1.0;
 integrator.initIntegration(0.0, new double[]{0.0}, t);
 integrator.addEventHandler(new DummyEventHandlerWithReset(0.5), 0.01, 1e-6, 10);
 integrator.setExpandable(new DummyExpandableODEWithEvent(equations));
 integrator.acceptStep(new StepInterpolator(0.0, 0.1), new double[]{0.0}, new double[]{0.1}, 0.1);
 assertEquals(0.1, integrator.getCurrentStepStart(), 1e-6);
 assertEquals(0.1, integrator.getCurrentSignedStepsize(), 1e-6);
 assertEquals(1, integrator.getEvaluations());
 integrator.acceptStep(new StepInterpolator(0.1, 0.1), new double[]{0.1}, new double[]{0.2}, 0.1);
 assertEquals(0.2, integrator.getCurrentStepStart(), 1e-6);
 assertEquals(0.1, integrator.getCurrentSignedStepsize(), 1e-6);
 assertEquals(2, integrator.getEvaluations());
 assertTrue(integrator.getResetOccurred());
 }

 @Test
 public void testSanityChecks() throws Exception {
 FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(1);
 AbstractIntegrator integrator = new MyAbstractIntegrator("MyIntegrator");
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.sanityChecks(equations, 1.0);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testSanityChecksWithTooSmallStepSize() throws Exception {
 FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(1);
 AbstractIntegrator integrator = new MyAbstractIntegrator("MyIntegrator");
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.sanityChecks(equations, 0.0001);
 }
}

 class MyAbstractIntegrator extends AbstractIntegrator {
 // Empty implementation
 }

 class DummyStepHandler implements StepHandler {
 // Empty implementation
 }

 class DummyEventHandler implements EventHandler {
 private final double eventTime;

 public DummyEventHandler(double eventTime) {
 this.eventTime = eventTime;
 }

 public boolean evaluateStep(StepInterpolator interpolator) {
 return false;
 }

 public void stepAccepted(double t, double[] yPrimary) {
 // Empty implementation
 }

 public void handleStep(StepInterpolator interpolator, boolean isLastStep) {
 // Empty implementation
 }
 }

 class DummyEventHandlerWithStop implements EventHandler {
 private final double eventTime;

 public DummyEventHandlerWithStop(double eventTime) {
 this.eventTime = eventTime;
 }

 public boolean evaluateStep(StepInterpolator interpolator) {
 return false;
 }

 public void stepAccepted(double t, double[] yPrimary) {
 // Empty implementation
 }

 public boolean stop() {
 return true;
 }
 }

 class DummyEventHandlerWithReset implements EventHandler {
 private final double eventTime;

 public DummyEventHandlerWithReset(double eventTime) {
 this.eventTime = eventTime;
 }

 public boolean evaluateStep(StepInterpolator interpolator) {
 return false;
 }

 public void stepAccepted(double t, double[] yPrimary) {
 // Empty implementation
 }

 public boolean reset(double t, double[] yComplete) {
 return true;
 }
 }

 class DummyDifferentialEquations implements FirstOrderDifferentialEquations {
 private static final int DIMENSION = 1;

 public DummyDifferentialEquations() {
 }

 public int getDimension() {
 return DIMENSION;
 }

 public void computeDerivatives(double t, double[] y, double[] yDot) {
 yDot[0] = 1.0;
 }
 }

 class DummyDifferentialEquationsWithEvent extends DummyDifferentialEquations implements ExpandableStatefulODE {
 // Empty implementation
 }

 class DummyExpandableODE implements ExpandableStatefulODE {
 private final FirstOrderDifferentialEquations equations;

 public DummyExpandableODE(FirstOrderDifferentialEquations equations) {
 this.equations = equations;
 }

 public int getDimension() {
 return equations.getDimension();
 }

 public void setTime(double time) {
 // Empty implementation
 }

 public void setPrimaryState(double[] state) {
 // Empty implementation
 }

 public double getTime() {
 return 0.0;
 }

 public double[] getPrimaryState() {
 return new double[0];
 }

 public void computeDerivatives(double t, double[] y, double[] yDot) {
 equations.computeDerivatives(t, y, yDot);
 }
 }

 class DummyExpandableODEWithEvent extends DummyExpandableODE {
 // Empty implementation
 }