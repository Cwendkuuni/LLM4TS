package com.google.common.cache; // make sure to import all necessary classes

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class CacheBuilderSpecTest {

    private CacheBuilderSpec spec;
    
    @Before
    public void setUp() {
        spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=5, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=30s");
    }

    @Test
    public void testParse() {
        assertNotNull(spec);
        assertEquals(10, spec.initialCapacity);
        assertEquals(5, spec.maximumSize);
        assertEquals(20, spec.maximumWeight);
        assertEquals(5, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(TimeUnit.DAYS, spec.accessExpirationTimeUnit);
        assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
        assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidSpecification() {
        CacheBuilderSpec.parse("invalidSpecification");
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec disabledSpec = CacheBuilderSpec.disableCaching();
        assertEquals(0, disabledSpec.maximumSize);
        assertNull(disabledSpec.initialCapacity);
        assertNull(disabledSpec.maximumWeight);
        assertNull(disabledSpec.concurrencyLevel);
        assertNull(disabledSpec.recordStats);
        assertNull(disabledSpec.writeExpirationDuration);
        assertNull(disabledSpec.writeExpirationTimeUnit);
        assertNull(disabledSpec.accessExpirationDuration);
        assertNull(disabledSpec.accessExpirationTimeUnit);
        assertNull(disabledSpec.refreshDuration);
        assertNull(disabledSpec.refreshTimeUnit);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        // add tests for each field set in the spec
        assertEquals(10, cacheBuilder.initialCapacity());
        assertEquals(5, cacheBuilder.maximumSize());
        assertEquals(20, cacheBuilder.maximumWeight());
        assertEquals(5, cacheBuilder.concurrencyLevel());
        assertTrue(cacheBuilder.recordStats());
        assertEquals(TimeUnit.DAYS, cacheBuilder.expireAfterAccess());
        assertEquals(TimeUnit.HOURS, cacheBuilder.expireAfterWrite());
        assertEquals(TimeUnit.SECONDS, cacheBuilder.refreshAfterWrite());
    }

    @Test(expected = AssertionError.class)
    public void testUnknownKeyStrength() {
        new CacheBuilderSpec("unknownKeyStrength=value");
    }

    @Test(expected = AssertionError.class)
    public void testUnknownValueStrength() {
        new CacheBuilderSpec("key=unknownValueStrength");
    }

    @Test
    public void testParseInvalidKeyValuePair() {
        try {
            new CacheBuilderSpec("invalidKeyValuePair=value");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("blank key-value pair", e.getMessage());
        }
    }

    @Test
    public void testParseEmptySpecification() {
        try {
            new CacheBuilderSpec("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("", e.getMessage());
        }
    }

    @Test
    public void testParseMultipleKeyValuePairs() {
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=5, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=30s, maxSize=10");
        assertEquals(5, spec2.maximumSize);
        assertEquals(10, spec2.maximumSize);
        // add more assertions for other fields
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidDurationFormat() {
        new CacheBuilderSpec("expireAfterAccess=invalidFormat");
    }

    @Test(expected = NullPointerException.class)
    public void testParseNullValue() {
        try {
            new CacheBuilderSpec("initialCapacity=10,");
            fail();
        } catch (IllegalArgumentException e) {
            // expected, but add a test for a null key
            new CacheBuilderSpec(null);
            fail();
        }
    }
}