package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import java.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;

@Beta
public final class CacheBuilderSpecTest {

    @Test
    public void testParse_EmptySpec() {
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
    public void testParse_InitialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        assertEquals(10, spec.initialCapacity);
        assertEquals(null, spec.maximumSize);
        assertEquals(null, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(null, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_MaximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=50");
        assertNull(spec.initialCapacity);
        assertEquals(50, spec.maximumSize);
        assertEquals(null, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(null, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_MaximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=100");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertEquals(100, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(null, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_ConcurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=5");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertEquals(5, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(null, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_WeakKeys() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(null, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_SoftValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_WeakValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_RecordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertEquals(true, spec.recordStats);
        assertEquals(null, spec.valueStrength);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_WriteExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=1h");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(1L, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_AccessExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1m");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(1L, spec.accessExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_RefreshExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=1d");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(1L, spec.refreshDuration);
        assertEquals(TimeUnit.DAYS, spec.refreshTimeUnit);
    }
}