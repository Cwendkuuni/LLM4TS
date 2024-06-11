import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixTestCase;
import org.apache.commons.math3.optim.InitialGuess;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for Weight.java
 */
public class WeightTest extends RealMatrixTestCase {

    /**
     * Test diagonal Weight matrix construction.
     */
    @Test
    public void testDiagonalWeightMatrixConstruction() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObject = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0}, {3.0}});
        RealMatrix actual = weightObject.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test non-diagonal Weight matrix construction.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonDiagonalWeightMatrixConstruction() {
        double[][] weight = {{-1.0, 2.0}, {3.0, 4.0}};
        Weight weightObject = new Weight(new Array2DRowRealMatrix(weight));
    }

    /**
     * Test getWeight method.
     */
    @Test
    public void testGetWeightMethod() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObject = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0}, {3.0}});
        RealMatrix actual = weightObject.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test getWeight method with existing Weight object.
     */
    @Test
    public void testGetWeightMethodWithExistingWeightObject() {
        double[] weight = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObject = new Weight(weightMatrix);
        RealMatrix actual = weightObject.getWeight();
        RealMatrix expected = weightMatrix.copy();
        assertEquals(expected.getData(), actual.getData(), 0.0);
    }

    /**
     * Test initialization of Weight object with null weight.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInitializationWithNullWeight() {
        new Weight(null);
    }

    /**
     * Test initialization of Weight object with empty weight.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInitializationWithEmptyWeight() {
        new Weight(new double[0]);
    }
}