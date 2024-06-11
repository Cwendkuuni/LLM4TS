import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RandomData;
import org.junit.Test;

public class PoissonDistributionImplTest {

    @Test
    public void testProbability() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.13533528323066488, dist.probability(0), 0.0000001);
        assertEquals(0.22313879738954827, dist.probability(1), 0.0000001);
        assertEquals(0.28070175438639296, dist.probability(2), 0.0000001);
        assertEquals(0.22313879738954827, dist.probability(1), 0.0000001);
        assertEquals(0.07711051228336832, dist.probability(3), 0.0000001);
        assertEquals(0.02274601528369515, dist.probability(4), 0.0000001);
        assertEquals(0.006624976253276372, dist.probability(5), 0.0000001);
        assertEquals(0.001984425021433195, dist.probability(6), 0.0000001);
        assertEquals(0.00064846615241111, dist.probability(7), 0.0000001);
        assertEquals(0.000212928435133614, dist.probability(8), 0.0000001);
        assertEquals(0.000069875596641679, dist.probability(9), 0.0000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0);
        assertEquals(0.04225825377261458, dist.cumulativeProbability(0), 0.0000001);
        assertEquals(0.13533528323066488, dist.cumulativeProbability(1), 0.0000001);
        assertEquals(0.2494836462468651, dist.cumulativeProbability(2), 0.0000001);
        assertEquals(0.3862305615854448, dist.cumulativeProbability(3), 0.0000001);
        assertEquals(0.5153332290520843, dist.cumulativeProbability(4), 0.0000001);
        assertEquals(0.6380096628268938, dist.cumulativeProbability(5), 0.0000001);
        assertEquals(0.754714875456883, dist.cumulativeProbability(6), 0.0000001);
        assertEquals(0.8664802053362025, dist.cumulativeProbability(7), 0.0000001);
        assertEquals(0.9734245654651915, dist.cumulativeProbability(8), 0.0000001);
        assertEquals(0.9994632167942505, dist.cumulativeProbability(9), 0.0000001);
        assertEquals(1.0, dist.cumulativeProbability(10), 0.0000001);
    }

    @Test
    public void testNormalApproximateProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(0.13533528323066488, dist.probability(1), 0.0000001);
        assertEquals(0.2494836462468651, dist.cumulativeProbability(1), 0.0000001);
        assertEquals(0.2494836462468651, dist.normalApproximateProbability(1), 0.0000001);

        // test for larger values
        assertEquals(0.6826894921373947, dist.probability(5), 0.0000001);
        assertEquals(0.6826894921373947, dist.cumulativeProbability(5), 0.0000001);
        assertEquals(0.6826894921373947, dist.normalApproximateProbability(5), 0.0000001);

        // test for smaller values
        assertEquals(0.0, dist.probability(-1), 0.0);
        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0);
        assertEquals(0.0, dist.normalApproximateProbability(-1), 0.0);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        int sample = dist.sample();
        assertEquals(2, sample);

        PoissonDistribution dist2 = new PoissonDistributionImpl(0.5);
        sample = dist2.sample();
        assertEquals(0, sample);

        PoissonDistribution dist3 = new PoissonDistributionImpl(10.0);
        sample = dist3.sample();
        assertEquals(7, sample);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        double p = 0.1;
        double result = dist.inverseCumulativeProbability(p);
        assertEquals(1, result, 0.0);

        p = 0.2494836462468651;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(1, result, 0.0);

        p = 0.6826894921373947;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(2, result, 0.0);

        p = 0.9994632167942505;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(4, result, 0.0);

        p = 0.9734245654651915;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(5, result, 0.0);

        p = 0.95;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(6, result, 0.0);

        p = 0.5;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(1, result, 0.0);

        p = 0.01;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(0, result, 0.0);

        p = 0.0000001;
        result = dist.inverseCumulativeProbability(p);
        assertEquals(0, result, 0.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegativeProbability() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        dist.inverseCumulativeProbability(-0.1);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityProbabilityGreaterThanOne() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        dist.inverseCumulativeProbability(1.1);
    }

    @Test
    public void testGetMean() {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, dist.getMean(), 0.0);
    }

    @Test
    public void testCumulativeProbabilityWithEpsilon() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5, 0.00000001, 10000000);
        assertEquals(0.04225825377261458, dist.cumulativeProbability(0), 0.00000001);
    }

    @Test
    public void testCumulativeProbabilityWithMaxIterations() throws MathException {
        PoissonDistribution dist = new PoissonDistributionImpl(2.5, 0.0000001, 10);
        assertEquals(0.04225825377261458, dist.cumulativeProbability(0), 0.0000001);
    }
}