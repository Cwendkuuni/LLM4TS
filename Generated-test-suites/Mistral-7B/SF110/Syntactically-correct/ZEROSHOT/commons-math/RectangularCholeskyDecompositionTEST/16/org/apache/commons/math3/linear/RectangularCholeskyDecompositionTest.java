package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 private static final double SMALL_VALUE = 1e-10;

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testExceptionThrownForNonPositiveDefiniteMatrix() {
     RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -1.0, 4.0}, {3.0, 4.0, -1.0}});
     try {
         new RectangularCholeskyDecomposition(matrix);
         Assert.fail();
     } catch (NonPositiveDefiniteMatrixException e) {
         // expected
     }
 }

 @Test
 public void testDecompositionWithPositiveDefiniteMatrix() {
     RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 4.0}, {3.0, 4.0, 10.0}});
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
     RealMatrix root = decomposition.getRootMatrix();
     int rank = decomposition.getRank();
     Assert.assertEquals(3, rank);
     Assert.assertArrayEquals(new double[][]{
             {1.0, 0.0, 0.0},
             {0.44948974278317, 0.81649657998137, 0.0},
             {0.0, 0.74248003273891, 2.0}
     }, root.getData(), 0.0);
 }

 @Test
 public void testDecompositionWithSymmetricMatrix() {
     RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
     RealMatrix root = decomposition.getRootMatrix();
     int rank = decomposition.getRank();
     Assert.assertEquals(2, rank);
     Assert.assertArrayEquals(new double[][]{
             {1.0, 0.0},
             {0.44948974278317, 0.81649657998137}
     }, root.getData(), 0.0);
 }

 @Test
 public void testDecompositionWithIdentityMatrix() {
     RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
     RealMatrix root = decomposition.getRootMatrix();
     int rank = decomposition.getRank();
     Assert.assertEquals(3, rank);
     Assert.assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}, root.getData(), 0.0);
 }

 @Test
 public void testDecompositionWithSingularMatrix() {
     RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
     try {
         new RectangularCholeskyDecomposition(matrix);
         Assert.fail();
     } catch (NonPositiveDefiniteMatrixException e) {
         // expected
     }
 }

 @Test
 public void testDecompositionWithDiagonalMatrix() {
     RealMatrix matrix = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 5.0, 10.0});
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
     RealMatrix root = decomposition.getRootMatrix();
     int rank = decomposition.getRank();
     Assert.assertEquals(3, rank);
     Assert.assertArrayEquals(new double[][]{
             {1.0, 0.0, 0.0},
             {0.0, 2.23606797749979, 0.0},
             {0.0, 0.0, 3.16227766016838}
     }, root.getData(), 0.0);
 }

 @Test
 public void testDecompositionWithDiagonalDominantMatrix() {
     RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2.1, 1.0, 0.0}, {1.0, 2.1, 0.0}, {0.0, 0.0, 0.1}});
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
     RealMatrix root = decomposition.getRootMatrix();
     int rank = decomposition.getRank();
     Assert.assertEquals(3, rank);
     Assert.assertArrayEquals(new double[][]{
             {1.0, 0.0, 0.0},
             {0.59217686318679, 0.73831997285961, 0.0},
             {0.0, 0.0, 0.1}
     }, root.getData(), 0.0);
 }

 @Test
 public void testDecompositionWithLargeMatrix() {
     int size = 100;
     RealMatrix matrix = MatrixUtils.createRealMatrix(size, size);
     for (int i = 0; i < size; i++) {
         for (int j = 0; j < size; j++) {
             if (i == j) {
                 matrix.setEntry(i, j, 10.0 + i);
             } else if (Math.abs(i - j) <= 5) {
                 matrix.setEntry(i, j, 1.0);
             } else {
                 matrix.setEntry(i, j, 0.0);
             }
         }
     }
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, SMALL_VALUE);
     RealMatrix root = decomposition.getRootMatrix();
     int rank = decomposition.getRank();
     Assert.assertTrue(rank >= size - 5);
 }
}