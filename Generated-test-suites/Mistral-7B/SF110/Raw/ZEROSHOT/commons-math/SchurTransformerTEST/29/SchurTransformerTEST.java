package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 2.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.5384602516981366, -0.5384602516981366, 0.816496580927726},
                {0.816496580927726, 0.5384602516981366, 0.0},
                {-0.5384602516981366, 0.5384602516981366, 0.5384602516981366}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{1.9021322468562347, 0.0, 0.0},
                {0.0, 0.901066337923617, 0.0},
                {0.0, 0.0, 0.901066337923617}});
        RealMatrix expectedPt = MatrixUtils.createRealMatrix(expectedP.transpose());

        RealMatrix p = schurTransformer.getP();
        RealMatrix pt = schurTransformer.getPT();
        RealMatrix t = schurTransformer.getT();

        assertNotNull(p);
        assertNotNull(pt);
        assertNotNull(t);
        assertArrayEquals(expectedP.getData(), p.getData(), 0.001);
        assertArrayEquals(expectedPt.getData(), pt.getData(), 0.001);
        assertArrayEquals(expectedT.getData(), t.getData(), 0.001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0}});
        new SchurTransformer(inputMatrix);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformerWithMaxCountExceededException() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 1000000.0}});
        new SchurTransformer(inputMatrix);
    }
}