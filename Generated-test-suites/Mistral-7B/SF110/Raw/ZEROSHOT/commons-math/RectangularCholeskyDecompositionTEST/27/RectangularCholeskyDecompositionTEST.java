package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testNonPositiveDefiniteMatrixException() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }

 @Test
 public void testDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 2.0, 2.0}, {2.0, 3.0, 1.0}, {2.0, 1.0, 3.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{1.0 / Precision.sqrt(4.0), 0.0, 0.0},
 {-0.5 * (1.0 / Precision.sqrt(4.0)) * 2.0 / 3.0, 1.0 / Precision.sqrt(3.0), 0.0},
 {-0.5 * (1.0 / Precision.sqrt(4.0)) * 2.0 / 3.0, 0.0, 1.0 / Precision.sqrt(3.0)}});

 assertEquals(expectedRootMatrix, rootMatrix);
 assertEquals(3, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected NonPositiveDefiniteMatrixException");
 }
 }

 @Test
 public void testIdentityMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});

 assertEquals(expectedRootMatrix, rootMatrix);
 assertEquals(2, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected NonPositiveDefiniteMatrixException");
 }
 }

 @Test
 public void testSymmetricPositiveDefiniteMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4.0, 2.0, 2.0}, {2.0, 3.0, 1.0}, {2.0, 1.0, 3.0},
 {2.0, 1.0, 1.0}, {2.0, 1.0, 1.0}, {1.0, 1.0, 3.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{1.0 / Precision.sqrt(4.0), 0.0, 0.0, 0.0, 0.0, 0.0},
 {-0.5 * (1.0 / Precision.sqrt(4.0)) * 2.0 / 3.0, 1.0 / Precision.sqrt(3.0), 0.0, 0.0, 0.0, 0.0},
 {-0.5 * (1.0 / Precision.sqrt(4.0)) * 2.0 / 3.0, 0.0, 1.0 / Precision.sqrt(3.0), 0.0, 0.0, 0.0},
 {-0.5 * (1.0 / Precision.sqrt(4.0)) * 2.0 / 3.0, 0.0, 0.0, 1.0 / Precision.sqrt(3.0), 0.0, 0.0},
 {-0.5 * (1.0 / Precision.sqrt(4.0)) * 2.0 / 3.0, 0.0, 0.0, 0.0, 1.0 / Precision.sqrt(3.0), 0.0},
 {-0.5 * (1.0 / Precision.sqrt(4.0)) * 2.0 / 3.0, 0.0, 0.0, 0.0, 0.0, 1.0 / Precision.sqrt(3.0)}});

 assertEquals(expectedRootMatrix, rootMatrix);
 assertEquals(6, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected NonPositiveDefiniteMatrixException");
 }
 }

 @Test
 public void testSingularMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();

 RealMatrix expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{1.0 / Math.sqrt(1.0), 0.0, 0.0},
 {0.0, 1.0 / Math.sqrt(1.0), 0.0},
 {0.0, 0.0, 1.0 / Math.sqrt(1.0)}});

 assertEquals(expectedRootMatrix, rootMatrix);
 assertEquals(3, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected NonPositiveDefiniteMatrixException");
 }
 }
}