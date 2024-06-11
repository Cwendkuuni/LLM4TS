package org.apache.commons.math3.ode;

import static org.junit.Assert.*;
import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.ode.events.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.util.*;
import org.junit.Test;

public class AbstractIntegratorTest {

 AbstractIntegrator integrator = null;
 FirstOrderDifferentialEquations equations = null;

 @BeforeClass
 public static void setupClass() {
 integrator = new AbstractODEIntegrator("TestIntegrator") {
 @Override
 public void integrate(ExpandableStatefulODE p0, double p1) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 // Place your implementation here
 }
 };
 }

 @Before
 public void setUp() {
 equations = new AbstractDifferentialEquations() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 yDot[0] = y[0] * 0.1;
 }
 };
 }

 @Test
 public void testAddStepHandler() {
 integrator.addStepHandler(new StepInterpolatorAdapter() {
 @Override
 public double acceptStep(AbstractODEIntegrator integrator, double[] y, double[] yDot, double tEnd) throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 fail("Should not be called");
 }
 });
 integrator.addStepHandler(new StepInterpolatorAdapter() {
 @Override
 public double acceptStep(AbstractODEIntegrator integrator, double[] y, double[] yDot, double tEnd) throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 assertEquals(0.0, y[0], 1e-9);
 return 1.0;
 }
 });

 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.computeDerivatives(0.0, new double[]{0.0}, new double[]{});
 double result = integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[]{});
 assertEquals(1.0, result, 1e-9);
 }

 @Test
 public void testClearStepHandlers() {
 integrator.addStepHandler(new StepInterpolatorAdapter() {
 @Override
 public double acceptStep(AbstractODEIntegrator integrator, double[] y, double[] yDot, double tEnd) throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
 fail("Should not be called");
 }
 });
 integrator.clearStepHandlers();
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.computeDerivatives(0.0, new double[]{0.0}, new double[]{});
 double result = integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[]{});
 assertEquals(1.0, result, 1e-9);
 }

 @Test
 public void testAddEventHandler() {
 integrator.addEventHandler(new AbstractEventAdapter() {
 @Override
 public void init(double t0, double[] y0, double t) {
 // Place your implementation here
 }
 }, 0.0, 1.0e-9, 10);
 integrator.addEventHandler(new AbstractEventAdapter() {
 @Override
 public void init(double t0, double[] y0, double t) {
 // Place your implementation here
 }
 }, 0.5, 1.0e-9, 10);
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.computeDerivatives(0.0, new double[]{0.0}, new double[]{});
 double result = integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[]{});
 assertEquals(1.0, result, 1e-9);
 }

 @Test
 public void testClearEventHandlers() {
 integrator.addEventHandler(new AbstractEventAdapter() {
 @Override
 public void init(double t0, double[] y0, double t) {
 // Place your implementation here
 }
 }, 0.0, 1.0e-9, 10);
 integrator.addEventHandler(new AbstractEventAdapter() {
 @Override
 public void init(double t0, double[] y0, double t) {
 // Place your implementation here
 }
 }, 0.5, 1.0e-9, 10);
 integrator.clearEventHandlers();
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.computeDerivatives(0.0, new double[]{0.0}, new double[]{});
 double result = integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[]{});
 assertEquals(1.0, result, 1e-9);
 }

 @Test
 public void testGetName() {
 assertEquals("TestIntegrator", integrator.getName());
 }

 @Test
 public void testGetStepHandlers() {
 assertEquals(2, integrator.getStepHandlers().size());
 }

 @Test
 public void testGetEventHandlers() {
 integrator.addEventHandler(new AbstractEventAdapter() {
 @Override
 public void init(double t0, double[] y0, double t) {
 // Place your implementation here
 }
 }, 0.0, 1.0e-9, 10);
 integrator.addEventHandler(new AbstractEventAdapter() {
 @Override
 public void init(double t0, double[] y0, double t) {
 // Place your implementation here
 }
 }, 0.5, 1.0e-9, 10);
 assertEquals(2, integrator.getEventHandlers().size());
 }

 @Test
 public void testGetMaxEvaluations() {
 assertEquals(-1, integrator.getMaxEvaluations());
 }

 @Test
 public void testSetMaxEvaluations() {
 integrator.setMaxEvaluations(10);
 assertEquals(10, integrator.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 integrator.evaluations.resetCount();
 integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[]{});
 assertEquals(1, integrator.getEvaluations());
 }

 @Test
 public void testIntegrate() throws NoBracketingException, DimensionMismatchException, MaxCountExceededException, NumberIsTooSmallException {
 integrator.evaluations.resetCount();
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[]{});
 assertEquals(1, integrator.getEvaluations());
 double result = integrator.getExpandable().getTime();
 assertEquals(1.0, result, 1e-9);
 }

 @Test
 public void testResetOccurred() {
 integrator.addEventHandler(new AbstractEventAdapter() {
 @Override
 public void init(double t0, double[] y0, double t) {
 integrator.resetOccurred = true;
 }
 }, 0.5, 1.0e-9, 10);
 integrator.initIntegration(0.0, new double[]{0.0}, 1.0);
 integrator.computeDerivatives(0.0, new double[]{0.0}, new double[]{});
 integrator.integrate(equations, 0.0, new double[]{0.0}, 1.0, new double[]{});
 assertTrue(integrator.resetOccurred);
 reset(integrator.resetOccurred);
 }
}