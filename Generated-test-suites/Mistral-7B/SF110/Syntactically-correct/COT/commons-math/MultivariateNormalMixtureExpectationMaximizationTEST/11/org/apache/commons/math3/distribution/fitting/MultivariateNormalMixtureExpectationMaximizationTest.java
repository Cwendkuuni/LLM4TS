import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.precision.DoubleArray;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructorWithValidData() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(model.getFittedModel());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyData() {
        double[][] data = {};
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithDataOfDifferentSizes() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithValidInitialMixtureAndParameters() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture, 1000, 1.0E-5);
        assertEquals(3.0, model.getLogLikelihood(), 1.0E-5);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithInitialMixtureHavingSingularCovarianceMatrices() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        initialMixture.getComponents().get(0).getSecond().setCovarianceMatrix(Array2DRowRealMatrix.identity(2));
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            model.fit(initialMixture);
            fail("Expected SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            model.fit(initialMixture, 0, 1.0E-5);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            model.fit(initialMixture, 1000, -1.0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testEstimateWithValidDataAndComponents() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MixtureMultivariateNormalDistribution expectedModel = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        expectedModel.getComponents().add(new Pair<Double, MultivariateNormalDistribution>(0.33, new MultivariateNormalDistribution(new DoubleArray(new double[]{1.5, 1.5}), Covariance.IDENTITY_2X2)));
        expectedModel.getComponents().add(new Pair<Double, MultivariateNormalDistribution>(0.33, new MultivariateNormalDistribution(new DoubleArray(new double[]{2.5, 2.5}), Covariance.IDENTITY_2X2)));
        expectedModel.getComponents().add(new Pair<Double, MultivariateNormalDistribution>(0.34, new MultivariateNormalDistribution(new DoubleArray(new double[]{3.5, 3.5}), Covariance.IDENTITY_2X2)));
        MixtureMultivariateNormalDistribution model = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
        assertEquals(expectedModel, model);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithInvalidNumberOfComponents() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        try {
            MixtureMultivariateNormalDistribution.estimate(data, 1);
            fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidNumberOfDataPoints() {
        double[][] data = {};
        try {
            MixtureMultivariateNormalDistribution.estimate(data, 1);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidNumberOfComponentsThanDataPoints() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {4.0, 5.0}};
        try {
            MixtureMultivariateNormalDistribution.estimate(data, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}