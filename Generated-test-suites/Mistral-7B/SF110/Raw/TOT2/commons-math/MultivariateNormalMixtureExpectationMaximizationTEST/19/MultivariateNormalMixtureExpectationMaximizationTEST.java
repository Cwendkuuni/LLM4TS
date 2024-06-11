import org.junit.Test;
import org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructorWithValidData() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.1, 3.2}, {3.1, 4.3}};
        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        Assert.assertNotNull(mmme.getFittedModel());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyData() {
        double[][] data = {};
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidData() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.1}, {3.1, 4.3}};
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected exception
        }
    }
}