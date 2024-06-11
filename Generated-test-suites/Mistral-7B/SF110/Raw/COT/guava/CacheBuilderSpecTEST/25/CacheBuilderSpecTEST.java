package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.cache.*;
import org.junit.*;

import java.util.*;
import java.util.concurrent.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Test parsing a valid cache builder specification
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=15, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals(10, spec.initialCapacity);
        assertEquals(15, spec.maximumSize);
        assertEquals(5, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(1, spec.accessExpirationTimeUnit.toUnits(1));
        assertEquals(2 * 24 * 60 * 60, spec.writeExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
        assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
        assertNull(spec.maximumWeight);

        // Test parsing an empty cache builder specification
        String emptyCacheBuilderSpecification = "";
        try {
            CacheBuilderSpec emptySpec = CacheBuilderSpec.parse(emptyCacheBuilderSpecification);
            fail("Expected IllegalArgumentException when parsing an empty cache builder specification");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test parsing an invalid cache builder specification
        String invalidCacheBuilderSpecification = "invalidKey=value";
        try {
            CacheBuilderSpec invalidSpec = CacheBuilderSpec.parse(invalidCacheBuilderSpecification);
            fail("Expected IllegalArgumentException when parsing an invalid cache builder specification");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test parsing a cache builder specification with duplicate keys
        String duplicateKeyCacheBuilderSpecification = "initialCapacity=10, maximumSize=15, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, expireAfterWrite=3d, refreshAfterWrite=30s";
        try {
            CacheBuilderSpec duplicateSpec = CacheBuilderSpec.parse(duplicateCacheBuilderSpecification);
            fail("Expected IllegalArgumentException when parsing a cache builder specification with duplicate keys");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToCacheBuilder() {
        // Test creating a CacheBuilder using the default specification
        CacheBuilder<Object, Object> defaultCacheBuilder = CacheBuilderSpec.parse("").toCacheBuilder();
        assertNull(defaultCacheBuilder.initialCapacity());
        assertNull(defaultCacheBuilder.maximumSize());
        assertNull(defaultCacheBuilder.maximumWeight());
        assertNull(defaultCacheBuilder.concurrencyLevel());
        assertNull(defaultCacheBuilder.weakKeys());
        assertNull(defaultCacheBuilder.weakValues());
        assertNull(defaultCacheBuilder.softValues());
        assertNull(defaultCacheBuilder.recordStats());
        assertNull(defaultCacheBuilder.expireAfterAccess());
        assertNull(defaultCacheBuilder.expireAfterWrite());
        assertNull(defaultCacheBuilder.refreshAfterWrite());

        // Test creating a CacheBuilder using a specified specification
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=15, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        assertEquals(10, cacheBuilder.initialCapacity());
        assertEquals(15, cacheBuilder.maximumSize());
        assertEquals(5, cacheBuilder.concurrencyLevel());
        assertTrue(cacheBuilder.weakKeys());
        assertTrue(cacheBuilder.weakValues());
        assertTrue(cacheBuilder.softValues());
        assertTrue(spec.recordStats);
        assertEquals(1, cacheBuilder.expireAfterAccess().toUnitMultiplier());
        assertEquals(2 * 24 * 60 * 60, cacheBuilder.expireAfterWrite().toSeconds());
        assertEquals(30, cacheBuilder.refreshAfterWrite().toSeconds());
    }

    @Test(expected = NullPointerException.class)
    public void testToCacheBuilderWithNullSpecification() {
        CacheBuilderSpec spec = null;
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
    }
}