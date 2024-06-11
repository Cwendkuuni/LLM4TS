package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.cache.*;
import org.junit.*;

import java.util.*;
import java.util.concurrent.*;

public class CacheBuilderSpecTest {

    

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=15";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals(10, spec.getInitialCapacity());
        assertEquals(50, spec.getMaximumSize());
        assertEquals(15, spec.getConcurrencyLevel());
        assertNull(spec.getKeyStrength());
        assertNull(spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getAccessExpirationTimeUnit());
        assertNull(spec.getRefreshTimeUnit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithEmptySpecification() {
        String cacheBuilderSpecification = "";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidKeyValuePair() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=15=invalidKey";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParseWithUnknownKey() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=15, unknownKey=value";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertNull(spec.getValueParserForKey("unknownKey"));
    }

    @Test
    public void testParseWithNullValue() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=15, recordStats=null";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertTrue(spec.isRecordStats());
    }

    @Test
    public void testToCacheBuilder() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=15";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        assertEquals(10, cacheBuilder.initialCapacity());
        assertEquals(50, cacheBuilder.maximumSize());
        assertEquals(15, cacheBuilder.concurrencyLevel());
        assertNull(cacheBuilder.weakKeys());
        assertNull(cacheBuilder.weakValues());
        assertNull(cacheBuilder.recordStats());
        assertNull(cacheBuilder.expireAfterWrite(0, TimeUnit.SECONDS));
        assertNull(cacheBuilder.expireAfterAccess(0, TimeUnit.SECONDS));
        assertNull(cacheBuilder.refreshAfterWrite(0, TimeUnit.SECONDS));
    }

    @Test
    public void testToCacheBuilderWithRecordStats() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=15, recordStats=true";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        assertTrue(cacheBuilder.recordStats());
    }

    @Test
    public void testToCacheBuilderWithExpireAfterWrite() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=15, expireAfterWrite=1d";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        assertEquals(1, cacheBuilder.expireAfterWrite(0, TimeUnit.DAYS));
        assertEquals(TimeUnit.DAYS, cacheBuilder.expireAfterWriteTimeUnit());
    }

    @Test
    public void testToCacheBuilderWithExpireAfterAccess() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=15, expireAfterAccess=30s";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        assertEquals(30, cacheBuilder.expireAfterAccess(0, TimeUnit.SECONDS));
        assertEquals(TimeUnit.SECONDS, cacheBuilder.expireAfterAccessTimeUnit());
    }

    @Test
    public void testToCacheBuilderWithRefreshAfterWrite() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=15, refreshAfterWrite=5m";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        assertEquals(5 * 60, cacheBuilder.refreshAfterWrite(0, TimeUnit.SECONDS));
        assertEquals(TimeUnit.MINUTES, cacheBuilder.refreshAfterWriteTimeUnit());
    }

    @Test(expected = AssertionError.class)
    public void testToCacheBuilderWithUnknownKeyStrength() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=15, weakKeys=STRANGE";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        fail("Expected AssertionError");
    }

    @Test(expected = AssertionError.class)
    public void testToCacheBuilderWithUnknownValueStrength() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=15, weakValues=STRANGE";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        fail("Expected AssertionError");
    }

    
}