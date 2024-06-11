package com.example.junit.test;

import static org.junit.Assert.*;

import com.google.common.cache.*;
import org.junit.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String spec = "initialCapacity=10, maximumSize=50, concurrencyLevel=10";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(spec);
        assertEquals(10, cacheBuilderSpec.initialCapacity);
        assertEquals(50, cacheBuilderSpec.maximumSize);
        assertEquals(10, cacheBuilderSpec.concurrencyLevel);
        assertTrue(cacheBuilderSpec.keyStrength == null);
        assertTrue(cacheBuilderSpec.valueStrength == null);
        assertTrue(cacheBuilderSpec.recordStats == null);
        assertTrue(cacheBuilderSpec.writeExpirationDuration == 0);
        assertTrue(cacheBuilderSpec.writeExpirationTimeUnit == null);
        assertTrue(cacheBuilderSpec.accessExpirationDuration == 0);
        assertTrue(cacheBuilderSpec.accessExpirationTimeUnit == null);
        assertTrue(cacheBuilderSpec.refreshDuration == 0);
        assertTrue(cacheBuilderSpec.refreshTimeUnit == null);
    }

    @Test
    public void testEmptyString() {
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse("");
        assertTrue(cacheBuilderSpec.initialCapacity == null);
        assertTrue(cacheBuilderSpec.maximumSize == null);
        assertTrue(cacheBuilderSpec.concurrencyLevel == null);
        assertTrue(cacheBuilderSpec.keyStrength == null);
        assertTrue(cacheBuilderSpec.valueStrength == null);
        assertTrue(cacheBuilderSpec.recordStats == null);
        assertTrue(cacheBuilderSpec.writeExpirationDuration == 0);
        assertTrue(cacheBuilderSpec.writeExpirationTimeUnit == null);
        assertTrue(cacheBuilderSpec.accessExpirationDuration == 0);
        assertTrue(cacheBuilderSpec.accessExpirationTimeUnit == null);
        assertTrue(cacheBuilderSpec.refreshDuration == 0);
        assertTrue(cacheBuilderSpec.refreshTimeUnit == null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() {
        String spec = "initialCapacity=10, maximumSize";
        CacheBuilderSpec.parse(spec);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownKey() {
        String spec = "unknownKey=value";
        CacheBuilderSpec.parse(spec);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueForInitialCapacity() {
        String spec = "initialCapacity=abc";
        CacheBuilderSpec.parse(spec);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateKey() {
        String spec = "initialCapacity=10, initialCapacity=20";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(spec);
        assertEquals(20, cacheBuilderSpec.initialCapacity);
    }
}