package com.google.common.cache;

import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

 @Test
 public void testParseValidSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, concurrencyLevel=5, weakKeys, softValues, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=1m");

 assertEquals("initialCapacity=5, maximumSize=10, concurrencyLevel=5, weakKeys, softValues, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=1m", spec.toParsableString());

 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(5, spec.concurrencyLevel);
 assertNull(spec.maximumWeight);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 assertEquals(1L, TimeUnit.HOURS.toNanos(1));
 assertEquals(2L, TimeUnit.DAYS.toNanos(2));
 assertEquals(1L, TimeUnit.MINUTES.toNanos(1));
 }

 @Test
 public void testParseEmptySpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("");
 assertNull(spec);
 }

 @Test
 public void testParseInvalidSpec() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidKey=invalidValue");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseMultipleKeyValuePair() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, concurrencyLevel=5, weakKeys, softValues, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=1m, maximumWeight=3");
 fail("Expected AssertionError");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testParseInvalidValueType() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidKey=invalidValue");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 checkArgument(e.getMessage().contains("key invalidKey value set to invalidValue, must be integer"));
 }
 }

 @Test
 public void testParseInvalidTimeUnit() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1InvalidUnit");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 checkArgument(e.getMessage().contains("key expireAfterAccess invalid format. was 1InvalidUnit, must end with one of [dDhHmMsS]"));
 }
 }

 @Test
 public void testParseDisableCaching() {
 CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
 assertEquals("maximumSize=0", spec.toParsableString());
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, concurrencyLevel=5, weakKeys, softValues, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=1m");

 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

 assertEquals(5, cacheBuilder.initialCapacity());
 assertEquals(10, cacheBuilder.maximumSize());
 assertEquals(5, cacheBuilder.concurrencyLevel());
 assertEquals(CacheBuilder.WeakKeysPolicy.WEAK, cacheBuilder.weakKeys());
 assertEquals(CacheBuilder.ValueReferencePolicy.SOFT, cacheBuilder.softValues());
 assertEquals(1L, cacheBuilder.expireAfterAccess(TimeUnit.HOURS));
 assertEquals(2L, cacheBuilder.expireAfterWrite(TimeUnit.DAYS));
 assertEquals(1L, cacheBuilder.refreshAfterWrite(TimeUnit.MINUTES));
 }
}