import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testInitialization() {
        try {
            double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
            MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
            Assert.assertNotNull(mmeme.getFittedModel());
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Initialization should not throw an exception");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEmptyDataInitialization() {
        double[][] data = new double[0][];
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testNotSquareDataInitialization() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testDataWithSinglePoint() {
        double[][] data = new double[][]{{1.0, 2.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitWithInitialMixtureAndSpecifiedIterationsAndThreshold() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {1.5, 1.5}, {1.5, 1.5}, {1.5, 1.5}};
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(initialMixture, 100, 1.0E-6);
        MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
        double logLikelihood = mmeme.getLogLikelihood();
        Assert.assertEquals(3, fittedModel.getComponents().size());
        Assert.assertNotNull(fittedModel.getComponents().get(0).getFirst());
        Assert.assertNotNull(fittedModel.getComponents().get(0).getSecond());
        Assert.assertEquals(2, fittedModel.getComponents().get(0).getSecond().getMeans().length);
        Assert.assertTrue(logLikelihood > 0.0);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovariance() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {1.5, 1.5}, {1.5, 1.5}, {1.5, 1.5}, {0.0, 0.0}, {0.0, 0.0}};
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(initialMixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeMaxIterations() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(new MixtureMultivariateNormalDistribution(Collections.singletonList(new Pair<Double, MultivariateNormalDistribution>(1.0, new MultivariateNormalDistribution(new double[]{}, new Covariance(new double[0][0]).getCovarianceMatrix().getData())))), -1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeThreshold() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(new MixtureMultivariateNormalDistribution(Collections.singletonList(new Pair<Double, MultivariateNormalDistribution>(1.0, new MultivariateNormalDistribution(new double[]{}, new Covariance(new double[0][0]).getCovarianceMatrix().getData())))), 100, -1.0);
    }

    @Test
    public void testFitWithInitialMixture() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {1.5, 1.5}, {1.5, 1.5}, {1.5, 1.5}};
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmeme.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
        double logLikelihood = mmeme.getLogLikelihood();
        Assert.assertEquals(3, fittedModel.getComponents().size());
        Assert.assertNotNull(fittedModel.getComponents().get(0).getFirst());
        Assert.assertNotNull(fittedModel.getComponents().get(0).getSecond());
        Assert.assertEquals(2, fittedModel.getComponents().get(0).getSecond().getMeans().length);
        Assert.assertTrue(logLikelihood > 0.0);
    }

    @Test
    public void testEstimation() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {1.5, 1.5}, {1.5, 1.5}, {1.5, 1.5}};
        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
        Assert.assertEquals(3, fittedModel.getComponents().size());
        Assert.assertNotNull(fittedModel.getComponents().get(0).getFirst());
        Assert.assertNotNull(fittedModel.getComponents().get(0).getSecond());
        Assert.assertEquals(2, fittedModel.getComponents().get(0).getSecond().getMeans().length);
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {1.5, 1.5}, {1.5, 1.5}, {1.5, 1.5}};
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        double logLikelihood = mmeme.getLogLikelihood();
        Assert.assertTrue(logLikelihood > 0.0);
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {1.5, 1.5}, {1.5, 1.5}, {1.5, 1.5}};
        MultivariateNormalMixtureExpectationMaximization mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
        Assert.assertEquals(3, fittedModel.getComponents().size());
        Assert.assertNotNull(fittedModel.getComponents().get(0).getFirst());
        Assert.assertNotNull(fittedModel.getComponents().get(0).getSecond());
        Assert.assertEquals(2, fittedModel.getComponents().get(0).getSecond().getMeans().length);
    }
}

class DataRow implements Comparable<DataRow>, Cloneable {
    private final double[] row;
    private Double mean;

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

    @Override
    public String toString() {
        return "DataRow{" +
                "row=" + Arrays.toString(this.row) +
                '}';
    }

    public DataRow(double[] data) {
        this.row = data;
        this.mean = 0.0;
        for (int i = 0; i < data.length; ++i) {
            this.mean += data[i];
        }
        this.mean /= (Double)data.length;
    }

    public double[] getRow() {
        return this.row.clone();
    }
}