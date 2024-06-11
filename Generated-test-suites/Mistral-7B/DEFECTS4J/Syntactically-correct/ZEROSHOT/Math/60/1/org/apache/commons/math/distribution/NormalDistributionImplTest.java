import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionCreation() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0);
        assertEquals(1, dist.getStandardDeviation(), 0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionCreationNegativeSD() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testDensity() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double density = dist.density(0);
        assertEquals(0.39894228040143273, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = dist.cumulativeProbability(0);
        assertEquals(0.5, p, 0.0001);

        p = dist.cumulativeProbability(2);
        assertEquals(0.9772468549614653, p, 0.0001);

        p = dist.cumulativeProbability(-2);
        assertEquals(0.0227531450385347, p, 0.0001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testInverseCumulativeProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double p = 0.3;
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        assertEquals(-0.8028670028146944, inverseCumulativeProbability, 0.0001);

        p = 0.95;
        inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        assertEquals(0.9758205225747372, inverseCumulativeProbability, 0.0001);

        p = 0.025;
        inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        assertEquals(-1.2815520268522953, inverseCumulativeProbability, 0.0001);

        p = 0;
        inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability);

        p = 1;
        inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityConvergence() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        dist.inverseCumulativeProbability(0.51);
    }

    @Test
    public void testSample() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double sample = dist.sample();
        assertTrue(sample >= -3.0 && sample <= 3.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals(1e-9, dist.getSolverAbsoluteAccuracy(), 1e-10);
    }
}