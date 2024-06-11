import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.solvers.UnivariateRealSolver;
import org.apache.commons.math3.solvers.LevenbergMarquardtSolver;
import org.apache.commons.math3.solvers.BrentSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

 @Test
 public void testSolve_defaultSolver_simple() {
 UnivariateRealFunction function = new FunctionWithRootAtZero();
 double expectedResult = 0.0;
 try {
 double result = UnivariateRealSolverUtils.solve(function, -1.0, 1.0);
 assertEquals(expectedResult, result, 1e-9);
 } catch (ConvergenceException | FunctionEvaluationException e) {
 throw new RuntimeException("Unexpected exception", e);
 }
 }

 @Test
 public void testSolve_defaultSolver_maxIterations() {
 UnivariateRealFunction function = new FunctionWithRootAtOneMillion();
 try {
 UnivariateRealSolverUtils.solve(function, 0.0, 2.0);
 fail("Expected ConvergenceException");
 } catch (ConvergenceException e) {
 // Expected
 } catch (FunctionEvaluationException e) {
 throw new RuntimeException("Unexpected exception", e);
 }
 }

 @Test
 public void testSolve_absoluteAccuracy_simple() {
 UnivariateRealFunction function = new FunctionWithRootAtZero();
 double expectedResult = 0.0;
 double absoluteAccuracy = 1e-12;
 try {
 double result = UnivariateRealSolverUtils.solve(function, -1.0, 1.0, absoluteAccuracy);
 assertEquals(expectedResult, result, absoluteAccuracy);
 } catch (ConvergenceException | FunctionEvaluationException e) {
 throw new RuntimeException("Unexpected exception", e);
 }
 }

 @Test
 public void testBracket_simple() {
 UnivariateRealFunction function = new FunctionWithRootAtZero();
 double[] expectedBracketing = {-1.0, 0.0};
 try {
 double[] bracketing = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
 assertArrayEquals(expectedBracketing, bracketing);
 } catch (ConvergenceException | FunctionEvaluationException e) {
 throw new RuntimeException("Unexpected exception", e);
 }
 }

 @Test
 public void testBracket_maxIterations() {
 UnivariateRealFunction function = new FunctionWithRootAtOneMillion();
 try {
 UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0, 1);
 fail("Expected ConvergenceException");
 } catch (ConvergenceException e) {
 // Expected
 } catch (FunctionEvaluationException e) {
 throw new RuntimeException("Unexpected exception", e);
 }
 }

 @Test
 public void testBracket_multipleRoots() {
 UnivariateRealFunction function = new FunctionWithTwoRoots();
 double[] expectedBracketing1 = {-1.0, 0.0};
 double[] expectedBracketing2 = {2.0, 3.0};
 try {
 double[] bracketing1 = UnivariateRealSolverUtils.bracket(function, 0.0, -1.0, 1.0);
 assertArrayEquals(expectedBracketing1, bracketing1);

 double[] bracketing2 = UnivariateRealSolverUtils.bracket(function, 1.0, 2.0, 3.0);
 assertArrayEquals(expectedBracketing2, bracketing2);
 } catch (ConvergenceException | FunctionEvaluationException e) {
 throw new RuntimeException("Unexpected exception", e);
 }
 }

 @Test
 public void testSolverFactory_defaultSolver() {
 UnivariateRealSolverFactory factory = UnivariateRealSolverUtils.LazyHolder.FACTORY;
 UnivariateRealSolver solver = factory.getSolver();
 assertTrue(solver instanceof BrentSolver);
 }

 @Test
 public void testSolverFactory_customSolver() {
 UnivariateRealSolverFactory factory = UnivariateRealSolverUtils.LazyHolder.FACTORY;
 UnivariateRealSolver solver = factory.getSolver("LevenbergMarquardt");
 assertTrue(solver instanceof LevenbergMarquardtSolver);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSolverFactory_invalidSolver() {
 UnivariateRealSolverFactory factory = UnivariateRealSolverUtils.LazyHolder.FACTORY;
 factory.getSolver("FooBar");
 }
}

class FunctionWithRootAtZero implements UnivariateRealFunction {
 @Override
 public double value(double x) {
 return x * x - 1.0;
 }
}

class FunctionWithRootAtOneMillion implements UnivariateRealFunction {
 @Override
 public double value(double x) {
 return Math.pow(Math.E, x) - 1.0e6;
 }
}

class FunctionWithTwoRoots implements UnivariateRealFunction {
 @Override
 public double value(double x) {
 return Math.pow(x - 1.0, 2) - 1.0;
 }
}