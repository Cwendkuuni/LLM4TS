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
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.junit.Test;
import java.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;
 private double[] initialState;

 @Before
 public void setUp() {
 integrator = new MyAbstractIntegrator("MyIntegrator");
 equations = new MyDifferentialEquations();
 initialState = new double[]{1.0, 2.0};
 }

 @Test
 public void testConstructor() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 assertNotNull(testIntegrator);
 testIntegrator = new AbstractIntegrator("TestIntegrator");
 assertNotNull(testIntegrator);
 }

 @Test
 public void testSetName() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 assertEquals("", testIntegrator.getName());
 testIntegrator.setName("TestIntegrator");
 assertEquals("TestIntegrator", testIntegrator.getName());
 }

 @Test
 public void testAddStepHandler() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 StepHandler handler = new MyStepHandler();
 testIntegrator.addStepHandler(handler);
 assertEquals(1, testIntegrator.getStepHandlers().size());
 }

 @Test
 public void testGetStepHandlers() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 StepHandler handler = new MyStepHandler();
 testIntegrator.addStepHandler(handler);
 List<StepHandler> handlers = testIntegrator.getStepHandlers();
 assertEquals(handler, handlers.get(0));
 }

 @Test
 public void testClearStepHandlers() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 StepHandler handler = new MyStepHandler();
 testIntegrator.addStepHandler(handler);
 testIntegrator.clearStepHandlers();
 assertEquals(0, testIntegrator.getStepHandlers().size());
 }

 @Test
 public void testAddEventHandler() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 EventHandler handler = new MyEventHandler();
 testIntegrator.addEventHandler(handler, 0.0, 0.01, 10);
 assertEquals(1, testIntegrator.getEventHandlers().size());
 }

 @Test
 public void testGetEventHandlers() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 EventHandler handler = new MyEventHandler();
 testIntegrator.addEventHandler(handler, 0.0, 0.01, 10);
 List<EventHandler> handlers = testIntegrator.getEventHandlers();
 assertEquals(handler, handlers.get(0));
 }

 @Test
 public void testClearEventHandlers() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 EventHandler handler = new MyEventHandler();
 testIntegrator.addEventHandler(handler, 0.0, 0.01, 10);
 testIntegrator.clearEventHandlers();
 assertEquals(0, testIntegrator.getEventHandlers().size());
 }

 @Test
 public void testGetCurrentStepStart() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 testIntegrator.initIntegration(0.0, initialState, 1.0);
 assertNaN(testIntegrator.getCurrentStepStart());
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 testIntegrator.initIntegration(0.0, initialState, 1.0);
 assertNaN(testIntegrator.getCurrentSignedStepsize());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyStepHandlers() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 StepHandler handler = new MyStepHandler();
 testIntegrator.addStepHandler(handler);
 testIntegrator.getStepHandlers().set(0, new MyStepHandler2());
 }

 @Test
 public void testIntegrate() throws Exception {
 ExpandableStatefulODE ode = new ExpandableStatefulODE(equations);
 ode.setTime(0.0);
 ode.setPrimaryState(initialState);
 double result = integrator.integrate(ode, 1.0);
 assertNotEquals(0.0, result);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithInvalidDimension() throws Exception {
 ExpandableStatefulODE ode = new ExpandableStatefulODE(new MyDifferentialEquations2());
 ode.setTime(0.0);
 ode.setPrimaryState(initialState);
 integrator.integrate(ode, 1.0);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateWithSmallStepSize() throws Exception {
 ExpandableStatefulODE ode = new ExpandableStatefulODE(equations);
 ode.setTime(0.0);
 ode.setPrimaryState(initialState);
 integrator.setStepSize(1.0e-10);
 integrator.integrate(ode, 1.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxEvaluationsReached() throws Exception {
 integrator.setMaxEvaluations(0);
 ExpandableStatefulODE ode = new ExpandableStatefulODE(equations);
 ode.setTime(0.0);
 ode.setPrimaryState(initialState);
 integrator.integrate(ode, 1.0);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithNoBracketing() throws Exception {
 integrator.setMaxEvaluations(1);
 ExpandableStatefulODE ode = new ExpandableStatefulODE(new MyDifferentialEquationsNoBracketing());
 ode.setTime(0.0);
 ode.setPrimaryState(initialState);
 integrator.integrate(ode, 1.0);
 }

 @Test
 public void testInitIntegration() {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 testIntegrator.initIntegration(0.0, initialState, 1.0);
 assertFalse(testIntegrator.getStatesInitialized());
 }

 @Test
 public void testAcceptStep() throws Exception {
 AbstractStepInterpolator interpolator = new MyStepInterpolator();
 integrator.initIntegration(0.0, initialState, 1.0);
 integrator.setExpandable(equations);
 integrator.acceptStep(interpolator, initialState, new double[equations.getDimension()], 1.0);
 assertTrue(integrator.getStatesInitialized());
 }

 @Test
 public void testAcceptStepWithEvent() throws Exception {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 AbstractEvent event = new MyEvent();
 testIntegrator.addEventHandler(event, 0.0, 0.01, 10);
 testIntegrator.initIntegration(0.0, initialState, 1.0);
 testIntegrator.setExpandable(equations);
 double result = testIntegrator.acceptStep(new MyStepInterpolator(), initialState, new double[equations.getDimension()], 1.0);
 assertEquals(1.0, result, 1e-9);
 }

 @Test
 public void testAcceptStepWithMultipleEvents() throws Exception {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 AbstractEvent event1 = new MyEvent1();
 AbstractEvent event2 = new MyEvent2();
 testIntegrator.addEventHandler(event1, 0.0, 0.01, 10);
 testIntegrator.addEventHandler(event2, 0.5, 0.01, 10);
 testIntegrator.initIntegration(0.0, initialState, 1.0);
 testIntegrator.setExpandable(equations);
 double result = testIntegrator.acceptStep(new MyStepInterpolator(), initialState, new double[equations.getDimension()], 1.0);
 assertEquals(0.5, result, 1e-9);
 }

 @Test
 public void testAcceptStepWithStepHandler() throws Exception {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 StepHandler stepHandler = new MyStepHandler();
 testIntegrator.addStepHandler(stepHandler);
 testIntegrator.initIntegration(0.0, initialState, 1.0);
 testIntegrator.setExpandable(equations);
 testIntegrator.acceptStep(new MyStepInterpolator(), initialState, new double[equations.getDimension()], 1.0);
 assertTrue(stepHandler.isStepHandled());
 }

 @Test
 public void testAcceptStepWithReset() throws Exception {
 AbstractIntegrator testIntegrator = new AbstractIntegrator();
 EventHandler eventHandler = new MyEventHandler();
 testIntegrator.addEventHandler(eventHandler, 0.0, 0.01, 10);
 testIntegrator.initIntegration(0.0, initialState, 1.0);
 testIntegrator.setExpandable(equations);
 testIntegrator.acceptStep(new MyStepInterpolator(), initialState, new double[equations.getDimension()], 1.0);
 assertTrue(testIntegrator.getResetOccurred());
 }

 @Test
 public void testSanityChecks() throws Exception {
 ExpandableStatefulODE ode = new ExpandableStatefulODE(equations);
 ode.setTime(0.0);
 ode.setPrimaryState(initialState);
 integrator.sanityChecks(ode, 1.0);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testSanityChecksWithSmallStepSize() throws Exception {
 ExpandableStatefulODE ode = new ExpandableStatefulODE(equations);
 ode.setTime(0.0);
 ode.setPrimaryState(initialState);
 integrator.sanityChecks(ode, 1.0e-10);
 }

}

 public class MyAbstractIntegrator extends AbstractIntegrator {
 private static final long serialVersionUID = 1L;
 }

 public class MyDifferentialEquations implements FirstOrderDifferentialEquations {
 private static final long serialVersionUID = 1L;

 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void computePrimaryDerivatives(double t, double[] y, double[] yPrime) {
 yPrime[0] = y[1];
 yPrime[1] = -y[0];
 }
 }

 public class MyDifferentialEquations2 implements FirstOrderDifferentialEquations {
 private static final long serialVersionUID = 1L;

 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computePrimaryDerivatives(double t, double[] y, double[] yPrime) {
 // This equation does not have a closed-form solution,
 // so it cannot be tested using the AbstractIntegrator.
 }
 }

 public class MyDifferentialEquationsNoBracketing implements FirstOrderDifferentialEquations {
 private static final long serialVersionUID = 1L;

 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computePrimaryDerivatives(double t, double[] y, double[] yPrime) {
 yPrime[0] = Math.sin(t);
 }
 }

 public class MyStepInterpolator extends AbstractStepInterpolator {
 private static final long serialVersionUID = 1L;

 @Override
 public double getStep(double t, double[] y) {
 return 0.1;
 }
 }

 public class MyEventHandler implements EventHandler {
 private static final long serialVersionUID = 1L;
 private double eventTime;

 public MyEventHandler() {
 eventTime = 0.5;
 }

 @Override
 public void init(double t, double[] y, double tEnd) {
 // Empty implementation
 }

 @Override
 public boolean evaluateStep(AbstractStepInterpolator interpolator) {
 return Precision.greaterOrEqual(interpolator.getInterpolatedTime(), eventTime);
 }

 @Override
 public void stepAccepted(double t, double[] y) {
 // Empty implementation
 }

 @Override
 public void reset(double t, double[] y) {
 // Empty implementation
 }
 }

 public class MyEventHandler2 implements EventHandler {
 private static final long serialVersionUID = 1L;

 @Override
 public void init(double t, double[] y, double tEnd) {
 // Empty implementation
 }

 @Override
 public boolean evaluateStep(AbstractStepInterpolator interpolator) {
 return false;
 }

 @Override
 public void stepAccepted(double t, double[] y) {
 // Empty implementation
 }

 @Override
 public void reset(double t, double[] y) {
 // Empty implementation
 }
 }

 public class MyStepHandler implements StepHandler {
 private static final long serialVersionUID = 1L;
 private boolean stepHandled = false;

 @Override
 public void handleStep(AbstractStepInterpolator interpolator, boolean last) {
 stepHandled = true;
 }

 public boolean isStepHandled() {
 return stepHandled;
 }
 }