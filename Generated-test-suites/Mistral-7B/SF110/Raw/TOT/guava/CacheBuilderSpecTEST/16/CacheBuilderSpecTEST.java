package com.google.common.cache;

import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

 private static final String CACHE_BUILDER_SPECIFICATION = "initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, weakValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h";

 @Test
 public void testParseCacheBuilderSpecification() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(CACHE_BUILDER_SPECIFICATION);

 assertEquals("initialCapacity was not set correctly", 10, spec.initialCapacity);
 assertEquals("maximumSize was not set correctly", 50, spec.maximumSize);
 assertEquals("maximumWeight was not set correctly", 100, spec.maximumWeight);
 assertEquals("concurrencyLevel was not set correctly", 5, spec.concurrencyLevel);
 assertEquals("keyStrength was not set correctly", LocalCache.Strength.WEAK, spec.keyStrength);
 assertEquals("valueStrength was not set correctly", LocalCache.Strength.WEAK, spec.valueStrength);
 assertTrue("recordStats was not set correctly", spec.recordStats);
 assertEquals("writeExpirationDuration was not set correctly", 2 * 24 * 60 * 60 * 1000L, spec.writeExpirationDuration);
 assertEquals("writeExpirationTimeUnit was not set correctly", TimeUnit.DAYS, spec.writeExpirationTimeUnit);
 assertEquals("accessExpirationDuration was not set correctly", 1 * 60 * 60 * 1000L, spec.accessExpirationDuration);
 assertEquals("accessExpirationTimeUnit was not set correctly", TimeUnit.HOURS, spec.accessExpirationTimeUnit);
 assertEquals("refreshDuration was not set correctly", 3 * 60 * 60 * 1000L, spec.refreshDuration);
 assertEquals("refreshTimeUnit was not set correctly", TimeUnit.HOURS, spec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidCacheBuilderSpecification() {
 String invalidSpecification = "initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, weakValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h, invalidKey=invalidValue";
 CacheBuilderSpec spec = CacheBuilderSpec.parse(invalidSpecification);
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testParseEmptyCacheBuilderSpecification() {
 String emptySpecification = "";
 CacheBuilderSpec spec = CacheBuilderSpec.parse(emptySpecification);
 assertNull("expected null for empty CacheBuilderSpecification", spec);
 }

 @Test
 public void testParseCacheBuilderSpecificationWithInvalidKey() {
 String invalidKeySpecification = "invalidKey=value";
 CacheBuilderSpec spec = CacheBuilderSpec.parse(invalidKeySpecification);
 assertNull("expected null for invalid key in CacheBuilderSpecification", spec);
 }

 @Test
 public void testParseCacheBuilderSpecificationWithInvalidValue() {
 String invalidValueSpecification = "initialCapacity=invalid";
 CacheBuilderSpec spec = CacheBuilderSpec.parse(invalidValueSpecification);
 assertNull("expected null for invalid value in CacheBuilderSpecification", spec);
 }

 @Test
 public void testParseCacheBuilderSpecificationWithMissingKey() {
 String missingKeySpecification = "maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h";
 CacheBuilderSpec spec = CacheBuilderSpec.parse(missingKeySpecification);
 assertNull("expected null for missing key in CacheBuilderSpecification", spec.keyStrength);
 assertNull("expected null for missing key in CacheBuilderSpecification", spec.valueStrength);
 }

 @Test
 public void testParseCacheBuilderSpecificationWithMissingValue() {
 String missingValueSpecification = "initialCapacity=10, maximumSize=50, maximumWeight=100, concurrencyLevel=5, weakKeys, recordStats, expireAfterAccess=1h";
 CacheBuilderSpec spec = CacheBuilderSpec.parse(missingValueSpecification);
 assertNull("expected null for missing value in CacheBuilderSpecification", spec.writeExpirationTimeUnit);
 assertNull("expected null for missing value in CacheBuilderSpecification", spec.accessExpirationTimeUnit);
 assertNull("expected null for missing value in CacheBuilderSpecification", spec.refreshTimeUnit);
 }

 @Test
 public void testToCacheBuilderReturnsExpectedBuilder() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(CACHE_BUILDER_SPECIFICATION);
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

 if (spec.initialCapacity != null) {
 assertEquals("initialCapacity was not set correctly in the builder", spec.initialCapacity, cacheBuilder.initialCapacity());
 }
 if (spec.maximumSize != null) {
 assertEquals("maximumSize was not set correctly in the builder", spec.maximumSize, cacheBuilder.maximumSize());
 }
 if (spec.maximumWeight != null) {
 assertEquals("maximumWeight was not set correctly in the builder", spec.maximumWeight, cacheBuilder.maximumWeight());
 }
 if (spec.concurrencyLevel != null) {
 assertEquals("concurrencyLevel was not set correctly in the builder", spec.concurrencyLevel, cacheBuilder.concurrencyLevel());
 }
 if (spec.keyStrength != null) {
 switch (spec.keyStrength) {
 case WEAK:
 assertEquals("weakKeys was not set correctly in the builder", CacheBuilder.WeakKeys.weakKeys(), cacheBuilder.weakKeys());
 break;
 case SOFT:
 assertEquals("softValues was not set correctly in the builder", CacheBuilder.SoftValues.softValues(), cacheBuilder.softValues());
 break;
 case STRONG:
 assertEquals("strongValues was not set correctly in the builder", CacheBuilder.CacheBuilder.noEviction(), cacheBuilder);
 break;
 default:
 fail("Unexpected keyStrength value: " + spec.keyStrength);
 }
 }
 if (spec.valueStrength != null) {
 switch (spec.valueStrength) {
 case SOFT:
 assertEquals("softValues was not set correctly in the builder", CacheBuilder.SoftValues.softValues(), cacheBuilder.softValues());
 break;
 case WEAK:
 assertEquals("weakValues was not set correctly in the builder", CacheBuilder.WeakValues.weakValues(), cacheBuilder.weakValues());
 break;
 case STRONG:
 assertEquals("strongValues was not set correctly in the builder", CacheBuilder.CacheBuilder.noEviction(), cacheBuilder);
 break;
 default:
 fail("Unexpected valueStrength value: " + spec.valueStrength);
 }
 }
 if (spec.recordStats != null) {
 assertTrue("recordStats was not set correctly in the builder", cacheBuilder.recordStats());
 }
 if (spec.writeExpirationDuration != 0) {
 assertEquals("writeExpirationDuration was not set correctly in the builder", spec.writeExpirationDuration, cacheBuilder.expireAfterWrite(spec.writeExpirationDuration, spec.writeExpirationTimeUnit).expirationTimeToLive());
 }
 if (spec.accessExpirationDuration != 0) {
 assertEquals("accessExpirationDuration was not set correctly in the builder", spec.accessExpirationDuration, cacheBuilder.expireAfterAccess(spec.accessExpirationDuration, spec.accessExpirationTimeUnit).expirationTimeToLive());
 }
 if (spec.refreshDuration != 0) {
 assertEquals("refreshDuration was not set correctly in the builder", spec.refreshDuration, cacheBuilder.refreshAfterWrite(spec.refreshDuration, spec.refreshTimeUnit).refreshAfterWrite(spec.refreshDuration, spec.refreshTimeUnit).expirationTimeToLive());
 }
 }

}