import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.apache.commons.math3.util.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = {{-1.0, 0.5}, {1.0, -1.0}, {2.0, 1.5}, {-0.5, 1.0}};
        try {
            new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyData() {
        double[][] data = {};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithDataWithDifferentSizes() {
        double[][] data = {{1.0, 1.0}, {1.0}, {1.0, 1.0, 1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithDataPointWithLessThanTwoColumns() {
        double[][] data = {{1.0}, {1.0, 1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitWithInitialMixtureAndSpecifiedParameters() {
        double[][] data = {{-1.0, 0.5}, {1.0, -1.0}, {2.0, 1.5}, {-0.5, 1.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>( Arrays.asList(
                    new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new CovarianceMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}})),
                    new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new CovarianceMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}}) )
                )));
            model.fit(initialMixture, 10, 0.001);
            MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
            assertEquals(2, fittedModel.getComponents().size());
            for (Object component : fittedModel.getComponents()) {
                MultivariateNormalDistribution mvn = (MultivariateNormalDistribution) component;
                double[] mean = mvn.getMean();
                RealMatrix covMatrix = mvn.getCovarianceMatrix();
                assertEquals(new double[]{0.0, 0.5}, mean);
                assertEquals(new double[][]{
                        {1.0, 0.5},
                        {0.5, 1.0}
                }, covMatrix.getData());
            }
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFitWithInitialMixtureAndDefaultParameters() {
        double[][] data = {{-1.0, 0.5}, {1.0, -1.0}, {2.0, 1.5}, {-0.5, 1.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>( Arrays.asList(
                    new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new CovarianceMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}})),
                    new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new CovarianceMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}}) )
                )));
            model.fit(initialMixture);
            MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
            assertEquals(2, fittedModel.getComponents().size());
            for (Object component : fittedModel.getComponents()) {
                MultivariateNormalDistribution mvn = (MultivariateNormalDistribution) component;
                double[] mean = mvn.getMean();
                RealMatrix covMatrix = mvn.getCovarianceMatrix();
                assertEquals(new double[]{0.0, 0.5}, mean);
                assertEquals(new double[][]{
                        {1.0, 0.5},
                        {0.5, 1.0}
                }, covMatrix.getData());
            }
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEstimateMethod() {
        double[][] data = {{-1.0, 0.5}, {1.0, -1.0}, {2.0, 1.5}, {-0.5, 1.0}};
        try {
            MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            assertEquals(2, result.getComponents().size());
            for (Object component : result.getComponents()) {
                MultivariateNormalDistribution mvn = (MultivariateNormalDistribution) component;
                double[] mean = mvn.getMean();
                RealMatrix covMatrix = mvn.getCovarianceMatrix();
                assertEquals(new double[]{0.0, 0.5}, mean);
                assertEquals(new double[][]{
                        {1.0, 0.5},
                        {0.5, 1.0}
                }, covMatrix.getData());
            }
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithLessThanTwoComponents() {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateWithMoreComponentsThanDataPoints() {
        double[][] data = {{1.0}, {1.0, 1.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
    }

    @Test
    public void testGetLogLikelihood() {
        double[][] data = {{-1.0, 0.5}, {1.0, -1.0}, {2.0, 1.5}, {-0.5, 1.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>( Arrays.asList(
                    new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new CovarianceMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}})),
                    new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new CovarianceMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}}) )
                )));
            model.fit(initialMixture, 10, 0.001);
            double logLikelihood = model.getLogLikelihood();
            assertTrue(logLikelihood > Double.NEGATIVE_INFINITY);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetFittedModel() {
        double[][] data = {{-1.0, 0.5}, {1.0, -1.0}, {2.0, 1.5}, {-0.5, 1.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>( Arrays.asList(
                    new MultivariateNormalDistribution(new NormalDistribution(0.0, 1.0), new CovarianceMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}})),
                    new MultivariateNormalDistribution(new NormalDistribution(1.0, 1.0), new CovarianceMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.0}}) )
                )));
            model.fit(initialMixture, 10, 0.001);
            MixtureMultivariateNormalDistribution fittedModel = model.getFittedModel();
            assertNotNull(fittedModel);
            assertEquals(2, fittedModel.getComponents().size());
            for (Object component : fittedModel.getComponents()) {
                MultivariateNormalDistribution mvn = (MultivariateNormalDistribution) component;
                double[] mean = mvn.getMean();
                RealMatrix covMatrix = mvn.getCovarianceMatrix();
                assertEquals(new double[]{0.0, 0.5}, mean);
                assertEquals(new double[][]{
                        {1.0, 0.5},
                        {0.5, 1.0}
                }, covMatrix.getData());
            }
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}