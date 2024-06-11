import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    @Test
    public void testParseEmptySpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParseValidSpec() {
        String specification = "initialCapacity=5,maximumSize=10,maximumWeight=5,concurrencyLevel=3,weakKeys,softValues,recordStats,expireAfterAccess=1h,expireAfterWrite=2d,refreshAfterWrite=30s";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(specification);
        assertEquals(5, spec.initialCapacity);
        assertEquals(10, spec.maximumSize);
        assertEquals(5, spec.maximumWeight);
        assertEquals(3, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(3600, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertEquals(3600, spec.accessExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
        assertEquals(30000, spec.refreshDuration);
        assertEquals(TimeUnit.MILLISECONDS, spec.refreshTimeUnit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidSpec() {
        String specification = "invalidKey=5,maximumSize=10,maximumWeight=5,concurrencyLevel=3,weakKeys,softValues,recordStats,expireAfterAccess=1h,expireAfterWrite=2d,refreshAfterWrite=30s,invalidKeyValue=abc";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(specification);
    }
}