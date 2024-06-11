package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testIdentityMatrix() {
        RealMatrix input = MatrixUtils.createIdentityMatrix(5);
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = MatrixUtils.createIdentityMatrix(5);
        RealMatrix actualP = transformer.getP();
        RealMatrix expectedT = MatrixUtils.createIdentityMatrix(5);
        RealMatrix actualT = transformer.getT();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData());
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix input = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 3, 4}});
        new SchurTransformer(input);
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix input = MatrixUtils.createSingularMatrix(3);
        SchurTransformer transformer = null;
        try {
            transformer = new SchurTransformer(input);
        } catch (MaxCountExceededException e) {
            // Expected to fail
        }
        assertNull(transformer);
    }

    @Test
    public void testConvergence() {
        RealMatrix input = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}});
        SchurTransformer transformer = new SchurTransformer(input);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.53846023, -0.81649658, 0.12634342},
                {0.81649658, 0.53846023, -0.12634342},
                {-0.12634342, -0.12634342, 0.81649658}});
        RealMatrix actualP = transformer.getP();
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{1.11246341, 0.0, -0.61717435},
                {0.0, 1.11246341, 0.61717435},
                {-0.61717435, 0.61717435, 1.11246341}});
        RealMatrix actualT = transformer.getT();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData());
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData());
    }
}