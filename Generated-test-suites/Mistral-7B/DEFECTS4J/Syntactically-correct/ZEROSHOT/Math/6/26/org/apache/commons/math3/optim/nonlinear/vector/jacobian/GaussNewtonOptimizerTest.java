import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.univariate.DifferentiableUnivariateFunction;
import org.apache.commons.math3.optim.linear.GaussNewtonOptimizer;
import org.apache.commons.math3.optim.linear.GaussNewtonOptimizer.ConvergenceChecker;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GaussNewtonOptimizerTest {

 @Test
 public void testGaussNewtonOptimizerWithLUSolver() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1.0E-6, 1.0E-6));

 // Define a simple function for testing.
 DifferentiableUnivariateFunction function = new DifferentiableUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(x, 2) + x - 1;
 }

 @Override
 public double derivative(double x) {
 return 3 * x * x - 2 * x * 2 + 1;
 }
 };

 double[] startPoint = { 1.0 };
 double[] expectedPoint = { 1.5 };
 double expectedCost = -2.5;

 PointValuePair result = optimizer.optimize(function, startPoint);

 assertEquals(expectedPoint.length, result.getPoint().length);
 assertArrayEquals(expectedPoint, result.getPoint(), 1e-6);
 assertEquals(expectedCost, result.getValue(), 1e-6);
 }

 @Test
 public void testGaussNewtonOptimizerWithQRSolver() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleValueChecker(1.0E-6, 1.0E-6));

 // Define a simple function for testing.
 DifferentiableUnivariateFunction function = new DifferentiableUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 3) - 2 * x * Math.pow(x, 2) + x - 1;
 }

 @Override
 public double derivative(double x) {
 return 3 * x * x - 2 * x * 2 + 1;
 }
 };

 double[] startPoint = { 1.0 };
 double[] expectedPoint = { 1.5 };
 double expectedCost = -2.5;

 PointValuePair result = optimizer.optimize(function, startPoint);

 assertEquals(expectedPoint.length, result.getPoint().length);
 assertArrayEquals(expectedPoint, result.getPoint(), 1e-6);
 assertEquals(expectedCost, result.getValue(), 1e-6);
 }

 @Test
 public void testGaussNewtonOptimizerWithSingularMatrixException() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleValueChecker(1.0E-6, 1.0E-6));

 DifferentiableUnivariateFunction function = new DifferentiableUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }

 @Override
 public double derivative(double x) {
 return 2 * x;
 }
 };

 double[] startPoint = { 0.0 };
 double[] expectedPoint = { 0.0 };
 double expectedCost = 0.0;

 try {
 PointValuePair result = optimizer.optimize(function, startPoint);
 fail("Singular Matrix Exception is expected.");
 } catch (ConvergenceException e) {
 // Expected exception.
 }

 assertEquals(expectedPoint.length, startPoint.length);
 assertArrayEquals(expectedPoint, startPoint, 1e-6);
 assertEquals(expectedCost, 0.0, 1e-6);
 }

 @Test
 public void testGaussNewtonOptimizerWithNullChecker() {
 GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);

 DifferentiableUnivariateFunction function = new DifferentiableUnivariateFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }

 @Override
 public double derivative(double x) {
 return 2 * x;
 }
 };

 double[] startPoint = { 0.0 };

 try {
 optimizer.optimize(function, startPoint);
 fail("NullPointerException is expected.");
 } catch (NullArgumentException e) {
 // Expected exception.
 }
 }
}