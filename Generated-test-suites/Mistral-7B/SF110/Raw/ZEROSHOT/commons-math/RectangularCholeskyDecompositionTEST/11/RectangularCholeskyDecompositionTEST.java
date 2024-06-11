package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testRectangularCholeskyDecomposition() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 10.0}});
 try {
 RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
 RealMatrix rootMatrix = decomp.getRootMatrix();
 int rank = decomp.getRank();
 assertEquals(2, rank);
 assertTrue(MatrixUtils.isIdentity(rootMatrix.subMatrix(0, 2, 0, rank).transpose().multiply(rootMatrix.subMatrix(0, 2, 0, rank))));
 } catch (NonPositiveDefiniteMatrixException e) {
 failure();
 }

 RealMatrix smallMatrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 2.0}});
 try {
 RectangularCholeskyDecomposition smallDecomp = new RectangularCholeskyDecomposition(smallMatrix, 0.001);
 RealMatrix smallRootMatrix = smallDecomp.getRootMatrix();
 int smallRank = smallDecomp.getRank();
 assertEquals(2, smallRank);
 assertTrue(MatrixUtils.isIdentity(smallRootMatrix.subMatrix(0, 2, 0, smallRank).transpose().multiply(smallRootMatrix.subMatrix(0, 2, 0, smallRank))));
 } catch (NonPositiveDefiniteMatrixException e) {
 failure();
 }

 RealMatrix negativeMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, -5.0, 6.0}, {3.0, 6.0, -10.0}});
 try {
 RectangularCholeskyDecomposition negDecomp = new RectangularCholeskyDecomposition(negativeMatrix);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // expected
 }
 }
}