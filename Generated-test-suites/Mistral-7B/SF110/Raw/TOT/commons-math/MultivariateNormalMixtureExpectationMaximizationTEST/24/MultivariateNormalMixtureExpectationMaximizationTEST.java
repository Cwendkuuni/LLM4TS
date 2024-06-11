package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithValidDataAndInitialModel() throws Exception {
        double[][] testData = {
                {1.0, 2.0},
                {2.5, 3.5},
                {3.0, 4.0},
                {1.5, 2.5},
                {2.2, 3.2}
        };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))));
        MultivariateNormalMixtureExpectationMaximizationmodel = new MultivariateNormalMixtureExpectationMaximization(testData);
        model.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
        double logLikelihood = model.getLogLikelihood();

        // Check that the number of components matches the initial model
        Assert.assertEquals(initialMixture.getComponents().size(), fittedModel.getComponents().size());

        // Check that the means and covariance matrices match the expected values
        List<Pair<Double, MultivariateNormalDistribution>> components = fittedModel.getComponents();
        for (int i = 0; i < components.size(); i++) {
            MultivariateNormalDistribution mvn = components.get(i).getSecond();
            double[] means = mvn.getMeans();
            RealMatrix covMat = mvn.getCovarianceMatrix();

            // Check mean values
            Assert.assertArrayEquals(new double[]{1.5250236152502361, 2.5520763157894737}, means, 0.001);

            // Check covariance matrices are symmetric and positive definite
            Assert.assertTrue(covMat.isSymmetric());
            Assert.assertTrue(Covariance.isPositiveDefinite(new Covariance(new double[][]{
                    {covMat.getEntry(0, 0), covMat.getEntry(0, 1)},
                    {covMat.getEntry(1, 0), covMat.getEntry(1, 1)}
            })));
        }

        // Check that the log-likelihood is positive
        Assert.assertTrue(logLikelihood > 0);

        // Check that the fitted model can correctly compute the density for new data points
        double[] newDataPoint = {2.0, 3.0};
        double density = fittedModel.density(newDataPoint);
        Assert.assertTrue(density > 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithEmptyData() throws Exception {
        double[][] testData = {};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))));
        MultivariateNormalMixtureExpectationMaximizationmodel = new MultivariateNormalMixtureExpectationMaximization(testData);
        model.fit(initialMixture);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithDataWithInvalidColumnCount() throws Exception {
        double[][] testData = {
                {1.0, 2.0, 3.0},
                {2.5, 3.5},
                {3.0, 4.0},
                {1.5, 2.5}
        };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))));
        MultivariateNormalMixtureExpectationMaximizationmodel = new MultivariateNormalMixtureExpectationMaximization(testData);
        model.fit(initialMixture);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithDataWithSingularCovarianceMatrices() throws Exception {
        double[][] testData = {
                {1.0, 2.0},
                {2.5, 3.5},
                {3.0, 4.0},
                {1.5, 2.5},
                {2.2, 3.2}
        };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.0, 0.0},
                        {0.0, 0.0}
                }))));
        MultivariateNormalMixtureExpectationMaximizationmodel = new MultivariateNormalMixtureExpectationMaximization(testData);
        model.fit(initialMixture);
    }

    @Test
    public void testEstimateWithValidDataAndValidNumberOfComponents() throws Exception {
        double[][] testData = {
                {1.0, 2.0},
                {2.5, 3.5},
                {3.0, 4.0},
                {1.5, 2.5},
                {2.2, 3.2}
        };
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(testData, 2);

        // Check that the number of components matches the specified number
        Assert.assertEquals(2, estimatedModel.getComponents().size());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidNumberOfComponents() throws Exception {
        double[][] testData = {
                {1.0, 2.0},
                {2.5, 3.5},
                {3.0, 4.0},
                {1.5, 2.5},
                {2.2, 3.2}
        };
        MultivariateNormalMixtureExpectationMaximization.estimate(testData, -1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithDataWithInvalidRowCount() throws Exception {
        double[][] testData = {};
        MultivariateNormalMixtureExpectationMaximization.estimate(testData, 2);
    }

    @Test
    public void testEstimateWithDataWithValidNumberOfComponents() throws Exception {
        double[][] testData = {
                {1.0, 2.0},
                {2.5, 3.5},
                {3.0, 4.0},
                {1.5, 2.5},
                {2.2, 3.2}
        };
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(testData, 2);

        // Check that the estimated means and covariance matrices are close to the expected values
        List<Pair<Double, MultivariateNormalDistribution>> components = estimatedModel.getComponents();
        for (int i = 0; i < components.size(); i++) {
            MultivariateNormalDistribution mvn = components.get(i).getSecond();
            double[] means = mvn.getMeans();
            RealMatrix covMat = mvn.getCovarianceMatrix();

            // Check mean values
            StandardDeviation meanStdDev = new StandardDeviation();
            meanStdDev.evaluate(new double[]{1.5250236152502361, 2.5520763157894737});
            Assert.assertArrayEquals(new double[]{1.5, 2.5}, means, meanStdDev.getStdDev() * 1.96);

            // Check covariance matrices are symmetric and positive definite
            Assert.assertTrue(covMat.isSymmetric());
            Assert.assertTrue(Covariance.isPositiveDefinite(new Covariance(new double[][]{
                    {covMat.getEntry(0, 0), covMat.getEntry(0, 1)},
                    {covMat.getEntry(1, 0), covMat.getEntry(1, 1)}
            })));
        }
    }
}