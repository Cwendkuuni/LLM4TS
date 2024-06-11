import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testDiagonalWeightConstruction() {
        // Given
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);

        // When
        RealMatrix actualWeight = weightObj.getWeight();

        // Then
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3, new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        assertEquals(expectedWeight, actualWeight);
    }

    // Continue with other test cases for other methods and edge cases
}