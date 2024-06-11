package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Arrays2D;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RectangularCholeskyDecomposition Test Cases")
public class RectangularCholeskyDecompositionTest {

 @DisplayName("Test construction with positive definite matrix")
 @Nested
 class PositiveDefiniteTest {

 @DisplayName("Test identity matrix")
 @Test
 void testIdentityMatrix() {
 RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
 RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
 assertDoesNotThrow(() -> decomp);
 RealMatrix root = decomp.getRootMatrix();
 assertEquals(3, root.getRowDimension());
 assertEquals(3, root.getColumnDimension());
 Arrays2D.assertEquals(MatrixUtils.createIdentityMatrix(3).getData(), root.getData(), 1e-12);
 }

 @DisplayName("Test diagonal matrix")
 @Test
 void testDiagonalMatrix() {
 RealMatrix matrix = MatrixUtils.createDiagonalMatrix(new double[]{1.0, 2.0, 3.0});
 RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
 assertDoesNotThrow(() -> decomp);
 RealMatrix root = decomp.getRootMatrix();
 assertEquals(3, root.getRowDimension());
 assertEquals(3, root.getColumnDimension());
 Arrays2D.assertEquals(MatrixUtils.createDiagonalMatrix(new double[]{1.0, 1.0, 1.0}).getData(), root.getData(), 1e-12);
 }

 @DisplayName("Test non-diagonal matrix")
 @Test
 void testNonDiagonalMatrix() {
 RealMatrix matrix = new OpenMatrix(new double[][]{{1.0, 0.0, 0.5}, {0.0, 2.0, 1.0}, {0.5, 1.0, 3.0}});
 RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
 assertDoesNotThrow(() -> decomp);
 RealMatrix root = decomp.getRootMatrix();
 assertEquals(3, root.getRowDimension());
 assertEquals(3, root.getColumnDimension());
 Arrays2D.assertEquals(new double[][]{{1.0, 0.0, 0.5}, {0.0, 1.0, 0.5}, {0.5, 0.5, 1.5}}, root.getData(), 1e-12);
 }
 }

 @DisplayName("Test construction with non-positive definite matrix")
 @Nested
 class NonPositiveDefiniteTest {

 @DisplayName("Test singular matrix")
 @Test
 void testSingularMatrix() {
 RealMatrix matrix = MatrixUtils.createSingularMatrix(2);
 assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
 }

 @DisplayName("Test matrix with negative diagonal elements")
 @Test
 void testNegativeDiagonalMatrix() {
 RealMatrix matrix = MatrixUtils.createDiagonalMatrix(new double[]{-1.0, 2.0});
 assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
 }

 @DisplayName("Test matrix with negative off-diagonal elements")
 @Test
 void testNegativeOffDiagonalMatrix() {
 RealMatrix matrix = new OpenMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}});
 assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
 }
 }
}