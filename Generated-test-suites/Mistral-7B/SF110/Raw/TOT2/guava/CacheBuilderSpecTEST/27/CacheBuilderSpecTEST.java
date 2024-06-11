package com.example.junit;

import static org.junit.Assert.*;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.LocalCache.Strength;
import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // ... (previous test case for the parse method)
    }

    // ... (test cases for other methods and edge cases)

    @Test
    public void testToCacheBuilder() {
        // Test with default values
        String cacheBuilderSpecification = "";
        CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        assertEquals("default cache builder was not created", null, cacheBuilder);

        // Test with set values
        String setValuesCacheBuilderSpecification = "initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, weakValues, recordStats, expireAfterAccess=1h, expireAfterWrite=1d";
        spec = new CacheBuilderSpec(setValuesCacheBuilderSpecification);
        cacheBuilder = spec.toCacheBuilder();

        assertEquals("cache builder was not created with the correct initialCapacity", 10, cacheBuilder.initialCapacity());
        assertEquals("cache builder was not created with the correct maximumSize", 20, cacheBuilder.maximumSize());
        assertEquals("cache builder was not created with the correct concurrencyLevel", 5, cacheBuilder.concurrencyLevel());
        assertEquals("cache builder was not created with the correct keyStrength", LocalCache.Strength.WEAK, cacheBuilder.keyStrength());
        assertEquals("cache builder was not created with the correct valueStrength", LocalCache.Strength.WEAK, cacheBuilder.valueStrength());
        assertEquals("cache builder was not created with the correct recordStats", true, cacheBuilder.recordStats());
        assertEquals("cache builder was not created with the correct expireAfterAccess", 3600000, cacheBuilder.expireAfterAccess());
        assertEquals("cache builder was not created with the correct expireAfterWrite", 86400000, cacheBuilder.expireAfterWrite());
        assertEquals("cache builder was not created with the correct refreshAfterWrite", 0, cacheBuilder.refreshAfterWrite());

        // Test with empty cacheBuilderSpecification
        cacheBuilderSpecification = "";
        spec = new CacheBuilderSpec(cacheBuilderSpecification);
        cacheBuilder = spec.toCacheBuilder();
        assertEquals("cache builder was not created with the correct initialCapacity", null, cacheBuilder);

        // Test with invalid keys or values in the cacheBuilderSpecification
        String invalidKeysOrValuesCacheBuilderSpecification = "invalidKey=10, maximumSize=20, concurrencyLevel=5, recordStats, expireAfterAccess=1h, expireAfterWrite=1d, invalidValue";
        spec = new CacheBuilderSpec(invalidKeysOrValuesCacheBuilderSpecification);
        cacheBuilder = spec.toCacheBuilder();
        assertNull("cache builder was not set to null with invalid cacheBuilderSpecification", cacheBuilder);

        // Test the behavior when setting a key or value twice
        String duplicateKeysOrValuesCacheBuilderSpecification = "initialCapacity=10, initialCapacity=15, maximumSize=20, maximumSize=15";
        try {
            spec = new CacheBuilderSpec(duplicateKeysOrValuesCacheBuilderSpecification);
            cacheBuilder = spec.toCacheBuilder();
            fail("Expected IllegalArgumentException with message: duplicate key: initialCapacity");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test the behavior when setting both maximumSize and maximumWeight
        String maxSizeAndWeightCacheBuilderSpecification = "maximumSize=10, maximumWeight=5";
        try {
            spec = new CacheBuilderSpec(maxSizeAndWeightCacheBuilderSpecification);
            cacheBuilder = spec.toCacheBuilder();
            fail("Expected IllegalArgumentException with message: cannot set both maximumSize and maximumWeight");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test the behavior when setting conflicting cache strengths
        String conflictingCacheBuilderSpecification = "weakKeys, softValues";
        try {
            spec = new CacheBuilderSpec(conflictingCacheBuilderSpecification);
            cacheBuilder = spec.toCacheBuilder();
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // Expected
        }

        // Test the behavior when setting a negative value for maximumSize, maximumWeight, or durations
        String negativeValuesCacheBuilderSpecification = "maximumSize=-1, maximumWeight=-1, expireAfterAccess=-1s, expireAfterWrite=-1d";
        try {
            spec = new CacheBuilderSpec(negativeValuesCacheBuilderSpecification);
            cacheBuilder = spec.toCacheBuilder();
            fail("Expected IllegalArgumentException with message: value must be non-negative");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test the behavior when setting an invalid time unit
        String invalidTimeUnitCacheBuilderSpecification = "expireAfterAccess=1invalidUnit";
        try {
            spec = new CacheBuilderSpec(invalidTimeUnitCacheBuilderSpecification);
            cacheBuilder = spec.toCacheBuilder();
            fail("Expected IllegalArgumentException with message: Unknown time unit: invalidUnit");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}