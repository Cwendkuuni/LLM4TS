package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import java.util.Arrays;

public class SchurTransformerTest {

    @org.junit.Test
    public void testSchurTransformer() {
        double[][] inputMatrixData = {{1.0, 2.0, 3.0}, {2.0, 4.0, 3.0}, {3.0, 3.0, 5.0}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputMatrixData);

        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.53846931, -0.53846931, 0.81649660},
                {-0.53846931, 0.53846931, -0.81649660}, {0, 0, 1}});

        RealMatrix actualP = schurTransformer.getP();
        assertEquals(expectedP, actualP);

        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{-2.81261414, 0.28126142, 2.44948975},
                {0.28126142, -0.74142136, 0.42258064}, {2.44948975, 0.42258064, 1.49323855}});

        RealMatrix actualT = schurTransformer.getT();
        assertEquals(expectedT, actualT);

        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{-2.81261414, -0.74142136, 2.44948975},
                {0.28126142, 0.42258064, 0.42258064}, {2.44948975, 0.42258064, 1.49323855}});

        RealMatrix actualPT = schurTransformer.getPT();
        assertEquals(expectedPT, actualPT);

        SchurTransformer schurTransformer2 = new SchurTransformer(inputMatrix);

        RealMatrix actualP2 = schurTransformer2.getP();
        assertEquals(expectedP, actualP2);

        RealMatrix actualT2 = schurTransformer2.getT();
        assertEquals(expectedT, actualT2);

        RealMatrix actualPt2 = schurTransformer2.getPT();
        assertEquals(expectedPT, actualPt2);

        SchurTransformer schurTransformer3 = new SchurTransformer(MatrixUtils.createRealIdentityMatrix(3));

        RealMatrix actualP3 = schurTransformer3.getP();
        RealMatrix expectedIdentityP = MatrixUtils.createRealIdentityMatrix(3);
        assertEquals(expectedIdentityP, actualP3);

        RealMatrix actualT3 = schurTransformer3.getT();
        RealMatrix expectedIdentityT = MatrixUtils.createRealIdentityMatrix(3);
        assertEquals(expectedIdentityT, actualT3);

        RealMatrix actualPt3 = schurTransformer3.getPT();
        assertEquals(expectedIdentityP, actualPt3);

        try {
            new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}}));
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }

        try {
            SchurTransformer schurTransformer4 = new SchurTransformer(MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 3.0}, {3.0, 3.0, 5.0}, {3.0, 3.0, 5.0}}));
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testGetNorm() {
        double[][] matrixData = {{1.0, 2.0, 3.0}, {2.0, 4.0, 3.0}, {3.0, 3.0, 5.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        SchurTransformer schurTransformer = new SchurTransformer(matrix);

        double expected = 42.0;
        double actual = schurTransformer.getNorm();

        assertEquals(expected, actual, Precision.SAFE_EPSILON);
    }

    @org.junit.Test
    public void testFindSmallSubDiagonalElement() {
        double[][] matrixData = {{1.0, 2.0, 3.0}, {2.0, 4.0, 3.0}, {3.0, 3.0, 5.0}};
        SchurTransformer schurTransformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixData));

        int expected = 1;
        int actual = schurTransformer.findSmallSubDiagonalElement(2, schurTransformer.getNorm());

        assertEquals(expected, actual);
    }
}