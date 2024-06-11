package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 @Test
 public void testEmbeddedRungeKuttaIntegrator() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{-0.236926885627273, -0.278465319046135, 0.236926885627273, 0.278465319046135},
 new double[][]{{0.3568763253188094, 0.0, 0.0, 0.0}, {-0.0, 0.3568763253188094, 0.0, 0.0}, {-0.3568763253188094, -0.3568763253188094, 0.312221683287087, 0.0}, {0.3568763253188094, -0.3568763253188094, -0.312221683287087, 0.3568763253188094}},
 new double[]{-0.0, 0.0},
 new MyRungeKuttaStepInterpolator(), 1.0e-12, 1.0e-12);

 ExpandableStatefulODE equations = new ExpandableStatefulODE() {
 @Override
 public void computeDerivatives(double t, double[] y) {
 y[0] = 2.0 * y[0];
 y[1] = 3.0 * y[1];
 }

 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public int getStateDimension() {
 return 2;
 }
 };

 double[] initialState = new double[]{1.0, 1.0};
 try {
 integrator.integrate(equations, initialState[0], initialState, 5.0);
 double[] finalState = equations.getCompleteState();
 assertEquals(new double[]{4.0, 12.0}, finalState, 0.001);
 } catch (Exception e) {
 fail("Integration failed: " + e.getMessage());
 }
 }

 @Test
 public void testEmbeddedRungeKuttaIntegratorWithAbsoluteTolerance() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{-0.236926885627273, -0.278465319046135, 0.236926885627273, 0.278465319046135},
 new double[][]{{0.3568763253188094, 0.0, 0.0, 0.0}, {-0.0, 0.3568763253188094, 0.0, 0.0}, {-0.3568763253188094, -0.3568763253188094, 0.312221683287087, 0.0}, {0.3568763253188094, -0.3568763253188094, -0.312221683287087, 0.3568763253188094}},
 new double[]{-0.0, 0.0},
 new MyRungeKuttaStepInterpolator(), 1.0e-10, new double[]{1.0e-8, 1.0e-8});

 ExpandableStatefulODE equations2 = new ExpandableStatefulODE() {
 @Override
 public void computeDerivatives(double t, double[] y) {
 y[0] = 2.0 * y[0];
 y[1] = 3.0 * y[1];
 }

 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public int getStateDimension() {
 return 2;
 }
 };

 double[] initialState2 = new double[]{1.0, 1.0};
 try {
 integrator.integrate(equations2, initialState2[0], initialState2, 5.0);
 double[] finalState2 = equations2.getCompleteState();
 assertEquals(new double[]{4.0, 12.0}, finalState2, 0.001);
 } catch (Exception e) {
 fail("Integration failed: " + e.getMessage());
 }
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testNumberIsTooSmallException() {
 EmbeddedRungeKuttaIntegrator integrator = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{-0.236926885627273, -0.278465319046135, 0.236926885627273, 0.278465319046135},
 new double[][]{{0.3568763253188094, 0.0, 0.0, 0.0}, {-0.0, 0.3568763253188094, 0.0, 0.0}, {-0.3568763253188094, -0.3568763253188094, 0.312221683287087, 0.0}, {0.3568763253188094, -0.3568763253188094, -0.312221683287087, 0.3568763253188094}},
 new double[]{-0.0, 0.0},
 new MyRungeKuttaStepInterpolator(), 0.0, 1.0e-12);

 ExpandableStatefulODE equations3 = new ExpandableStatefulODE() {
 @Override
 public void computeDerivatives(double t, double[] y) {
 y[0] = 2.0 * y[0];
 y[1] = 3.0 * y[1];
 }

 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public int getStateDimension() {
 return 2;
 }
 };

 double[] initialState3 = new double[]{1.0, 1.0};
 try {
 integrator.integrate(equations3, initialState3[0], initialState3, 0.1);
 fail("NumberIsTooSmallException expected");
 } catch (NumberIsTooSmallException e) {
 // Expected
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testDimensionMismatchException() {
 ExpandableStatefulODE equations4 = new ExpandableStatefulODE() {
 @Override
 public void computeDerivatives(double t, double[] y) {
 y[0] = 2.0 * y[0];
 y[1] = 3.0 * y[1];
 y[2] = 4.0 * y[1]; // Wrong state dimension
 }

 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public int getStateDimension() {
 return 3;
 }
 };

 EmbeddedRungeKuttaIntegrator integrator2 = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{-0.236926885627273, -0.278465319046135, 0.236926885627273, 0.278465319046135},
 new double[][]{{0.3568763253188094, 0.0, 0.0, 0.0}, {-0.0, 0.3568763253188094, 0.0, 0.0}, {-0.3568763253188094, -0.3568763253188094, 0.312221683287087, 0.0}, {0.3568763253188094, -0.3568763253188094, -0.312221683287087, 0.3568763253188094}},
 new double[]{-0.0, 0.0},
 new MyRungeKuttaStepInterpolator(), 1.0e-12, 1.0e-12);

 double[] initialState4 = new double[]{1.0, 1.0};
 try {
 integrator2.integrate(equations4, initialState4[0], initialState4, 5.0);
 fail("DimensionMismatchException expected");
 } catch (DimensionMismatchException e) {
 // Expected
 }
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() {
 ExpandableStatefulODE equations5 = new ExpandableStatefulODE() {
 @Override
 public void computeDerivatives(double t, double[] y) {
 y[0] = 1.0 / (1.0 + t); // This equation doesn't converge
 }

 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public int getStateDimension() {
 return 1;
 }
 };

 EmbeddedRungeKuttaIntegrator integrator3 = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{-0.236926885627273, -0.278465319046135, 0.236926885627273, 0.278465319046135},
 new double[][]{{0.3568763253188094, 0.0, 0.0, 0.0}, {-0.0, 0.3568763253188094, 0.0, 0.0}, {-0.3568763253188094, -0.3568763253188094, 0.312221683287087, 0.0}, {0.3568763253188094, -0.3568763253188094, -0.312221683287087, 0.3568763253188094}},
 new double[]{-0.0, 0.0},
 new MyRungeKuttaStepInterpolator(), 1.0e-12, 1.0e-12, 1000);

 double[] initialState5 = new double[]{1.0};
 try {
 integrator3.integrate(equations5, initialState5[0], initialState5, 10.0);
 fail("MaxCountExceededException expected");
 } catch (MaxCountExceededException e) {
 // Expected
 }
 }

 @Test(expected = NoBracketingException.class)
 public void testNoBracketingException() {
 ExpandableStatefulODE equations6 = new ExpandableStatefulODE() {
 @Override
 public void computeDerivatives(double t, double[] y) {
 y[0] = Math.sqrt(Math.abs(y[0]));
 }

 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public int getStateDimension() {
 return 1;
 }
 };

 EmbeddedRungeKuttaIntegrator integrator4 = new MyEmbeddedRungeKuttaIntegrator("MyIntegrator", true, new double[]{-0.236926885627273, -0.278465319046135, 0.236926885627273, 0.278465319046135},
 new double[][]{{0.3568763253188094, 0.0, 0.0, 0.0}, {-0.0, 0.3568763253188094, 0.0, 0.0}, {-0.3568763253188094, -0.3568763253188094, 0.312221683287087, 0.0}, {0.3568763253188094, -0.3568763253188094, -0.312221683287087, 0.3568763253188094}},
 new double[]{-0.0, 0.0},
 new MyRungeKuttaStepInterpolator(), 1.0e-12, 1.0e-12);

 double[] initialState6 = new double[]{1.0};
 try {
 integrator4.integrate(equations6, initialState6[0], initialState6, -1.0);
 fail("NoBracketingException expected");
 } catch (NoBracketingException e) {
 // Expected
 }
 }
}

class MyEmbeddedRungeKuttaIntegrator extends EmbeddedRungeKuttaIntegrator {
 // Empty implementation
}

class MyRungeKuttaStepInterpolator extends StepInterpolator {
 // Empty implementation
}