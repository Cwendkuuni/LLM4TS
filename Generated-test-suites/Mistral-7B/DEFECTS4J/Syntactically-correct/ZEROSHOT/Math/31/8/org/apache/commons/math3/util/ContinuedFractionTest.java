import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ContinuedFractionTest {

 @Test
 public void testEvaluateWithDefaultParameters() {
 ContinuedFraction continuedFraction = new MyContinuedFraction();
 double result = continuedFraction.evaluate(0.5);
 double expectedResult = 1.619045642;
 assertEquals(expectedResult, result, 0.0000001);
 }

 @Test
 public void testEvaluateWithCustomEpsilon() {
 ContinuedFraction continuedFraction = new MyContinuedFraction();
 double result = continuedFraction.evaluate(0.5, 1e-12);
 double expectedResult = 1.61904564236141;
 assertEquals(expectedResult, result, 1e-13);
 }

 @Test
 public void testEvaluateWithCustomMaxIterations() {
 ContinuedFraction continuedFraction = new MyContinuedFraction();
 double result = continuedFraction.evaluate(0.5, 1e-9, 10);
 double expectedResult = 1.61904564223;
 assertEquals(expectedResult, result, 1e-10);
 }

 @Test(expected = ConvergenceException.class)
 public void testConvergenceExceptionWhenMaxIterationsReached() {
 ContinuedFraction continuedFraction = new MyContinuedFraction();
 double result = continuedFraction.evaluate(0.5, 1e-9, 3);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededExceptionWhenMaxIterationsReached() {
 ContinuedFraction continuedFraction = new MyContinuedFraction();
 double result = continuedFraction.evaluate(0.5, 1e-9, Integer.MIN_VALUE);
 }

 @Test
 public void testInfiniteDivergenceException() {
 ContinuedFraction continuedFraction = new MyContinuedFractionWithDivergence();
 try {
 continuedFraction.evaluate(0.0);
 } catch (ConvergenceException e) {
 assertEquals(ConvergenceException.Type.INFINITY_DIVERGENCE, e.getType());
 }
 }

 @Test
 public void testNaNException() {
 ContinuedFraction continuedFraction = new MyContinuedFractionWithNaN();
 try {
 continuedFraction.evaluate(0.0);
 } catch (ConvergenceException e) {
 assertEquals(ConvergenceException.Type.NAN_DIVERGENCE, e.getType());
 }
 }

}

class MyContinuedFraction extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 return x;
 }

 @Override
 protected double getB(int n, double x) {
 return 1.0;
 }
}

class MyContinuedFractionWithDivergence extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 return 1.0 / x;
 }

 @Override
 protected double getB(int n, double x) {
 return 1.0;
 }
}

class MyContinuedFractionWithNaN extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 return 1.0 / Math.sqrt(x);
 }

 @Override
 protected double getB(int n, double x) {
 return 1.0;
 }
}