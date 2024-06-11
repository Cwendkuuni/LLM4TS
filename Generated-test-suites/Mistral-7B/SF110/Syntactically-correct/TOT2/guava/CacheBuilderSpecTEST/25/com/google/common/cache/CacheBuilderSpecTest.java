import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, weakValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=30s";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals(10, spec.initialCapacity);
        assertEquals(50, spec.maximumSize);
        assertEquals(100, spec.maximumWeight);
        assertEquals(5, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(Duration.ONE_DAY, spec.accessExpirationTimeUnit);
        assertEquals(Duration.HOURS, spec.writeExpirationTimeUnit);
        assertEquals(Duration.SECONDS, spec.refreshTimeUnit);
        assertEquals(1, spec.writeExpirationDuration);
        assertEquals(2, spec.writeExpirationDuration * 60);
        assertEquals(30, spec.refreshDuration);
    }
}