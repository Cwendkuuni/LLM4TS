package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import java.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;

@Beta
public class CacheBuilderSpecTest {

    @Test
    public void testParse_emptySpec_returnsDefaultSpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNull(spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
        assertEquals(LocalCache.Strength.STRONG, spec.valueStrength);
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
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5");
        assertEquals(5, spec.initialCapacity.intValue());
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
        assertEquals(LocalCache.Strength.STRONG, spec.valueStrength);
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
        assertEquals(null, spec.initialCapacity);
        assertEquals(10L, spec.maximumSize.longValue());
        assertEquals(0L, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
        assertEquals(LocalCache.Strength.STRONG, spec.valueStrength);
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
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=20");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(20L, spec.maximumWeight.longValue());
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
        assertEquals(LocalCache.Strength.STRONG, spec.valueStrength);
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
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=3");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(3, spec.concurrencyLevel.intValue());
        assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
        assertEquals(LocalCache.Strength.STRONG, spec.valueStrength);
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
        assertEquals(null, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.STRONG, spec.valueStrength);
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
        assertEquals(null, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
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
        assertEquals(null, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
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
        assertEquals(null, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
        assertEquals(LocalCache.Strength.STRONG, spec.valueStrength);
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
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=3d");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
        assertEquals(LocalCache.Strength.STRONG, spec.valueStrength);
        assertFalse(spec.recordStats);
        assertEquals(3L, spec.writeExpirationDuration);
        assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_accessExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=2h");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
        assertEquals(LocalCache.Strength.STRONG, spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertEquals(2L, spec.accessExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_refreshExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=1h");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0L, spec.maximumSize);
        assertEquals(0L, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
        assertEquals(LocalCache.Strength.STRONG, spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertEquals(1L, spec.refreshDuration);
        assertEquals(TimeUnit.HOURS, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_combinedOptions() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, weakKeys, softValues, expireAfterWrite=3h, expireAfterAccess=2d, refreshAfterWrite=1m");
        assertEquals(10, spec.initialCapacity.intValue());
        assertEquals(50L, spec.maximumSize.longValue());
        assertEquals(100L, spec.maximumWeight.longValue());
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(3L * 60 * 60, spec.writeExpirationDuration); // 3 hours in milliseconds
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertEquals(2 * 24 * 60 * 60, spec.accessExpirationDuration); // 2 days in milliseconds
        assertEquals(TimeUnit.DAYS, spec.accessExpirationTimeUnit);
        assertEquals(1L, spec.refreshDuration);
        assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
    }
}