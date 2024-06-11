package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.junit.Test;
import java.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;

 @Test
 public void testConstructor() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // implementation not needed for testing
 }
 };
 assertEquals("TestIntegrator", integrator.getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullName() {
 integrator = new AbstractIntegrator(null);
 }

 @Test
 public void testAddStepHandler() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // implementation not needed for testing
 }
 };
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(StepContext context, int index) {
 fail("Not called.");
 }
 });
 assertTrue(integrator.getStepHandlers().contains(integrator.getStepHandlers().iterator().next()));
 }

 @Test
 public void testGetStepHandlers() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // implementation not needed for testing
 }
 };
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(StepContext context, int index) {
 fail("Not called.");
 }
 });
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(StepContext context, int index) {
 fail("Not called.");
 }
 });
 List<StepHandler> handlers = integrator.getStepHandlers();
 assertEquals(2, handlers.size());
 assertEquals(handlers.get(0), handlers.get(1));
 }

 @Test
 public void testClearStepHandlers() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // implementation not needed for testing
 }
 };
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void handleStep(StepContext context, int index) {
 fail("Not called.");
 }
 });
 integrator.clearStepHandlers();
 assertTrue(integrator.getStepHandlers().isEmpty());
 }

 @Test
 public void testAddEventHandler() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // implementation not needed for testing
 }
 };
 integrator.addEventHandler(new MockEventHandler(), 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.01, 5));
 assertTrue(integrator.getEventHandlers().contains(integrator.getEventHandlers().iterator().next()));
 }

 @Test
 public void testGetEventHandlers() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // implementation not needed for testing
 }
 };
 integrator.addEventHandler(new MockEventHandler(), 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.01, 5));
 integrator.addEventHandler(new MockEventHandler(), 0.1, 0.01, 1, new BracketingNthOrderBrentSolver(0.01, 5));
 List<EventHandler> handlers = integrator.getEventHandlers();
 assertEquals(2, handlers.size());
 assertEquals(handlers.get(0), handlers.get(1));
 }

 @Test
 public void testClearEventHandlers() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // implementation not needed for testing
 }
 };
 integrator.addEventHandler(new MockEventHandler(), 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.01, 5));
 integrator.clearEventHandlers();
 assertTrue(integrator.getEventHandlers().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEventHandlerWithNullEventHandler() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // implementation not needed for testing
 }
 };
 integrator.addEventHandler(null, 0.0, 0.01, 1, new BracketingNthOrderBrentSolver(0.01, 5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEventHandlerWithNullEventHandlerSolver() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // implementation not needed for testing
 }
 };
 integrator.addEventHandler(new MockEventHandler(), 0.0, 0.01, 1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEventHandlerWithNegativeMaxIterationCount() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // implementation not needed for testing
 }
 };
 integrator.addEventHandler(new MockEventHandler(), 0.0, 0.01, -1, new BracketingNthOrderBrentSolver(0.01, 5));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEventHandlerWithMaxIterationCountLesserThanOne() {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // implementation not needed for testing
 }
 };
 integrator.addEventHandler(new MockEventHandler(), 0.0, 0.01, 0.5, new BracketingNthOrderBrentSolver(0.01, 5));
 }

}