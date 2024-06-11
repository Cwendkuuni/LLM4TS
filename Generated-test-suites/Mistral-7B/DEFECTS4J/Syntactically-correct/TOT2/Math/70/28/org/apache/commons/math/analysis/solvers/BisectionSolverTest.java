import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BisectionSolverTest {

 private UnivariateRealFunction function;
 private BisectionSolver solver;

 @Test
 public void testEmptyConstructor() {
 solver = new BisectionSolver();
 }

 @Test
 public void testConstructorWithFunction() {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x * x - 4.0;
 }
 };
 solver = new BisectionSolver(function);
 }

 @Test
 public void testSolve_TwoValuesWithSameSign() {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x);
 }
 };
 solver = new BisectionSolver(function);
 assertThrows(FunctionEvaluationException.class, () -> solver.solve(-1.0, 1.0));
 }

 @Test
 public void testSolve_TwoValuesWithOppositeSigns() {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) * Math.sin(x);
 }
 };
 solver = new BisectionSolver(function);
 assertEquals(0.0011574534536712645, solver.solve(function, -2.0, 2.0), 0.0001);
 }

 @Test
 public void testSolve_MaxIterationsExceeded() {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.log(Math.abs(x - 1.0));
 }
 };
 solver = new BisectionSolver(function);
 assertThrows(MaxIterationsExceededException.class, () -> solver.solve(function, -1.0, 1.0));
 }

 @Test
 public void testSolve_AbsoluteAccuracy() {
 function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 1.0, 2.0);
 }
 };
 solver = new BisectionSolver(function);
 assertEquals(1.0, solver.solve(function, -1.1, 1.1), 0.01);
 }
}