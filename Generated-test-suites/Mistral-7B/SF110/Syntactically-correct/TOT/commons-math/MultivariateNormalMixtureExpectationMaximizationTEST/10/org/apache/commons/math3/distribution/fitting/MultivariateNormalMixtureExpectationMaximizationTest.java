import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.FittingResult;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithPositiveDataAndDefaults() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}, {4.0, 4.0}, {5.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmme.fit();
        MixtureMultivariateNormalDistribution fittedModel = mmme.getFittedModel();
        FittingResult result = fittedModel.getFittingResult();
        assertEquals(5.0, result.getNumberOfDataPoints(), 0.0);
        assertEquals(2.0, result.getNumberOfComponents(), 0.0);
        assertEquals(1000.0, result.getIterations(), 0.0);
        assertEquals(1.0E-5, result.getThreshold(), 0.0);
        MultivariateNormalDistribution[] components = fittedModel.getComponents().toArray(new MultivariateNormalDistribution[0]);
        for (MultivariateNormalDistribution component : components) {
            assertNotNull(component.getMeans());
            assertNotNull(component.getCovarianceMatrix());
            assertFalse(component.getCovarianceMatrix().isIdentityMatrix());
            double[] means = component.getMeans();
            double[] covarianceMatrix = component.getCovarianceMatrix().getData();
            assertArrayEquals(means.length, covarianceMatrix.length, "Component has incorrect number of dimensions");
            for (int i = 0; i < means.length; ++i) {
                assertTrue(means[i] > 0.0);
            }
            for (int i = 0; i < means.length; ++i) {
                for (int j = 0; j < means.length; ++j) {
                    assertTrue(covarianceMatrix[i * means.length + j] > 0.0);
                }
            }
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithEmptyData() throws NotStrictlyPositiveException {
        double[][] data = new double[0][];
        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmme.fit();
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithDataOfIncorrectDimension() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmme.fit();
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFitWithDataOfInsufficientSize() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}, {4.0, 4.0}, {5.0, 5.0}, {6.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmme.fit(new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>()), 0, 0.0);
    }

    @Test
    public void testFitWithPositiveDataAndCustomParameters() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}, {4.0, 4.0}, {5.0, 5.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmme.fit(initialMixture, 500, 1.0E-8);
        MixtureMultivariateNormalDistribution fittedModel = mmme.getFittedModel();
        FittingResult result = fittedModel.getFittingResult();
        assertEquals(5.0, result.getNumberOfDataPoints(), 0.0);
        assertEquals(2.0, result.getNumberOfComponents(), 0.0);
        assertEquals(500.0, result.getIterations(), 0.0);
        assertEquals(1.0E-8, result.getThreshold(), 0.0);
    }

    @Test
    public void testEstimateWithPositiveDataAndDefaults() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}, {4.0, 4.0}, {5.0, 5.0}};
        MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        MixtureMultivariateNormalDistribution actualModel = new MultivariateNormalMixtureExpectationMaximization().estimate(data, 2);
        assertEquals(expectedModel, actualModel);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithNegativeData() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{-1.0, -2.0}, {-2.0, -1.0}, {-3.0, -3.0}, {-4.0, -4.0}, {-5.0, -5.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithDataOfIncorrectDimension() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithDataOfInsufficientSize() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}, {4.0, 4.0}, {5.0, 5.0}, {6.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }
}