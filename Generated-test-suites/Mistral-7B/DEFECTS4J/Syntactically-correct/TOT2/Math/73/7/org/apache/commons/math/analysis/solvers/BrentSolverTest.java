// The complete JUnit 4 test file (BrentSolverTest.java)

package org.spaceroots.mantissa.roots;

import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.functions.scalar.TestFunction;

public class BrentSolverTest {
   // Test method for findRoot() of BrentSolver class
   @Test
   public void testFindRoot() throws FunctionException {
      // Test case 1: Test with function f(x) = x^2 - 4
      BrentSolver solver = new BrentSolver();
      ComputableFunction function = FunctionFactory.createFunction(new TestFunction(x -> x * x - 4.0));
      ConvergenceChecker checker = new ConvergenceChecker() {
         @Override
         public int converged(double x, double fx, double xLow, double fxLow) {
            return ConvergenceChecker.LOW;
         }
      };
      double x0 = 1.0, x1 = 3.0;
      double root = solver.findRoot(function, checker, 100, x0, x0 * x0, x1, x1 * x1);
      Assert.assertEquals(2.0, root, 0.001);

      // Test case 2: Test with function f(x) = sin(x)
      function = FunctionFactory.createFunction(Math::sin);
      checker = new ConvergenceChecker() {
         @Override
         public int converged(double x, double fx, double xLow, double fxLow) {
            return ConvergenceChecker.HIGH;
         }
      };
      double x2 = 0.5 * Math.PI;
      try {
         solver.findRoot(function, checker, 100, x2, Math.sin(x2), x2 + 0.1, Math.sin(x2 + 0.1));
         Assert.fail("Test failed: findRoot() did not throw FunctionException");
      } catch (FunctionException ex) {
         // Expected
      }

      // Test case 3: Test with function f(x) = x^2 + eps
      function = FunctionFactory.createFunction(x -> x * x + BrentSolver.epsilon);
      checker = new ConvergenceChecker() {
         @Override
         public int converged(double x, double fx, double xLow, double fxLow) {
            return ConvergenceChecker.LOW;
         }
      };
      double x3 = 0.0;
      double root3 = solver.findRoot(function, checker, 100, x3, x3 * x3 + BrentSolver.epsilon, x3 + 1.0, x3 * x3 + 2.0 * BrentSolver.epsilon);
      Assert.assertEquals(1.0, root3, 0.001);
   }
}