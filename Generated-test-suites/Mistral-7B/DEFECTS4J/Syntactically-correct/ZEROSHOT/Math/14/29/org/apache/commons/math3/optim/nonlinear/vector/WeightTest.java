import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixTestCase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeightTest {

    @Test
    public void testDiagonalWeightMatrixCreation() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);

        RealMatrix actualWeight = weight.getWeight();
        RealMatrixTestCase.assertMatrixEquals(expectedWeight, actualWeight, 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightMatrixCreation() {
        double[] weights = new double[]{1.0, 2.0};
        new Weight(weights);
    }

    @Test
    public void testSquareWeightMatrixCreation() {
        double[] weights = new double[]{1.0, 1.0, 1.0, 1.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(4, 4);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 1.0);
        expectedWeight.setEntry(2, 2, 1.0);
        expectedWeight.setEntry(3, 3, 1.0);

        RealMatrix actualWeight = weight.getWeight();
        RealMatrixTestCase.assertMatrixEquals(expectedWeight, actualWeight, 0.0);
    }

    @Test
    public void testCopyWeightMatrix() {
        double[] weights = new double[]{1.0, 1.0, 1.0, 1.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(4, 4);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 1.0);
        expectedWeight.setEntry(2, 2, 1.0);
        expectedWeight.setEntry(3, 3, 1.0);

        RealMatrix actualWeight = weight.getWeight();
        RealMatrix copy = weight.getWeight().copy();

        RealMatrixTestCase.assertMatrixEquals(expectedWeight, copy, 0.0);
        RealMatrixTestCase.assertMatrixEquals(expectedWeight, actualWeight, 0.0);
    }

    @Test
    public void testNullWeightMatrix() {
        Weight weight = new Weight(null);

        assertNotNull(weight.getWeight());
    }

    @Test
    public void testEmptyWeightMatrix() {
        Weight weight = new Weight(new double[]{});

        assertEquals(0, weight.getWeight().getRowDimension());
        assertEquals(0, weight.getWeight().getColumnDimension());
    }
}