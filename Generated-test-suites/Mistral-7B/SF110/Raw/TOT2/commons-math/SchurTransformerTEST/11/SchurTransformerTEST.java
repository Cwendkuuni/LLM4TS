package org.example;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix inputMatrix = Array2D.denseOfArray(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.829367587326828, -0.498940756523825}, {-0.498940756523825, 0.829367587326828}});
        RealMatrix actualP = schurTransformer.getP();

        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetT() {
        RealMatrix inputMatrix = Array2D.denseOfArray(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{-0.545124426794924, 1.0}, {1.0, -0.545124426794924}});
        RealMatrix actualT = schurTransformer.getT();

        assertEquals(expectedT, actualT);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testTransformWithNonSquareMatrix() {
        RealMatrix inputMatrix = Array2D.denseOfArray(new double[][]{{1.0, 2.0}, {2.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
    }
}