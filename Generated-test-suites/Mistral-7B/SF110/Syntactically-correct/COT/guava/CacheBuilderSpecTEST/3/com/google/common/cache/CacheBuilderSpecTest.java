package com.example.cachebuilder;

import static org.junit.Assert.*;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.AccessDurationParser;
import com.google.common.cache.CacheBuilderSpec.ConcurrencyLevelParser;
import com.google.common.cache.CacheBuilderSpec.InitialCapacityParser;
import com.google.common.cache.CacheBuilderSpec.KeyStrengthParser;
import com.google.common.cache.CacheBuilderSpec.MaximumSizeParser;
import com.google.common.cache.CacheBuilderSpec.MaximumWeightParser;
import com.google.common.cache.CacheBuilderSpec.RecordStatsParser;
import com.google.common.cache.CacheBuilderSpec.RefreshDurationParser;
import com.google.common.cache.CacheBuilderSpec.ValueStrengthParser;
import com.google.common.cache.CacheBuilderSpec.WriteDurationParser;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

 @org.junit.Test
 public void testParse() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=50,concurrencyLevel=5," +
 "weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=1m,refreshAfterWrite=5s");

 assertEquals("initialCapacity=10,maximumSize=50,concurrencyLevel=5," +
 "weakKeys(),softValues(),recordStats(),expireAfterWrite=1h,expireAfterAccess=1m,refreshAfterWrite=5s", spec.toParsableString());

 assertEquals(10, spec.initialCapacity);
 assertEquals(50, spec.maximumSize);
 assertEquals(5, spec.concurrencyLevel);
 assertTrue(spec.keyStrength == CacheBuilder.WeakKeys);
 assertTrue(spec.valueStrength == CacheBuilder.SoftValues);
 assertTrue(spec.recordStats);
 assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
 assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
 assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
 assertEquals(1, spec.writeExpirationDuration);
 assertEquals(1, spec.accessExpirationDuration);
 assertEquals(5, spec.refreshDuration);

 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expectedCacheBuilder = CacheBuilder.newBuilder()
 .initialCapacity(10)
 .maximumSize(50)
 .concurrencyLevel(5)
 .weakKeys()
 .softValues()
 .recordStats()
 .expireAfterWrite(1, TimeUnit.HOURS)
 .expireAfterAccess(1, TimeUnit.MINUTES)
 .refreshAfterWrite(5, TimeUnit.SECONDS);

 assertEquals(expectedCacheBuilder, cacheBuilder);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testParse_InvalidFormat() {
 CacheBuilderSpec.parse("expireAfterAccess=1d,writeExpirationDuration=1m");
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testParse_UnknownKey() {
 CacheBuilderSpec.parse("unknownKey=10");
 }

 @org.junit.Test(expected = AssertionError.class)
 public void testParse_UnknownStrength() {
 CacheBuilderSpec.parse("weakValues=ASDASD");
 }

 @org.junit.Test
 public void testDisableCaching() {
 CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();

 assertEquals("maximumSize=0", spec.toParsableString());

 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 CacheBuilder<Object, Object> expectedCacheBuilder = CacheBuilder.newBuilder().maximumSize(0);

 assertEquals(expectedCacheBuilder, cacheBuilder);
 }
}