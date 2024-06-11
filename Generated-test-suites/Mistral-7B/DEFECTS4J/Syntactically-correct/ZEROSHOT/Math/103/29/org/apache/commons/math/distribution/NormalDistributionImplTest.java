import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Assert;
import org.junit.Test;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImpl() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        Assert.assertEquals(0.0, normalDistribution.getMean(), 0.0);
        Assert.assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testNormalDistributionImplWithCustomMeanAndStandardDeviation() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(2.5, 0.5);
        Assert.assertEquals(2.5, normalDistribution.getMean(), 0.0);
        Assert.assertEquals(0.5, normalDistribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStandardDeviation() {
        new NormalDistributionImpl(0, -1);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        double pdfValue;
        double expectedCumulativeProbability = 0.0;
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            pdfValue = normalDistribution.probabilityDensityFunction(x);
            expectedCumulativeProbability += pdfValue;
            double actualCumulativeProbability = normalDistribution.cumulativeProbability(x);
            Assert.assertEquals(expectedCumulativeProbability, actualCumulativeProbability, 0.001);
        }
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRange() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        normalDistribution.cumulativeProbability(Double.POSITIVE_INFINITY);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        double probability = 0.15;
        double expectedValue = 0.75;
        double actualValue = normalDistribution.inverseCumulativeProbability(probability);
        Assert.assertEquals(expectedValue, actualValue, 0.001);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityNegativeProbability() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        normalDistribution.inverseCumulativeProbability(-0.1);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbabilityGreaterThanOne() throws MathException {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        normalDistribution.inverseCumulativeProbability(1.1);
    }

    @Test
    public void testProbabilityDensityFunction() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        double pdfValue;
        for (double x = -5.0; x <= 5.0; x += 0.1) {
            pdfValue = normalDistribution.probabilityDensityFunction(x);
            Assert.assertTrue(pdfValue > 0.0);
        }
    }

    @Test
    public void testProbabilityDensityFunctionOutOfRange() {
        NormalDistribution normalDistribution = new NormalDistributionImpl(0, 1);
        double pdfValue = normalDistribution.probabilityDensityFunction(Double.POSITIVE_INFINITY);
        Assert.assertEquals(0.0, pdfValue, 0.0);
    }
}