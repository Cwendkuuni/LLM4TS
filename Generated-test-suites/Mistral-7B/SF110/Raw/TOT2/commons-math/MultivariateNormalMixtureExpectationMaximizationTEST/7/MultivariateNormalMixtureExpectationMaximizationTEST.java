import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyData() {
        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[0][]);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithDataOfWrongLength() {
        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][] { new double[1], new double[2] });
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
        }

        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][] { new double[2], new double[1] });
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithDataOfDifferentDimensions() {
        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][] { new double[2], new double[3] });
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
        }
    }

    @Test
    public void testConstructorWithValidData() {
        double[][] data = new double[][] { { 1.0, 2.0 }, { 3.0, 4.0 } };
        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        Assert.assertEquals(2, mmme.getData().length);
        Assert.assertNotNull(mmme.getFittedModel());
    }
}