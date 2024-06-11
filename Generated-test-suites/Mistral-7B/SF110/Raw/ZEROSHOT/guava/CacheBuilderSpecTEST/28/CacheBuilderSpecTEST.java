package com.google.common.cache;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

public class CacheBuilderSpecTest {

 @Test
 public void testParseCacheBuilderSpec() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=2,"
           + " keyStrength=WEAK, valueStrength=SOFT, recordStats=true,"
           + " writeExpirationDuration=1h, accessExpirationDuration=1m, refreshDuration=30s");

  assertEquals("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=2,"
          + " keyStrength=WEAK, valueStrength=SOFT, recordStats=true,"
          + " writeExpirationDuration=1h, accessExpirationDuration=1m, refreshDuration=30s", spec.toParsableString());

  assertEquals(5, spec.initialCapacity);
  assertEquals(10, spec.maximumSize);
  assertEquals(20, spec.maximumWeight);
  assertEquals(2, spec.concurrencyLevel);
  assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
  assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
  assertTrue(spec.recordStats);
  assertEquals(1, spec.writeExpirationDuration.toHours());
  assertEquals(1, spec.accessExpirationDuration.toMinutes());
  assertEquals(30, spec.refreshDuration.toSeconds());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseCacheBuilderSpecInvalidFormat() {
  try {
   CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=2,"
           + " keyStrength=WEAK, valueStrength=SOFT, recordStats=true,"
           + " writeExpirationDuration=1h, accessExpirationDuration=1m, refreshDuration=30s, foo=bar");
   fail("IllegalArgumentException expected");
  } catch (IllegalArgumentException e) {
   assertEquals("key foo does not take values", e.getMessage());
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseCacheBuilderSpecInvalidValueType() {
  try {
   CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10l, maximumWeight=20, concurrencyLevel=2,"
           + " keyStrength=WEAK, valueStrength=SOFT, recordStats=true,"
           + " writeExpirationDuration=1h, accessExpirationDuration=1m, refreshDuration=30s");
   fail("IllegalArgumentException expected");
  } catch (IllegalArgumentException e) {
   assertEquals("key maximumSize value set to 10l, must be integer", e.getMessage());
  }
 }

 @Test
 public void testToCacheBuilder() {
  CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=2,"
          + " keyStrength=WEAK, valueStrength=SOFT, recordStats=true,"
          + " writeExpirationDuration=1h, accessExpirationDuration=1m, refreshDuration=30s");

  CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

  assertEquals(5, cacheBuilder.initialCapacity());
  assertEquals(10, cacheBuilder.maximumSize());
  assertEquals(20, cacheBuilder.maximumWeight());
  assertEquals(2, cacheBuilder.concurrencyLevel());
  assertTrue(cacheBuilder.recordStats());

  assertNull(cacheBuilder.expireAfterWrite());
  assertNull(cacheBuilder.expireAfterAccess());
  assertNull(cacheBuilder.refreshAfterWrite());

  CacheBuilder<Object, Object> cacheBuilderWithExpireAfterWrite = spec.toCacheBuilder();
  assertEquals(1, cacheBuilderWithExpireAfterWrite.expireAfterWrite(1, TimeUnit.HOURS).getExpirationTime().toHours());

  CacheBuilder<Object, Object> cacheBuilderWithExpireAfterAccess = spec.toCacheBuilder();
  assertEquals(1, cacheBuilderWithExpireAfterAccess.expireAfterAccess(1, TimeUnit.MINUTES).getExpirationTime().toMinutes());

  CacheBuilder<Object, Object> cacheBuilderWithRefreshAfterWrite = spec.toCacheBuilder();
  assertEquals(30, cacheBuilderWithRefreshAfterWrite.refreshAfterWrite(30, TimeUnit.SECONDS).getDuration().toSeconds());
 }

 @Test
 public void testEquals() {
  CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=2,"
          + " keyStrength=WEAK, valueStrength=SOFT, recordStats=true,"
          + " writeExpirationDuration=1h, accessExpirationDuration=1m, refreshDuration=30s");

  CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=2,"
          + " keyStrength=WEAK, valueStrength=SOFT, recordStats=true,"
          + " writeExpirationDuration=1h, accessExpirationDuration=1m, refreshDuration=30s");

  assertEquals(spec1, spec2);

  CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=2,"
          + " keyStrength=WEAK, valueStrength=SOFT, recordStats=true,"
          + " writeExpirationDuration=1h, accessExpirationDuration=1m, refreshDuration=30s");

  spec1.initialCapacity = 6;

  assertNotEquals(spec1, spec3);
 }
}