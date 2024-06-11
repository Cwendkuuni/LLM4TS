package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec cacheBuilderSpec;

 @Before
 public void setup() {
 cacheBuilderSpec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, concurrencyLevel=2, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1m");
 }

 @Test
 public void testParse() {
 // Test valid specification
 assertEquals(5, cacheBuilderSpec.initialCapacity);
 assertEquals(10, cacheBuilderSpec.maximumSize);
 assertEquals(2, cacheBuilderSpec.concurrencyLevel);
 assertTrue(cacheBuilderSpec.keyStrength.equals(LocalCache.Strength.WEAK));
 assertTrue(cacheBuilderSpec.valueStrength.equals(LocalCache.Strength.SOFT));
 assertTrue(cacheBuilderSpec.recordStats);
 assertEquals(1, TimeUnit.HOURS.toSeconds(cacheBuilderSpec.accessExpirationDuration));
 assertEquals(1, TimeUnit.DAYS.toSeconds(cacheBuilderSpec.writeExpirationDuration));
 assertEquals(1, TimeUnit.MINUTES.toSeconds(cacheBuilderSpec.refreshDuration));

 // Test empty specification
 CacheBuilderSpec emptySpec = new CacheBuilderSpec("");
 assertNull(emptySpec.initialCapacity);
 assertNull(emptySpec.maximumSize);
 assertNull(emptySpec.maximumWeight);
 assertNull(emptySpec.concurrencyLevel);
 assertNull(emptySpec.keyStrength);
 assertNull(emptySpec.valueStrength);
 assertFalse(emptySpec.recordStats);
 assertNull(emptySpec.writeExpirationTimeUnit);
 assertNull(emptySpec.accessExpirationTimeUnit);
 assertNull(emptySpec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSpecification() {
 // Test missing equals sign in key-value pair
 new CacheBuilderSpec("initialCapacity=5, maximumSize10");

 // Test unknown key
 new CacheBuilderSpec("unknownKey=value");

 // Test invalid value (non-integer)
 new CacheBuilderSpec("initialCapacity=five");

 // Test missing value
 new CacheBuilderSpec("initialCapacity=");
 }

 @Test
 public void testToParsableString() {
 assertEquals("initialCapacity=5, maximumSize=10, concurrencyLevel=2, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1m", cacheBuilderSpec.toParsableString());
 }

 // More tests can be added here for different scenarios
}