import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class AbstractSimplexTest {

 private AbstractSimplex simplex;
 private MultivariateFunction function;

 @Before
 public void setup() {
 int n = 3;
 double sideLength = 1.0;
 simplex = new AbstractSimplex(n, sideLength);
 function = new QuadraticFunction(new double[]{-1.0, 1.0, 1.0}, 10.0);
 }

 @Test
 public void testGetDimension() {
 assertEquals(3, simplex.getDimension());
 }

 @Test
 public void testGetSize() {
 assertEquals(4, simplex.getSize());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexNullConstructor() {
 new AbstractSimplex(null);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexEmptyConstructor() {
 new AbstractSimplex(0);
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexEmptySteps() {
 AbstractSimplex simplex1 = new AbstractSimplex(new double[0]);
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexMismatchedStepsDimensions() {
 AbstractSimplex simplex1 = new AbstractSimplex(new double[]{1.0, 2.0, 3.0, 4.0});
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexNonPositiveNumberOfPoints() {
 new AbstractSimplex(-1);
 }

 @Test(expected = MathIllegalArgumentException.class)
 public void testAbstractSimplexEqualVertices() {
 AbstractSimplex simplex1 = new AbstractSimplex(new double[]{0.0, 0.0, 0.0});
 }

 @Test
 public void testAbstractSimplexCreateWithReferenceSimplex() {
 AbstractSimplex simplex2 = new AbstractSimplex(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {0.5, 0.5}});
 assertEquals(2, simplex2.getDimension());
 assertEquals(3, simplex2.getSize());
 }

 @Test
 public void testBuild() {
 double[] startPoint = new double[]{0.0, 0.0, 0.0};
 simplex.build(startPoint);
 PointValuePair[] points = simplex.getPoints();
 assertArrayEquals(new double[][]{startPoint, new double[]{startPoint[0] + simplex.startConfiguration[0][0], startPoint[1] + simplex.startConfiguration[0][1], startPoint[2] + simplex.startConfiguration[0][2]}}, points, 0.001);
 }

 @Test
 public void testEvaluate() {
 simplex.evaluate(function, null);
 PointValuePair worstPoint = simplex.getPoint(simplex.getSize() - 1);
 double worstValue = worstPoint.getValue();
 assertEquals(-10.0, worstValue, 0.001);
 }

 @Test
 public void testReplaceWorstPoint() {
 PointValuePair newPoint = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 5.0);
 simplex.replaceWorstPoint(newPoint, null);
 PointValuePair replacedPoint = simplex.getPoint(simplex.getSize() - 1);
 assertEquals(5.0, replacedPoint.getValue(), 0.001);
 }

 @Test
 public void testGetPointAndSetPoint() {
 PointValuePair point1 = simplex.getPoint(1);
 double value1 = point1.getValue();
 simplex.setPoint(1, new PointValuePair(new double[]{2.0, 2.0, 2.0}, 3.0));
 PointValuePair point2 = simplex.getPoint(1);
 assertEquals(3.0, point2.getValue(), 0.001);
 assertEquals(point1.getPointRef(), point2.getPointRef());
 simplex.setPoint(1, point1);
 assertEquals(value1, point1.getValue(), 0.001);
 }

 @Test
 public void testGetPointsAndSetPoints() {
 PointValuePair[] points1 = simplex.getPoints();
 simplex.setPoints(new PointValuePair[]{new PointValuePair(new double[]{2.0, 2.0, 2.0}, 3.0), new PointValuePair(new double[]{
 3.0, 3.0, 3.0}, 4.0)});
 PointValuePair[] points2 = simplex.getPoints();
 assertArrayEquals(new PointValuePair[]{new PointValuePair(new double[]{2.0, 2.0, 2.0}, 3.0), new PointValuePair(new double[]{3.0, 3.0, 3.0}, 4.0)}, points2, 0.001);
 }
}