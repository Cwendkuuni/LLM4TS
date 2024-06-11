package org.apache.commons.math3.ode.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.solvers.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;
import org.junit.Test;
import java.util.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;

 @Test
 public void testAbstractIntegrator_Constructor() {
 integrator = new AbstractIntegrator("TestIntegrator");
 assertEquals("TestIntegrator", integrator.getName());
 assertNotNull(integrator.getStepHandlers());
 assertTrue(integrator.getStepHandlers().isEmpty());
 assertNotNull(integrator.getEventHandlers());
 assertTrue(integrator.getEventHandlers().isEmpty());
 assertFalse(integrator.isLastStep());
 assertFalse(integrator.resetOccurred);
 assertNull(integrator.getExpandable());
 assertEquals(-1, integrator.getMaxEvaluations());
 assertEquals(0, integrator.getEvaluations());
 }

 @Test
 public void testAddStepHandler() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void init(double t, double[] y, double tStep) {
 // initialize step handler
 }

 @Override
 public void handleStep(AbstractODEIntegrator integrator, boolean last) {
 // handle step
 }
 });
 assertEquals(1, integrator.getStepHandlers().size());
 }

 @Test
 public void testGetStepHandlers() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void init(double t, double[] y, double tStep) {
 // initialize step handler
 }

 @Override
 public void handleStep(AbstractODEIntegrator integrator, boolean last) {
 // handle step
 }
 });
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void init(double t, double[] y, double tStep) {
 // initialize step handler
 }

 @Override
 public void handleStep(AbstractODEIntegrator integrator, boolean last) {
 // handle step
 }
 });
 assertEquals(2, integrator.getStepHandlers().size());
 }

 @Test
 public void testClearStepHandlers() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void init(double t, double[] y, double tStep) {
 // initialize step handler
 }

 @Override
 public void handleStep(AbstractODEIntegrator integrator, boolean last) {
 // handle step
 }
 });
 integrator.addStepHandler(new StepHandler() {
 @Override
 public void init(double t, double[] y, double tStep) {
 // initialize step handler
 }

 @Override
 public void handleStep(AbstractODEIntegrator integrator, boolean last) {
 // handle step
 }
 });
 integrator.clearStepHandlers();
 assertTrue(integrator.getStepHandlers().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddStepHandler_NullHandler() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addStepHandler(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testAddEventHandler() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addEventHandler(new EventHandler() {
 @Override
 public boolean evaluatesEvent(double t, double[] y) {
 // evaluate event
 return true;
 }

 @Override
 public void handleEvent(double t, double[] y) {
 // handle event
 }
 }, 0.1, 1e-6, 5);
 assertEquals(1, integrator.getEventHandlers().size());
 }

 @Test
 public void testGetEventHandlers() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addEventHandler(new EventHandler() {
 @Override
 public boolean evaluatesEvent(double t, double[] y) {
 // evaluate event
 return true;
 }

 @Override
 public void handleEvent(double t, double[] y) {
 // handle event
 }
 }, 0.1, 1e-6, 5);
 integrator.addEventHandler(new EventHandler() {
 @Override
 public boolean evaluatesEvent(double t, double[] y) {
 // evaluate event
 return true;
 }

 @Override
 public void handleEvent(double t, double[] y) {
 // handle event
 }
 }, 0.2, 1e-6, 5);
 assertEquals(2, integrator.getEventHandlers().size());
 }

 @Test
 public void testClearEventHandlers() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addEventHandler(new EventHandler() {
 @Override
 public boolean evaluatesEvent(double t, double[] y) {
 // evaluate event
 return true;
 }

 @Override
 public void handleEvent(double t, double[] y) {
 // handle event
 }
 }, 0.1, 1e-6, 5);
 integrator.addEventHandler(new EventHandler() {
 @Override
 public boolean evaluatesEvent(double t, double[] y) {
 // evaluate event
 return true;
 }

 @Override
 public void handleEvent(double t, double[] y) {
 // handle event
 }
 }, 0.2, 1e-6, 5);
 integrator.clearEventHandlers();
 assertTrue(integrator.getEventHandlers().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddEventHandler_NullEventHandler() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.addEventHandler(null, 0.1, 1e-6, 5);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testGetCurrentStepStart() {
 integrator = new AbstractIntegrator("TestIntegrator");
 assertEquals(Double.NaN, integrator.getCurrentStepStart(), 0.0);
 }

 @Test
 public void testSetMaxEvaluations() {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.setMaxEvaluations(10);
 assertEquals(10, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetMaxEvaluations() {
 integrator = new AbstractIntegrator("TestIntegrator");
 assertEquals(-1, integrator.getMaxEvaluations());
 integrator.setMaxEvaluations(10);
 assertEquals(10, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 integrator = new AbstractIntegrator("TestIntegrator");
 assertEquals(0, integrator.getEvaluations());
 integrator.computeDerivatives(0.0, new double[0], new double[0]);
 assertEquals(1, integrator.getEvaluations());
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrate_MaxEvaluationsReached() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 super.integrate(equations, t);
 throw new MaxCountExceededException("Test MaxCountExceededException");
 }
 };
 integrator.setMaxEvaluations(1);
 FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(1);
 double[] y0 = new double[1];
 double[] y = new double[1];
 double tEnd = 1.0;
 try {
 integrator.integrate(equations, 0.0, y0, tEnd, y);
 fail("MaxCountExceededException expected but not thrown");
 } catch (MaxCountExceededException e) {
 // expected
 }
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrate_TimeTooSmall() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void sanityChecks(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException {
 throw new NumberIsTooSmallException("Test NumberIsTooSmallException");
 }
 };
 FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(1);
 double[] y0 = new double[1];
 double[] y = new double[1];
 double tEnd = 1e-6;
 try {
 integrator.integrate(equations, 0.0, y0, tEnd, y);
 fail("NumberIsTooSmallException expected but not thrown");
 } catch (NumberIsTooSmallException e) {
 // expected
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrate_DimensionMismatchException() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator = new AbstractIntegrator("TestIntegrator");
 integrator.setMaxEvaluations(1);
 FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(2);
 double[] y0 = new double[1];
 double[] y = new double[3];
 double tEnd = 1.0;
 try {
 integrator.integrate(equations, 0.0, y0, tEnd, y);
 fail("DimensionMismatchException expected but not thrown");
 } catch (DimensionMismatchException e) {
 // expected
 }
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrate_NoBracketingException() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator = new AbstractIntegrator("TestIntegrator") {
 @Override
 protected void acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd) throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 throw new NoBracketingException("Test NoBracketingException");
 }
 };
 FirstOrderDifferentialEquations equations = new DummyDifferentialEquations(1);
 double[] y0 = new double[1];
 double[] y = new double[1];
 double tEnd = 1.0;
 try {
 integrator.integrate(equations, 0.0, y0, tEnd, y);
 fail("NoBracketingException expected but not thrown");
 } catch (NoBracketingException e) {
 // expected
 }
 }

}