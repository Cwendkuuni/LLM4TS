import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstruction() {
        NormalDistributionImpl nd = new NormalDistributionImpl(0, 1);
        assertEquals(0, nd.getMean(), 0.001);
        assertEquals(1, nd.getStandardDeviation(), 0.001);

        NormalDistributionImpl nd2 = new NormalDistributionImpl(2, 3);
        assertEquals(2, nd2.getMean(), 0.001);
        assertEquals(3, nd2.getStandardDeviation(), 0.001);

        try {
            new NormalDistributionImpl(-1, 2);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        try {
            new NormalDistributionImpl(0, -2);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }

    @Test
    public void testNormalDistributionImplMethods() {
        NormalDistributionImpl nd = new NormalDistributionImpl(0, 1);

        double density = nd.density(0.5);
        assertEquals(0.1543083357256928, density, 0.00001);

        double cumulativeProbability = 0.5;
        double x = nd.inverseCumulativeProbability(cumulativeProbability);
        assertEquals(0, x, 0.001);

        cumulativeProbability = 0.999;
        x = nd.inverseCumulativeProbability(cumulativeProbability);
        assertEquals(3.044668336886628, x, 0.01);

        cumulativeProbability = 0.1;
        x = nd.inverseCumulativeProbability(cumulativeProbability);
        assertEquals(-1.0, x, 0.01);

        cumulativeProbability = 0.9;
        x = nd.inverseCumulativeProbability(cumulativeProbability);
        assertEquals(0.8416216602881157, x, 0.01);

        double sample = nd.sample();
        assertEquals(0, sample, 0.01);

        NormalDistributionImpl nd2 = new NormalDistributionImpl(1, 2);
        sample = nd2.sample();
        assertEquals(3.416216602881157, sample, 0.01);
    }
}