package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.UnivariateRealDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testEstimate() {
        double[][] data = {
                {1.0, 2.0},
                {3.0, 4.0},
                {3.5, 4.5},
                {1.5, 2.5},
                {2.5, 3.5},
                {2.0, 3.0},
                {1.8, 2.8},
                {2.2, 3.2}
        };

        int numComponents = 3;
        MixtureMultivariateNormalDistribution expectedResult = new MixtureMultivariateNormalDistribution(getComponents(data, numComponents));

        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);

        assertNotNull(result);
        assertTrue(result.getComponents().size() == numComponents);
        assertEquals(expectedResult.getComponents().get(0).getFirst(), result.getComponents().get(0).getFirst(), 0.001);
        assertEquals(expectedResult.getComponents().get(0).getSecond().getMeans(), result.getComponents().get(0).getSecond().getMeans(), 0.001);
        assertEquals(expectedResult.getComponents().get(0).getSecond().getCovarianceMatrix().getData(), result.getComponents().get(0).getSecond().getCovarianceMatrix().getData(), 0.001);

        for (int i = 1; i < numComponents; ++i) {
            MultivariateNormalDistribution mvn1 = expectedResult.getComponents().get(i).getSecond();
            MultivariateNormalDistribution mvn2 = result.getComponents().get(i).getSecond();
            assertNotNull(mvn2);
            assertEquals(mvn1.getMean(), mvn2.getMean(), 0.001);
            RealMatrix covMat1 = mvn1.getCovarianceMatrix();
            RealMatrix covMat2 = mvn2.getCovarianceMatrix();
            assertNotNull(covMat2);
            assertArrayEquals(covMat1.getData(), covMat2.getData(), 0.001);
        }
    }

    @Test
    public void testFit() {
        double[][] data = {
                {1.0, 2.0},
                {3.0, 4.0},
                {3.5, 4.5},
                {1.5, 2.5},
                {2.5, 3.5},
                {2.0, 3.0},
                {1.8, 2.8},
                {2.2, 3.2}
        };

        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);

        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();

        assertNotNull(fittedModel);
        assertEquals(initialMixture.getComponents().size(), fittedModel.getComponents().size());
        for (int i = 0; i < initialMixture.getComponents().size(); ++i) {
            MultivariateNormalDistribution mvn1 = initialMixture.getComponents().get(i).getSecond();
            MultivariateNormalDistribution mvn2 = fittedModel.getComponents().get(i).getSecond();
            assertNotNull(mvn2);
            assertEquals(mvn1.getMean(), mvn2.getMean(), 0.001);
            RealMatrix covMat1 = mvn1.getCovarianceMatrix();
            RealMatrix covMat2 = mvn2.getCovarianceMatrix();
            assertNotNull(covMat2);
            assertArrayEquals(covMat1.getData(), covMat2.getData(), 0.001);
        }

        double logLikelihood = model.getLogLikelihood();
        assertTrue(logLikelihood > -1000);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFit_withInvalidData() {
        double[][] data = {
                {1.0, 2.0},
                {3.0},
                {3.5, 4.5},
                {1.5, 2.5},
                {2.5, 3.5},
                {2.0, 3.0},
                {1.8, 2.8},
                {2.2}
        };

        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);

        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFit_withInvalidInitialMixture() {
        double[][] data = {
                {1.0, 2.0},
                {3.0, 4.0},
                {3.5, 4.5},
                {1.5, 2.5},
                {2.5, 3.5},
                {2.0, 3.0},
                {1.8, 2.8},
                {2.2, 3.2}
        };

        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new Pair<Double, MultivariateNormalDistribution>(1.0, new MultivariateNormalDistribution(new double[0][0]))));

        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture);
    }

    private List<Pair<Double, MultivariateNormalDistribution>> getComponents(double[][] data, int numComponents) {
        List<Pair<Double, MultivariateNormalDistribution>> components = new ArrayList<>();
        for (int i = 0; i < numComponents; ++i) {
            int numRows = data.length / numComponents;
            double[][] subData = new double[numRows][data[0].length];
            for (int j = i * numRows; j < (i + 1) * numRows; ++j) {
                System.arraycopy(data[j], 0, subData[j - i * numRows], 0, data[j].length);
            }

            MultivariateNormalDistribution mvn = new MultivariateNormalDistribution(Covariance.cov(subData).getMean(), Covariance.cov(subData).getCovarianceMatrix());
            components.add(new Pair<>(1.0 / numRows, mvn));
        }
        return components;
    }
}