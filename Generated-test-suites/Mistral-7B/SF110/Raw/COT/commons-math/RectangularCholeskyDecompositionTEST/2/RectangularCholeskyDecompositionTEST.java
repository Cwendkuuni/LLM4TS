package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.RealMatrix;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.ArrayUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 private static final double SMALL_VALUE = 1e-15;

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithNonPositiveDefiniteMatrix() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
  try {
   new RectangularCholeskyDecomposition(matrix);
   fail("NonPositiveDefiniteMatrixException not thrown");
  } catch (NonPositiveDefiniteMatrixException e) {
   // expected exception
  }
 }

 @Test
 public void testConstructorWithPositiveDefiniteMatrix() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
  RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
  RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 0.0}, {0.0, 1.0 / Math.sqrt(2.0)}});
  RealMatrix actualRoot = decomposition.getRootMatrix();
  assertTrue(ArrayUtils.isEqual(expectedRoot.getData(), actualRoot.getData()));
  assertEquals(2, decomposition.getRank());
 }

 @Test
 public void testConstructorWithSingularMatrix() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 0.0}});
  try {
   new RectangularCholeskyDecomposition(matrix);
   fail("NonPositiveDefiniteMatrixException not thrown");
  } catch (NonPositiveDefiniteMatrixException e) {
   // expected exception
  }
 }

 @Test
 public void testGetRank() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
  RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
  assertEquals(2, decomposition.getRank());

  RealMatrix singularMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 0.0}});
  try {
   new RectangularCholeskyDecomposition(singularMatrix);
   fail("NonPositiveDefiniteMatrixException not thrown");
  } catch (NonPositiveDefiniteMatrixException e) {
   // expected exception
  }
 }

 @Test
 public void testGetRootMatrix() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
  RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
  RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 0.0}, {0.0, 1.0 / Math.sqrt(2.0)}});
  RealMatrix actualRoot = decomposition.getRootMatrix();
  assertTrue(ArrayUtils.isEqual(expectedRoot.getData(), actualRoot.getData()));

  RealMatrix singularMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 0.0}});
  try {
   new RectangularCholeskyDecomposition(singularMatrix);
   fail("NonPositiveDefiniteMatrixException not thrown");
  } catch (NonPositiveDefiniteMatrixException e) {
   // expected exception
  }
 }

 @Test
 public void testGetRootMatrixWithSmallValues() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0e-10}});
  RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-15);
  RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 0.0}, {0.0, 1.0 / Math.sqrt(2.0)}});
  RealMatrix actualRoot = decomposition.getRootMatrix();
  assertTrue(ArrayUtils.isEqual(expectedRoot.getData(), actualRoot.getData()));
  assertEquals(2, decomposition.getRank());
 }
}