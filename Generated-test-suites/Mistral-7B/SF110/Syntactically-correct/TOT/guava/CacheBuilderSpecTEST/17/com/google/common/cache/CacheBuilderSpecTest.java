package com.google.common.cache;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.*;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;
 private CacheBuilder<String, String> cacheBuilder;

 @Before
 public void setUp() {
 spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, weakValues, recordStats, expireAfterAccess=10s, expireAfterWrite=20s, refreshAfterWrite=30s");
 cacheBuilder = spec.toCacheBuilder();
 }

 @Test
 public void testParsing() {
 CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(spec.toParsableString());
 assertEquals(spec.initialCapacity, parsedSpec.initialCapacity);
 assertEquals(spec.maximumSize, parsedSpec.maximumSize);
 assertEquals(spec.maximumWeight, parsedSpec.maximumWeight);
 assertEquals(spec.concurrencyLevel, parsedSpec.concurrencyLevel);
 assertEquals(spec.keyStrength, parsedSpec.keyStrength);
 assertEquals(spec.valueStrength, parsedSpec.valueStrength);
 assertEquals(spec.recordStats, parsedSpec.recordStats);
 assertEquals(spec.writeExpirationDuration, parsedSpec.writeExpirationDuration);
 assertEquals(spec.writeExpirationTimeUnit, parsedSpec.writeExpirationTimeUnit);
 assertEquals(spec.accessExpirationDuration, parsedSpec.accessExpirationDuration);
 assertEquals(spec.accessExpirationTimeUnit, parsedSpec.accessExpirationTimeUnit);
 assertEquals(spec.refreshDuration, parsedSpec.refreshDuration);
 assertEquals(spec.refreshTimeUnit, parsedSpec.refreshTimeUnit);
 }

 @Test
 public void testInitialCapacity() {
 Cache<String, String> cache = cacheBuilder.build();
 assertEquals(10, cache.size());
 }

 @Test
 public void testMaximumSize() {
 Cache<String, String> cache = cacheBuilder.build();
 for (int i = 0; i < 21; i++) {
 cache.put("key" + i, "value" + i);
 }
 assertEquals(20, cache.size());
 try {
 cache.put("key21", "value21");
 fail();
 } catch (CapacityExceededException e) {
 // expected
 }
 }

 @Test
 public void testMaximumWeight() {
 Cache<String, String> cache = cacheBuilder.build();
 for (int i = 0; i < 31; i++) {
 String key = "key" + i;
 String value = "value" + i;
 long valueLength = key.length() + value.length();
 if (valueLength > 30) {
 continue;
 }
 cache.put(key, value);
 }
 assertEquals(30, cache.asMap().values().stream().mapToLong(s -> s.length()).sum());
 }

 @Test
 public void testConcurrencyLevel() {
 Cache<String, String> cache = cacheBuilder.build();
 ExecutorService executor = Executors.newFixedThreadPool(cache.concurrencyLevel());
 for (int i = 0; i < 10; i++) {
 executor.submit(() -> {
 cache.put("key" + i, "value" + i);
 });
 }
 for (int i = 0; i < 5; i++) {
 try {
 cache.put("key11", "value11");
 fail();
 } catch (RejectedExecutionException e) {
 // expected
 }
 }
 executor.shutdown();
 }

 @Test
 public void testWeakKeys() {
 Cache<String, String> cache = cacheBuilder.build();
 // Add some keys
 cache.put("key1", "value1");
 cache.put("key2", "value2");
 // Clear cache
 cache.invalidateAll();
 // Check that keys are removed
 assertFalse(cache.containsKey("key1"));
 assertFalse(cache.containsKey("key2"));
 // Add some keys again
 cache.put("key1", "value1");
 cache.put("key2", "value2");
 // Clear cache again
 cache.invalidateAll();
 // Check that keys are still removed
 assertFalse(cache.containsKey("key1"));
 assertFalse(cache.containsKey("key2"));
 }

 @Test
 public void testWeakValues() {
 Cache<String, String> cache = cacheBuilder.build();
 // Add some values
 cache.put("key1", "value1");
 cache.put("key2", "value2");
 // Clear cache
 cache.invalidateAll();
 // Check that values are removed
 assertNull(cache.get("key1"));
 assertNull(cache.get("key2"));
 // Add some keys again
 cache.put("key1", "value1");
 cache.put("key2", "value2");
 // Clear cache again
 cache.invalidateAll();
 // Check that keys are still removed
 assertNull(cache.get("key1"));
 assertNull(cache.get("key2"));
 }

 @Test
 public void testRecordStats() {
 Cache<String, String> cache = cacheBuilder.build();
 long numPut = 0;
 long numGet = 0;
 long numRemove = 0;
 long numHit = 0;
 long numMiss = 0;
 CacheStats stats = cache.stats();
 for (int i = 0; i < 10; i++) {
 String key = "key" + i;
 String value = "value" + i;
 cache.put(key, value);
 numPut++;
 String valueFromCache = cache.get(key);
 if (valueFromCache != null) {
 numHit++;
 } else {
 numMiss++;
 }
 numGet++;
 }
 assertEquals(10, cache.size());
 assertEquals(10, numPut);
 assertEquals(10, numGet);
 assertEquals(0, numRemove);
 assertEquals(5, numHit);
 assertEquals(5, numMiss);
 assertEquals(5, stats.hits());
 assertEquals(5, stats.misses());
 assertEquals(5, stats.putCount());
 assertEquals(5, stats.getCount());
 assertEquals(5, stats.loadSuccesses());
 assertEquals(5, stats.loadFailures());
 }

 @Test
 public void testExpireAfterAccess() {
 Cache<String, String> cache = cacheBuilder.build();
 String key = "testKey";
 String value = "testValue";
 cache.put(key, value);
 assertEquals(value, cache.get(key));
 sleep(11000); // Wait for expiration
 assertEquals(null, cache.get(key));
 }

 @Test
 public void testExpireAfterWrite() {
 Cache<String, String> cache = cacheBuilder.build();
 String key = "testKey";
 String value = "testValue";
 cache.put(key, value);
 assertEquals(value, cache.get(key));
 cache.put(key, "newValue");
 assertEquals("newValue", cache.get(key));
 sleep(21000); // Wait for expiration
 assertEquals(null, cache.get(key));
 }

 @Test
 public void testRefreshAfterWrite() {
 Cache<String, String> cache = cacheBuilder.build();
 String key = "testKey";
 String value = "testValue";
 cache.put(key, value);
 assertEquals(value, cache.get(key));
 cache.put(key, "newValue");
 assertEquals("newValue", cache.get(key));
 sleep(31000); // Wait for expiration and refresh
 assertEquals("newValue", cache.get(key));
 }
}