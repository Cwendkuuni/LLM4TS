package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testEmptyMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("NonPositiveDefiniteMatrixException should be thrown for empty matrix");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testSingularMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {0.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("NonPositiveDefiniteMatrixException should be thrown for singular matrix");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testPositiveDefiniteMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = decomposition.getRootMatrix();
 RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{FastMath.SQRT(1.0), 0.0}, {0.5, FastMath.SQRT(0.5)}});
 assertArrayEquals("Incorrect root matrix.", expected.getData(), root.getData(), 0.0);
 }

 @Test
 public void testNonPositiveDefiniteMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, -0.5}, {-0.5, 1.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("NonPositiveDefiniteMatrixException should be thrown for non-positive definite matrix");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testNonPositiveDefiniteMatrixWithSmallPivot() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, -0.5}, {-0.5, 1.0}, {0.0, 0.0}});
 try {
 new RectangularCholeskyDecomposition(matrix, 1e-10);
 fail("NonPositiveDefiniteMatrixException should be thrown for non-positive definite matrix with small pivot");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testSwapRows() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 1.0},{1.0, 2.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = decomposition.getRootMatrix();
 RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{FastMath.SQRT(3.0), 0.0}, {0.0, FastMath.SQRT(2.0)}});
 assertArrayEquals("Incorrect root matrix.", expected.getData(), root.getData(), 0.0);
 }

 @Test
 public void testRankLowerThanExpected() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.0}, {0.5, 1.0, 0.5}, {0.0, 0.5, 1.0}});
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 assertEquals("Incorrect rank.", 2, decomposition.getRank());
 }

 @Test
 public void testRankHigherThanExpected() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.0, 0.0}, {0.5, 1.0, 0.5, 0.5}, {0.0, 0.5, 1.0, 1.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("NonPositiveDefiniteMatrixException should be thrown for matrix with rank higher than expected");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }
}