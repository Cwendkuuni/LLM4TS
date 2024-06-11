package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.sampling.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 private static final double EPSILON = 1e-12;
 private static final double ABS_TOL = 1e-12;
 private static final double REL_TOL = 1e-6;

 @Test
 public void testRk4Integrator() throws Exception {
 EmbeddedRungeKuttaIntegrator rk4 = new Rk4MethodIntegrator("Rk4", true, new double[]{0.5, 0.25, 0.25, 0.25, 0}, new double[][]{
      {0.5, 0}, {0, 0.5}, {0, 0.5}, {0, 0.5}, {0, 0.5}
 }, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator() {
 @Override
 public double[] interpolate(double[] y, double[] yDot, int index, double time, double[] derivatives, double[] secondary) {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public void reinitialize(ExpandableStatefulODE equations, double[] y, double[] yDot, boolean forward, ODEFunctionMapper primaryMapper, ODEFunctionMapper[] secondaryMappers) {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public void storeTime(double time) {
 throw new UnsupportedOperationException("Not supported yet.");
 }
 });

 ExpandableStatefulODE equations = new DummyODE() {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public double[] getCompleteState(double time) {
 return new double[]{Math.exp(time), Math.sin(time)};
 }

 @Override
 public double[] getDerivatives(double time, double[] state) {
 return new double[]{state[0] * (1 + time), state[1] * Math.cos(time)};
 }
 };

 rk4.integrate(equations, 0.0, 1.0);
 double[] actual = equations.getCompleteState();
 double[] expected = new double[]{Math.E, Math.sin(1.0)};
 assertArrayEquals(expected, actual, EPSILON);
 }

 @Test
 public void testRkFalc03Integrator() throws Exception {
 EmbeddedRungeKuttaIntegrator rkFalc03 = new RkFalc03MethodIntegrator("RkFalc03", true, new double[]{0.3, 0, 0.2, 0.2, 0.2, 0.2, 0.1}, new double[][]{
      {0.3, 0}, {0, 0.3}, {0, 0.3}, {0, 0.3}, {0, 0.3}, {0, 0.3}, {0, 0.3}
 }, new double[]{0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.1}, new RungeKuttaStepInterpolator() {
 @Override
 public double[] interpolate(double[] y, double[] yDot, int index, double time, double[] derivatives, double[] secondary) {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public void reinitialize(ExpandableStatefulODE equations, double[] y, double[] yDot, boolean forward, ODEFunctionMapper primaryMapper, ODEFunctionMapper[] secondaryMappers) {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public void storeTime(double time) {
 throw new UnsupportedOperationException("Not supported yet.");
 }
 });

 ExpandableStatefulODE equations2 = new DummyODE() {
 @Override
 public int getDimension() {
 return 2;
 }

 @Override
 public double[] getCompleteState(double time) {
 return new double[]{Math.exp(time), Math.sin(time)};
 }

 @Override
 public double[] getDerivatives(double time, double[] state) {
 return new double[]{state[0] * (1 + time), state[1] * Math.cos(time)};
 }
 };

 rkFalc03.integrate(equations2, 0.0, 1.0);
 double[] actual2 = equations2.getCompleteState();
 double[] expected2 = new double[]{Math.E, Math.sin(1.0)};
 assertArrayEquals(expected2, actual2, EPSILON);
 }

 @Test(expected = NoBracketingException.class)
 public void testNoBracketingException() throws Exception {
 ExpandableStatefulODE equations3 = new DummyODE() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public double[] getCompleteState(double time) {
 return new double[]{Math.sin(time)};
 }

 @Override
 public double[] getDerivatives(double time, double[] state) {
 return new double[]{Math.cos(time)};
 }
 };

 EmbeddedRungeKuttaIntegrator rk4_noBracketing = new Rk4MethodIntegrator("Rk4_noBracketing", true, new double[]{0.5, 0.25, 0.25, 0.25, 0}, new double[][]{
      {0.5, 0}, {0, 0.5}, {0, 0.5}, {0, 0.5}, {0, 0.5}
 }, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator() {
 @Override
 public double[] interpolate(double[] y, double[] yDot, int index, double time, double[] derivatives, double[] secondary) {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public void reinitialize(ExpandableStatefulODE equations, double[] y, double[] yDot, boolean forward, ODEFunctionMapper primaryMapper, ODEFunctionMapper[] secondaryMappers) {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public void storeTime(double time) {
 throw new UnsupportedOperationException("Not supported yet.");
 }
 });

 rk4_noBracketing.integrate(equations3, 0.0, Math.PI);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() throws Exception {
 ExpandableStatefulODE equations4 = new DummyODE() {
 @Override
 public int getDimension() {
 return 1;
 }

 @Override
 public double[] getCompleteState(double time) {
 return new double[]{Math.sin(time)};
 }

 @Override
 public double[] getDerivatives(double time, double[] state) {
 return new double[]{Math.cos(time)};
 }
 };

 EmbeddedRungeKuttaIntegrator rk4_maxCountExceeded = new Rk4MethodIntegrator("Rk4_maxCountExceeded", true, new double[]{0.5, 0.25, 0.25, 0.25, 0}, new double[][]{
      {0.5, 0}, {0, 0.5}, {0, 0.5}, {0, 0.5}, {0, 0.5}
 }, new double[]{0.25, 0.25, 0.25, 0.25}, new RungeKuttaStepInterpolator() {
 @Override
 public double[] interpolate(double[] y, double[] yDot, int index, double time, double[] derivatives, double[] secondary) {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public void reinitialize(ExpandableStatefulODE equations, double[] y, double[] yDot, boolean forward, ODEFunctionMapper primaryMapper, ODEFunctionMapper[] secondaryMappers) {
 throw new UnsupportedOperationException("Not supported yet.");
 }

 @Override
 public void storeTime(double time) {
 throw new UnsupportedOperationException("Not supported yet.");
 }
 });

 rk4_maxCountExceeded.setMaxStep(0.0001);
 rk4_maxCountExceeded.integrate(equations4, 0.0, Math.PI * 2);
 }
}