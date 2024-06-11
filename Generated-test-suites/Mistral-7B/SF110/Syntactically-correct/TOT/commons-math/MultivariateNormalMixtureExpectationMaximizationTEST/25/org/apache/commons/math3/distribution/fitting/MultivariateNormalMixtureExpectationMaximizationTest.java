import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution.Component;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFactory;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private static final double[][] TEST_DATA = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 3.0, 4.0}, {1.2, 1.8, 2.5}, {1.7, 2.2, 3.1}};

    @Test
    public void testFitWithValidInitialModelAndData() throws Exception {
        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(Collections.singletonList(new Component(1.0,
                new MultivariateNormalDistribution(new double[]{0.0, 0.0, 0.0}, RealMatrixFactory.IDENTITY_2D))));
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(TEST_DATA);
        model.fit(initialModel);

        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
        assertEquals(5, fittedModel.getComponents().size());
        for (Component component : fittedModel.getComponents()) {
            assertTrue(component.getSecond().getProbability() > 0.0);
            MultivariateNormalDistribution distribution = component.getSecond();
            double[] means = distribution.getMeans();
            RealMatrix covarianceMatrix = distribution.getCovarianceMatrix();
            assertEquals(TEST_DATA[0].length, means.length);
            assertEquals(TEST_DATA[0].length, covarianceMatrix.getRowDimension());
            assertEquals(TEST_DATA[0].length, covarianceMatrix.getColumnDimension());
            for (int i = 0; i < TEST_DATA[0].length; ++i) {
                assertEquals(TEST_DATA[0][i], means[i], 1e-5);
            }
            RealMatrix expectedCovariance = new Covariance(TEST_DATA).getCovarianceMatrix();
            assertMatrixEquals(expectedCovariance.getData(), covarianceMatrix.getData(), 1e-5);
        }
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrix() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 3.0, 4.0}, {1.2, 1.8, 2.5}, {1.7, 2.2, 3.1}, {1.0, 2.0, 0.0}};
        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(Collections.singletonList(new Component(1.0,
                new MultivariateNormalDistribution(new double[]{0.0, 0.0, 0.0}, RealMatrixFactory.IDENTITY_2D))));
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialModel);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidInitialModel() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 3.0, 4.0}, {1.2, 1.8, 2.5}, {1.7, 2.2, 3.1}};
        MixtureMultivariateNormalDistribution initialModel = new MixtureMultivariateNormalDistribution(Collections.singletonList(new Component(-1.0,
                new MultivariateNormalDistribution(new double[]{0.0, 0.0, 0.0}, RealMatrixFactory.IDENTITY_2D))));
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialModel);
    }

    @Test
    public void testEstimate() throws Exception {
        double[][] data = new double[][]{{1.0, 2.0, 3.0}, {1.5, 2.5, 3.5}, {2.0, 3.0, 4.0}, {1.2, 1.8, 2.5}, {1.7, 2.2, 3.1}};
        int numComponents = 3;
        MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);

        double[][] dataForEstimate = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; ++i) {
            System.arraycopy(data[i], 0, dataForEstimate[i], 0, data[i].length);
        }

        MultivariateNormalMixtureExpectationModel model = new MultivariateNormalMixtureExpectationMaximization(dataForEstimate);
        model.fit(expectedModel);
        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();

        assertEquals(numComponents, fittedModel.getComponents().size());
        for (Component component : fittedModel.getComponents()) {
            assertTrue(component.getSecond().getProbability() > 0.0);
            MultivariateNormalDistribution distribution = component.getSecond();
            double[] means = distribution.getMeans();
            RealMatrix covarianceMatrix = distribution.getCovarianceMatrix();
            assertEquals(TEST_DATA[0].length, means.length);
            assertEquals(TEST_DATA[0].length, covarianceMatrix.getRowDimension());
            assertEquals(TEST_DATA[0].length, covarianceMatrix.getColumnDimension());
            for (int i = 0; i < TEST_DATA[0].length; ++i) {
                assertEquals(TEST_DATA[0][i], means[i], 1e-5);
            }
            RealMatrix expectedCovariance = new Covariance(data).getCovarianceMatrix();
            assertMatrixEquals(expectedCovariance.getData(), covarianceMatrix.getData(), 1e-5);
        }
    }

    private void assertMatrixEquals(double[][] expected, double[][] actual, double epsilon) {
        for (int i = 0; i < expected.length; ++i) {
            for (int j = 0; j < expected[i].length; ++j) {
                assertEquals(expected[i][j], actual[i][j], epsilon);
            }
        }
    }
}