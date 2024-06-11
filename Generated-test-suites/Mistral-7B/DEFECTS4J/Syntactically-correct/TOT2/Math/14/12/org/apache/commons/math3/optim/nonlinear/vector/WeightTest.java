import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class WeightTest {
    @Test
    public void testWeightDiagonal() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightSquareMatrix() {
        double[][] weight = {{1.0, 0.5}, {0.5, 1.0}};
        RealMatrix weightObj = new Array2DRowRealMatrix(weight);
        Weight weightInstance = new Weight(weightObj);
        RealMatrix expected = weightObj;
        RealMatrix actual = weightInstance.getWeight();
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }
}