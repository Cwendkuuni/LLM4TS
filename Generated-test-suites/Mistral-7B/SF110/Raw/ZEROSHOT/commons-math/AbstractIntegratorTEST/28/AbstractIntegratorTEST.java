package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.stephandlers.StepHandler;
import org.apache.commons.math3.ode.stephandlers.StepHandlerAdapter;
import org.apache.commons.math3.ode.util.ExpandableODEState;
import org.apache.commons.math3.ode.util.ExpandableODEStatefulODE;
import org.apache.commons.math3.ode.util.UnivariateSolver;
import org.apache.commons.math3.special.BrentSolver;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;

public class AbstractIntegratorTest {

 private static final String NAME = "TestIntegrator";

 @Test
 public void testConstructor() {
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 assertNotNull(integrator);
 assertEquals(NAME, integrator.getName());
 }

 @Test
 public void testAddStepHandler() {
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 StepHandler handler = new StepHandlerAdapter();
 integrator.addStepHandler(handler);
 assertTrue(integrator.getStepHandlers().contains(handler));
 }

 @Test
 public void testClearStepHandlers() {
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 StepHandler handler = new StepHandlerAdapter();
 integrator.addStepHandler(handler);
 integrator.clearStepHandlers();
 assertFalse(integrator.getStepHandlers().contains(handler));
 }

 @Test
 public void testAddEventHandler() {
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 StepHandler handler = new StepHandlerAdapter();
 integrator.addEventHandler(createMockEventHandler(), 1.0, 1.0e-10, 10);
 assertEquals(1, integrator.getEventHandlers().size());
 }

 @Test
 public void testClearEventHandlers() {
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 integrator.addEventHandler(createMockEventHandler(), 1.0, 1.0e-10, 10);
 integrator.clearEventHandlers();
 assertEquals(0, integrator.getEventHandlers().size());
 }

