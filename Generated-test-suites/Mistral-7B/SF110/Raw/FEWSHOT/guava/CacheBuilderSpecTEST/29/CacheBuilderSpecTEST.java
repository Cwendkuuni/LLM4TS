package com.google.common.cache;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.*;
import java.util.*;
import com.google.common.collect.*;
import com.google.common.base.*;
import javax.annotation.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_EmptySpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
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
        assertEquals("", spec.specification);
    }

    @Test
    public void testParse_InitialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertEquals(Integer.valueOf(10), spec.initialCapacity);
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
        assertEquals("initialCapacity=10", spec.specification);
    }

    @Test
    public void testParse_MaximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertEquals(Long.valueOf(100), spec.maximumSize);
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
        assertEquals("maximumSize=100", spec.specification);
    }

    @Test
    public void testParse_MaximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=200");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertEquals(Long.valueOf(200), spec.maximumWeight);
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
        assertEquals("maximumWeight=200", spec.specification);
    }

    @Test
    public void testParse_ConcurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=5");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertEquals(Integer.valueOf(5), spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("concurrencyLevel=5", spec.specification);
    }

    @Test
    public void testParse_WeakKeys() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("weakKeys", spec.specification);
    }

    @Test
    public void testParse_SoftValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertNull(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("softValues", spec.specification);
    }

    @Test
    public void testParse_WeakValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
        assertNull(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("weakValues", spec.specification);
    }

    @Test
    public void testParse_RecordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
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
        assertTrue(spec.recordStats);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("recordStats", spec.specification);
    }

    @Test
    public void testParse_ExpireAfterAccess() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=30s");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(30, spec.accessExpirationDuration);
        assertEquals(TimeUnit.SECONDS, spec.accessExpirationTimeUnit);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("expireAfterAccess=30s", spec.specification);
    }

    @Test
    public void testParse_ExpireAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=1h");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertEquals(3600, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("expireAfterWrite=1h", spec.specification);
    }

    @Test
    public void testParse_RefreshAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=5m");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertEquals(5, spec.refreshDuration);
        assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
        assertEquals("refreshAfterWrite=5m", spec.specification);
    }

    @Test
    public void testParse_MultipleOptions() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=50,expireAfterAccess=1d,expireAfterWrite=1h,recordStats,weakKeys,softValues");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertEquals(Integer.valueOf(10), spec.initialCapacity);
        assertEquals(Long.valueOf(50), spec.maximumSize);
        assertEquals(1, spec.accessExpirationDuration);
        assertEquals(TimeUnit.DAYS, spec.accessExpirationTimeUnit);
        assertEquals(3600, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertTrue(spec.recordStats);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("initialCapacity=10,maximumSize=50,expireAfterAccess=1d,expireAfterWrite=1h,recordStats,weakKeys,softValues", spec.specification);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidFormat() {
        CacheBuilderSpec.parse("expireAfterWrite=30d,hours");
    }
}