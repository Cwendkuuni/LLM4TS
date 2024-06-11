import org.junit.Test;
import org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyData() {
        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][]{});
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithLessThan2Rows() {
        try {
            double[][] data = new double[][]{{1, 2}, {1, 1}};
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    // ...
}