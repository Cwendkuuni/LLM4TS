package com.example.cachebuilder;

import static org.junit.Assert.*;
import com.google.common.cache.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

 private static final String BASIC_SPEC = "initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=10, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=30s, writeExpirationTimeUnit=SECONDS, accessExpirationDuration=1h, accessExpirationTimeUnit=HOURS, refreshDuration=2m, refreshTimeUnit=MINUTES";
 private static final CacheBuilderSpec CACHE_BUILDER_SPEC = CacheBuilderSpec.parse(BASIC_SPEC);

 @Test
 public void testParse() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(BASIC_SPEC);
 assertNotNull(spec);
 assertEquals(BASIC_SPEC, spec.toParsableString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidKeyValuePair() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=, recordStats=true");
 fail("An IllegalArgumentException should have been thrown");
 }

 @Test(expected = AssertionError.class)
 public void testUnknownKeyStrength() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=10, keyStrength=INVALID, valueStrength=SOFT, recordStats=true, writeExpirationDuration=30s, writeExpirationTimeUnit=SECONDS, accessExpirationDuration=1h, accessExpirationTimeUnit=HOURS, refreshDuration=2m, refreshTimeUnit=MINUTES");
 fail("An AssertionError should have been thrown");
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Integer, String> expectedCacheBuilder = CacheBuilder.newBuilder()
 .initialCapacity(10)
 .maximumSize(50)
 .maximumWeight(100)
 .concurrencyLevel(10)
 .weakKeys()
 .softValues()
 .recordStats()
 .expireAfterWrite(30, TimeUnit.SECONDS)
 .expireAfterAccess(3600, TimeUnit.SECONDS)
 .refreshAfterWrite(120, TimeUnit.SECONDS);

 CacheBuilder<Integer, String> actualCacheBuilder = CACHE_BUILDER_SPEC.toCacheBuilder();
 assertEquals(expectedCacheBuilder, actualCacheBuilder);
 }

 @Test
 public void testToString() {
 assertEquals("CacheBuilderSpec{toParsableString='initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=10, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=30s, writeExpirationTimeUnit=SECONDS, accessExpirationDuration=1h, accessExpirationTimeUnit=HOURS, refreshDuration=2m, refreshTimeUnit=MINUTES'}", CACHE_BUILDER_SPEC.toString());
 }

 @Test
 public void testHashCode() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=10, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=30s, writeExpirationTimeUnit=SECONDS, accessExpirationDuration=1h, accessExpirationTimeUnit=HOURS, refreshDuration=2m, refreshTimeUnit=MINUTES");
 CacheBuilderSpec spec2 = new CacheBuilderSpec(BASIC_SPEC);
 assertEquals(spec1.hashCode(), spec2.hashCode());
 }

 @Test
 public void testEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=10, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=30s, writeExpirationTimeUnit=SECONDS, accessExpirationDuration=1h, accessExpirationTimeUnit=HOURS, refreshDuration=2m, refreshTimeUnit=MINUTES");
 CacheBuilderSpec spec2 = new CacheBuilderSpec(BASIC_SPEC);
 assertTrue(spec1.equals(spec2));
 }
}