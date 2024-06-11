import static org.junit.Assert.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import java.util.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    // Basic test case for constructor
    @Test
    public void testConstructor() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization instance = new MultivariateNormalMixtureExpectationMaximization(data);
        assertArrayEquals(data, instance.data);
        assertEquals(2, instance.data.length);
        assertEquals(2, instance.data[0].length);
    }

    // Basic test case for fit method
    @Test
    public void testFit() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(List.of(
                new MultivariateNormalDistribution(new MultivariateNormalDistribution.CovarianceMatrix(new double[][]{
                        {1.0, 0.0},
                        {0.0, 1.0}}, new double[]{0.0, 0.0}), 0.5),
                new MultivariateNormalDistribution(new MultivariateNormalDistribution.CovarianceMatrix(new double[][]{
                        {2.0, 0.0},
                        {0.0, 2.0}}, new double[]{1.0, 1.0}), 0.5)
        ));
        MultivariateNormalMixtureExpectationMaximization instance = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 1.0}, {2.0, 2.0}});
        instance.fit(initialMixture, 10, 0.001);
        assertEquals(instance.getFittedModel(), initialMixture);
    }

    // Edge cases and exceptions test cases

    // Test for empty data
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorEmptyData() throws NotStrictlyPositiveException {
        new MultivariateNormalMixtureExpectationMaximization(new double[][]{});
    }

    // Test for non-square data
    @Test(expected = DimensionMismatchException.class)
    public void testConstructorNonSquareData() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    // Test for negative data
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorNegativeData() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = new double[][]{{-1.0, 2.0}, {2.0, 3.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    // Test for negative maxIterations or threshold
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeMaxIterations() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(List.of(
                new MultivariateNormalDistribution(new MultivariateNormalDistribution.CovarianceMatrix(new double[][]{
                        {1.0, 0.0},
                        {0.0, 1.0}}, new double[]{0.0, 0.0}), 0.5),
                new MultivariateNormalDistribution(new MultivariateNormalDistribution.CovarianceMatrix(new double[][]{
                        {2.0, 0.0},
                        {0.0, 2.0}}, new double[]{1.0, 1.0}), 0.5)
        ));
        MultivariateNormalMixtureExpectationMaximization instance = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 1.0}, {2.0, 2.0}});
        instance.fit(initialMixture, -1, 0.001);
    }

    // Test for negative threshold
    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeThreshold() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(List.of(
                new MultivariateNormalDistribution(new MultivariateNormalDistribution.CovarianceMatrix(new double[][]{
                        {1.0, 0.0},
                        {0.0, 1.0}}, new double[]{0.0, 0.0}), 0.5),
                new MultivariateNormalDistribution(new MultivariateNormalDistribution.CovarianceMatrix(new double[][]{
                        {2.0, 0.0},
                        {0.0, 2.0}}, new double[]{1.0, 1.0}), 0.5)
        ));
        MultivariateNormalMixtureExpectationMaximization instance = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 1.0}, {2.0, 2.0}});
        instance.fit(initialMixture, 1000, -1.0);
    }

    // Test for SingularMatrixException
    @Test(expected = SingularMatrixException.class)
    public void testFitSingularMatrixException() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(List.of(
                new MultivariateNormalDistribution(new MultivariateNormalDistribution.CovarianceMatrix(new double[][]{
                        {1.0, 0.0},
                        {0.0, 1.0}}, new double[]{0.0, 0.0}), 0.5),
                new MultivariateNormalDistribution(new MultivariateNormalDistribution.CovarianceMatrix(new double[][]{
                        {2.0, 0.0},
                        {0.0, 2.0}}, new double[]{1.0, 1.0}), 0.5)
        ));
        MultivariateNormalMixtureExpectationMaximization instance = new MultivariateNormalMixtureExpectationMaximization(new double[][]{{1.0, 1.0}, {2.0, 1.0}});
        instance.fit(initialMixture, 1000, 0.001);
    }
}