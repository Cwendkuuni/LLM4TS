package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.Array2DRowRealMatrix;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testValidDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
 try {
 RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = rcd.getRootMatrix();
 int rank = rcd.getRank();

 // Check dimensions
 Assert.assertEquals("Unexpected root matrix dimensions", 3, root.getRowDimension());
 Assert.assertEquals("Unexpected root matrix columns", 2, root.getColumnDimension());
 Assert.assertEquals("Unexpected rank", 3, rank);

 // Check decomposition
 for (int i = 0; i < 3; i++) {
 for (int j = 0; j < rank; j++) {
 double expected = i == j ? FastMath.sqrt(matrix.getEntry(i, i)) : 0.0;
 Assert.assertEquals("Unexpected value at [" + i + ", " + j + "] in root matrix", expected, root.getEntry(i, j), 1e-14);
 }
 }
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not reach this point
 Assert.fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
 }
 }

 @Test
 public void testInvalidDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
 try {
 RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-8);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }

 @Test
 public void testSwapRows() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
 try {
 RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = rcd.getRootMatrix();
 int rank = rcd.getRank();
 // Swap rows
 Array2DRowRealMatrix swappedMatrix = new Array2DRowRealMatrix(new double[][]{{2.0, 3.0, 1.0}, {3.0, 2.0, 1.0}, {1.0, 1.0, 2.0}});
 RectangularCholeskyDecomposition swappedRcd = new RectangularCholeskyDecomposition(swappedMatrix);
 RealMatrix swappedRoot = swappedRcd.getRootMatrix();
 int swappedRank = swappedRcd.getRank();
 // Check that roots are equal
 for (int i = 0; i < 3; i++) {
 for (int j = 0; j < rank; j++) {
 Assert.assertEquals("Unexpected value at [" + i + ", " + j + "] in root matrices", swappedRoot.getEntry(i, j), root.getEntry(i, j), 1e-14);
 }
 }
 // Check that ranks are equal
 Assert.assertEquals("Unexpected rank", rank, swappedRank);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not reach this point
 Assert.fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
 }
 }
}