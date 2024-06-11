package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class SchurTransformerTest
{
    @Test
    public void testSchurTransformer() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{
                {0.53452328, -0.48259562, -0.73797354},
                {-0.81922187, 0.42535437, 1.44969534},
                {-0.43175773, -0.9038205, 0.27580383}
        });

        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{
                {1.61497266, 2.67423515, 2.78524584},
                {2.67423515, 5.55111512, -0.62025384},
                {2.78524584, -0.62025384, 5.82973312}
        });

        RealMatrix p = schurTransformer.getP();
        RealMatrix t = schurTransformer.getT();

        assertEquals(expectedP, p);
        assertEquals(expectedT, t);
    }

    @Test
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});

        assertThrows(NonSquareMatrixException.class, () -> new SchurTransformer(inputMatrix));
    }

    @Test
    public void testSchurTransformerWithNullMatrix() {
        RealMatrix inputMatrix = null;

        assertThrows(NullPointerException.class, () -> new SchurTransformer(inputMatrix));
    }

    @Test
    public void testGetPT() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{
                {0.53452328, -0.48259562, -0.73797354},
                {-0.81922187, 0.42535437, 1.44969534},
                {-0.43175773, -0.9038205, 0.27580383}
        });

        RealMatrix pt = schurTransformer.getPT();

        assertEquals(expectedPT, pt);
    }

    @Test
    public void testGetT() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{
                {1.61497266, 2.67423515, 2.78524584},
                {2.67423515, 5.55111512, -0.62025384},
                {2.78524584, -0.62025384, 5.82973312}
        });

        RealMatrix t = schurTransformer.getT();

        assertEquals(expectedT, t);
    }

    @Test
    public void testGetP_Cached() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix cachedP = schurTransformer.getP();

        SchurTransformer schurTransformer2 = new SchurTransformer(inputMatrix);

        RealMatrix cachedP2 = schurTransformer2.getP();

        assertEquals(cachedP, cachedP2);
    }

    @Test
    public void testGetPT_Cached() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix cachedPt = schurTransformer.getPT();

        SchurTransformer schurTransformer2 = new SchurTransformer(inputMatrix);

        RealMatrix cachedPt2 = schurTransformer2.getPT();

        assertEquals(cachedPt, cachedPt2);
    }

    @Test
    public void testGetT_Cached() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix cachedT = schurTransformer.getT();

        SchurTransformer schurTransformer2 = new SchurTransformer(inputMatrix);

        RealMatrix cachedT2 = schurTransformer2.getT();

        assertEquals(cachedT, cachedT2);
    }

    @Test
    public void testTransform() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 5.0},
                {3.0, 5.0, 6.0}
        });

        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);

        RealMatrix p = schurTransformer.getP();
        RealMatrix t = schurTransformer.getT();

        SchurTransformer schurTransformer2 = new SchurTransformer(t);

        RealMatrix p2 = schurTransformer2.getP();

        Assert.assertArrayEquals(p.getData(), p2.getData(), PrecisionUtils.getDelta(1.0e-14));
    }
}