package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        RealMatrix matrix = Array2DRenderers.create(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);

        RealMatrix expectedP = Array2DRenderers.create(new double[][]{{0.53571288, -0.85773667, 0.0}, {-0.85773667, 0.53571288, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix expectedT = Array2DRenderers.create(new double[][]{{-0.44948974, -0.44948974, 0.89442718}, {-0.44948974, 1.89895558, 0.44948974}, {0.89442718, 0.44948974, -0.44948974}});
        RealMatrix expectedPt = expectedP.transpose();

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();
        RealMatrix actualPt = schurTransformer.getPT();

        assertEquals(expectedP.getRowDimension(), actualP.getRowDimension());
        assertEquals(expectedP.getColumnDimension(), actualP.getColumnDimension());
        assertNotNull(actualP);
        assertEquals(expectedT.getRowDimension(), actualT.getRowDimension());
        assertEquals(expectedT.getColumnDimension(), actualT.getColumnDimension());
        assertNotNull(actualT);
        assertEquals(expectedPt.getRowDimension(), actualPt.getRowDimension());
        assertEquals(expectedPt.getColumnDimension(), actualPt.getColumnDimension());
        assertNotNull(actualPt);

        assertArrayEquals(expectedP.getData(), actualP.getData(), 1e-12);
        assertArrayEquals(expectedT.getData(), actualT.getData(), 1e-12);
        assertArrayEquals(expectedPt.getData(), actualPt.getData(), 1e-12);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = Array2DRenderers.create(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        new SchurTransformer(matrix);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformerWithMaxIterationsExceeded() {
        RealMatrix matrix = Array2DRenderers.create(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        new SchurTransformer(matrix);
    }
}