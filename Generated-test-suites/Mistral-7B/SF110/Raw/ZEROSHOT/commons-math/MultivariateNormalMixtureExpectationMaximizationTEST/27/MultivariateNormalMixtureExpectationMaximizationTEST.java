package org.apache.commons.math3.distribution.fitting.test;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private static final double[][] TEST_DATA = {
            {1.0, 2.0},
            {1.5, 2.5},
            {2.0, 3.0},
            {2.5, 3.5},
            {3.0, 4.0}
    };

    @Test
    public void testEstimate() throws Exception {
        MixtureMultivariateNormalDistribution expectedModel = new MixtureMultivariateNormalDistribution(2);
        MixtureMultivariateNormalDistribution model = MultivariateNormalMixtureExpectationMaximization.estimate(TEST_DATA, 2);
        Assert.assertEquals(expectedModel, model);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidNumberOfDataPoints() throws Exception {
        new MultivariateNormalMixtureExpectationMaximization(new double[][] {});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithDataPointWithInvalidNumberOfColumns() throws Exception {
        new MultivariateNormalMixtureExpectationMaximization(new double[][] {
                {1.0, 2.0},
                {1.5, 2.5, 3.0}
        });
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithDataPointWithInvalidNumberOfDimensions() throws Exception {
        new MultivariateNormalMixtureExpectationMaximization(new double[][] {
                {1.0, 2.0},
                {1.5, 2.5},
                {1.0}
        });
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidNumberOfIterations() throws Exception {
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(TEST_DATA);
        model.fit(null, -1, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() throws Exception {
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(TEST_DATA);
        model.fit(null, 1000, -1.0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithInitialModelWithInvalidNumberOfComponents() throws Exception {
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(TEST_DATA);
        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(1);
        model.fit(initialModel, 1000, 1.0E-5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithInitialModelWithInvalidNumberOfMeans() throws Exception {
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(TEST_DATA);
        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(2, new MultivariateNormalDistribution[2]);
        model.fit(initialModel, 1000, 1.0E-5);
    }

    @Test
    public void testFit() throws Exception {
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(TEST_DATA);
        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(2);
        model.fit(initialModel, 1000, 1.0E-5);
        MixtureMultivariateNormalDistribution expectedModel = new MixtureMultivariateNormalDistribution(2);
        expectedModel.getComponents().get(0).getSecond().getMeans()[0] = new double[] {1.527418432623238, 2.537245432332758};
        expectedModel.getComponents().get(0).getSecond().getMeans()[1] = new double[] {1.534790544518253, 2.607550825787403};
        expectedModel.getComponents().get(1).getSecond().getMeans()[0] = new double[] {3.462581567376762, 3.441854567667148};
        expectedModel.getComponents().get(1).getSecond().getMeans()[1] = new double[] {3.466309455481747, 3.582449544518253};
        RealMatrix expectedCovMats = new Array2DRowRealMatrix(new double[][] {
                {0.431524273890095, -0.056583619188688},
                {-0.056583619188688, 0.337153419873729}
        });
        for (int i = 0; i < expectedModel.getComponents().size(); ++i) {
            MultivariateNormalDistribution mvn = expectedModel.getComponents().get(i).getSecond();
            MixtureMultivariateNormalDistribution component = model.getFittedModel().getComponents().get(i);
            MultivariateNormalDistribution componentMvn = component.getSecond();
            Assert.assertArrayEquals(mvn.getMeans(), componentMvn.getMeans(), 0.001);
            Assert.assertEquals(Precision.compareDoubles(mvn.getCovarianceMatrix().determinant(), componentMvn.getCovarianceMatrix().determinant(), 0.001));
            Assert.assertEquals(Precision.compareDoubles(Precision.norm(Covariance.cov(TEST_DATA).subtract(componentMvn.getCovarianceMatrix())), 0.0, 0.001));
        }
        Assert.assertEquals(expectedModel.getLogProbabilityDensity(TEST_DATA[0]), model.getLogLikelihood(), 0.001);
        Assert.assertEquals(expectedModel.getLogProbabilityDensity(TEST_DATA[1]), model.getLogLikelihood(), 0.001);
        Assert.assertEquals(expectedModel.getLogProbabilityDensity(TEST_DATA[2]), model.getLogLikelihood(), 0.001);
        Assert.assertEquals(expectedModel.getLogProbabilityDensity(TEST_DATA[3]), model.getLogLikelihood(), 0.001);
        Assert.assertEquals(expectedModel.getLogProbabilityDensity(TEST_DATA[4]), model.getLogLikelihood(), 0.001);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrices() throws Exception {
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(TEST_DATA);
        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(2);
        model.fit(initialModel, 1000, 1.0E-5);
        double[][] testData2 = new double[][] {
                {1.5, 2.5},
                {1.0, 1.0},
                {2.5, 3.5},
                {3.0, 4.0}
        };
        model.density(testData2);
    }

    @Test(expected = ConvergenceException.class)
    public void testFitWithNonConvergentModel() throws Exception {
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(TEST_DATA);
        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(2);
        model.fit(initialModel, 50, 1.0E-5);
    }

}