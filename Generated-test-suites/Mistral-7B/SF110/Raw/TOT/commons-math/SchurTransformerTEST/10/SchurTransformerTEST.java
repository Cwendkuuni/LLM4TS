package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        // Test with valid input
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.53571923, 0.85743254, -0.17622563}, {-0.85743254, 0.53571923, -0.17622563}, {-0.17622563, -0.17622563, 0.97375576}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{4.32180983, 1.34469631, -1.36150495}, {1.34469631, 5.97615142, 1.33639856}, {-1.36150495, 1.33639856, 7.16380474}});
        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);

        // Test with non-square matrix
        matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            new SchurTransformer(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedPt = new Array2DRowRealMatrix(new double[][]{{-0.53571923, 0.85743254, 0.17622563}, {0.85743254, -0.53571923, 0.17622563}, {0.17622563, 0.17622563, -0.97375576}});
        RealMatrix actualPt = schurTransformer.getPT();
        assertEquals(expectedPt, actualPt);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{4.32180983, 1.34469631, -1.36150495}, {1.34469631, 5.97615142, 1.33639856}, {-1.36150495, 1.33639856, 7.16380474}});
        RealMatrix actualT = schurTransformer.getT();
        assertEquals(expectedT, actualT);
    }
}