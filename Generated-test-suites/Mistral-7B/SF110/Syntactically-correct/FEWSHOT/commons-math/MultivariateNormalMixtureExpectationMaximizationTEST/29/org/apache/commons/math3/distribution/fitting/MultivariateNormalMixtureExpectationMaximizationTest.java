package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.math.MathArrays;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDataLength() throws Exception {
        double[][] data = new double[][]{-1.0, {1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorDataWithNegativeRow() throws Exception {
        double[][] data = {{-1.0, 1.0}, {1.0, 1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDataWithMismatchedColumns() throws Exception {
        double[][] data = {{1.0, 1.0, 2.0}, {1.0, 1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorDataWithEmptyRows() throws Exception {
        double[][] data = {{1.0}, {}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithInitialMixtureMismatchedComponentsSize() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))),
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                })))));
        new MultivariateNormalMixtureExpectationMaximization(data).fit(initialMixture);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithInitialMixtureMismatchedComponentsMeanSize() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))),
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                })))));
        new MultivariateNormalMixtureExpectationMaximization(data).fit(initialMixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInitialMixtureNegativeMaxIterations() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))),
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                })))));
        new MultivariateNormalMixtureExpectationMaximization(data).fit(initialMixture, -1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInitialMixtureNegativeThreshold() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))),
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                })))));
        new MultivariateNormalMixtureExpectationMaximization(data).fit(initialMixture, 1000, -1.0);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovMat() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 0.0}
                }))),
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 0.0}
                })))));
        new MultivariateNormalMixtureExpectationMaximization(data).fit(initialMixture);
    }

    @Test(expected = ConvergenceException.class)
    public void testFitWithUnconvergedEM() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))),
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                })))));
        new MultivariateNormalMixtureExpectationMaximization(data).fit(initialMixture, 1, 1.0E-5);
    }

    @Test
    public void testFit() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))),
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                })))));
        new MultivariateNormalMixtureExpectationMaximization(data).fit(initialMixture);
        double expectedLogLikelihood = -1.5228715443933383;
        double actualLogLikelihood = new MultivariateNormalMixtureExpectationMaximization(data).getLogLikelihood();
        assertEquals(expectedLogLikelihood, actualLogLikelihood, 1.0e-6);
    }

    @Test
    public void testEstimate() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MixtureMultivariateNormalDistribution expectedModel = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.5, 1.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.5, 0.5},
                        {0.5, 0.5}
                }))),
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{2.5, 2.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.5, 0.5},
                        {0.5, 0.5}
                })))));
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        assertEquals(expectedModel, estimatedModel);
    }

    @Test
    public void testGetFittedModel() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))),
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                })))));
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = mnme.getFittedModel();
        MixtureMultivariateNormalDistribution expectedModel = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.5, 1.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.5, 0.5},
                        {0.5, 0.5}
                }))),
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{2.5, 2.5}, new Array2DRowRealMatrix(new double[][]{
                        {0.5, 0.5},
                        {0.5, 0.5}
                })))));
        assertEquals(expectedModel, fittedModel);
    }

    @Test
    public void testGetLogLikelihood() throws Exception {
        double[][] data = {{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                }))),
                new Pair<>(0.5, new MultivariateNormalDistribution(new double[]{2.0, 2.0}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0},
                        {1.0, 1.0}
                })))));
        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit(initialMixture);
        double expectedLogLikelihood = -1.5228715443933383;
        double actualLogLikelihood = mnme.getLogLikelihood();
        assertEquals(expectedLogLikelihood, actualLogLikelihood, 1.0e-6);
    }
}