package org.apache.commons.math3.linear;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

 private RealMatrix createMatrix(double[][] data) {
 return new Array2DRowRealMatrix(data);
 }

 @Test
 public void testRectangularCholeskyDecomposition() throws NonPositiveDefiniteMatrixException {
 RealMatrix matrix = createMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
 RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
 RealMatrix expected = createMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.44948974278317, 0.81649658092772, 0.0}, {0.0, 1.41421374125095, 1.87499292243115}});
 RealMatrix actual = decomp.getRootMatrix();
 assertEquals(expected, actual);
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNonPositiveDefiniteException() throws NonPositiveDefiniteMatrixException {
 RealMatrix matrix = createMatrix(new double[][]{{1.0, -1.0, 0.0}, {-1.0, 1.0, 0.0}, {0.0, 0.0, -1.0}});
 new RectangularCholeskyDecomposition(matrix, 0.0); //should throw NonPositiveDefiniteMatrixException
 }

 @Test(expected = NonPositiveDefiniteMatrixException.class)
 public void testNegativeEntryInMatrix() throws NonPositiveDefiniteMatrixException {
 RealMatrix matrix = createMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, -1.0}});
 new RectangularCholeskyDecomposition(matrix, 0.0); //should throw NonPositiveDefiniteMatrixException
 }

 @Test
 public void testSmallValueDiagonalEntry() throws NonPositiveDefiniteMatrixException {
 RealMatrix matrix = createMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 0.001}});
 RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.001);
 RealMatrix expected = createMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.44948974278317, 0.81649658092772, 0.0}, {0.0, 1.41421374125095, 0.99984691415645}});
 RealMatrix actual = decomp.getRootMatrix();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetRank() throws NonPositiveDefiniteMatrixException {
 RealMatrix matrix = createMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
 RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
 int actualRank = decomp.getRank();
 int expectedRank = 3;
 assertEquals(expectedRank, actualRank);
 }

}