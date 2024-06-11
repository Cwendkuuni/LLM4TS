package com.google.common.cache;

import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats=true, expireAfterWrite=1h, expireAfterAccess=30m, refreshAfterWrite=1d");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        // Assertions for the generated CacheBuilder
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        // Assertions for the empty CacheBuilder
    }

    @Test
    public void testInvalidCacheBuilderSpecification() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidKeyValuePair");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullCacheBuilderSpecification() {
        CacheBuilderSpec spec = null;
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
    }

    @Test
    public void testEmptyCacheBuilderSpec() {
        CacheBuilderSpec spec = new CacheBuilderSpec("");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        // Assertions for the empty CacheBuilder
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeyValuePair() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,");
        spec.toCacheBuilder();
    }
}