 @Test
 public void testGetCurrentStepStart() {
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 double t0 = 0.0;
 double[] y0 = new double[1];
 double t = 1.0;
 integrator.initIntegration(t0, y0, t);
 assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0.0);
 integrator.integrate(new MockDifferentialEquations(1), t0, y0, t, y0);
 assertEquals(t0, integrator.getCurrentStepStart(), 0.0);
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 double t0 = 0.0;
 double[] y0 = new double[1];
 double t = 1.0;
 integrator.initIntegration(t0, y0, t);
 assertEquals(Double.NaN, integrator.getCurrentSignedStepsize(), 0.0);
 integrator.integrate(new MockDifferentialEquations(1), t0, y0, t, y0);
 assertEquals(0.0, integrator.getCurrentSignedStepsize(), 0.0);
 }

 @Test
 public void testSetMaxEvaluations() {
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 integrator.setMaxEvaluations(10);
 assertEquals(10, integrator.getMaxEvaluations());
 integrator.setMaxEvaluations(-1);
 assertEquals(-1, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 integrator.integrate(new MockDifferentialEquations(1), 0.0, new double[1], 1.0, new double[1]);
 assertEquals(1, integrator.getEvaluations());
 }

 @Test
 public void testIntegrate() throws Exception {
 ExpandableODEState initialState = new ExpandableODEStatefulODE(new MockDifferentialEquations(1), 0.0, new double[1]);
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(initialState.getODE());
 integrator.integrate(initialState.getODE(), 0.0);
 double[] result = new double[1];
 integrator.integrate(new MockDifferentialEquations(1), 0.0, new double[1], 1.0, result);
 assertArrayEquals(new double[]{1.0}, result, 0.0);
 }

 @Test
 public void testComputeDerivatives() throws Exception {
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 ExpandableStatefulODE ode = new ExpandableStatefulODE(new MockDifferentialEquations(1));
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(ode);
 double[] y = new double[1];
 double[] yDot = new double[1];
 integrator.computeDerivatives(0.5, y, yDot);
 assertArrayEquals(new double[]{0.5, 0.5}, new double[]{y[0], yDot[0]}, 0.0);
 }

 @Test
 public void testStateInitialized() {
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 assertFalse(integrator.isStateInitialized());
 integrator.initIntegration(0.0, new double[1], 1.0);
 assertFalse(integrator.isStateInitialized());
 integrator.integrate(new MockDifferentialEquations(1), 0.0, new double[1], 1.0, new double[1]);
 assertTrue(integrator.isStateInitialized());
 }

 @Test
 public void testAcceptStep() throws Exception {
 ExpandableODEState initialState = new ExpandableODEStatefulODE(new MockDifferentialEquations(1), 0.0, new double[1]);
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(initialState.getODE());
 integrator.addEventHandler(createMockEventHandler(), 0.5, 1.0e-10, 10, createMockSolver());
 integrator.integrate(new MockDifferentialEquations(1), 0.0, new double[1], 1.0, new double[1]);
 assertEquals(1.0, integrator.getCurrentStepStart(), 0.0);
 assertEquals(0.5, integrator.acceptStep(createMockInterpolator(0.5), new double[1], new double[1], 0.6), 0.0);
 assertArrayEquals(new double[]{0.6, 1.1}, new double[]{integrator.getCurrentSignedStepsize(), integrator.getCurrentStepStart()}, 0.0);
 assertEquals(1, integrator.getEventHandlers().size());
 integrator.acceptStep(createMockInterpolator(0.6), new double[1], new double[1], 0.8);
 assertEquals(1, integrator.getEventHandlers().size());
 integrator.acceptStep(createMockInterpolator(0.8), new double[1], new double[1], 1.0);
 assertEquals(0, integrator.getEventHandlers().size());
 }

 @Test
 public void testIntegrateWithEvents() throws Exception {
 ExpandableODEState initialState = new ExpandableODEStatefulODE(new MockDifferentialEquations(1), 0.0, new double[1]);
 AbstractIntegrator integrator = new AbstractIntegrator(NAME);
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.setExpandable(initialState.getODE());
 integrator.addEventHandler(createMockEventHandler1(), 0.5, 1.0e-10, 10, createMockSolver());
 integrator.addEventHandler(createMockEventHandler2(), 0.7, 1.0e-10, 10, createMockSolver());
 integrator.integrate(new MockDifferentialEquations(1), 0.0, new double[1], 1.0, new double[1]);
 assertEquals(1.0, integrator.getCurrentStepStart(), 0.0);
 assertEquals(0.5, integrator.acceptStep(createMockInterpolator(0.5), new double[1], new double[1], 0.6), 0.0);
 assertEquals(1, integrator.getEventHandlers().size());
 integrator.acceptStep(createMockInterpolator(0.6), new double[1], new double[1], 0.8);
 assertEquals(2, integrator.getEventHandlers().size());
 integrator.acceptStep(createMockInterpolator(0.8), new double[1], new double[1], 1.0);
 assertEquals(0, integrator.getEventHandlers().size());
 }

 private FirstOrderDifferentialEquations createMockDifferentialEquations(int n) {
 return new MockDifferentialEquations(n);
 }

 private ExpandableStatefulODE createMockODE() {
 return new ExpandableStatefulODE(new MockDifferentialEquations(1));
 }

 private StepHandler createMockStepHandler() {
 return new StepHandlerAdapter();
 }

 private EventHandler createMockEventHandler() {
 return new MockEventHandler();
 }

 private EventHandler createMockEventHandler1() {
 return new MockEventHandler1();
 }

 private EventHandler createMockEventHandler2() {
 return new MockEventHandler2();
 }

 private UnivariateSolver createMockSolver() {
 return new BrentSolver();
 }

 private AbstractStepInterpolator createMockInterpolator(double t) {
 return new AbstractStepInterpolator() {
 @Override
 public double getGlobalPreviousTime() {
 return t - 0.1;
 }

 @Override
 public double getGlobalCurrentTime() {
 return t;
 }
 };
 }

 private static class MockDifferentialEquations implements FirstOrderDifferentialEquations {
 private final int dimension;

 public MockDifferentialEquations(int dimension) {
 this.dimension = dimension;
 }

 @Override
 public int getDimension() {
 return dimension;
 }

 @Override
 public void computePrimaryDerivatives(double t, double[] y, double[] yPrime) {
 yPrime[0] = y[0];
 }
 }

 private static class MockEventHandler implements EventHandler {
 @Override
 public void evaluateStep(AbstractStepInterpolator interpolator) {
 return true;
 }

 @Override
 public boolean stop() {
 return false;
 }
 }

 private static class MockEventHandler1 implements EventHandler {
 @Override
 public void evaluateStep(AbstractStepInterpolator interpolator) {
 return true;
 }

 @Override
 public boolean stop() {
 return true;
 }
 }

 private static class MockEventHandler2 implements EventHandler {
 @Override
 public void evaluateStep(AbstractStepInterpolator interpolator) {
 return true;
 }

 @Override
 public boolean stop() {
 return false;
 }
 }
}