package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.AbstractEvent;
import org.apache.commons.math3.ode.events.When;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.ode.sampling.StepControlledIntegrator;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.SecondOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.AbstractEventHandler;
import org.apache.commons.math3.ode.events.AbstractStepHandler;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.*;
import java.lang.reflect.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private ExpandableStatefulODE equations;
 private double[] initialState;
 private double[] state;
 private double endTime;

 @Before
 public void setUp() {
 integrator = new AbstractStepControlledIntegrator() {
 @Override
 public void integrate(ExpandableStatefulODE ode, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 super.integrate(ode, t);
 }
 };
 integrator.setName("TestIntegrator");
 integrator.setMaxEvaluations(-1);
 equations = new ExpandableStatefulODE(new FirstOrderDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivative(double t, double y[], double yPrime[]) {
 yPrime[0] = y[0] * 2;
 }
 });
 initialState = new double[]{1.0};
 endTime = 2.0;
 }

 @After
 public void tearDown() {
 integrator = null;
 equations = null;
 initialState = null;
 state = null;
 }

 @Test
 public void testAbstractIntegrator() {
 // Test constructor
 integrator = new AbstractIntegrator("TestIntegrator");
 assertNotNull(integrator);
 assertEquals("TestIntegrator", integrator.getName());

 // Test addStepHandler
 integrator.addStepHandler(new AbstractStepHandler() {
 @Override
 public void handleStep(double t, double[] y, double[] yDot) {
 assertEquals(1.0, y[0], 1e-14);
 }
 });
 integrator.addStepHandler(new AbstractStepHandler() {
 @Override
 public void handleStep(double t, double[] y, double[] yDot) {
 assertEquals(2.0, y[0], 1e-14);
 }
 });
 integrator.addStepHandler(new AbstractStepHandler() {
 @Override
 public void handleStep(double t, double[] y, double[] yDot) {
 assertEquals(3.0, y[0], 1e-14);
 }
 });

 // Test getStepHandlers
 List<StepHandler> stepHandlers = integrator.getStepHandlers();
 assertEquals(3, stepHandlers.size());

 // Test clearStepHandlers
 integrator.clearStepHandlers();
 stepHandlers = integrator.getStepHandlers();
 assertEquals(0, stepHandlers.size());

 // Test addEventHandler
 integrator.addEventHandler(new AbstractEventHandler() {
 @Override
 public boolean evaluateStep(StepInterpolator interpolator) throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 return interpolator.getGlobalCurrentTime() > 1.5;
 }
 }, 0.1, 1e-12, 10);

 // Test getEventHandlers
 List<EventHandler> eventHandlers = integrator.getEventHandlers();
 assertEquals(1, eventHandlers.size());

 // Test clearEventHandlers
 integrator.clearEventHandlers();
 eventHandlers = integrator.getEventHandlers();
 assertEquals(0, eventHandlers.size());

 // Test getCurrentStepStart
 integrator.initIntegration(0.0, initialState, endTime);
 assertEquals(Double.NaN, integrator.getCurrentStepStart(), 1e-14);

 // Test getCurrentSignedStepsize
 integrator.initIntegration(0.0, initialState, endTime);
 integrator.setMaxEvaluations(1);
 integrator.setStepSize(0.1);
 integrator.integrate(equations, 0.0, initialState, 0.1, state);
 assertEquals(0.1, integrator.getCurrentSignedStepsize(), 1e-14);

 // Test setMaxEvaluations
 integrator.initIntegration(0.0, initialState, endTime);
 integrator.setMaxEvaluations(3);
 integrator.integrate(equations, 0.0, initialState, 0.1, state);
 integrator.integrate(equations, 0.1, state, 0.2, state);
 integrator.integrate(equations, 0.2, state, 0.3, state);
 assertEquals(3, integrator.getEvaluations());

 // Test initIntegration
 integrator.initIntegration(0.0, initialState, endTime);
 assertEquals(Double.NaN, integrator.getCurrentStepStart(), 1e-14);
 assertEquals(0.0, integrator.getEvaluations(), 1e-14);
 integrator.setStepSize(0.1);
 integrator.integrate(equations, 0.0, initialState, 0.1, state);
 assertEquals(0.1, integrator.getCurrentStepStart(), 1e-14);
 assertEquals(1, integrator.getEvaluations());

 // Test setEquations and getExpandable
 integrator.setEquations(equations);
 integrator.initIntegration(0.0, initialState, endTime);
 ExpandableStatefulODE expandable = integrator.getExpandable();
 assertEquals(equations, expandable);

 // Test integrate
 integrator.initIntegration(0.0, initialState, endTime);
 integrator.setMaxEvaluations(1);
 integrator.integrate(equations, 0.0, initialState, 0.1, state);
 assertEquals(1.1, state[0], 1e-14);

 // Test computeDerivatives
 integrator.initIntegration(0.0, initialState, endTime);
 integrator.computeDerivatives(0.0, initialState, null);

 // Test setStateInitialized
 integrator.initIntegration(0.0, initialState, endTime);
 integrator.setStateInitialized(false);
 integrator.integrate(equations, 0.0, initialState, 0.1, state);
 integrator.integrate(equations, 0.1, state, 0.2, state);
 integrator.setStateInitialized(true);
 integrator.integrate(equations, 0.2, state, 0.3, state);
 assertTrue(integrator.isLastStep());
 integrator.reset();
 integrator.integrate(equations, 0.2, state, 0.3, state);
 assertFalse(integrator.isLastStep());
 }
}