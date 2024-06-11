package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

import org.junit.Test;

public class SchurTransformerTest
{
    @Test
    public void testSchurTransformerConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        assertNotNull(transformer.getP());
        assertNotNull(transformer.getPT());
        assertNotNull(transformer.getT());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.9999999999999999, 0.0, 0.0}, {-0.0430435190853461, 0.9999999999999996, 0.0}, {-0.0430435190853461, -0.0430435190853461, 0.9999999999999998}});
        assertArrayEquals(expected.getData(), transformer.getP().getData(), 1e-12);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.9999999999999999, -0.0430435190853461, -0.0430435190853461}, {0.0, 0.9999999999999996, -0.0430435190853461}, {0.0, 0.0, 0.9999999999999998}});
        assertArrayEquals(expected.getData(), transformer.getPT().getData(), 1e-12);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{6.0, 3.0, 0.0}, {3.0, 4.0, 0.0}, {0.0, 0.0, 1.0}});
        assertArrayEquals(expected.getData(), transformer.getT().getData(), 1e-12);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTWhenCachedTIsNull() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.cachedT = null;
        transformer.getT();
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[][] expectedMatrixP = {{0.9999999999999999, 0.0, 0.0}, {-0.0430435190853461, 0.9999999999999996, 0.0}, {-0.0430435190853461, -0.0430435190853461, 0.9999999999999998}};
        double[][] expectedMatrixT = {{6.0, 3.0, 0.0}, {3.0, 4.0, 0.0}, {0.0, 0.0, 1.0}};
        assertArrayEquals(expectedMatrixP, transformer.matrixP, 1e-12);
        assertArrayEquals(expectedMatrixT, transformer.matrixT, 1e-12);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWhenMaxIterationsExceeded() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 4.0, 5.0, 6.0}, {3.0, 5.0, 7.0, 8.0}, {4.0, 6.0, 8.0, 9.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.MAX_ITERATIONS = 1;
        transformer.transform();
    }
}