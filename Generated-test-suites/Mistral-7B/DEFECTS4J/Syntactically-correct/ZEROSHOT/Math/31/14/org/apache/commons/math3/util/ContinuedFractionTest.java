import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ContinuedFractionTest {

 @Test
 public void testEvaluateSimpleSquareRoot() {
 ContinuedFraction sqrt = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return n % 2 == 0 ? x / Math.pow(2, n / 2.0) : (n + 1) / (n + x / Math.pow(2, n / 2.0));
 }

 @Override
 protected double getB(int n, double x) {
 return n % 2 == 1 ? x / Math.pow(2, n / 2.0) : -1.0 / (n + 1);
 }
 };

 double epsilon = 1e-14;
 double x = 2.0;
 double result = sqrt.evaluate(x, epsilon);

 assertEquals("Square root of 2 should equal 1.4142135625", 1.4142135625, result, epsilon);
 }

 @Test
 public void testEvaluateSine() {
 ContinuedFraction sine = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return -1.0 / (2.0 * x * FastMath.PI) * Precision.pow(FastMath.sin(FastMath.PI / (2.0 * n + 1.0)), 2);
 }

 @Override
 protected double getB(int n, double x) {
 return (FastMath.PI / (2.0 * n + 1.0)) / x;
 }
 };

 double epsilon = 1e-14;
 double x = 1.0;
 double result = sine.evaluate(x, epsilon);

 assertEquals("Sine of 1 should equal 0.14112000805986", 0.14112000805986, result, epsilon);
 }

 @Test
 public void testEvaluateE() {
 ContinuedFraction e = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return 2.0;
 }

 @Override
 protected double getB(int n, double x) {
 return (n + 1.0) / (n + 2.0);
 }
 };

 double epsilon = 1e-14;
 double x = 1.0;
 double result = e.evaluate(x, epsilon);

 assertEquals("Euler's number e should equal 2.718281828459045", 2.718281828459045, result, epsilon);
 }

 @Test(expected = ConvergenceException.class)
 public void testConvergenceException() {
 ContinuedFraction sqrtNegative = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return x / Math.pow(2, n / 2.0);
 }

 @Override
 protected double getB(int n, double x) {
 return -1.0 / (n + 1.0);
 }
 };

 double x = -1.0;
 double epsilon = 1e-14;
 sqrtNegative.evaluate(x, epsilon);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() {
 ContinuedFraction goldRatio = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return (1.0 + Math.sqrt(5.0)) / 2.0;
 }

 @Override
 protected double getB(int n, double x) {
 return (1.0 - Math.sqrt(5.0)) / 2.0;
 }
 };

 double epsilon = 1e-14;
 double x = 1.0;
 goldRatio.evaluate(x, 1);
 }

}