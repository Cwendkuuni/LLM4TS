import org.junit.Test;
import org.junit.Assert.*; // for Assert.assertEquals, Assert.assertThrows
import org.apache.commons.math3.distribution.fitting.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructorWithValidData() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization nnme = new MultivariateNormalMixtureExpectationMaximization(data);
        Assert.assertNotNull(nnme.getFittedModel());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDataLength() {
        double[][] data = new double[][]{{1.0, 2.0}};
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidDataShape() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0}};
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidDataSize() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 1.0}};
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected exception
        }
    }
}