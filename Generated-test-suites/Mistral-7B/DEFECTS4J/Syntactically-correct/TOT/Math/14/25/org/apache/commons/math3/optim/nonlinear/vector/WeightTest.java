import org.apache.commons.math3.optim.initializer.Weight;
import org.apache.commons.math3.optim.initializer.WeightImpl;
import org.apache.commons.math3.optim.nonlinear.SimpleValueChecker;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Weight.
 */
public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testDiagonalWeightMatrix() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);
        RealMatrix actualWeight = weightObj.getWeight();

        // Test equality
        assertEquals("Unexpected weight matrix.", expectedWeight, actualWeight);

        // Test getDiagonal()
        RealMatrix diagonal = weightObj.getWeight().getDiagonal();
        assertArrayEquals("Unexpected diagonal.", new double[]{1.0, 2.0, 3.0}, diagonal.getData(), 0.0);

        // Test isIdentity()
        SimpleValueChecker checker = new SimpleValueChecker(1.0);
        assertFalse("Weight matrix is identity.", weightObj.getWeight().isIdentity(checker));
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testWeightMatrix() {
        double[][] weightData = new double[][]{
                {1.0, 0.0},
                {0.0, 2.0}
        };
        RealMatrix weight = new Array2DRowRealMatrix(weightData);
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = weight.copy();
        RealMatrix actualWeight = weightObj.getWeight();

        // Test equality
        assertEquals("Unexpected weight matrix.", expectedWeight, actualWeight);

        // Test getColumnDimension()
        assertEquals("Unexpected column dimension.", 2, weightObj.getWeight().getColumnDimension());

        // Test getRowDimension()
        assertEquals("Unexpected row dimension.", 2, weightObj.getWeight().getRowDimension());
    }
}