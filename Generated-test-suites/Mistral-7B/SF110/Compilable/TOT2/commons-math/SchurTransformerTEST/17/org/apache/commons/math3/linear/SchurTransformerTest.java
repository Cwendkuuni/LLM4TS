package org.apache.commons.math3.linear;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SchurTransformerTest {
    @Test
    public void testGetP() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 3.0}
        });
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{
                {0.7465751712538523, -0.661202536062734},
                {-0.661202536062734, 0.7465751712538523}
        });
        RealMatrix actualP = schurTransformer.getP();
        assertEquals(expectedP, actualP);
    }
}