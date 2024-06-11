import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplWithValidParameters() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        assertEquals("Mean should be equal to 0", 0, dist.getMean(), 0.001);
        assertEquals("Standard deviation should be equal to 1", 1, dist.getStandardDeviation(), 0.001);
        assertEquals("Density at 0 should be equal to 0.3989422804", 0.3989422804, dist.density(0), 0.0001);
        assertEquals("Density at 1 should be equal to 0.3989422804", 0.3989422804, dist.density(1), 0.0001);
        assertEquals("Density at -1 should be equal to 0.3989422804", 0.3989422804, dist.density(-1), 0.0001);
        assertEquals("Cumulative probability at 0 should be equal to 0.5", 0.5, dist.cumulativeProbability(0), 0.001);
        assertEquals("Cumulative probability at 1 should be equal to 0.8413447460", 0.8413447460, dist.cumulativeProbability(1), 0.001);
        assertEquals("Cumulative probability at -1 should be equal to 0.1586552540", 0.1586552540, dist.cumulativeProbability(-1), 0.001);
        // Test inverse cumulative probability
        double p = 0.25;
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        double expectedValue = -0.6744897578;
        assertEquals("Inverse cumulative probability at p=0.25 should be equal to -0.6744897578", expectedValue, inverseCumulativeProbability, 0.001);
        p = 0.75;
        inverseCumulativeProbability = dist.inverseCumulativeProbability(p);
        double expectedValue1 = 0.6744897578;
        assertEquals("Inverse cumulative probability at p=0.75 should be equal to 0.6744897578", expectedValue1, inverseCumulativeProbability, 0.001);
        // Test sampling
        double sample = dist.sample();
        assertEquals("Sample should be a valid NormalDistribution value", dist.getMean() + dist.getStandardDeviation() * FastMath.random(), sample, 0.01);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNormalDistributionImplWithInvalidParameters() {
        new NormalDistributionImpl(-1, 1);
    }

    @Test
    public void testNormalDistributionImplWithZeroStandardDeviation() {
        NormalDistribution dist = new NormalDistributionImpl(0, 0);
        try {
            dist.density(0);
        } catch (ArithmeticException e) {
            // Expected result
            assertEquals("Exception message should be 'Division by zero'", "Division by zero", e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbabilityWithSmallProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(0);
        double expectedValue = Double.NEGATIVE_INFINITY;
        assertEquals("Inverse cumulative probability at p=0 should be equal to -infinity", expectedValue, inverseCumulativeProbability, 0.001);
        inverseCumulativeProbability = dist.inverseCumulativeProbability(1.0E-300);
        expectedValue = Double.NEGATIVE_INFINITY;
        assertEquals("Inverse cumulative probability at p close to 0 should be equal to -infinity", expectedValue, inverseCumulativeProbability, 0.001);
        try {
            inverseCumulativeProbability = dist.inverseCumulativeProbability(-1.0E-300);
        } catch (NumberIsTooSmallException e) {
            // Expected result
            assertEquals("Exception message should be 'p is too small'", "p is too small", e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbabilityWithBigProbability() {
        NormalDistribution dist = new NormalDistributionImpl(0, 1);
        double inverseCumulativeProbability = dist.inverseCumulativeProbability(1);
        double expectedValue = Double.POSITIVE_INFINITY;
        assertEquals("Inverse cumulative probability at p=1 should be equal to infinity", expectedValue, inverseCumulativeProbability, 0.001);
        inverseCumulativeProbability = dist.inverseCumulativeProbability(1.1);
        expectedValue = Double.POSITIVE_INFINITY;
        assertEquals("Inverse cumulative probability at p>1 should be equal to infinity", expectedValue, inverseCumulativeProbability, 0.001);
    }
}