import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.*;
import org.apache.commons.math3.analysis.function.*;
import org.junit.Test;
import org.junit.Assert.*;

public class AbstractSimplexTest {

 @Test
 public void testAbstractSimplexWithNullArgumentException() {
 try {
 new AbstractSimplex(null);
 fail("Expected NullArgumentException");
 } catch (NullArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithZeroDimension() {
 try {
 new AbstractSimplex(0);
 fail("Expected ZeroException");
 } catch (ZeroException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithZeroSteps() {
 PointValuePair[] points;
 try {
 new AbstractSimplex(new double[0]);
 } catch (Exception e) {
 points = e.getSuppressed(DimensionMismatchException.class)[0].getPoints();
 }
 Assert.assertNotNull(points);
 Assert.assertEquals(0, points.length);
 }

 @Test
 public void testAbstractSimplexWithEqualVertices() {
 try {
 new AbstractSimplex(new double[]{-1, -1}, new double[]{-1, -1});
 fail("Expected Exception");
 } catch (MathIllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithOnePoint() {
 PointValuePair[] points;
 AbstractSimplex simplex = new AbstractSimplex(3, 1.0);
 simplex.build(new double[]{0.0, 0.0, 0.0});
 simplex.evaluate(new SquaredFunction(), null);
 points = simplex.getPoints();
 Assert.assertEquals(4, points.length);
 }

 @Test
 public void testAbstractSimplexWithReferenceSimplex() {
 PointValuePair[] expectedPoints = new PointValuePair[]{
 new PointValuePair(new double[]{0.0, 0.0}, 0.0),
 new PointValuePair(new double[]{1.0, 0.0}, 1.0),
 new PointValuePair(new double[]{0.5, 0.5}, 0.5),
 new PointValuePair(new double[]{1.0, 1.0}, 1.0)
 };
 AbstractSimplex simplex = new AbstractSimplex(new double[][]{
 new double[]{0.0, 0.0},
 new double[]{1.0, 0.0},
 new double[]{0.5, 0.5},
 new double[]{1.0, 1.0}
 });
 PointValuePair[] points = simplex.getPoints();
 Assert.assertArrayEquals(expectedPoints, points);
 }

 @Test
 public void testAbstractSimplexWithIterate() {
 MultivariateFunction function = new AddFunction(new SineFunction(), new CosineFunction());
 AbstractSimplex simplex = new SimplexMethod(3);
 simplex.build(new double[]{0.0, 0.0, 0.0});
 simplex.iterate(function, new MaxFunction());
 PointValuePair worstPoint = simplex.getPoint(simpleX.getSize() - 1);
 double expectedValue = Math.PI + Math.E;
 Assert.assertEquals(expectedValue, worstPoint.getValue(), 0.001);
 }

}