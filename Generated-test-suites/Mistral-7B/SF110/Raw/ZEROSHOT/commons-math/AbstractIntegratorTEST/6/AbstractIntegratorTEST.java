package org.apache.commons.math3.ode.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;

 @Before
 public void setUp() {
 integrator = new MyAbstractIntegrator("Test Integrator");
 equations = new MyDifferentialEquations();
 }

 @After
 public void tearDown() {
 integrator = null;
 equations = null;
 }

 @Test
 public void testGetName() {
 assertEquals("Test Integrator", integrator.getName());
 }

 @Test
 public void testAddStepHandler() {
 integrator.addStepHandler(new MyStepHandler());
 List<StepHandler> stepHandlers = integrator.getStepHandlers();
 assertEquals(1, stepHandlers.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyStepHandlers() {
 integrator.getStepHandlers().clear();
 }

 @Test
 public void testClearStepHandlers() {
 integrator.clearStepHandlers();
 assertEquals(0, integrator.getStepHandlers().size());
 }

 @Test
 public void testAddEventHandler() {
 integrator.addEventHandler(new MyEventHandler(1.0), 0.001, 1.0e-8, 10);
 List<EventHandler> eventHandlers = integrator.getEventHandlers();
 assertEquals(1, eventHandlers.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyEventHandlers() {
 integrator.getEventHandlers().clear();
 }

 @Test
 public void testClearEventHandlers() {
 integrator.clearEventHandlers();
 assertEquals(0, integrator.getEventHandlers().size());
 }

 @Test
 public void testGetCurrentStepStart() {
 double t0 = 0.0;
 double[] y0 = new double[1];
 integrator.initIntegration(t0, y0, 1.0);
 assertEquals(t0, integrator.getCurrentStepStart(), 1e-15);
 }

 @Test
 public void testGetCurrentSignedStepsize() {
 double t0 = 0.0;
 double[] y0 = new double[1];
 integrator.initIntegration(t0, y0, 1.0);
 assertNotNull(integrator.getCurrentSignedStepsize());
 }

 @Test(expected = IllegalStateException.class)
 public void testIntegrateWithNoEquations() {
 integrator.integrate(null, 0.0, new double[1], 1.0, new double[1]);
 }

 @Test
 public void testIntegrate() throws Exception {
 double t0 = 0.0;
 double[] y0 = new double[1];
 double t = 1.0;
 double[] y = new double[1];
 integrator.initIntegration(t0, y0, t);
 integrator.integrate(equations, t0, y0, t, y);
 assertEquals(t, integrator.getCurrentStepStart(), 1e-15);
 System.arraycopy(y, 0, y0, 0, y0.length);
 integrator.integrate(equations, t, y, t, y);
 }

 @Test(expected = IllegalStateException.class)
 public void testIntegrateWithNoInitialState() {
 integrator.integrate(equations, 0.0, null, 1.0, new double[1]);
 }

 @Test
 public void testComputeDerivatives() throws Exception {
 double t = 0.0;
 double[] y = new double[1];
 integrator.initIntegration(0.0, y, t);
 integrator.computeDerivatives(t, y, new double[1]);
 }

 @Test(expected = IllegalStateException.class)
 public void testIntegrateWithMaxEvaluationsReached() throws Exception {
 integrator.setMaxEvaluations(1);
 integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
 integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
 }

 @Test(expected = IllegalStateException.class)
 public void testIntegrateWithNoBracketing() throws Exception {
 integrator.setExpandable(new MyNoBracketingExpandableODE());
 integrator.integrate(equations, 0.0, new double[1], 1.0, new double[1]);
 }

 @Test
 public void testAcceptStep() throws Exception {
 double t0 = 0.0;
 double[] y0 = new double[1];
 integrator.initIntegration(t0, y0, 1.0);
 integrator.setExpandable(new MyExpandableODE());
 integrator.setMaxEvaluations(100);

 double[] y = new double[1];
 double tEnd = 2.0;

 AbstractStepInterpolator interpolator = new MyStepInterpolator(0.0, 1.0, 0.5);
 double step = integrator.acceptStep(interpolator, y, new double[1], tEnd);
 assertEquals(1.0, step, 1e-15);

 interpolator.setGlobalCurrentTime(1.5);
 step = integrator.acceptStep(interpolator, y, new double[1], tEnd);
 assertEquals(1.5, step, 1e-15);

 interpolator.setGlobalCurrentTime(2.0);
 step = integrator.acceptStep(interpolator, y, new double[1], tEnd);
 assertEquals(2.0, step, 1e-15);

 interpolator.setGlobalCurrentTime(2.5);
 step = integrator.acceptStep(interpolator, y, new double[1], tEnd);
 assertNotNull(step);
 assertTrue(integrator.isLastStep());
 }

 @Test
 public void testSanityChecks() throws Exception {
 integrator.setExpandable(new MySanityCheckExpandableODE());
 integrator.sanityChecks(equations, 1.0);
 }
}

class MyAbstractIntegrator extends AbstractIntegrator {

 public MyAbstractIntegrator(String name) {
 super(name);
 }

 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Your implementation here
 }
}

class MyDifferentialEquations implements FirstOrderDifferentialEquations {

 public int getDimension() {
 return 1;
 }

 public void computePrimaryDerivative(double t, double[] y, double[] yDot) {
 yDot[0] = y[0] * 2.0;
 }
}

class MyEventHandler implements EventHandler {

 private final double eventTime;

 public MyEventHandler(double eventTime) {
 this.eventTime = eventTime;
 }

 public boolean evaluateStep(AbstractStepInterpolator interpolator) {
 return interpolator.getGlobalCurrentTime() >= eventTime;
 }

 public void stepAccepted(double t, double[] y) {
 System.out.println("Event occurred at t = " + t);
 }
}

class MyStepHandler implements StepHandler {

 public void handleStep(StepContext context, boolean lastStep) {
 System.out.println("Step handler called with lastStep = " + lastStep);
 }
}

class MyStepInterpolator extends StepInterpolator {

 private final double startTime;
 private final double endTime;
 private final double interval;

 public MyStepInterpolator(double startTime, double endTime, double interval) {
 super(startTime, endTime, interval);
 this.startTime = startTime;
 this.endTime = endTime;
 this.interval = interval;
 }

 @Override
 public double getGlobalCurrentTime() {
 return getCurrentTime();
 }

 @Override
 public double getGlobalPreviousTime() {
 return getPreviousTime();
 }

 @Override
 public void setSoftCurrentTime(double time) {
 setCurrentTime(time);
 }

 @Override
 public void setSoftPreviousTime(double time) {
 setPreviousTime(time);
 }

 @Override
 public void setInterpolatedTime(double time) {
 setCurrentTime(time);
 }

 @Override
 public double getInterpolatedState(int index) {
 return getPrimaryState()[index];
 }

 @Override
 public double[] getInterpolatedSecondaryState(int index) {
 return new double[0];
 }
}

class MyExpandableODE extends ExpandableStatefulODE {

 public MyExpandableODE() {
 super(new MyDifferentialEquations());
 }
}

class MyNoBracketingExpandableODE extends ExpandableStatefulODE {

 public MyNoBracketingExpandableODE() {
 super(new MyDifferentialEquations());
 }

 @Override
 public Timer getTimer() {
 throw new UnsupportedOperationException();
 }
}

class MySanityCheckExpandableODE extends ExpandableStatefulODE {

 public MySanityCheckExpandableODE() {
 super(new MyDifferentialEquations());
 }

 @Override
 public double getTime() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void setTime(double time) {
 throw new UnsupportedOperationException();
 }

 @Override
 public double[] getPrimaryState() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void setPrimaryState(double[] state) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void setPrimaryMapper(PrimaryMapper mapper) {
 throw new UnsupportedOperationException();
 }

 @Override
 public PrimaryMapper getPrimaryMapper() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void setSecondaryMappers(Collection<EquationsMapper> mappers) {
 throw new UnsupportedOperationException();
 }

 @Override
 public Collection<EquationsMapper> getSecondaryMappers() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void setEventHandlers(Collection<EventHandler> handlers) {
 throw new UnsupportedOperationException();
 }

 @Override
 public Collection<EventHandler> getEventHandlers() {
 throw new UnsupportedOperationException();
 }

 @Override
 public boolean hasEvents() {
 throw new UnsupportedOperationException();
 }

 @Override
 public boolean hasEvents(EventHandler handler) {
 throw new UnsupportedOperationException();
 }

 @Override
 public double getEventTime(int index) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void setEventTime(int index, double time) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void removeEvent(int index) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void removeEvent(EventHandler handler) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void clearEvents() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void reinitializeBegin(AbstractStepInterpolator interpolator) {
 // Your implementation here
 }

 @Override
 public boolean evaluateStep(AbstractStepInterpolator interpolator) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void stepAccepted(double t, double[] y) {
 // Your implementation here
 }

 @Override
 public boolean stop() {
 throw new UnsupportedOperationException();
 }

 @Override
 public void reset(double t, double[] y) {
 // Your implementation here
 }
}