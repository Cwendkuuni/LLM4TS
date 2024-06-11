package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.ArrayUtils;
import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testEmptyMatrix() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[0][0]);
 try {
 new RectangularCholeskyDecomposition(matrix);
 Assert.fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testIdentityMatrix() {
 RealMatrix matrix = MatrixUtils.createIdentityMatrix(5);
 try {
 RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decompositon.getRootMatrix();
 int rank = decompositon.getRank();
 Assert.assertEquals(5, rank);
 Assert.assertArrayEquals(new double[][]{
 new double[]{1.0, 0.0, 0.0, 0.0, 0.0},
 new double[]{0.0, 1.0, 0.0, 0.0, 0.0},
 new double[]{0.0, 0.0, 1.0, 0.0, 0.0},
 new double[]{0.0, 0.0, 0.0, 1.0, 0.0},
 new double[]{0.0, 0.0, 0.0, 0.0, 1.0}
 }, rootMatrix.getData(), 0.0);
 } catch (NonPositiveDefiniteMatrixException e) {
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSingularMatrix() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
 new double[]{1.0, 0.0},
 new double[]{0.0, 0.0}
 });
 try {
 new RectangularCholeskyDecomposition(matrix);
 Assert.fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testNonSymmetricMatrix() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
 new double[]{1.0, 2.0},
 new double[]{2.0, 3.0}
 });
 try {
 new RectangularCholeskyDecomposition(matrix);
 Assert.fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testPositiveDefiniteMatrix() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
 new double[]{4.0, 2.0, 4.0},
 new double[]{2.0, 1.0, 2.0},
 new double[]{4.0, 2.0, 16.0}
 });
 try {
 RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decompositon.getRootMatrix();
 int rank = decompositon.getRank();
 Assert.assertEquals(3, rank);
 double epsilon = 1e-12;
 Assert.assertArrayEquals(new double[][]{
 new double[]{1.0, 1.0, 0.0},
 new double[]{1.0, (1.0 + epsilon), 1.0},
 new double[]{0.0, 1.0, 4.0}
 }, rootMatrix.getData(), epsilon);
 } catch (NonPositiveDefiniteMatrixException e) {
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testLargeMatrix() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
 new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0},
 new double[]{2.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0},
 new double[]{3.0, 2.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0},
 new double[]{4.0, 3.0, 2.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0},
 new double[]{5.0, 4.0, 3.0, 2.0, 1.0, 2.0, 3.0, 4.0, 5.0},
 new double[]{6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 2.0, 3.0, 4.0},
 new double[]{7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 2.0, 3.0},
 new double[]{8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 2.0},
 new double[]{9.0, 8.0, 7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0}
 });
 try {
 RectangularCholeskyDecomposition decompositon = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decompositon.getRootMatrix();
 int rank = decompositon.getRank();
 Assert.assertEquals(9, rank);
 double epsilon = 1e-12;
 Assert.assertArrayEquals(expected, rootMatrix.getData(), epsilon);
 } catch (NonPositiveDefiniteMatrixException e) {
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 private static final double[][] expected = new double[][]{
 new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
 new double[]{1.0, (1.0 + Precision.EPSILON), (1.0 + Precision.EPSILON), (1.0 + Precision.EPSILON), (1.0 + Precision.EPSILON), (1.0 + Precision.EPSILON), (1.0 + Precision.EPSILON), (1.0 + Precision.EPSILON), (1.0 + Precision.EPSILON)},
 new double[]{1.0, (1.0 + Precision.EPSILON), (1.0 + 2.0*Precision.EPSILON), (1.0 + 2.0*Precision.EPSILON), (1.0 + 3.0*Precision.EPSILON), (1.0 + 3.0*Precision.EPSILON), (1.0 + 4.0*Precision.EPSILON), (1.0 + 4.0*Precision.EPSILON), (1.0 + 5.0*Precision.EPSILON)},
 new double[]{0.0, 0.0, (3.0 + 2.0*Precision.EPSILON), (3.0 + 3.0*Precision.EPSILON), (3.0 + 4.0*Precision.EPSILON), (3.0 + 4.0*Precision.EPSILON), (3.0 + 5.0*Precision.EPSILON), (3.0 + 5.0*Precision.EPSILON), (3.0 + 6.0*Precision.EPSILON)},
 new double[]{0.0, 0.0, 0.0, (4.0 + 3.0*Precision.EPSILON), (4.0 + 4.0*Precision.EPSILON), (4.0 + 4.0*Precision.EPSILON), (4.0 + 5.0*Precision.EPSILON), (4.0 + 5.0*Precision.EPSILON), (4.0 + 6.0*Precision.EPSILON)},
 new double[]{0.0, 0.0, 0.0, 0.0, (5.0 + 4.0*Precision.EPSILON), (5.0 + 5.0*Precision.EPSILON), (5.0 + 5.0*Precision.EPSILON), (5.0 + 6.0*Precision.EPSILON), (5.0 + 6.0*Precision.EPSILON)},
 new double[]{0.0, 0.0, 0.0, 0.0, 0.0, (6.0 + 5.0*Precision.EPSILON), (6.0 + 6.0*Precision.EPSILON), (6.0 + 6.0*Precision.EPSILON), (6.0 + 7.0*Precision.EPSILON)},
 new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, (7.0 + 6.0*Precision.EPSILON), (7.0 + 7.0*Precision.EPSILON), (7.0 + 7.0*Precision.EPSILON)},
 new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, (8.0 + 7.0*Precision.EPSILON), (8.0 + 8.0*Precision.EPSILON)}
 };

}