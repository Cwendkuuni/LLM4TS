import static org.junit.Assert.*;
import static com.google.common.cache.CacheBuilderSpec.*;
import static com.google.common.collect.ImmutableMap.*;
import static com.google.common.base.Preconditions.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParseStandardCacheSpec() {
        String cacheBuilderSpec = "initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterWrite=1m, expireAfterAccess=5m, refreshAfterWrite=1h";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);

        assertEquals(5, spec.initialCapacity);
        assertEquals(10, spec.maximumSize);
        assertEquals(3, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(1, spec.writeExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.writeExpirationTimeUnit);
        assertEquals(5, spec.accessExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
        assertEquals(1, spec.refreshDuration);
        assertEquals(TimeUnit.HOURS, spec.refreshTimeUnit);
    }

    @Test
    public void testParseEmptySpec() {
        String cacheBuilderSpec = "";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);
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
    public void testParseMissingKey() {
        String cacheBuilderSpec = "initialCapacity=5, maximumSize, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterWrite=1m, expireAfterAccess=5m, refreshAfterWrite=1h";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpec);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unknown key: maximumSize", e.getMessage());
        }
    }

    @Test
    public void testParseInvalidIntegerKey() {
        String cacheBuilderSpec = "initialCapacity=five, maximumSize=10, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterWrite=1m, expireAfterAccess=5m, refreshAfterWrite=1h";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpec);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid integer value for key initialCapacity", e.getMessage());
        }
    }

    @Test
    public void testParseInvalidLongKey() {
        String cacheBuilderSpec = "initialCapacity=5, maximumSize=10, concurrencyLevel=three, weakKeys, softValues, recordStats, expireAfterWrite=1m, expireAfterAccess=5m, refreshAfterWrite=1h";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpec);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid integer value for key concurrencyLevel", e.getMessage());
        }
    }

    @Test
    public void testParseInvalidDuration() {
        String cacheBuilderSpec = "initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterWrite=invalidDuration, expireAfterAccess=5m, refreshAfterWrite=1h";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpec);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid duration format for key expireAfterWrite", e.getMessage());
        }
    }

    @Test
    public void testParseInvalidTimeUnit() {
        String cacheBuilderSpec = "initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterWrite=1d, expireAfterAccess=5m, refreshAfterWrite=invalidTimeUnit";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpec);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid time unit for key refreshAfterWrite", e.getMessage());
        }
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterWrite=1m, expireAfterAccess=5m, refreshAfterWrite=1h");
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        assertEquals(5, cacheBuilder.initialCapacity());
        assertEquals(10, cacheBuilder.maximumSize());
        assertEquals(3, cacheBuilder.concurrencyLevel());
        assertEquals(Cache.Builder.WeakKeysPolicy.WEAK, cacheBuilder.weakKeys());
        assertEquals(Cache.Builder.WeakValuesPolicy.WEAK, cacheBuilder.weakValues());
        assertTrue(cacheBuilder.recordStats());
        assertEquals(1, cacheBuilder.expireAfterWriteInMillis());
        assertEquals(1, cacheBuilder.expireAfterAccessInMillis());
        assertEquals(1 * 60 * 60 * 1000, cacheBuilder.refreshAfterWriteInMillis());
    }
}