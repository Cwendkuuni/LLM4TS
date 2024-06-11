package com.google.common.cache;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class CacheBuilderSpecTest {

 @Test
 public void testParse() {
  String cacheBuilderSpecification = "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=5m, refreshAfterWrite=30s";
  CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

  assertEquals(5, spec.getInitialCapacity());
  assertEquals(10, spec.getMaximumSize());
  assertEquals(20, spec.getMaximumWeight());
  assertEquals(3, spec.getConcurrencyLevel());
  assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
  assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
  assertTrue(spec.isRecordStats());

  assertEquals(1L, TimeUnit.HOURS.toMillis(spec.getAccessExpirationDuration()));
  assertEquals(TimeUnit.HOURS, spec.getAccessExpirationTimeUnit());
  assertEquals(5L, TimeUnit.MINUTES.toMillis(spec.getWriteExpirationDuration()));
  assertEquals(TimeUnit.MINUTES, spec.getWriteExpirationTimeUnit());
  assertEquals(30L, TimeUnit.SECONDS.toMillis(spec.getRefreshDuration()));
  assertEquals(TimeUnit.SECONDS, spec.getRefreshTimeUnit());
 }

 @Test
 public void testParseEmptySpecification() {
  String cacheBuilderSpecification = "";
  try {
   CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
   fail("expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   assertEquals("blank specification", e.getMessage());
  }
 }

 @Test
 public void testParseInvalidSpecification() {
  String cacheBuilderSpecification = "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, recordStats=true, expireAfterAccess=1h, expireAfterWrite=5m, refreshAfterWrite=30s, invalidKey=value";
  try {
   CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
   fail("expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   assertEquals("unknown key invalidKey", e.getMessage());
  }
 }

 @Test
 public void testToCacheBuilder() {
  String cacheBuilderSpecification = "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=5m, refreshAfterWrite=30s";
  CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
  CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

  assertEquals(CacheBuilder.newBuilder().initialCapacity(5), cacheBuilder);
  assertEquals(CacheBuilder.newBuilder().maximumSize(10), cacheBuilder.asBuilder());
  assertEquals(CacheBuilder.newBuilder().maximumWeight(20), cacheBuilder.asBuilder().asBuilder());
  assertEquals(CacheBuilder.newBuilder().concurrencyLevel(3), cacheBuilder.asBuilder().asBuilder().asBuilder());
  assertEquals(CacheBuilder.newBuilder().weakKeys(), cacheBuilder.asBuilder().asBuilder().asBuilder().asBuilder());
  assertEquals(CacheBuilder.newBuilder().softValues(), cacheBuilder.asBuilder().asBuilder().asBuilder().asBuilder().asBuilder());
  assertTrue(cacheBuilder.asBuilder().asBuilder().asBuilder().asBuilder().asBuilder().recordStats());

  assertEquals(1L, TimeUnit.HOURS.toMillis(cacheBuilder.expireAfterAccess(TimeUnit.HOURS)), cacheBuilder.expireAfterAccess(TimeUnit.HOURS));
  assertEquals(5L, TimeUnit.MINUTES.toMillis(cacheBuilder.expireAfterWrite(TimeUnit.MINUTES)), cacheBuilder.expireAfterWrite(TimeUnit.MINUTES));
  assertEquals(30L, TimeUnit.SECONDS.toMillis(cacheBuilder.refreshAfterWrite(TimeUnit.SECONDS)), cacheBuilder.refreshAfterWrite(TimeUnit.SECONDS));
 }

 @Test
 public void testToCacheBuilderWithDefaultValues() {
  String cacheBuilderSpecification = "maximumSize=10, maximumWeight=20, concurrencyLevel=3, recordStats=true";
  CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
  CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

  assertEquals(CacheBuilder.newBuilder(), cacheBuilder);
  assertEquals(CacheBuilder.newBuilder().maximumSize(10), cacheBuilder.asBuilder());
  assertEquals(CacheBuilder.newBuilder().maximumWeight(20), cacheBuilder.asBuilder().asBuilder());
  assertEquals(CacheBuilder.newBuilder().concurrencyLevel(3), cacheBuilder.asBuilder().asBuilder().asBuilder());
  assertTrue(cacheBuilder.asBuilder().asBuilder().asBuilder().recordStats());

  assertNull(cacheBuilder.expireAfterAccess(TimeUnit.HOURS));
  assertNull(cacheBuilder.expireAfterWrite(TimeUnit.MINUTES));
  assertNull(cacheBuilder.refreshAfterWrite(TimeUnit.SECONDS));
 }
}