import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, weakValues, recordStats, expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1m";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals(10, cacheBuilderSpec.getInitialCapacity());
        assertEquals(20, cacheBuilderSpec.getMaximumSize());
        assertEquals(30, cacheBuilderSpec.getMaximumWeight());
        assertEquals(5, cacheBuilderSpec.getConcurrencyLevel());
        assertTrue(cacheBuilderSpec.getKeyStrength() == LocalCache.Strength.WEAK);
        assertTrue(cacheBuilderSpec.getValueStrength() == LocalCache.Strength.WEAK);
        assertTrue(cacheBuilderSpec.isRecordStats());
        assertEquals(1, cacheBuilderSpec.getWriteExpirationTimeUnit().toHours(cacheBuilderSpec.getWriteExpirationDuration()));
        assertEquals(1, cacheBuilderSpec.getAccessExpirationTimeUnit().toHours(cacheBuilderSpec.getAccessExpirationDuration()));
        assertEquals(1, cacheBuilderSpec.getRefreshTimeUnit().toMinutes(cacheBuilderSpec.getRefreshDuration()));
    }
}