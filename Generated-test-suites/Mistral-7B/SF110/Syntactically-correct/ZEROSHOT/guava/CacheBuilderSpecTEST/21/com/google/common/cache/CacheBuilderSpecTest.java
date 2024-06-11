package com.example.cache; // replace with your package name

import static org.junit.Assert.*;
import com.google.common.cache.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

 private static final String VALID_SPEC = "initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=30s, expireAfterWrite=60s, refreshAfterWrite=1h";
 private static final String INVALID_SPEC = "invalidKey=invalidValue";
 private static final String MISSING_VALUE_SPEC = "initialCapacity=10, keyStrength=weak";

 @Test
 public void testParseValidSpec() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse(VALID_SPEC);
  assertEquals(VALID_SPEC, spec.toParsableString());

  assertEquals(10, spec.initialCapacity);
  assertEquals(50L, spec.maximumSize);
  assertEquals(100L, spec.maximumWeight);
  assertEquals(10, spec.concurrencyLevel);
  assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
  assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
  assertTrue(spec.recordStats);

  assertEquals(30L, TimeUnit.SECONDS.toNanos(spec.accessExpirationDuration));
  assertEquals(TimeUnit.SECONDS, spec.accessExpirationTimeUnit);
  assertEquals(60L, TimeUnit.SECONDS.toNanos(spec.writeExpirationDuration));
  assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
  assertEquals(1L * 60L * 60L * 1000L * 1000L, TimeUnit.HOURS.toNanos(1)); // 1 hour in nanoseconds
  assertEquals(TimeUnit.HOURS, spec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpec() {
  try {
   CacheBuilderSpec.parse(INVALID_SPEC);
  } catch (IllegalArgumentException e) {
   assertEquals("key invalidKey does not take values", e.getMessage());
  }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingValueSpec() {
  try {
   CacheBuilderSpec.parse(MISSING_VALUE_SPEC);
  } catch (IllegalArgumentException e) {
   assertEquals("key valueStrength takes no value", e.getMessage());
  }
 }

 @Test
 public void testToCacheBuilder() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse(VALID_SPEC);
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();

  assertTrue(builder.initialCapacity(10));
  assertTrue(builder.maximumSize(50));
  assertTrue(builder.maximumWeight(100));
  assertTrue(builder.concurrencyLevel(10));
  assertTrue(builder.weakKeys());
  assertTrue(builder.softValues());
  assertTrue(builder.recordStats());

  assertTrue(builder.expireAfterAccess(30, TimeUnit.SECONDS));
  assertTrue(builder.expireAfterWrite(60, TimeUnit.SECONDS));
  assertTrue(builder.refreshAfterWrite(1, TimeUnit.HOURS));
 }
}