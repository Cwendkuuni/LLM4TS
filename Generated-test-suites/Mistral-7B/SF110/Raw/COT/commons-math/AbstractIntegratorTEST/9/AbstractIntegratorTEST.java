package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.util.*;
import org.junit.Test;

import java.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 integrator = new MyAbstractIntegrator("TestIntegrator");
 }

 @Test
 public void testConstructor() {
 integrator = new AbstractIntegrator(); // default constructor
 assertNull(integrator.getName());
 }

 @Test
 public void testGetName() {
 assertEquals("TestIntegrator", integrator.getName());
 }

 @Test
 public void testAddStepHandler() {
 integrator.addStepHandler(new MyStepHandler());
 assertEquals(1, integrator.getStepHandlers().size());
 }

 @Test
 public void testGetStepHandlers() {
 List<StepHandler> handlers = (List<StepHandler>) integrator.getStepHandlers();
 StepHandler handler = handlers.get(0);
 assertEquals("TestStepHandler", handler.getClass().getSimpleName());
 }

 @Test
 public void testClearStepHandlers() {
 integrator.clearStepHandlers();
 assertEquals(0, integrator.getStepHandlers().size());
 }

 @Test
 public void testAddEventHandler() {
 integrator.addEventHandler(new MyEventHandler(0.5), 1.0, 1e-6, 10);
 assertEquals(1, integrator.getEventHandlers().size());
 }

 @Test
 public void testGetEventHandlers() {
 List<EventHandler> handlers = (List<EventHandler>) integrator.getEventHandlers();
 EventHandler handler = handlers.get(0);
 assertEquals("TestEventHandler", handler.getClass().getSimpleName());
 }

 @Test
 public void testClearEventHandlers() {
 integrator.clearEventHandlers();
 assertEquals(0, integrator.getEventHandlers().size());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithWrongStateSize() {
 integrator.integrate(new MyEquations(1.0), 0.0, new double[]{1.0}, 1.0, new double[]{2.0});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithWrongDestinationStateSize() {
 integrator.integrate(new MyEquations(1.0), 0.0, new double[]{1.0}, 1.0, new double[]{2.0, 3.0});
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, MaxCountExceededException, NoBracketingException, DimensionMismatchException {
 double[] initialState = {1.0, 1.0};
 double[] finalState = {2.0, 2.0};
 assertEquals(2.0, integrator.integrate(new MyEquations(1.0), 0.0, initialState, 1.0, finalState)[0], 1e-6);
 assertArrayEquals(finalState, integrator.getExpandable().getPrimaryState(), 1e-6);
 }

 @Test
 public void testAcceptStepForward() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 AbstractStepInterpolator interpolator = new StepInterpolator(new ExpandableStatefulODE(new MyEquations(1.0)), 0.1, 1.0);
 double[] initialState = {1.0, 1.0};
 double[] finalState = {2.0, 2.0};
 double interpolatedTime = integrator.acceptStep(interpolator, initialState, null, 1.0);
 assertArrayEquals(finalState, integrator.getExpandable().getPrimaryState(), 1e-6);
 assertEquals(1.0, interpolatedTime, 1e-6);
 }

 @Test
 public void testAcceptStepBackward() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 AbstractStepInterpolator interpolator = new StepInterpolator(new ExpandableStatefulODE(new MyEquations(1.0)), -0.1, 1.0);
 double[] initialState = {1.0, 1.0};
 double[] finalState = {2.0, 2.0};
 double interpolatedTime = integrator.acceptStep(interpolator, initialState, null, 1.0);
 assertArrayEquals(finalState, integrator.getExpandable().getPrimaryState(), 1e-6);
 assertEquals(1.0, interpolatedTime, 1e-6);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxEvaluations() throws MaxCountExceededException, DimensionMismatchException {
 integrator.setMaxEvaluations(0);
 integrator.integrate(new MyEquations(1.0), 0.0, new double[]{1.0}, 1.0, new double[]{1.0});
 }

 @Test(expected = NoBracketingException.class)
 public void testNoBracketing() throws MaxCountExceededException, DimensionMismatchException {
 integrator.setMaxEvaluations(10);
 integrator.integrate(new MyEquationsNoBracketing(1.0), 0.0, new double[]{1.0}, 1.0, new double[]{1.0});
 }
}

class MyAbstractIntegrator extends AbstractIntegrator {
 // Empty implementation
}

class MyStepHandler implements StepHandler {
 // Empty implementation
}

class MyEventHandler implements EventHandler {
 double eventTime;

 public MyEventHandler(double eventTime) {
 this.eventTime = eventTime;
 }

 @Override
 public boolean evaluateStep(AbstractStepInterpolator interpolator) {
 return interpolator.getCurrentTime() >= eventTime;
 }

 @Override
 public void stepAccepted(double time, double[] y) {
 // Empty implementation
 }

 @Override
 public boolean stop() {
 return false;
 }

 @Override
 public void init(double t0, double[] y0, double t) {
 // Empty implementation
 }
}

class MyEquations implements FirstOrderDifferentialEquations {
 double time;
 double[] primaryState;

 public MyEquations(double time) {
 this.time = time;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 yDot[0] = 1.0;
 yDot[1] = 1.0;
 }

 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public double getTime() {
 return time;
 }

 @Override
 public double[] getPrimaryState() {
 return primaryState;
 }

 @Override
 public double[] getSecondaryState() {
 return new double[0];
 }
}

class MyEquationsNoBracketing implements FirstOrderDifferentialEquations {
 double time;
 double[] primaryState;

 public MyEquationsNoBracketing(double time) {
 this.time = time;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 yDot[0] = y[1];
 yDot[1] = -y[0];
 }

 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public double getTime() {
 return time;
 }

 @Override
 public double[] getPrimaryState() {
 return primaryState;
 }

 @Override
 public double[] getSecondaryState() {
 return new double[0];
 }
}