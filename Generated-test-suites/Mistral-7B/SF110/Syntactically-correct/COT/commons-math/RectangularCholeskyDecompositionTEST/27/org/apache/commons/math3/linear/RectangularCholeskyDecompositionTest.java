package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RectangularCholeskyDecompositionTest {

 private RealMatrix matrix;
 private RealMatrix expectedRootMatrix;
 private RealMatrix actualRootMatrix;

 @Before
 public void setUp() {
 matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 4.0}});
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testConstructorWithNonPositiveDefiniteMatrix() {
 try {
 new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, -1.0}, {2.0, -1.0, 1.0}, {-1.0, 1.0, 1.0}}));
 } catch (NonPositiveDefiniteMatrixException e) {
 assertEquals(-1.0, e.getEigenvalue(), 1e-9);
 assertEquals(2, e.getIndex());
 }
 }

 @Test
 public void testConstructorWithValidMatrix() throws NonPositiveDefiniteMatrixException {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 expectedRootMatrix = new Array2DRowRealMatrix(new double[][]{{1.0 / FastMath.sqrt(3.0), 0.0, 0.0}, {-1.0 / FastMath.sqrt(2.0), 1.0 / FastMath.sqrt(5.0), 0.0}, {-0.5 / FastMath.sqrt(3.0), -1.0 / 2.0 * FastMath.sqrt(5.0) / FastMath.sqrt(3.0), 1.0 / FastMath.sqrt(4.0)}});
 actualRootMatrix = decomposition.getRootMatrix();
 assertEquals(expectedRootMatrix.getRowDimension(), actualRootMatrix.getRowDimension());
 assertEquals(expectedRootMatrix.getColumnDimension(), actualRootMatrix.getColumnDimension());
 for (int i = 0; i < expectedRootMatrix.getRowDimension(); i++) {
 for (int j = 0; j < expectedRootMatrix.getColumnDimension(); j++) {
 assertEquals(expectedRootMatrix.getEntry(i, j), actualRootMatrix.getEntry(i, j), 1e-9);
 }
 }
 }

 @Test
 public void testGetRank() throws NonPositiveDefiniteMatrixException {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 assertEquals(3, decomposition.getRank());
 }

 @Test
 public void testGetRootMatrix() throws NonPositiveDefiniteMatrixException {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix actualRootMatrix = decomposition.getRootMatrix();
 assertNotNull(actualRootMatrix);
 }
}