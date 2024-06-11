package org.apache.commons.math3.linear;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.jupiter.api.*;

@DisplayName("Testing RectangularCholeskyDecomposition class")
public class RectangularCholeskyDecompositionTest {

 private RealMatrix inputMatrix;
 private RealMatrix expectedRootMatrix;
 private RealMatrix actualRootMatrix;
 private int rank;

 @BeforeEach
 void setup() {
 inputMatrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 2.0, 3.0, 4.0},
 {2.0, 5.0, 6.0, 7.0},
 {3.0, 6.0, 10.0, 9.0},
 {4.0, 7.0, 9.0, 12.0}
 });
 expectedRootMatrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0, 0.0},
 {0.7071067811865476, 0.4225806451612952, 0.0, 0.0},
 {-0.5251332189379426, -0.216506355921117, 0.7071067811865476, 0.0},
 {-0.5251332189379426, -0.7125825381390951, 0.4225806451612952, 0.7071067811865476}
 });
 }

 @Test
 @DisplayName("Test constructor with positive definite matrix")
 void testConstructorPositiveDefinite() {
 try {
 RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(inputMatrix, 1e-9);
 actualRootMatrix = rectangularCholeskyDecomposition.getRootMatrix();
 rank = rectangularCholeskyDecomposition.getRank();
 assertArrayEquals(expectedRootMatrix.getData(), actualRootMatrix.getData(), 1e-9);
 assertEquals(expectedRootMatrix.getRowDimension(), rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 @DisplayName("Test constructor with singular matrix")
 void testConstructorSingular() {
 RealMatrix singularMatrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 2.0, 3.0, 4.0},
 {2.0, 1.0, 0.0, 5.0},
 {3.0, 0.0, 0.0, 7.0},
 {4.0, 5.0, 7.0, 1.0}
 });
 try {
 RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(singularMatrix, 1e-9);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 @DisplayName("Test getRootMatrix method")
 void testGetRootMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 2.0, 3.0, 4.0},
 {2.0, 5.0, 6.0, 7.0},
 {3.0, 6.0, 10.0, 9.0},
 {4.0, 7.0, 9.0, 12.0}
 });
 try {
 RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, 1e-9);
 actualRootMatrix = rectangularCholeskyDecomposition.getRootMatrix();
 assertArrayEquals(expectedRootMatrix.getData(), actualRootMatrix.getData(), 1e-9);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 @DisplayName("Test getRank method")
 void testGetRank() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 2.0, 3.0, 4.0},
 {2.0, 5.0, 6.0, 7.0},
 {3.0, 6.0, 10.0, 9.0},
 {4.0, 7.0, 9.0, 12.0}
 });
 try {
 RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, 1e-9);
 rank = rectangularCholeskyDecomposition.getRank();
 assertEquals(3, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 @DisplayName("Test constructor with small eigenvalues")
 void testConstructorSmallEigenvalues() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 2.0, 3.0, 4.0},
 {2.0, 5.0, 6.0, 7.0},
 {3.0, 6.0, 9.99998E-5, 9.0},
 {4.0, 7.0, 9.0, 12.0}
 });
 try {
 RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(matrix, 1e-9);
 actualRootMatrix = rectangularCholeskyDecomposition.getRootMatrix();
 rank = rectangularCholeskyDecomposition.getRank();
 assertArrayEquals(expectedRootMatrix.getData(), actualRootMatrix.getData(), 1e-9);
 assertEquals(3, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 @DisplayName("Test constructor with non-positive definite matrix")
 void testConstructorNonPositiveDefinite() {
 RealMatrix nonPositiveDefiniteMatrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 2.0, 3.0, 4.0},
 {2.0, -1.0, 6.0, 7.0},
 {3.0, 6.0, -10.0, 9.0},
 {4.0, 7.0, 9.0, -12.0}
 });
 try {
 RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(nonPositiveDefiniteMatrix, 1e-9);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 @DisplayName("Test constructor with diagonal matrix")
 void testConstructorDiagonal() {
 RealMatrix diagonalMatrix = new Array2DRowRealMatrix(new double[][] {
 {1.0, 0.0, 0.0, 0.0},
 {0.0, 5.0, 0.0, 0.0},
 {0.0, 0.0, 10.0, 0.0},
 {0.0, 0.0, 0.0, 12.0}
 });
 try {
 RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(diagonalMatrix, 1e-9);
 actualRootMatrix = rectangularCholeskyDecomposition.getRootMatrix();
 rank = rectangularCholeskyDecomposition.getRank();
 assertArrayEquals(expectedRootMatrix.getData(), actualRootMatrix.getData(), 1e-9);
 assertEquals(4, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 @DisplayName("Test constructor with identity matrix")
 void testConstructorIdentity() {
 RealMatrix identityMatrix = MatrixUtils.createIdentityMatrix(4);
 try {
 RectangularCholeskyDecomposition rectangularCholeskyDecomposition = new RectangularCholeskyDecomposition(identityMatrix, 1e-9);
 actualRootMatrix = rectangularCholeskyDecomposition.getRootMatrix();
 rank = rectangularCholeskyDecomposition.getRank();
 assertArrayEquals(MatrixUtils.createIdentityMatrix(4).getData(), actualRootMatrix.getData(), 1e-9);
 assertEquals(4, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }
}