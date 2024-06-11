package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testInstance() {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 } };
        MultivariateNormalMixtureExpectationMaximization mnm = null;
        try {
            mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (Exception e) {
            fail("Failed to create instance: " + e.getMessage());
        }
        assertNotNull(mnm);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidDataLength() {
        double[][] data = { { 1.0, 2.0 } };
        MultivariateNormalMixtureExpectationMaximization mnm = null;
        try {
            mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (Exception e) {
            if (e.getMessage().equals("data.length < 1")) {
                throw e;
            } else {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInvalidDataRows() {
        double[][] data = { { 1.0, 2.0 }, { 3.0 }, { 5.0, 6.0 } };
        MultivariateNormalMixtureExpectationMaximization mnm = null;
        try {
            mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (DimensionMismatchException e) {
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInvalidDataColumns() {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0 } };
        MultivariateNormalMixtureExpectationMaximization mnm = null;
        try {
            mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (DimensionMismatchException e) {
            fail("Unexpected exception: " + e.getMessage());
        } catch (Exception e) {
            if (e.getMessage().contains("NumberIsTooSmallException")) {
                throw e;
            } else {
                fail("Unexpected exception: " + e.getMessage());
            }
        }
    }

    @Test
    public void testFit() {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 }, { 1.0, 2.0 } };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MixtureComponent(0.5, new MultivariateNormalDistribution(new RealVectorValue(new double[] { 1.0, 1.0 }),
                        new DiagonalCovariance(new double[] { 1.0, 1.0 }))),
                new MixtureComponent(0.5, new MultivariateNormalDistribution(new RealVectorValue(new double[] { 3.0, 3.0 }),
                        new DiagonalCovariance(new double[] { 1.0, 1.0 })))));
        MultivariateNormalMixtureExpectationMaximization mnm = null;
        try {
            mnm = new MultivariateNormalMixtureExpectationMaximization(data);
            mnm.fit(initialMixture);
            double[][] expectedData = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 }, { 1.0, 2.0 } };
            MixtureMultivariateNormalDistribution fittedModel = mnm.getFittedModel();
            List<MixtureComponent> components = fittedModel.getComponents();
            assertEquals(initialMixture.getComponents().size(), components.size());
            for (int i = 0; i < components.size(); ++i) {
                MixtureComponent component1 = components.get(i);
                MixtureComponent component2 = initialMixture.getComponents().get(i);
                assertEquals(component1.getWeight(), component2.getWeight(), 0.001);
                RealVector mean1 = component1.getSecond().getMeans();
                RealVector mean2 = component2.getSecond().getMeans();
                assertArrayEquals(mean1.toArray(), mean2.toArray(), 0.001);
                RealMatrix covMat1 = component1.getSecond().getCovarianceMatrix().getData();
                RealMatrix covMat2 = component2.getSecond().getCovarianceMatrix().getData();
                assertArrayEquals(covMat1, covMat2, 0.001);
            }
        } catch (Exception e) {
            fail("Failed to fit: " + e.getMessage());
        }
    }

    @Test
    public void testFitWithDefaults() {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 }, { 1.0, 2.0 } };
        MixtureMultivariateNormalDistribution initialMixture = new MultivariateNormalMixtureExpectationMaximization(data).getFittedModel();
        MultivariateNormalMixtureExpectationMaximization mnm = null;
        try {
            mnm = new MultivariateNormalMixtureExpectationMaximization(data);
            mnm.fit();
            MixtureMultivariateNormalDistribution fittedModel = mnm.getFittedModel();
            assertEquals(initialMixture.getComponents().size(), fittedModel.getComponents().size());
            for (int i = 0; i < fittedModel.getComponents().size(); ++i) {
                MixtureComponent component1 = fittedModel.getComponents().get(i);
                MixtureComponent component2 = initialMixture.getComponents().get(i);
                assertEquals(component1.getWeight(), component2.getWeight(), 0.001);
                RealVector mean1 = component1.getSecond().getMeans();
                RealVector mean2 = component2.getSecond().getMeans();
                assertArrayEquals(mean1.toArray(), mean2.toArray(), 0.001);
                RealMatrix covMat1 = component1.getSecond().getCovarianceMatrix().getData();
                RealMatrix covMat2 = component2.getSecond().getCovarianceMatrix().getData();
                assertArrayEquals(covMat1, covMat2, 0.001);
            }
        } catch (Exception e) {
            fail("Failed to fit with defaults: " + e.getMessage());
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 }, { 1.0, 2.0 } };
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        double[][] expectedData = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 }, { 1.0, 2.0 } };
        MixtureMultivariateNormalDistribution fittedModel = new MultivariateNormalMixtureExpectationMaximization(expectedData).getFittedModel();
        assertEquals(estimatedModel.getComponents().size(), 2);
        for (int i = 0; i < estimatedModel.getComponents().size(); ++i) {
            MixtureComponent component1 = estimatedModel.getComponents().get(i);
            MixtureComponent component2 = fittedModel.getComponents().get(i);
            assertEquals(component1.getWeight(), component2.getWeight(), 0.001);
            RealVector mean1 = component1.getSecond().getMeans();
            RealVector mean2 = component2.getSecond().getMeans();
            assertArrayEquals(mean1.toArray(), mean2.toArray(), 0.001);
            RealMatrix covMat1 = component1.getSecond().getCovarianceMatrix().getData();
            RealMatrix covMat2 = component2.getSecond().getCovarianceMatrix().getData();
            assertArrayEquals(covMat1, covMat2, 0.001);
        }
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 }, { 1.0, 2.0 } };
        MultivariateNormalMixtureExpectationMaximization mnm = null;
        try {
            mnm = new MultivariateNormalMixtureExpectationMaximization(data);
            mnm.fit();
            double expectedLogLikelihood = 6.211682366409912;
            double logLikelihood = mnm.getLogLikelihood();
            assertEquals(expectedLogLikelihood, logLikelihood, 0.001);
        } catch (Exception e) {
            fail("Failed to get log likelihood: " + e.getMessage());
        }
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = { { 1.0, 2.0 }, { 3.0, 4.0 }, { 5.0, 6.0 }, { 1.0, 2.0 } };
        MultivariateNormalMixtureExpectationMaximization mnm = null;
        try {
            mnm = new MultivariateNormalMixtureExpectationMaximization(data);
            mnm.fit();
            MixtureMultivariateNormalDistribution fittedModel = mnm.getFittedModel();
            assertNotNull(fittedModel);
        } catch (Exception e) {
            fail("Failed to get fitted model: " + e.getMessage());
        }
    }

}