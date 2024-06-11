import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.solver.BisectionSolver;
import org.apache.commons.math3.solver.UnivariateRealSolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

 private UnivariateRealFunction function;
 private UnivariateRealSolver solver;

 @Before
 public void setup() {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2) - 4;
 }
 };
 solver = new BisectionSolver();
 }

 @Test
 public void testSolve() throws Exception {
 double expected = 2.0;
 double result = solver.solve(function, -5.0, 5.0);
 assertEquals(expected, result, 1e-6);
 }

 @Test(expected = MaxIterationsExceededException.class)
 public void testMaxIterationsExceededException() throws Exception {
 double min = -10.0;
 double max = 10.0;
 solver.solve(function, min, max);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testNumberIsTooSmallException() throws Exception {
 double min = 0.0;
 double max = 1e-10;
 solver.solve(function, min, max);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedOperationException() throws Exception {
 solver.solve(function, 0.0, 0.0);
 }
}