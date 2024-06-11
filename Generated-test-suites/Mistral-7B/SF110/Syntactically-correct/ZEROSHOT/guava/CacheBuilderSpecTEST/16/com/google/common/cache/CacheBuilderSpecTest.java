package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.LocalCache;
import com.google.common.cache.LocalCache.Strength;
import java.util.concurrent.*;
import org.junit.Test;

@Beta
public class CacheBuilderSpecTest {

 private static final String VALID_SPEC = "maximumSize=10, writeExpirationDuration=1h, accessExpirationDuration=30m, concurrencyLevel=5";
 private static final String INVALID_SPEC = "initialCapacity=5, maximumWeight=10, keyStrength=SOFT, valueStrength=STRONG, recordStats=true, expireAfterWrite=3d, expireAfterAccess=5min, refreshAfterWrite=1h, refreshInterval=2d";

 @Test
 public void testParseValidSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(VALID_SPEC);
 assertEquals(VALID_SPEC, spec.toParsableString());

 assertEquals(10, spec.maximumSize);
 assertEquals(1, spec.writeExpirationTimeUnit.toHours(spec.writeExpirationDuration));
 assertEquals(30, spec.accessExpirationTimeUnit.toMinutes(spec.accessExpirationDuration));
 assertEquals(5, spec.concurrencyLevel);
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumWeight);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(INVALID_SPEC);
 fail("Parsing invalid spec should have thrown an exception");
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(VALID_SPEC);
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

 assertEquals("maximumSize=10", cacheBuilder.toString());
 assertEquals(10, cacheBuilder.maximumSize());
 assertEquals(1, cacheBuilder.expireAfterWrite());
 assertEquals(TimeUnit.HOURS, cacheBuilder.expireAfterWriteTimeUnit());
 assertEquals(30, cacheBuilder.expireAfterAccess());
 assertEquals(TimeUnit.MINUTES, cacheBuilder.expireAfterAccessTimeUnit());
 assertEquals(5, cacheBuilder.concurrencyLevel());
 assertFalse(cacheBuilder.recordStats());
 }

 @Test
 public void testParseEmptySpec() {
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
 public void testParseKeyValuePair() {
 List<String> keyValuePair = ImmutableList.of("maximumSize=10");
 List<String> keyAndValue = CacheBuilderSpec.KEY_VALUE_SPLITTER.split(keyValuePair.get(0));
 assertEquals(1, keyAndValue.size());
 assertEquals("maximumSize", keyAndValue.get(0));
 assertNull(keyAndValue.get(1));
 }

 @Test
 public void testParseInvalidKeyValuePair() {
 List<String> keyValuePair = ImmutableList.of("maximumSize=10, initialCapacity=5");
 List<String> keyAndValue = CacheBuilderSpec.KEY_VALUE_SPLITTER.split(keyValuePair.get(0));
 assertEquals(2, keyAndValue.size());
 assertEquals("maximumSize", keyAndValue.get(0));
 assertEquals("initialCapacity", keyAndValue.get(1));
 }

 @Test
 public void testParseInvalidKey() {
 List<String> keyValuePair = ImmutableList.of("invalidKey=10");
 try {
 CacheBuilderSpec.VALUE_PARSERS.get("invalidKey").parse(new CacheBuilderSpec(""), "invalidKey", null);
 fail("Parsing invalid key should have thrown an exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseIntegerValue() {
 List<String> keyValuePair = ImmutableList.of("maximumSize=10");
 CacheBuilderSpec.InitialCapacityParser parser = (CacheBuilderSpec p0, int value) -> p0.initialCapacity = value;
 parser.parseInteger(new CacheBuilderSpec(""), 10);
 assertEquals(10, new CacheBuilderSpec("").initialCapacity);
 }

 @Test
 public void testParseLongValue() {
 List<String> keyValuePair = ImmutableList.of("maximumSize=1000");
 CacheBuilderSpec.MaximumSizeParser parser = (CacheBuilderSpec p0, long value) -> {
 p0.maximumSize = value;
 p0.maximumWeight = null;
 };
 parser.parseLong(new CacheBuilderSpec(""), 1000L);
 assertEquals(1000, new CacheBuilderSpec("").maximumSize);
 assertNull(new CacheBuilderSpec("").maximumWeight);
 }

 @Test
 public void testParseConcurrencyLevel() {
 List<String> keyValuePair = ImmutableList.of("concurrencyLevel=5");
 CacheBuilderSpec.ConcurrencyLevelParser parser = (CacheBuilderSpec p0, int value) -> p0.concurrencyLevel = value;
 parser.parseInteger(new CacheBuilderSpec(""), 5);
 assertEquals(5, new CacheBuilderSpec("").concurrencyLevel);
 }

 @Test
 public void testParseKeyStrength() {
 List<String> keyValuePair = ImmutableList.of("weakKeys");
 CacheBuilderSpec.KeyStrengthParser parser = new CacheBuilderSpec.KeyStrengthParser(LocalCache.Strength.WEAK);
 parser.parse(new CacheBuilderSpec(""), "weakKeys", null);
 assertEquals(LocalCache.Strength.WEAK, new CacheBuilderSpec("").keyStrength);
 }

 @Test
 public void testParseValueStrength() {
 List<String> keyValuePair = ImmutableList.of("softValues");
 CacheBuilderSpec.ValueStrengthParser parser = new CacheBuilderSpec.ValueStrengthParser(LocalCache.Strength.SOFT);
 parser.parse(new CacheBuilderSpec(""), "softValues", null);
 assertEquals(LocalCache.Strength.SOFT, new CacheBuilderSpec("").valueStrength);
 }

 @Test
 public void testParseRecordStats() {
 List<String> keyValuePair = ImmutableList.of("recordStats=true");
 CacheBuilderSpec.RecordStatsParser parser = new CacheBuilderSpec.RecordStatsParser();
 parser.parse(new CacheBuilderSpec(""), "recordStats", null);
 assertTrue(new CacheBuilderSpec("").recordStats);
 }

 @Test
 public void testParseAccessExpiration() {
 List<String> keyValuePair = ImmutableList.of("expireAfterAccess=1h");
 CacheBuilderSpec.AccessDurationParser parser = new CacheBuilderSpec.AccessDurationParser();
 parser.parse(new CacheBuilderSpec(""), "expireAfterAccess", null);
 assertEquals(1, parser.accessExpirationTimeUnit.toHours(parser.accessExpirationDuration));
 assertEquals(1, new CacheBuilderSpec("").accessExpirationTimeUnit.toHours(new CacheBuilderSpec("").accessExpirationDuration));
 }

 @Test
 public void testParseWriteExpiration() {
 List<String> keyValuePair = ImmutableList.of("expireAfterWrite=5m");
 CacheBuilderSpec.WriteDurationParser parser = new CacheBuilderSpec.WriteDurationParser();
 parser.parse(new CacheBuilderSpec(""), "expireAfterWrite", null);
 assertEquals(5, parser.writeExpirationTimeUnit.toMinutes(parser.writeExpirationDuration));
 assertEquals(5, new CacheBuilderSpec("").writeExpirationTimeUnit.toMinutes(new CacheBuilderSpec("").writeExpirationDuration));
 }

 @Test
 public void testParseRefreshDuration() {
 List<String> keyValuePair = ImmutableList.of("refreshAfterWrite=1h");
 CacheBuilderSpec.RefreshDurationParser parser = new CacheBuilderSpec.RefreshDurationParser();
 parser.parse(new CacheBuilderSpec(""), "refreshAfterWrite", null);
 assertEquals(1, parser.refreshTimeUnit.toHours(parser.refreshDuration));
 assertEquals(1, new CacheBuilderSpec("").refreshTimeUnit.toHours(new CacheBuilderSpec("").refreshDuration));
 }

}