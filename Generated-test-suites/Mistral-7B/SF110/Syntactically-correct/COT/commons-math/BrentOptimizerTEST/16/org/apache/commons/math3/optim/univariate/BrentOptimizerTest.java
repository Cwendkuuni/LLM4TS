import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.NonlinearConstraint;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConstraint;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMinimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMaximizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeBoth;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private UnivariateOptimizer optimizer;

 @Test
 public void testConstructorWithRelativeAndAbsoluteTolAndChecker() {
     optimizer = new BrentOptimizer(0.01d, 0.001d, new GoalTypeMinimizer(new GoalTypeConstraint(0.0)));
     assertNotNull(optimizer);
 }

 @Test
 public void testConstructorWithRelativeAndAbsoluteTol() {
     optimizer = new BrentOptimizer(0.01d, 0.001d);
     assertNotNull(optimizer);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorWithNegativeRelativeTol() {
     optimizer = new BrentOptimizer(-0.01d, 0.001d, new GoalTypeMinimizer(new GoalTypeConstraint(0.0)));
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorWithZeroOrNegativeAbsoluteTol() {
     optimizer = new BrentOptimizer(0.01d, -0.001d, new GoalTypeMinimizer(new GoalTypeConstraint(0.0)));
 }

 @Test
 public void testDoOptimizeWithMinimizationGoal() {
     optimizer = new BrentOptimizer(0.01d, 0.001d, new GoalTypeMinimizer(new GoalTypeConstraint(0.0)));
     PointValuePair result = optimizer.optimize(new SquareFunction(), -10.0, 10.0);
     assertEquals(-2.44948974278317, result.getValue(), 0.001);
     assertEquals(-2.44948974278317, result.getPoint(), 0.001);
 }

 @Test
 public void testDoOptimizeWithMaximizationGoal() {
     optimizer = new BrentOptimizer(0.01d, 0.001d, new GoalTypeMaximizer(new GoalTypeConstraint(0.0)));
     PointValuePair result = optimizer.optimize(new SquareFunction(), -10.0, 10.0);
     assertEquals(2.44948974278317, result.getValue(), 0.001);
     assertEquals(2.44948974278317, result.getPoint(), 0.001);
 }

 @Test
 public void testDoOptimizeWithBothGoal() {
     optimizer = new BrentOptimizer(0.01d, 0.001d, new GoalTypeBoth(new GoalTypeMinimizer(new GoalTypeConstraint(0.0)), new GoalTypeMaximizer(new GoalTypeConstraint(0.0))));
     PointValuePair result = optimizer.optimize(new SquareFunction(), -10.0, 10.0);
     assertEquals(0.0, result.getValue(), 0.001);
     assertEquals(0.0, result.getPoint(), 0.001);
 }

 @Test(expected = ConvergenceException.class)
 public void testDoOptimizeWithChecker() {
     optimizer = new BrentOptimizer(0.01d, 0.001d, new CustomConvergenceChecker());
     PointValuePair result = optimizer.optimize(new SquareFunction(), -10.0, 10.0);
 }

 @Test
 public void testDoOptimizeWithIllegalMinValue() {
     optimizer = new BrentOptimizer(0.01d, 0.001d, new GoalTypeMinimizer(new GoalTypeConstraint(0.0)));
     try {
         optimizer.optimize(new SquareFunction(), -Double.MAX_VALUE, 10.0);
         fail();
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testDoOptimizeWithIllegalMaxValue() {
     optimizer = new BrentOptimizer(0.01d, 0.001d, new GoalTypeMinimizer(new GoalTypeConstraint(0.0)));
     try {
         optimizer.optimize(new SquareFunction(), -10.0, Double.MAX_VALUE);
         fail();
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testDoOptimizeWithIllegalInitialValue() {
     optimizer = new BrentOptimizer(0.01d, 0.001d, new GoalTypeMinimizer(new GoalTypeConstraint(0.0)));
     try {
         optimizer.optimize(new SquareFunction(), Double.NaN, 10.0);
         fail();
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testDoOptimizeWithIllegalInitialValueType() {
     optimizer = new BrentOptimizer(0.01d, 0.001d, new GoalTypeMinimizer(new GoalTypeConstraint(0.0)));
     try {
         optimizer.optimize(new IntegerFunction(), 0, 10);
         fail();
     } catch (UnsupportedOperationException e) {
         // expected
     }
 }

 @Test
 public void testDoOptimizeWithCustomInitializer() {
     optimizer = new BrentOptimizer(0.01d, 0.001d, new GoalTypeMinimizer(new GoalTypeConstraint(0.0)));
     ValueInitializer<Double> initializer = new ValueInitializer<Double>() {
         @Override
         public Double initialize() {
             return 5.0;
         }
     };
     PointValuePair result = optimizer.optimize(new SquareFunction(), initializer);
     assertEquals(-2.25, result.getValue(), 0.001);
     assertEquals(-2.25, result.getPoint(), 0.001);
 }

 @Test
 public void testDoOptimizeWithCustomConstraint() {
     optimizer = new BrentOptimizer(0.01d, 0.001d, new GoalTypeMinimizer(new CustomConstraint()));
     PointValuePair result = optimizer.optimize(new SquareFunction(), -10.0, 10.0);
     assertEquals(-2.44948974278317, result.getValue(), 0.001);
     assertEquals(-2.44948974278317, result.getPoint(), 0.001);
 }

}