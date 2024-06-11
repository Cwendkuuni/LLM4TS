package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.EventHandler;
import org.apache.commons.math3.ode.ExpandableState;
import org.apache.commons.math3.ode.events.RelativisticEvent;
import org.apache.commons.math3.ode.events.WhenReachedEvent;
import org.junit.Test;
import java.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator abstractIntegrator;
 private ExpandableStatefulODE expandableODE;
 private double t0 = 0.0;
 private double[] y0 = {1.0, 2.0};
 private double t = 10.0;
 private double[] y = new double[2];
 private StepHandler stepHandler;
 private EventHandler eventHandler;
 private RelativisticEvent relativisticEvent;
 private WhenReachedEvent whenReachedEvent;

 @Before
 public void setUp() {
 abstractIntegrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Empty implementation for testing purposes
 }
 };
 stepHandler = new StepHandler() {
 @Override
 public void handleStep(StepContext1D stepContext, boolean lastStep) {
 // Empty implementation for testing purposes
 }
 };
 eventHandler = new EventHandler() {
 @Override
 public int handle(double t, double[] y, double[] yDot, double[] eventData, int type, double[] localData, StepContext context) {
 // Empty implementation for testing purposes
 return 0;
 }
 };
 relativisticEvent = new RelativisticEvent(0.5, 1.0);
 whenReachedEvent = new WhenReachedEvent(2.0);
 expandableODE = new ExpandableStatefulODE(new MyDifferentialEquations()) {{
 setTime(t0);
 setPrimaryState(y0);
 setStepHandler(stepHandler);
 setEventHandlers(Collections.singletonList(eventHandler));
 }};
 }

 @Test
 public void testConstructor() {
 assertEquals("TestIntegrator", abstractIntegrator.getName());
 assertEquals(0, abstractIntegrator.getStepHandlers().size());
 assertEquals(0, abstractIntegrator.getEventHandlers().size());
 assertEquals(-1, abstractIntegrator.getMaxEvaluations());
 assertEquals(0, abstractIntegrator.getEvaluations());
 }

 @Test
 public void testAddStepHandler() {
 abstractIntegrator.addStepHandler(stepHandler);
 assertEquals(1, abstractIntegrator.getStepHandlers().size());
 }

 @Test
 public void testGetStepHandlers() {
 List<StepHandler> stepHandlers = abstractIntegrator.getStepHandlers();
 assertEquals(1, stepHandlers.size());
 assertEquals(stepHandler, stepHandlers.get(0));
 }

 @Test
 public void testClearStepHandlers() {
 abstractIntegrator.clearStepHandlers();
 assertEquals(0, abstractIntegrator.getStepHandlers().size());
 }

 @Test
 public void testAddEventHandler() {
 abstractIntegrator.addEventHandler(eventHandler, 0.1, 1.0e-6, 10);
 assertEquals(1, abstractIntegrator.getEventHandlers().size());
 }

 @Test
 public void testGetEventHandlers() {
 List<EventHandler> eventHandlers = abstractIntegrator.getEventHandlers();
 assertEquals(1, eventHandlers.size());
 assertEquals(eventHandler, eventHandlers.get(0));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithDimensionMismatchException() {
 expandableODE.setPrimaryState(new double[]{1.0});
 try {
 abstractIntegrator.integrate(expandableODE, t);
 fail("DimensionMismatchException expected");
 } catch (DimensionMismatchException e) {
 // Expected
 }
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateWithNumberIsTooSmallException() {
 stepSize = 0.01;
 abstractIntegrator.setMaxEvaluations(1);
 try {
 abstractIntegrator.integrate(expandableODE, t);
 fail("NumberIsTooSmallException expected");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxCountExceededException() {
 abstractIntegrator.setMaxEvaluations(1);
 try {
 abstractIntegrator.integrate(expandableODE, t);
 fail("MaxCountExceededException expected");
 } catch (MaxCountExceededException e) {
 // Expected
 }
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithNoBracketingException() {
 expandableODE.setPrimaryState(new double[]{0.0, 0.0});
 expandableODE.setEventHandlers(new ArrayList<EventHandler>() {{
 add(new WhenReachedEvent(0.0));
 add(new WhenReachedEvent(1.0));
 }});
 try {
 abstractIntegrator.integrate(expandableODE, t);
 fail("NoBracketingException expected");
 } catch (NoBracketingException e) {
 // Expected
 }
 }

 @Test
 public void testSanityChecks() {
 try {
 abstractIntegrator.sanityChecks(expandableODE, t);
 } catch (NumberIsTooSmallException e) {
 fail("NumberIsTooSmallException not expected");
 }
 }

 @Test
 public void testIntegrate() {
 abstractIntegrator.setMaxEvaluations(100);
 abstractIntegrator.integrate(expandableODE, t);
 assertEquals(t, abstractIntegrator.getCurrentStepStart());
 assertEquals(-1, abstractIntegrator.getEvaluations() % 100);
 }

 @Test
 public void testAcceptStep() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 double previousT = t0;
 double currentT = t0;
 StepContext1D stepContext = new StepContext1D(previousT, currentT, y0, y0, expandableODE);
 double tEnd = t + 1.0;
 try {
 double stepSize = abstractIntegrator.acceptStep(stepContext, y, tEnd);
 assertEquals(t, stepSize);
 } finally {
 assertEquals(1, abstractIntegrator.getEvaluations());
 }
 }

 @Test
 public void testAcceptStepWithRelativisticEvent() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException, NumberIsTooSmallException {
 relativisticEvent.setIntegrator(abstractIntegrator);
 expandableODE.addEvent(relativisticEvent);
 double previousT = t0;
 double currentT = t0;
 StepContext1D stepContext = new StepContext1D(previousT, currentT, y0, y0, expandableODE);
 try {
 abstractIntegrator.acceptStep(stepContext, y, t + 0.5);
 fail("RelativisticEvent should stop the integration");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testAcceptStepWithWhenReachedEvent() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 whenReachedEvent.setIntegrator(abstractIntegrator);
 expandableODE.addEvent(whenReachedEvent);
 double previousT = t0;
 double currentT = t0;
 StepContext1D stepContext = new StepContext1D(previousT, currentT, y0, y0, expandableODE);
 try {
 abstractIntegrator.acceptStep(stepContext, y, t + 0.1);
 fail("WhenReachedEvent should stop the integration");
 } catch (Exception e) {
 // Expected
 }
 }

 @Test
 public void testResetOccurred() throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 double previousT = t0;
 double currentT = t0;
 StepContext1D stepContext = new StepContext1D(previousT, currentT, y0, y0, expandableODE);
 try {
 abstractIntegrator.acceptStep(stepContext, y, t + 0.1);
 assertEquals(false, abstractIntegrator.getResetOccurred());
 } finally {
 abstractIntegrator.resetOccurred = false;
 }
 try {
 abstractIntegrator.acceptStep(stepContext, y, t + 0.1);
 assertEquals(true, abstractIntegrator.getResetOccurred());
 } finally {
 abstractIntegrator.resetOccurred = false;
 }
 }
}