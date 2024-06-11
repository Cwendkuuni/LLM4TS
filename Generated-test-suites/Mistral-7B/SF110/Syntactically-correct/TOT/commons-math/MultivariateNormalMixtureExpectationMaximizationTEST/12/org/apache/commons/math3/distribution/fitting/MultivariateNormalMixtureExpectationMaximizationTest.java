package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithEmptyData() {
        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][]{});
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInvalidData() {
        double[][] data = new double[][]{
                new double[]{1.0, 2.0},
                new double[]{3.0, 4.0, 5.0},
                new double[]{1.0, 2.0, 4.0}
        };

        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInvalidInitialMixture() {
        double[][] data = new double[][]{
                new double[]{1.0, 2.0},
                new double[]{3.0, 4.0},
                new double[]{5.0, 6.0}
        };

        List<Pair<Double, MultivariateNormalDistribution>> initialMixture = new ArrayList<>();
        initialMixture.add(new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(2, 2))));
        initialMixture.add(new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(2, 2))));

        try {
            new MultivariateNormalMixtureExpectationMaximization(data).fit(new MixtureMultivariateNormalDistribution(initialMixture), 1000, 1.0E-5);
            Assert.fail("Expected SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithValidData() {
        double[][] data = new double[][]{
                new double[]{1.0, 2.0},
                new double[]{2.5, 3.5},
                new double[]{1.5, 2.5},
                new double[]{3.0, 3.5},
                new double[]{2.0, 2.5}
        };

        List<Pair<Double, MultivariateNormalDistribution>> initialMixture = new ArrayList<>();
        initialMixture.add(new Pair<>(0.3, new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(2, 2))));
        initialMixture.add(new Pair<>(0.7, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(2, 2))));

        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit(initialMixture);

        double expectedLogLikelihood = -1.3431324629828755;
        double actualLogLikelihood = mnme.getLogLikelihood();
        Assert.assertEquals(expectedLogLikelihood, actualLogLikelihood, 1e-5);

        MixtureMultivariateNormalDistribution fittedModel = mnme.getFittedModel();
        double[][] expectedMeans = new double[][]{
                new double[]{0.7132391274347257, 1.800623866433201},
                new double[]{1.959662186386626, 2.8000939126515987}
        };
        RealMatrix expectedCovMats = new Array2DRowRealMatrix(2, 2).setEntry(0, 0, 1.862131064490547);
        expectedCovMats.setEntry(0, 1, 0.5339788538423887);
        expectedCovMats.setEntry(1, 0, 0.5339788538423887);
        expectedCovMats.setEntry(1, 1, 1.721132714742528);

        for (int i = 0; i < 2; ++i) {
            MultivariateNormalDistribution component = fittedModel.getComponents().get(i).getSecond();
            Assert.assertArrayEquals(expectedMeans[i], component.getMean(), 1e-5);
            RealMatrix covMat = component.getCovarianceMatrix().getData();
            Assert.assertEquals(expectedCovMats.getEntry(0, 0), covMat.getEntry(0, 0), 1e-5);
            Assert.assertEquals(expectedCovMats.getEntry(0, 1), covMat.getEntry(0, 1), 1e-5);
            Assert.assertEquals(expectedCovMats.getEntry(1, 0), covMat.getEntry(1, 0), 1e-5);
            Assert.assertEquals(expectedCovMats.getEntry(1, 1), covMat.getEntry(1, 1), 1e-5);
        }
    }

    @Test
    public void testEstimateWithValidData() {
        double[][] data = new double[][]{
                new double[]{1.0, 2.0},
                new double[]{2.5, 3.5},
                new double[]{1.5, 2.5},
                new double[]{3.0, 3.5},
                new double[]{2.0, 2.5}
        };

        MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        double[][] expectedMeans = new double[][]{
                new double[]{0.7252036263615071, 1.784537952873946},
                new double[]{2.2283362201881806, 2.7642838781839635}
        };
        RealMatrix expectedCovMats = new Array2DRowRealMatrix(2, 2).setEntry(0, 0, 1.824226555096697);
        expectedCovMats.setEntry(0, 1, 0.5767951328803034);
        expectedCovMats.setEntry(1, 0, 0.5767951328803034);
        expectedCovMats.setEntry(1, 1, 1.711637675931108);

        for (int i = 0; i < 2; ++i) {
            MultivariateNormalDistribution component = expectedModel.getComponents().get(i).getSecond();
            Assert.assertArrayEquals(expectedMeans[i], component.getMean(), 1e-5);
            RealMatrix covMat = component.getCovarianceMatrix().getData();
            Assert.assertEquals(expectedCovMats.getEntry(0, 0), covMat.getEntry(0, 0), 1e-5);
            Assert.assertEquals(expectedCovMats.getEntry(0, 1), covMat.getEntry(0, 1), 1e-5);
            Assert.assertEquals(expectedCovMats.getEntry(1, 0), covMat.getEntry(1, 0), 1e-5);
            Assert.assertEquals(expectedCovMats.getEntry(1, 1), covMat.getEntry(1, 1), 1e-5);
        }
    }
}