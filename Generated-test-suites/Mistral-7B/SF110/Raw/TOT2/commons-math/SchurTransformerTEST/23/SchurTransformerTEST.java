import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.535717854, -0.857418346}, {-0.857418346, 0.535717854}});
        RealMatrix actualP = transformer.getP();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
    }

    // Add edge cases and exception handling tests here
}