package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 private static final double SMALL = 1e-12;

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNonPositiveDefiniteMatrixException() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
  try {
   new RectangularCholeskyDecomposition(matrix);
   fail();
  } catch (NonPositiveDefiniteMatrixException e) {
   //expected exception
  }
 }

 @Test
 public void testDecomposition() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
  try {
   RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
   RealMatrix root = rcd.getRootMatrix();
   int rank = rcd.getRank();
   assertEquals(6, root.getRowDimension());
   assertEquals(3, root.getColumnDimension());
   assertEquals(3, rank);
   // check if the matrix is Cholesky decomposed
   RealMatrix original = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
   RealMatrix product = original.multiply(root);
   RealMatrix productTranspose = root.transpose().multiply(original);
   assertTrue(Precision.equals(product, productTranspose, SMALL));
  } catch (NonPositiveDefiniteMatrixException e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testDecompositionSmallMatrix() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
  try {
   RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, PrecisionUtils.EPSILON);
   RealMatrix root = rcd.getRootMatrix();
   int rank = rcd.getRank();
   assertEquals(2, root.getRowDimension());
   assertEquals(2, root.getColumnDimension());
   assertEquals(2, rank);
   // check if the matrix is Cholesky decomposed
   RealMatrix original = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
   RealMatrix product = original.multiply(root);
   RealMatrix productTranspose = root.transpose().multiply(original);
   assertTrue(Precision.equals(product, productTranspose, 1e-15));
  } catch (NonPositiveDefiniteMatrixException e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test
 public void testIdentityMatrix() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
  try {
   RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
   RealMatrix root = rcd.getRootMatrix();
   int rank = rcd.getRank();
   assertEquals(2, root.getRowDimension());
   assertEquals(2, root.getColumnDimension());
   assertEquals(2, rank);
   // check if the matrix is Cholesky decomposed
   RealMatrix original = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
   RealMatrix product = original.multiply(root);
   RealMatrix productTranspose = root.transpose().multiply(original);
   assertTrue(Precision.equals(product, productTranspose));
  } catch (NonPositiveDefiniteMatrixException e) {
   fail("Unexpected exception: " + e.getMessage());
  }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testSingularMatrix() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
  try {
   new RectangularCholeskyDecomposition(matrix);
   fail();
  } catch (NonPositiveDefiniteMatrixException e) {
   //expected exception
  }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNegativeMatrix() {
  RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 1.0}, {1.0, -1.0}});
  try {
   new RectangularCholeskyDecomposition(matrix);
   fail();
  } catch (NonPositiveDefiniteMatrixException e) {
   //expected exception
  }
 }

}