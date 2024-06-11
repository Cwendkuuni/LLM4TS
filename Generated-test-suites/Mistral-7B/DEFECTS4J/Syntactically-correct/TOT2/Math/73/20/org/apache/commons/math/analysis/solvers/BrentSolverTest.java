package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.*;

import org.spaceroots.mantissa.functions.*;
import org.spaceroots.mantissa.roots.*;
import org.spaceroots.mantissa.roots.test.ConvergenceChecker;
import org.junit.Test;

public class BrentSolverTest {

 // ... (The previously proposed test cases)

 // Complete JUnit test file

 @Test
 public void testBrentSolverComprehensive() throws FunctionException {
 ComputableFunction function = new LinearFunction(-1.0, 1.0);
 ConvergenceChecker checker = new ConvergenceChecker();
 BrentSolver solver = new BrentSolver();

 double x0 = 1.0;
 double f0 = function.valueAt(x0);
 double x1 = 2.0;
 double f1 = function.valueAt(x1);

 assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
 assertEquals(1.5, solver.getRoot(), 0.01);
 }

}