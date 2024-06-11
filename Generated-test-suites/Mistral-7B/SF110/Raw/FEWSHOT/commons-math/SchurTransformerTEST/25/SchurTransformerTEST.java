package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.special.FastMath;
import org.apache.commons.math3.util.Precision;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructionWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {3.0, 4.0, 5.0} });
        new SchurTransformer(matrix);
    }

    @Test
    public void testGettersForEmptyTransform() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();
        assertEquals(matrix, p);
        assertEquals(matrix.transpose(), pt);
        assertEquals(matrix, t);
    }

    @Test
    public void testTransformWithSimpleMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0, 3.0} });
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][] { {1.0, -2.0}, {-2.0, 1.0} });
        RealMatrix expectedPT = expectedP.transpose();
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][] { {1.0, -2.0}, {-2.0, 1.0}});

        assertMatrixEquals(expectedP, p);
        assertMatrixEquals(expectedPT, pt);
        assertMatrixEquals(expectedT, t);
    }

    @Test
    public void testTransformWithSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 2.0}, {2.0, 1.0} });
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        RealMatrix expectedP = MatrixUtils.createIdentityMatrix(2);
        RealMatrix expectedPT = expectedP.transpose();
        RealMatrix expectedT = expectedP;

        assertMatrixEquals(expectedP, p);
        assertMatrixEquals(expectedPT, pt);
        assertMatrixEquals(expectedT, t);
    }

    @Test
    public void testTransformWithDiagonalMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {1.0, 0.0}, {0.0, 2.0} });
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][] { {1.0, 0.0}, {0.0, 1.0} });
        RealMatrix expectedPT = expectedP.transpose();
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][] { {1.0, 0.0}, {0.0, 1.0}});

        assertMatrixEquals(expectedP, p);
        assertMatrixEquals(expectedPT, pt);
        assertMatrixEquals(expectedT, t);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithNonConvergingMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { {0.0, 1.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 0.0} });
        new SchurTransformer(matrix);
    }

    private void assertMatrixEquals(RealMatrix a, RealMatrix b) {
        assertEquals(a.getRowDimension(), b.getRowDimension());
        assertEquals(a.getColumnDimension(), b.getColumnDimension());

        for (int i = 0; i < a.getRowDimension(); i++) {
            RealVector rowA = a.getColumnVector(i);
            RealVector rowB = b.getColumnVector(i);
            for (int j = 0; j < rowA.getDimension(); j++) {
                assertEquals(rowA.getEntry(j), rowB.getEntry(j), 1e-15);
            }
        }
    }
}