package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

 @Test
 public void testAbstractSimplexWithZeroArguments() {
 try {
 new AbstractSimplex(0);
 fail("Should have thrown an exception with zero arguments");
 } catch (ZeroException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithNullArguments() {
 try {
 new AbstractSimplex(null);
 fail("Should have thrown an exception with null arguments");
 } catch (NullArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithOneDimension() {
 final AbstractSimplex simplex = new AbstractSimplex(1);
 assertEquals(1, simplex.getDimension());
 assertEquals(2, simplex.getSize());
 }

 @Test
 public void testAbstractSimplexWithTwoDimensionsAndValidArguments() {
 final double[] steps = {1.0, 1.0};
 final AbstractSimplex simplex = new AbstractSimplex(steps);
 assertEquals(2, simplex.getDimension());
 assertEquals(3, simplex.getSize());
 }

 @Test(expected = ZeroException.class)
 public void testAbstractSimplexWithTwoDimensionsAndZeroStep() {
 final double[] steps = {1.0, 0.0};
 try {
 new AbstractSimplex(steps);
 } catch (ZeroException e) {
 // Expected
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexWithInvalidArguments() {
 final double[] steps = {1.0, 1.0, 1.0};
 try {
 new AbstractSimplex(steps);
 } catch (DimensionMismatchException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexWithReferenceSimplex() {
 final double[][] referenceSimplex = {
 {1.0, 1.0},
 {2.0, 1.0},
 {1.0, 2.0},
 {2.0, 2.0}
 };
 final AbstractSimplex simplex = new AbstractSimplex(referenceSimplex);
 assertEquals(2, simplex.getDimension());
 assertEquals(4, simplex.getSize());
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testAbstractSimplexWithEmptyReferenceSimplex() {
 try {
 new AbstractSimplex(new double[0][0]);
 } catch (NotStrictlyPositiveException e) {
 // Expected
 }
 }

 @Test(expected = DimensionMismatchException.class)
 public void testAbstractSimplexWithInvalidReferenceSimplex() {
 final double[][] referenceSimplex = {
 {1.0, 1.0},
 {2.0, 1.0},
 {1.0, 2.0},
 {1.0, 2.0}
 };
 try {
 new AbstractSimplex(referenceSimplex);
 } catch (DimensionMismatchException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractSimplexGettersAndSetters() {
 final AbstractSimplex simplex = new AbstractSimplex(new int[0]);
 simplex.setDimension(3);
 simplex.setSize(7);
 assertEquals(3, simplex.getDimension());
 assertEquals(7, simplex.getSize());
 }

 @Test
 public void testAbstractSimplexSetPoints() {
 final AbstractSimplex simplex = new AbstractSimplex(new int[0]);
 simplex.setDimension(3);
 simplex.setSize(7);
 final PointValuePair[] points = new PointValuePair[7];
 for (int i = 0; i < 7; ++i) {
 points[i] = new PointValuePair(new double[3], Double.NaN);
 }
 simplex.setPoints(points);
 assertEquals(points, simplex.getPoints());
 }

 @Test
 public void testAbstractSimplexGetPoint() {
 final AbstractSimplex simplex = new AbstractSimplex(new int[0]);
 simplex.setDimension(3);
 simplex.setSize(7);
 final PointValuePair point = new PointValuePair(new double[3], 0.0);
 simplex.setPoint(0, point);
 assertEquals(point, simplex.getPoint(0));
 }
}