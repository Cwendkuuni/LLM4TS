package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.ArrayRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class SchurTransformerTest
{
    @org.junit.Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException()
    {
        RealMatrix matrix = new ArrayRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @org.junit.Test
    public void testIdentityMatrix()
    {
        RealMatrix inputMatrix = new ArrayRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = new ArrayRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        assertEquals("Transformed P matrix is not the same as the input matrix", expectedP, transformer.getP());
        RealMatrix expectedPt = new ArrayRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        assertEquals("Transformed P' matrix is not the same as the input matrix", expectedPt, transformer.getPT());
        RealMatrix expectedT = new ArrayRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        assertEquals("Transformed T matrix is not the same as the input matrix", expectedT, transformer.getT());
    }

    @org.junit.Test
    public void testSingularMatrix()
    {
        RealMatrix inputMatrix = new ArrayRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        SchurTransformer transformer = null;
        try {
            transformer = new SchurTransformer(inputMatrix);
        }
        catch (Exception e) {
            // Expected MaxCountExceededException
        }
        assertNotNull("Transformer was created without throwing an exception", transformer);
        RealMatrix expectedP = new ArrayRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix actualP = null;
        try {
            actualP = transformer.getP();
        }
        catch (NullPointerException e) {
            // Expected null P matrix
        }
        assertNull("P matrix was not null", actualP);
        RealMatrix expectedPt = new ArrayRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix actualPt = null;
        try {
            actualPt = transformer.getPT();
        }
        catch (NullPointerException e) {
            // Expected null P' matrix
        }
        assertNull("P' matrix was not null", actualPt);
        RealMatrix expectedT = new ArrayRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealMatrix actualT = null;
        try {
            actualT = transformer.getT();
        }
        catch (NullPointerException e) {
            // Expected null T matrix
        }
        assertNull("T matrix was not null", actualT);
    }

    @org.junit.Test
    public void testTransform()
    {
        RealMatrix inputMatrix = new ArrayRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 2.0}, {3.0, 2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = new ArrayRealMatrix(new double[][]{{0.53846152, -0.53846152, 0.81649658}, {-0.53846152, 0.53846152, -0.81649658}, {0.0, 0.0, 1.0}});
        RealMatrix actualP = transformer.getP();
        assertEquals("Transformed P matrix is not the same as the expected P matrix", expectedP, actualP);
        RealMatrix expectedPt = new ArrayRealMatrix(new double[][]{{0.53846152, 0.81649658, -0.53846152}, {-0.53846152, 0.81649658, 0.53846152}, {0.0, -1.0, 0.0}});
        RealMatrix actualPt = transformer.getPT();
        assertEquals("Transformed P' matrix is not the same as the expected P' matrix", expectedPt, actualPt);
        RealMatrix expectedT = new ArrayRealMatrix(new double[][]{{-0.76271193, -0.11983896, -0.44165466}, {-0.11983896, 1.67631282, -0.11983896}, {-0.44165466, -0.11983896, -0.76271193}});
        RealMatrix actualT = transformer.getT();
        assertEquals("Transformed T matrix is not the same as the expected T matrix", expectedT, actualT);
    }
}