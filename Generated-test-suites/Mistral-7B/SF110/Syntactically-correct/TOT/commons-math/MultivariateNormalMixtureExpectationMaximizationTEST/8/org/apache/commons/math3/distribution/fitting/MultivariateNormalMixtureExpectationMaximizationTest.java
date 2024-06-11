import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution.Pair;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[0][0]);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0}});
            Assert.fail("Expected DimensionMismatchException");
        } catch (DimensionMismatchException e) {
            // Expected
        }

        try {
            new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 2.0}, {1.0, 3.0}});
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFitWithValidInitialMixtureAndData() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 3.5}, {3.0, 3.5}, {3.5, 3.5}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new Pair<>(0.3, new MultivariateNormalDistribution(new double[]{1.0, 1.5}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0}, {1.0, 1.0}}))),
                new Pair<>(0.7, new MultivariateNormalDistribution(new double[]{2.5, 2.5}, new Array2DRowRealMatrix(new double[][]{
                        {2.0, 2.0}, {2.0, 2.0}})))));

        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        mnme.fit(initialMixture);

        List<Pair<Double, MultivariateNormalDistribution>> expectedComponents = mnme.fittedModel.getComponents();
        List<Pair<Double, MultivariateNormalDistribution>> actualComponents = initialMixture.getComponents();

        Assert.assertEquals(expectedComponents.size(), actualComponents.size());
        for (int i = 0; i < expectedComponents.size(); ++i) {
            Assert.assertEquals(expectedComponents.get(i).getFirst(), actualComponents.get(i).getFirst(), 0.001);
            MultivariateNormalDistribution expectedMvn = expectedComponents.get(i).getSecond();
            MultivariateNormalDistribution actualMvn = actualComponents.get(i).getSecond();
            RealMatrix expectedCovMat = expectedMvn.getCovarianceMatrix();
            RealMatrix actualCovMat = actualMvn.getCovarianceMatrix();
            Assert.assertEquals(expectedCovMat.getRowDimension(), actualCovMat.getRowDimension());
            Assert.assertEquals(expectedCovMat.getColumnDimension(), actualCovMat.getColumnDimension());
            for (int j = 0; j < expectedCovMat.getRowDimension(); ++j) {
                for (int k = 0; k < expectedCovMat.getColumnDimension(); ++k) {
                    Assert.assertEquals(expectedCovMat.getEntry(j, k), actualCovMat.getEntry(j, k), 0.001);
                }
            }
        }
    }

    @Test
    public void testFitWithInvalidInitialMixture() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 3.5}, {3.0, 3.5}, {3.5, 3.5}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Collections.singletonList(
                new Pair<>(0.3, new MultivariateNormalDistribution(new double[]{1.0, 1.5}, new Array2DRowRealMatrix(new double[][]{
                        {1.0, 1.0}, {1.0, 1.0}})))));

        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);

        try {
            mnme.fit(initialMixture);
            Assert.fail("Expected SingularMatrixException, NotStrictlyPositiveException, or DimensionMismatchException");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testFitWithInvalidData() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 3.5}, {3.0, 3.5}, {3.5, 3.5}, {4.0, 4.0}};

        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
            Assert.fail("Expected NotStrictlyPositiveException or DimensionMismatchException");
        } catch (Exception e) {
            // Expected
        }

        try {
            MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
            mnme.fit(new MixtureMultivariateNormalDistribution(Collections.emptyList()));
            Assert.fail("Expected SingularMatrixException, NotStrictlyPositiveException, or DimensionMismatchException");
        } catch (Exception e) {
            // Expected
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 3.5}, {3.0, 3.5}, {3.5, 3.5}, {4.0, 4.0}};

        MixtureMultivariateNormalDistribution expectedMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);

        MultivariateNormalMixtureExpectationMaximization mnme = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution actualMixture = mnme.fittedModel;

        Assert.assertEquals(expectedMixture.getComponents().size(), actualMixture.getComponents().size());
        for (int i = 0; i < expectedMixture.getComponents().size(); ++i) {
            Pair<Double, MultivariateNormalDistribution> expectedComponent = expectedMixture.getComponents().get(i);
            Pair<Double, MultivariateNormalDistribution> actualComponent = actualMixture.getComponents().get(i);

            Assert.assertEquals(expectedComponent.getFirst(), actualComponent.getFirst(), 0.001);
            MultivariateNormalDistribution expectedMvn = expectedComponent.getSecond();
            MultivariateNormalDistribution actualMvn = actualComponent.getSecond();

            RealMatrix expectedCovMat = expectedMvn.getCovarianceMatrix();
            RealMatrix actualCovMat = actualMvn.getCovarianceMatrix();

            Assert.assertEquals(expectedCovMat.getRowDimension(), actualCovMat.getRowDimension());
            Assert.assertEquals(expectedCovMat.getColumnDimension(), actualCovMat.getColumnDimension());
            for (int j = 0; j < expectedCovMat.getRowDimension(); ++j) {
                for (int k = 0; k < expectedCovMat.getColumnDimension(); ++k) {
                    Assert.assertEquals(expectedCovMat.getEntry(j, k), actualCovMat.getEntry(j, k), 0.001);
                }
            }
        }
    }
}