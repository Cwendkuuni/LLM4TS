package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import static org.junit.Assert.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import java.util.*;

public class AbstractSimplexTest {

 private AbstractSimplex abstractSimplex;
 private MultivariateFunction testFunction;
 private Comparator<PointValuePair> comparator;

 @Test
 public void testAbstractSimplexConstruction() {
     abstractSimplex = new AbstractSimplex(3);
     assertEquals(3, abstractSimplex.getDimension());
     assertArrayEquals(new double[][]{
              {0.0, 0.0, 0.0}, {0.0, 0.0, 1.0}, {0.0, 0.0, 2.0}, {0.0, 1.0, 0.0},
              {0.0, 1.0, 1.0}, {0.0, 1.0, 2.0}, {0.0, 2.0, 0.0}, {0.0, 2.0, 1.0},
              {0.0, 2.0, 2.0}, {1.0, 0.0, 0.0}, {1.0, 0.0, 1.0}, {1.0, 0.0, 2.0},
              {1.0, 1.0, 0.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 2.0}, {1.0, 2.0, 0.0},
              {1.0, 2.0, 1.0}, {1.0, 2.0, 2.0}, {2.0, 0.0, 0.0}, {2.0, 0.0, 1.0},
              {2.0, 0.0, 2.0}
         }, abstractSimplex.getPoints());

     abstractSimplex = new AbstractSimplex(3, 1.0);
     assertEquals(3, abstractSimplex.getDimension());
     assertArrayEquals(new double[][]{
              {0.0, 0.0, 0.0}, {0.0, 0.0, 1.0}, {0.0, 0.0, 2.0}, {0.0, 1.0, 0.0},
              {0.0, 1.0, 1.0}, {0.0, 1.0, 2.0}, {0.0, 2.0, 0.0}, {0.0, 2.0, 1.0},
              {0.0, 2.0, 2.0}, {1.0, 0.0, 0.0}, {1.0, 0.0, 1.0}, {1.0, 0.0, 2.0},
              {1.0, 1.0, 0.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 2.0}, {1.0, 2.0, 0.0},
              {1.0, 2.0, 1.0}, {1.0, 2.0, 2.0}, {2.0, 0.0, 0.0}, {2.0, 0.0, 1.0},
              {2.0, 0.0, 2.0}
         }, abstractSimplex.getPoints());
 }

 @Test
 public void testAbstractSimplexFromReferencePoints() {
     double[][] referenceSimplex = new double[][]{
             {0.0, 0.0, 0.0}, {0.0, 1.0, 1.0}, {1.0, 0.0, 0.0}, {1.0, 1.0, 1.0}
     };
     abstractSimplex = new AbstractSimplex(referenceSimplex);
     assertEquals(3, abstractSimplex.getDimension());
     assertArrayEquals(referenceSimplex, abstractSimplex.getStartConfiguration());
     assertArrayEquals(new double[][]{
             {0.0, 0.0, 0.0}, {0.0, 1.0, 1.0}, {1.0, 0.0, 0.0}, {1.0, 1.0, 1.0}
         }, abstractSimplex.getPoints());
 }

 @Test(expected = NullArgumentException.class)
 public void testAbstractSimplexNullStartConfiguration() {
     try {
         abstractSimplex = new AbstractSimplex(null);
     } catch (NullArgumentException e) {
         assertEquals("startConfiguration", e.getMessage());
     }
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexEmptyStartConfiguration() {
     try {
         abstractSimplex = new AbstractSimplex(new double[0][0]);
     } catch (ZeroException e) {
         assertEquals("Number of dimensions of the simplex", e.getMessage());
     }
 }

 @Test
 public void testAbstractSimplexBuild() {
     abstractSimplex = new AbstractSimplex(4);
     double[] startPoint = new double[]{0.0, 0.0, 0.0, 0.0};
     abstractSimplex.build(startPoint);
     assertArrayEquals(new double[][]{
             {0.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 0.0, 1.0}, {0.0, 0.0, 1.0, 0.0},
             {0.0, 0.0, 1.0, 1.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 1.0},
             {0.0, 1.0, 1.0, 0.0}, {0.0, 1.0, 1.0, 1.0}, {1.0, 0.0, 0.0, 0.0},
             {1.0, 0.0, 0.0, 1.0}, {1.0, 0.0, 1.0, 0.0}, {1.0, 0.0, 1.0, 1.0},
             {1.0, 1.0, 0.0, 0.0}, {1.0, 1.0, 0.0, 1.0}, {1.0, 1.0, 1.0, 0.0},
             {1.0, 1.0, 1.0, 1.0}
         }, abstractSimplex.getPoints());
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexBuildInvalidStartPoint() {
     try {
         abstractSimplex = new AbstractSimplex(3);
         double[] startPoint = new double[]{0.0, 1.0, 1.0, 1.0};
         abstractSimplex.build(startPoint);
     } catch (DimensionMismatchException e) {
         assertEquals("Number of dimensions of the simplex", e.getMessage());
     }
 }

 @Test
 public void testAbstractSimplexEvaluate() {
     testFunction = new ConstMultivariateFunction(new double[]{1.0, 2.0, 3.0});
     comparator = new Comparator<PointValuePair>() {
         @Override
         public int compare(PointValuePair o1, PointValuePair o2) {
             return Double.compare(o1.getValue(), o2.getValue());
         }
     };
     abstractSimplex = new AbstractSimplex(3);
     abstractSimplex.build(new double[]{0.0, 0.0, 0.0});
     abstractSimplex.evaluate(testFunction, comparator);
     assertEquals(1, abstractSimplex.getSize());
     assertArrayEquals(new double[]{0.0, 0.0, 0.0}, abstractSimplex.getPoint(0).getPointRef());
     assertEquals(1.0, abstractSimplex.getPoint(0).getValue(), 0.001);
 }

 @Test
 public void testAbstractSimplexReplaceWorstPoint() {
     testFunction = new ConstMultivariateFunction(new double[]{1.0, 2.0, 3.0});
     comparator = new Comparator<PointValuePair>() {
         @Override
         public int compare(PointValuePair o1, PointValuePair o2) {
             return Double.compare(o1.getValue(), o2.getValue());
         }
     };
     abstractSimplex = new AbstractSimplex(3);
     abstractSimplex.build(new double[]{0.0, 0.0, 0.0});
     abstractSimplex.evaluate(testFunction, comparator);
     PointValuePair worstPoint = abstractSimplex.getPoint(0);
     double worstValue = worstPoint.getValue();
     abstractSimplex.replaceWorstPoint(new PointValuePair(new double[]{1.0, 1.0, 1.0}, worstValue), comparator);
     assertEquals(1, abstractSimplex.getSize());
     assertArrayEquals(new double[]{1.0, 1.0, 1.0}, abstractSimplex.getPoint(0).getPointRef());
     assertEquals(worstValue, abstractSimplex.getPoint(1).getValue(), 0.001);
 }
}