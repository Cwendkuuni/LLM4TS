package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.UtilConversions;
import org.junit.Test;

import java.util.Arrays;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.5459883421401444, 0.8164965809277261, -0.1118033988755811},
                {-0.8164965809277261, 0.5459883421401444, -0.1118033988755811},
                {0.1118033988755811, 0.1118033988755811, 0.382683432295082}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{-0.184776319031456, 1.0, 0.0},
                {-0.746820302832658, 0.735398163397447, 0.408248290463863},
                {3.66622025258526, -1.86622025258526, 2.37252903600536}});

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        PrecisionUtils.assertEquals(expectedP, actualP, 1e-12);
        PrecisionUtils.assertEquals(expectedT, actualT, 1e-12);

        RealMatrix actualPt = schurTransformer.getPT();
        PrecisionUtils.assertEquals(expectedP.transpose(), actualPt, 1e-12);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        new SchurTransformer(inputMatrix);
    }
}