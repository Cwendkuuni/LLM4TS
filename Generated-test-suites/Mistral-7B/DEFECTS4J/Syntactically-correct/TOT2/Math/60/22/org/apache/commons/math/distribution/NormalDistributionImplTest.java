import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl_ValidParameters() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        assertEquals(0.0, normalDistribution.getMean(), 0.0);
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);

        double density = normalDistribution.density(0.0);
        assertTrue(density > 0);

        double probability = normalDistribution.cumulativeProbability(0.0);
        assertEquals(0.5, probability, 0.01);

        double inverseProbability = normalDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, inverseProbability, 0.01);

        double sample = normalDistribution.sample();
        assertTrue(sample >= -3.5 && sample <= 3.5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImpl_InvalidParameters() {
        new NormalDistributionImpl(-1.0, 1.0);
    }

    @Test
    public void testNormalDistributionImpl_ValidDensities() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);

        for (double x = -5.0; x <= 5.0; x += 0.1) {
            double density = normalDistribution.density(x);
            double expectedDensity = FastMath.exp(-0.5 * Math.pow(x, 2)) / (1.0 * Math.sqrt(2 * Math.PI));
            assertEquals(expectedDensity, density, 0.01);
        }
    }

    @Test
    public void testNormalDistributionImpl_ValidProbabilities() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double probability = normalDistribution.cumulativeProbability(p);
            double expectedProbability = 0.5 * (1.0 + Erf.erf(p / (1.0 * Math.sqrt(2.0))));
            assertEquals(expectedProbability, probability, 0.01);
        }
    }

    @Test
    public void testNormalDistributionImpl_ValidInverseProbabilities() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);

        for (double p = 0.0; p <= 1.0; p += 0.01) {
            double inverseProbability = normalDistribution.inverseCumulativeProbability(p);
            double expectedInverseProbability = normalDistribution.quantile(p);
            assertEquals(expectedInverseProbability, inverseProbability, 0.01);
        }
    }

    @Test
    public void testNormalDistributionImpl_SampleSeries() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0.0, 1.0);
        RandomDataGenerator randomData = new RandomDataGenerator();

        double[] samples = new double[1000];

        for (int i = 0; i < samples.length; i++) {
            samples[i] = normalDistribution.sample(randomData);
        }

        for (int i = 0; i < samples.length; i++) {
            assertTrue(samples[i] >= -3.5 && samples[i] <= 3.5);
        }
    }
}