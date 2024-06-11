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

    @Test
    public void testSchurTransformer() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.36655994, 0.81649658, 0.40824829}, {0.81649658, 0.54538228, -0.40824829}, {0.40824829, -0.40824829, 0.81649658}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{3.13131313, 0.0, 0.0}, {0.0, 3.08069624, 0.0}, {0.0, 0.0, 2.97833325}});
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);

        RealMatrix actualPt = schurTransformer.getPT();
        RealMatrix expectedPt = MatrixUtils.createRealMatrix(new double[][]{{0.36655994, 0.81649658, 0.40824829}, {0.81649658, 0.54538228, -0.40824829}, {0.40824829, -0.40824829, 0.81649658}});
        assertEquals(expectedPt, actualPt);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);

        double normBefore = schurTransformer.getNorm();
        schurTransformer.transform();
        double normAfter = schurTransformer.getNorm();
        assertTrue(normAfter < normBefore);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        schurTransformer.transform();
        schurTransformer.transform();
    }
}