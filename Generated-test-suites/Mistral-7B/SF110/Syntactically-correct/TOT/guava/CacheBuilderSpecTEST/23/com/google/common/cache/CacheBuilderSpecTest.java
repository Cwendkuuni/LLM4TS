package com.google.common.cache;

import static org.junit.Assert.*;
import static com.google.common.cache.CacheBuilder.*;
import static com.google.common.base.Preconditions.*;

import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.junit.Test;

public class CacheBuilderSpecTest {

 private static final Function<String, CacheBuilderSpec> CACHE_BUILDER_SPEC_PARSER = CacheBuilderSpec::parse;

 @Test
 public void testParseValidSpecification() {
 final String cacheSpec = "initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys(), weakValues(), expireAfterWrite=1d, expireAfterAccess=2h";
 final CacheBuilderSpec cacheBuilderSpec = CACHE_BUILDER_SPEC_PARSER.apply(cacheSpec);
 assertEquals(10, cacheBuilderSpec.initialCapacity);
 assertEquals(20, cacheBuilderSpec.maximumSize);
 assertEquals(5, cacheBuilderSpec.concurrencyLevel);
 assertTrue(cacheBuilderSpec.keyStrength == LocalCache.Strength.WEAK);
 assertTrue(cacheBuilderSpec.valueStrength == LocalCache.Strength.WEAK);
 assertNotNull(cacheBuilderSpec.writeExpirationDuration);
 assertEquals(TimeUnit.DAYS, cacheBuilderSpec.writeExpirationTimeUnit);
 assertNotNull(cacheBuilderSpec.accessExpirationDuration);
 assertEquals(TimeUnit.HOURS, cacheBuilderSpec.accessExpirationTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecification() {
 final String cacheSpec = "initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys(), weakValues(), expireAfterWrite=1d, expireAfterAccess=2h, invalidKey=invalidValue";
 final CacheBuilderSpec cacheBuilderSpec = CACHE_BUILDER_SPEC_PARSER.apply(cacheSpec);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testParseEmptySpecification() {
 final String emptyCacheSpec = "";
 final CacheBuilderSpec cacheBuilderSpec = CACHE_BUILDER_SPEC_PARSER.apply(emptyCacheSpec);
 assertNull(cacheBuilderSpec.initialCapacity);
 assertNull(cacheBuilderSpec.maximumSize);
 assertNull(cacheBuilderSpec.maximumWeight);
 assertNull(cacheBuilderSpec.concurrencyLevel);
 assertNull(cacheBuilderSpec.keyStrength);
 assertNull(cacheBuilderSpec.valueStrength);
 assertNull(cacheBuilderSpec.recordStats);
 assertNull(cacheBuilderSpec.writeExpirationDuration);
 assertNull(cacheBuilderSpec.writeExpirationTimeUnit);
 assertNull(cacheBuilderSpec.accessExpirationDuration);
 assertNull(cacheBuilderSpec.accessExpirationTimeUnit);
 assertNull(cacheBuilderSpec.refreshDuration);
 assertNull(cacheBuilderSpec.refreshTimeUnit);
 }

 @Test
 public void testDisableCaching() {
 final CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.disableCaching();
 assertTrue(cacheBuilderSpec.maximumSize == 0);
 }

 @Test
 public void testToCacheBuilder() {
 final String cacheSpec = "initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys(), weakValues(), expireAfterWrite=1d, expireAfterAccess=2h";
 final CacheBuilderSpec cacheBuilderSpec = CACHE_BUILDER_SPEC_PARSER.apply(cacheSpec);
 final CacheBuilder<Integer, String> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
 assertNotNull(cacheBuilder);
 assertEquals(cacheBuilderSpec.initialCapacity, cacheBuilder.initialCapacity());
 assertEquals(cacheBuilderSpec.maximumSize, cacheBuilder.maximumSize());
 assertEquals(cacheBuilderSpec.concurrencyLevel, cacheBuilder.concurrencyLevel());
 assertTrue(cacheBuilder.weakKeys());
 assertTrue(cacheBuilder.weakValues());
 assertNotNull(cacheBuilder.expireAfterWrite(cacheBuilderSpec.writeExpirationDuration, cacheBuilderSpec.writeExpirationTimeUnit));
 assertNotNull(cacheBuilder.expireAfterAccess(cacheBuilderSpec.accessExpirationDuration, cacheBuilderSpec.accessExpirationTimeUnit));
 assertNull(cacheBuilder.refreshAfterWrite(cacheBuilderSpec.refreshDuration, cacheBuilderSpec.refreshTimeUnit));
 }

 @Test
 public void testToParsableString() {
 final String cacheSpec = "initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys(), weakValues(), expireAfterWrite=1d, expireAfterAccess=2h";
 final CacheBuilderSpec cacheBuilderSpec = CACHE_BUILDER_SPEC_PARSER.apply(cacheSpec);
 assertEquals(cacheSpec, cacheBuilderSpec.toParsableString());
 }
}