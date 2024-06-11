package org.apache.commons.math3.ode.nonstiff.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;

public class EmbeddedRungeKuttaIntegratorTest {

 private EmbeddedRungeKuttaIntegrator integrator;
 private ExpandableStatefulODE equations;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 final double[] c = { 0.3, 0.4, 0.5 };
 final double[][] a = { { 0.0, 0.4, 0.0 }, { 0.0, 0.0, 0.5 }, { 0.5, 0.0, 0.0 } };
 final double[] b = { 0.2, 0.0, 0.4 };
 final RungeKuttaStepInterpolator prototype = new RungeKuttaFehlberg78Method();
 integrator = new EmbeddedRungeKuttaIntegrator() {
 @Override
 public int getOrder() {
 return 7;
 }
 }.init( "Embedded Runge-Kutta Integrator", true, c, a, b, prototype, 0.001, 0.01, 1.0e-12, 1.0e-12 );
 equations = new SimpleDifferentialEquations( "Equations" ) {
 @Override
 public int getDimension() {
 return 3;
 }
 @Override
 public void computeDerivatives(double time, double[] y, double[] yPrime) {
 yPrime[0] = y[1];
 yPrime[1] = y[2];
 yPrime[2] = -y[0] - y[1] - 0.1 * y[2];
 }
 };
 }

 @Test
 public void testIntegrate() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 final double tFinal = 5.0;
 final double[] y0 = { 1.0, 1.0, 1.0 };
 integrator.integrate(equations, tFinal);
 assertArrayEquals(new double[] { 4.550229379681256, 4.560381212753757, 3.524858135838859 }, equations.getCompleteState(), 1e-8);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testIntegrateWithSmallStep() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 integrator.setMinStep(1e-10);
 integrator.integrate(equations, 1.0);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testIntegrateWithMismatchedDimension() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 final SimpleDifferentialEquations mismatchedEquations = new SimpleDifferentialEquations( "Mismatched Equations" ) {
 @Override
 public int getDimension() {
 return 4;
 }
 };
 integrator.integrate(mismatchedEquations, 1.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testIntegrateWithMaxCountExceeded() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 final ExpandableStatefulODE maxCountEquations = new MaxCountEquations( "Max Count Equations", 100000 );
 integrator.integrate(maxCountEquations, 1.0);
 }

 @Test(expected = NoBracketingException.class)
 public void testIntegrateWithNoBracketing() throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
 final ExpandableStatefulODE noBracketingEquations = new NoBracketingEquations( "No Bracketing Equations", 1.0, 2.0 );
 integrator.integrate(noBracketingEquations, 1.5);
 }

 @Test
 public void testGetSafety() {
 assertEquals(0.9, integrator.getSafety(), 1e-12);
 }

 @Test
 public void testSetSafety() {
 integrator.setSafety(0.8);
 assertEquals(0.8, integrator.getSafety(), 1e-12);
 }

 @Test
 public void testGetMinReduction() {
 assertEquals(0.2, integrator.getMinReduction(), 1e-12);
 }

 @Test
 public void testSetMinReduction() {
 integrator.setMinReduction(0.3);
 assertEquals(0.3, integrator.getMinReduction(), 1e-12);
 }

 @Test
 public void testGetMaxGrowth() {
 assertEquals(10.0, integrator.getMaxGrowth(), 1e-12);
 }

 @Test
 public void testSetMaxGrowth() {
 integrator.setMaxGrowth(5.0);
 assertEquals(5.0, integrator.getMaxGrowth(), 1e-12);
 }
}