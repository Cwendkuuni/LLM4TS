package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoRootFoundException;
import org.apache.commons.math3.exception.PrecisionModifiedException;
import org.apache.commons.math3.function.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {

 private UnivariateRealFunction function;
 private BisectionSolver solver;

 @Before
 public void setUp() {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 1;
 }
 };
 solver = new BisectionSolver();
 }

 @Test
 public void testSolverWithValidArguments() throws MaxIterationsExceededException, NoRootFoundException, PrecisionModifiedException {
 double root = solver.solve(function, -2.0, 2.0);
 Assert.assertEquals(0.0, root, 1E-6);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testSolverWithMaxIterationsExceeded() throws MaxIterationsExceededException {
 solver = new BisectionSolver(1);
 double root = solver.solve(function, -2.0, 2.0);
 }

 @Test(expected = NoRootFoundException.class)
 public void testSolverWithNoRoot() throws MaxIterationsExceededException, NoRootFoundException {
 UnivariateRealFunction functionWithNoRoot = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(Math.PI) * Math.cos(Math.PI);
 }
 };
 solver = new BisectionSolver();
 Assert.assertThrows(NoRootFoundException.class, () -> solver.solve(functionWithNoRoot, -5.0, 5.0));
 }

 @Test
 public void testSolverWithDifferentPrecision() throws MaxIterationsExceededException, NoRootFoundException, PrecisionModifiedException {
 double rootWithHighPrecision = solver.solve(function, -2.0, 2.0);
 double rootWithLowPrecision = solver.solve(function, -2.0, 2.0, 1E-4);
 Assert.assertEquals(rootWithHighPrecision, rootWithLowPrecision, 1E-6);
 }
}