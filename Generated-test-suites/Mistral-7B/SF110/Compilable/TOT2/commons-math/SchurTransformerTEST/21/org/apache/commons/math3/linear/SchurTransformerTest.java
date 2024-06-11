package org.apache.commons.math3.linear;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix actual = transformer.getP();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.999976958481279, 0.0}, {0.0, 0.999992222425166}});
        assertEquals(expected, actual);
    }
}