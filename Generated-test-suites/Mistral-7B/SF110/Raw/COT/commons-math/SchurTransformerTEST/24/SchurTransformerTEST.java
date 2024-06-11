package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.ShiftInfo;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0, 3.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.973581183428369, 0.236028043459222},
                {-0.236028043459222, 0.973581183428369}});
        RealMatrix actual = transformer.getP();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.973581183428369, -2.025812752362196},
                {2.025812752362196, 0.973581183428369}});
        RealMatrix actual = transformer.getPT();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0 - 2.0 * 0.236028043459222, 1.0},
                {2.0 * 0.236028043459222, 1.0 - 2.0 * 0.236028043459222}});
        RealMatrix actual = transformer.getT();
        assertEquals(expected, actual);
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{5.0, 3.0, 2.0}, {3.0, 2.0, 1.0}, {2.0, 1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[][] expectedMatrixP = {{-0.0, 0.9999999999999995, 1.000000000000004},
                {1.000000000000004, -0.0, 0.9999999999999997},
                {0.9999999999999997, 0.0, -0.0}};
        double[][] expectedMatrixT = {{4.000000000000001, 1.341640793830425, -0.645115414245948},
                {1.341640793830425, 2.220446049250313, 0.668935535357329},
                {-0.645115414245948, 0.668935535357329, 1.321328453731181}};

        double[][] actualMatrixP = transformer.getP().getData();
        double[][] actualMatrixT = transformer.getT().getData();

        PrecisionUtils.assertArrayEquals(expectedMatrixP, actualMatrixP, 1e-10);
        PrecisionUtils.assertArrayEquals(expectedMatrixT, actualMatrixT, 1e-10);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithConvergenceFailed() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{5.0, 3.0, 2.0}, {3.0, 2.0, 1.0}, {2.0, 1.0, 1.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
        transformer.transform(); // This should throw an exception
    }
}