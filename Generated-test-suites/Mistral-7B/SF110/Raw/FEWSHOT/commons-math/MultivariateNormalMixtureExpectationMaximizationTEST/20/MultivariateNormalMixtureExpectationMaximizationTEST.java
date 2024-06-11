package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.stat.correlation.*;
import org.apache.commons.math3.stat.descriptive.*;
import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFitWithInitialMixtureAndCustomSettings() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5}
        };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new GaussianDistribution(new MultivariateNormalVector(new double[]{1.0, 1.0}), new Covariance(new double[][]{
                        {1.5, 0.5},
                        {0.5, 1.5}
                }))));

        MultivariateNormalMixtureExpectationMaximization mneme = new MultivariateNormalMixtureExpectationMaximization(data);
        mneme.fit(initialMixture, 10, 1e-4);

        MixtureMultivariateNormalDistribution fittedModel = mneme.getFittedModel();
        assertEquals(2, fittedModel.getComponents().size());

        for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
            MultivariateNormalDistribution c = component.getSecond();
            assertEquals(1.0 / 4.0, component.getFirst(), 0.001);
            assertEquals(new MultivariateNormalVector(new double[]{1.0, 1.0}), c.getMean());
            RealMatrix covMat = c.getCovarianceMatrix();
            assertEquals(2, covMat.getRowDimension());
            assertEquals(2, covMat.getColumnDimension());
            assertEquals(1.5, covMat.getEntry(0, 0), 0.001);
            assertEquals(0.5, covMat.getEntry(0, 1), 0.001);
            assertEquals(0.5, covMat.getEntry(1, 0), 0.001);
            assertEquals(1.5, covMat.getEntry(1, 1), 0.001);
        }

        double logLikelihood = mneme.getLogLikelihood();
        assertTrue(logLikelihood > -1500.0 && logLikelihood < -1495.0);
    }

    @Test
    public void testFitWithDefaultSettings() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5}
        };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new GaussianDistribution(new MultivariateNormalVector(new double[]{1.0, 1.0}), new Covariance(new double[][]{
                        {1.5, 0.5},
                        {0.5, 1.5}
                }))));

        MultivariateNormalMixtureExpectationMaximization mneme = new MultivariateNormalMixtureExpectationMaximization(data);
        mneme.fit(initialMixture);

        MixtureMultivariateNormalDistribution fittedModel = mneme.getFittedModel();
        assertEquals(2, fittedModel.getComponents().size());

        for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
            MultivariateNormalDistribution c = component.getSecond();
            double weight = component.getFirst();
            assertEquals(1.0 / 4.0, weight, 0.001);
            assertEquals(new MultivariateNormalVector(new double[]{1.0, 1.0}), c.getMean());
            RealMatrix covMat = c.getCovarianceMatrix();
            assertEquals(2, covMat.getRowDimension());
            assertEquals(2, covMat.getColumnDimension());
            assertEquals(1.5, covMat.getEntry(0, 0), 0.001);
            assertEquals(0.5, covMat.getEntry(0, 1), 0.001);
            assertEquals(0.5, covMat.getEntry(1, 0), 0.001);
            assertEquals(1.5, covMat.getEntry(1, 1), 0.001);
        }

        double logLikelihood = mneme.getLogLikelihood();
        assertTrue(logLikelihood > -1500.0 && logLikelihood < -1495.0);
    }

    @Test
    public void testEstimateWithCustomNumberOfComponents() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5}
        };

        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);

        assertEquals(3, fittedModel.getComponents().size());

        for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
            MultivariateNormalDistribution c = component.getSecond();
            double weight = component.getFirst();
            double sumOfWeights = 0.0;
            for (Pair<Double, MultivariateNormalDistribution> comp : fittedModel.getComponents()) {
                sumOfWeights += comp.getFirst();
            }
            assertEquals(weight / sumOfWeights, 1.0 / 3.0, 0.001);
        }
    }

    @Test
    public void testEstimateWithDefaultNumberOfComponents() throws Exception {
        double[][] data = {
                {1.0, 2.0},
                {1.5, 2.5},
                {2.0, 3.0},
                {2.5, 3.5}
        };

        MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data);

        assertEquals(2, fittedModel.getComponents().size());

        for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
            MultivariateNormalDistribution c = component.getSecond();
            double weight = component.getFirst();
            double sumOfWeights = 0.0;
            for (Pair<Double, MultivariateNormalDistribution> comp : fittedModel.getComponents()) {
                sumOfWeights += comp.getFirst();
            }
            assertEquals(weight / sumOfWeights, 1.0 / 2.0, 0.001);
        }
    }
}