package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.optim.nonlinear.scalar.go.AbstractGoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.go.PointValuePair;
import org.junit.Before;
import org.junit.Test;

public class AbstractSimplexTest {

 private AbstractSimplex simplex;
 private MultivariateFunction function;

 @Before
 public void setUp() {
 function = new QuadraticFunction(new double[]{-1.0, 1.0}, new double[]{-1.0, 1.0}, 1.0);
 simplex = new SimpleX(2);
 }

 @Test
 public void testAbstractSimplexConstruction() {
 assertEquals(2, simplex.getDimension());
 assertEquals(3, simplex.getSize());
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
 public void testAbstractSimplexZeroSteps() {
 new AbstractSimplex(new double[]{0.0});
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexMismatchedReferenceSimplex() {
 new AbstractSimplex(new double[][]{{1.0, 1.0}, {2.0, 2.0}});
 }

 @Test
 public void testAbstractSimplexReferenceSimplex() {
 simplex = new AbstractSimplex(new double[][]{{0.0, 0.0}, {1.0, 1.0}, {1.0, 2.0}});
 assertEquals(2, simplex.getDimension());
 assertEquals(3, simplex.getSize());
 }

 @Test
 public void testAbstractSimplexBuild() {
 simplex.build(new double[]{0.5, 0.5});
 assertEquals(3, simplex.getSize());
 for (PointValuePair p : simplex.getPoints()) {
 assertTrue(Arrays.equals(new double[]{0.5, 0.5}, p.getPointRef()));
 }
 }

 @Test
 public void testAbstractSimplexEvaluate() {
 simplex.build(new double[]{0.5, 0.5});
 simplex.evaluate(function, AbstractGoalType.MINIMIZE);
 for (PointValuePair p : simplex.getPoints()) {
 assertNotNull(p.getValue());
 }
 }

 @Test
 public void testAbstractSimplexReplaceWorstPoint() {
 PointValuePair worstPoint = simplex.getPoint(0);
 PointValuePair newPoint = new PointValuePair(new double[]{1.0, 1.0}, 1.0);
 simplex.replaceWorstPoint(newPoint, AbstractGoalType.MINIMIZE);
 assertNotEquals(worstPoint, simplex.getPoint(0));
 }

 @Test
 public void testAbstractSimplexSetPoints() {
 PointValuePair[] newPoints = new PointValuePair[]{new PointValuePair(new double[]{1.0, 1.0}, 1.0), new PointValuePair(new double[]{1.0, 2.0}, 2.0)};
 simplex.setPoints(newPoints);
 assertEquals(newPoints.length, simplex.getSize());
 }

}