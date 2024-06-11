package org.apache.commons.math3.distribution.fitting.test;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithValidInput() {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.2},
                {2.5, 2.5},
                {2.0, 2.8},
                {1.8, 2.9}
        };
        MixtureMultivariateNormalDistribution expectedModel = estimate(data, 2);
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit();
        MixtureMultivariateNormalDistribution actualModel = mnme.getFittedModel();
        Assert.assertEquals(expectedModel.getComponents().size(), actualModel.getComponents().size());
        for (int i = 0; i < expectedModel.getComponents().size(); ++i) {
            MultivariateNormalDistribution expectedComponent = expectedModel.getComponents().get(i).getSecond();
            MultivariateNormalDistribution actualComponent = actualModel.getComponents().get(i).getSecond();
            Assert.assertArrayEquals(expectedComponent.getMeans(), actualComponent.getMeans());
            RealMatrix expectedCovMat = expectedComponent.getCovarianceMatrix().getData();
            RealMatrix actualCovMat = actualComponent.getCovarianceMatrix().getData();
            Assert.assertArrayEquals(expectedCovMat, actualCovMat);
            Assert.assertEquals(expectedComponent.getProbability(), actualComponent.getProbability(), 1e-10);
        }
        Assert.assertEquals(expectedModel.getLogLikelihood(), mnme.getLogLikelihood(), 1e-10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyData() {
        double[][] data = {};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithDataOfInvalidLength() {
        double[][] data = {{1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithDataOfInvalidDimension() {
        double[][] data = {{1.0, 2.0}, {1.5, 3.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitWithInvalidInitialMixture() {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.2},
                {2.5, 2.5},
                {2.0, 2.8},
                {1.8, 2.9}
        };
        MixtureMultivariateNormalDistribution invalidInitialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new Pair<Double, MultivariateNormalDistribution>(0.0, new MultivariateNormalDistribution(new double[0][0]))));
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            mnme.fit(invalidInitialMixture);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithValidInitialMixtureAndDefaultParameters() {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.2},
                {2.5, 2.5},
                {2.0, 2.8},
                {1.8, 2.9}
        };
        MixtureMultivariateNormalDistribution initialMixture = estimate(data, 2);
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit(initialMixture);
        MixtureMultivariateNormalDistribution actualModel = mnme.getFittedModel();
        MixtureMultivariateNormalDistribution expectedModel = estimate(data, 2);
        Assert.assertEquals(expectedModel.getComponents().size(), actualModel.getComponents().size());
        for (int i = 0; i < expectedModel.getComponents().size(); ++i) {
            MultivariateNormalDistribution expectedComponent = expectedModel.getComponents().get(i).getSecond();
            MultivariateNormalDistribution actualComponent = actualModel.getComponents().get(i).getSecond();
            Assert.assertArrayEquals(expectedComponent.getMeans(), actualComponent.getMeans());
            RealMatrix expectedCovMat = expectedComponent.getCovarianceMatrix().getData();
            RealMatrix actualCovMat = actualComponent.getCovarianceMatrix().getData();
            Assert.assertArrayEquals(expectedCovMat, actualCovMat);
            Assert.assertEquals(expectedComponent.getProbability(), actualComponent.getProbability(), 1e-10);
        }
        Assert.assertEquals(expectedModel.getLogLikelihood(), mnme.getLogLikelihood(), 1e-10);
    }

    @Test
    public void testFitWithValidInitialMixtureAndCustomParameters() {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.2},
                {2.5, 2.5},
                {2.0, 2.8},
                {1.8, 2.9}
        };
        MixtureMultivariateNormalDistribution initialMixture = estimate(data, 2);
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit(initialMixture, 500, 1e-7);
        MixtureMultivariateNormalDistribution actualModel = mnme.getFittedModel();
        MixtureMultivariateNormalDistribution expectedModel = estimate(data, 2);
        Assert.assertEquals(expectedModel.getComponents().size(), actualModel.getComponents().size());
        for (int i = 0; i < expectedModel.getComponents().size(); ++i) {
            MultivariateNormalDistribution expectedComponent = expectedModel.getComponents().get(i).getSecond();
            MultivariateNormalDistribution actualComponent = actualModel.getComponents().get(i).getSecond();
            Assert.assertArrayEquals(expectedComponent.getMeans(), actualComponent.getMeans());
            RealMatrix expectedCovMat = expectedComponent.getCovarianceMatrix().getData();
            RealMatrix actualCovMat = actualComponent.getCovarianceMatrix().getData();
            Assert.assertArrayEquals(expectedCovMat, actualCovMat);
            Assert.assertEquals(expectedComponent.getProbability(), actualComponent.getProbability(), 1e-10);
        }
        Assert.assertEquals(expectedModel.getLogLikelihood(), mnme.getLogLikelihood(), 1e-10);
    }

    private static MixtureMultivariateNormalDistribution estimate(double[][] data, int numComponents) {
        return new MultivariateNormalMixtureExpectationMaximization().estimate(data, numComponents);
    }

}