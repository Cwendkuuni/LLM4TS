package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double DELTA = 1e-12;
 private static final double EPSILON = 1e-15;

 @Test
 public void testEmbeddedRungeKuttaIntegrator_constructor() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, new double[]{0.5, 0.3, 0.2, 0.1, 0.1}, new double[][]{{0.5, 0, 0.5, 0, 0.5}, {0, 0.5, 0.5, 0.5, 0.5}, {0, 0, 0.5, 1.0, 0.5}, {0, 0, 0, 0.5, 1.0}, {0, 0, 0, 0, 1.0}}, new RungeKuttaStepInterpolator(new RungeKuttaMethod("TestMethod", 5)), 1.0e-12, 1.0e12, 1.0e-12, 1.0e-8);
 assertNotNull(integrator);
 assertEquals("TestIntegrator", integrator.getName());
 assertFalse(integrator.isFSAL());
 assertArrayEquals(new double[]{0.5, 0.3, 0.2, 0.1, 0.1}, integrator.getC(), DELTA);
 assertArrayEquals(new double[][]{{0.5, 0, 0.5, 0, 0.5}, {0, 0.5, 0.5, 0.5, 0.5}, {0, 0, 0.5, 1.0, 0.5}, {0, 0, 0, 0.5, 1.0}, {0, 0, 0, 0, 1.0}}, integrator.getA(), DELTA);
 assertArrayEquals(new double[]{0.5, 0.5, 0.5, 0.5, 1.0}, integrator.getB(), DELTA);
 assertTrue(integrator.prototype instanceof RungeKuttaStepInterpolator);
 assertEquals(-1.0 / 5.0, integrator.exp, EPSILON);
 assertEquals(0.9, integrator.getSafety(), EPSILON);
 assertEquals(0.2, integrator.getMinReduction(), EPSILON);
 assertEquals(10.0, integrator.getMaxGrowth(), EPSILON);
 }

 @Test
 public void testEmbeddedRungeKuttaIntegrator_constructor_vectors() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("TestIntegrator", false, new double[]{0.5, 0.3, 0.2, 0.1, 0.1}, new double[][]{{0.5, 0, 0.5, 0, 0.5}, {0, 0.5, 0.5, 0.5, 0.5}, {0, 0, 0.5, 1.0, 0.5}, {0, 0, 0, 0.5, 1.0}, {0, 0, 0, 0, 1.0}}, new RungeKuttaStepInterpolator(new RungeKuttaMethod("TestMethod", 5)), new double[]{1.0e-12, 1.0e-12, 1.0e-12, 1.0e-12, 1.0e-12}, new double[]{1.0e-8, 1.0e-8, 1.0e-8, 1.0e-8, 1.0e-8});
 assertNotNull(integrator);
 assertEquals("TestIntegrator", integrator.getName());
 assertFalse(integrator.isFSAL());
 assertArrayEquals(new double[]{0.5, 0.3, 0.2, 0.1, 0.1}, integrator.getC(), DELTA);
 assertArrayEquals(new double[][]{{0.5, 0, 0.5, 0, 0.5}, {0, 0.5, 0.5, 0.5, 0.5}, {0, 0, 0.5, 1.0, 0.5}, {0, 0, 0, 0.5, 1.0}, {0, 0, 0, 0, 1.0}}, integrator.getA(), DELTA);
 assertArrayEquals(new double[]{0.5, 0.5, 0.5, 0.5, 1.0}, integrator.getB(), DELTA);
 assertTrue(integrator.prototype instanceof RungeKuttaStepInterpolator);
 assertEquals(-1.0 / 5.0, integrator.exp, EPSILON);
 assertEquals(0.9, integrator.getSafety(), EPSILON);
 for (int i = 0; i < integrator.getVectorAbsoluteTolerances().length; i++) {
 assertEquals(1.0e-8, integrator.getVectorAbsoluteTolerance(i), EPSILON);
 }
 for (int i = 0; i < integrator.getVectorRelativeTolerances().length; i++) {
 assertEquals(1.0e-8, integrator.getVectorRelativeTolerance(i), EPSILON);
 }
 }
}