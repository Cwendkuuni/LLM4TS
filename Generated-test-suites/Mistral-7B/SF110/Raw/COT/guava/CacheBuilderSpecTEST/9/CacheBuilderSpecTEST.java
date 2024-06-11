package com.google.common.cache;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.AccessDurationParser;
import com.google.common.cache.CacheBuilderSpec.ConcurrencyLevelParser;
import com.google.common.cache.CacheBuilderSpec.InitialCapacityParser;
import com.google.common.cache.CacheBuilderSpec.KeyStrengthParser;
import com.google.common.cache.CacheBuilderSpec.MaximumSizeParser;
import com.google.common.cache.CacheBuilderSpec.MaximumWeightParser;
import com.google.common.cache.CacheBuilderSpec.RecordStatsParser;
import com.google.common.cache.CacheBuilderSpec.RefreshDurationParser;
import com.google.common.cache.CacheBuilderSpec.ValueStrengthParser;
import com.google.common.cache.CacheBuilderSpec.WriteDurationParser;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        // Test parsing a valid cache builder specification
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=30s";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=30s", cacheBuilderSpec.toParsableString());

        // Test getters
        assertEquals(10, cacheBuilderSpec.initialCapacity);
        assertEquals(50L, cacheBuilderSpec.maximumSize);
        assertEquals(100L, cacheBuilderSpec.maximumWeight);
        assertEquals(5, cacheBuilderSpec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, cacheBuilderSpec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, cacheBuilderSpec.valueStrength);
        assertEquals(true, cacheBuilderSpec.recordStats);
        assertEquals(1L, cacheBuilderSpec.writeExpirationDuration);
        assertEquals(TimeUnit.DAYS, cacheBuilderSpec.writeExpirationTimeUnit);
        assertEquals(1L, cacheBuilderSpec.accessExpirationDuration);
        assertEquals(TimeUnit.DAYS, cacheBuilderSpec.accessExpirationTimeUnit);
        assertEquals(30L, cacheBuilderSpec.refreshDuration);
        assertEquals(TimeUnit.SECONDS, cacheBuilderSpec.refreshTimeUnit);

        // Test getting cache builder from the specification
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();

        // Test that the cache builder has the correct configurations
        assertEquals(10, cacheBuilder.initialCapacity());
        assertEquals(50, cacheBuilder.maximumSize());
        assertEquals(100, cacheBuilder.maximumWeight());
        assertEquals(5, cacheBuilder.concurrencyLevel());
        assertTrue(cacheBuilder.weakKeys());
        assertTrue(cacheBuilder.softValues());
        assertTrue(cacheBuilder.recordStats());
        assertEquals(1L, cacheBuilder.expireAfterAccessInMilliseconds());
        assertEquals(2L * 60 * 60 * 1000L, cacheBuilder.expireAfterWriteInMillis());
        assertEquals(30L, cacheBuilder.refreshAfterWriteInMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidSpecification() {
        String cacheBuilderSpecification = "invalidKey=invalidValue";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
        fail("Expected an IllegalArgumentException");
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=30s");
        assertEquals("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=30s", cacheBuilderSpec.toParsableString());
    }

    @Test
    public void testToCacheBuilder_InitialCapacity() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("initialCapacity=10");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(10, cacheBuilder.initialCapacity());
    }

    @Test
    public void testToCacheBuilder_MaximumSize() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("maximumSize=50");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(50L, cacheBuilder.maximumSize());
    }

    @Test
    public void testToCacheBuilder_MaximumWeight() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("maximumWeight=100");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(100L, cacheBuilder.maximumWeight());
    }

    @Test
    public void testToCacheBuilder_ConcurrencyLevel() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("concurrencyLevel=5");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(5, cacheBuilder.concurrencyLevel());
    }

    @Test
    public void testToCacheBuilder_KeyStrength_WeakKeys() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("weakKeys");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertTrue(cacheBuilder.weakKeys());
    }

    @Test
    public void testToCacheBuilder_ValueStrength_SoftValues() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("softValues");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertTrue(cacheBuilder.softValues());
    }

    @Test
    public void testToCacheBuilder_ValueStrength_WeakValues() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("weakValues");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertTrue(cacheBuilder.weakValues());
    }

    @Test
    public void testToCacheBuilder_RecordStats() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("recordStats");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertTrue(cacheBuilder.recordStats());
    }

    @Test
    public void testToCacheBuilder_WriteExpiration_Days() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("expireAfterWrite=1d");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(1L * 24 * 60 * 60 * 1000L, cacheBuilder.expireAfterWriteInMillis());
    }

    @Test
    public void testToCacheBuilder_WriteExpiration_Hours() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("expireAfterWrite=2h");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(2L * 60 * 60 * 1000L, cacheBuilder.expireAfterWriteInMillis());
    }

    @Test
    public void testToCacheBuilder_WriteExpiration_Minutes() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("expireAfterWrite=3m");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(3L * 60 * 1000L, cacheBuilder.expireAfterWriteInMillis());
    }

    @Test
    public void testToCacheBuilder_WriteExpiration_Seconds() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("expireAfterWrite=4s");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(4L * 1000L, cacheBuilder.expireAfterWriteInMillis());
    }

    @Test
    public void testToCacheBuilder_AccessExpiration_Days() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("expireAfterAccess=1d");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(1L * 24 * 60 * 60 * 1000L, cacheBuilder.expireAfterAccessInMillis());
    }

    @Test
    public void testToCacheBuilder_AccessExpiration_Hours() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("expireAfterAccess=2h");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(2L * 60 * 60 * 1000L, cacheBuilder.expireAfterAccessInMillis());
    }

    @Test
    public void testToCacheBuilder_AccessExpiration_Minutes() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("expireAfterAccess=3m");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(3L * 60 * 1000L, cacheBuilder.expireAfterAccessInMillis());
    }

    @Test
    public void testToCacheBuilder_AccessExpiration_Seconds() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("expireAfterAccess=4s");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(4L * 1000L, cacheBuilder.expireAfterAccessInMillis());
    }

    @Test
    public void testToCacheBuilder_RefreshDuration_Days() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("refreshAfterWrite=1d");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(1L * 24 * 60 * 60 * 1000L, cacheBuilder.refreshAfterWriteInMillis());
    }

    @Test
    public void testToCacheBuilder_RefreshDuration_Hours() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("refreshAfterWrite=2h");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(2L * 60 * 60 * 1000L, cacheBuilder.refreshAfterWriteInMillis());
    }

    @Test
    public void testToCacheBuilder_RefreshDuration_Minutes() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("refreshAfterWrite=3m");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(3L * 60 * 1000L, cacheBuilder.refreshAfterWriteInMillis());
    }

    @Test
    public void testToCacheBuilder_RefreshDuration_Seconds() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("refreshAfterWrite=4s");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        assertEquals(4L * 1000L, cacheBuilder.refreshAfterWriteInMillis());
    }

    @Test
    public void testHashCodeAndEquals() {
        CacheBuilderSpec cacheBuilderSpec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterWrite=1d, expireAfterAccess=1h, refreshAfterWrite=1m");
        CacheBuilderSpec cacheBuilderSpec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterWrite=1d, expireAfterAccess=1h, refreshAfterWrite=1m");
        CacheBuilderSpec cacheBuilderSpec3 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=6, weakKeys, softValues, recordStats, expireAfterWrite=1d, expireAfterAccess=1h, refreshAfterWrite=1m");

        assertEquals(cacheBuilderSpec1, cacheBuilderSpec2);
        assertNotEquals(cacheBuilderSpec1, cacheBuilderSpec3);

        CacheBuilderSpec cacheBuilderSpec4 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterWrite=1d, expireAfterAccess=1h, refreshAfterWrite=1m");
        cacheBuilderSpec4.initialCapacity = 11;

        assertNotEquals(cacheBuilderSpec1, cacheBuilderSpec4);
    }
}