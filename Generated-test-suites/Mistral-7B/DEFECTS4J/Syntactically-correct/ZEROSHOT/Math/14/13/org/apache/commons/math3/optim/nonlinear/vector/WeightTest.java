import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testWeightConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = w.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testWeightConstructorWithRealMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0},
                {0.0, 0.0}
        });
        Weight w = new Weight(weight);
        RealMatrix expected = weight.copy();

        RealMatrix actual = w.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test constructor with non-square RealMatrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareRealMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0}
        });
        new Weight(weight);
    }

    /**
     * Test getWeight() method.
     */
    @Test
    public void testGetWeight() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = w.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test getWeight() method with the same RealMatrix instance.
     */
    @Test
    public void testGetWeightWithSameRealMatrixInstance() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0},
                {0.0, 0.0}
        });
        Weight w = new Weight(weight);
        RealMatrix actual = w.getWeight();

        RealMatrix expected = weight.copy();
        assertEquals(expected, actual);

        // Modify weight instance
        weight.setEntry(0, 0, 4.0);

        // Test getWeight() method again
        actual = w.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test the inverse of the weight matrix.
     */
    @Test
    public void testInverse() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix weightMatrix = w.getWeight();

        SingularValueDecomposition svd = new SingularValueDecomposition(weightMatrix);
        RealMatrix invWeightMatrix = svd.getInverse();

        RealMatrix expectedInvWeight = new Array2DRowRealMatrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                double invWeightElement = 1 / (weight[i] * invWeightMatrix.getEntry(i, i));
                expectedInvWeight.setEntry(i, j, invWeightElement *
                        (i == j ? invWeightMatrix.getEntry(i, i) : 0.0));
            }
        }

        RealMatrix actualInvWeight = w.inverse();
        assertEquals(expectedInvWeight, actualInvWeight);

        // Check the inverse with the original weight matrix
        RealMatrix originalWeight = new Array2DRowRealMatrix(new double[][]{
                        {1.0, 0.0},
                        {0.0, 2.0},
                        {0.0, 0.0}
                });
        Weight originalWeightObject = new Weight(originalWeight);
        RealMatrix originalInvWeight = originalWeightObject.inverse();
        assertEquals(expectedInvWeight, originalInvWeight);
    }

    /**
     * Test the determinant of the weight matrix.
     */
    @Test
    public void testDeterminant() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix weightMatrix = w.getWeight();

        double expectedDeterminant = 1.0 / (weight[0] * weight[1] * weight[2]);

        double actualDeterminant = w.determinant();
        assertEquals(expectedDeterminant, actualDeterminant, 1e-10);

        // Test with the same weight matrix, different instance
        RealMatrix originalWeight = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0},
                {0.0, 0.0}
        });
        Weight originalWeightObject = new Weight(originalWeight);
        double originalDeterminant = originalWeightObject.determinant();
        assertEquals(expectedDeterminant, originalDeterminant, 1e-10);
    }

    /**
     * Test the transpose of the weight matrix.
     */
    @Test
    public void testTranspose() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix weightMatrix = w.getWeight();

        RealMatrix expectedTranspose = new Array2DRowRealMatrix(3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                expectedTranspose.setEntry(j, i, weightMatrix.getEntry(i, j));
            }
        }

        RealMatrix actualTranspose = w.transpose();
        assertEquals(expectedTranspose, actualTranspose);

        // Test with the same weight matrix, different instance
        RealMatrix originalWeight = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0},
                {0.0, 0.0}
        });
        Weight originalWeightObject = new Weight(originalWeight);
        RealMatrix originalTranspose = originalWeightObject.transpose();
        assertEquals(expectedTranspose, originalTranspose);
    }

    /**
     * Test if the weight matrix is positive definite.
     */
    @Test
    public void testIsPositiveDefinite() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix weightMatrix = w.getWeight();

        double tolerance = 1e-15;
        RealVector eigenvalues = new SingularValueDecomposition(weightMatrix).getDiagonal();

        for (double eigenvalue : eigenvalues) {
            assertTrue(Precision.isGreater(eigenvalue, 0.0, tolerance));
        }

        // Test with the same weight matrix, different instance
        RealMatrix originalWeight = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0},
                {0.0, 0.0}
        });
        Weight originalWeightObject = new Weight(originalWeight);
        assertTrue(originalWeightObject.isPositiveDefinite());
    }
}