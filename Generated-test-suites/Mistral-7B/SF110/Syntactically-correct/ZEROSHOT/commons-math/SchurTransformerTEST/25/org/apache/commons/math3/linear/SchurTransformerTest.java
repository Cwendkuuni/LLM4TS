package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.742578111, 0.611452818, -0.449594925}, {-0.611452818, 0.811626523, -0.418356681}, {0.449594925, -0.418356681, 0.888888889}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{-1.236289898, -0.949487103, 0.0}, {-0.949487103, 1.265456892, 0.0}, {0.0, 0.0, 1.0}});

        RealMatrix p = schurTransformer.getP();
        RealMatrix t = schurTransformer.getT();
        RealMatrix pt = schurTransformer.getPT();

        PrecisionUtils.assertEquals(expectedP, p, Precision.EPSILON);
        PrecisionUtils.assertEquals(expectedT, t, Precision.EPSILON);
        PrecisionUtils.assertEquals(expectedP.transpose(), pt, Precision.EPSILON);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        new SchurTransformer(matrix);
    }
}