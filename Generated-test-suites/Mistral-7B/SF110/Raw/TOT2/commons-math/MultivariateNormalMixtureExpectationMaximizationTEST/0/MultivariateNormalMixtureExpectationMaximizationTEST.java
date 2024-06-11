import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testMultivariateNormalMixtureExpectationMaximizationConstructor() {
        // Test with valid data
        double[][] validData = new double[][] {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(validData);
        assertTrue(multivariateNormalMixtureExpectationMaximization.getData().length == validData.length);

        // Test with empty data
        double[][] emptyData = new double[][] {};
        try {
            new MultivariateNormalMixtureExpectationMaximization(emptyData);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        // Test with data with different row lengths
        double[][] differentData = new double[][] {{1.0, 2.0}, {3.0}, {4.0, 5.0}};
        try {
            new MultivariateNormalMixtureExpectationMaximization(differentData);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        // Test with data with fewer than 2 columns
        double[][] fewerColumnsData = new double[][] {{1.0, 2.0}, {3.0}};
        try {
            new MultivariateNormalMixtureExpectationMaximization(fewerColumnsData);
            fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInitialMixtureAndSpecificMaxIterationsAndThreshold() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        double[][] testData = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};

        MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(testData);
        multivariateNormalMixtureExpectationMaximization.fit(initialMixture, 10, 0.1);

        assertEquals(multivariateNormalMixtureExpectationMaximization.getLogLikelihood(), 3.0, 0.1); // Approximately
        assertTrue(multivariateNormalMixtureExpectationMaximization.getFittedModel().getComponents().size() == 3);
    }

    @Test
    public void testEstimate() {
        double[][] testData = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {1.0, 2.0}, {3.0, 4.0}};

        MixtureMultivariateNormalDistribution expectedEstimate = MultivariateNormalMixtureExpectationMaximization.estimate(testData, 3);

        MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(testData);
        MixtureMultivariateNormalDistribution actualEstimate = multivariateNormalMixtureExpectationMaximization.getFittedModel();

        assertEquals(expectedEstimate, actualEstimate);
    }

    @Test
    public void testFitWithInitialMixture() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        double[][] testData = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};

        MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(testData);
        multivariateNormalMixtureExpectationMaximization.fit(initialMixture);

        assertEquals(multivariateNormalMixtureExpectationMaximization.getLogLikelihood(), 3.0, 0.1); // Approximately
        assertTrue(multivariateNormalMixtureExpectationMaximization.getFittedModel().getComponents().size() > 0);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrix() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        double[][] testData = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 5.0}};

        MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(testData);
        multivariateNormalMixtureExpectationMaximization.fit(initialMixture);

        fail("Expected SingularMatrixException");
    }

    @Test(expected = ConvergenceException.class)
    public void testFitWithMaxIterationsAndThresholdNotReached() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        double[][] testData = new double[][] {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};

        MultivariateNormalMixtureExpectationMaximization multivariateNormalMixtureExpectationMaximization = new MultivariateNormalMixtureExpectationMaximization(testData);
        multivariateNormalMixtureExpectationMaximization.fit(initialMixture, 1, 1);

        fail("Expected ConvergenceException");
    }
}