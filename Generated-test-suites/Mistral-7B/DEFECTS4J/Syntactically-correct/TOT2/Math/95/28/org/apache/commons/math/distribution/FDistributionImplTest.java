import org.apache.commons.math.distribution.FDistribution;
import org.apache.commons.math.distribution.FDistributionImpl;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testConstructor() {
        FDistribution distribution = new FDistributionImpl(5.0, 10.0);
        assertNotNull(distribution);
    }
}