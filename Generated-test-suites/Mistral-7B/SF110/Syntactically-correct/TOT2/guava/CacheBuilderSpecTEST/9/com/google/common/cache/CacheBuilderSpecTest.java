package com.example.test.cachebuilder;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.LocalCache.Strength;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpec = "initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);
        assertEquals(5, spec.initialCapacity);
        assertEquals(10, spec.maximumSize);
        assertEquals(3, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
        assertEquals("initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys", spec.toParsableString());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, spec.maximumSize);
        assertEquals(null, spec.initialCapacity);
        assertEquals(null, spec.maximumWeight);
        assertEquals(null, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test
    public void testToCacheBuilder_ValidSpec() {
        String cacheBuilderSpec = "initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, maximumWeight=20, recordStats=true, expireAfterWrite=1h, expireAfterAccess=30m, refreshAfterWrite=1d";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        assertEquals(5, builder.initialCapacity());
        assertEquals(10, builder.maximumSize());
        assertEquals(3, builder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, builder.weakKeys());
        assertEquals(LocalCache.Strength.SOFT, builder.softValues());
        assertEquals(LocalCache.Strength.WEAK, builder.weakValues());
        assertEquals(true, builder.recordStats());
        assertEquals(1, builder.expireAfterWrite(TimeUnit.HOURS));
        assertEquals(30, builder.expireAfterAccess(TimeUnit.MINUTES));
        assertEquals(1, builder.refreshAfterWrite(TimeUnit.DAYS));
    }

    @Test
    public void testToCacheBuilder_EmptySpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNotNull(spec.toCacheBuilder());
        assertEquals(0, spec.maximumSize);
        assertEquals(0, spec.maximumWeight);
        assertEquals(0, spec.concurrencyLevel);
        assertEquals(null, spec.keyStrength);
        assertEquals(null, spec.valueStrength);
        assertEquals(false, spec.recordStats);
        assertEquals(null, spec.writeExpirationDuration);
        assertEquals(null, spec.writeExpirationTimeUnit);
        assertEquals(null, spec.accessExpirationDuration);
        assertEquals(null, spec.accessExpirationTimeUnit);
        assertEquals(null, spec.refreshDuration);
        assertEquals(null, spec.refreshTimeUnit);
    }

    @Test(expected = AssertionError.class)
    public void testToCacheBuilder_InvalidSpec() {
        String cacheBuilderSpec = "initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, maximumWeight=20, recordStats=true, expireAfterWrite=1h, expireAfterAccess=30m, refreshAfterWrite=1d, unknownKey=5";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);
        spec.toCacheBuilder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_EmptySpec() {
        String emptySpec = "";
        try {
            CacheBuilderSpec.parse(emptySpec);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("blank key-value pair", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidSpec1() {
        String invalidSpec1 = "invalidKey=5";
        try {
            CacheBuilderSpec.parse(invalidSpec1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("unknown key invalidKey", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidSpec2() {
        String invalidSpec2 = "initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, maximumWeight=20, recordStats=true, expireAfterWrite=1h, expireAfterAccess=30m, refreshAfterWrite=1d, unknownKey=5, unknownKey=5";
        try {
            CacheBuilderSpec.parse(invalidSpec2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("key invalidSpec2 with more than one equals sign", e.getMessage());
        }
    }
}