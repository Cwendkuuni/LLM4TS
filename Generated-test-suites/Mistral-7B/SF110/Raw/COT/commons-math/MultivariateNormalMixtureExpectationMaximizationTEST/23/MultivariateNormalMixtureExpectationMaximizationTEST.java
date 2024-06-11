import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        assertNotNull(mnmem);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorDataIsNull() {
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(null);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorEmptyData() {
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(new double[0][]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDataContainsRowWithInvalidLength() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, 1, 1.0E-5);
        assertEquals(3, mnmem.getFittedModel().getComponents().size());
        assertEquals(0.3333333333333333, mnmem.getFittedModel().getComponents().get(0).getFirst(), 0.000001);
        assertEquals(0.3333333333333333, mnmem.getFittedModel().getComponents().get(1).getFirst(), 0.000001);
        assertEquals(0.3333333333333333, mnmem.getFittedModel().getComponents().get(2).getFirst(), 0.000001);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrix() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0, 0.0}, {3.0, 4.0, 0.0}, {5.0, 6.0, 0.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, -1, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture, 1000, -1.0);
    }

    @Test
    public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
        assertEquals(3, result.getComponents().size());
        assertEquals(0.3333333333333333, result.getComponents().get(0).getFirst(), 0.000001);
        assertEquals(0.3333333333333333, result.getComponents().get(1).getFirst(), 0.000001);
        assertEquals(0.3333333333333333, result.getComponents().get(2).getFirst(), 0.000001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidNumberOfComponents() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 0);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithDataOfInvalidShape() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
    }
}