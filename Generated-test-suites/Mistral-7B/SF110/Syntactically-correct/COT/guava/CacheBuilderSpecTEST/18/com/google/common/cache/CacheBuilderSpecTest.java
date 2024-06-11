package com.example.junit;

import com.google.common.cache.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParseWithEmptySpec() {
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
    public void testParseWithValidSpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,weakKeys,softValues,recordStats,expireAfterAccess=30s,expireAfterWrite=1m,refreshAfterWrite=5m");
        assertEquals(5, spec.initialCapacity);
        assertEquals(10, spec.maximumSize);
        assertEquals(20, spec.maximumWeight);
        assertEquals(5, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(30, spec.accessExpirationDuration);
        assertEquals(TimeUnit.SECONDS, spec.accessExpirationTimeUnit);
        assertEquals(60, spec.writeExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.writeExpirationTimeUnit);
        assertEquals(5, spec.refreshDuration);
        assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidSpec() {
        CacheBuilderSpec.parse("invalid=5");
    }

    @Test
    public void testToCacheBuilderWithNullSpec() {
        CacheBuilderSpec spec = null;
        try {
            CacheBuilder<Object, Object> builder = new CacheBuilderSpec(spec).toCacheBuilder();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testToCacheBuilderWithValidSpec() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,weakKeys,softValues,recordStats,expireAfterAccess=30s,expireAfterWrite=1m,refreshAfterWrite=5m");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        // additional assertions based on expected builder configuration
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,weakKeys,softValues,recordStats,expireAfterAccess=30s,expireAfterWrite=1m,refreshAfterWrite=5m");
        assertEquals("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,weakKeys,softValues,recordStats,expireAfterAccess=30s,expireAfterWrite=1m,refreshAfterWrite=5m", spec.toParsableString());
    }

    @Test
    public void testToString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,weakKeys,softValues,recordStats,expireAfterAccess=30s,expireAfterWrite=1m,refreshAfterWrite=5m");
        assertEquals("CacheBuilderSpec{toParsableString='initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,weakKeys,softValues,recordStats,expireAfterAccess=30s,expireAfterWrite=1m,refreshAfterWrite=5m'}", spec.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,weakKeys,softValues,recordStats,expireAfterAccess=30s,expireAfterWrite=1m,refreshAfterWrite=5m");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,weakKeys,softValues,recordStats,expireAfterAccess=30s,expireAfterWrite=1m,refreshAfterWrite=5m");
        CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,weakKeys,softValues,recordStats,expireAfterAccess=30s,expireAfterWrite=1m,refreshAfterWrite=6m");
        assertEquals(spec1, spec2);
        assertNotEquals(spec1, spec3);

        CacheBuilderSpec spec4 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=6,weakKeys,softValues,recordStats,expireAfterAccess=30s,expireAfterWrite=1m,refreshAfterWrite=5m");
        assertNotEquals(spec1, spec4);

        CacheBuilderSpec spec5 = new CacheBuilderSpec();
        assertNotEquals(spec1, spec5);
    }
}