package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.FastMath;

public class HypergeometricDistributionTest {

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativePopulationSize() {
        new HypergeometricDistribution(new MersenneTwister(), -5, 2, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeNumberOfSuccesses() {
        new HypergeometricDistribution(new MersenneTwister(), 5, -2, 5);
    }

    @Test(expected = NotPositiveException.class)
    public void testHypergeometricDistributionNegativeSampleSize() {
        new HypergeometricDistribution(new MersenneTwister(), 5, 2, -5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionNumberOfSuccessesLargerThanPopulationSize() {
        new HypergeometricDistribution(new MersenneTwister(), 5, 6, 5);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testHypergeometricDistributionSampleSizeLargerThanPopulationSize() {
        new HypergeometricDistribution(new MersenneTwister(), 5, 5, 6);
    }

    @Test
    public void testHypergeometricDistributionZeroParameterValues() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 0, 0, 0);
        assertEquals(0, distribution.getNumberOfSuccesses());
        assertEquals(0, distribution.getPopulationSize());
        assertEquals(0, distribution.getSampleSize());
        assertEquals(Double.NaN, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testHypergeometricDistributionSimpleParameters() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 5);
        assertEquals(5, distribution.getNumberOfSuccesses());
        assertEquals(10, distribution.getPopulationSize());
        assertEquals(5, distribution.getSampleSize());
        assertEquals(1.0 / 11.0, distribution.probability(0), 0.001);
        assertEquals(6.0 / 11.0, distribution.probability(1), 0.001);
        assertEquals(1.0, distribution.probability(5), 0.001);
        assertEquals(5.0 / 10.0, distribution.getNumericalMean(), 0.001);
        assertEquals(0.03888888888888889, distribution.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testHypergeometricDistributionCumulativeProbability() {
        HypergeometricDistribution distribution = new HypergeometricDistribution(new MersenneTwister(), 10, 5, 5);
        int[] expectedDomain = distribution.getDomain();
        double[] expectedCumulativeProbabilities = new double[] {
                0.0, 0.009999999999999999, 0.019999999999999997, 0.029999999999999996, 0.039999999999999995, 0.049999999999999993,
                0.059999999999999991, 0.069999999999999989, 0.079999999999999986, 0.089999999999999983, 0.099999999999999979,
                0.1, 0.109999999999999985, 0.119999999999999981, 0.129999999999999977, 0.139999999999999973, 0.149999999999999968,
                0.159999999999999963, 0.169999999999999958, 0.179999999999999953, 0.189999999999999948, 0.199999999999999942,
                0.2, 0.209999999999999936, 0.219999999999999929, 0.229999999999999922, 0.239999999999999914, 0.249999999999999906,
                0.259999999999999897, 0.269999999999999888, 0.279999999999999878, 0.289999999999999868, 0.299999999999999857,
                0.3, 0.309999999999999845, 0.319999999999999834, 0.329999999999999823, 0.339999999999999811, 0.349999999999999799,
                0.359999999999999787, 0.369999999999999774, 0.379999999999999761, 0.389999999999999747, 0.399999999999999733,
                0.4, 0.409999999999999718, 0.419999999999999708, 0.429999999999999697, 0.439999999999999685, 0.449999999999999673,
                0.459999999999999659, 0.469999999999999645, 0.479999999999999630, 0.489999999999999615, 0.4999999999999996, 0.5,
                0.509999999999999585, 0.519999999999999574, 0.529999999999999563, 0.539999999999999551, 0.549999999999999539,
                0.559999999999999526, 0.569999999999999513, 0.5799999999999995, 0.589999999999999485, 0.599999999999999473,
                0.6, 0.599999999999999459, 0.609999999999999447, 0.619999999999999435, 0.629999999999999422, 0.639999999999999408,
                0.649999999999999393, 0.659999999999999378, 0.669999999999999362, 0.679999999999999346, 0.689999999999999329,
                0.7, 0.689999999999999311, 0.699999999999999293, 0.709999999999999274, 0.719999999999999254, 0.729999999999999233,
                0.739999999999999211, 0.749999999999999188, 0.759999999999999164, 0.769999999999999140, 0.779999999999999115,
                0.789999999999999089, 0.799999999999999062, 0.8, 0.799999999999999035, 0.809999999999999007, 0.819999999999998978,
                0.829999999999998948, 0.839999999999998917, 0.849999999999998885, 0.859999999999998852, 0.869999999999998818,
                0.879999999999998783, 0.889999999999998747, 0.899999999999998711, 0.9, 0.899999999999998674, 0.909999999999998636,
                0.919999999999998598, 0.929999999999998559, 0.939999999999998519, 0.949999999999998477, 0.959999999999998435,
                0.969999999999998382, 0.979999999999998328, 0.989999999999998273, 0.999999999999998217, 1.0
        };
        for (int i = 0; i < expectedDomain.length; i++) {
            int x = expectedDomain[i];
            double expectedCumulativeProbability = expectedCumulativeProbabilities[i];
            double actualCumulativeProbability = distribution.cumulativeProbability(x);
            assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.0001);
        }
    }
}