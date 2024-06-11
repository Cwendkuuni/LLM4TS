import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class BisectionSolverTest {

 private UnivariateRealFunction f;
 private BisectionSolver solver;

 @SuppressWarnings("UnusedDeclaration")
 @Before
 public void setup() {
 f = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return x * x - 2.0;
 }
 };
 solver = new BisectionSolver();
 }

 @Test
 public void testNullFunction() {
 assertThrows(NullPointerException.class, () -> new BisectionSolver(null));
 }

 @Test
 public void testSolveSimple() throws MaxIterationsExceededException, FunctionEvaluationException {
 double root = solver.solve(f, -2.0, 2.0);
 assertEquals(0.0, root, 1e-8);
 }

 @Test
 public void testSolveWithInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
 double initial = 1.0;
 double root = solver.solve(f, -2.0, 2.0, initial);
 assertEquals(Math.sqrt(2.0), root, 1e-8);
 }

 @Test
 public void testSolveMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
 assertThrows(MaxIterationsExceededException.class, () -> solver.solve(f, -2.0, 2.0, 0.0));
 }

 @Test
 public void testSolveFunctionEvaluationException() throws MaxIterationsExceededException {
 UnivariateRealFunction brokenFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) throws UnsupportedOperationException {
 throw new UnsupportedOperationException();
 }
 };
 assertThrows(FunctionEvaluationException.class, () -> solver.solve(brokenFunction, -2.0, 2.0));
 }

 @Test
 public void testSolveNumberIsTooSmall() throws MaxIterationsExceededException, FunctionEvaluationException {
 UnivariateRealFunction tooSmallFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Precision.SAFE_MIN;
 }
 };
 assertThrows(NumberIsTooSmallException.class, () -> solver.solve(tooSmallFunction, -2.0, 2.0));
 }

 @Test
 public void testSolveInvalidInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
 UnivariateRealFunction invalidFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) throws UnsupportedOperationException {
 return x * x;
 }
 };
 assertFalse(solver.solve(invalidFunction, -2.0, -2.0) != 0.0);
 }

 @Test
 public void testSolveInvalidInterval2() throws MaxIterationsExceededException, FunctionEvaluationException {
 UnivariateRealFunction invalidFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) throws UnsupportedOperationException {
 return x * x;
 }
 };
 assertFalse(solver.solve(invalidFunction, 2.0, 2.0) != 0.0);
 }

 @Test
 public void testSolveInvalidInitialGuess() throws MaxIterationsExceededException, FunctionEvaluationException {
 UnivariateRealFunction invalidFunction = new UnivariateRealFunction() {
 @Override
 public double value(double x) throws UnsupportedOperationException {
 return x * x - 2.0;
 }
 };
 assertFalse(solver.solve(invalidFunction, 1.0, 0.0) != 0.0);
 }
}