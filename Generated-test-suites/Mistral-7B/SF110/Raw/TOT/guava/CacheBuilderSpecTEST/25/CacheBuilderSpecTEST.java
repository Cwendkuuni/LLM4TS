package com.google.common.cache;

import com.google.common.annotations.*;
import com.google.common.cache.*;
import com.google.common.collect.*;
import org.junit.*;
import static org.junit.Assert.*;

@Beta
public class CacheBuilderSpecTest {

 @BeforeClass
 public static void setup() {
 KEYS_SPLITTER = Splitter.on(',').trimResults();
 KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
 VALUE_PARSERS = ImmutableMap.builder()
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
 .put("refreshInterval", new RefreshDurationParser())
 .build();
 }

 @Test
 public void testParse_emptySpec() {
 final String spec = "";
 final CacheBuilderSpec expected = new CacheBuilderSpec("");
 final CacheBuilderSpec actual = CacheBuilderSpec.parse(spec);
 assertEquals(expected, actual);
 }

 @Test
 public void testParse_simpleSpec() {
 final String spec = "initialCapacity=5, maximumSize=10, concurrencyLevel=3";
 final CacheBuilderSpec expected = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, concurrencyLevel=3");
 final CacheBuilderSpec actual = CacheBuilderSpec.parse(spec);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidSpec() {
 final String spec = "invalidKey=value";
 CacheBuilderSpec.parse(spec);
 fail("An IllegalArgumentException was expected");
 }

 @Test
 public void testParse_mixedSpec() {
 final String spec = "initialCapacity=5, concurrencyLevel=3, expireAfterAccess=1h, recordStats=true";
 final CacheBuilderSpec expected = new CacheBuilderSpec("initialCapacity=5, concurrencyLevel=3, expireAfterAccess=1h, recordStats=true");
 final CacheBuilderSpec actual = CacheBuilderSpec.parse(spec);
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_noSettings() {
 final CacheBuilderSpec spec = new CacheBuilderSpec("");
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder();
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_initialCapacity() {
 final CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5");
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().initialCapacity(5);
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_maximumSize() {
 final CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=10");
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().maximumSize(10);
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_maximumWeight() {
 final CacheBuilderSpec spec = new CacheBuilderSpec("maximumWeight=5");
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().maximumWeight(5);
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_concurrencyLevel() {
 final CacheBuilderSpec spec = new CacheBuilderSpec("concurrencyLevel=3");
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().concurrencyLevel(3);
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_weakKeys() {
 final CacheBuilderSpec spec = new CacheBuilderSpec("weakKeys");
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().weakKeys();
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_softValues() {
 final CacheBuilderSpec spec = new CacheBuilderSpec("softValues");
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().softValues();
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_weakValues() {
 final CacheBuilderSpec spec = new CacheBuilderSpec("weakValues");
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().weakValues();
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_recordStats() {
 final CacheBuilderSpec spec = new CacheBuilderSpec("recordStats=true");
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().recordStats();
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_expireAfterAccess() {
 final long duration = 60;
 final TimeUnit timeUnit = TimeUnit.SECONDS;
 final CacheBuilderSpec spec = new CacheBuilderSpec("expireAfterAccess=" + duration + timeUnit.toString());
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().expireAfterAccess(duration, timeUnit);
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_expireAfterWrite() {
 final long duration = 60;
 final TimeUnit timeUnit = TimeUnit.SECONDS;
 final CacheBuilderSpec spec = new CacheBuilderSpec("expireAfterWrite=" + duration + timeUnit.toString());
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().expireAfterWrite(duration, timeUnit);
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_refreshAfterWrite() {
 final long duration = 60;
 final TimeUnit timeUnit = TimeUnit.SECONDS;
 final CacheBuilderSpec spec = new CacheBuilderSpec("refreshAfterWrite=" + duration + timeUnit.toString());
 final CacheBuilder<Object, Object> expected = CacheBuilder.newBuilder().refreshAfterWrite(duration, timeUnit);
 final CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(expected, actual);
 }

 @Test
 public void testToCacheBuilder_invalidExpirationSetting() {
 final String spec = "expireAfterWrite=invalid";
 try {
 CacheBuilderSpec.parse(spec).toCacheBuilder();
 fail("An IllegalArgumentException was expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}