import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme;
        try {
            mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
            return;
        }
        // To be implemented: Verify the internal state of the object
    }

    @Test
    public void testFit_InitialMixtureNull() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme;
        try {
            mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
            return;
        }
        MixtureMultivariateNormalDistribution initialMixture = null;
        try {
            mmeme.fit(initialMixture, 10, 0.001);
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Initial mixture cannot be null", e.getMessage());
        }
    }

    @Test
    public void testFit_MaxIterationsNegative() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme;
        try {
            mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
            return;
        }
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        try {
            mmeme.fit(initialMixture, -1, 0.001);
            Assert.fail("Unexpected result: did not throw an exception");
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals("Maximum number of iterations must be non-negative", e.getMessage());
        }
    }

    @Test
    public void testFit_ThresholdNegative() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme;
        try {
            mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
            return;
        }
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        try {
            mmeme.fit(initialMixture, 10, -0.001);
            Assert.fail("Unexpected result: did not throw an exception");
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals("The convergence threshold must be non-negative", e.getMessage());
        }
    }

    @Test
    public void testFit_InitialMixtureEmpty() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme;
        try {
            mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
            return;
        }
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        try {
            mmeme.fit(initialMixture, 10, 0.001);
            Assert.fail("Unexpected result: did not throw an exception");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("The given mixture must have at least two components"));
        }
    }

    @Test
    public void testFit_InitialMixtureInvalidNumComponents() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme;
        try {
            mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
            return;
        }
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(Arrays.asList(
                new MultivariateNormalDistribution(new NormalDistribution(1.0), new Array2DRowRealMatrix(new double[2][2]))));
        try {
            mmeme.fit(initialMixture, 10, 0.001);
            Assert.fail("Unexpected result: did not throw an exception");
        } catch (DimensionMismatchException e) {
            Assert.assertEquals("The number of components in the initial mixture does not match the number of columns in the input data", e.getMessage());
        }
    }

    @Test
    public void testFit() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization mmeme;
        try {
            mmeme = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
            return;
        }
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<>());
        try {
            mmeme.fit(initialMixture, 1000, 1.0E-5);
            MixtureMultivariateNormalDistribution fittedModel = mmeme.getFittedModel();
            // To be implemented: Verify the internal state of the object and the fitted model
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1.0, 2.0}, {2.0, 1.0}, {3.0, 3.0}};
        try {
            MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            MixtureMultivariateNormalDistribution expected = new MixtureMultivariateNormalDistribution(Arrays.asList(
                    new Pair<>(0.33333, new MultivariateNormalDistribution(new double[]{1.5, 1.5}, new Array2DRowRealMatrix(new double[2][2]))),
                    new Pair<>(0.66667, new MultivariateNormalDistribution(new double[]{2.5, 2.5}, new Array2DRowRealMatrix(new double[2][2])))));
            Assert.assertEquals(expected.toString(), result.toString());
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            Assert.fail("Unexpected exception thrown: " + e.getMessage());
        }
    }
}