package com.google.common.cache;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static com.google.common.cache.CacheBuilderSpec.KEYS_SPLITTER;
import static com.google.common.cache.CacheBuilderSpec.KEY_VALUE_SPLITTER;

import java.util.concurrent.*;
import java.util.*;
import com.google.common.collect.*;
import com.google.common.base.*;
import javax.annotation.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

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
    }

    @Test
    public void testParse_initialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertEquals(Integer.valueOf(5), spec.initialCapacity);
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
    public void testParse_maximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertEquals(Long.valueOf(10), spec.maximumSize);
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
    public void testParse_maximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=20");
        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertEquals(Long.valueOf(20), spec.maximumWeight);
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
    }

    @Test
    public void testParse_recordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats=true");
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
        assertTrue(spec.recordStats);
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
        assertEquals(Duration.ONE_DAY, spec.accessExpirationDuration);
        assertEquals(TimeUnit.DAYS, spec.accessExpirationTimeUnit);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_expireAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=2h");
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
        assertEquals(Duration.TWENTY_HOURS, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_refreshAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=3m");
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
        assertEquals(Duration.THREE_MINUTES, spec.refreshDuration);
        assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_invalidSpec() {
        CacheBuilderSpec.parse("invalid=123");
    }

    @Test
    public void testParse_multipleProperties() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5\n" +
                "maximumSize=10\n" +
                "maximumWeight=20\n" +
                "concurrencyLevel=3\n" +
                "weakKeys\n" +
                "softValues\n" +
                "recordStats\n" +
                "expireAfterAccess=1d\n" +
                "expireAfterWrite=2h\n" +
                "refreshAfterWrite=3m");

        assertEquals(CacheBuilderSpec.class, spec.getClass());
        assertEquals(Integer.valueOf(5), spec.initialCapacity);
        assertEquals(Long.valueOf(10), spec.maximumSize);
        assertEquals(Long.valueOf(20), spec.maximumWeight);
        assertEquals(Integer.valueOf(3), spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(Duration.ONE_DAY, spec.accessExpirationDuration);
        assertEquals(TimeUnit.DAYS, spec.accessExpirationTimeUnit);
        assertEquals(Duration.TWENTY_HOURS, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertEquals(Duration.THREE_MINUTES, spec.refreshDuration);
        assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
    }
}