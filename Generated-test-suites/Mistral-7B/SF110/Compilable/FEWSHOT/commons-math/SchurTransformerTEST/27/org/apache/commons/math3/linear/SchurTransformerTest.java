package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.53846931, 0.85065061, 0.13205662}, {-0.85065061, 0.53846931, 0.13205662}, {-0.13205662, -0.13205662, 0.96592583}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{3.02205154, 3.31822368, 2.20165903}, {3.31822368, 3.90532433, 2.72240281}, {2.20165903, 2.72240281, 1.84740254}});

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertEquals(expectedP.getEntry(0, 0), actualP.getEntry(0, 0), 0.0001);
        assertEquals(expectedP.getEntry(0, 1), actualP.getEntry(0, 1), 0.0001);
        assertEquals(expectedP.getEntry(0, 2), actualP.getEntry(0, 2), 0.0001);
        assertEquals(expectedP.getEntry(1, 0), actualP.getEntry(1, 0), 0.0001);
        assertEquals(expectedP.getEntry(1, 1), actualP.getEntry(1, 1), 0.0001);
        assertEquals(expectedP.getEntry(1, 2), actualP.getEntry(1, 2), 0.0001);
        assertEquals(expectedP.getEntry(2, 0), actualP.getEntry(2, 0), 0.0001);
        assertEquals(expectedP.getEntry(2, 1), actualP.getEntry(2, 1), 0.0001);
        assertEquals(expectedP.getEntry(2, 2), actualP.getEntry(2, 2), 0.0001);

        assertEquals(expectedT.getEntry(0, 0), actualT.getEntry(0, 0), 0.0001);
        assertEquals(expectedT.getEntry(0, 1), actualT.getEntry(0, 1), 0.0001);
        assertEquals(expectedT.getEntry(0, 2), actualT.getEntry(0, 2), 0.0001);
        assertEquals(expectedT.getEntry(1, 0), actualT.getEntry(1, 0), 0.0001);
        assertEquals(expectedT.getEntry(1, 1), actualT.getEntry(1, 1), 0.0001);
        assertEquals(expectedT.getEntry(1, 2), actualT.getEntry(1, 2), 0.0001);
        assertEquals(expectedT.getEntry(2, 0), actualT.getEntry(2, 0), 0.0001);
        assertEquals(expectedT.getEntry(2, 1), actualT.getEntry(2, 1), 0.0001);
        assertEquals(expectedT.getEntry(2, 2), actualT.getEntry(2, 2), 0.0001);
    }
}