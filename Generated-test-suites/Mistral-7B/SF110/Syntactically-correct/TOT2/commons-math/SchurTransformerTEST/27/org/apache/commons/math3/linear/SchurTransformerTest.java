import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {
    @Test
    public void testGetP() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{{0.966, 0.278}, {-0.278, 0.966}});
        RealMatrix actualResult = schurTransformer.getP();
        Assert.assertArrayEquals(expectedResult.getData(), actualResult.getData(), 0.001);
    }
}