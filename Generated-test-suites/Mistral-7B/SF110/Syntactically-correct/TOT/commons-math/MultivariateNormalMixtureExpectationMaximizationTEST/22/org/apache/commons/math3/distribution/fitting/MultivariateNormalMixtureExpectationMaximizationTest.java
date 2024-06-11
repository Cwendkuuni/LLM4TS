package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithValidData() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {1.5, 1.5}, {2.5, 2.5}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(
                Arrays.asList(new Pair<>(0.3, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new RealMatrix(new double[][]{
                        {1.0, 0.0}, {0.0, 1.0}}))),
                        new Pair<>(0.7, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new RealMatrix(new double[][]{
                                {2.0, 2.0}, {2.0, 2.0}})))));
        try {
            MultivariateNormalMixtureExpectationMaximization testObject = new MultivariateNormalMixtureExpectationMaximization(data);
            testObject.fit(initialMixture);
            MixtureMultivariateNormalDistribution fittedModel = testObject.getFittedModel();
            double logLikelihood = testObject.getLogLikelihood();

            // Check number of components
            Assert.assertEquals(initialMixture.getComponents().size(), fittedModel.getComponents().size());

            // Check component weights
            List<Pair<Double, MultivariateNormalDistribution>> initialComponents = initialMixture.getComponents();
            List<Pair<Double, MultivariateNormalDistribution>> fittedComponents = fittedModel.getComponents();
            for (int i = 0; i < initialComponents.size(); ++i) {
                Assert.assertEquals(initialComponents.get(i).getFirst(), fittedComponents.get(i).getFirst(), 0.001);
            }

            // Check component means
            for (int i = 0; i < initialComponents.size(); ++i) {
                MultivariateNormalDistribution initialMean = initialComponents.get(i).getSecond();
                MultivariateNormalDistribution fittedMean = fittedComponents.get(i).getSecond();
                RealMatrix initialCovMatrix = initialMean.getCovarianceMatrix();
                RealMatrix fittedCovMatrix = fittedMean.getCovarianceMatrix();
                double[] initialMeanValues = initialMean.getMean();
                double[] fittedMeanValues = fittedMean.getMean();

                // Check means
                Assert.assertArrayEquals(initialMeanValues, fittedMeanValues, 0.001);

                // Check covariance matrices
                RealMatrix initialCovMatrixDiagonal = RealMatrixUtils.getIdentityMatrix(initialCovMatrix.getRowDimension());
                RealMatrix fittedCovMatrixDiagonal = RealMatrixUtils.getIdentityMatrix(fittedCovMatrix.getRowDimension());
                RealMatrix initialCovMatrixDifference = RealMatrixUtils.subtract(initialCovMatrix, initialCovMatrixDiagonal);
                RealMatrix fittedCovMatrixDifference = RealMatrixUtils.subtract(fittedCovMatrix, fittedCovMatrixDiagonal);
                Assert.assertTrue(Covariance.isIdentity(initialCovMatrixDifference, 0.001));
                Assert.assertTrue(Covariance.isIdentity(fittedCovMatrixDifference, 0.001));
            }

            // Check log likelihood
            double expectedLogLikelihood = 0.0;
            for (int i = 0; i < data.length; ++i) {
                double density = 0.0;
                for (Pair<Double, MultivariateNormalDistribution> component : initialMixture.getComponents()) {
                    MultivariateNormalDistribution mvn = component.getSecond();
                    double weight = component.getFirst();
                    double rowDensity = mvn.density(data[i]);
                    density += weight * rowDensity;
                }
                expectedLogLikelihood += Math.log(density);
            }
            expectedLogLikelihood /= data.length;
            Assert.assertEquals(expectedLogLikelihood, logLikelihood, 0.001);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | SingularMatrixException e) {
            Assert.fail("Exception should not be thrown when input is valid: " + e.getMessage());
        }
    }

    @Test
    public void testFitWithInvalidDataThrowsException() {
        double[][] data = {};
        try {
            MultivariateNormalMixtureExpectationMaximization testObject = new MultivariateNormalMixtureExpectationMaximization(data);
            testObject.fit(new MixtureMultivariateNormalDistribution(Collections.emptyList()));
            Assert.fail("Should have thrown exception");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        } catch (Exception e) {
            Assert.assertEquals("The number of rows in the data matrix must be greater than zero", e.getMessage());
        }
    }

    @Test
    public void testEstimateWithValidData() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {1.5, 1.5}, {2.5, 2.5}};
        try {
            MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            double logLikelihood = expectedModel.logProbability(data);

            // Check number of components
            Assert.assertEquals(2, expectedModel.getComponents().size());

            // Check component weights and means
            for (Pair<Double, MultivariateNormalDistribution> component : expectedModel.getComponents()) {
                double weight = component.getFirst();
                MultivariateNormalDistribution mvn = component.getSecond();
                double[] means = mvn.getMean();
                RealMatrix covMatrix = mvn.getCovarianceMatrix();

                // Check means
                Assert.assertArrayEquals(new double[]{1.5, 1.5}, means);

                // Check covariance matrices
                RealMatrix covMatrixDiagonal = RealMatrixUtils.getIdentityMatrix(covMatrix.getRowDimension());
                RealMatrix covMatrixDifference = RealMatrixUtils.subtract(covMatrix, covMatrixDiagonal);
                Assert.assertTrue(Covariance.isIdentity(covMatrixDifference, 0.001));
            }

            // Check log likelihood
            double expectedLogLikelihood = 0.0;
            for (int i = 0; i < data.length; ++i) {
                double density = 0.0;
                for (Pair<Double, MultivariateNormalDistribution> component : expectedModel.getComponents()) {
                    MultivariateNormalDistribution mvn = component.getSecond();
                    double weight = component.getFirst();
                    double rowDensity = mvn.density(data[i]);
                    density += weight * rowDensity;
                }
                expectedLogLikelihood += Math.log(density);
            }
            expectedLogLikelihood /= data.length;
            Assert.assertEquals(expectedLogLikelihood, logLikelihood, 0.001);
        } catch (Exception e) {
            Assert.fail("Exception should not be thrown when input is valid: " + e.getMessage());
        }
    }

    @Test
    public void testEstimateWithInvalidNumberOfComponentsThrowsException() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {1.5, 1.5}, {2.5, 2.5}};
        try {
            MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 0);
            Assert.fail("Should have thrown exception");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        } catch (Exception e) {
            Assert.assertEquals("The number of components must be greater than zero", e.getMessage());
        }
    }

    @Test
    public void testEstimateWithInvalidDataThrowsException() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {1.5, 1.5}, {2.5, 2.5}, {3.0, 4.0}};
        try {
            MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            Assert.fail("Should have thrown exception");
        } catch (DimensionMismatchException e) {
            // Pass
        } catch (Exception e) {
            Assert.assertEquals("All rows in the data matrix must have the same number of columns", e.getMessage());
        }
    }
}