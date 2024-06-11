package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.special.FastMath;
import org.apache.commons.math3.util.Precision;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = RealMatrixUtils.createRealMatrix(new double[][]{{0.577350269189626, 0.577350269189626, 0.577350269189626},
                                                                           {0.577350269189626, 0.861136311594053, 0.382683432365089},
                                                                           {0.577350269189626, -0.382683432365089, 0.861136311594053}});
        RealMatrix actualP = schurTransformer.getP();
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = RealMatrixUtils.createRealMatrix(new double[][]{{0.577350269189626, 0.577350269189626, 0.577350269189626},
                                                                           {0.861136311594053, 0.382683432365089, -0.382683432365089},
                                                                           {0.382683432365089, -0.861136311594053, 0.577350269189626}});
        RealMatrix actualPT = schurTransformer.getPT();
        assertEquals(expectedPT, actualPT);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedT = RealMatrixUtils.createRealMatrix(new double[][]{{0.0, 0.0, 1.0}, {1.0, 0.0, -1.0}, {1.0, 0.0, 0.0}});
        RealMatrix actualT = schurTransformer.getT();
        assertEquals(expectedT, actualT);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithMaxIterationsExceeded() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        schurTransformer.transform();
    }
}