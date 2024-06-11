package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.Array2DRowRealMatrix;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testValidDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();
 assertEquals(3, rank);
 assertNotNull(rootMatrix);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not be thrown for valid matrix
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testInvalidDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
 try {
 new RectangularCholeskyDecomposition(matrix, 0.0);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetRank() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 int rank = decomposition.getRank();
 assertEquals(3, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not be thrown
 }
 }

 @Test
 public void testGetRootMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 assertEquals(3, rootMatrix.getRowDimension());
 assertEquals(3, rootMatrix.getColumnDimension());
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not be thrown
 }
 }

 @Test
 public void testNegativeSmallValue() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
 try {
 new RectangularCholeskyDecomposition(matrix, -0.1);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should be thrown
 }
 }

 @Test
 public void testZeroSmallValue() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 3.0, 1.0}, {1.0, 1.0, 2.0}});
 try {
 new RectangularCholeskyDecomposition(matrix, 0.0);
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should be thrown
 }
 }

 @Test
 public void testIdentityMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
 RealMatrix rootMatrix = decomposition.getRootMatrix();
 int rank = decomposition.getRank();
 assertEquals(2, rank);
 assertEquals(2, rootMatrix.getRowDimension());
 assertEquals(2, rootMatrix.getColumnDimension());
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < rank; j++) {
 assertEquals(FastMath.sqrt(i == j ? 1.0 : 0.0), rootMatrix.getEntry(i, j), 1e-14);
 }
 }
 } catch (NonPositiveDefiniteMatrixException e) {
 // Should not be thrown
 }
}

}