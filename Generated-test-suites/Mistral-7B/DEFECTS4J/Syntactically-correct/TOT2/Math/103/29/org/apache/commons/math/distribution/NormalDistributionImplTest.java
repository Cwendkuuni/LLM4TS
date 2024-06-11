import org.apache.commons.math.distribution.NormalDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testConstructor() {
        NormalDistribution dist = new NormalDistributionImpl(1.0, 2.0);
        NormalDistribution expected = new NormalDistribution();
        assertEquals(expected.getMean(), dist.getMean(), 0.001);
        assertEquals(expected.getStandardDeviation(), dist.getStandardDeviation(), 0.001);
    }
}