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
    public void testParseInitialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5");
        assertEquals(5, spec.initialCapacity);
        assertEquals(0, spec.maximumSize);
        assertEquals(0, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParseMaximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10");
        assertEquals(null, spec.initialCapacity);
        assertEquals(10, spec.maximumSize);
        assertEquals(0, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParseMaximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=20");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0, spec.maximumSize);
        assertEquals(20, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParseConcurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=3");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0, spec.maximumSize);
        assertEquals(0, spec.maximumWeight);
        assertEquals(3, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParseKeyStrengthWeak() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0, spec.maximumSize);
        assertEquals(0, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParseValueStrengthSoft() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0, spec.maximumSize);
        assertEquals(0, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParseValueStrengthWeak() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0, spec.maximumSize);
        assertEquals(0, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParseRecordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats=true");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0, spec.maximumSize);
        assertEquals(0, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(true, spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParseWriteExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=1h");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0, spec.maximumSize);
        assertEquals(0, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(3600000, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParseAccessExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=2m");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0, spec.maximumSize);
        assertEquals(0, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(120000, spec.accessExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
        assertEquals(0, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testParseRefreshExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=3s");
        assertEquals(null, spec.initialCapacity);
        assertEquals(0, spec.maximumSize);
        assertEquals(0, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(0, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(0, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(3000, spec.refreshDuration);
        assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
    }
}