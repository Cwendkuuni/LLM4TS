package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.LocalCache;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

@Beta
public class CacheBuilderSpecTest {

 private static final String DEFAULT_SPEC = "initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h";
 private static final String DISABLE_CACHE_SPEC = "maximumSize=0";

 @Test
 public void testParseCacheBuilderSpec() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse(DEFAULT_SPEC);
  assertEquals(10, spec.initialCapacity);
  assertEquals(16, spec.concurrencyLevel);
  assertEquals(1000, spec.maximumSize);
  assertEquals(10000, spec.maximumWeight);
  assertNull(spec.keyStrength);
  assertNull(spec.valueStrength);
  assertTrue(spec.recordStats);
  assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
  assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
  assertEquals(TimeUnit.HOURS, spec.refreshTimeUnit);

  CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
  assertEquals(CacheBuilder.newBuilder().initialCapacity(10).concurrencyLevel(16).maximumSize(1000).maximumWeight(10000).weakKeys().weakValues().recordStats().expireAfterWrite(1, TimeUnit.HOURS).expireAfterAccess(1, TimeUnit.MINUTES).refreshAfterWrite(1, TimeUnit.HOURS), cacheBuilder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidCacheBuilderSpec() {
  String invalidSpec = "initialCapacity=10, concurrencyLevel=16, maximumSize=foo, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h, invalidKey=bar";
  CacheBuilderSpec.parse(invalidSpec);
 }

 @Test
 public void testParseDisableCaching() {
  CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
  assertEquals(CacheBuilderSpec.parse("maximumSize=0"), spec);
  CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
  assertEquals(CacheBuilder.newBuilder().maximumSize(0), cacheBuilder);
 }

 @Test
 public void testParseEmptySpec() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse("");
  assertNull(spec.initialCapacity);
  assertNull(spec.concurrencyLevel);
  assertNull(spec.maximumSize);
  assertNull(spec.maximumWeight);
  assertNull(spec.keyStrength);
  assertNull(spec.valueStrength);
  assertFalse(spec.recordStats);
  assertNull(spec.writeExpirationTimeUnit);
  assertNull(spec.accessExpirationTimeUnit);
  assertNull(spec.refreshTimeUnit);
  CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
  assertEquals(CacheBuilder.newBuilder(), cacheBuilder);
 }

 @Test
 public void testToString() {
  CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h");
  assertEquals(DEFAULT_SPEC, spec.toParsableString());
  assertEquals("CacheBuilderSpec{toParsableString='initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, keyStrength=null, valueStrength=null, recordStats=true, writeExpirationDuration=0, writeExpirationTimeUnit=null, accessExpirationDuration=0, accessExpirationTimeUnit=null, refreshDuration=0, refreshTimeUnit=null, specification='initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h'}", spec.toString());
 }

 @Test
 public void testEquals() {
  CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h");
  CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h");
  assertTrue(spec1.equals(spec2));

  CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h");
  spec3.keyStrength = LocalCache.Strength.SOFT;
  assertFalse(spec1.equals(spec3));

  CacheBuilderSpec spec4 = new CacheBuilderSpec("initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h");
  spec4.writeExpirationDuration = 1;
  spec4.writeExpirationTimeUnit = TimeUnit.MINUTES;
  assertFalse(spec1.equals(spec4));

  CacheBuilderSpec spec5 = new CacheBuilderSpec("initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h");
  spec5.recordStats = false;
  assertFalse(spec1.equals(spec5));
 }

 @Test
 public void testHashCode() {
  CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h");
  int hashCode1 = spec1.hashCode();

  CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h");
  int hashCode2 = spec2.hashCode();
  assertEquals(hashCode1, hashCode2);

  CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h");
  spec3.keyStrength = LocalCache.Strength.SOFT;
  int hashCode3 = spec3.hashCode();
  assertNotEquals(hashCode1, hashCode3);

  CacheBuilderSpec spec4 = new CacheBuilderSpec("initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h");
  spec4.writeExpirationDuration = 1;
  spec4.writeExpirationTimeUnit = TimeUnit.MINUTES;
  int hashCode4 = spec4.hashCode();
  assertNotEquals(hashCode1, hashCode4);

  CacheBuilderSpec spec5 = new CacheBuilderSpec("initialCapacity=10, concurrencyLevel=16, maximumSize=1000, maximumWeight=10000, weakKeys(), weakValues(), recordStats(), expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1h");
  spec5.recordStats = false;
  int hashCode5 = spec5.hashCode();
  assertNotEquals(hashCode1, hashCode5);
 }
}