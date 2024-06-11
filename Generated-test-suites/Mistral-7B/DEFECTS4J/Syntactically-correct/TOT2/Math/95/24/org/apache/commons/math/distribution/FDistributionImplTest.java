import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl_constructors() {
        FDistributionImpl distribution = new FDistributionImpl(1.0, 1.0);
        assertNotNull(distribution);
        assertEquals(1.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(1.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testCumulativeProbability_validArguments() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(1.0, 1.0);
        double result = distribution.cumulativeProbability(1.0);
        assertEquals(0.5, result, 1e-6);
    }

    @Test
    public void testCumulativeProbability_negatives() throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(1.0, 1.0);
        double result = distribution.cumulativeProbability(-1.0);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testInverseCumulativeProbability_validProbability()
        throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(1.0, 1.0);
        double result = distribution.inverseCumulativeProbability(0.5);
        assertEquals(1.0, result, 1e-6);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_invalidProbability()
        throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(1.0, 1.0);
        distribution.inverseCumulativeProbability(-0.5);
    }

    @Test
    public void testInverseCumulativeProbability_largeProbability()
        throws MathException {
        FDistributionImpl distribution = new FDistributionImpl(1.0, 1.0);
        double result = distribution.inverseCumulativeProbability(1.0);
        double expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, result, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNumeratorDegreesOfFreedom_negative() {
        FDistributionImpl distribution = new FDistributionImpl(1.0, 1.0);
        distribution.setNumeratorDegreesOfFreedom(-1.0);
    }
}