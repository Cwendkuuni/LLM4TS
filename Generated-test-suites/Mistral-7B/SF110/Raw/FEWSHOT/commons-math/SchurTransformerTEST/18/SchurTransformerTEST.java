package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.FastMath;

public class SchurTransformerTest {
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix p = transformer.getP();
        assertEquals(expectedP, p);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 4.0, 7.0}, {2.0, 5.0, 8.0}, {3.0, 6.0, 9.0}});
        RealMatrix pt = transformer.getPT();
        assertEquals(expectedPT, pt);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{5.0, -2.0, 1.0}, {-2.0, 3.0, -2.0}, {1.0, -2.0, 3.0}});
        RealMatrix t = transformer.getT();
        assertEquals(expectedT, t);
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{5.0, -2.0, 1.0}, {-2.0, 3.0, -2.0}, {1.0, -2.0, 3.0}});
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();
        assertEquals(expectedP, p);
        assertEquals(expectedT, t);
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double expectedNorm = FastMath.sqrt(36.0 * 36.0 + 49.0 * 49.0 + 64.0 * 64.0);
        double norm = transformer.getNorm();
        assertEquals(expectedNorm, norm, 1e-14);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        int expectedIl = 5;
        int il = transformer.findSmallSubDiagonalElement(5, transformer.getNorm());
        assertEquals(expectedIl, il);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithMaxCountExceeded() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}, {10.0, 11.0, 12.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
    }
}