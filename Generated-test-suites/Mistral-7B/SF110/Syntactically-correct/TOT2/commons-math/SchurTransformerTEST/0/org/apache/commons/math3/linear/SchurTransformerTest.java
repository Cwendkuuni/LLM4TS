import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.9328941659587913, -0.244948974278317}, {-0.244948974278317, 0.9328941659587913}});
        RealMatrix actualP = transformer.getP();
        Assert.assertEquals(expectedP, actualP);
    }
}