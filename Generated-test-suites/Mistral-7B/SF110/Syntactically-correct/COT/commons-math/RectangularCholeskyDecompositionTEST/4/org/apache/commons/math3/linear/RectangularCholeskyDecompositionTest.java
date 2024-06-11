package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

 private RealMatrix createMatrix(double[][] values) {
 return new Array2DRrealMatrix(values);
 }

 @Test
 public void testRectangularCholeskyDecomposition() {
 RealMatrix matrix = new Array2DRrealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = decomposition.getRootMatrix();
 int rank = decomposition.getRank();
 assertEquals(3, rank);
 assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.44948974278317, 1.0, 0.0}, {0.27258966413538, 0.81649658092773, 1.0}}, root.getData(), 0.001);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("NonPositiveDefiniteMatrixException should not be thrown");
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNonPositiveDefiniteMatrixException() {
 RealMatrix matrix = new Array2DRrealMatrix(new double[][]{{1.0, -2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("NonPositiveDefiniteMatrixException should be thrown");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetRank() {
 RealMatrix matrix = new Array2DRrealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 int rank = decomposition.getRank();
 assertEquals(3, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("NonPositiveDefiniteMatrixException should not be thrown");
 }
 }

 @Test
 public void testGetRootMatrix() {
 RealMatrix matrix = new Array2DRrealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = decomposition.getRootMatrix();
 assertNotNull(root);
 assertEquals(3, root.getRowDimension());
 assertEquals(3, root.getColumnDimension());
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("NonPositiveDefiniteMatrixException should not be thrown");
 }
 }

 @Test
 public void testSmallValue() {
 RealMatrix matrix = new Array2DRrealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.999999999999e-05, 6.0}, {3.0, 6.0, 9.0}});
 try {
 RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1.0e-5);
 RealMatrix root = decomposition.getRootMatrix();
 int rank = decomposition.getRank();
 assertEquals(3, rank);
 assertArrayEquals(new double[][]{{1.0, 0.0, 0.0}, {0.44948974278317, 1.0, 0.0}, {0.27258966413538, 0.81649658092773, 1.0}}, root.getData(), 0.001);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("NonPositiveDefiniteMatrixException should not be thrown");
 }
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNegativeMatrix() {
 RealMatrix matrix = new Array2DRrealMatrix(new double[][]{{1.0, -2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("NonPositiveDefiniteMatrixException should be thrown");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected exception
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testOutOfBoundsIndex() {
 RealMatrix matrix = new Array2DRrealMatrix(new double[][]{{1.0, 2.0}, {2.0, 4.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("ArrayIndexOutOfBoundsException should be thrown");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Ignore this exception
 } catch (ArrayIndexOutOfBoundsException ex) {
 // Expected exception
 }
 }

}