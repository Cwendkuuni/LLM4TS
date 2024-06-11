import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.junit.Test;
import org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization instance = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(instance.getFittedModel());
    }
}