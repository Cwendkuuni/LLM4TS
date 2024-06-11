import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstruction() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMean() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        double[] probabilities = new double[10];
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = dist.probability(i);
        }
        double[] expectedProbabilities = new double[]{0.1138729183212483, 0.1329338342356543, 0.1405054223467676, 0.1378553821336263, 0.1287973138735627, 0.117713637143417, 0.107490919611991, 0.0978231273029163, 0.088445664119783, 0.081158291315285};
        assertArrayEquals(expectedProbabilities, probabilities, 0.001);
    }

    @Test
    public void testCumulativeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        double[] cumulativeProbabilities = new double[11];
        for (int i = 0; i < cumulativeProbabilities.length; i++) {
            cumulativeProbabilities[i] = dist.cumulativeProbability(i);
        }
        double[] expectedCumulativeProbabilities = new double[]{0.0, 0.1138729183212483, 0.2277458366424967, 0.341558754963745, 0.4553316732850934, 0.5691645916064419, 0.6830675109278894, 0.796940430259337, 0.9098933496008845, 0.9998335596688875, 1.0};
        assertArrayEquals(expectedCumulativeProbabilities, cumulativeProbabilities, 0.001);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeX() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0);
    }

    @Test
    public void testCumulativeProbabilityWithMaxValueX() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(1.0, dist.cumulativeProbability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testSample() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        assertEquals(2, sample, 0);
    }

    @Test
    public void testSampleWithLargeMean() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(100.0);
        int sample = dist.sample();
        assertEquals(100, sample, 0);
    }

    @Test
    public void testNormalApproximateProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        double[] probabilities = new double[10];
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = dist.normalApproximateProbability(i);
        }
        double[] expectedProbabilities = new double[]{0.1127520143175505, 0.1271970231245446, 0.1397724127596343, 0.1499322312250443, 0.1575110271195604, 0.1636232219245219, 0.1678353551837641, 0.1702135930437176, 0.1699794739370919, 0.1664607795525025};
        assertArrayEquals(expectedProbabilities, probabilities, 0.001);
    }

    @Test
    public void testInverseCumulativeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        double p = 0.2;
        int result = dist.inverseCumulativeProbability(p);
        int expected = 1;
        assertEquals(expected, result);
    }

    @Test
    public void testInverseCumulativeProbabilityWithLargeProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        double p = 0.9999999;
        int result = dist.inverseCumulativeProbability(p);
        int expected = Integer.MAX_VALUE;
        assertEquals(expected, result);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        dist.inverseCumulativeProbability(-0.1);
    }

    @Test
    public void testCumulativeProbabilityWithLargeInput() throws Exception {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        double cumulativeProbability = dist.cumulativeProbability(Integer.MAX_VALUE / 2);
        assertEquals(1.0, cumulativeProbability, 0.0);
    }
}