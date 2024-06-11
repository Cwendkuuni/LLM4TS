package com.google.common.cache;

import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import java.util.concurrent.*;
import org.junit.*;

public class CacheBuilderSpecTest {

 @Test
 public void testParseEmptySpec() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse("");
  assertEquals(CacheBuilderSpec.disableCaching(), spec);
 }

 @Test
 public void testParseValidSpec() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, concurrencyLevel=5, weakKeys, recordStats=true, expireAfterWrite=30s, expireAfterAccess=2m");
  CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
  assertEquals(10, builder.initialCapacity());
  assertEquals(50, builder.maximumSize());
  assertEquals(5, builder.concurrencyLevel());
  assertTrue(builder.weakKeys());
  assertTrue(builder.recordStats());
  assertEquals(30, TimeUnit.SECONDS.toSeconds(builder.expireAfterWrite()));
  assertEquals(2, TimeUnit.MINUTES.toSeconds(builder.expireAfterAccess()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidKeyValuePair() {
  CacheBuilderSpec.parse("invalidKey=invalidValue");
 }

 @Test
 public void testParseUnknownKey() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse("unknownKey=value");
  assertEquals(CacheBuilderSpec.disableCaching(), spec);
 }

 @Test
 public void testParseInvalidDurationFormat() {
  CacheBuilderSpec.parse("invalidDurationFormat=d");
 }

 @Test
 public void testParseInvalidValueType() {
  CacheBuilderSpec.parse("maximumSize=invalidValue");
 }

 @Test
 public void testParseDuplicateKey() {
  CacheBuilderSpec.parse("initialCapacity=10, initialCapacity=20");
 }

 @Test
 public void testParseMaximumSizeAndMaximumWeight() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10, maximumWeight=20");
  assertEquals("Both maximumSize and maximumWeight should be null after parsing", null, spec.maximumSize);
  assertEquals("Both maximumSize and maximumWeight should be null after parsing", null, spec.maximumWeight);
  try {
   spec.toCacheBuilder();
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   assertEquals("Unable to set both maximumSize and maximumWeight", e.getMessage());
  }
 }

 @Test
 public void testParseConcurrencyLevelAndKeyStrength() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=10, weakKeys");
  assertEquals(10, spec.concurrencyLevel);
  assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 }

 @Test
 public void testParseConcurrencyLevelAndValueStrength() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=10, weakValues");
  assertEquals(10, spec.concurrencyLevel);
  assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
 }

 @Test
 public void testParseConcurrencyLevelAndRecordStats() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=10, recordStats=true");
  assertEquals(10, spec.concurrencyLevel);
  assertTrue(spec.recordStats);
 }

 @Test
 public void testParseConcurrencyLevelAndAll() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=10, initialCapacity=5, maximumSize=10, maximumWeight=5, weakKeys, recordStats=true, expireAfterWrite=30s, expireAfterAccess=2m, refreshAfterWrite=1h");
  assertEquals(5, spec.initialCapacity);
  assertEquals(10, spec.maximumSize);
  assertEquals(5, spec.maximumWeight);
  assertEquals(10, spec.concurrencyLevel);
  assertTrue(spec.keyStrength == LocalCache.Strength.WEAK);
  assertTrue(spec.valueStrength == LocalCache.Strength.WEAK);
  assertTrue(spec.recordStats);
  assertEquals(30, TimeUnit.SECONDS.toSeconds(spec.writeExpirationDuration));
  assertEquals(2, TimeUnit.MINUTES.toSeconds(spec.accessExpirationDuration));
  assertEquals(1, TimeUnit.HOURS.toSeconds(spec.refreshDuration));
 }

 @Test
 public void testParseInvalidExpirationTimeUnit() {
  try {
   CacheBuilderSpec.parse("expireAfterWrite=30InvalidUnits");
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   assertEquals("Invalid expiration time unit", e.getMessage());
  }
 }

 @Test
 public void testParseZeroExpirationDuration() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=0s");
  assertNotNull(spec.writeExpirationDuration);
  assertEquals(0, spec.writeExpirationDuration.longValue());
 }

 @Test
 public void testParseNegativeExpirationDuration() {
  try {
   CacheBuilderSpec.parse("expireAfterWrite=-1s");
   fail("Expected IllegalArgumentException");
  } catch (IllegalArgumentException e) {
   assertEquals("Expiration duration must be non-negative", e.getMessage());
  }
 }
}