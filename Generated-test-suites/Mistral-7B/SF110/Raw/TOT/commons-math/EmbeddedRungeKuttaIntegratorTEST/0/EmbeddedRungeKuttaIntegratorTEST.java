package org.apache.commons.math3.ode.nonstiff.test;

import org.apache.commons.math3.ode.*;
import org.apache.commons.math3.ode.nonstiff.EmbeddedRungeKuttaIntegrator;
import org.apache.commons.math3.ode.sampling.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmbeddedRungeKuttaIntegratorTest {

 @Test
 public void testRK2Integrator() {
 EmbeddedRungeKuttaIntegrator rk2Integrator = new RungeKutta2Integrator("RK2", false, new double[]{0.0, 0.0}, new double[][]{
 {0.0, 0.5}, {0.5, 0.0}
 }, new double[]{0.0, 1.0}, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

 double[] initialValues = {1.0, 1.0};
 double[] expectedValues = {1.1, 1.0};
 double startTime = 0.0;
 double endTime = 0.1;

 ExpandableStatefulODE equations = new DummyODE(initialValues);

 try {
 rk2Integrator.integrate(equations, startTime, endTime);
 assertTrue(Arrays.equals(expectedValues, equations.getCompleteState()));
 } catch (Exception e) {
 fail("Exception thrown: " + e.getMessage());
 }
 }

 @Test
 public void testRK2Integrator_FSAL() {
 EmbeddedRungeKuttaIntegrator rk2Integrator = new RungeKutta2Integrator("RK2", true, new double[]{0.0, 0.0}, new double[][]{
 {0.0, 0.5}, {0.5, 0.0}
 }, new double[]{0.0, 1.0}, new RungeKuttaStepInterpolator(), 0.001, 0.01, new double[]{1e-6, 1e-6}, new double[]{1e-6, 1e-6});

 double[] initialValues = {1.0, 1.0};
 double[] expectedValues = {1.1, 1.0};
 double startTime = 0.0;
 double endTime = 0.1;

 ExpandableStatefulODE equationsFsal = new DummyODEFsal(initialValues);

 try {
 rk2Integrator.integrate(equationsFsal, startTime, endTime);
 assertTrue(Arrays.equals(expectedValues, equationsFsal.getCompleteState()));
 } catch (Exception e) {
 fail("Exception thrown: " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRK2Integrator_InvalidOrder() {
 EmbeddedRungeKuttaIntegrator rk2Integrator = new RungeKutta2Integrator("RK2", false, new double[]{0.0, 0.0}, new double[][]{
 {0.0, 0.5}, {0.5, 0.0}
 }, new double[]{0.0, 1.0}, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);
 rk2Integrator.setOrder(-1);
 }

 @Test
 public void testRK4Integrator() {
 EmbeddedRungeKuttaIntegrator rk4Integrator = new RungeKutta4Integrator("RK4", false, new double[]{0.0, 0.0}, new double[][]{
 {0.0, 0.25}, {0.25, 0.25}, {0.25, 0.25}, {0.25, 0.25}
 }, new double[]{0.0, 0.5, 0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01, null, null);

 double[] initialValues = {1.0, 1.0};
 double[] expectedValues = {1.25, 1.2};
 double startTime = 0.0;
 double endTime = 0.1;

 ExpandableStatefulODE equations = new DummyODE(initialValues);

 try {
 rk4Integrator.integrate(equations, startTime, endTime);
 assertTrue(Arrays.equals(expectedValues, equations.getCompleteState()));
 } catch (Exception e) {
 fail("Exception thrown: " + e.getMessage());
 }
 }

 @Test
 public void testRK4Integrator_FSAL() {
 EmbeddedRungeKuttaIntegrator rk4Integrator = new RungeKutta4Integrator("RK4", true, new double[]{0.0, 0.0}, new double[][]{
 {0.0, 0.25}, {0.25, 0.25}, {0.25, 0.25}, {0.25, 0.25}
 }, new double[]{0.0, 0.5, 0.5, 0.5}, new RungeKuttaStepInterpolator(), 0.001, 0.01, new double[]{1e-6, 1e-6, 1e-6, 1e-6}, new double[]{1e-6, 1e-6, 1e-6, 1e-6});

 double[] initialValues = {1.0, 1.0};
 double[] expectedValues = {1.25, 1.2};
 double startTime = 0.0;
 double endTime = 0.1;

 ExpandableStatefulODE equationsFsal = new DummyODEFsal(initialValues);

 try {
 rk4Integrator.integrate(equationsFsal, startTime, endTime);
 assertTrue(Arrays.equals(expectedValues, equationsFsal.getCompleteState()));
 } catch (Exception e) {
 fail("Exception thrown: " + e.getMessage());
 }
 }
}

class DummyODE implements ExpandableStatefulODE {
 private double[] state;

 public DummyODE(double[] initialValues) {
 this.state = initialValues;
 }

 @Override
 public int getDimension() {
 return state.length;
 }

 @Override
 public int getPrimaryDimension() {
 return state.length;
 }

 @Override
 public int getSecondaryDimension() {
 return 0;
 }

 @Override
 public double getTime() {
 return 0.0;
 }

 @Override
 public void setTime(double t) {
 // Empty
 }

 @Override
 public double[] getCompleteState() {
 return state;
 }

 @Override
 public void setCompleteState(double[] newState) {
 this.state = newState;
 }

 @Override
 public void setEquations(ODEStateAndDerivative equations) {
 // Empty
 }

 @Override
 public void setPrimaryMapper(ODEFunctionMapper mapper) {
 // Empty
 }

 @Override
 public void setSecondaryMappers(ODEFunctionMapper[] mappers) {
 // Empty
 }
}

class DummyODEFsal implements ExpandableStatefulODE, StatefulODE {
 private double[] state;

 public DummyODEFsal(double[] initialValues) {
 this.state = initialValues;
 }

 @Override
 public int getDimension() {
 return state.length;
 }

 @Override
 public int getPrimaryDimension() {
 return state.length;
 }

 @Override
 public int getSecondaryDimension() {
 return 0;
 }

 @Override
 public double getTime() {
 return 0.0;
 }

 @Override
 public void setTime(double t) {
 // Empty
 }

 @Override
 public double[] getCompleteState() {
 return state;
 }

 @Override
 public void setCompleteState(double[] newState) {
 this.state = newState;
 }

 @Override
 public void setEquations(ODEStateAndDerivative equations) {
 // Empty
 }

 @Override
 public double evaluateDerivative(double t, double[] y) {
 return new double[]{0.0};
 }
}