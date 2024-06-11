package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.linear.MatrixUtils.createRealMatrix;
import static org.apache.commons.math3.linear.RealMatrixUtils.eye;
import static org.apache.commons.math3.linear.RealMatrixUtils.subtract;

public class RectangularCholeskyDecompositionTest {

 private static final double EPSILON = 1e-10;

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithNegativeDefiniteMatrix() {
     RealMatrix matrix = subtract(eye(3), createRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 1.0, 1.0}, {2.0, 1.0, -1.0}}));
     try {
         new RectangularCholeskyDecomposition(matrix);
         fail("Expected NonPositiveDefiniteMatrixException");
     } catch (NonPositiveDefiniteMatrixException e) {
         // expected
     }
 }

 @Test
 public void testConstructorWithPositiveDefiniteMatrix() {
     RealMatrix matrix = createRealMatrix(new double[][]{{3.0, 2.0, 2.0}, {2.0, 3.0, 2.0}, {2.0, 2.0, 3.0}});
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
     RealMatrix expected = createRealMatrix(new double[][]{{1.0 / Math.sqrt(3.0), 0.0, 0.0},
     {0.5 * (1.0 / Math.sqrt(3.0)) * (1.0 / Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0), 0.0},
     {0.5 * (1.0 / Math.sqrt(3.0)) * (1.0 / Math.sqrt(2.0)), -0.5 * (1.0 / Math.sqrt(2.0)) * (1.0 / Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0)}});
     Assert.assertEquals(expected, decomposition.getRootMatrix());
 }

 @Test
 public void testConstructorWithSmallMatrix() {
     RealMatrix matrix = createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
     try {
         new RectangularCholeskyDecomposition(matrix, 1e-5);
         fail("Expected NonPositiveDefiniteMatrixException");
     } catch (NonPositiveDefiniteMatrixException e) {
         // expected
     }
 }

 @Test
 public void testGetRank() {
     RealMatrix matrix = createRealMatrix(new double[][]{{3.0, 2.0, 2.0}, {2.0, 3.0, 2.0}, {2.0, 2.0, 3.0}});
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
     Assert.assertEquals(3, decomposition.getRank());
 }

 @Test
 public void testGetRootMatrix() {
     RealMatrix matrix = createRealMatrix(new double[][]{{3.0, 2.0, 2.0}, {2.0, 3.0, 2.0}, {2.0, 2.0, 3.0}});
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
     RealMatrix expected = createRealMatrix(new double[][]{{1.0 / Math.sqrt(3.0), 0.0, 0.0},
     {0.5 * (1.0 / Math.sqrt(3.0)) * (1.0 / Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0), 0.0},
     {0.5 * (1.0 / Math.sqrt(3.0)) * (1.0 / Math.sqrt(2.0)), -0.5 * (1.0 / Math.sqrt(2.0)) * (1.0 / Math.sqrt(2.0)), 1.0 / Math.sqrt(2.0)}});
     Assert.assertEquals(expected, decomposition.getRootMatrix());
 }
}