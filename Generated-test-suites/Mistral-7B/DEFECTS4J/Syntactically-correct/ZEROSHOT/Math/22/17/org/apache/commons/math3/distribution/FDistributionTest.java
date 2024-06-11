import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() throws NotStrictlyPositiveException {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fDist.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(3.0, fDist.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(-1.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(0.0, 3.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double x = 1.5;
        double expected = 0.6227884686232236; // calculated using MathWorld formula
        assertEquals(expected, fDist.density(x), 0.0000001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double x = 1.5;
        double expected = 0.938527358298134; // calculated using MathWorld formula
        assertEquals(expected, fDist.cumulativeProbability(x), 0.0000001);
    }

    @Test
    public void testFDistributionNumericalMean() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double expected = 1.5;
        assertEquals(expected, fDist.getNumericalMean(), 0.0);
    }

    @Test
    public void testFDistributionNumericalVariance() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        double expected = 0.75;
        assertEquals(expected, fDist.getNumericalVariance(), 0.0);
    }

    @Test
    public void testFDistributionSupportBounds() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        assertEquals(0.0, fDist.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fDist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testFDistributionSupportInclusive() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        assertTrue(fDist.isSupportLowerBoundInclusive());
        assertFalse(fDist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testFDistributionSupportConnected() {
        FDistribution fDist = new FDistribution(2.0, 3.0);
        assertTrue(fDist.isSupportConnected());
    }
}