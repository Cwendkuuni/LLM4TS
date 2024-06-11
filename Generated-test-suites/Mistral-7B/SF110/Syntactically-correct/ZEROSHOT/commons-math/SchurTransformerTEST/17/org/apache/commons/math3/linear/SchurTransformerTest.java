package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.ShiftInfo;

import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        double[][] matrixData = new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 7.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(matrixData);

        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix pMatrix = schurTransformer.getP();
        RealMatrix tMatrix = schurTransformer.getT();

        // Test if P is correct
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 0.86602540378444, -0.47942555264422}, {0.0, 0.47942555264422, 0.86602540378444}});
        assertTrue(Precision.equals(expectedP, pMatrix, 1.0E-12));

        // Test if T is correct
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{5.5, 0.0, 0.0}, {1.38258267562133, 3.31728958459142, 0.0}, {-1.81126193290489, 0.0, 4.0}});
        assertTrue(Precision.equals(expectedT, tMatrix, 1.0E-12));

        // Test if PT is correct
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{{5.5, 1.38258267562133, -1.81126193290489}, {0.0, 3.31728958459142, 0.0}, {0.0, 0.0, 4.0}});
        RealMatrix ptMatrix = schurTransformer.getPT();
        assertTrue(Precision.equals(expectedPT, ptMatrix, 1.0E-12));

        // Test ifgetting cached matrices multiple times returns the same instance
        RealMatrix cachedP1 = schurTransformer.getP();
        RealMatrix cachedP2 = schurTransformer.getP();
        assertEquals(cachedP1, cachedP2);

        RealMatrix cachedT1 = schurTransformer.getT();
        RealMatrix cachedT2 = schurTransformer.getT();
        assertEquals(cachedT1, cachedT2);

        RealMatrix cachedPT1 = schurTransformer.getPT();
        RealMatrix cachedPT2 = schurTransformer.getPT();
        assertEquals(cachedPT1, cachedPT2);

        // Test exception when input matrix is not square
        try {
            new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }
    }
}