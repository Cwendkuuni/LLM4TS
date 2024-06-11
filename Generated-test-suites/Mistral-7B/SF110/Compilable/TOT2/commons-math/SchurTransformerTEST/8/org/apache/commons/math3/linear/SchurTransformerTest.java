package org.apache.commons.math3.linear;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.linear.RealMatrix;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.57348227, 0.85702754}, {-0.85702754, 0.57348227}});
        RealMatrix actualP = transformer.getP();
        Assert.assertEquals(expectedP, actualP);
    }
}