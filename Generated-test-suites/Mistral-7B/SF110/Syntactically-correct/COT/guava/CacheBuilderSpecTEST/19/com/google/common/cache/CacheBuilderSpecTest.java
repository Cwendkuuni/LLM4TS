package com.example.cachebuilder; // replace with your test package

import static org.junit.Assert.*;
import com.google.common.cache.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Test
 public void testParse() {
  spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, expireAfterWrite=1h, recordStats=true");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertEquals("expected initial capacity", 5, builder.initialCapacity());
  assertEquals("expected maximum size", 10, builder.maximumSize());
  assertEquals("expected concurrency level", 3, builder.concurrencyLevel());
  assertEquals("expected weak keys", CacheBuilder.WeakKeysStrategy.weakKeys(), builder.strength());
  assertEquals("expected expire after write", 1, builder.expireAfterWrite(TimeUnit.HOURS));
  assertTrue("expected record stats", builder.recordStats());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecification() {
  spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, expireAfterWrite=1h, recordStats=true, invalidKey=value");
 }

 @Test
 public void testParseWithEmptySpec() {
  spec = CacheBuilderSpec.parse("");
  assertNull("expected null for empty spec", spec);
 }

 @Test
 public void testToParsableString() {
  spec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, expireAfterWrite=1h, recordStats=true");
  assertEquals("expected parsable string to be same as specification", "initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, expireAfterWrite=1h, recordStats=true", spec.toParsableString());
 }

 @Test
 public void testToCacheBuilderWithNoSettings() {
  spec = new CacheBuilderSpec("");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertNull("expected null for no settings", builder);
 }

 @Test
 public void testToCacheBuilderWithInitialCapacityOnly() {
  spec = new CacheBuilderSpec("initialCapacity=5");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertEquals("expected initial capacity in cache builder", 5, builder.initialCapacity());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToCacheBuilderWithInvalidInitialCapacity() {
  spec = new CacheBuilderSpec("initialCapacity=foo");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
 }

 @Test
 public void testToCacheBuilderWithMaximumSizeOnly() {
  spec = new CacheBuilderSpec("maximumSize=100");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertEquals("expected maximum size in cache builder", 100, builder.maximumSize());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToCacheBuilderWithInvalidMaximumSize() {
  spec = new CacheBuilderSpec("maximumSize=foo");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
 }

 @Test
 public void testToCacheBuilderWithConcurrencyLevelOnly() {
  spec = new CacheBuilderSpec("concurrencyLevel=3");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertEquals("expected concurrency level in cache builder", 3, builder.concurrencyLevel());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToCacheBuilderWithInvalidConcurrencyLevel() {
  spec = new CacheBuilderSpec("concurrencyLevel=foo");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
 }

 @Test
 public void testToCacheBuilderWithWeakKeys() {
  spec = new CacheBuilderSpec("weakKeys");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertEquals("expected weak keys in cache builder", CacheBuilder.WeakKeysStrategy.weakKeys(), builder.strength());
 }

 @Test
 public void testToCacheBuilderWithSoftValues() {
  spec = new CacheBuilderSpec("softValues");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertEquals("expected soft values in cache builder", CacheBuilder.SoftValuesStrategy.softValues(), builder.valueAccessStrategy());
 }

 @Test
 public void testToCacheBuilderWithWeakValues() {
  spec = new CacheBuilderSpec("weakValues");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertEquals("expected weak values in cache builder", CacheBuilder.WeakValuesStrategy.weakValues(), builder.valueExpirationStrategy());
 }

 @Test(expected = AssertionError.class)
 public void testToCacheBuilderWithUnknownKeyStrength() {
  spec = new CacheBuilderSpec("unknownStrength");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
 }

 @Test
 public void testToCacheBuilderWithRecordStats() {
  spec = new CacheBuilderSpec("recordStats=true");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertTrue("expected record stats in cache builder", builder.recordStats());
 }

 @Test
 public void testToCacheBuilderWithExpireAfterWrite() {
  spec = new CacheBuilderSpec("expireAfterWrite=1h");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertEquals("expected expire after write duration", 1, builder.expireAfterWrite(TimeUnit.HOURS));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToCacheBuilderWithInvalidExpireAfterWriteDuration() {
  spec = new CacheBuilderSpec("expireAfterWrite=foo");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
 }

 @Test
 public void testToCacheBuilderWithExpireAfterAccess() {
  spec = new CacheBuilderSpec("expireAfterAccess=1d");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertEquals("expected expire after access duration", 1, builder.expireAfterAccess(TimeUnit.DAYS));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToCacheBuilderWithInvalidExpireAfterAccessDuration() {
  spec = new CacheBuilderSpec("expireAfterAccess=foo");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
 }

 @Test
 public void testToCacheBuilderWithRefreshAfterWrite() {
  spec = new CacheBuilderSpec("refreshAfterWrite=1h");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertEquals("expected refresh after write duration", 1, builder.refreshAfterWrite(TimeUnit.HOURS));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToCacheBuilderWithInvalidRefreshAfterWriteDuration() {
  spec = new CacheBuilderSpec("refreshAfterWrite=foo");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
 }
}