package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.FastMath;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.Utils;

@RunWith(Parameterized.class)
public class SchurTransformerTest {

    private final RealMatrix inputMatrix;
    private final double[][] expectedMatrixP;
    private final double[][] expectedMatrixT;
    private final double[][] expectedMatrixPt;

    public SchurTransformerTest(RealMatrix inputMatrix, double[][] expectedMatrixP, double[][] expectedMatrixT, double[][] expectedMatrixPt) {
        this.inputMatrix = inputMatrix;
        this.expectedMatrixP = expectedMatrixP;
        this.expectedMatrixT = expectedMatrixT;
        this.expectedMatrixPt = expectedMatrixPt;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 1.0}, {3.0, 1.0, 1.0}}),
                        new double[][]{{0.53851739, 0.85013947, 0.0}, {-0.85013947, 0.53851739, 0.0}, {0.0, 0.0, 1.0}},
                        new double[][]{{0.53851739, -0.85013947, 0.0}, {-0.85013947, 0.53851739, 0.0}, {0.0, 0.0, 1.0}},
                        new double[][]{{0.53851739, 0.85013947, 0.0}, {-0.85013947, 0.53851739, 0.0}, {-0.0, 0.0, 1.0}}},
                { new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 1.0}, {3.0, 1.0, 1.0}, {1.0, 1.0, 1.0}}),
                        null, null, null},
                { new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 1.0, 1.0, 1.0}, {3.0, 1.0, 1.0, 1.0}, {4.0, 1.0, 1.0, 1.0}}),
                        null, null, null}
        });
    }

    @Test
    public void testSchurTransformer() throws NonSquareMatrixException {
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix actualMatrixP = schurTransformer.getP();
        RealMatrix actualMatrixT = schurTransformer.getT();
        RealMatrix actualMatrixPt = schurTransformer.getPT();

        if (expectedMatrixP != null) {
            assertArrayEquals(expectedMatrixP, actualMatrixP.getData(), 0.0);
        }

        if (expectedMatrixT != null) {
            assertArrayEquals(expectedMatrixT, actualMatrixT.getData(), 0.0);
        }

        if (expectedMatrixPt != null) {
            assertArrayEquals(expectedMatrixPt, actualMatrixPt.getData(), 0.0);
        }
    }

    private static void assertArrayEquals(double[][] a, double[][] b, double epsilon) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                double diff = FastMath.abs(a[i][j] - b[i][j]);
                assertTrue("Expected values to be close, but they are far apart. i: " + i + ", j: " + j,
                        PrecisionUtils.isClose(a[i][j], b[i][j], epsilon));
            }
        }
    }

    private static void assertArrayEquals(double[][] a, double[][] b) {
        assertArrayEquals(a, b, Precision.EPSILON);
    }
}