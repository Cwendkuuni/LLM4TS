package com.example.cachebuilder;

import static org.junit.Assert.*;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.AccessDurationParser;
import com.google.common.cache.CacheBuilderSpec.ConcurrencyLevelParser;
import com.google.common.cache.CacheBuilderSpec.DurationParser;
import com.google.common.cache.CacheBuilderSpec.InitialCapacityParser;
import com.google.common.cache.CacheBuilderSpec.KeyStrengthParser;
import com.google.common.cache.CacheBuilderSpec.MaximumSizeParser;
import com.google.common.cache.CacheBuilderSpec.MaximumWeightParser;
import com.google.common.cache.CacheBuilderSpec.RecordStatsParser;
import com.google.common.cache.CacheBuilderSpec.RefreshDurationParser;
import com.google.common.cache.CacheBuilderSpec.ValueStrengthParser;
import com.google.common.cache.CacheBuilderSpec.WriteDurationParser;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec testSpec;

 @Before
 public void setUp() {
 testSpec = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, keyStrength=weak, valueStrength=soft, recordStats=true, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3s");
 }

 @Test
 public void testParse() {
 CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=null, concurrencyLevel=3, keyStrength=weak, valueStrength=weak, recordStats=false, expireAfterAccess=1m, expireAfterWrite=1h, refreshAfterWrite=1s");
 assertEquals("initialCapacity=5, maximumSize=10, maximumWeight=null, concurrencyLevel=3, keyStrength=weak, valueStrength=weak, recordStats=false, expireAfterAccess=1m, expireAfterWrite=1h, refreshAfterWrite=1s", parsedSpec.toParsableString());
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = testSpec.toCacheBuilder();
 assertNotNull(cacheBuilder);
 assertTrue(cacheBuilder.initialCapacity() == 10);
 assertTrue(cacheBuilder.maximumSize(20));
 assertTrue(cacheBuilder.maximumWeight(30));
 assertTrue(cacheBuilder.concurrencyLevel(5));
 assertTrue(cacheBuilder.weakKeys());
 assertTrue(cacheBuilder.softValues());
 assertTrue(cacheBuilder.recordStats());
 assertTrue(cacheBuilder.expireAfterWrite(2L, TimeUnit.DAYS));
 assertTrue(cacheBuilder.expireAfterAccess(1L, TimeUnit.HOURS));
 assertTrue(cacheBuilder.refreshAfterWrite(3L, TimeUnit.SECONDS));
 }

 @Test(expected = AssertionError.class)
 public void testUnknownKeyParsing() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("unknownKey=10");
 }

 @Test
 public void testMaxCapacityAndWeightNull() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("concurrencyLevel=5, recordStats=true");
 assertNull(testSpec.maximumSize);
 assertNull(testSpec.maximumWeight);
 }

 @Test
 public void testMaxCapacityAndWeightBothSet() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("maximumSize=10, maximumWeight=20, concurrencyLevel=5, recordStats=true");
 assertEquals(10, testSpec.maximumSize.intValue());
 assertEquals(20, testSpec.maximumWeight.intValue());
 }

 @Test
 public void testInitialCapacityNegative() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("initialCapacity=-5");
 assertNull(testSpec.initialCapacity);
 }

 @Test
 public void testInitialCapacityZero() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("initialCapacity=0");
 assertNull(testSpec.initialCapacity);
 }

 @Test
 public void testInitialCapacityPositive() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("initialCapacity=5");
 assertEquals(5, testSpec.initialCapacity.intValue());
 }

 @Test
 public void testConcurrencyLevelZero() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("concurrencyLevel=0");
 assertNull(testSpec.concurrencyLevel);
 }

 @Test
 public void testConcurrencyLevelOne() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("concurrencyLevel=1");
 assertEquals(1, testSpec.concurrencyLevel.intValue());
 }

 @Test
 public void testConcurrencyLevelGreaterThanZero() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("concurrencyLevel=5");
 assertEquals(5, testSpec.concurrencyLevel.intValue());
 }

 @Test
 public void testKeyStrength() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("keyStrength=weak");
 assertEquals(LocalCache.Strength.WEAK, testSpec.keyStrength);
 }

 @Test
 public void testValueStrength() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("valueStrength=soft");
 assertEquals(LocalCache.Strength.SOFT, testSpec.valueStrength);
 }

 @Test
 public void testValueStrengthWeak() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("valueStrength=weak");
 assertEquals(LocalCache.Strength.WEAK, testSpec.valueStrength);
 }

 @Test
 public void testValueStrengthStrong() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("valueStrength=strong");
 assertEquals(LocalCache.Strength.STRONG, testSpec.valueStrength);
 }

 @Test(expected = AssertionError.class)
 public void testUnknownValueStrength() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("valueStrength=unknown");
 }

 @Test
 public void testRecordStatsFalse() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("recordStats=false");
 assertFalse(testSpec.recordStats);
 }

 @Test
 public void testRecordStatsTrue() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("recordStats=true");
 assertTrue(testSpec.recordStats);
 }

 @Test(expected = AssertionError.class)
 public void testUnknownRecordStats() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("recordStats=unknown");
 }

 @Test
 public void testWriteExpirationDurationZero() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("expireAfterWrite=0s");
 assertNull(testSpec.writeExpirationDuration);
 assertNull(testSpec.writeExpirationTimeUnit);
 }

 @Test
 public void testWriteExpirationDurationPositive() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("expireAfterWrite=5s");
 assertEquals(5L, testSpec.writeExpirationDuration.longValue());
 assertEquals(TimeUnit.SECONDS, testSpec.writeExpirationTimeUnit);
 }

 @Test
 public void testWriteExpirationDurationMillis() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("expireAfterWrite=5000ms");
 assertEquals(5000L, testSpec.writeExpirationDuration.longValue());
 assertEquals(TimeUnit.MILLISECONDS, testSpec.writeExpirationTimeUnit);
 }

 @Test(expected = AssertionError.class)
 public void testWriteExpirationDurationInvalidFormat() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("expireAfterWrite=5s,invalid");
 }

 @Test
 public void testAccessExpirationDurationZero() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("expireAfterAccess=0s");
 assertNull(testSpec.accessExpirationDuration);
 assertNull(testSpec.accessExpirationTimeUnit);
 }

 @Test
 public void testAccessExpirationDurationPositive() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("expireAfterAccess=5s");
 assertEquals(5L, testSpec.accessExpirationDuration.longValue());
 assertEquals(TimeUnit.SECONDS, testSpec.accessExpirationTimeUnit);
 }

 @Test
 public void testAccessExpirationDurationMillis() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("expireAfterAccess=5000ms");
 assertEquals(5000L, testSpec.accessExpirationDuration.longValue());
 assertEquals(TimeUnit.MILLISECONDS, testSpec.accessExpirationTimeUnit);
 }

 @Test(expected = AssertionError.class)
 public void testAccessExpirationDurationInvalidFormat() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("expireAfterAccess=5s,invalid");
 }

 @Test
 public void testRefreshDurationZero() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("refreshAfterWrite=0s");
 assertNull(testSpec.refreshDuration);
 assertNull(testSpec.refreshTimeUnit);
 }

 @Test
 public void testRefreshDurationPositive() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("refreshAfterWrite=5s");
 assertEquals(5L, testSpec.refreshDuration.longValue());
 assertEquals(TimeUnit.SECONDS, testSpec.refreshTimeUnit);
 }

 @Test
 public void testRefreshDurationMillis() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("refreshAfterWrite=5000ms");
 assertEquals(5000L, testSpec.refreshDuration.longValue());
 assertEquals(TimeUnit.MILLISECONDS, testSpec.refreshTimeUnit);
 }

 @Test(expected = AssertionError.class)
 public void testRefreshDurationInvalidFormat() {
 CacheBuilderSpec testSpec = new CacheBuilderSpec("refreshAfterWrite=5s,invalid");
 }

 @Test
 public void testValueParserParsesInteger() {
 ImmutableMap<String, ValueParser> valueParsers = ImmutableMap.of(
 "initialCapacity", new InitialCapacityParser(),
 "maximumSize", new MaximumSizeParser(),
 "maximumWeight", new MaximumWeightParser(),
 "concurrencyLevel", new ConcurrencyLevelParser(),
 "recordStats", new RecordStatsParser());
 InitialCapacityParser initialCapacityParser = (InitialCapacityParser)valueParsers.get("initialCapacity");
 initialCapacityParser.parse(testSpec, 10);
 assertEquals(10, testSpec.initialCapacity.intValue());
 }

 @Test
 public void testValueParserParsesLong() {
 ImmutableMap<String, ValueParser> valueParsers = ImmutableMap.of(
 "initialCapacity", new InitialCapacityParser(),
 "maximumSize", new MaximumSizeParser(),
 "maximumWeight", new MaximumWeightParser(),
 "concurrencyLevel", new ConcurrencyLevelParser(),
 "recordStats", new RecordStatsParser());
 MaximumSizeParser maximumSizeParser = (MaximumSizeParser)valueParsers.get("maximumSize");
 maximumSizeParser.parse(testSpec, 20L);
 assertEquals(20, testSpec.maximumSize.longValue());
 }

 @Test
 public void testValueParserThrowsExceptionForUnknownKey() {
 ImmutableMap<String, ValueParser> valueParsers = ImmutableMap.of(
 "initialCapacity", new InitialCapacityParser(),
 "maximumSize", new MaximumSizeParser(),
 "maximumWeight", new MaximumWeightParser(),
 "concurrencyLevel", new ConcurrencyLevelParser(),
 "recordStats", new RecordStatsParser());
 ValueParser valueParser = valueParsers.get("unknownKey");
 try {
 valueParser.parse(testSpec, "unknownValue");
 fail("Should have thrown an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }
}