package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.util.Precision;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double EPSILON = 1e-10;
 private static final double ACCURACY = 1e-6;

 @Test
 public void testEmbeddedRungeKuttaIntegratorConstructor() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{
 {0.25, 0}, {-0.25, 0.25}, {0, 0.25}, {0.25, -0.25}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator() {});
 assertEquals("RK4", integrator.getName());
 assertFalse(integrator.isFSAL());
 assertEquals(4, integrator.getOrder());
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorConstructor2() {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF45", false, new double[]{0.25, 0.25, 0.25, 0.25, 0.1}, new double[][]{
 {0.25, 0}, {-0.25, 0.25}, {0, 0.25}, {0.25, -0.25}, {-0.2, 0.15}}, new double[][]{
 {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}}, new RungeKuttaStepInterpolator() {});
 assertEquals("RKF45", integrator.getName());
 assertFalse(integrator.isFSAL());
 assertEquals(5, integrator.getOrder());
 }

 @Test
 public void testInitializeStep() throws NoBracketingException {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{
 {0.25, 0}, {-0.25, 0.25}, {0, 0.25}, {0.25, -0.25}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator() {});
 double h = integrator.initializeStep(true, 4, null, 0.0, new double[]{0.0}, new double[]{0.0}, new double[]{0.0}, new double[]{0.0});
 assertEquals(0.01, h, ACCURACY);
 }

 @Test
 public void testInitializeStep2() throws NoBracketingException {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF45", false, new double[]{0.25, 0.25, 0.25, 0.25, 0.1}, new double[][]{
 {0.25, 0}, {-0.25, 0.25}, {0, 0.25}, {0.25, -0.25}, {-0.2, 0.15}}, new double[][]{
 {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}}, new RungeKuttaStepInterpolator() {});
 double h = integrator.initializeStep(false, 5, null, 0.0, new double[]{0.0}, new double[]{0.0}, new double[]{0.0}, new double[]{0.0});
 assertEquals(0.02, h, ACCURACY);
 }

 @Test
 public void testEstimateError() throws NoBracketingException {
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{
 {0.25, 0}, {-0.25, 0.25}, {0, 0.25}, {0.25, -0.25}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator() {});
 double[] y0 = new double[]{0.0};
 double[] y1 = new double[]{1.0};
 double[] y2 = new double[]{1.1};
 double error = integrator.estimateError(new double[][]{y0, y1, y2}, y0, y2, 0.1);
 assertEquals(1.0e-6, error, EPSILON);
 }

 @Test
 public void testIntegrate() throws NoBracketingException, DimensionMismatchException, MaxCountExceededException, NumberIsTooSmallException {
 ExpandableStatefulODE equations = new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public void initialize(double time) {
 this.setTime(time);
 this.setCompleteState(new double[]{0.0});
 }

 @Override
 public void computeDerivatives(double time, double[] y) {
 y[0] = y[0] * 0.9;
 }

 @Override
 public double getTime() {
 return this.time;
 }

 @Override
 public void setTime(double time) {
 this.time = time;
 }

 @Override
 public double[] getCompleteState() {
 return this.y;
 }

 @Override
 public void setCompleteState(double[] y) {
 this.y = y;
 }

 private double time;
 private double[] y;
 };
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RK4", true, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{
 {0.25, 0}, {-0.25, 0.25}, {0, 0.25}, {0.25, -0.25}}, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator() {});
 double[] y = new double[]{0.0};
 integrator.integrate(equations, 1.0);
 assertEquals(0.6, equations.getCompleteState()[0], ACCURACY);
 }

 @Test
 public void testIntegrate2() throws NoBracketingException, DimensionMismatchException, MaxCountExceededException, NumberIsTooSmallException {
 ExpandableStatefulODE equations = new ExpandableStatefulODE() {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public void initialize(double time) {
 this.setTime(time);
 this.setCompleteState(new double[]{0.0, 0.0});
 }

 @Override
 public void computeDerivatives(double time, double[] y) {
 y[0] = y[0] * 0.9;
 y[1] = y[1] * 0.8;
 }

 @Override
 public double getTime() {
 return this.time;
 }

 @Override
 public void setTime(double time) {
 this.time = time;
 }

 @Override
 public double[] getCompleteState() {
 return this.y;
 }

 @Override
 public void setCompleteState(double[] y) {
 this.y = y;
 }

 private double time;
 private double[] y;
 };
 EmbeddedRungeKuttaIntegrator integrator = new EmbeddedRungeKuttaIntegrator("RKF45", false, new double[]{0.25, 0.25, 0.25, 0.25, 0.1}, new double[][]{
 {0.25, 0}, {-0.25, 0.25}, {0, 0.25}, {0.25, -0.25}, {-0.2, 0.15}}, new double[][]{
 {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}, {0.25, 0.25, 0.25, 0.25, 0.25}}, new RungeKuttaStepInterpolator() {});
 double[] y = new double[]{0.0, 0.0};
 integrator.integrate(equations, 1.5);
 assertEquals(new double[]{0.435312028081903, 0.249424448592907}, equations.getCompleteState(), Precision.SAFE_EPSILON);
 }
}