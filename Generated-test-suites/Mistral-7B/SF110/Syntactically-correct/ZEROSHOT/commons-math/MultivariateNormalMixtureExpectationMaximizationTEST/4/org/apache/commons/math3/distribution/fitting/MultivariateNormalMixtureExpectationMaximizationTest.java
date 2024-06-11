import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testInitialization() {
        double[][] data = { {1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0} };
        try {
            MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
            assertNotNull(mmme.getFittedModel());
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            fail("Initialization should not throw exceptions: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInitializationWithInvalidDataLength() {
        double[][] data = {};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testInitializationWithInvalidDataColumns() {
        double[][] data = { {1.0}, {2.0, 3.0}, {3.0, 4.0} };
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testInitializationWithInvalidDataRows() {
        double[][] data = { {1.0, 2.0}, {2.0, 3.0} };
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFittingWithGivenParameters() {
        double[][] data = { {1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        try {
            MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
            mmme.fit(initialMixture, 100, 1.0E-6);
            MixtureMultivariateNormalDistribution fittedModel = mmme.getFittedModel();
            assertEquals(2, fittedModel.getComponents().size());
            for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
                assertTrue(component.getFirst() > 0.0);
                assertTrue(component.getSecond().getMeans().length == data[0].length);
                assertTrue(component.getSecond().getCovarianceMatrix().getRowDimension() == data[0].length);
                assertTrue(component.getSecond().getCovarianceMatrix().getColumnDimension() == data[0].length);
            }
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Fitting should not throw exceptions: " + e.getMessage());
        }
    }

    @Test(expected = ConvergenceException.class)
    public void testFittingWithInvalidThreshold() {
        double[][] data = { {1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
        mmme.fit(initialMixture, 100, 0.0);
    }

    @Test
    public void testFittingWithDefaultParameters() {
        double[][] data = { {1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0} };
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(2);
        try {
            MultivariateNormalMixtureExpectationMaximization mmme = new MultivariateNormalMixtureExpectationMaximization(data);
            mmme.fit(initialMixture);
            MixtureMultivariateNormalDistribution fittedModel = mmme.getFittedModel();
            assertEquals(2, fittedModel.getComponents().size());
            for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
                assertTrue(component.getFirst() > 0.0);
                assertTrue(component.getSecond().getMeans().length == data[0].length);
                assertTrue(component.getSecond().getCovarianceMatrix().getRowDimension() == data[0].length);
                assertTrue(component.getSecond().getCovarianceMatrix().getColumnDimension() == data[0].length);
            }
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Fitting should not throw exceptions: " + e.getMessage());
        }
    }

    @Test
    public void testEstimation() {
        double[][] data = { {1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}, {4.0, 5.0}, {5.0, 6.0} };
        try {
            MixtureMultivariateNormalDistribution fittedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
            assertEquals(3, fittedModel.getComponents().size());
            for (Pair<Double, MultivariateNormalDistribution> component : fittedModel.getComponents()) {
                assertTrue(component.getFirst() > 0.0);
                assertTrue(component.getSecond().getMeans().length == data[0].length);
                assertTrue(component.getSecond().getCovarianceMatrix().getRowDimension() == data[0].length);
                assertTrue(component.getSecond().getCovarianceMatrix().getColumnDimension() == data[0].length);
            }
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Estimation should not throw exceptions: " + e.getMessage());
        }
    }
}