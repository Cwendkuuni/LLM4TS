import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class PoissonDistributionImplTest {

    @Test
    public void testConstructors() {
        PoissonDistributionImpl pd1 = new PoissonDistributionImpl(1.5);
        PoissonDistributionImpl pd2 = new PoissonDistributionImpl(5.0);
        PoissonDistributionImpl pd3 = new PoissonDistributionImpl(5.0, 1E-10);
        PoissonDistributionImpl pd4 = new PoissonDistributionImpl(5.0, 1000000);

        assertEquals(1.5, pd1.getMean(), 0.001);
        assertEquals(5.0, pd2.getMean(), 0.001);
        assertEquals(5.0, pd3.getMean(), 0.001);
        assertEquals(5.0, pd4.getMean(), 0.001);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalArgumentConstructor() {
        new PoissonDistributionImpl(-1.0);
    }

    @Test
    public void testProbability() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);

        // test probability for non-negative integers
        assertEquals(0.07377961956, pd.probability(0), 0.0000001);
        assertEquals(0.10615437517, pd.probability(1), 0.0000001);
        assertEquals(0.13281264391, pd.probability(2), 0.0000001);

        // test probability for negative integer and maximum value
        assertEquals(0.0, pd.probability(-1), 0.0);
        assertEquals(1.0, pd.probability(Integer.MAX_VALUE), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);

        // test cumulative probability for non-negative integers
        assertEquals(0.0, pd.cumulativeProbability(-1), 0.0);
        assertEquals(0.07377961956, pd.cumulativeProbability(0), 0.0000001);
        assertEquals(0.20273304101, pd.cumulativeProbability(2), 0.0000001);
        assertEquals(1.0, pd.cumulativeProbability(Integer.MAX_VALUE), 0.0);

        // test cumulative probability for large values using normal approximation
        double x = 100.0;
        double y = pd.normalApproximateProbability((int) x);
        assertEquals(pd.cumulativeProbability((int) x), y, 0.001);
    }

    @Test
    public void testSample() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);

        for (int i = 0; i < 100; i++) {
            int s = pd.sample();
            assertTrue(s >= 0);
            assertTrue(s <= Integer.MAX_VALUE);
            assertEquals(pd.probability(s), pd.cumulativeProbability(s), 0.01);
        }
    }

    @Test(expected = MathException.class)
    public void testMathExceptionSample() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(-1.0);
        pd.sample();
    }

    @Test
    public void testGetDomainLowerBound() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.0);
        assertEquals(0, pd.getDomainLowerBound(0.1));
    }

    @Test
    public void testGetDomainUpperBound() {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(1.0);
        assertEquals(Integer.MAX_VALUE, pd.getDomainUpperBound(0.1));
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        PoissonDistributionImpl pd = new PoissonDistributionImpl(3.0);

        // test inverseCumulativeProbability for small probabilities
        double p = 0.01;
        int x = pd.inverseCumulativeProbability(p);
        assertEquals(0, x);
        assertEquals(Precision.isClose(p, pd.probability(0), 0.001), true);

        // test inverseCumulativeProbability for large probabilities
        p = 0.99;
        x = pd.inverseCumulativeProbability(p);
        assertEquals(Integer.MAX_VALUE, x);
        assertEquals(Precision.isClose(p, pd.probability(Integer.MAX_VALUE), 0.001), true);

        // test inverseCumulativeProbability for intermediate probabilities
        p = 0.5;
        x = pd.inverseCumulativeProbability(p);
        assertEquals(1, x);
        assertEquals(Precision.isClose(p, pd.probability(1), 0.001), true);
    }
}