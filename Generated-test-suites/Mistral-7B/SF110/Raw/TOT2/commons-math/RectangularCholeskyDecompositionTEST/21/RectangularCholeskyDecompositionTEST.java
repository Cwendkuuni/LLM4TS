import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNoArgsNegative() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix));
    }

    @Test
    public void testConstructorArgs() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertArrayEquals(new double[][]{{-0.44948974278317, -0.73439357324122}, {-0.73439357324122, -0.44948974278317}}, root.getData(), 0.001);
        Assert.assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertArrayEquals(new double[][]{{-0.44948974278317, -0.73439357324122}, {-0.73439357324122, -0.44948974278317}}, root.getData(), 0.001);
    }

    @Test
    public void testGetRootMatrixWrongDimensions() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertNull(root);
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        Assert.assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorArgsNonPositiveDefinite() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 2.0}, {2.0, -1.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0));
    }

    @Test
    public void testConstructorArgsSmall() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertArrayEquals(new double[][]{{-0.44948974278317, -0.73439357324122}, {-0.73439357324122, -0.44948974278317}}, root.getData(), 0.001);
        Assert.assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorArgsSingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0));
    }
}