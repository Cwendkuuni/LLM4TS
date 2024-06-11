package org.junit.tests.schurtransformer; // replace with your test package name

import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer_IdentityMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createIdentityMatrix(3);
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createIdentityMatrix(3);
        RealMatrix expectedT = MatrixUtils.createIdentityMatrix(3);

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        Assert.assertEquals(expectedP, actualP);
        Assert.assertEquals(expectedT, actualT);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
    }

    @Test
    public void testSchurTransformer_SmallMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{-0.7071067811865475, 0.7071067811865475}, {0.7071067811865475, -0.7071067811865475}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{-0.408248290463863, 0.816496580927726}, {0.816496580927726, -0.408248290463863}});

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        Assert.assertEquals(expectedP, actualP);
        Assert.assertEquals(expectedT, actualT);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformer_MaxIterationsReached() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
    }
}