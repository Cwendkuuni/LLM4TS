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
    public void testParse_emptySpec_returnsDefaultSpec() {
        CacheBuilderSpec defaultSpec = CacheBuilderSpec.parse("");
        CacheBuilder<Object, Object> builder = defaultSpec.toCacheBuilder();
        assertEquals(0, builder.initialCapacity());
        assertEquals(0L, builder.maximumSize());
        assertEquals(0L, builder.maximumWeight());
        assertEquals(CacheBuilder.Builder.defaultConcurrencyLevel(), builder.concurrencyLevel());
        assertNull(defaultSpec.keyStrength);
        assertNull(defaultSpec.valueStrength);
        assertFalse(defaultSpec.recordStats);
        assertNull(defaultSpec.writeExpirationTimeUnit);
        assertNull(defaultSpec.accessExpirationTimeUnit);
        assertNull(defaultSpec.refreshTimeUnit);
    }

    @Test
    public void testParse_initialCapacity_setsInitialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(5, builder.initialCapacity());
        assertEquals(0L, builder.maximumSize());
        assertEquals(0L, builder.maximumWeight());
        assertEquals(CacheBuilder.Builder.defaultConcurrencyLevel(), builder.concurrencyLevel());
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_maximumSize_setsMaximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(0, builder.initialCapacity());
        assertEquals(10L, builder.maximumSize());
        assertEquals(0L, builder.maximumWeight());
        assertEquals(CacheBuilder.Builder.defaultConcurrencyLevel(), builder.concurrencyLevel());
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_maximumWeight_setsMaximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=20");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(0, builder.initialCapacity());
        assertEquals(0L, builder.maximumSize());
        assertEquals(20L, builder.maximumWeight());
        assertEquals(CacheBuilder.Builder.defaultConcurrencyLevel(), builder.concurrencyLevel());
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_concurrencyLevel_setsConcurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=5");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(5, builder.initialCapacity());
        assertEquals(0L, builder.maximumSize());
        assertEquals(0L, builder.maximumWeight());
        assertEquals(5, builder.concurrencyLevel());
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_weakKeys_setsKeyStrength() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(0, builder.initialCapacity());
        assertEquals(0L, builder.maximumSize());
        assertEquals(0L, builder.maximumWeight());
        assertEquals(CacheBuilder.Builder.defaultConcurrencyLevel(), builder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertNull(spec.valueStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_softValues_setsValueStrength() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(0, builder.initialCapacity());
        assertEquals(0L, builder.maximumSize());
        assertEquals(0L, builder.maximumWeight());
        assertEquals(CacheBuilder.Builder.defaultConcurrencyLevel(), builder.concurrencyLevel());
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertNull(spec.keyStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_weakValues_setsValueStrength() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(0, builder.initialCapacity());
        assertEquals(0L, builder.maximumSize());
        assertEquals(0L, builder.maximumWeight());
        assertEquals(CacheBuilder.Builder.defaultConcurrencyLevel(), builder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
        assertNull(spec.keyStrength);
        assertFalse(spec.recordStats);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_recordStats_setsRecordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats=true");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(0, builder.initialCapacity());
        assertEquals(0L, builder.maximumSize());
        assertEquals(0L, builder.maximumWeight());
        assertEquals(CacheBuilder.Builder.defaultConcurrencyLevel(), builder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_expireAfterAccess_setsAccessExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=30s");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(0, builder.initialCapacity());
        assertEquals(0L, builder.maximumSize());
        assertEquals(0L, builder.maximumWeight());
        assertEquals(CacheBuilder.Builder.defaultConcurrencyLevel(), builder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertFalse(spec.recordStats);
        assertEquals(TimeUnit.SECONDS, spec.accessExpirationTimeUnit);
        assertEquals(30L, spec.accessExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_expireAfterWrite_setsWriteExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=1h");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(0, builder.initialCapacity());
        assertEquals(0L, builder.maximumSize());
        assertEquals(0L, builder.maximumWeight());
        assertEquals(CacheBuilder.Builder.defaultConcurrencyLevel(), builder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertFalse(spec.recordStats);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertEquals(1L, spec.writeExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_refreshAfterWrite_setsRefreshDuration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=1m");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(0, builder.initialCapacity());
        assertEquals(0L, builder.maximumSize());
        assertEquals(0L, builder.maximumWeight());
        assertEquals(CacheBuilder.Builder.defaultConcurrencyLevel(), builder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertFalse(spec.recordStats);
        assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
        assertEquals(1L, spec.refreshDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationTimeUnit);
    }

    @Test(expected = AssertionError.class)
    public void testParse_unknownKey_throwsAssertionError() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("unknownKey=value");
    }

    @Test
    public void testToParsableString_returnsCorrectSpecString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,weakKeys,recordStats=true,expireAfterAccess=30s,expireAfterWrite=1h,refreshAfterWrite=1m");
        String specString = spec.toParsableString();
        assertEquals("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,weakKeys,recordStats=true,expireAfterAccess=30s,expireAfterWrite=1h,refreshAfterWrite=1m", specString);
    }
}