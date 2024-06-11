package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.Streams;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        double[][] inputMatrix = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 7.0}
        };

        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        SchurTransformer transformer = new SchurTransformer(input);

        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 0.9692024995, 0.2745875263}, {0.0, 0.2745875263, 0.9692024995}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{7.0, 1.0, 0.0}, {1.0, 5.0, 1.14840657}, {0.0, 1.14840657, 2.0}});

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        PrecisionUtils.assertEquals(expectedP, actualP, 1E-13);
        PrecisionUtils.assertEquals(expectedT, actualT, 1E-13);

        double[][] expectedPt = Streams.asDoubleMatrix(expectedP.transpose());
        RealMatrix expectedPtRealMatrix = new Array2DRowRealMatrix(expectedPt);

        RealMatrix actualPt = transformer.getPT();

        PrecisionUtils.assertEquals(expectedPtRealMatrix, actualPt, 1E-13);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        double[][] inputMatrix = {
                {1.0, 2.0},
                {3.0}
        };

        RealMatrix input = new Array2DRowRealMatrix(inputMatrix);
        new SchurTransformer(input);
    }
}