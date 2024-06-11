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
    public void testParse_emptySpec() {
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
    public void testParse_initialCapacity() {
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
    public void testParse_maximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=500");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertEquals(Long.valueOf(500), spec.maximumSize);
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
        assertEquals("maximumSize=500", spec.specification);
    }

    @Test
    public void testParse_maximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=1000");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertEquals(Long.valueOf(1000), spec.maximumWeight);
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
        assertEquals("maximumWeight=1000", spec.specification);
    }

    @Test
    public void testParse_concurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=3");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertEquals(Integer.valueOf(3), spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("concurrencyLevel=3", spec.specification);
    }

    @Test
    public void testParse_weakKeys() {
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
    public void testParse_softValues() {
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
    public void testParse_weakValues() {
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
    public void testParse_recordStats() {
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
    public void testParse_expireAfterAccess() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1d");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(1L, spec.accessExpirationDuration.longValue());
        assertEquals(TimeUnit.DAYS, spec.accessExpirationTimeUnit);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("expireAfterAccess=1d", spec.specification);
    }

    @Test
    public void testParse_expireAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=5s");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(5L, spec.writeExpirationDuration.longValue());
        assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
        assertEquals("expireAfterWrite=5s", spec.specification);
    }

    @Test
    public void testParse_refreshAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=1m");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(1L, spec.refreshDuration.longValue());
        assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertEquals("refreshAfterWrite=1m", spec.specification);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=500,concurrencyLevel=3,weakKeys,expireAfterAccess=1d,expireAfterWrite=5s,refreshAfterWrite=1m");
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        assertEquals(10, cacheBuilder.initialCapacity());
        assertEquals(500, cacheBuilder.maximumSize());
        assertEquals(3, cacheBuilder.concurrencyLevel());
        assertTrue(cacheBuilder.weakKeys());
        assertTrue(cacheBuilder.recordStats());
        assertEquals(1, cacheBuilder.expireAfterAccessInMillis());
        assertEquals(5, cacheBuilder.expireAfterWriteCauseEvictionInMillis());
        assertEquals(1, cacheBuilder.refreshAfterWriteInMillis());
        assertFalse(cacheBuilder.expireAfterWriteNanos());
        assertFalse(cacheBuilder.recordStats());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=500,concurrencyLevel=3,weakKeys,expireAfterAccess=1d,expireAfterWrite=5s,refreshAfterWrite=1m");
        assertEquals("initialCapacity=10,maximumSize=500,concurrencyLevel=3,weakKeys,expireAfterAccess=1d,expireAfterWrite=5s,refreshAfterWrite=1m", spec.toParsableString());
    }

    @Test
    public void testToString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=500,concurrencyLevel=3,weakKeys,expireAfterAccess=1d,expireAfterWrite=5s,refreshAfterWrite=1m");
        assertEquals("CacheBuilderSpec{specification='initialCapacity=10,maximumSize=500,concurrencyLevel=3,weakKeys,expireAfterAccess=1d,expireAfterWrite=5s,refreshAfterWrite=1m'}", spec.toString());
    }

    @Test
    public void testHashCodeEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=500,concurrencyLevel=3,weakKeys,expireAfterAccess=1d,expireAfterWrite=5s,refreshAfterWrite=1m");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10,maximumSize=500,concurrencyLevel=3,weakKeys,expireAfterAccess=1d,expireAfterWrite=5s,refreshAfterWrite=1m");
        CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=10,maximumSize=500,concurrencyLevel=3,weakKeys,expireAfterAccess=1d,expireAfterWrite=6s,refreshAfterWrite=1m");

        assertEquals(spec1, spec1);
        assertEquals(spec1, spec2);
        assertNotEquals(spec1, spec3);
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
}