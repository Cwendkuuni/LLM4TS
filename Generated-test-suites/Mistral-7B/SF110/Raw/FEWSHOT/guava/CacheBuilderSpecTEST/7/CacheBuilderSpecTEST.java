package com.google.common.cache;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.util.concurrent.*;
import java.util.*;
import com.google.common.collect.*;
import com.google.common.base.*;
import javax.annotation.*;

import org.junit.Test;

public class CacheBuilderSpecTest {
    @Test
    public void testParse_initialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5");
        assertEquals(5, spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
    }

    @Test
    public void testParse_maximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10");
        assertNull(spec.initialCapacity);
        assertEquals(10, spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
    }

    @Test
    public void testParse_maximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=20");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertEquals(20, spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
    }

    @Test
    public void testParse_concurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=3");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertEquals(3, spec.concurrencyLevel);
    }

    @Test
    public void testParse_weakKeys() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
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
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertEquals(true, spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_expireAfterAccess() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=10m");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertEquals(10, spec.accessExpirationDuration);
        assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_expireAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=5s");
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertEquals(5, spec.writeExpirationDuration);
        assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParse_refreshAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=2h");
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
        assertEquals(2, spec.refreshDuration);
        assertEquals(TimeUnit.HOURS, spec.refreshTimeUnit);
    }

    @Test
    public void testParse_invalidSpec() {
        try {
            CacheBuilderSpec.parse("invalidKey=value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("key invalidKey does not take values", e.getMessage());
        }
    }

    @Test
    public void testParse_invalidSpecFormat() {
        try {
            CacheBuilderSpec.parse("expireAfterWrite=10d,1h");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("key expireAfterWrite invalid format.  was 10d,1h, must end with one of [dDhHmMsS]", e.getMessage());
        }
    }

    @Test
    public void testToCacheBuilder() {
        final CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5" +
                ", maximumSize=10" +
                ", weakKeys" +
                ", recordStats=true" +
                ", expireAfterAccess=30s" +
                ", expireAfterWrite=60s" +
                ", refreshAfterWrite=5m");

        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();

        assertEquals(CacheBuilder.newBuilder(), builder);
        assertEquals(5, builder.initialCapacity());
        assertEquals(10, builder.maximumSize());
        assertEquals(CacheBuilder.newBuilder().weakKeys(), builder);
        assertTrue(builder.recordStats());
        assertEquals(CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.SECONDS), builder);
        assertEquals(CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS), builder);
        assertEquals(CacheBuilder.newBuilder().refreshAfterWrite(5, TimeUnit.MINUTES), builder);
    }

    @Test
    public void testToParsableString() {
        final CacheBuilderSpec spec = new CacheBuilderSpec("invalidSpec");
        assertEquals("invalidSpec", spec.toParsableString());

        final CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=5" +
                ", maximumSize=10" +
                ", weakKeys" +
                ", recordStats=true" +
                ", expireAfterAccess=30s" +
                ", expireAfterWrite=60s" +
                ", refreshAfterWrite=5m");
        assertEquals("initialCapacity=5,maximumSize=10,weakKeys,recordStats=true,expireAfterAccess=30s,expireAfterWrite=60s,refreshAfterWrite=5m", spec2.toParsableString());
    }
}