package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithInitialMixture() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new double[]{-0.5, -0.5}, new Array2DRowRealMatrix(new double[][]{ {1.0, 1.0}, {1.0, 1.0} })),
                new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new Array2DRowRealMatrix(new double[][]{ {1.0, 1.0}, {1.0, 1.0} }))));

        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);
        mnmem.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = mnmem.getFittedModel();

        // Check the number of components
        assertEquals(2, fittedModel.getComponents().size());

        // Check the mean and covariance for each component
        List<Pair<Double, MultivariateNormalDistribution>> components = fittedModel.getComponents();
        for (int i = 0; i < 2; i++) {
            MultivariateNormalDistribution mvn = components.get(i).getSecond();
            double[] mean = mvn.getMeans();
            RealMatrix covMatrix = mvn.getCovarianceMatrix();
            double[][] covMatData = covMatrix.getData();

            // Check mean
            if (i == 0) {
                assertArrayEquals(new double[]{-0.495951263754215, -0.501393500163545}, mean, 0.0001);
            } else {
                assertArrayEquals(new double[]{0.496227885658946, 0.501568236732231}, mean, 0.0001);
            }

            // Check covariance
            assertArrayEquals(new double[][]{ {0.884831487871709, 0.680016386647424}, {0.680016386647424, 0.919165971361559} }, covMatData, 0.001);
        }

        // Check log-likelihood
        assertEquals(1.121349277758352, mnmem.getLogLikelihood(), 0.0001);
    }

    @Test
    public void testFitWithoutInitialMixture() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5} };

        MultivariateNormalMixtureExpectationMaximization mnmem = new MultivariateNormalMixtureExpectationMaximization(data);

        MixtureMultivariateNormalDistribution initialMixture = mnmem.estimate(data, 2);
        mnmem.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = mnmem.getFittedModel();

        // Check the number of components
        assertEquals(2, fittedModel.getComponents().size());

        // Check the mean and covariance for each component
        List<Pair<Double, MultivariateNormalDistribution>> components = fittedModel.getComponents();
        for (int i = 0; i < 2; i++) {
            MultivariateNormalDistribution mvn = components.get(i).getSecond();
            double[] mean = mvn.getMeans();
            RealMatrix covMatrix = mvn.getCovarianceMatrix();
            double[][] covMatData = covMatrix.getData();

            // Check mean
            if (i == 0) {
                assertArrayEquals(new double[]{-0.495951263754215, -0.501393500163545}, mean, 0.0001);
            } else {
                assertArrayEquals(new double[]{0.496227885658946, 0.501568236732231}, mean, 0.0001);
            }

            // Check covariance
            assertArrayEquals(new double[][]{ {0.884831487871709, 0.680016386647424}, {0.680016386647424, 0.919165971361559} }, covMatData, 0.001);
        }

        // Check log-likelihood
        assertEquals(1.121349277758352, mnmem.getLogLikelihood(), 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeData() throws Exception {
        double[][] data = { {-1.0, -2.0}, {-1.5, -2.5}, {-2.0, -3.0}, {-2.5, -3.5} };
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithEmptyData() throws Exception {
        new MultivariateNormalMixtureExpectationMaximization(new double[][]{});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithDataRowsOfDifferentLength() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5, 3.0}, {2.0, 3.0}, {2.5, 3.5} };
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidNumComponents() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5} };
        new MultivariateNormalMixtureExpectationMaximization(data, -1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testFitWithMoreComponentsThanDataPoints() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5} };
        new MultivariateNormalMixtureExpectationMaximization(data, 5);
    }
}