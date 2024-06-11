import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructorWithValidData() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = null;
        try {
            mnm = new MultivariateNormalMixtureExpectationMaximization(data);
            assertNotNull(mnm);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyData() {
        double[][] data = {};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDataLength() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}, {1.0, 1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithDataRowsLessThanTwoColumns() {
        double[][] data = {{1.0, 2.0}, {1.0, 1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitWithValidInitialMixtureAndValues() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.1, 0.1},
                        {0.1, 0.1}
                }))));
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(initialMixture, 10, 0.001);
        assertNotNull(mnm.getFittedModel());
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrices() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.0, 0.0},
                        {0.0, 0.0}
                }))));
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            mnm.fit(initialMixture, 10, 0.001);
            fail("Unexpected exception: SingularMatrixException not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof SingularMatrixException);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.1, 0.1},
                        {0.1, 0.1}
                }))));
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            mnm.fit(initialMixture, -1, 0.001);
            fail("Unexpected exception: NotStrictlyPositiveException not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NotStrictlyPositiveException);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.1, 0.1},
                        {0.1, 0.1}
                }))));
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        try {
            mnm.fit(initialMixture, 10, -1.0);
            fail("Unexpected exception: NotStrictlyPositiveException not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NotStrictlyPositiveException);
        }
    }

    @Test
    public void testFitWithValidInitialMixture() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.1, 0.1},
                        {0.1, 0.1}
                }))));
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(initialMixture);
        assertNotNull(mnm.getFittedModel());
    }

    @Test
    public void testEstimateWithValidDataAndComponents() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        assertNotNull(mnm.getFittedModel());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidNumComponents() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization.estimate(data, -1);
            fail("Unexpected exception: NotStrictlyPositiveException not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NotStrictlyPositiveException);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithInvalidData() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
            fail("Unexpected exception: DimensionMismatchException not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof DimensionMismatchException);
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.1, 0.1},
                        {0.1, 0.1}
                }))));
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(initialMixture);
        double logLikelihood = mnm.getLogLikelihood();
        assertNotNull(logLikelihood);
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{0.5, 0.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.1, 0.1},
                        {0.1, 0.1}
                }))));
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = mnm.getFittedModel();
        assertNotNull(fittedModel);
    }
}