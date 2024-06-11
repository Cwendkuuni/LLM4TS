import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testInitialization() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
            Assert.assertNotNull(mnm.getFittedModel());
            Assert.assertNotNull(mnm.getLogLikelihood());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEmptyData() {
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(new double[][]{});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMismatchedData() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testTwoDimDataWithFewerThanTwoColumns() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0}};
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
            fail("Unexpected success");
        } catch (NumberIsTooSmallException e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInitialModel() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))));
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(initialMixture, 10, 1.0E-5);
        MixtureMultivariateNormalDistribution fittedModel = mnm.getFittedModel();
        double logLikelihood = mnm.getLogLikelihood();
        // Perform some checks on the fitted model
        // ...
        Assert.assertNotNull(fittedModel);
        Assert.assertNotNull(logLikelihood);
    }

    @Test
    public void testFitWithoutInitialModel() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(10, 1.0E-5);
        MixtureMultivariateNormalDistribution fittedModel = mnm.getFittedModel();
        double logLikelihood = mnm.getLogLikelihood();
        // Perform some checks on the fitted model
        // ...
        Assert.assertNotNull(fittedModel);
        Assert.assertNotNull(logLikelihood);
    }

    @Test
    public void testEstimation() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        MixtureMultivariateNormalDistribution expected = new MultivariateNormalMixtureDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.3333333333333333, 0.6666666666666667}, new Array2DRowRealMatrix(new double[][]{
                        {0.625, 0.375},
                        {0.375, 0.625}
                })),
                new MultivariateNormalDistribution(new double[]{0.6666666666666667, 0.3333333333333333}, new Array2DRowRealMatrix(new double[][]{
                        {0.375, 0.625},
                        {0.625, 0.375}
                }))));
        // Perform some checks on the estimated model
        // ...
        Assert.assertEquals(expected.getComponents(), result.getComponents());
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(10, 1.0E-5);
        double logLikelihood = mnm.getLogLikelihood();
        // Perform some checks on the log likelihood
        // ...
        Assert.assertNotNull(logLikelihood);
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(10, 1.0E-5);
        MixtureMultivariateNormalDistribution fittedModel = mnm.getFittedModel();
        // Perform some checks on the fitted model
        // ...
        Assert.assertNotNull(fittedModel);
    }

    private static class DataRow implements Comparable<DataRow> {
        private final double[] row;
        private Double mean;

        DataRow(double[] data) {
            this.row = data;
            this.mean = 0.0;
            for (int i = 0; i < data.length; ++i) {
                this.mean += data[i];
            }
            this.mean /= (Double)data.length;
        }

        @Override
        public int compareTo(DataRow other) {
            return this.mean.compareTo(other.mean);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DataRow)) return false;
            DataRow that = (DataRow) o;
            return Arrays.equals(this.row, that.row);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(this.row);
        }

        public double[] getRow() {
            return this.row;
        }
    }
}