package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0, 5.0}
        });
        new SchurTransformer(matrix);
    }

    @Test
    public void testSchurTransformerGetters() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 1.0, 4.0},
                {3.0, 4.0, 1.0}
        });
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();
        assertTrue(p.isSymmetric());
        assertTrue(pt.isSymmetric());
        assertTrue(t.isUpperTriangular());
    }

    @Test
    public void testSchurTransformerTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 1.0, 4.0},
                {3.0, 4.0, 1.0}
        });
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix pt = transformer.getPT();
        RealMatrix t = transformer.getT();

        double[][] expectedP = {
                {0.449489742783171, 0.734385558329914, -0.433012701892219},
                {0.734385558329914, 0.861962554599797, -0.135315225528263},
                {-0.433012701892219, -0.135315225528263, 0.498638717575211}
        };
        double[][] expectedT = {
                {5.11237231651337, 2.23606797749978, 1.46754174374993},
                {2.23606797749978, 3.2399984178228, 2.27448706879045},
                {1.46754174374993, 2.27448706879045, 1.0}
        };

        for (int i = 0; i < p.getRowDimension(); i++) {
            assertArrayEquals(expectedP[i], p.getRowVector(i).toArray(), 0.0);
        }

        for (int i = 0; i < t.getRowDimension(); i++) {
            assertArrayEquals(expectedT[i], t.getRowVector(i).toArray(), 0.0);
        }

        for (int i = 0; i < p.getColumnDimension(); i++) {
            assertArrayEquals(expectedP[i], p.getColumnVector(i).toArray(), 0.0);
        }

        for (int i = 0; i < t.getColumnDimension(); i++) {
            assertArrayEquals(expectedT[i], t.getColumnVector(i).toArray(), 0.0);
        }
    }
}