import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithInitialMixtureAndCustomParameters() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {2.0, 1.0},
                {1.5, 1.5},
                {2.5, 2.5},
                {1.8, 1.8},
                {1.2, 1.2}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }), new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                })));

        MultivariateNormalMixtureExpectationMaximization testObject = new MultivariateNormalMixtureExpectationMaximization(data);
        testObject.fit(initialMixture, 10, 1e-5);

        MixtureMultivariateNormalDistribution fittedModel = testObject.getFittedModel();
        double logLikelihood = testObject.getLogLikelihood();

        double[][] expectedMeans = {
                {1.3729129925225613, 1.3729129925225613},
                {1.6270870074774386, 1.6270870074774386}
        };

        double[][] expectedCovMats = {
                {0.3088328278086781, 0.3088328278086781},
                {0.4363284404634905, 0.4363284404634905}
        };

        List<MultivariateNormalDistribution> components = fittedModel.getComponents();
        for (int i = 0; i < components.size(); i++) {
            MultivariateNormalDistribution component = components.get(i);
            double[] componentMeans = component.getSecond().getMeans();
            RealMatrix componentCovMat = component.getSecond().getCovarianceMatrix();

            Assert.assertArrayEquals(expectedMeans[i], componentMeans, 0.001);
            Assert.assertEquals(expectedCovMats[i].length, componentCovMat.getRowDimension());
            Assert.assertEquals(expectedCovMats[i].length, componentCovMat.getColumnDimension());

            double[][] covMatData = componentCovMat.getData();
            for (int j = 0; j < expectedCovMats[i].length; j++) {
                for (int k = 0; k < expectedCovMats[i].length; k++) {
                    Assert.assertEquals(expectedCovMats[i][j][k], covMatData[j][k], 0.001);
                }
            }
        }

        Assert.assertEquals(logLikelihood, -2.912353496219346, 0.001);
    }

    @Test
    public void testFitWithInitialMixtureAndDefaultParameters() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {2.0, 1.0},
                {1.5, 1.5},
                {2.5, 2.5},
                {1.8, 1.8},
                {1.2, 1.2}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }), new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                })));

        MultivariateNormalMixtureExpectationMaximization testObject = new MultivariateNormalMixtureExpectationMaximization(data);
        testObject.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = testObject.getFittedModel();
        double logLikelihood = testObject.getLogLikelihood();

        double[][] expectedMeans = {
                {1.3729129925225613, 1.3729129925225613},
                {1.6270870074774386, 1.6270870074774386}
        };

        double[][] expectedCovMats = {
                {0.3088328278086781, 0.3088328278086781},
                {0.4363284404634905, 0.4363284404634905}
        };

        List<MultivariateNormalDistribution> components = fittedModel.getComponents();
        for (int i = 0; i < components.size(); i++) {
            MultivariateNormalDistribution component = components.get(i);
            double[] componentMeans = component.getSecond().getMeans();
            RealMatrix componentCovMat = component.getSecond().getCovarianceMatrix();

            Assert.assertArrayEquals(expectedMeans[i], componentMeans, 0.001);
            Assert.assertEquals(expectedCovMats[i].length, componentCovMat.getRowDimension());
            Assert.assertEquals(expectedCovMats[i].length, componentCovMat.getColumnDimension());

            double[][] covMatData = componentCovMat.getData();
            for (int j = 0; j < expectedCovMats[i].length; j++) {
                for (int k = 0; k < expectedCovMats[i].length; k++) {
                    Assert.assertEquals(expectedCovMats[i][j][k], covMatData[j][k], 0.001);
                }
            }
        }

        Assert.assertEquals(logLikelihood, -2.912353496219346, 0.001);
    }

    @Test
    public void testFitWithDefaultParameters() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {2.0, 1.0},
                {1.5, 1.5},
                {2.5, 2.5},
                {1.8, 1.8},
                {1.2, 1.2}
        };

        MultivariateNormalMixtureExpectationMaximization testObject = new MultivariateNormalMixtureExpectationMaximization(data);
        testObject.fit();

        MixtureMultivariateNormalDistribution fittedModel = testObject.getFittedModel();
        double logLikelihood = testObject.getLogLikelihood();

        double[][] expectedMeans = {
                {1.3729129925225613, 1.3729129925225613},
                {1.6270870074774386, 1.6270870074774386}
        };

        double[][] expectedCovMats = {
                {0.3088328278086781, 0.3088328278086781},
                {0.4363284404634905, 0.4363284404634905}
        };

        List<MultivariateNormalDistribution> components = fittedModel.getComponents();
        for (int i = 0; i < components.size(); i++) {
            MultivariateNormalDistribution component = components.get(i);
            double[] componentMeans = component.getSecond().getMeans();
            RealMatrix componentCovMat = component.getSecond().getCovarianceMatrix();

            Assert.assertArrayEquals(expectedMeans[i], componentMeans, 0.001);
            Assert.assertEquals(expectedCovMats[i].length, componentCovMat.getRowDimension());
            Assert.assertEquals(expectedCovMats[i].length, componentCovMat.getColumnDimension());

            double[][] covMatData = componentCovMat.getData();
            for (int j = 0; j < expectedCovMats[i].length; j++) {
                for (int k = 0; k < expectedCovMats[i].length; k++) {
                    Assert.assertEquals(expectedCovMats[i][j][k], covMatData[j][k], 0.001);
                }
            }
        }

        Assert.assertEquals(logLikelihood, -2.912353496219346, 0.001);
    }

    @Test
    public void testEstimate() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {2.0, 1.0},
                {1.5, 1.5},
                {2.5, 2.5},
                {1.8, 1.8},
                {1.2, 1.2}
        };

        int numComponents = 2;
        MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);

        double[][] expectedMeans = {
                {1.3729129925225613, 1.3729129925225613},
                {1.6270870074774386, 1.6270870074774386}
        };

        double[][] expectedCovMats = {
                {0.3088328278086781, 0.3088328278086781},
                {0.4363284404634905, 0.4363284404634905}
        };

        List<MultivariateNormalDistribution> components = expectedModel.getComponents();
        for (int i = 0; i < components.size(); i++) {
            MultivariateNormalDistribution component = components.get(i);
            double[] componentMeans = component.getSecond().getMeans();
            RealMatrix componentCovMat = component.getSecond().getCovarianceMatrix();

            Assert.assertArrayEquals(expectedMeans[i], componentMeans, 0.001);
            Assert.assertEquals(expectedCovMats[i].length, componentCovMat.getRowDimension());
            Assert.assertEquals(expectedCovMats[i].length, componentCovMat.getColumnDimension());

            double[][] covMatData = componentCovMat.getData();
            for (int j = 0; j < expectedCovMats[i].length; j++) {
                for (int k = 0; k < expectedCovMats[i].length; k++) {
                    Assert.assertEquals(expectedCovMats[i][j][k], covMatData[j][k], 0.001);
                }
            }
        }
    }
}