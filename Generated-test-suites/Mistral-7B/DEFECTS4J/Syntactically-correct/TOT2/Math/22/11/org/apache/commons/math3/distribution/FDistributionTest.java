import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionConstructionWithValidParameters() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertEquals(numeratorDegreesOfFreedom, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionWithInvalidNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = -1.0;
        double denominatorDegreesOfFreedom = 7.0;
        try {
            new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionWithInvalidDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = -1.0;
        try {
            new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double inverseCumulativeProbability = 0.1;
        double x = fd.inverseCumulativeProbability(inverseCumulativeProbability);
        double expectedX = 0.5 * (numeratorDegreesOfFreedom / denominatorDegreesOfFreedom) *
                (1.0 - (Math.sqrt(1.0 + 4.0 * numeratorDegreesOfFreedom / denominatorDegreesOfFreedom) * inverseCumulativeProbability));
        assertEquals(expectedX, x, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityOutOfRange() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double inverseCumulativeProbability = 1.1;
        fd.inverseCumulativeProbability(inverseCumulativeProbability);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testDensity() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 3.0;
        double density = fd.density(x);
        double expectedDensity = Math.pow(x, 1.5) / ((Math.pow(numeratorDegreesOfFreedom, 1.5) * Math.pow(denominatorDegreesOfFreedom, 1.5)) * Math.gamma(numeratorDegreesOfFreedom) * Math.gamma(denominatorDegreesOfFreedom));
        assertEquals(expectedDensity, density, 0.01);
    }

    @Test
    public void testCumulativeProbability() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double x = 3.0;
        double cumulativeProbability = fd.cumulativeProbability(x);
        double expectedCumulativeProbability = Beta.regularizedBeta((numeratorDegreesOfFreedom * x) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom * x), 0.5 * numeratorDegreesOfFreedom, 0.5 * denominatorDegreesOfFreedom);
        assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.01);
    }

    @Test
    public void testGetNumericalMean() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double numericalMean = fd.getNumericalMean();
        double expectedNumericalMean = numeratorDegreesOfFreedom / (numeratorDegreesOfFreedom - 2);
        assertEquals(expectedNumericalMean, numericalMean, 0.01);
    }

    @Test
    public void testGetNumericalVariance() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        double numericalVariance = fd.getNumericalVariance();
        double expectedNumericalVariance = (2 * Math.pow(denominatorDegreesOfFreedom, 2) * (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) / ((numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) * (denominatorDegreesOfFreedom - 4)));
        assertEquals(expectedNumericalVariance, numericalVariance, 0.01);
    }
}