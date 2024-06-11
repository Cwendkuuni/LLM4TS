import org.junit.Test;
import org.junit.Assert.*;

public class NormalDistributionImplTest {
    @Test
    public void testNormalDistributionImplConstructor() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(2.5, 1.5);
        assertEquals(2.5, normalDistribution.getMean(), 0.01);
        assertEquals(1.5, normalDistribution.getStandardDeviation(), 0.01);
    }
}