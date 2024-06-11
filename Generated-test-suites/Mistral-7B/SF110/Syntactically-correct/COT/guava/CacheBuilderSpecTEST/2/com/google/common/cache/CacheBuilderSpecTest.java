package com.example.cachebuilder;

import com.google.common.cache.CacheBuilderSpec;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=50,concurrencyLevel=10");
        assertEquals(10, spec.getInitialCapacity());
        assertEquals(50, spec.getMaximumSize());
        assertEquals(10, spec.getConcurrencyLevel());
        assertEquals(null, spec.getKeyStrength());
        assertEquals(null, spec.getValueStrength());
        assertEquals(false, spec.isRecordStats());
        assertEquals(null, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(null, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(null, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(0, spec.getMaximumSize());
        assertNull(spec.getInitialCapacity());
        assertNull(spec.getMaximumWeight());
        assertNull(spec.getConcurrencyLevel());
        assertNull(spec.getKeyStrength());
        assertNull(spec.getValueStrength());
        assertNull(spec.isRecordStats());
        assertNull(spec.getWriteExpirationDuration());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getAccessExpirationDuration());
        assertNull(spec.getAccessExpirationTimeUnit());
        assertNull(spec.getRefreshDuration());
        assertNull(spec.getRefreshTimeUnit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidSpecification() {
        CacheBuilderSpec.parse("invalidSpecification");
    }

    @Test
    public void testToCacheBuilder_withInitialCapacity() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10");
        CacheBuilder<Integer, String> cacheBuilder = spec.toCacheBuilder();
        assertEquals(10, cacheBuilder.initialCapacity());
        assertNull(spec.getMaximumSize());
        assertNull(spec.getMaximumWeight());
        assertNull(spec.getConcurrencyLevel());
        assertNull(spec.getKeyStrength());
        assertNull(spec.getValueStrength());
        assertNull(spec.isRecordStats());
        assertNull(spec.getWriteExpirationDuration());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getAccessExpirationDuration());
        assertNull(spec.getAccessExpirationTimeUnit());
        assertNull(spec.getRefreshDuration());
        assertNull(spec.getRefreshTimeUnit());
    }

    @Test
    public void testToCacheBuilder_withMaximumSize() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=50");
        CacheBuilder<Integer, String> cacheBuilder = spec.toCacheBuilder();
        assertEquals(null, cacheBuilder.initialCapacity());
        assertEquals(50, cacheBuilder.maximumSize());
        assertNull(spec.getMaximumWeight());
        assertNull(spec.getConcurrencyLevel());
        assertNull(spec.getKeyStrength());
        assertNull(spec.getValueStrength());
        assertNull(spec.isRecordStats());
        assertNull(spec.getWriteExpirationDuration());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getAccessExpirationDuration());
        assertNull(spec.getAccessExpirationTimeUnit());
        assertNull(spec.getRefreshDuration());
        assertNull(spec.getRefreshTimeUnit());
    }

    @Test
    public void testToCacheBuilder_withMaximumWeight() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumWeight=100");
        CacheBuilder<Integer, String> cacheBuilder = spec.toCacheBuilder();
        assertEquals(null, cacheBuilder.initialCapacity());
        assertNull(cacheBuilder.maximumSize());
        assertEquals(100, cacheBuilder.maximumWeight());
        assertNull(spec.getConcurrencyLevel());
        assertNull(spec.getKeyStrength());
        assertNull(spec.getValueStrength());
        assertNull(spec.isRecordStats());
        assertNull(spec.getWriteExpirationDuration());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getAccessExpirationDuration());
        assertNull(spec.getAccessExpirationTimeUnit());
        assertNull(spec.getRefreshDuration());
        assertNull(spec.getRefreshTimeUnit());
    }

    @Test
    public void testToCacheBuilder_withConcurrencyLevel() {
        CacheBuilderSpec spec = new CacheBuilderSpec("concurrencyLevel=5");
        CacheBuilder<Integer, String> cacheBuilder = spec.toCacheBuilder();
        assertEquals(null, cacheBuilder.initialCapacity());
        assertNull(cacheBuilder.maximumSize());
        assertNull(cacheBuilder.maximumWeight());
        assertEquals(5, cacheBuilder.concurrencyLevel());
        assertNull(spec.getKeyStrength());
        assertNull(spec.getValueStrength());
        assertNull(spec.isRecordStats());
        assertNull(spec.getWriteExpirationDuration());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getAccessExpirationDuration());
        assertNull(spec.getAccessExpirationTimeUnit());
        assertNull(spec.getRefreshDuration());
        assertNull(spec.getRefreshTimeUnit());
    }

    @Test
    public void testToCacheBuilder_withKeyStrength() {
        CacheBuilderSpec spec = new CacheBuilderSpec("weakKeys");
        CacheBuilder<Integer, String> cacheBuilder = spec.toCacheBuilder();
        assertEquals(null, cacheBuilder.initialCapacity());
        assertNull(cacheBuilder.maximumSize());
        assertNull(cacheBuilder.maximumWeight());
        assertNull(cacheBuilder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, cacheBuilder.keyStrength());
        assertNull(spec.getValueStrength());
        assertNull(spec.isRecordStats());
        assertNull(spec.getWriteExpirationDuration());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getAccessExpirationDuration());
        assertNull(spec.getAccessExpirationTimeUnit());
        assertNull(spec.getRefreshDuration());
        assertNull(spec.getRefreshTimeUnit());
    }

    @Test
    public void testToCacheBuilder_withValueStrength() {
        CacheBuilderSpec spec = new CacheBuilderSpec("weakValues");
        CacheBuilder<Integer, String> cacheBuilder = spec.toCacheBuilder();
        assertEquals(null, cacheBuilder.initialCapacity());
        assertNull(cacheBuilder.maximumSize());
        assertNull(cacheBuilder.maximumWeight());
        assertNull(cacheBuilder.concurrencyLevel());
        assertNull(cacheBuilder.keyStrength());
        assertEquals(LocalCache.Strength.WEAK, cacheBuilder.valueStrength());
        assertNull(spec.isRecordStats());
        assertNull(spec.getWriteExpirationDuration());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getAccessExpirationDuration());
        assertNull(spec.getAccessExpirationTimeUnit());
        assertNull(spec.getRefreshDuration());
        assertNull(spec.getRefreshTimeUnit());
    }

    @Test
    public void testToCacheBuilder_withRecordStats() {
        CacheBuilderSpec spec = new CacheBuilderSpec("recordStats=true");
        CacheBuilder<Integer, String> cacheBuilder = spec.toCacheBuilder();
        assertEquals(null, cacheBuilder.initialCapacity());
        assertNull(cacheBuilder.maximumSize());
        assertNull(cacheBuilder.maximumWeight());
        assertNull(cacheBuilder.concurrencyLevel());
        assertNull(cacheBuilder.keyStrength());
        assertNull(cacheBuilder.valueStrength());
        assertTrue(cacheBuilder.recordStats());
        assertNull(spec.getWriteExpirationDuration());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getAccessExpirationDuration());
        assertNull(spec.getAccessExpirationTimeUnit());
        assertNull(spec.getRefreshDuration());
        assertNull(spec.getRefreshTimeUnit());
    }

    @Test
    public void testToCacheBuilder_withWriteExpiration() {
        CacheBuilderSpec spec = new CacheBuilderSpec("expireAfterWrite=10s");
        CacheBuilder<Integer, String> cacheBuilder = spec.toCacheBuilder();
        assertEquals(null, cacheBuilder.initialCapacity());
        assertNull(cacheBuilder.maximumSize());
        assertNull(cacheBuilder.maximumWeight());
        assertNull(cacheBuilder.concurrencyLevel());
        assertNull(cacheBuilder.keyStrength());
        assertNull(cacheBuilder.valueStrength());
        assertNull(cacheBuilder.recordStats());
        assertEquals(10, cacheBuilder.expireAfterWrite(TimeUnit.SECONDS));
        assertEquals(TimeUnit.SECONDS, cacheBuilder.expireAfterWriteTimeUnit());
        assertNull(spec.getAccessExpirationDuration());
        assertNull(spec.getAccessExpirationTimeUnit());
        assertNull(spec.getRefreshDuration());
        assertNull(spec.getRefreshTimeUnit());
    }

    @Test
    public void testToCacheBuilder_withAccessExpiration() {
        CacheBuilderSpec spec = new CacheBuilderSpec("expireAfterAccess=1m");
        CacheBuilder<Integer, String> cacheBuilder = spec.toCacheBuilder();
        assertEquals(null, cacheBuilder.initialCapacity());
        assertNull(cacheBuilder.maximumSize());
        assertNull(cacheBuilder.maximumWeight());
        assertNull(cacheBuilder.concurrencyLevel());
        assertNull(cacheBuilder.keyStrength());
        assertNull(cacheBuilder.valueStrength());
        assertNull(cacheBuilder.recordStats());
        assertNull(cacheBuilder.expireAfterWrite(null));
        assertNull(cacheBuilder.expireAfterWriteTimeUnit());
        assertEquals(1, cacheBuilder.expireAfterAccess(TimeUnit.MINUTES));
        assertEquals(TimeUnit.MINUTES, cacheBuilder.expireAfterAccessTimeUnit());
        assertNull(spec.getWriteExpirationDuration());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getRefreshDuration());
        assertNull(spec.getRefreshTimeUnit());
    }

    @Test
    public void testToCacheBuilder_withRefreshDuration() {
        CacheBuilderSpec spec = new CacheBuilderSpec("refreshAfterWrite=2h");
        CacheBuilder<Integer, String> cacheBuilder = spec.toCacheBuilder();
        assertEquals(null, cacheBuilder.initialCapacity());
        assertNull(cacheBuilder.maximumSize());
        assertNull(cacheBuilder.maximumWeight());
        assertNull(cacheBuilder.concurrencyLevel());
        assertNull(cacheBuilder.keyStrength());
        assertNull(cacheBuilder.valueStrength());
        assertNull(cacheBuilder.recordStats());
        assertNull(cacheBuilder.expireAfterWrite(null));
        assertNull(cacheBuilder.expireAfterWriteTimeUnit());
        assertNull(cacheBuilder.expireAfterAccess(null));
        assertNull(cacheBuilder.expireAfterAccessTimeUnit());
        assertEquals(2, cacheBuilder.refreshAfterWrite(TimeUnit.HOURS));
        assertEquals(TimeUnit.HOURS, cacheBuilder.refreshAfterWriteTimeUnit());
        assertNull(spec.getWriteExpirationDuration());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getAccessExpirationDuration());
        assertNull(spec.getAccessExpirationTimeUnit());
    }

    @Test(expected = AssertionError.class)
    public void testToCacheBuilder_withUnknownKeyStrength() {
        CacheBuilderSpec spec = new CacheBuilderSpec("unknownKeyStrength");
        spec.toCacheBuilder();
    }

    @Test(expected = AssertionError.class)
    public void testToCacheBuilder_withUnknownValueStrength() {
        CacheBuilderSpec spec = new CacheBuilderSpec("unknownValueStrength");
        spec.toCacheBuilder();
    }
}