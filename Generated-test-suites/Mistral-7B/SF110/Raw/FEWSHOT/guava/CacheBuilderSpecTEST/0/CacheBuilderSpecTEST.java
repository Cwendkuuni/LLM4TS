package com.google.common.cache;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import static com.google.common.base.Preconditions.*;
import javax.annotation.Nullable;

import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParseInitialCapacity() {
        String cacheBuilderSpecification = "initialCapacity=10";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(10, spec.initialCapacity);
    }

    @Test
    public void testParseMaximumSize() {
        String cacheBuilderSpecification = "maximumSize=50";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(50, spec.maximumSize);
        assertNull(spec.maximumWeight);
    }

    @Test
    public void testParseMaximumWeight() {
        String cacheBuilderSpecification = "maximumSize=50,maximumWeight=75";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(50, spec.maximumSize);
        assertEquals(75, spec.maximumWeight);
    }

    @Test
    public void testParseConcurrencyLevel() {
        String cacheBuilderSpecification = "concurrencyLevel=3";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(3, spec.concurrencyLevel);
    }

    @Test
    public void testParseKeyStrengthWeak() {
        String cacheBuilderSpecification = "weakKeys";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
    }

    @Test
    public void testParseValueStrengthSoft() {
        String cacheBuilderSpecification = "softValues";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
    }

    @Test
    public void testParseValueStrengthWeak() {
        String cacheBuilderSpecification = "weakValues";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
    }

    @Test
    public void testParseRecordStats() {
        String cacheBuilderSpecification = "recordStats";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertTrue(spec.recordStats);
    }

    @Test
    public void testParseAccessExpirationDuration() {
        String cacheBuilderSpecification = "expireAfterAccess=1d";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(1, spec.accessExpirationTimeUnit.toUnits(TimeUnit.DAYS));
        assertEquals(1 * 24 * 60 * 60 * 1000, spec.accessExpirationDuration);
    }

    @Test
    public void testParseAccessExpirationTimeUnitHours() {
        String cacheBuilderSpecification = "expireAfterAccess=2h";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(1, spec.accessExpirationTimeUnit.toUnits(TimeUnit.HOURS));
        assertEquals(2 * 60 * 60 * 1000, spec.accessExpirationDuration);
    }

    @Test
    public void testParseAccessExpirationTimeUnitMinutes() {
        String cacheBuilderSpecification = "expireAfterAccess=3m";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(1, spec.accessExpirationTimeUnit.toUnits(TimeUnit.MINUTES));
        assertEquals(3 * 60 * 1000, spec.accessExpirationDuration);
    }

    @Test
    public void testParseAccessExpirationTimeUnitSeconds() {
        String cacheBuilderSpecification = "expireAfterAccess=4s";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(1, spec.accessExpirationTimeUnit.toUnits(TimeUnit.SECONDS));
        assertEquals(4 * 1000, spec.accessExpirationDuration);
    }

    @Test
    public void testParseWriteExpirationDuration() {
        String cacheBuilderSpecification = "expireAfterWrite=5m";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(1, spec.writeExpirationTimeUnit.toUnits(TimeUnit.MINUTES));
        assertEquals(5 * 60 * 1000, spec.writeExpirationDuration);
    }

    @Test
    public void testParseWriteExpirationTimeUnitHours() {
        String cacheBuilderSpecification = "expireAfterWrite=6h";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(1, spec.writeExpirationTimeUnit.toUnits(TimeUnit.HOURS));
        assertEquals(6 * 60 * 60 * 1000, spec.writeExpirationDuration);
    }

    @Test
    public void testParseWriteExpirationTimeUnitDays() {
        String cacheBuilderSpecification = "expireAfterWrite=7d";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(1, spec.writeExpirationTimeUnit.toUnits(TimeUnit.DAYS));
        assertEquals(7 * 24 * 60 * 60 * 1000, spec.writeExpirationDuration);
    }

    @Test
    public void testParseRefreshDuration() {
        String cacheBuilderSpecification = "refreshAfterWrite=1h";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(1, spec.refreshTimeUnit.toUnits(TimeUnit.HOURS));
        assertEquals(1 * 60 * 60 * 1000, spec.refreshDuration);
    }

    @Test
    public void testParseRefreshDurationNegative() {
        String cacheBuilderSpecification = "refreshAfterWrite=-1s";
        try {
            CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("negative duration", e.getMessage());
        }
    }

    @Test
    public void testParseRefreshDurationZero() {
        String cacheBuilderSpecification = "refreshAfterWrite=0s";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(0, spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParseUnknownKey() {
        String cacheBuilderSpecification = "unknownKey=5";
        try {
            CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("unknown key "));
        }
    }

    @Test
    public void testParseMissingValue() {
        String cacheBuilderSpecification = "initialCapacity=";
        try {
            CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("value omitted for key "));
        }
    }

    @Test
    public void testParseValueNotInteger() {
        String cacheBuilderSpecification = "maximumSize=foo";
        try {
            CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("must be integer"));
        }
    }

    @Test
    public void testParseUnknownTimeUnit() {
        String cacheBuilderSpecification = "expireAfterAccess=1x";
        try {
            CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("unknown time unit"));
        }
    }
}