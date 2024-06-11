package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.ArrayRealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public class SchurTransformerTest
{
    @org.junit.Test
    public void testSchurTransformerConstructor() {
        RealMatrix matrix = new ArrayRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new ArrayRealMatrix(new double[][]{{0.57357823, 0.53452303, 0.81649658}, {-0.53452303, 0.81649658, 0.57357823}, {-0.81649658, -0.57357823, 0.53452303}});
        RealMatrix expectedT = new ArrayRealMatrix(new double[][]{{4.14142137, -1.54308063, 0.0}, {-1.54308063, 2.63602824, 1.2955273}, {0.0, 1.2955273, 3.63637247}});
        RealMatrix p = schurTransformer.getP();
        RealMatrix t = schurTransformer.getT();
        assertEquals(expectedP, p);
        assertEquals(expectedT, t);

        // Test with non-square matrix
        try {
            new SchurTransformer(new ArrayRealMatrix(new double[][]{{1.0}}));
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testGetP() {
        RealMatrix matrix = new ArrayRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix p = schurTransformer.getP();
        assertEquals(p.getRowDimension(), p.getColumnDimension());
    }

    @org.junit.Test
    public void testGetPT() {
        RealMatrix matrix = new ArrayRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix pt = schurTransformer.getPT();
        RealMatrix p = schurTransformer.getP();
        assertEquals(pt.getRowDimension(), p.getColumnDimension());
        assertEquals(pt, p.transpose());
    }

    @org.junit.Test
    public void testGetT() {
        RealMatrix matrix = new ArrayRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix t = schurTransformer.getT();
        assertEquals(t.getRowDimension(), t.getColumnDimension());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetPBeforeTransform() {
        RealMatrix matrix = new ArrayRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        schurTransformer.getP();
        schurTransformer.transform();
        RealMatrix p = schurTransformer.getP();
    }

    @org.junit.Test
    public void testTransform() {
        RealMatrix matrix = new ArrayRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        schurTransformer.transform();

        RealMatrix expectedP = new ArrayRealMatrix(new double[][]{{0.57357823, 0.53452303, 0.81649658}, {-0.53452303, 0.81649658, 0.57357823}, {-0.81649658, -0.57357823, 0.53452303}});
        RealMatrix expectedT = new ArrayRealMatrix(new double[][]{{4.14142137, -1.54308063, 0.0}, {-1.54308063, 2.63602824, 1.2955273}, {0.0, 1.2955273, 3.63637247}});

        RealMatrix p = schurTransformer.getP();
        RealMatrix t = schurTransformer.getT();

        assertEquals(expectedP, p);
        assertEquals(expectedT, t);
    }

    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testTransformWithMaxIterationsReached() {
        RealMatrix matrix = new ArrayRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 1.0, 4.0}, {3.0, 4.0, 1.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        schurTransformer.transform();
    }
}