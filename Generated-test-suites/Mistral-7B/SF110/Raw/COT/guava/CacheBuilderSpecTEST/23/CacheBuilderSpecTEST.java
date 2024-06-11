package com.example.cachebuilder;

import com.google.common.cache.CacheBuilderSpec;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals("10", cacheBuilderSpec.getInitialCapacity());
        assertEquals(50L, cacheBuilderSpec.getMaximumSize());
        assertEquals(100L, cacheBuilderSpec.getMaximumWeight());
        assertEquals(5, cacheBuilderSpec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, cacheBuilderSpec.getKeyStrength());
        assertEquals(LocalCache.Strength.SOFT, cacheBuilderSpec.getValueStrength());
        assertTrue(cacheBuilderSpec.isRecordStats());
        assertEquals(1L * 60L * 60L, cacheBuilderSpec.getAccessExpirationDuration().toMillis());
        assertEquals(TimeUnit.HOURS, cacheBuilderSpec.getAccessExpirationTimeUnit());
        assertEquals(2L * 24L * 60L * 60L * 1000L, cacheBuilderSpec.getWriteExpirationDuration().toMillis());
        assertEquals(TimeUnit.DAYS, cacheBuilderSpec.getWriteExpirationTimeUnit());
        assertEquals(30L * 1000L, cacheBuilderSpec.getRefreshDuration().toMillis());
        assertEquals(TimeUnit.SECONDS, cacheBuilderSpec.getRefreshTimeUnit());

        String invalidCacheBuilderSpecification = "invalidKey=invalidValue";
        try {
            CacheBuilderSpec.parse(invalidCacheBuilderSpecification);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();

        // Check initialCapacity
        assertEquals(10, cacheBuilder.initialCapacity());

        // Check maximumSize
        Cache<Object, Object> cache = cacheBuilder.build();
        int size = cache.size();
        for (int i = 0; i < 51; i++) {
            cache.put(Integer.toString(i), Integer.toString(i));
            size++;
        }
        assertEquals(50, size);

        // Check maximumWeight
        Cache<Object, Object> weightedCache = CacheBuilder.newBuilder().maximumWeight(100).build();
        Object heavyKey = new byte[1024 * 1024]; // 1MB
        weightedCache.put("heavyKey", heavyKey);
        try {
            cacheBuilderSpec = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=50, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
            cacheBuilder = cacheBuilderSpec.toCacheBuilder();
            cacheBuilder.build().put("heavyKey2", heavyKey); // Should fail
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        // Check concurrencyLevel
        Cache<Object, Object> concurrentCache = CacheBuilder.newBuilder().concurrencyLevel(10).build();
        for (int i = 0; i < 11; i++) {
            concurrentCache.put("thread" + i, Integer.toString(i));
        }
        for (int i = 0; i < 11; i++) {
            Thread thread = new Thread(() -> {
                concurrentCache.put("thread" + (i % 11), Integer.toString(i % 11));
            });
            thread.start();
        }
        for (int i = 0; i < 11; i++) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                fail("Unexpected InterruptedException");
            }
        }
        assertEquals(11, concurrentCache.size());

        // Check weakKeys
        cache = CacheBuilder.newBuilder().weakKeys().build();
        cache.put("key1", "value1");
        cache.invalidate("key1");
        assertNull(cache.getIfPresent("key1"));

        // Check softValues
        Cache<String, SoftReference<Integer>> softValueCache = CacheBuilder.newBuilder().softValues().build();
        softValueCache.put("key1", new SoftReference<>(1));
        softValueCache.getIfPresent("key1"); // Returns soft reference
        assertNull(softValueCache.getIfPresent("key1")); // Soft reference is garbage collected

        // Check recordStats
        Cache<Object, Object> statsCache = CacheBuilder.newBuilder().recordStats().build();
        statsCache.put("key1", "value1");
        statsCache.remove("key1");
        assertEquals(1, statsCache.stats().cacheHits());
        assertEquals(1, statsCache.stats().cacheMisses());
        assertEquals(1, statsCache.stats().cacheLoadSuccesses());
        assertEquals(1, statsCache.stats().cacheLoadFailures());
        assertEquals(1, statsCache.stats().evictionCount());

        // Check expireAfterAccess
        cache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build();
        cache.put("key1", "value1");
        cache.get("key1");
        Thread.sleep(1 * 60 * 60 * 1000); // Wait for expiration
        assertNull(cache.get("key1"));

        // Check expireAfterWrite
        cache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.DAYS).build();
        cache.put("key1", "value1");
        Thread.sleep(2 * 24 * 60 * 60 * 1000); // Wait for expiration
        assertNull(cache.get("key1"));

        // Check refreshAfterWrite
        cache = CacheBuilder.newBuilder().refreshAfterWrite(30, TimeUnit.SECONDS).build();
        cache.put("key1", "value1");
        Thread.sleep(30 * 1000); // Wait for refresh
        assertEquals("value1", cache.get("key1"));
        cache.put("key1", "value2");
        assertEquals("value2", cache.get("key1"));
    }

    @Test
    public void testToString() {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
        assertEquals("CacheBuilderSpec{initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=0s, writeExpirationTimeUnit=SECONDS, accessExpirationDuration=3600000ms, accessExpirationTimeUnit=MILLISECONDS, refreshDuration=0s, refreshTimeUnit=SECONDS}", cacheBuilderSpec.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        CacheBuilderSpec cacheBuilderSpec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
        CacheBuilderSpec cacheBuilderSpec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
        CacheBuilderSpec cacheBuilderSpec3 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30ms"); // Different refreshTimeUnit

        assertEquals(cacheBuilderSpec1, cacheBuilderSpec2);
        assertNotEquals(cacheBuilderSpec1, cacheBuilderSpec3);

        int hashCode1 = cacheBuilderSpec1.hashCode();
        int hashCode2 = cacheBuilderSpec2.hashCode();
        int hashCode3 = cacheBuilderSpec3.hashCode();
        assertTrue(hashCode1 == hashCode2 && hashCode1 != hashCode3);
    }
}