package org.apache.commons.math3.linear;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1.0, -1.0}, {-1.0, 1.0}});
        RealMatrix actualP = schurTransformer.getP();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData());
    }
}