import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealFunction;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.AmoebaPoint;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MultiDirectionalTest {

 private static final double EPSILON = 1e-6;
 private static final int MAX_EVALUATIONS = 100;

 @Test
 public void testMultiDirectionalOptimizer() throws Exception {
 // Test with a simple function
 ObjectiveFunction f = new ObjectiveFunction() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }

 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 };

 MultiDirectional optimizer = new MultiDirectional();
 optimizer.setMaxEvaluations(MAX_EVALUATIONS);
 AmoebaPoint x0 = new AmoebaPoint(f, EPSILON);
 PointValuePair solution = optimizer.optimize(x0);

 assertEquals(2.0, solution.getPoint()[0], EPSILON);
 assertEquals(0.0, solution.getValue(), EPSILON);

 // Test with a more complex function
 ObjectiveFunction g = new ObjectiveFunction() {
 @Override
 public double value(double[] x) {
 return Math.pow(Math.sin(x[0]) + Math.pow(x[1], 2), 2) + Math.exp(-x[0]);
 }

 @Override
 public GoalType getGoalType() {
 return GoalType.MINIMIZE;
 }
 };

 optimizer = new MultiDirectional();
 optimizer.setMaxEvaluations(MAX_EVALUATIONS);
 x0 = new AmoebaPoint(g, new double[]{3.0, 2.0}, new SimpleValueChecker(EPSILON));
 solution = optimizer.optimize(x0);

 assertEquals(-2.5371652786390683, solution.getPoint()[0], EPSILON);
 assertEquals(4.540286313559677e-14, solution.getValue(), EPSILON);
 }

}