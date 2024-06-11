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
import org.apache.commons.math3.ode.StepHandler;
import org.apache.commons.math3.ode.event.*;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.Precision;
import org.junit.*;

public class AbstractIntegratorTest {

 private AbstractIntegrator integrator;
 private FirstOrderDifferentialEquations equations;

 @Before
 public void setUp() {
 integrator = new DummyIntegrator();
 equations = new DummyDifferentialEquations();
 }

 @Test
 public void testIntegrate() throws Exception {
 // Test integration with no events or step handlers
 final double[] y = new double[equations.getDimension()];
 integrator.initIntegration(0.0, y, 1.0);
 integrator.integrate(equations, 0.0, y, 1.0, y);
 assertEquals(1.0, integrator.getCurrentStepStart(), 1e-12);

 // Test integration with an event
 integrator = new DummyIntegrator();
 integrator.addEventHandler(new DummyEvent(), 0.5, 1e-6, 10);
 integrator.initIntegration(0.0, y, 1.0);
 integrator.integrate(equations, 0.0, y, 1.0, y);
 assertEquals(0.5, integrator.getCurrentStepStart(), 1e-12);

 // Test integration with a step handler
 integrator = new DummyIntegrator();
 integrator.addStepHandler(new DummyStepHandler());
 integrator.initIntegration(0.0, y, 1.0);
 integrator.integrate(equations, 0.0, y, 1.0, y);
 assertTrue(integrator.getStepHandlers().contains(integrator.getStepHandlers().get(0)));

 // Test integration with an event and a step handler
 integrator = new DummyIntegrator();
 integrator.addEventHandler(new DummyEvent(), 0.5, 1e-6, 10);
 integrator.addStepHandler(new DummyStepHandler());
 integrator.initIntegration(0.0, y, 1.0);
 integrator.integrate(equations, 0.0, y, 1.0, y);
 assertEquals(0.5, integrator.getCurrentStepStart(), 1e-12);
 assertTrue(integrator.getStepHandlers().contains(integrator.getStepHandlers().get(0)));
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatchException() throws Exception {
 integrator = new DummyIntegrator();
 integrator.initIntegration(0.0, new double[1], 1.0);
 integrator.integrate(equations, 0.0, new double[2], 1.0, new double[2]);
 fail("DimensionMismatchException not thrown");
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() throws Exception {
 integrator = new DummyIntegrator();
 integrator.setMaxEvaluations(0);
 integrator.initIntegration(0.0, y, 1.0);
 integrator.integrate(equations, 0.0, y, 1.0, y);
 fail("MaxCountExceededException not thrown");
 }

 @Test(expected = NoBracketingException.class)
 public void testNoBracketingException() throws Exception {
 integrator = new DummyIntegrator();
 integrator.setMaxEvaluations(1);
 integrator.initIntegration(0.0, y, 1.0);
 integrator.integrate(new DummyDifferentialEquations() {
 @Override
 public void computeDerivatives(double t, double[] y, double[] yDot) {
 yDot[0] = y[1];
 yDot[1] = -y[0];
 }
 }, 0.0, y, 1.0, y);
 fail("NoBracketingException not thrown");
 }

 @Test
 public void testAcceptStep() throws Exception {
 // Test acceptance of a step
 integrator = new DummyIntegrator();
 integrator.addEventHandler(new DummyEvent(), 0.5, 1e-6, 10);
 integrator.addStepHandler(new DummyStepHandler());
 StepInterpolator interpolator = new StepInterpolator(new RectangularStepInterpolator());
 final double[] y0 = new double[] { 0.1, 0.2 };
 integrator.initIntegration(0.0, y0, 1.0);
 integrator.integrate(equations, 0.0, y0, 0.5, y0);
 interpolator.setGlobalPreviousTime(0.0);
 interpolator.setGlobalCurrentTime(0.5);
 integrator.acceptStep(interpolator, y0, null, 0.6);
 assertEquals(0.6, integrator.getCurrentStepStart(), 1e-12);

 // Test acceptance of an event
 integrator = new DummyIntegrator();
 integrator.addEventHandler(new DummyEvent(), 0.5, 1e-6, 10);
 integrator.addStepHandler(new DummyStepHandler());
 interpolator = new StepInterpolator(new RectangularStepInterpolator());
 final double[] y1 = new double[] { 0.1, 0.2 };
 integrator.initIntegration(0.0, y1, 1.0);
 integrator.integrate(equations, 0.0, y1, 0.5, y1);
 interpolator.setGlobalPreviousTime(0.0);
 interpolator.setGlobalCurrentTime(0.5);
 integrator.acceptStep(interpolator, y1, null, 0.5);
 assertEquals(0.5, integrator.getCurrentStepStart(), 1e-12);

 // Test acceptance of a reset
 integrator = new DummyIntegrator();
 integrator.addEventHandler(new DummyEvent(), 0.5, 1e-6, 10);
 integrator.addStepHandler(new DummyStepHandler());
 interpolator = new StepInterpolator(new RectangularStepInterpolator());
 final double[] y2 = new double[] { 0.1, 0.2 };
 integrator.initIntegration(0.0, y2, 1.0);
 integrator.integrate(equations, 0.0, y2, 0.5, y2);
 interpolator.setGlobalPreviousTime(0.0);
 interpolator.setGlobalCurrentTime(0.5);
 integrator.acceptStep(interpolator, y2, null, 0.5);
 assertEquals(0.5, integrator.getCurrentStepStart(), 1e-12);
 integrator.resetOccurred = false;
 integrator.acceptStep(interpolator, y2, null, 0.5);
 assertFalse(integrator.resetOccurred);
 integrator.resetOccurred = false;
 integrator.acceptStep(interpolator, y2, null, 0.6);
 assertTrue(integrator.resetOccurred);
 }
}