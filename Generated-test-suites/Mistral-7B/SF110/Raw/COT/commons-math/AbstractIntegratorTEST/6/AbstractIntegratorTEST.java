package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.junit.Test;
import java.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 integrator = new MyAbstractIntegrator("MyIntegrator");
 equations = new MyDifferentialEquations();
 }

 @Test
 public void testConstructor() {
 AbstractIntegrator actual = new AbstractIntegrator("AbstractIntegrator");
 assertEquals("AbstractIntegrator", actual.getName());
 }

 @Test
 public void testAddStepHandler() {
 StepHandler handler = new MyStepHandler();
 integrator.addStepHandler(handler);
 assertTrue(integrator.getStepHandlers().contains(handler));
 }

 @Test
 public void testGetStepHandlers() {
 StepHandler handler = new MyStepHandler();
 integrator.addStepHandler(handler);
 List<StepHandler> expected = Arrays.asList(handler);
 assertEquals(expected, integrator.getStepHandlers());
 }

 @Test
 public void testClearStepHandlers() {
 StepHandler handler = new MyStepHandler();
 integrator.addStepHandler(handler);
 integrator.clearStepHandlers();
 assertFalse(integrator.getStepHandlers().isEmpty());
 }

 @Test
 public void testAddEventHandler() {
 EventHandler handler = new MyEventHandler();
 integrator.addEventHandler(handler, 1.0, 1.0e-6, 10);
 assertTrue(integrator.getEventHandlers().contains(handler));
 }

 @Test
 public void testGetEventHandlers() {
 EventHandler handler = new MyEventHandler();
 integrator.addEventHandler(handler, 1.0, 1.0e-6, 10);
 List<EventHandler> expected = Arrays.asList(handler);
 assertEquals(expected, integrator.getEventHandlers());
 }

 @Test
 public void testClearEventHandlers() {
 EventHandler handler = new MyEventHandler();
 integrator.addEventHandler(handler, 1.0, 1.0e-6, 10);
 integrator.clearEventHandlers();
 assertFalse(integrator.getEventHandlers().isEmpty());
 }

 @Test
 public void testGetCurrentStepStart() {
 assertNaN(integrator.getCurrentStepStart());
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 assertNaN(integrator.getCurrentSignedStepsize());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithInvalidDimensions() {
 integrator.integrate(new MyDifferentialEquations(3), 0.0, new double[]{1.0, 2.0, 3.0}, 1.0, new double[]{1.0, 2.0});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithInvalidOutputDimensions() {
 integrator.integrate(new MyDifferentialEquations(2), 0.0, new double[]{1.0, 2.0}, 1.0, new double[]{3.0});
 }

 @Test
 public void testIntegrate() throws Exception {
 double[] initialValues = {1.0, 1.0};
 double time = 2.0;
 double[] expectedOutput = {2.0, 2.0};
 double[] actualOutput = integrator.integrate(equations, 0.0, initialValues, time, new double[2]);
 assertArrayEquals(expectedOutput, actualOutput);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateWithSmallStepSize() throws Exception {
 integrator.setMaxEvaluations(1);
 integrator.integrate(equations, 0.0, initialValues, 1.0, new double[2]);
 fail("Integration should not have been successful.");
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxEvaluationsReached() throws Exception {
 integrator.setMaxEvaluations(1);
 integrator.integrate(equations, 0.0, initialValues, 1.0, new double[2]);
 integrator.integrate(equations, 1.0, initialValues, 2.0, new double[2]);
 fail("Integration should not have been successful.");
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithNoBracketing() throws Exception {
 integrator.setMaxEvaluations(1);
 integrator.integrate(new MyDifferentialEquationsNoBracketing(), 0.0, new double[]{1.0}, 1.0, new double[]{1.0});
 fail("Integration should not have been successful.");
 }

 @Test
 public void testIntegrateWithStepHandlers() throws Exception {
 StepHandler stepHandler = new MyStepHandler();
 integrator.addStepHandler(stepHandler);
 double[] initialValues = {1.0, 1.0};
 double time = 2.0;
 double[] expectedOutput = {2.0, 2.0};
 double[] actualOutput = integrator.integrate(equations, 0.0, initialValues, time, new double[2]);
 assertArrayEquals(expectedOutput, actualOutput);
 stepHandler.assertStepCalled();
 }

 @Test
 public void testIntegrateWithEventHandler() throws Exception {
 EventHandler eventHandler = new MyEventHandler();
 integrator.addEventHandler(eventHandler, 0.5, 1.0e-6, 1);
 double[] initialValues = {1.0, 1.0};
 double time = 2.0;
 double[] expectedOutput = {2.0, 2.0};
 double[] actualOutput = integrator.integrate(equations, 0.0, initialValues, time, new double[2]);
 assertArrayEquals(expectedOutput, actualOutput);
 eventHandler.assertEventTriggered();
 }

 @Test
 public void testIntegrateWithBothHandlers() throws Exception {
 StepHandler stepHandler = new MyStepHandler();
 integrator.addStepHandler(stepHandler);
 EventHandler eventHandler = new MyEventHandler();
 integrator.addEventHandler(eventHandler, 0.5, 1.0e-6, 1);
 double[] initialValues = {1.0, 1.0};
 double time = 2.0;
 double[] expectedOutput = {2.0, 2.0};
 double[] actualOutput = integrator.integrate(equations, 0.0, initialValues, time, new double[2]);
 assertArrayEquals(expectedOutput, actualOutput);
 stepHandler.assertStepCalled();
 eventHandler.assertEventTriggered();
 }

 @Test
 public void testSetMaxEvaluations() {
 integrator.setMaxEvaluations(10);
 assertEquals(10, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetMaxEvaluations() {
 integrator.setMaxEvaluations(10);
 assertEquals(10, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 integrator.setMaxEvaluations(10);
 integrator.integrate(equations, 0.0, initialValues, 2.0, new double[2]);
 assertEquals(10, integrator.getEvaluations());
 }

 @Test
 public void testInitIntegration() {
 integrator.initIntegration(0.0, initialValues, 1.0);
 assertTrue(integrator.isStateInitialized());
 }

 @Test
 public void testSetEquations() {
 ExpandableStatefulODE equations = new ExpandableStatefulODE(new MyDifferentialEquations());
 integrator.setEquations(equations);
 assertEquals(equations, integrator.getExpandable());
 }

 @Test
 public void testGetExpandable() {
 ExpandableStatefulODE equations = new ExpandableStatefulODE(new MyDifferentialEquations());
 integrator.setEquations(equations);
 assertEquals(equations, integrator.getExpandable());
 }

 @Test
 public void testGetEvaluationsCounter() {
 ExpandableStatefulODE equations = new ExpandableStatefulODE(new MyDifferentialEquations());
 integrator.setEquations(equations);
 assertEquals(integrator.getEvaluationsCounter(), integrator.getEvaluationsCounter());
 }

 @Test
 public void testAcceptStepWithNoEvents() throws Exception {
 double[] initialValues = {1.0, 1.0};
 double time = 2.0;
 double[] expectedOutput = {2.0, 2.0};
 double[] actualOutput = integrator.integrate(equations, 0.0, initialValues, time, new double[2])[0];
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testAcceptStepWithOneEvent() throws Exception {
 StepHandler stepHandler = new MyStepHandler();
 integrator.addStepHandler(stepHandler);
 EventHandler eventHandler = new MyEventHandler();
 integrator.addEventHandler(eventHandler, 0.5, 1.0e-6, 1);
 double[] initialValues = {1.0, 1.0};
 double time = 2.0;
 double[] expectedOutput = {2.0, 2.0};
 double[] actualOutput = integrator.integrate(equations, 0.0, initialValues, time, new double[2])[0];
 assertEquals(expectedOutput, actualOutput);
 eventHandler.assertEventTriggered();
 stepHandler.assertStepCalled();
 }

 @Test
 public void testAcceptStepWithMultipleEvents() throws Exception {
 StepHandler stepHandler = new MyStepHandler();
 integrator.addStepHandler(stepHandler);
 EventHandler eventHandler1 = new MyEventHandler();
 integrator.addEventHandler(eventHandler1, 0.5, 1.0e-6, 1);
 EventHandler eventHandler2 = new MyEventHandler();
 integrator.addEventHandler(eventHandler2, 1.0, 1.0e-6, 1);
 double[] initialValues = {1.0, 1.0};
 double time = 2.0;
 double[] expectedOutput = {2.0, 2.0};
 double[] actualOutput = integrator.integrate(equations, 0.0, initialValues, time, new double[2])[0];
 assertEquals(expectedOutput, actualOutput);
 eventHandler1.assertEventTriggered();
 eventHandler2.assertEventTriggered();
 stepHandler.assertStepCalled();
 }

 @Test
 public void testResetOccurred() throws Exception {
 StepHandler stepHandler = new MyStepHandler();
 integrator.addStepHandler(stepHandler);
 EventHandler eventHandler = new MyEventHandler();
 integrator.addEventHandler(eventHandler, 0.5, 1.0e-6, 1);
 double[] initialValues = {1.0, 1.0};
 double time = 2.0;
 double[] expectedOutput = {2.0, 2.0};
 double[] actualOutput = integrator.integrate(equations, 0.0, initialValues, time, new double[2])[0];
 assertEquals(expectedOutput, actualOutput);
 eventHandler.assertEventTriggered();
 stepHandler.assertStepCalled();
 integrator.reset();
 integrator.integrate(equations, 0.0, initialValues, time, new double[2]);
 assertFalse(integrator.isLastStep());
 }
}

class MyAbstractIntegrator extends AbstractIntegrator {
 private MyAbstractIntegrator(String name) {
 super(name);
 }

 protected void integrate(ExpandableStatefulODE equations, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 super.integrate(equations, p1);
 }
}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {
 public int getDimension() {
 return 2;
 }

 public double getTime() {
 return 0.0;
 }

 public void setTime(double time) {
 // Empty implementation
 }

 public double[] getPrimaryState() {
 return new double[]{1.0, 1.0};
 }

 public void setPrimaryState(double[] state) {
 // Empty implementation
 }

 public double[] getPrimaryDerivatives(double t, double[] y) {
 return new double[]{y[0] + 1.0, y[1] + 1.0};
 }

 public void setPrimaryDerivatives(double t, double[] yDot) {
 // Empty implementation
 }
}

class MyDifferentialEquationsNoBracketing implements FirstOrderDifferentialEquations {
 public int getDimension() {
 return 1;
 }

 public double getTime() {
 return 0.0;
 }

 public void setTime(double time) {
 // Empty implementation
 }

 public double[] getPrimaryState() {
 return new double[]{1.0};
 }

 public void setPrimaryState(double[] state) {
 // Empty implementation
 }

 public double[] getPrimaryDerivatives(double t, double[] y) {
 return new double[]{y[0] + 1.0};
 }

 public void setPrimaryDerivatives(double t, double[] yDot) {
 // Empty implementation
 }

 public double[] getSecondaryState(int secondaryIndex) {
 throw new UnsupportedOperationException();
 }

 public void setSecondaryState(int secondaryIndex, double[] secondaryState) {
 throw new UnsupportedOperationException();
 }

 public int getNumberOfEquations() {
 return 1;
 }

 public int getNumberOfSecondaryStates() {
 return 0;
 }

 public double[] getSecondaryDerivatives(double t, double[] y, double[] secondary) {
 throw new UnsupportedOperationException();
 }

 public void setSecondaryDerivatives(double t, double[] secondary) {
 throw new UnsupportedOperationException();
 }
}

class MyEventHandler implements EventHandler<AbstractODEEvent> {
 private int eventCount;

 public MyEventHandler() {
 eventCount = 0;
 }

 @Override
 public void handleStep(double t, double[] y, AbstractODEIntegrator integrator) {
 eventCount++;
 }

 public void assertEventTriggered() {
 assertEquals(1, eventCount);
 eventCount = 0;
 }

 public double getEventTime() {
 return 0.5;
 }
}

class MyStepHandler implements StepHandler {
 private int stepCount;

 public MyStepHandler() {
 stepCount = 0;
 }

 @Override
 public void handleStep(double t, double[] y, double[] yDot, double[] events, double[] eventTimes, int eventIndices, ODEStateAndDerivative oldStateAndDerivative, ODEStateAndDerivative newStateAndDerivative, AbstractODEIntegrator integrator) {
 stepCount++;
 }

 public void assertStepCalled() {
 assertEquals(1, stepCount);
 stepCount = 0;
 }
}