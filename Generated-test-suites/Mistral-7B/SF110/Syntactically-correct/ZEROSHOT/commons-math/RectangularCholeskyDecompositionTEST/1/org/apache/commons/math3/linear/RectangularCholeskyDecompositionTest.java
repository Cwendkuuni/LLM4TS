package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.util.ArrayUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

 @Test
 public void testValidDecomposition() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 1.0, 3.0}, {2.0, 3.0, 5.0}});
 try {
 RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = rcd.getRootMatrix();
 int rank = rcd.getRank();

 assertEquals("The root matrix should have the correct dimensions", 3, root.getRowDimension());
 assertEquals("The root matrix should have the correct number of columns", 2, root.getColumnDimension());
 assertEquals("The rank should be 2", 2, rank);

 // Check triangle inequality
 for (int i = 0; i < 3; i++) {
 for (int j = 0; j < i; j++) {
 double e = root.getEntry(i, j) + root.getEntry(j, i);
 double v = FastMath.abs(root.getEntry(i, i) - root.getEntry(j, j));
 assertTrue("The triangle inequality is not satisfied for elements " + i + "," + j + ": " + e + " != " + v, e >= v);
 }
 }
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testInvalidDecomposition() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
 try {
 new RectangularCholeskyDecomposition(matrix, 0.0);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testNonSquareMatrix() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0, 4.0}});
 try {
 new RectangularCholeskyDecomposition(matrix);
 fail("Expected NonPositiveDefiniteMatrixException");
 } catch (NonPositiveDefiniteMatrixException e) {
 // Expected
 }
 }

 @Test
 public void testSmallEigenvalues() {
 RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 1.0, 1e-6}, {0.0, 1e-6, 1.0}});
 try {
 RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix, 1e-12);
 RealMatrix root = rcd.getRootMatrix();
 int rank = rcd.getRank();

 assertEquals("The root matrix should have the correct dimensions", 3, root.getRowDimension());
 assertEquals("The root matrix should have the correct number of columns", 2, root.getColumnDimension());
 assertEquals("The rank should be 3", 3, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSetEntries() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
 try {
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 2; j++) {
 matrix.setEntry(i, j, i + j + 1.0);
 }
 }
 RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = rcd.getRootMatrix();
 int rank = rcd.getRank();

 assertEquals("The root matrix should have the correct dimensions", 2, root.getRowDimension());
 assertEquals("The root matrix should have the correct number of columns", 2, root.getColumnDimension());
 assertEquals("The rank should be 2", 2, rank);

 // Check triangle inequality
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < i; j++) {
 double e = root.getEntry(i, j) + root.getEntry(j, i);
 double v = FastMath.abs(root.getEntry(i, i) - root.getEntry(j, j));
 assertTrue("The triangle inequality is not satisfied for elements " + i + "," + j + ": " + e + " != " + v, e >= v);
 }
 }
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
}

 @Test
 public void testSetSymmetric() {
 RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
 try {
 for (int i = 0; i < 2; i++) {
 for (int j = i; j < 2; j++) {
 matrix.setEntry(i, j, matrix.getEntry(j, i));
 }
 }
 RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = rcd.getRootMatrix();
 int rank = rcd.getRank();

 assertEquals("The root matrix should have the correct dimensions", 2, root.getRowDimension());
 assertEquals("The root matrix should have the correct number of columns", 2, root.getColumnDimension());
 assertEquals("The rank should be 2", 2, rank);

 // Check triangle inequality
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < i; j++) {
 double e = root.getEntry(i, j) + root.getEntry(j, i);
 double v = FastMath.abs(root.getEntry(i, i) - root.getEntry(j, j));
 assertTrue("The triangle inequality is not satisfied for elements " + i + "," + j + ": " + e + " != " + v, e >= v);
 }
 }
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testRandom() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
 new double[]{1.5075555555555555, -1.23456, 1.7320555555555555},
 new double[]{-1.23456, 2.0, -0.54321},
 new double[]{1.7320555555555555, -0.54321, 3.0}
 });
 try {
 RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
 RealMatrix root = rcd.getRootMatrix();
 int rank = rcd.getRank();

 assertEquals("The root matrix should have the correct dimensions", 3, root.getRowDimension());
 assertEquals("The root matrix should have the correct number of columns", 3, root.getColumnDimension());
 assertEquals("The rank should be 3", 3, rank);
 } catch (NonPositiveDefiniteMatrixException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

}