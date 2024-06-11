import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.direct.DirectSearchOptimizer;
import org.apache.commons.math3.optim.nonlinear.function.GoalType;
import org.apache.commons.math3.optim.nonlinear.function.RealFunction;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MultiDirectionalTest {

 private MultiDirectional optimizer;
 private RealFunction testFunction;

 @Before
 public void setUp() {
 testFunction = new QuadraticFunction(-1, 10);
 optimizer = new MultiDirectional();
 optimizer.setGoalType(GoalType.MINIMIZE);
 optimizer.setMaxIterations(100);
 optimizer.setConvergenceChecker(new RealConvergenceChecker(1.0E-6, 1.0E-6));
 }

 @Test
 public void testDefaultConstructor() {
 optimizer = new MultiDirectional();
 assertEquals(2.0, optimizer.getKhi(), 0.01);
 assertEquals(0.5, optimizer.getGamma(), 0.01);
 }

 @Test
 public void testCustomConstructor() {
 optimizer = new MultiDirectional(3.0, 0.8);
 assertEquals(3.0, optimizer.getKhi(), 0.01);
 assertEquals(0.8, optimizer.getGamma(), 0.01);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeKhi() {
 optimizer = new MultiDirectional(-1.0, 0.5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeGamma() {
 optimizer = new MultiDirectional(2.0, -0.5);
 }

 @Test
 public void testIterateSimplex() throws Exception {
 optimizer.optimize(testFunction, 0.0);
 assertEquals(testFunction.value(0.0), optimizer.getFmin(), 0.01);
 }

}

class QuadraticFunction implements RealFunction {
 private double a;
 private double b;

 public QuadraticFunction(double a, double b) {
 this.a = a;
 this.b = b;
 }

 @Override
 public double value(double x) {
 return a * Math.pow(x, 2) + b;
 }
}