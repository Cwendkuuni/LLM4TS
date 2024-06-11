package org.apache.commons.math3.distribution.fitting.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.distribution.fitting.*;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testMultivariateNormalMixtureExpectationMaximizationConstructor() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnmee = new MultivariateNormalMixtureExpectationMaximization(data);
        assertEquals(3, mnmee.getData().length);
        for (int i = 0; i < 3; ++i) {
            assertEquals(2, mnmee.getData()[i].length);
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationConstructorWithEmptyData() {
        double[][] data = new double[0][];
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationConstructorWithDataOfDifferentSize() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testMultivariateNormalMixtureExpectationMaximizationConstructorWithDataWithLessThanTwoColumns() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitMethodWithInitialMixtureAndMaxIterationsAndThreshold() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnmee = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5)));
        mnmee.fit(initialMixture, 10, 1.0E-5);
        assertEquals(10, mnmee.getNumIterations());
        assertEquals(1.0E-5, mnmee.getThreshold(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitMethodWithNegativeMaxIterations() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnmee = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5)));
        mnmee.fit(initialMixture, -10, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitMethodWithNegativeThreshold() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnmee = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5)));
        mnmee.fit(initialMixture, 10, -1.0E-5);
    }

    @Test
    public void testFitMethodWithInitialMixture() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnmee = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5)));
        mnmee.fit(initialMixture);
        assertEquals(1000, mnmee.getMaxIterations());
        assertEquals(1.0E-5, mnmee.getThreshold(), 0.0);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitMethodWithSingularMatrix() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0, 0.0}, {3.0, 4.0, 0.0}, {5.0, 6.0, 0.0}};
        MultivariateNormalMixtureExpectationMaximization mnmee = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5)));
        mnmee.fit(initialMixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitMethodWithNegativeInitialMixtureWeight() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnmee = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5)));
        initialMixture.getComponents().get(0).setWeight(-0.5);
        mnmee.fit(initialMixture);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitMethodWithInitialMixtureWithMixtureComponentOfWrongDimension() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnmee = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5)));
        MultivariateNormalDistribution wrongComponent = new MultivariateNormalDistribution(new NormalDistribution(new NormalDistribution(0.0, 1.0), 1.0), new Covariance(new double[2][2]));
        initialMixture.addComponent(new MultivariateNormalDistributionComponent(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5), wrongComponent));
        mnmee.fit(initialMixture);
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnmee = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5)));
        mnmee.fit(initialMixture);
        double expectedLogLikelihood = 2.9953655416072565;
        assertEquals(expectedLogLikelihood, mnmee.getLogLikelihood(), 0.0);
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mnmee = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5)));
        mnmee.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = new MixtureMultivariateNormalDistribution(mnmee.getFittedModel().getComponents());
        assertEquals(1.0, fittedModel.getComponents().size(), 1.0);
        MultivariateNormalDistribution mvnd = fittedModel.getComponents().get(0).getSecond();
        assertEquals(2, mvnd.getMeans().length);
        double[] means = mvnd.getMeans();
        assertEquals(0.5, means[0], 0.0);
        assertEquals(0.5, means[1], 0.0);
        RealMatrix covMat = mvnd.getCovarianceMatrix();
        RealMatrix expectedCovMat = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        });
        assertEquals(expectedCovMat, covMat);
    }

    @Test
    public void testEstimateMethod() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MixtureMultivariateNormalDistribution expectedMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), 0.5)));
        MixtureMultivariateNormalDistribution estimatedMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
        assertEquals(expectedMixture.getComponents().get(0).getFirst(), estimatedMixture.getComponents().get(0).getFirst(), 0.0);
        MultivariateNormalDistribution estimatedMvnd = estimatedMixture.getComponents().get(0).getSecond();
        double[] estimatedMeans = estimatedMvnd.getMeans();
        assertEquals(0.5, estimatedMeans[0], 0.0);
        assertEquals(0.5, estimatedMeans[1], 0.0);
        RealMatrix estimatedCovMat = estimatedMvnd.getCovarianceMatrix();
        RealMatrix expectedCovMat = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        });
        assertEquals(expectedCovMat, estimatedCovMat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEstimateMethodWithDataOfWrongSize() {
        double[][] data = new double[][]{{1.0, 2.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEstimateMethodWithNumberOfComponentsGreaterThanDataSize() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEstimateMethodWithNumberOfComponentsLessThanDataSize() {
        double[][] data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test
    public void testDataRowClass() {
        double[] data = new double[]{1.0, 2.0};
        DataRow dr = new DataRow(data);
        double[] expectedMeans = new double[]{1.0, 2.0};
        assertEquals(expectedMeans, dr.getMeans(), 0.0);
        assertEquals(1.0 / data.length, dr.getWeight(), 0.0);
    }

}