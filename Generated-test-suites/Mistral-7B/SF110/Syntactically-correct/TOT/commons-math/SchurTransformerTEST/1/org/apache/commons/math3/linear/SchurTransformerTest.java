package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testEmptyMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[0][0]);
        new SchurTransformer(matrix);
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(5);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        Assert.assertEquals(p, MatrixUtils.createIdentityMatrix(5));
        Assert.assertEquals(pt, MatrixUtils.createIdentityMatrix(5));
        Assert.assertEquals(t, MatrixUtils.createIdentityMatrix(5));
    }

    @Test
    public void testRandomMatrix() {
        RealMatrix matrix = MatrixUtils.createRandomRealMatrix(5, 5);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][] {
            {1.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, 0.632120559, -0.265403753, -0.150118626, -0.043245334},
            {0.0, -0.265403753, 0.614681537, -0.363179682, -0.107677979},
            {0.0, -0.150118626, -0.363179682, 0.642849334, 0.132279397},
            {0.0, -0.043245334, -0.107677979, 0.132279397, 0.731573243}
        });

        RealMatrix expectedPt = MatrixUtils.createRealMatrix(new double[][] {
            {1.0, 0.632120559, 0.265403753, 0.150118626, 0.043245334},
            {0.632120559, 0.385194159, 0.108482482, 0.036726608, -0.048881676},
            {0.265403753, 0.108482482, 0.530927355, 0.239170591, -0.078485976},
            {0.150118626, 0.036726608, 0.239170591, 0.632487435, 0.130310603},
            {0.043245334, -0.048881676, -0.078485976, 0.130310603, 0.731005864}
        });

        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][] {
            {0.632120559, -0.150118626, 0.043245334, 0.0, 0.0},
            {-0.150118626, 0.614681537, -0.363179682, 0.0, 0.0},
            {0.043245334, -0.363179682, 0.642849334, 0.0, 0.0},
            {0.0, 0.0, 0.0, 1.0, 0.0},
            {0.0, 0.0, 0.0, 0.0, 1.0}
        });

        Assert.assertEquals(p, expectedP, 1e-12);
        Assert.assertEquals(pt, expectedPt, 1e-12);
        Assert.assertEquals(t, expectedT, 1e-12);
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1.0, 0.0},
            {0.0, 0.0}
        });
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        Assert.assertNull(p);
        Assert.assertNull(pt);
        Assert.assertNull(t);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() {
        RealMatrix matrix = MatrixUtils.createRandomRealMatrix(10, 10);
        SchurTransformer transformer = new SchurTransformer(matrix);
        transformer.transform();
    }
}