package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_IdentityMatrix() {
        RealMatrix identityMatrix = MatrixUtils.eye(5);
        SchurTransformer schurTransformer = new SchurTransformer(identityMatrix);
        RealMatrix p = schurTransformer.getP();
        RealMatrix t = schurTransformer.getT();
        assertEquals(identityMatrix, p.transpose().multiply(t));
    }

    @Test
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        try {
            new SchurTransformer(matrix);
            fail();
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSchurTransformer_Transform() {
        RealMatrix matrix = MatrixUtils.randomRealMatrix(5, 5);
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix p = schurTransformer.getP();
        RealMatrix t = schurTransformer.getT();
        // check that the matrix is transformed correctly
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 1.0}});
        RealMatrix expectedT = t;
        assertEquals(expectedP, p);
        assertEquals(expectedT, t);
    }

    @Test
    public void testSchurTransformer_Transform_HugeMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0}, {2.0, 3.0}, {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0, 10.0, 11.0}, {12.0, 13.0, 14.0, 15.0, 16.0}, {17.0, 18.0, 19.0, 20.0, 21.0},
                {22.0, 23.0, 24.0, 25.0, 26.0}, {27.0, 28.0, 29.0, 30.0, 31.0}, {32.0, 33.0, 34.0, 35.0, 36.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix p = schurTransformer.getP();
        RealMatrix t = schurTransformer.getT();
        // check that the matrix is transformed correctly
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0},
                {0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}});
        RealMatrix expectedT = t;
        assertEquals(expectedP, p);
        assertEquals(expectedT, t);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformer_MaxIterationsReached() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        schurTransformer.transform();
    }
}