package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.FastMath;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.ShiftInfo;

import java.util.Arrays;

public class SchurTransformerTest {

    @org.junit.Test
    public void testSchurTransformer() throws MaxCountExceededException, NonSquareMatrixException {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        // Test getP() method
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix actualP = schurTransformer.getP();
        assertEquals(expectedP, actualP);

        // Test getPT() method
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix actualPT = schurTransformer.getPT();
        assertEquals(expectedPT, actualPT);

        // Test getT() method
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{3.548319035142275, 1.3196859628391833, 0.0},
                {1.3196859628391833, 2.373972561480327, 1.282421718160354},
                {0.0, 1.282421718160354, 2.833423743341929}});
        RealMatrix actualT = schurTransformer.getT();
        assertEquals(expectedT, actualT, 0.0000001);

        // Test transformer handling non-square matrices
        try {
            new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}}));
            fail();
        } catch (NonSquareMatrixException e) {
            // Expected
        }

        // Test transformer convergence
        double[][] testMatrix = {{-1.0, 0.0, 1.0}, {-2.0, 2.0, -1.0}, {-3.0, 0.0, 2.0}};
        RealMatrix inputMatrix2 = new Array2DRowRealMatrix(testMatrix);
        SchurTransformer schurTransformer2 = new SchurTransformer(inputMatrix2);

        try {
            schurTransformer2.transform();
            fail();
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }
}