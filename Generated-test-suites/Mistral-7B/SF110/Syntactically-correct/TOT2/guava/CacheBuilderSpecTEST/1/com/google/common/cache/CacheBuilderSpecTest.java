package com.google.common.cache;

import static org.junit.Assert.*;
import static com.google.common.cache.CacheBuilderSpec.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

 // Test for parse method with empty string
 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptySpec() {
 CacheBuilderSpec spec = parse("");
 }

 // Test for parse method with valid specification
 @Test
 public void testParseValidSpec() {
 final String spec = "initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, recordStats, expireAfterWrite=1h";
 CacheBuilderSpec parsedSpec = parse(spec);
 assertEquals(5, parsedSpec.initialCapacity);
 assertEquals(10, parsedSpec.maximumSize);
 assertEquals(3, parsedSpec.concurrencyLevel);
 assertEquals(LocalCache.Strength.WEAK, parsedSpec.keyStrength);
 assertEquals(true, parsedSpec.recordStats);
 assertEquals(TimeUnit.HOURS, parsedSpec.writeExpirationTimeUnit);
 assertEquals(1, parsedSpec.writeExpirationDuration);
 }

 // Test for parse method with invalid key-value pair
 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidKeyValuePair() {
 final String spec = "initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, recordStats, expireAfterWrite=1h=5";
 CacheBuilderSpec parsedSpec = parse(spec);
 }

 // Test for parse method with unknown key
 @Test(expected = IllegalArgumentException.class)
 public void testParseUnknownKey() {
 final String spec = "unknownKey=5";
 CacheBuilderSpec parsedSpec = parse(spec);
 }

 // Test for toCacheBuilder method with no settings
 @Test
 public void testToCacheBuilderNoSettings() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 }

 // Test for toCacheBuilder method with initialCapacity setting
 @Test
 public void testToCacheBuilderInitialCapacity() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(5, cacheBuilder.initialCapacity());
 }

 // Test for toCacheBuilder method with maximumSize setting
 @Test
 public void testToCacheBuilderMaximumSize() {
 CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=10");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(10, cacheBuilder.maximumSize());
 }

 // Test for toCacheBuilder method with maximumWeight setting
 @Test
 public void testToCacheBuilderMaximumWeight() {
 CacheBuilderSpec spec = new CacheBuilderSpec("maximumWeight=10");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(10, cacheBuilder.maximumWeight());
 }

 // Test for toCacheBuilder method with concurrencyLevel setting
 @Test
 public void testToCacheBuilderConcurrencyLevel() {
 CacheBuilderSpec spec = new CacheBuilderSpec("concurrencyLevel=5");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(5, cacheBuilder.concurrencyLevel());
 }

 // Test for toCacheBuilder method with weakKeys setting
 @Test
 public void testToCacheBuilderWeakKeys() {
 CacheBuilderSpec spec = new CacheBuilderSpec("weakKeys");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.weakKeys());
 }

 // Test for toCacheBuilder method with softValues setting
 @Test
 public void testToCacheBuilderSoftValues() {
 CacheBuilderSpec spec = new CacheBuilderSpec("softValues");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.softValues());
 }

 // Test for toCacheBuilder method with weakValues setting
 @Test
 public void testToCacheBuilderWeakValues() {
 CacheBuilderSpec spec = new CacheBuilderSpec("weakValues");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.weakValues());
 }

 // Test for toCacheBuilder method with recordStats setting
 @Test
 public void testToCacheBuilderRecordStats() {
 CacheBuilderSpec spec = new CacheBuilderSpec("recordStats");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.recordStats());
 }

 // Test for toCacheBuilder method with expireAfterWrite setting
 @Test
 public void testToCacheBuilderExpireAfterWrite() {
 CacheBuilderSpec spec = new CacheBuilderSpec("expireAfterWrite=1h");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(TimeUnit.HOURS, cacheBuilder.expireAfterWrite());
 assertEquals(1, cacheBuilder.expireAfterWrite(TimeUnit.HOURS));
 }

 // Test for toCacheBuilder method with expireAfterAccess setting
 @Test
 public void testToCacheBuilderExpireAfterAccess() {
 CacheBuilderSpec spec = new CacheBuilderSpec("expireAfterAccess=1h");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(TimeUnit.HOURS, cacheBuilder.expireAfterAccess());
 assertEquals(1, cacheBuilder.expireAfterAccess(TimeUnit.HOURS));
 }

 // Test for toCacheBuilder method with refreshAfterWrite setting
 @Test
 public void testToCacheBuilderRefreshAfterWrite() {
 CacheBuilderSpec spec = new CacheBuilderSpec("refreshAfterWrite=1h");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(TimeUnit.HOURS, cacheBuilder.refreshAfterWrite());
 assertEquals(1, cacheBuilder.refreshAfterWrite(TimeUnit.HOURS));
 }

 // Test for toCacheBuilder method with all settings
 @Test
 public void testToCacheBuilderAllSettings() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, recordStats, expireAfterWrite=1h, expireAfterAccess=2m, refreshAfterWrite=30s");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(5, cacheBuilder.initialCapacity());
 assertEquals(10, cacheBuilder.maximumSize());
 assertEquals(3, cacheBuilder.concurrencyLevel());
 assertTrue(cacheBuilder.weakKeys());
 assertTrue(cacheBuilder.recordStats());
 assertEquals(TimeUnit.HOURS, cacheBuilder.expireAfterWrite());
 assertEquals(1, cacheBuilder.expireAfterWrite(TimeUnit.HOURS));
 assertEquals(TimeUnit.MINUTES, cacheBuilder.expireAfterAccess());
 assertEquals(2, cacheBuilder.expireAfterAccess(TimeUnit.MINUTES));
 assertEquals(TimeUnit.SECONDS, cacheBuilder.refreshAfterWrite());
 assertEquals(30, cacheBuilder.refreshAfterWrite(TimeUnit.SECONDS));
 }
}