import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.distribution.PoissonDistributionImpl;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructorWithValidArguments() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(2.5);
        assertEquals(2.5, poissonDistribution.getMean(), 0.001);
    }

    @Test
    public void testProbabilityMethod() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        double probability = poissonDistribution.probability(2);
        assertEquals(0.04213189384587158, probability, 0.0001);

        probability = poissonDistribution.probability(10);
        assertEquals(0.006222338203183745, probability, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityMethod() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(2);
        assertEquals(0.0498337847856264, cumulativeProbability, 0.0001);

        cumulativeProbability = poissonDistribution.cumulativeProbability(10);
        assertEquals(0.9533506418118722, cumulativeProbability, 0.0001);
    }

    @Test
    public void testNormalApproximateProbabilityMethod() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        double normalApproximateProbability = poissonDistribution.normalApproximateProbability(2);
        assertEquals(0.04979924640002633, normalApproximateProbability, 0.0001);

        normalApproximateProbability = poissonDistribution.normalApproximateProbability(10);
        assertEquals(0.953331248611568, normalApproximateProbability, 0.0001);
    }

    @Test
    public void testSampleMethod() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        int sample = poissonDistribution.sample();
        assertEquals(2, sample);

        sample = poissonDistribution.sample();
        assertEquals(5, sample);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidArgument() {
        new PoissonDistributionImpl(-2.5);
    }

    @Test
    public void testGetDomainLowerBoundMethod() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        assertEquals(0, poissonDistribution.getDomainLowerBound(0.0));
    }

    @Test
    public void testGetDomainUpperBoundMethod() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        assertEquals(Integer.MAX_VALUE, poissonDistribution.getDomainUpperBound(1.0));
    }

    @Test
    public void testInverseCumulativeProbabilityMethod() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        double inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(0.0498337847856264);
        assertEquals(2, inverseCumulativeProbability, 0.01);

        inverseCumulativeProbability = poissonDistribution.inverseCumulativeProbability(0.9533506418118722);
        assertEquals(10, inverseCumulativeProbability, 0.01);
    }

    @Test
    public void testCumulativeProbabilityWithLargeValues() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(100000.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(100000);
        assertEquals(1.0, cumulativeProbability, 0.001);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeValues() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        try {
            poissonDistribution.cumulativeProbability(-1);
            fail("Expected MathException for negative argument");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testCumulativeProbabilityWithOutOfRangeValues() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        try {
            poissonDistribution.cumulativeProbability(Integer.MIN_VALUE);
            fail("Expected MathException for out of range argument");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testProbabilityWithNegativeValues() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        try {
            poissonDistribution.probability(-1);
            fail("Expected IllegalArgumentException for negative argument");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testProbabilityWithOutOfRangeValues() {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        try {
            poissonDistribution.probability(Integer.MIN_VALUE);
            fail("Expected IllegalArgumentException for out of range argument");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSampleWithNegativeValues() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        try {
            poissonDistribution.sample();
            fail("Expected MathException for negative argument");
        } catch (MathRuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testSampleWithOutOfRangeValues() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        try {
            poissonDistribution.sample(Integer.MAX_VALUE);
            fail("Expected MathException for out of range argument");
        } catch (MathRuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testInverseCumulativeProbabilityWithNegativeValues() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        try {
            poissonDistribution.inverseCumulativeProbability(-0.1);
            fail("Expected MathException for negative argument");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testInverseCumulativeProbabilityWithOutOfRangeValues() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(3.0);
        try {
            poissonDistribution.inverseCumulativeProbability(1.1);
            fail("Expected MathException for out of range argument");
        } catch (MathException e) {
            // Expected
        }
    }

    @Test
    public void testCumulativeProbabilityConvergence() throws Exception {
        PoissonDistributionImpl poissonDistribution = new PoissonDistributionImpl(1000000.0);
        double cumulativeProbability = poissonDistribution.cumulativeProbability(1000000);
        assertEquals(1.0, cumulativeProbability, 0.0001);

        // Check convergence with normal approximation
        double normalApproximateCumulativeProbability = 0.0;
        for (int i = 0; i < 10000; i++) {
            normalApproximateCumulativeProbability += poissonDistribution.normalApproximateProbability(i);
        }
        assertEquals(1.0, normalApproximateCumulativeProbability, 0.01);
    }
}