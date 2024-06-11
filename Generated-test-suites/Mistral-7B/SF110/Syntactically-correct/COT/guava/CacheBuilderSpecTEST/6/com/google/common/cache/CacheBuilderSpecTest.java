package com.example.cachebuilder; // replace with your test package

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");

        assertEquals(10, spec.initialCapacity);
        assertEquals(50L, spec.maximumSize);
        assertEquals(100L, spec.maximumWeight);
        assertEquals(5, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(3600000L, spec.accessExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
        assertEquals(86400000L, spec.writeExpirationDuration);
        assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
        assertEquals(30000L, spec.refreshDuration);
        assertEquals(TimeUnit.MILLISECONDS, spec.refreshTimeUnit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() {
        CacheBuilderSpec.parse("invalidKey=10, expireAfterAccess=1d, expireAfterWrite=2d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIntegerValueNotProvided() {
        CacheBuilderSpec.parse("invalidKey=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTimeUnit() {
        CacheBuilderSpec.parse("expireAfterAccess=1w"); // w is not a valid time unit
    }

    @Test(expected = AssertionError.class)
    public void testInvalidKeyStrength() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, unknownStrength=SOFT, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
    }

    @Test(expected = AssertionError.class)
    public void testInvalidValueStrength() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, unknownStrength=SOFT, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        // add assertions for cacheBuilder properties based on your requirements
    }

    @Test
    public void testToString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
        assertEquals("CacheBuilderSpec{toParsableString='initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s'}", spec.toString());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
        CacheBuilderSpec spec3 = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=31s");

        assertTrue(spec1.equals(spec2));
        assertFalse(spec1.equals(spec3));
    }
}