package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import com.google.common.base.*;
import com.google.common.cache.*;
import com.google.common.collect.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.*;

@Beta
@SuppressWarnings("unchecked")
public class CacheBuilderSpecTest {

 @BeforeClass
 static void setup() {
 KEYS_SPLITTER = Splitter.on(',').trimResults();
 KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
 VALUE_PARSERS = ImmutableMap.<String, ValueParser>builder()
 .put("initialCapacity", new InitialCapacityParser())
 .put("maximumSize", new MaximumSizeParser())
 .put("maximumWeight", new MaximumWeightParser())
 .put("concurrencyLevel", new ConcurrencyLevelParser())
 .put("weakKeys", new KeyStrengthParser(LocalCache.Strength.WEAK))
 .put("softValues", new ValueStrengthParser(LocalCache.Strength.SOFT))
 .put("weakValues", new ValueStrengthParser(LocalCache.Strength.WEAK))
 .put("recordStats", new RecordStatsParser())
 .put("expireAfterAccess", new AccessDurationParser())
 .put("expireAfterWrite", new WriteDurationParser())
 .put("refreshAfterWrite", new RefreshDurationParser())
 .build();
 }

 @AfterClass
 static void teardown() {
 KEYS_SPLITTER = null;
 KEY_VALUE_SPLITTER = null;
 VALUE_PARSERS = null;
 }

 @Test
 public void testParse_EmptySpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("");
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParse_InitialCapacity() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5");
 assertEquals(5, spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParse_MaximumSize() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10");
 assertNull(spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParse_MaximumWeight() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=20");
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertEquals(20, spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParse_ConcurrencyLevel() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=3");
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertEquals(3, spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParse_KeyStrength() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParse_ValueStrength() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 assertNull(spec.recordStats);
 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParse_RecordStats() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats=true");
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
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
 public void testParse_AccessDuration() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1h");
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 assertEquals(1L, spec.accessExpirationDuration.longValue());
 assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParse_AccessDuration_InvalidUnit() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1dmin");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParse_WriteDuration() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=2s");
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertEquals(2L, spec.writeExpirationDuration.longValue());
 assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParse_WriteDuration_InvalidUnit() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=3hmin");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParse_RefreshDuration() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=1m");
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertEquals(1L, spec.refreshDuration.longValue());
 assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
 }

 @Test
 public void testParse_RefreshDuration_InvalidUnit() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=2dmin");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParse_MultipleKeys() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, concurrencyLevel=3, expireAfterAccess=1h, expireAfterWrite=2d, recordStats=true, refreshAfterWrite=30s");
 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(3, spec.concurrencyLevel);
 assertEquals(LocalCache.Strength.DEFAULT, spec.keyStrength);
 assertEquals(LocalCache.Strength.DEFAULT, spec.valueStrength);
 assertTrue(spec.recordStats);
 assertEquals(1L, spec.accessExpirationDuration.longValue());
 assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
 assertEquals(2L * 24L * 60L * 60L, spec.writeExpirationDuration.longValue());
 assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testToCacheBuilder_NoSpecifiedValues() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("");
 CacheBuilder<String, String> cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.isBuildingKeyDistributionSensitively());
 assertFalse(cacheBuilder.isRecordStats());
 assertFalse(cacheBuilder.isExpireAfterAccessPresent());
 assertFalse(cacheBuilder.isExpireAfterWritePresent());
 assertFalse(cacheBuilder.isRefreshAfterWritePresent());
 }

 @Test
 public void testToCacheBuilder_InitialCapacity() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5");
 CacheBuilder<String, String> cacheBuilder = spec.toCacheBuilder();
 assertEquals(5, cacheBuilder.initialCapacity());
 }

 @Test
 public void testToCacheBuilder_MaximumSize() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10");
 CacheBuilder<String, String> cacheBuilder = spec.toCacheBuilder();
 assertEquals(10, cacheBuilder.maximumSize());
 }

 @Test
 public void testToCacheBuilder_MaximumWeight() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=20");
 CacheBuilder<String, String> cacheBuilder = spec.toCacheBuilder();
 assertEquals(20, cacheBuilder.maximumWeight());
 }

 @Test
 public void testToCacheBuilder_ConcurrencyLevel() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=3");
 CacheBuilder<String, String> cacheBuilder = spec.toCacheBuilder();
 assertEquals(3, cacheBuilder.concurrencyLevel());
 }

 @Test
 public void testToCacheBuilder_KeyStrength() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
 CacheBuilder<String, String> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.WeakAccessPolicy.SOFT_EVICTION, cacheBuilder.keyEvictionPolicy());
 }

 @Test
 public void testToCacheBuilder_ValueStrength() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
 CacheBuilder<String, String> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.ValueEvictionPolicy.SOFT_EVICTION, cacheBuilder.valueEvictionPolicy());
 }

 @Test
 public void testToCacheBuilder_RecordStats() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats=true");
 CacheBuilder<String, String> cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.isRecordStats());
 }

 @Test
 public void testToCacheBuilder_AccessExpiration() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1h");
 CacheBuilder<String, String> cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.isExpireAfterAccessPresent());
 assertEquals(1L, cacheBuilder.expireAfterAccess(TimeUnit.HOURS));
 }

 @Test
 public void testToCacheBuilder_WriteExpiration() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=2d");
 CacheBuilder<String, String> cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.isExpireAfterWritePresent());
 assertEquals(2L * 24L * 60L * 60L, cacheBuilder.expireAfterWrite(TimeUnit.DAYS));
 }

 @Test
 public void testToCacheBuilder_RefreshExpiration() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=30s");
 CacheBuilder<String, String> cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.isRefreshAfterWritePresent());
 assertEquals(30L, cacheBuilder.refreshAfterWrite(TimeUnit.SECONDS));
 }

}