package com.google.common.cache;

import static org.junit.Assert.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParseInitialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5");
        assertEquals(5, spec.getInitialCapacity());
    }

    @Test
    public void testParseMaximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10");
        assertEquals(10, spec.getMaximumSize());
        assertEquals(0, spec.getMaximumWeight());
    }

    @Test
    public void testParseMaximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=5,maximumWeight=15");
        assertEquals(0, spec.getMaximumSize());
        assertEquals(15, spec.getMaximumWeight());
    }

    @Test
    public void testParseConcurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=3");
        assertEquals(3, spec.getConcurrencyLevel());
    }

    @Test
    public void testParseWeakKeys() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
    }

    @Test
    public void testParseSoftValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
    }

    @Test
    public void testParseWeakValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues");
        assertEquals(LocalCache.Strength.WEAK, spec.getValueStrength());
    }

    @Test
    public void testParseRecordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats=true");
        assertTrue(spec.isRecordStats());
    }

    @Test
    public void testParseExpireAfterAccess() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1d");
        assertEquals(1, spec.getAccessExpirationDuration());
        assertEquals(TimeUnit.DAYS, spec.getAccessExpirationTimeUnit());
    }

    @Test
    public void testParseExpireAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=2h");
        assertEquals(2, spec.getWriteExpirationDuration());
        assertEquals(TimeUnit.HOURS, spec.getWriteExpirationTimeUnit());
    }

    @Test
    public void testParseRefreshAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=3m");
        assertEquals(3, spec.getRefreshDuration());
        assertEquals(TimeUnit.MINUTES, spec.getRefreshTimeUnit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalid() {
        CacheBuilderSpec.parse("invalid=5");
    }

    @Test
    public void testParseEmpty() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNull(spec);
    }
}