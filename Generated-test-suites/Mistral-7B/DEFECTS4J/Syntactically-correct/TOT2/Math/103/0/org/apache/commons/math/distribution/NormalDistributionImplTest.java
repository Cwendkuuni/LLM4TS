import org.junit.Test;
import org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testConstructor() {
        NormalDistributionImpl dist = new NormalDistributionImpl(1.0, 2.0);
        assertEquals("Mean should be set correctly", 1.0, dist.getMean(), 0.001);
        assertEquals("Standard deviation should be set correctly", 2.0, dist.getStandardDeviation(), 0.001);
    }
}