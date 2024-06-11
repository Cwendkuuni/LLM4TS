package com.google.common.cache;

import com.google.common.annotations.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import java.util.concurrent.*;
import static com.google.common.base.Preconditions.*;

@Beta
public class CacheBuilderSpecTest {

 private static final String CACHE_BUILDER_SPECIFICATION = "initialCapacity=5" +
         " maximumSize=10" +
         " maximumWeight=20L" +
         " concurrencyLevel=3" +
         " weakKeys" +
         " softValues" +
         " recordStats" +
         " expireAfterAccess=1h" +
         " expireAfterWrite=1d" +
         " refreshAfterWrite=30s";

 @VisibleForTesting
 static CacheBuilderSpec createCacheBuilderSpec(String specification) {
 return CacheBuilderSpec.parse(specification);
 }

 @Test
 public void testParse() {
 CacheBuilderSpec cacheBuilderSpec = createCacheBuilderSpec(CACHE_BUILDER_SPECIFICATION);

 assertEquals("initialCapacity was not set correctly", 5, cacheBuilderSpec.initialCapacity);
 assertEquals("maximumSize was not set correctly", 10, cacheBuilderSpec.maximumSize);
 assertEquals("maximumWeight was not set correctly", 20L, cacheBuilderSpec.maximumWeight);
 assertEquals("concurrencyLevel was not set correctly", 3, cacheBuilderSpec.concurrencyLevel);
 assertNull("keyStrength should be null", cacheBuilderSpec.keyStrength);
 assertNull("valueStrength should be null", cacheBuilderSpec.valueStrength);
 assertTrue("recordStats should be true", cacheBuilderSpec.recordStats);

 long writeExpirationDuration = cacheBuilderSpec.writeExpirationDuration;
 TimeUnit writeExpirationTimeUnit = cacheBuilderSpec.writeExpirationTimeUnit;
 assertEquals("writeExpirationDuration was not set correctly", 1 * 24 * 60 * 60 * 1000L, writeExpirationDuration);
 assertEquals("writeExpirationTimeUnit was not set correctly", TimeUnit.DAYS, writeExpirationTimeUnit);

 long accessExpirationDuration = cacheBuilderSpec.accessExpirationDuration;
 TimeUnit accessExpirationTimeUnit = cacheBuilderSpec.accessExpirationTimeUnit;
 assertEquals("accessExpirationDuration was not set correctly", 1 * 60 * 60 * 1000L, accessExpirationDuration);
 assertEquals("accessExpirationTimeUnit was not set correctly", TimeUnit.HOURS, accessExpirationTimeUnit);

 long refreshDuration = cacheBuilderSpec.refreshDuration;
 TimeUnit refreshTimeUnit = cacheBuilderSpec.refreshTimeUnit;
 assertEquals("refreshDuration was not set correctly", 30 * 1000L, refreshDuration);
 assertEquals("refreshTimeUnit was not set correctly", TimeUnit.SECONDS, refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCacheBuilderSpecificationFormat() {
 String invalidCacheBuilderSpecification = "key=value";
 CacheBuilderSpec cacheBuilderSpec = createCacheBuilderSpec(invalidCacheBuilderSpecification);
 fail("IllegalArgumentException was expected");
 }

 @Test(expected = AssertionError.class)
 public void testUnknownKeyStrength() {
 String unknownKeyStrengthCacheBuilderSpecification = "initialCapacity=5 maximumSize=10 concurrencyLevel=3 keyStrength=unknown";
 CacheBuilderSpec cacheBuilderSpec = createCacheBuilderSpec(unknownKeyStrengthCacheBuilderSpecification);
 fail("AssertionError was expected");
 }

 @Test(expected = AssertionError.class)
 public void testUnknownValueStrength() {
 String unknownValueStrengthCacheBuilderSpecification = "initialCapacity=5 maximumSize=10 concurrencyLevel=3 valueStrength=unknown";
 CacheBuilderSpec cacheBuilderSpec = createCacheBuilderSpec(unknownValueStrengthCacheBuilderSpecification);
 fail("AssertionError was expected");
 }

}