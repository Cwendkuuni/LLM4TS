package org.junit.tests;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;

public class SchurTransformerTest {

    @org.junit.Test
    public void testSchurTransformer_ValidInput() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.7391335984432821, -0.2152085376183324, 0.458564071917036},
                {-0.2152085376183324, 0.9732747135255439, 0.1348036133561072},
                {0.458564071917036, 0.1348036133561072, 0.9236043635432841}});
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{0.7391335984432821, 0.458564071917036, -0.2152085376183324},
                {-0.2152085376183324, 0.9236043635432841, 0.1348036133561072},
                {0.1348036133561072, -0.1348036133561072, 0.9732747135255439}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.800291874119215, -1.5811388390269604, 0.1825241316638396},
                {-1.5811388390269604, 6.581855593759067, -1.1053247113449727},
                {0.1825241316638396, -1.1053247113449727, 6.765780513771553}});

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualPT = schurTransformer.getPT();
        RealMatrix actualT = schurTransformer.getT();

        assertEquals("The P matrix is incorrect.", expectedP, actualP);
        assertEquals("The PT matrix is incorrect.", expectedPT, actualPT);
        assertEquals("The T matrix is incorrect.", expectedT, actualT);
    }

    @org.junit.Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformer_NonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        new SchurTransformer(inputMatrix);
    }

    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testSchurTransformer_ConvergenceFailed() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0}});
        new SchurTransformer(inputMatrix);
    }
}