import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private static final double EPSILON = 1e-6;

 @Test
 public void testMinimizeSimpleFunction() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 };
 double min = -1.0;
 double start = 1.0;
 double expected = 0.0;
 double result = new BrentOptimizer().optimize(function, GoalType.MINIMIZE, min, start);
 assertEquals("Expected minimum is not found", expected, result, EPSILON);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorNegativeAccuracy() {
 new BrentOptimizer().setAbsoluteAccuracy(-0.1);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorNegativeIterationCount() {
 new BrentOptimizer().setMaximalIterationCount(-1);
 }

 @Test
 public void testMinimizeFunctionWithBounds() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }
 };
 double min = -Math.PI;
 double max = Math.PI;
 double start = 0.0;
 double expected = -1.0;
 double result = new BrentOptimizer().optimize(function, GoalType.MINIMIZE, min, start, max);
 assertEquals("Expected minimum is not found", expected, result, EPSILON);
 }

 @Test
 public void testMinimizeFunctionWithStartValue() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2);
 }
 };
 double min = -Math.PI;
 double start = -2.0;
 double expected = -1.0;
 double result = new BrentOptimizer().optimize(function, GoalType.MINIMIZE, min, start);
 assertEquals("Expected minimum is not found", expected, result, EPSILON);
 }

 @Test
 public void testMaximizeSimpleFunction() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return -Math.pow(x, 2);
 }
 };
 double min = -1.0;
 double start = 1.0;
 double expected = 0.0;
 double result = new BrentOptimizer().optimize(function, GoalType.MAXIMIZE, min, start);
 assertEquals("Expected maximum is not found", expected, result, EPSILON);
 }

 @Test
 public void testMaximizeFunctionWithBounds() throws Exception {
 UnivariateRealFunction function = new UnivariateRealFunction() {
 @Override
 public double value(double x) {
 return Math.sin(x) * Math.cos(x);
 }
 };
 double min = -Math.PI;
 double max = Math.PI;
 double start = 0.0;
 double expected = 0.0;
 double result = new BrentOptimizer().optimize(function, GoalType.MAXIMIZE, min, start, max);
 assertEquals("Expected maximum is not found", expected, result, EPSILON);
 }
}