import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistributionTest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistributionCreation() {
        double[] means = new double[]{0.0, 1.0};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 1.0}};

        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, mnd.getDimension());
        assertArrayEquals(means, mnd.getMeans());

        RealMatrix covariance = mnd.getCovariances();
        assertArrayEquals(covariances, covariance.getData());
    }

    @Test
    public void testMeanAndCovarianceAccessors() {
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new double[]{1.0, 2.0},
                new double[][]{{3.0, 2.0}, {2.0, 4.0}});

        double[] means = mnd.getMeans();
        double[] stdDevs = mnd.getStandardDeviations();

        assertEquals(means.length, 2);
        assertEquals(stdDevs.length, 2);

        assertArrayEquals(new double[]{1.0, 2.0}, means);
        assertArrayEquals(new double[]{Math.sqrt(3.0), Math.sqrt(4.0)}, stdDevs);
    }

    @Test
    public void testSample() {
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new double[]{1.0, 2.0},
                new double[][]{{3.0, 2.0}, {2.0, 4.0}});

        double[] sample = mnd.sample();
        assertArrayEquals(new double[]{2.0, 4.0}, sample);

        double[] mean = mnd.getMeans();
        double[] stdDev = mnd.getStandardDeviations();

        for (int i = 0; i < sample.length; i++) {
            double expected = mean[i] + stdDev[i] * new java.util.Random().nextGaussian();
            assertArrayEquals(new double[]{expected}, new double[]{sample[i]});
        }
    }

    @Test
    public void testDensity() {
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new double[]{1.0, 2.0},
                new double[][]{{3.0, 2.0}, {2.0, 4.0}});

        double[] sample = mnd.sample();

        double density = mnd.density(sample);
        double expected = FastMath.pow(2 * FastMath.PI, -2) *
                FastMath.pow(mnd.getCovariances().determinant(), -0.5) *
                mnd.getExponentTerm(sample);
        assertEquals(expected, density, 0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidInput() {
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new double[]{1.0, 2.0},
                new double[][]{{3.0, 2.0}, {2.0, 4.0}});

        double[] invalidSample = new double[4];
        mnd.density(invalidSample);
    }

    @Test
    public void testSingularMatrixException() {
        double[] means = new double[]{0.0, 1.0};
        double[][] covariances = new double[][]{{1.0, 0.0}, {0.0, 0.0}};

        assertThrows(SingularMatrixException.class,
                () -> new MultivariateNormalDistribution(means, covariances));
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = new double[]{0.0, 1.0};
        double[][] covariances = new double[][]{{1.0, -1.0}, {-1.0, 1.0}};

        assertThrows(NonPositiveDefiniteMatrixException.class,
                () -> new MultivariateNormalDistribution(means, covariances));
    }

    @Test
    public void testDimensionMismatchException() {
        double[] means = new double[]{0.0};
        double[][] covariances = new double[][]{{1.0, 1.0}, {1.0, 2.0}};

        assertThrows(DimensionMismatchException.class,
                () -> new MultivariateNormalDistribution(means, covariances));

        double[] means2 = new double[]{0.0, 0.0};
        double[][] covariances2 = new double[][]{{1.0, 1.0}, {1.0, 1.0}};

        assertThrows(DimensionMismatchException.class,
                () -> new MultivariateNormalDistribution(means2, covariances2));
    }

    @Test
    public void testGetExponentTerm() {
        MultivariateNormalDistribution mnd = new MultivariateNormalDistribution(new double[]{1.0, 2.0},
                new double[][]{{3.0, 2.0}, {2.0, 4.0}});

        double[] sample = mnd.sample();
        double exponentTerm = mnd.getExponentTerm(sample);

        double[] means = mnd.getMeans();
        double[] stdDevs = mnd.getStandardDeviations();
        double[] centered = new double[sample.length];

        for (int i = 0; i < sample.length; i++) {
            centered[i] = sample[i] - means[i];
        }

        RealMatrix inverse = mnd.getCovariances().getInverse();
        double[] preMultiplied = inverse.preMultiply(centered).toArray();

        assertEquals(exponentTerm, FastMath.exp(-0.5 * Pair.product(preMultiplied, centered)));
    }

    @Test
    public void testNegativeEigenvalue() {
        double[] means = new double[]{0.0, 1.0};
        double[][] covariances = new double[][]{{-1.0, 0.0}, {0.0, -1.0}};

        assertThrows(NonPositiveDefiniteMatrixException.class,
                () -> new MultivariateNormalDistribution(means, covariances));
    }
}