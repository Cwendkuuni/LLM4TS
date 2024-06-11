package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.FastMath;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);

        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.742566848236663, -0.6154228336853744, 0.428652899713531},
                {-0.6154228336853744, 0.742566848236663, -0.428652899713531},
                {0.428652899713531, -0.428652899713531, 0.7320508075688772}});

        RealMatrix actualP = schurTransformer.getP();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);

        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{4.0, 3.0, 2.0}, {3.0, 4.0, 1.0}, {2.0, 1.0, 0.0}});

        RealMatrix actualT = schurTransformer.getT();
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);

        RealMatrix expectedPt = MatrixUtils.createRealMatrix(new double[][]{{0.742566848236663, 0.428652899713531, 0.428652899713531},
                {-0.6154228336853744, 0.7320508075688772, -0.428652899713531},
                {0.428652899713531, -0.428652899713531, 0.7320508075688772}});

        RealMatrix actualPt = schurTransformer.getPT();
        Assert.assertArrayEquals(expectedPt.getData(), actualPt.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0}});
        new SchurTransformer(matrix);
    }
}