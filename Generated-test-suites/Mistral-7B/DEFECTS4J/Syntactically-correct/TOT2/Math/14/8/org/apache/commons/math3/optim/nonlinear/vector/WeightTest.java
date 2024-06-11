import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;

public class WeightTest {
    @Test
    public void testWeightConstructorWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightInstance = new Weight(weight);
        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(3, 3);
        expectedWeightMatrix.setEntry(0, 0, 1.0);
        expectedWeightMatrix.setEntry(1, 1, 2.0);
        expectedWeightMatrix.setEntry(2, 2, 3.0);
        RealMatrix actualWeightMatrix = weightInstance.getWeight();
        Assert.assertArrayEquals(expectedWeightMatrix.getData(), actualWeightMatrix.getData(), 0.0);
    }
}