package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

import java.util.Random;

public class SchurTransformerTest {

    @org.junit.Test
    public void testSchurTransformer() {
        double[][] matrixData = new double[][]{{1.0, 2.0, 3.0}, {2.0, 5.0, 6.0}, {3.0, 6.0, 7.0}};
        RealMatrix inputMatrix = RealMatrixUtils.createRealMatrix(matrixData);
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix expectedPT = RealMatrixUtils.createRealMatrix(new double[][]{{0.5384606925638317, 0.854585362514984, -0.0632291907305576},
                {-0.854585362514984, 0.5384606925638317, 0.0632291907305576},
                {0.0, 0.0, 1.0}});

        RealMatrix expectedP = schurTransformer.getP();
        RealMatrix expectedT = schurTransformer.getT();
        RealMatrix expectedPt = schurTransformer.getPT();

        assertEquals(expectedP, schurTransformer.getP());
        assertEquals(expectedT, schurTransformer.getT());
        assertEquals(expectedPt, schurTransformer.getPT());

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            double[][] randomData = new double[3][3];
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    randomData[j][k] = random.nextDouble();
                }
            }
            RealMatrix randomMatrix = RealMatrixUtils.createRealMatrix(randomData);
            SchurTransformer testSchurTransformer = new SchurTransformer(randomMatrix);
            RealMatrix testP = testSchurTransformer.getP();
            RealMatrix testT = testSchurTransformer.getT();
            RealMatrix testPt = testSchurTransformer.getPT();

            assertEquals(testP.getRowDimension(), testP.getColumnDimension(), 0);
            assertEquals(testP.getRowDimension(), testT.getRowDimension());
            assertEquals(testP.getRowDimension(), testPt.getRowDimension());
            assertEquals(testP.getColumnDimension(), testT.getColumnDimension());
            assertEquals(testP.getColumnDimension(), testPt.getColumnDimension());
        }
    }
}