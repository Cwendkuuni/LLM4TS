import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionImpl;
import org.apache.commons.math3.special.Beta;
import org.junit.Assert;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testFDistributionImpl() {
        // Test constructor with valid parameters
        FDistribution distribution = new FDistributionImpl(2.0, 3.0);
        Assert.assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
        Assert.assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);

        // Test cumulativeProbability method
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            double cdf = distribution.cumulativeProbability(x);
            Assert.assertFalse(Double.isNaN(cdf));
            Assert.assertTrue(cdf >= 0.0 && cdf <= 1.0);
        }

        // Test inverseCumulativeProbability method
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double x = distribution.inverseCumulativeProbability(p);
            Assert.assertFalse(Double.isNaN(x));
            Assert.assertTrue(x > 0.0);
        }

        try {
            // Test inverseCumulativeProbability method with invalid probability
            distribution.inverseCumulativeProbability(-0.1);
            Assert.fail();
        } catch (MathException e) {
            // Expected exception
        }

        try {
            // Test inverseCumulativeProbability method with probability > 1
            distribution.inverseCumulativeProbability(1.1);
            Assert.fail();
        } catch (MathException e) {
            // Expected exception
        }

        // Test getDomainLowerBound, getDomainUpperBound and getInitialDomain methods
        for (double p = 0.0; p <= 1.0; p += 0.1) {
            double lowerBound = distribution.getDomainLowerBound(p);
            double upperBound = distribution.getDomainUpperBound(p);
            double initialDomain = distribution.getInitialDomain(p);

            Assert.assertEquals(0.0, lowerBound, 0.0);
            Assert.assertEquals(Double.MAX_VALUE, upperBound, 0.0);
            Assert.assertFalse(Double.isNaN(initialDomain));
            Assert.assertTrue(initialDomain > 0.0);
        }
    }
}