package com.google.common.cache;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParseInitialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        assertEquals(10, spec.initialCapacity);
    }

    @Test
    public void testParseMaximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=20");
        assertEquals(20, spec.maximumSize);
    }

    @Test
    public void testParseMaximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=30");
        assertEquals(30, spec.maximumWeight);
    }

    @Test
    public void testParseConcurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=5");
        assertEquals(5, spec.concurrencyLevel);
    }

    @Test
    public void testParseKeyStrengthWeak() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
    }

    @Test
    public void testParseValueStrengthSoft() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
    }

    @Test
    public void testParseValueStrengthWeak() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues");
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
    }

    @Test
    public void testParseRecordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats=true");
        assertTrue(spec.recordStats);
    }

    @Test
    public void testParseWriteExpirationDuration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=1h");
        assertEquals(1, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
    }

    @Test
    public void testParseWriteExpirationDurationWithUnit() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=3600s");
        assertEquals(1, spec.writeExpirationDuration);
        assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
    }

    @Test
    public void testParseAccessExpirationDuration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=2m");
        assertEquals(2, spec.accessExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
    }

    @Test
    public void testParseAccessExpirationDurationWithUnit() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=120s");
        assertEquals(2, spec.accessExpirationDuration);
        assertEquals(TimeUnit.SECONDS, spec.accessExpirationTimeUnit);
    }

    @Test
    public void testParseRefreshDuration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=30s");
        assertEquals(30, spec.refreshDuration);
        assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
    }

    @Test
    public void testParseInvalidSpec() {
        assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("invalid=key"));
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=20,maximumWeight=30,concurrencyLevel=5,weakKeys,softValues,recordStats=true,expireAfterWrite=1h,expireAfterAccess=2m,refreshAfterWrite=30s");
        assertEquals("initialCapacity=10,maximumSize=20,maximumWeight=30,concurrencyLevel=5,weakKeys,softValues,recordStats=true,expireAfterWrite=1h,expireAfterAccess=2m,refreshAfterWrite=30s", spec.toParsableString());
    }
}