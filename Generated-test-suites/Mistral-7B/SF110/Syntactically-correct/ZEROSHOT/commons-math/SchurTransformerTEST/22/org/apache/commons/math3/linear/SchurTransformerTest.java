package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerThrowsExceptionForNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {1.0, 3.0, 4.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGettersReturnsCorrectMatrices() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix pMatrix = transformer.getP();
        RealMatrix tMatrix = transformer.getT();
        RealMatrix ptMatrix = transformer.getPT();

        assertNotNull(pMatrix);
        assertNotNull(tMatrix);
        assertNotNull(ptMatrix);

        assertEquals(3, pMatrix.getRowDimension());
        assertEquals(3, pMatrix.getColumnDimension());
        assertEquals(3, tMatrix.getRowDimension());
        assertEquals(3, tMatrix.getColumnDimension());
        assertEquals(3, ptMatrix.getRowDimension());
        assertEquals(3, ptMatrix.getColumnDimension());
    }

    @Test
    public void testPerformTransform() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{5.0, 2.0, 0.0}, {2.0, 3.0, 3.0}, {0.0, 3.0, 5.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);

        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.7320508075688772, 0.6153688217165585, 0.395023531313438},
                {-0.6153688217165585, 0.8173269304816327, 0.24334244497653213},
                {0.395023531313438, -0.24334244497653213, 0.572978475463436}});

        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{3.0, -2.0, 0.0}, {-2.0, 3.5, -1.5}, {0.0, -1.5, 2.5}});

        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();

        assertArrayEquals(expectedP.getData(), p.getData(), 1e-10);
        assertArrayEquals(expectedT.getData(), t.getData(), 1e-10);
    }

    @Test
    public void testTransformerConvergence() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{5.0, 2.0, 0.0}, {2.0, 3.0, 3.0}, {0.0, 3.0, 5.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);

        try {
            transformer.transform();
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }
}