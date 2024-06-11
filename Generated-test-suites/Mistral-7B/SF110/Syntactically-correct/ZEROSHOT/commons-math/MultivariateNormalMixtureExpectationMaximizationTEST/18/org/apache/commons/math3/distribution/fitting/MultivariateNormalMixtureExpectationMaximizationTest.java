import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testInitialize() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization model;
        try {
            model = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            Assert.fail("Initialization should not throw an exception: " + e.getMessage());
            return;
        }
        Assert.assertNotNull(model.getFittedModel());
        Assert.assertEquals(3, model.getData().length);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInitializeWithInvalidLengthData() {
        double[][] data = new double[][]{{1.0, 2.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInitializeWithInvalidColumnsData() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInitializeWithInvalidRowsData() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {1.0, 2.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitWithInitialModelAndValidParameters() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        MultivariateNormalMixtureExpectationMaximization model;
        try {
            model = new MultivariateNormalMixtureExpectationMaximization(data);
            model.fit(initialMixture, 1000, 1.0E-5);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Fitting should not throw an exception: " + e.getMessage());
            return;
        }
        Assert.assertNotNull(model.getFittedModel());
        Assert.assertEquals(3, model.getData().length);
        Assert.assertNotSame(initialMixture, model.getFittedModel());
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrix() {
        double[][] data = new double[][]{{1.0, 2.0, 0.0}, {1.5, 2.5, 0.0}, {2.0, 3.0, 0.0}};
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
        MultivariateNormalMixtureExpectationMaximization model;
        try {
            model = new MultivariateNormalMixtureExpectationMaximization(data);
            model.fit(initialMixture);
        } catch (Exception e) {
            Assert.fail("Fitting should throw an exception: " + e.getMessage());
            return;
        }
        Assert.fail("Fitting should have thrown an exception");
    }

    @Test(expected = ConvergenceException.class)
    public void testFitWithUnconvergedModel() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        MultivariateNormalMixtureExpectationMaximization model;
        try {
            model = new MultivariateNormalMixtureExpectationMaximization(data);
            model.fit(initialMixture, 10, 1.0E-1);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Fitting should not throw an exception: " + e.getMessage());
            return;
        }
        Assert.fail("Fitting should have thrown an exception");
    }

    @Test
    public void testFitWithInitialModelAndDefaultParameters() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MixtureMultivariateNormalDistribution initialMixture = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        MultivariateNormalMixtureExpectationMaximization model;
        try {
            model = new MultivariateNormalMixtureExpectationMaximization(data);
            model.fit(initialMixture);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Fitting should not throw an exception: " + e.getMessage());
            return;
        }
        Assert.assertNotNull(model.getFittedModel());
        Assert.assertEquals(3, model.getData().length);
        Assert.assertNotSame(initialMixture, model.getFittedModel());
        Assert.assertGreaterThan(model.getLogLikelihood(), -Double.MAX_VALUE);
    }

    @Test
    public void testEstimateWithValidData() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MixtureMultivariateNormalDistribution expectedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        MixtureMultivariateNormalDistribution model = MultivariateNormalMixtureExpectationMaximization.estimate(data);
        Assert.assertNotNull(model);
        Assert.assertEquals(expectedModel.getComponents().size(), model.getFittedModel().getComponents().size());
        for (int i = 0; i < expectedModel.getComponents().size(); ++i) {
            MultivariateNormalDistribution expectedComponent = expectedModel.getComponents().get(i).getSecond();
            MultivariateNormalDistribution fittedComponent = model.getFittedModel().getComponents().get(i).getSecond();
            Assert.assertEquals(expectedComponent.getMeans().length, fittedComponent.getMeans().length);
            for (int j = 0; j < expectedComponent.getMeans().length; ++j) {
                Assert.assertEquals(expectedComponent.getMeans()[j], fittedComponent.getMeans()[j], 0.001);
            }
            RealMatrix expectedCovariance = expectedComponent.getCovarianceMatrix();
            RealMatrix fittedCovariance = fittedComponent.getCovarianceMatrix();
            Assert.assertTrue(Arrays.deepEquals(expectedCovariance.getData(), fittedCovariance.getData(), 0.001));
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithInvalidNumberOfComponents() {
        double[][] data = new double[][]{{1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateWithInvalidDataShape() {
        double[][] data = new double[][]{{1.0}, {1.5, 2.5}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }
}