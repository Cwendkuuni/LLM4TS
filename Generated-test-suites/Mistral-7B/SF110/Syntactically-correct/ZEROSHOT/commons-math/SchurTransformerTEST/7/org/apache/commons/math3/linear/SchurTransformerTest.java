package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.special.FastMath;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        double[][] testMatrix = {{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(testMatrix);
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        // Test getP() method
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1.0, -2.0/3.0, 1.0/3.0},
                                                                             {-2.0/3.0, 1.0, 2.0/3.0},
                                                                             {1.0/3.0, 2.0/3.0, 1.0}});
        RealMatrix actualP = schurTransformer.getP();
        MatrixAssert.assertEquals(expectedP, actualP, 1e-9);

        // Test getPT() method
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0/3.0, 1.0},
                                                                              {-2.0/3.0, 1.0, -2.0/3.0},
                                                                              {1.0/3.0, -2.0/3.0, 1.0}});
        RealMatrix actualPT = schurTransformer.getPT();
        MatrixAssert.assertEquals(expectedPT, actualPT, 1e-9);

        // Test getT() method
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{3.0, 0.0, 0.0},
                                                                             {0.0, 2.0, 1.0},
                                                                             {0.0, 1.0, 1.0}});
        RealMatrix actualT = schurTransformer.getT();
        MatrixAssert.assertEquals(expectedT, actualT, 1e-9);

        // Test transformation with a singular matrix
        double[][] singularMatrix = {{0.0, 1.0}, {1.0, 0.0}};
        RealMatrix inputSingularMatrix = MatrixUtils.createRealMatrix(singularMatrix);
        try {
            new SchurTransformer(inputSingularMatrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected exception
        }
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterationsExceeded() {
        double[][] testMatrix = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(testMatrix);
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
    }
}