import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0}});
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.53571299, -0.53571299, 0.85355339}, {-0.53571299, 0.53571299, -0.85355339}, {0, 0, 1}});
        RealMatrix actualP = schurTransformer.getP();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
    }
}