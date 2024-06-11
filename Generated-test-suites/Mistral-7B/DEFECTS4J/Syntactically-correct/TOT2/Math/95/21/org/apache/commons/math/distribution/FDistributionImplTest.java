import org.apache.commons.math.distribution.FDistributionImpl;
import org.apache.commons.math.special.Beta;
import org.junit.Test;

public class FDistributionImplTest {

    @Test
    public void testConstructor() {
        FDistributionImpl distribution = new FDistributionImpl(5.0, 10.0);
        assert (distribution.getNumeratorDegreesOfFreedom() == 5.0);
        assert (distribution.getDenominatorDegreesOfFreedom() == 10.0);
    }
}