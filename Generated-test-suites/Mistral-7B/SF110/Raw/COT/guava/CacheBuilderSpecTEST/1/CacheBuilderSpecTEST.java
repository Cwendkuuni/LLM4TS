package com.example.cachebuilder;

import static org.junit.Assert.*;

import com.google.common.cache.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, weakValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=5m");

        assertEquals(10, spec.getInitialCapacity());
        assertEquals(50, spec.getMaximumSize());
        assertEquals(100, spec.getMaximumWeight());
        assertEquals(5, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.WEAK, spec.getValueStrength());
        assertTrue(spec.isRecordStats());
        assertEquals(1, spec.getAccessExpirationTimeUnit().toHours(spec.getAccessExpirationDuration()));
        assertEquals(2, spec.getWriteExpirationTimeUnit().toDays(spec.getWriteExpirationDuration()));
        assertEquals(5, spec.getRefreshTimeUnit().toMinutes(spec.getRefreshDuration()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidSpecification() {
        CacheBuilderSpec.parse("invalidSpecification=5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseMissingValue() {
        CacheBuilderSpec.parse("initialCapacity=");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidValueType() {
        CacheBuilderSpec.parse("maximumSize=abcd");
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, weakValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=5m");
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        assertEquals(10, cacheBuilder.initialCapacity());
        assertEquals(50, cacheBuilder.maximumSize());
        assertEquals(100, cacheBuilder.maximumWeight());
        assertEquals(5, cacheBuilder.concurrencyLevel());
        assertTrue(cacheBuilder.weakKeys());
        assertTrue(cacheBuilder.weakValues());
        assertTrue(cacheBuilder.recordStats());
        assertEquals(1, cacheBuilder.expireAfterAccessInMilliseconds());
        assertEquals(2 * 24 * 60 * 60 * 1000, cacheBuilder.expireAfterWriteInMillis());
        assertEquals(5 * 60 * 1000, cacheBuilder.refreshAfterWriteInMillis());
    }

    @Test(expected = AssertionError.class)
    public void testUnknownKeyStrength() {
        CacheBuilderSpec spec = new CacheBuilderSpec("unknownKeyStrength=5");
    }

    @Test(expected = AssertionError.class)
    public void testUnknownValueStrength() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, unknownValueStrength=5");
    }
}