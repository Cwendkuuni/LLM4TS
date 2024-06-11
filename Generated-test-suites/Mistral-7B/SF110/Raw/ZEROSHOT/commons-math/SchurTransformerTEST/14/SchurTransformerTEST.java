package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.special.FastMath;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.ShiftInfo;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest
{
    @Test
    public void testSchurTransformer()
    {
        // Test with square matrix
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 4.0, 3.0}, {3.0, 3.0, 5.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][] {{0.08430526823813148, -0.1333957285417517, 0.9836192621482569},
                {-0.1333957285417517, 0.3759019536347108, -0.4470432225146653},
                {0.9836192621482569, -0.4470432225146653, 0.2078938334418665}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][] {{4.006759989524973, 1.4142136910882346, 1.4142136910882346},
                {1.4142136910882346, 4.731843312759768, 1.4142136910882346},
                {1.4142136910882346, 1.4142136910882346, 5.0}});
        RealMatrix p = schurTransformer.getP();
        RealMatrix t = schurTransformer.getT();
        assertEquals("P matrix is incorrect", expectedP, p);
        assertEquals("T matrix is incorrect", expectedT, t);

        // Test with non-square matrix
        RealMatrix nonSquareMatrix = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 3.0}});
        try {
            new SchurTransformer(nonSquareMatrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }

        // Test with MaxCountExceededException
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        try {
            transformer.transform();
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }
}