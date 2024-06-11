package com.example.cachebuilder;

import static org.junit.Assert.*;

import com.google.common.cache.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

 private static final String BASIC_SPEC = "initialCapacity=10, maximumSize=5, concurrencyLevel=5";
 private static final String EMPTY_SPEC = "";
 private static final String INVALID_SPEC = "invalidKey=invalidValue";

 @Test
 public void testParse() {
  CacheBuilderSpec spec = CacheBuilderSpec.parse(BASIC_SPEC);
  assertEquals(BASIC_SPEC, spec.toParsableString());
  assertEquals(10, spec.initialCapacity);
  assertEquals(5, spec.maximumSize);
  assertEquals(5, spec.concurrencyLevel);
  assertNull(spec.maximumWeight);
  assertNull(spec.keyStrength);
  assertNull(spec.valueStrength);
  assertFalse(spec.recordStats);
  assertNull(spec.writeExpirationDuration);
  assertNull(spec.writeExpirationTimeUnit);
  assertNull(spec.accessExpirationDuration);
  assertNull(spec.accessExpirationTimeUnit);
  assertNull(spec.refreshDuration);
  assertNull(spec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseNullSpec() {
  CacheBuilderSpec.parse(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptySpec() {
  CacheBuilderSpec.parse(EMPTY_SPEC);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpec() {
  CacheBuilderSpec.parse(INVALID_SPEC);
 }

 @Test
 public void testParseWithMaximumWeight() {
  String specWithMaximumWeight = "initialCapacity=10, maximumSize=5, concurrencyLevel=5, maximumWeight=100";
  CacheBuilderSpec spec = CacheBuilderSpec.parse(specWithMaximumWeight);
  assertEquals(specWithMaximumWeight, spec.toParsableString());
  assertEquals(10, spec.initialCapacity);
  assertEquals(5, spec.maximumSize);
  assertEquals(5, spec.concurrencyLevel);
  assertEquals(100, spec.maximumWeight);
  assertNull(spec.keyStrength);
  assertNull(spec.valueStrength);
  assertFalse(spec.recordStats);
  assertNull(spec.writeExpirationDuration);
  assertNull(spec.writeExpirationTimeUnit);
  assertNull(spec.accessExpirationDuration);
  assertNull(spec.accessExpirationTimeUnit);
  assertNull(spec.refreshDuration);
  assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParseWithKeyStrength() {
  String specWithKeyStrength = "initialCapacity=10, maximumSize=5, concurrencyLevel=5, weakKeys";
  CacheBuilderSpec spec = CacheBuilderSpec.parse(specWithKeyStrength);
  assertEquals(specWithKeyStrength, spec.toParsableString());
  assertEquals(10, spec.initialCapacity);
  assertEquals(5, spec.maximumSize);
  assertEquals(5, spec.concurrencyLevel);
  assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
  assertNull(spec.valueStrength);
  assertFalse(spec.recordStats);
  assertNull(spec.writeExpirationDuration);
  assertNull(spec.writeExpirationTimeUnit);
  assertNull(spec.accessExpirationDuration);
  assertNull(spec.accessExpirationTimeUnit);
  assertNull(spec.refreshDuration);
  assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParseWithValueStrength() {
  String specWithValueStrength = "initialCapacity=10, maximumSize=5, concurrencyLevel=5, softValues";
  CacheBuilderSpec spec = CacheBuilderSpec.parse(specWithValueStrength);
  assertEquals(specWithValueStrength, spec.toParsableString());
  assertEquals(10, spec.initialCapacity);
  assertEquals(5, spec.maximumSize);
  assertEquals(5, spec.concurrencyLevel);
  assertNull(spec.keyStrength);
  assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
  assertFalse(spec.recordStats);
  assertNull(spec.writeExpirationDuration);
  assertNull(spec.writeExpirationTimeUnit);
  assertNull(spec.accessExpirationDuration);
  assertNull(spec.accessExpirationTimeUnit);
  assertNull(spec.refreshDuration);
  assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParseWithRecordStats() {
  String specWithRecordStats = "initialCapacity=10, maximumSize=5, concurrencyLevel=5, recordStats=true";
  CacheBuilderSpec spec = CacheBuilderSpec.parse(specWithRecordStats);
  assertEquals(specWithRecordStats, spec.toParsableString());
  assertEquals(10, spec.initialCapacity);
  assertEquals(5, spec.maximumSize);
  assertEquals(5, spec.concurrencyLevel);
  assertNull(spec.keyStrength);
  assertNull(spec.valueStrength);
  assertTrue(spec.recordStats);
  assertNull(spec.writeExpirationDuration);
  assertNull(spec.writeExpirationTimeUnit);
  assertNull(spec.accessExpirationDuration);
  assertNull(spec.accessExpirationTimeUnit);
  assertNull(spec.refreshDuration);
  assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParseWithExpireAfterAccess() {
  String specWithExpireAfterAccess = "initialCapacity=10, maximumSize=5, concurrencyLevel=5, expireAfterAccess=1h";
  CacheBuilderSpec spec = CacheBuilderSpec.parse(specWithExpireAfterAccess);
  assertEquals(specWithExpireAfterAccess, spec.toParsableString());
  assertEquals(10, spec.initialCapacity);
  assertEquals(5, spec.maximumSize);
  assertEquals(5, spec.concurrencyLevel);
  assertNull(spec.keyStrength);
  assertNull(spec.valueStrength);
  assertFalse(spec.recordStats);
  assertEquals(3600000, spec.accessExpirationDuration);
  assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
  assertNull(spec.writeExpirationDuration);
  assertNull(spec.writeExpirationTimeUnit);
  assertNull(spec.refreshDuration);
  assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParseWithExpireAfterWrite() {
  String specWithExpireAfterWrite = "initialCapacity=10, maximumSize=5, concurrencyLevel=5, expireAfterWrite=2d";
  CacheBuilderSpec spec = CacheBuilderSpec.parse(specWithExpireAfterWrite);
  assertEquals(specWithExpireAfterWrite, spec.toParsableString());
  assertEquals(10, spec.initialCapacity);
  assertEquals(5, spec.maximumSize);
  assertEquals(5, spec.concurrencyLevel);
  assertNull(spec.keyStrength);
  assertNull(spec.valueStrength);
  assertFalse(spec.recordStats);
  assertNull(spec.accessExpirationDuration);
  assertNull(spec.accessExpirationTimeUnit);
  assertEquals(172800000, spec.writeExpirationDuration);
  assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
  assertNull(spec.refreshDuration);
  assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParseWithRefreshAfterWrite() {
  String specWithRefreshAfterWrite = "initialCapacity=10, maximumSize=5, concurrencyLevel=5, refreshAfterWrite=3h";
  CacheBuilderSpec spec = CacheBuilderSpec.parse(specWithRefreshAfterWrite);
  assertEquals(specWithRefreshAfterWrite, spec.toParsableString());
  assertEquals(10, spec.initialCapacity);
  assertEquals(5, spec.maximumSize);
  assertEquals(5, spec.concurrencyLevel);
  assertNull(spec.keyStrength);
  assertNull(spec.valueStrength);
  assertFalse(spec.recordStats);
  assertNull(spec.accessExpirationDuration);
  assertNull(spec.accessExpirationTimeUnit);
  assertNull(spec.writeExpirationDuration);
  assertNull(spec.writeExpirationTimeUnit);
  assertEquals(10800000, spec.refreshDuration);
  assertEquals(TimeUnit.HOURS, spec.refreshTimeUnit);
  assertNull(spec.keyStrength);
  assertNull(spec.valueStrength);
 }

 @Test
 public void testToCacheBuilder() {
  String spec = "initialCapacity=10, maximumSize=5, concurrencyLevel=5, weakKeys, expireAfterWrite=1h, expireAfterAccess=30m";
  CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(spec);
  CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
  assertTrue(cacheBuilder.isInitialLoadAllowed());
  assertEquals(10, cacheBuilder.initialCapacity());
  assertEquals(5, cacheBuilder.maximumSize());
  assertEquals(5, cacheBuilder.concurrencyLevel());
  assertTrue(cacheBuilder.isRecordStats());
  assertTrue(cacheBuilder.isLoadExpectedOnExpire());
  assertEquals(1, cacheBuilder.expireAfterWriteInNanos());
  assertEquals(30 * 60 * 1000L, cacheBuilder.expireAfterAccessInNanos());
  assertEquals(CacheBuilder.AccessMode.ACCESS_SPECIFIED, cacheBuilder.accessOrder());
  assertEquals(CacheBuilder.RemovalPolicy.REMOVE, cacheBuilder.removalPolicy());
 }

 @Test
 public void testToCacheBuilderWithMaximumWeight() {
  String specWithMaximumWeight = "initialCapacity=10, maximumSize=5, concurrencyLevel=5, maximumWeight=10L, weakKeys, expireAfterWrite=1h, expireAfterAccess=30m";
  CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(specWithMaximumWeight);
  CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
  assertTrue(cacheBuilder.isInitialLoadAllowed());
  assertEquals(10, cacheBuilder.initialCapacity());
  assertEquals(5, cacheBuilder.maximumSize());
  assertEquals(5, cacheBuilder.concurrencyLevel());
  assertEquals(10, cacheBuilder.maximumWeight());
  assertTrue(cacheBuilder.isRecordStats());
  assertTrue(cacheBuilder.isLoadExpectedOnExpire());
  assertEquals(1, cacheBuilder.expireAfterWriteInNanos());
  assertEquals(30 * 60 * 1000L, cacheBuilder.expireAfterAccessInNanos());
  assertEquals(CacheBuilder.AccessMode.ACCESS_SPECIFIED, cacheBuilder.accessOrder());
  assertEquals(CacheBuilder.RemovalPolicy.REMOVE, cacheBuilder.removalPolicy());
 }

 @Test
 public void testToCacheBuilderWithRefreshAfterWrite() {
  String specWithRefreshAfterWrite = "initialCapacity=10, maximumSize=5, concurrencyLevel=5, weakKeys, expireAfterWrite=1h, expireAfterAccess=30m, refreshAfterWrite=30s";
  CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(specWithRefreshAfterWrite);
  CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
  assertTrue(cacheBuilder.isInitialLoadAllowed());
  assertEquals(10, cacheBuilder.initialCapacity());
  assertEquals(5, cacheBuilder.maximumSize());
  assertEquals(5, cacheBuilder.concurrencyLevel());
  assertTrue(cacheBuilder.isRecordStats());
  assertTrue(cacheBuilder.isLoadExpectedOnExpire());
  assertEquals(1, cacheBuilder.expireAfterWriteInNanos());
  assertEquals(30 * 60 * 1000L, cacheBuilder.expireAfterAccessInNanos());
  assertEquals(30 * 1000L, cacheBuilder.refreshAfterWriteInNanos());
  assertEquals(CacheBuilder.AccessMode.ACCESS_SPECIFIED, cacheBuilder.accessOrder());
  assertEquals(CacheBuilder.RemovalPolicy.REMOVE, cacheBuilder.removalPolicy());
 }
}