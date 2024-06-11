package com.google.common.cache;

import static org.junit.Assert.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_emptySpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_initialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        assertEquals(10, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_maximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10");
        assertEquals(0, spec.initialCapacity);
        assertEquals(10L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_maximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=10");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(10L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_concurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=10");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(10, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_weakKeys() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_softValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_weakValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_recordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats=true");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertTrue(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_writeExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=5s");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertEquals(5, spec.writeExpirationDuration);
        assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_writeExpirationWithTimeUnit() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=5h");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertEquals(5 * 60 * 60, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_accessExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=5s");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertEquals(5, spec.accessExpirationDuration);
        assertEquals(TimeUnit.SECONDS, spec.accessExpirationTimeUnit);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_accessExpirationWithTimeUnit() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=5h");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertEquals(5 * 60 * 60, spec.accessExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_refreshAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=5s");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertEquals(5, spec.refreshDuration);
        assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
    }

    @Test
    public void testParse_refreshAfterWriteWithTimeUnit() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=5h");
        assertEquals(0, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertEquals(5 * 60 * 60, spec.refreshDuration);
        assertEquals(TimeUnit.HOURS, spec.refreshTimeUnit);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
    }
}