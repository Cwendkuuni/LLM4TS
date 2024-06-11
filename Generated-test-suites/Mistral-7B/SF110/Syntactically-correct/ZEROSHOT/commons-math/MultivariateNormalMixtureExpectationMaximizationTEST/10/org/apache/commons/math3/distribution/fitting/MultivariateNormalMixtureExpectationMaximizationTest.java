import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private static final double[][] DATA_2D = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 2.5, 3.0}, {1.8, 2.2, 3.2}, {1.2, 2.8, 3.6}, {2.2, 2.7, 3.1}, {1.9, 2.3, 2.9}, {2.4, 2.9, 3.4}};
    private static final int NUM_COMPONENTS = 3;

    @Test
    public void testEstimate() {
        MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(DATA_2D, NUM_COMPONENTS);
        MixtureMultivariateNormalDistribution estimatedModel = new MultivariateNormalMixtureExpectationMaximization(DATA_2D).getFittedModel();
        Assert.assertEquals(expectedModel.getComponents().size(), estimatedModel.getComponents().size());
        for (int i = 0; i < expectedModel.getComponents().size(); ++i) {
            MultivariateNormalDistribution expectedMvn = expectedModel.getComponents().get(i).getSecond();
            MultivariateNormalDistribution estimatedMvn = estimatedModel.getComponents().get(i).getSecond();
            Assert.assertEquals(expectedMvn.getMeans().length, estimatedMvn.getMeans().length);
            for (int j = 0; j < expectedMvn.getMeans().length; ++j) {
                Assert.assertEquals(expectedMvn.getMeans()[j], estimatedMvn.getMeans()[j], 1e-5);
            }
            RealMatrix expectedCovMat = expectedMvn.getCovarianceMatrix();
            RealMatrix estimatedCovMat = estimatedMvn.getCovarianceMatrix();
            for (int k = 0; k < expectedCovMat.getColumnDimension(); ++k) {
                for (int l = 0; l < expectedCovMat.getRowDimension(); ++l) {
                    Assert.assertEquals(expectedCovMat.getEntry(k, l), estimatedCovMat.getEntry(k, l), 1e-5);
                }
            }
        }
    }

    @Test
    public void testFit() {
        double[][] data = DATA_2D;
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, NUM_COMPONENTS);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
        Assert.assertEquals(initialMixture.getComponents().size(), fittedModel.getComponents().size());
        for (int i = 0; i < initialMixture.getComponents().size(); ++i) {
            MultivariateNormalDistribution initialMvn = initialMixture.getComponents().get(i).getSecond();
            MultivariateNormalDistribution fittedMvn = fittedModel.getComponents().get(i).getSecond();
            Assert.assertEquals(initialMvn.getMeans().length, fittedMvn.getMeans().length);
            for (int j = 0; j < initialMvn.getMeans().length; ++j) {
                Assert.assertEquals(initialMvn.getMeans()[j], fittedMvn.getMeans()[j], 1e-5);
            }
            RealMatrix initialCovMat = initialMvn.getCovarianceMatrix();
            RealMatrix fittedCovMat = fittedMvn.getCovarianceMatrix();
            for (int k = 0; k < initialCovMat.getColumnDimension(); ++k) {
                for (int l = 0; l < initialCovMat.getRowDimension(); ++l) {
                    Assert.assertEquals(initialCovMat.getEntry(k, l), fittedCovMat.getEntry(k, l), 1e-5);
                }
            }
        }
        Assert.assertEquals(initialMixture.getLogProbability(new double[]{1.0, 2.0, 3.0}), model.getLogLikelihood(), 1e-5);
    }

    @Test
    public void testFitWithInitialMixture() {
        double[][] data = DATA_2D;
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, NUM_COMPONENTS);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
        Assert.assertEquals(initialMixture.getComponents().size(), fittedModel.getComponents().size());
        for (int i = 0; i < initialMixture.getComponents().size(); ++i) {
            MultivariateNormalDistribution initialMvn = initialMixture.getComponents().get(i).getSecond();
            MultivariateNormalDistribution fittedMvn = fittedModel.getComponents().get(i).getSecond();
            Assert.assertEquals(initialMvn.getMeans().length, fittedMvn.getMeans().length);
            for (int j = 0; j < initialMvn.getMeans().length; ++j) {
                Assert.assertEquals(initialMvn.getMeans()[j], fittedMvn.getMeans()[j], 1e-5);
            }
            RealMatrix initialCovMat = initialMvn.getCovarianceMatrix();
            RealMatrix fittedCovMat = fittedMvn.getCovarianceMatrix();
            for (int k = 0; k < initialCovMat.getColumnDimension(); ++k) {
                for (int l = 0; l < initialCovMat.getRowDimension(); ++l) {
                    Assert.assertEquals(initialCovMat.getEntry(k, l), fittedCovMat.getEntry(k, l), 1e-5);
                }
            }
        }
        Assert.assertEquals(initialMixture.getLogProbability(new double[]{1.0, 2.0, 3.0}), model.getLogLikelihood(), 1e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeData() {
        double[][] data = new double[][]{{-1.0, 2.0}, {1.5, -2.5}, {2.0, -2.5}, {1.8, 2.2}, {1.2, 2.8}, {2.2, 2.7}, {1.9, 2.3}, {2.4, 2.9}, {-2.4, 3.4}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDataWithMismatchSizes() {
        double[][] data = new double[][]{{1.0}, {1.5, 2.5}, {2.0, 2.5}, {1.8, 2.2, 3.2}, {1.2, 2.8, 3.6}, {2.2, 2.7, 3.1}, {1.9, 2.3, 2.9}, {2.4, 2.9, 3.4}, {1.0, 1.5, 1.2}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testDataWithLessThanTwoColumns() {
        double[][] data = new double[][]{{1.0}, {1.5}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrices() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.5, 0.0, 3.5}, {2.0, 2.5, 3.0}, {1.8, 2.2, 3.2}, {1.2, 2.8, 3.6}, {2.2, 2.7, 3.1}, {1.9, 2.3, 2.9}, {2.4, 2.9, 3.4}, {1.0, 2.0, 0.0}};
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, NUM_COMPONENTS);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture);
    }

    @Test(expected = ConvergenceException.class)
    public void testFitWithLogLikelihoodDifferenceGreaterThanThreshold() {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 2.5, 3.0}, {1.8, 2.2, 3.2}, {1.2, 2.8, 3.6}, {2.2, 2.7, 3.1}, {1.9, 2.3, 2.9}, {2.4, 2.9, 3.4}, {1.0, 2.0, 3.0}};
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, NUM_COMPONENTS);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture, 1, 1e-4);
    }

    private static class DataRow {
        private final double[] data;
        private double mean;

        DataRow(double[] data) {
            this.data = data;
            this.mean = 0.0;
            for (int i = 0; i < data.length; ++i) {
                this.mean += data[i];
            }
            this.mean /= (Double)data.length;
        }

        public double[] getData() {
            return data;
        }

        public double getMean() {
            return mean;
        }
    }

    @Test
    public void testDataRow() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        DataRow dr = new DataRow(data);
        Assert.assertArrayEquals(data, dr.getData());
        Assert.assertEquals(dr.getMean(), (data.length > 0) ? Arrays.stream(data).sum() / (Double)data.length : 0.0, 1e-5);
    }

    @Test
    public void testDataRowCompare() {
        double[] data1 = new double[]{1.0, 2.0, 3.0};
        double[] data2 = new double[]{1.5, 2.5, 3.5};
        DataRow dr1 = new DataRow(data1);
        DataRow dr2 = new DataRow(data2);
        Assert.assertTrue(dr1.compareTo(dr2) < 0);
    }

    @Test
    public void testDataRowEquals() {
        double[] data1 = new double[]{1.0, 2.0, 3.0};
        double[] data2 = new double[]{1.0, 2.0, 3.0};
        DataRow dr1 = new DataRow(data1);
        DataRow dr2 = new DataRow(data2);
        Assert.assertTrue(dr1.equals(dr2));
    }

    @Test
    public void testDataRowHashCode() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        DataRow dr = new DataRow(data);
        int hashCode = dr.hashCode();
        Random random = new Random();
        for (int i = 0; i < 1000; ++i) {
            double[] dataNew = new double[3];
            for (int j = 0; j < 3; ++j) {
                dataNew[j] = random.nextDouble();
            }
            DataRow drNew = new DataRow(dataNew);
            Assert.assertNotEquals(hashCode, drNew.hashCode());
        }
    }

    private static class Covariance {
        public RealMatrix getCovarianceMatrix() {
            double[][] data = new double[3][3];
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    double sum = 0.0;
                    for (int k = 0; k < DATA_2D.length; ++k) {
                        sum += (DATA_2D[k][i] - getMean(i)) * (DATA_2D[k][j] - getMean(j));
                    }
                    data[i][j] = sum / DATA_2D.length;
                    data[j][i] = sum / DATA_2D.length;
                }
                data[i][i] += getStandardDeviation(i) * getStandardDeviation(i);
            }
            return new Array2DRowRealMatrix(data);
        }

        private double getMean(int dimension) {
            double sum = 0.0;
            for (double[] row : DATA_2D) {
                sum += row[dimension];
            }
            return sum / DATA_2D.length;
        }

        private double getStandardDeviation(int dimension) {
            StandardDeviation stdDeviation = new StandardDeviation();
            for (double[] row : DATA_2D) {
                stdDeviation.evaluate(row[dimension]);
            }
            return stdDeviation.getValue();
        }

        private double[][] DATA_2D = DATA_2D;
    }
}