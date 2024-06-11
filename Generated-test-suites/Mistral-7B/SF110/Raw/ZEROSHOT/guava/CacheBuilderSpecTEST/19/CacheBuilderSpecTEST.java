package com.google.common.cache;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.AccessDurationParser;
import com.google.common.cache.CacheBuilderSpec.ConcurrencyLevelParser;
import com.google.common.cache.CacheBuilderSpec.InitialCapacityParser;
import com.google.common.cache.CacheBuilderSpec.KeyStrengthParser;
import com.google.common.cache.CacheBuilderSpec.MaximumSizeParser;
import com.google.common.cache.CacheBuilderSpec.MaximumWeightParser;
import com.google.common.cache.CacheBuilderSpec.RefreshDurationParser;
import com.google.common.cache.CacheBuilderSpec.RecordStatsParser;
import com.google.common.cache.CacheBuilderSpec.ValueStrengthParser;
import com.google.common.cache.CacheBuilderSpec.WriteDurationParser;

public class CacheBuilderSpecTest {

 @Test
 public void testParse() {
 // Test parsing a valid cache builder specification
 final String cacheBuilderSpec = "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m";
 final CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);
 // Check that all fields have been set correctly
 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(20, spec.maximumWeight);
 assertEquals(3, spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertTrue(spec.recordStats);
 assertEquals(5, spec.writeExpirationDuration);
 assertEquals(TimeUnit.MINUTES, spec.writeExpirationTimeUnit);
 assertEquals(5, spec.accessExpirationDuration);
 assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
 assertEquals(15, spec.refreshDuration);
 assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);

 // Test parsing a cache builder specification with only required fields
 final String cacheBuilderSpecMinimal = "initialCapacity=5, maximumSize=10, concurrencyLevel=3";
 final CacheBuilderSpec specMinimal = CacheBuilderSpec.parse(cacheBuilderSpecMinimal);
 // Check that all fields have been set correctly, except for optional ones
 assertEquals(5, specMinimal.initialCapacity);
 assertEquals(10, specMinimal.maximumSize);
 assertEquals(3, specMinimal.concurrencyLevel);
 assertNull(specMinimal.keyStrength);
 assertNull(specMinimal.valueStrength);
 assertNull(specMinimal.recordStats);
 assertNull(specMinimal.writeExpirationTimeUnit);
 assertNull(specMinimal.accessExpirationTimeUnit);
 assertNull(specMinimal.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidCacheBuilderSpec() {
 // Test parsing an invalid cache builder specification
 final String cacheBuilderSpecInvalid = "initialCapacity=5, maximumSize=10, concurrencyLevel=3, expireAfterAccess=5d, expireAfterWrite=1h, refreshAfterWrite=15m, invalidOption=true";
 final CacheBuilderSpec specInvalid = CacheBuilderSpec.parse(cacheBuilderSpecInvalid);
 // The test should fail with an IllegalArgumentException
 }

 @Test
 public void testToCacheBuilder() {
 // Test creating a CacheBuilder instance from a valid cache builder specification
 final String cacheBuilderSpec = "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m";
 final CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);
 final CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 // Check that the CacheBuilder instance has been created with the correct options
 assertEquals(5, cacheBuilder.initialCapacity());
 assertEquals(10, cacheBuilder.maximumSize());
 assertEquals(20, cacheBuilder.maximumWeight());
 assertEquals(3, cacheBuilder.concurrencyLevel());
 assertTrue(cacheBuilder.weakKeys());
 assertTrue(cacheBuilder.softValues());
 assertTrue(cacheBuilder.recordStats());
 assertEquals(5, cacheBuilder.expireAfterAccessInMillis());
 assertEquals(1, cacheBuilder.expireAfterWrite(1, TimeUnit.HOURS).getExpirationTime(null));
 assertEquals(15, cacheBuilder.refreshAfterWrite(15, TimeUnit.MINUTES).getExpirationTime(null));
 }

 @Test
 public void testToParsableString() {
 // Test converting a CacheBuilderSpec instance back to a string
 final String cacheBuilderSpec = "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m";
 final CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);
 final String parsedSpec = spec.toParsableString();
 assertEquals(cacheBuilderSpec, parsedSpec);
 }

}