import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

 @Test
 public void testEvaluateSimpleContinuedFraction() {
 ContinuedFraction continuedFraction = new SimpleContinuedFraction();
 assertEquals(1.0, continuedFraction.evaluate(0.0), 1e-9);
 assertEquals(2.0, continuedFraction.evaluate(1.0), 1e-9);
 }

 @Test(expected = ConvergenceException.class)
 public void testEvaluateNonConvergentContinuedFraction() {
 ContinuedFraction continuedFraction = new NonConvergentContinuedFraction();
 continuedFraction.evaluate(0.0); // should throw ConvergenceException
 }

 @Test(expected = MaxCountExceededException.class)
 public void testEvaluateMaxIterationsExceeded() {
 ContinuedFraction continuedFraction = new MaxIterationsContinuedFraction();
 continuedFraction.evaluate(0.0, 1); // should throw MaxCountExceededException
 }

 @Test
 public void testEvaluateWithEpsilonAndMaxIterations() {
 ContinuedFraction continuedFraction = new TestableContinuedFraction();
 double epsilon = 1e-5;
 int maxIterations = 10;
 assertEquals(1.2345, continuedFraction.evaluate(0.0, epsilon, maxIterations), epsilon);
 }

 @Test
 public void testEvaluateWithNegativeArgument() {
 ContinuedFraction continuedFraction = new TestableContinuedFraction();
 double x = -1.0;
 double epsilon = 1e-5;
 int maxIterations = 10;
 assertEquals(-1.5432, continuedFraction.evaluate(x, epsilon, maxIterations), epsilon);
 }

 @Test
 public void testEvaluateWithInvalidEvaluationPoint() {
 ContinuedFraction continuedFraction = new TestableContinuedFraction();
 double x = Double.NaN;
 double epsilon = 1e-5;
 int maxIterations = 10;
 assertNaN(continuedFraction.evaluate(x, epsilon, maxIterations));
 }

}