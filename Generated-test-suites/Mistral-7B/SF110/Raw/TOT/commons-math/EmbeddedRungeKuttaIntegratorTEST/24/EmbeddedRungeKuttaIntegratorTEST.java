package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 @Test
 public void testRK4Integrator() {
 EmbeddedRungeKuttaIntegrator rk4 = new RK4Integrator("RK4", false, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25}, {0, 0.5}, {-0.25, 0.25}, {0.25, -0.25}, {0.5, 0}}, new RK4Method(), 0.01, 0.01, null, null);

 // Test with simple equation
 ExpandableStatefulODE equations = new DAEFunction("y' = x", new ODEFunction() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public double value(double t, double[] y) {
 return y[0];
 }

 @Override
 public double[] derivative(double t, double[] y) {
 return new double[]{y[0]};
 }
 });
 double[] y0 = new double[]{1.0};
 double tEnd = 5.0;

 try {
 rk4.integrate(equations, tEnd);
 double[] yFinal = equations.getCompleteState();
 assertEquals(Math.exp(5.0), yFinal[0], 1e-6);
 } catch (NumberIsTooSmallException | DimensionMismatchException | MaxCountExceededException | NoBracketingException e) {
 fail("Integration should have succeeded.");
 }

 // Test with non-linear equation
 equations = new DAEFunction("y' = x^2", new ODEFunction() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public double value(double t, double[] y) {
 return y[0] * y[0];
 }

 @Override
 public double[] derivative(double t, double[] y) {
 return new double[]{2.0 * y[0]};
 }
 });
 y0 = new double[]{1.0};
 tEnd = 5.0;

 try {
 rk4.integrate(equations, tEnd);
 double[] yFinal = equations.getCompleteState();
 assertEquals(Math.exp(5.0) * 5.0, yFinal[0], 1e-6);
 } catch (NumberIsTooSmallException | DimensionMismatchException | MaxCountExceededException | NoBracketingException e) {
 fail("Integration should have succeeded.");
 }

 // Test with step size adjustment
 rk4 = new RK4Integrator("RK4", false, new double[]{0.25, 0.25, 0.25, 0.25}, new double[][]{{0.25, 0.25}, {0, 0.5}, {-0.25, 0.25}, {0.25, -0.25}, {0.5, 0}}, new RK4Method(), 0.01, 0.01, new double[]{1e-6, 1e-6}, new double[]{1e-6, 1e-6});

 equations = new DAEFunction("y' = x^2", new ODEFunction() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public double value(double t, double[] y) {
 return y[0] * y[0];
 }

 @Override
 public double[] derivative(double t, double[] y) {
 return new double[]{2.0 * y[0]};
 }
 });
 y0 = new double[]{1.0};
 tEnd = 5.0;

 try {
 rk4.integrate(equations, tEnd);
 double[] yFinal = equations.getCompleteState();
 assertEquals(Math.exp(5.0) * 5.0, yFinal[0], 1e-6);
 } catch (NumberIsTooSmallException | DimensionMismatchException | MaxCountExceededException | NoBracketingException e) {
 fail("Integration should have succeeded.");
 }

 // Test with wrong equation dimensions
 equations = new DAEFunction("y' = x^2", new ODEFunction() {
 @Override
 public int getDimension() {
 return 2; // wrong dimension
 }

 @Override
 public double value(double t, double[] y) {
 return y[0] * y[0];
 }

 @Override
 public double[] derivative(double t, double[] y) {
 return new double[]{2.0 * y[0], 0.0};
 }
 });
 y0 = new double[]{1.0, 0.0};
 tEnd = 5.0;

 try {
 rk4.integrate(equations, tEnd);
 fail("Integration should have thrown an exception.");
 } catch (DimensionMismatchException e) {
 // Expected exception
 }
 }

 public static class DAEFunction implements ExpandableStatefulODE {
 private final String equation;
 private final ODEFunction func;

 public DAEFunction(String equation, ODEFunction func) {
 this.equation = equation;
 this.func = func;
 }

 @Override
 public int getDimension() {
 return func.getDimension();
 }

 @Override
 public String getEquationName() {
 return equation;
 }

 @Override
 public ODEFunction getFunction(double t) {
 return func;
 }

 @Override
 public double value(double t, double[] y) {
 return func.value(t, y);
 }

 @Override
 public double[] derivative(double t, double[] y) {
 return func.derivative(t, y);
 }
 }
}