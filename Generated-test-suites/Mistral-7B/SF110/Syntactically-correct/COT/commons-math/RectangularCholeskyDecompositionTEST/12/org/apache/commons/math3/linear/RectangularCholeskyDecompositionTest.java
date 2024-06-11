package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.LinearAlgebraTests;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 private static final double EPSILON = 1e-12;

 @Test
 public void testConstructionWithPositiveDefiniteMatrix() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 1.5}});
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
     RealMatrix root = decomposition.getRootMatrix();
     int rank = decomposition.getRank();
     assertEquals(2, rank);
     assertEquals(2, root.getRowDimension());
     assertEquals(rank, root.getColumnDimension());
     LinearAlgebraTests.assertEquals(new Array2DRowRealMatrix(new double[][]{{1.41421356, 0.0}, {0.0, 1.22474487}}), root, EPSILON);
 }

 @Test
 public void testConstructionWithSingularMatrix() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 0.0}});
     assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 1e-6));
 }

 @Test
 public void testConstructionWithNegativeDiagonalValues() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
     assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 1e-6));
 }

 @Test
 public void testGetRank() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 1.5}});
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
     int rank = decompositions.getRank();
     assertEquals(2, rank);
 }

 @Test
 public void testGetRootMatrix() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 1.5}});
     RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
     RealMatrix root = decompositions.getRootMatrix();
     assertEquals(2, root.getRowDimension());
     assertEquals(2, root.getColumnDimension());
     LinearAlgebraTests.assertEquals(new Array2DRowRealMatrix(new double[][]{{1.41421356, 0.0}, {0.0, 1.22474487}}), root, EPSILON);
 }

 @Test
 public void testGetRootMatrixWithSingularMatrix() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 0.0}});
     assertNull(new RectangularCholeskyDecomposition(matrix).getRootMatrix());
 }

 @Test
 public void testGetRootMatrixWithNegativeDiagonalValues() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, -1.0}});
     assertNull(new RectangularCholeskyDecomposition(matrix).getRootMatrix());
 }

 @Test
 public void testConstructorWithNonSquareMatrix() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});
     assertThrows(IllegalArgumentException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0));
 }

 @Test
 public void testConstructorWithNonPositiveDefiniteMatrixAndSmallValue() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 0.0}});
     assertFalse(new RectangularCholeskyDecomposition(matrix, 1e-6).isDecomposed());
 }

 @Test
 public void testConstructorWithPositiveDefiniteMatrixAndSmallValue() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 1.5}});
     RectangularCholeskyDecomposition decompositions = new RectangularCholeskyDecomposition(matrix, 1e-6);
     assertTrue(decompositions.isDecomposed());
 }

 @Test
 public void testConstructorWithPositiveDefiniteMatrixAndSmallValue_getRank() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 1.5}});
     RectangularCholeskyDecomposition decompositions = new RectangularCholeskyDecomposition(matrix, 1e-6);
     assertEquals(2, decompositions.getRank());
 }

 @Test
 public void testConstructorWithPositiveDefiniteMatrixAndSmallValue_getRootMatrix() {
     RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 1.5}});
     RectangularCholeskyDecomposition decompositions = new RectangularCholeskyDecomposition(matrix, 1e-6);
     RealMatrix root = decompositions.getRootMatrix();
     assertEquals(2, root.getRowDimension());
     assertEquals(2, root.getColumnDimension());
     LinearAlgebraTests.assertEquals(new Array2DRowRealMatrix(new double[][]{{1.41421356, 0.0}, {0.0, 1.22474487}}), root, EPSILON);
 }
}