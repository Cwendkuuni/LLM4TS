import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testEstimate() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5} };
        MixtureMultivariateNormalDistribution expectedModel = new MixtureMultivariateNormalDistribution(2);
        expectedModel.getComponents().get(0).setMeans(new double[]{1.5, 2.5});
        expectedModel.getComponents().get(0).setCovariance(new Array2DRowRealMatrix(2, 2).scalarMultiply(0.5));
        expectedModel.getComponents().get(1).setMeans(new double[]{2.5, 3.5});
        expectedModel.getComponents().get(1).setCovariance(new Array2DRowRealMatrix(2, 2).scalarMultiply(1.0));

        MixtureMultivariateNormalDistribution actualModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);

        assertEquals(expectedModel.getComponents().size(), actualModel.getComponents().size());

        for (int i = 0; i < expectedModel.getComponents().size(); i++) {
            MultivariateNormalDistribution expectedComponent = expectedModel.getComponents().get(i).getSecond();
            MultivariateNormalDistribution actualComponent = actualModel.getComponents().get(i).getSecond();
            assertEquals(expectedComponent.getMeans().length, actualComponent.getMeans().length);
            assertArrayEquals(expectedComponent.getMeans(), actualComponent.getMeans());
            RealMatrix expectedCovMat = expectedComponent.getCovariance();
            RealMatrix actualCovMat = actualComponent.getCovariance();
            assertEquals(expectedCovMat.getRowDimension(), actualCovMat.getRowDimension());
            assertEquals(expectedCovMat.getColumnDimension(), actualCovMat.getColumnDimension());
            for (int row = 0; row < expectedCovMat.getRowDimension(); row++) {
                for (int col = 0; col < expectedCovMat.getColumnDimension(); col++) {
                    assertEquals(expectedCovMat.getEntry(row, col), actualCovMat.getEntry(row, col), 0.001);
                }
            }
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateNotStrictlyPositiveData() throws Exception {
        double[][] data = {};
        MixtureMultivariateNormalDistribution.estimate(data, 1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testEstimateDimensionMismatchData() throws Exception {
        double[][] data = { {1.0}, {2.0, 2.0} };
        MixtureMultivariateNormalDistribution.estimate(data, 1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateNumberTooSmallData() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5} };
        MixtureMultivariateNormalDistribution.estimate(data, 0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateNumberTooLargeData() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5}, {1.5, 2.5}, {1.5, 2.5}, {1.5, 2.5} };
        MixtureMultivariateNormalDistribution.estimate(data, 6);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitSingularMatrix() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture, 1, 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNotStrictlyPositiveMaxIterations() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture, -1, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNotStrictlyPositiveThreshold() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture, 1000, -0.001);
    }

    @Test(expected = ConvergenceException.class)
    public void testFitConvergenceException() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture, 1000, 1.0E-12);
    }

    @Test
    public void testFit() throws Exception {
        double[][] data = { {1.0, 2.0}, {1.5, 2.5}, {2.0, 3.0}, {2.5, 3.5}, {3.0, 4.0} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization model = new MultivariateNormalMixtureExpectationMaximization(data);
        model.fit(initialMixture);

        MixtureMultivariateNormalDistribution expectedModel = new MixtureMultivariateNormalDistribution(2);
        expectedModel.getComponents().get(0).setMeans(new double[]{1.5, 2.5});
        expectedModel.getComponents().get(0).setCovariance(new Array2DRowRealMatrix(2, 2).scalarMultiply(0.5));
        expectedModel.getComponents().get(1).setMeans(new double[]{3.0, 4.0});
        expectedModel.getComponents().get(1).setCovariance(new Array2DRowRealMatrix(2, 2).scalarMultiply(1.0));

        assertEquals(expectedModel.getComponents().size(), model.getFittedModel().getComponents().size());

        for (int i = 0; i < expectedModel.getComponents().size(); i++) {
            MultivariateNormalDistribution expectedComponent = expectedModel.getComponents().get(i).getSecond();
            MultivariateNormalDistribution actualComponent = model.getFittedModel().getComponents().get(i).getSecond();
            assertEquals(expectedComponent.getMeans().length, actualComponent.getMeans().length);
            assertArrayEquals(expectedComponent.getMeans(), actualComponent.getMeans());
            RealMatrix expectedCovMat = expectedComponent.getCovariance();
            RealMatrix actualCovMat = actualComponent.getCovariance();
            assertEquals(expectedCovMat.getRowDimension(), actualCovMat.getRowDimension());
            assertEquals(expectedCovMat.getColumnDimension(), actualCovMat.getColumnDimension());
            for (int row = 0; row < expectedCovMat.getRowDimension(); row++) {
                for (int col = 0; col < expectedCovMat.getColumnDimension(); col++) {
                    assertEquals(expectedCovMat.getEntry(row, col), actualCovMat.getEntry(row, col), 0.001);
                }
            }
        }

        assertEquals(expectedModel.getLogLikelihood(), model.getLogLikelihood(), 0.001);
    }
}