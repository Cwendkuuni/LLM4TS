package com.google.common.cache;

import com.google.common.annotations.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;

@Beta
public class CacheBuilderSpecTest {

 private static final String VALID_CACHE_SPEC = "initialCapacity=10, maximumSize=100, maximumWeight=1000, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=1h, refreshAfterWrite=1m";
 private static final String INVALID_CACHE_SPEC = "invalidKey=invalidValue";
 private static final String MISSING_VALUE_CACHE_SPEC = "initialCapacity=10, maximumSize=";
 private static final String DUPLICATE_KEY_CACHE_SPEC = "initialCapacity=10, maximumSize=100, maximumSize=200";

 @VisibleForTesting
 static CacheBuilderSpec createValidCacheBuilderSpec() {
 return CacheBuilderSpec.parse(VALID_CACHE_SPEC);
 }

 @VisibleForTesting
 static CacheBuilderSpec createInvalidCacheBuilderSpec() {
 return CacheBuilderSpec.parse(INVALID_CACHE_SPEC);
 }

 @VisibleForTesting
 static CacheBuilderSpec createMissingValueCacheBuilderSpec() {
 return CacheBuilderSpec.parse(MISSING_VALUE_CACHE_SPEC);
 }

 @VisibleForTesting
 static CacheBuilderSpec createDuplicateKeyCacheBuilderSpec() {
 return CacheBuilderSpec.parse(DUPLICATE_KEY_CACHE_SPEC);
 }

 @Test
 public void testParseValidCacheBuilderSpec() {
 CacheBuilderSpec cacheBuilderSpec = createValidCacheBuilderSpec();
 assertEquals(10, cacheBuilderSpec.initialCapacity);
 assertEquals(100, cacheBuilderSpec.maximumSize);
 assertEquals(1000, cacheBuilderSpec.maximumWeight);
 assertEquals(10, cacheBuilderSpec.concurrencyLevel);
 assertEquals(LocalCache.Strength.WEAK, cacheBuilderSpec.keyStrength);
 assertEquals(LocalCache.Strength.SOFT, cacheBuilderSpec.valueStrength);
 assertTrue(cacheBuilderSpec.recordStats);
 assertEquals(Duration.ofDays(1), cacheBuilderSpec.accessExpirationTimeUnit, cacheBuilderSpec.accessExpirationDuration);
 assertEquals(Duration.ofHours(1), cacheBuilderSpec.writeExpirationTimeUnit, cacheBuilderSpec.writeExpirationDuration);
 assertEquals(Duration.ofMinutes(1), cacheBuilderSpec.refreshTimeUnit, cacheBuilderSpec.refreshDuration);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidCacheBuilderSpec() {
 CacheBuilderSpec cacheBuilderSpec = createInvalidCacheBuilderSpec();
 fail("Unexpectedly created a CacheBuilderSpec instance with an invalid specification.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingValueCacheBuilderSpec() {
 CacheBuilderSpec cacheBuilderSpec = createMissingValueCacheBuilderSpec();
 fail("Unexpectedly created a CacheBuilderSpec instance with a missing value.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseDuplicateKeyCacheBuilderSpec() {
 CacheBuilderSpec cacheBuilderSpec = createDuplicateKeyCacheBuilderSpec();
 fail("Unexpectedly created a CacheBuilderSpec instance with a duplicate key.");
 }

 @Test
 public void testToParsableString() {
 CacheBuilderSpec cacheBuilderSpec = createValidCacheBuilderSpec();
 assertEquals(VALID_CACHE_SPEC, cacheBuilderSpec.toParsableString());
 }

 @Test
 public void testToString() {
 CacheBuilderSpec cacheBuilderSpec = createValidCacheBuilderSpec();
 String expectedToString = String.format("CacheBuilderSpec{specification='%s'}", VALID_CACHE_SPEC);
 assertEquals(expectedToString, cacheBuilderSpec.toString());
 }

 @Test
 public void testHashCode() {
 CacheBuilderSpec cacheBuilderSpec1 = createValidCacheBuilderSpec();
 CacheBuilderSpec cacheBuilderSpec2 = createValidCacheBuilderSpec();
 assertEquals(cacheBuilderSpec1.hashCode(), cacheBuilderSpec2.hashCode());
 }

 @Test
 public void testEquals() {
 CacheBuilderSpec cacheBuilderSpec1 = createValidCacheBuilderSpec();
 CacheBuilderSpec cacheBuilderSpec2 = createValidCacheBuilderSpec();
 assertTrue(cacheBuilderSpec1.equals(cacheBuilderSpec2));
 }

 @Test
 public void testEqualsNull() {
 CacheBuilderSpec cacheBuilderSpec = createValidCacheBuilderSpec();
 assertFalse(cacheBuilderSpec.equals(null));
 }

 @Test
 public void testEqualsDifferentObject() {
 CacheBuilderSpec cacheBuilderSpec = createValidCacheBuilderSpec();
 assertFalse(cacheBuilderSpec.equals(new Object()));
 }
}