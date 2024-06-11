package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MathUtils;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerInitialization() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        assertNotNull(schurTransformer.getP());
        assertNotNull(schurTransformer.getPT());
        assertNotNull(schurTransformer.getT());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerInvalidInitialization() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.93249999999999997, -0.27609999999999999, -0.23199999999999999},
                {-0.27609999999999999, 0.93249999999999997, 0.23199999999999999},
                {-0.23199999999999999, -0.23199999999999999, 0.9658}});
        RealMatrix p = schurTransformer.getP();
        assertEquals(expectedP, p);
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{{0.93249999999999997, -0.27609999999999999, -0.23199999999999999},
                {-0.27609999999999999, 0.93249999999999997, 0.23199999999999999},
                {-0.23199999999999999, -0.23199999999999999, 0.9658}});
        RealMatrix pt = schurTransformer.getPT();
        assertEquals(expectedPT, pt);
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{4.0, 2.0, 1.0}, {2.0, 2.0, 2.0}, {1.0, 2.0, 6.0}});
        RealMatrix t = schurTransformer.getT();
        assertEquals(expectedT, t);
    }

    @Test
    public void testTransform() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.93249999999999997, -0.27609999999999999, -0.23199999999999999},
                {-0.27609999999999999, 0.93249999999999997, 0.23199999999999999},
                {-0.23199999999999999, -0.23199999999999999, 0.9658}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{4.0, 2.0, 1.0}, {2.0, 2.0, 2.0}, {1.0, 2.0, 6.0}});
        RealMatrix expectedPt = new Array2DRowRealMatrix(new double[][]{{-0.93249999999999997, 0.27609999999999999, 0.23199999999999999},
                {0.27609999999999999, -0.93249999999999997, -0.23199999999999999},
                {-0.23199999999999999, -0.23199999999999999, 0.9658}});
        RealMatrix p = schurTransformer.getP();
        RealMatrix pt = schurTransformer.getPT();
        RealMatrix t = schurTransformer.getT();
        assertEquals(expectedP, p);
        assertEquals(expectedPt, pt);
        assertEquals(expectedT, t);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformMaxIterations() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        schurTransformer.transform();
        schurTransformer.transform();
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        double expectedNorm = 10.061674251623637;
        double norm = schurTransformer.getNorm();
        assertEquals(PrecisionUtils.round(expectedNorm, 6), norm, 1e-6);
    }

    @Test
    public void testFindSmallSubDiagonalElement() {
        double[][] matrix = new double[][]{{1.0, 2.0, 3.0, 4.0}, {2.0, 4.0, 5.0, 6.0}, {3.0, 5.0, 7.0, 8.0}, {4.0, 6.0, 8.0, 10.0}};
        SchurTransformer schurTransformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));
        int expectedIl = 2;
        int il = schurTransformer.findSmallSubDiagonalElement(3, schurTransformer.getNorm());
        assertEquals(expectedIl, il);
    }

}