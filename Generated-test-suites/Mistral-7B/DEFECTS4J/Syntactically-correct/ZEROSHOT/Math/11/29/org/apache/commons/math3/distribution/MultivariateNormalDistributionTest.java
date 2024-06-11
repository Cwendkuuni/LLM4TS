import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MultivariateNormalDistributionTest {

    private static final int DIM = 3;
    private static final double EPS = 1e-14;

    private MultivariateNormalDistribution dist;
    private double[] means;
    private double[][] covariances;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        means = new double[]{0.1, 0.2, 0.3};
        covariances = new double[][]{
                {1.0, 0.5, 0.1},
                {0.5, 2.0, 1.5},
                {0.1, 1.5, 3.0}
        };
        rng = new Well19937c();
        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (NonPositiveDefiniteMatrixException | SingularMatrixException | DimensionMismatchException e) {
            throw new RuntimeException("Unexpected exception during setUp", e);
        }
    }

    @Test
    public void testGetMeans() {
        assertArrayEquals(means, dist.getMeans(), EPS);
    }

    @Test
    public void testGetCovariances() {
        RealMatrix expected = new Array2DRowRealMatrix(covariances);
        RealMatrix actual = dist.getCovariances();
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertEquals(expected, actual.transpose());
    }

    @Test
    public void testDensity() {
        double[] testValues = new double[]{0.5, 1.5, 2.5};
        double expected = FastMath.pow(2 * FastMath.PI, -DIM / 2) * FastMath.pow(dist.getCovariances().determinant(), -0.5) * getExponentTerm(testValues);
        double actual = dist.density(testValues);
        assertEquals(expected, actual, EPS);
    }

    @Test
    public void testSample() {
        double[] sample = dist.sample();
        for (int i = 0; i < DIM; i++) {
            assertEquals(means[i] + Math.random() * Math.sqrt(covariances[i][i]), sample[i], EPS);
        }
    }

    private double getExponentTerm(double[] values) {
        double sum = 0;
        for (int i = 0; i < DIM; i++) {
            double centered = values[i] - means[i];
            double preMultiplied = 0;
            for (int j = 0; j < DIM; j++) {
                preMultiplied += dist.getCovariances().getEntry(i, j) * centered;
            }
            sum += preMultiplied * centered;
        }
        return Math.exp(-0.5 * sum);
    }

    @Test
    public void testSingularValueDecomposition() {
        // MultivariateNormalDistribution uses eigenvalue decomposition
        // but we test SingularValueDecomposition for completeness
        try {
            SingularValueDecomposition svd = new SingularValueDecomposition(dist.getCovariances());
            RealMatrix expected = svd.getV().transpose().multiply(svd.getU());
            RealMatrix actual = dist.getCovariances();
            assertEquals(expected.getRowDimension(), actual.getRowDimension());
            assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
            assertEquals(expected, actual.transpose());
        } catch (SingularMatrixException e) {
            // This is expected since the covariance matrix is symmetric and positive definite
        }
    }
}