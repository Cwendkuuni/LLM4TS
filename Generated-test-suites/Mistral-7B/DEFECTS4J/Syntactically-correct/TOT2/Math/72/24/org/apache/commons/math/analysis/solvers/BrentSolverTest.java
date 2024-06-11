package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.assertAlmostEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.spaceroots.mantissa.functions.AbstractComputableFunction;
import org.spaceroots.mantissa.functions.ConvergenceChecker;
import org.spaceroots.mantissa.roots.BrentSolver;

@RunWith(JUnit4.class)
public class BrentSolverTest {

@Test
public void testBrentSolverConstructor() {
BrentSolver brentSolver = new BrentSolver();
assertNotNull(brentSolver);
}

// Test for the findRoot method with a simple function and converged result
@Test
public void testFindRootConverged() throws Exception {
ComputableFunction function = new ComputableFunction() {
@Override
public double valueAt(double x) {
return x * x - 4.0;
}
};
ConvergenceChecker checker = new ConvergenceChecker() {
@Override
public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
return ConvergenceStatus.LOW;
}
};
BrentSolver brentSolver = new BrentSolver();
assertFalse(brentSolver.findRoot(function, checker, 100, -2.0, 4.0, -1.0, 0.0)); // false because the interval already contains the root
assertAlmostEquals(0.0, brentSolver.getRoot(), 0.01);
}

// Test for the findRoot method with a function that does not have a root in the given interval
@Test
public void testFindRootNoRoot() throws Exception {
ComputableFunction function = new ComputableFunction() {
@Override
public double valueAt(double x) {
return Math.cos(x);
}
};
ConvergenceChecker checker = new ConvergenceChecker() {
@Override
public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
return ConvergenceStatus.LOW;
}
};
BrentSolver brentSolver = new BrentSolver();
assertFalse(brentSolver.findRoot(function, checker, 100, 0.0, 1.0, 0.0, 1.0));
assertNull(brentSolver.getRoot());
}

// Test for the findRoot method with a function that has multiple roots in the given interval
@Test
public void testFindRootMultipleRoots() throws Exception {
ComputableFunction function = new ComputableFunction() {
@Override
public double valueAt(double x) {
return Math.pow(x - 1.5, 3) - 0.25;
}
};
ConvergenceChecker checker = new ConvergenceChecker() {
@Override
public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
return ConvergenceStatus.LOW;
}
};
BrentSolver brentSolver = new BrentSolver();
assertFalse(brentSolver.findRoot(function, checker, 100, 1.0, 3.0, 1.5, 0.0)); // false because there are multiple roots in the interval
assertNull(brentSolver.getRoot());
}

// Test for the findRoot method with a null function
@Test(expected = NullPointerException.class)
public void testFindRootNullFunction() {
BrentSolver brentSolver = new BrentSolver();
assertFalse(brentSolver.findRoot(null, new ConvergenceChecker() {
@Override
public ConvergenceStatus convergence(double xLow, double fLow, double xHigh, double fHigh) {
return ConvergenceStatus.LOW;
}
}, 100, 0.0, 0.0, 0.0, 0.0, 0.0));
}

}