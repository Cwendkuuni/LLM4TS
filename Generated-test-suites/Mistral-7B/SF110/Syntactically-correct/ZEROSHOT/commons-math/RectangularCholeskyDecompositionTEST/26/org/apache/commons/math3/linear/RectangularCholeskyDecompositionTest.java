package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.ArrayUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testExceptionWithSmallNegativeValue() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, -1.0, 1.0}, {1.0, 1.0, -1.0}});
 try {
 new RectangularCholeskyDecomposition(matrix, -1.0);
 fail("NonPositiveDefiniteMatrixException expected");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testExceptionWithNegativeDiagonalElement() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, -1.0, 1.0}, {1.0, 1.0, -3.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("NonPositiveDefiniteMatrixException expected");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testIdentityMatrix() {
 RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 assertEquals(3, decomposition.getRank());
 assertArrayEquals(new double[][]{
 new double[]{1.0, 0.0, 0.0},
 new double[]{0.0, 1.0, 0.0},
 new double[]{0.0, 0.0, 1.0}},
 rootMatrix.getData(), 0.0);
 }

 @Test
 public void testDiagonalMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{5.0, 0.0}, {0.0, 9.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 assertEquals(2, decomposition.getRank());
 assertArrayEquals(new double[][]{
 new double[]{1.0, 0.0},
 new double[]{0.0, 1.0}},
 rootMatrix.getData(), 0.0);
 }

 @Test
 public void testSymmetricPositiveDefiniteMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 2.0}, {2.0, 5.0, 2.0}, {2.0, 2.0, 4.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 assertEquals(3, decomposition.getRank());
 assertArrayEquals(new double[][]{
 new double[]{1.0, 0.5, 0.0},
 new double[]{0.5, 1.0, 0.5},
 new double[]{0.0, 0.5, 1.0}},
 rootMatrix.getData(), 0.0);
 }

 @Test
 public void testNegativeDiagonalElementMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, -1.0, 1.0}, {1.0, 1.0, -2.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("NonPositiveDefiniteMatrixException expected");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testSwapRows() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 assertEquals(3, decomposition.getRank());
 assertArrayEquals(new double[][]{
 new double[]{1.0, 0.0, 0.0},
 new double[]{0.0, 1.0, 0.0},
 new double[]{0.0, 0.0, 1.0}},
 rootMatrix.getData(), 0.0);
 }

 @Test
 public void testZeroDiagonalElement() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 0.0}});
 try {
 new RectangularCholeskyDecomposition(matrix, 0.0);
 fail("NonPositiveDefiniteMatrixException expected");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testNPDMWithSmallNegativeEigenvalues() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, -1.1, 1.0}, {1.0, 1.0, -1.1}});
 try {
 new RectangularCholeskyDecomposition(matrix, 1e-10);
 fail("NonPositiveDefiniteMatrixException expected");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testGetRank() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 1.0}, {1.0, 5.0, 1.0}, {1.0, 1.0, 4.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 assertEquals(3, decomposition.getRank());
 }
}