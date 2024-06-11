package org.apache.commons.math3.linear;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(testMatrix);
        RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(new double[][]{{0.9999999999999998, 0.0}, {0.0, 0.9999999999999998}});
        RealMatrix actualMatrix = transformer.getP();

        // Test for equality
        assertEquals(expectedMatrix, actualMatrix);
    }
}