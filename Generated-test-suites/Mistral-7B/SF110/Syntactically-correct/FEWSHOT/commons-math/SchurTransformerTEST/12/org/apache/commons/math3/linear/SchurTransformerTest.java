package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.FastMath;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testInstanceWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetters() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        assertEquals(p, pt.transpose());
        assertEquals(t, transformer.getT());
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 1.0, 1.0}, {1.0, 1.0, 2.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{5.176595583079989E-17, 1.0, 5.176595583079989E-17},
                {1.0, 1.847759932699787E-16, 1.0},
                {5.176595583079989E-17, 1.0, 5.176595583079989E-17}});
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.9999999999999994, -1.932740399216234E-17, -6.661338143115158E-17},
                {-1.932740399216234E-17, 0.9999999999999994, 1.3322676306557754E-16},
                {-6.661338143115158E-17, 1.3322676306557754E-16, 0.9999999999999994}});
        RealMatrix expectedPt = MatrixUtils.createRealMatrix(new double[][]{{0.9999999999999994, 1.3322676306557754E-16, -6.661338143115158E-17},
                {1.3322676306557754E-16, 0.9999999999999994, 1.932740399216234E-17},
                {-6.661338143115158E-17, 1.932740399216234E-17, 0.9999999999999994}});

        RealMatrix actualT = transformer.getT();
        RealMatrix actualP = transformer.getP();
        RealMatrix actualPt = transformer.getPT();

        assertEquals(expectedT, actualT, 1.0E-15);
        assertEquals(expectedP, actualP, 1.0E-15);
        assertEquals(expectedPt, actualPt, 1.0E-15);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformFail() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 1.0, 1.0}, {1.0, 1.0, 2.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        transformer.transform();
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 1.0, 1.0}, {1.0, 1.0, 2.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double actualNorm = transformer.getNorm();
        double expectedNorm = FastMath.sqrt(3.0 * 3.0 + 2.0 * 2.0 + 1.0 * 1.0 + 2.0 * 2.0 + 1.0 * 1.0 + 1.0 * 1.0);

        assertEquals(expectedNorm, actualNorm, 1.0E-15);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] matrix = new double[][]{{3.0, 2.0, 1.0, 0.1}, {2.0, 1.0, 1.0, 1.1}, {1.0, 1.0, 1.0, 0.3}, {0.1, 1.1, 0.3, 0.2}};
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrix));
        int actualIl = transformer.findSmallSubDiagonalElement(3, transformer.getNorm());

        int expectedIl = 1;

        assertEquals(expectedIl, actualIl);
    }
}