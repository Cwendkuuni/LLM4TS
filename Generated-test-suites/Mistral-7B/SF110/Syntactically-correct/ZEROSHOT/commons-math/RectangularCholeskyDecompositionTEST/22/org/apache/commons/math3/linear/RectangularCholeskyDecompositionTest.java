package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.Arrays2D;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testConstructorWithPositiveDefiniteMatrix() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 Assert.assertEquals(6.0, rootMatrix.getEntry(0, 0), 1e-12);
 Assert.assertEquals(0.0, rootMatrix.getEntry(0, 1), 1e-12);
 Assert.assertEquals(0.0, rootMatrix.getEntry(0, 2), 1e-12);
 Assert.assertEquals(1.4142135625, rootMatrix.getEntry(1, 0), 1e-12);
 Assert.assertEquals(1.4142135625, rootMatrix.getEntry(1, 1), 1e-12);
 Assert.assertEquals(0.0, rootMatrix.getEntry(1, 2), 1e-12);
 Assert.assertEquals(1.4142135625, rootMatrix.getEntry(2, 0), 1e-12);
 Assert.assertEquals(1.4142135625, rootMatrix.getEntry(2, 1), 1e-12);
 Assert.assertEquals(2.4142135625, rootMatrix.getEntry(2, 2), 1e-12);
 Assert.assertEquals(3.0, rank);
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithNegativeDefiniteMatrix() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -4.0, 5.0}, {3.0, 5.0, -6.0}});
 new RectangularCholeskyDecomposition(matrix);
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithSingularMatrix() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 0.0}, {3.0, 0.0, 0.0}});
 new RectangularCholeskyDecomposition(matrix, 1e-14);
 }

 @Test
 public void testGetRootMatrix() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();

 Assert.assertEquals(3, rootMatrix.getRowDimension());
 Assert.assertEquals(3, rootMatrix.getColumnDimension());

 Assert.assertArrayEquals(new double[][]{
 {6.0, 0.0, 0.0},
 {1.4142135625, 1.4142135625, 0.0},
 {1.4142135625, 0.0, 2.4142135625}},
 Arrays2D.toPrimitive(rootMatrix.getData()), 1e-12);
 }

 @Test
 public void testGetRank() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 int rank = decomposition.getRank();

 Assert.assertEquals(3, rank);
 }
}