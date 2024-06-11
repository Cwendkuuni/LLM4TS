import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithInitialMixtureAndValidParameters() {
        double[][] data = { { 1.0, 2.0 }, { 1.5, 2.5 }, { 2.0, 3.0 }, { 2.5, 3.5 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{ 1.0, 1.0 }, new Array2DRowRealMatrix(new double[][]{{ 0.1, 0.1 }, { 0.1, 0.1 }})),
                new MultivariateNormalDistribution(new double[]{ 2.0, 2.0 }, new Array2DRowRealMatrix(new double[][]{{ 0.2, 0.2 }, { 0.2, 0.2 }}))
        ));

        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmme.fit(initialMixture, 100, 0.001);

        MixtureMultivariateNormalDistribution fittedModel = mmme.getFittedModel();
        double logLikelihood = mmme.getLogLikelihood();

        Assert.assertEquals(3, fittedModel.getComponents().size());
        for (MultivariateNormalDistribution component : fittedModel.getComponents()) {
            Assert.assertNotNull(component.getMeans());
            Assert.assertNotNull(component.getCovarianceMatrix());
        }
        Assert.assertFalse(Double.isNaN(logLikelihood));
        Assert.assertTrue(logLikelihood > 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithEmptyData() {
        double[][] data = {};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.emptyList());

        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmme.fit(initialMixture);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithDataWithDifferentNumberOfColumns() {
        double[][] data = { { 1.0, 2.0, 3.0 }, { 1.5, 2.5 }, { 2.0, 3.0, 4.0 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.emptyList());

        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmme.fit(initialMixture, 100, 0.001);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrix() {
        double[][] data = { { 1.0, 0.0 }, { 0.0, 1.0 }, { 0.0, 0.0 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.emptyList());

        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmme.fit(initialMixture, 100, 0.001);
    }

    @Test
    public void testEstimateWithValidDataAndNumberOfComponents() {
        double[][] data = { { 1.0, 2.0 }, { 1.5, 2.5 }, { 2.0, 3.0 }, { 2.5, 3.5 } };
        int numComponents = 2;

        MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);

        MixtureMultivariateNormalDistribution actualModel = new MultivariateNormalMixtureExpectationMaximization(data).getFittedModel();

        Assert.assertEquals(expectedModel.getComponents().size(), actualModel.getComponents().size());
        for (int i = 0; i < expectedModel.getComponents().size(); ++i) {
            MultivariateNormalDistribution expectedComponent = expectedModel.getComponents().get(i);
            MultivariateNormalDistribution actualComponent = actualModel.getComponents().get(i);
            Assert.assertArrayEquals(expectedComponent.getMeans(), actualComponent.getMeans());
            RealMatrix expectedCovarianceMatrix = expectedComponent.getCovarianceMatrix();
            RealMatrix actualCovarianceMatrix = actualComponent.getCovarianceMatrix();
            Assert.assertTrue(Arrays.deepEquals(expectedCovarianceMatrix.getData(), actualCovarianceMatrix.getData()));
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithDataWithDifferentNumberOfRowsOrColumns() {
        double[][] data = { { 1.0, 2.0 }, { 1.5, 2.5 }, { 2.0, 3.0 }, { 2.5, 3.5 }, { 3.0, 4.0 } };
        int numComponents = 2;

        MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidNumberOfComponents() {
        double[][] data = { { 1.0, 2.0 }, { 1.5, 2.5 }, { 2.0, 3.0 }, { 2.5, 3.5 } };
        int numComponents = 0;

        MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }

    @Test
    public void testGetLogLikelihoodWithFittedModel() {
        double[][] data = { { 1.0, 2.0 }, { 1.5, 2.5 }, { 2.0, 3.0 }, { 2.5, 3.5 } };
        MixtureMultivariateNormalDistribution fittedModel = new MultivariateNormalMixtureExpectationMaximization(data).getFittedModel();

        double logLikelihood = new MultivariateNormalMixtureExpectationMaximization(data).getLogLikelihood();

        Assert.assertEquals(fittedModel.getLogLikelihood(), logLikelihood, 0.0);
    }
}