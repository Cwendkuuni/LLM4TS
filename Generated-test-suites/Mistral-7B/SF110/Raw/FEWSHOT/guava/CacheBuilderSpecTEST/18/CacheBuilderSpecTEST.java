package com.google.common.cache;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(JUnit4.class)
public class CacheBuilderSpecTest {

 private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
 private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
 private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = CacheBuilderSpec.VALUE_PARSERS;

 @Test
 public void testParse_emptyString_returnsEmptySpec() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("");
     assertEquals(new CacheBuilderSpec(""), spec);
 }

 @Test
 public void testParse_initialCapacity() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
     assertEquals(new CacheBuilderSpec("initialCapacity=10"), spec);
     assertEquals(10, spec.getInitialCapacity());
 }

 @Test
 public void testParse_initialCapacity_previousSet() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10, initialCapacity=5");
     assertEquals(new CacheBuilderSpec("initialCapacity=10"), spec);
     assertEquals(10, spec.getInitialCapacity());
 }

 @Test
 public void testParse_maxSize() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10");
     assertEquals(new CacheBuilderSpec("maximumSize=10"), spec);
     assertEquals(10, spec.getMaximumSize());
 }

 @Test
 public void testParse_maxSize_previousSet() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10, maximumSize=5");
     assertEquals(new CacheBuilderSpec("maximumSize=10"), spec);
     assertEquals(10, spec.getMaximumSize());
 }

 @Test
 public void testParse_maxWeight() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=10");
     assertEquals(new CacheBuilderSpec("maximumWeight=10"), spec);
     assertEquals(10, spec.getMaximumWeight());
 }

 @Test
 public void testParse_maxWeight_previousSet() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=10, maximumWeight=5");
     assertEquals(new CacheBuilderSpec("maximumWeight=10"), spec);
     assertEquals(10, spec.getMaximumWeight());
 }

 @Test
 public void testParse_concurrencyLevel() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=10");
     assertEquals(new CacheBuilderSpec("concurrencyLevel=10"), spec);
     assertEquals(10, spec.getConcurrencyLevel());
 }

 @Test
 public void testParse_concurrencyLevel_previousSet() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=10, concurrencyLevel=5");
     assertEquals(new CacheBuilderSpec("concurrencyLevel=10"), spec);
     assertEquals(10, spec.getConcurrencyLevel());
 }

 @Test
 public void testParse_weakKeys() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
     assertEquals(new CacheBuilderSpec("weakKeys"), spec);
     assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
 }

 @Test
 public void testParse_weakKeys_previousSet() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys, keyStrength=SOFT");
     assertEquals(new CacheBuilderSpec("weakKeys"), spec);
     assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
 }

 @Test
 public void testParse_softValues() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
     assertEquals(new CacheBuilderSpec("softValues"), spec);
     assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
 }

 @Test
 public void testParse_softValues_previousSet() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues, valueStrength=WEAK");
     assertEquals(new CacheBuilderSpec("softValues"), spec);
     assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
 }

 @Test
 public void testParse_recordStats() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats");
     assertEquals(new CacheBuilderSpec("recordStats"), spec);
     assertTrue(spec.isRecordStats());
 }

 @Test
 public void testParse_recordStats_previousSet() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats, recordStats=false");
     assertEquals(new CacheBuilderSpec("recordStats"), spec);
     assertTrue(spec.isRecordStats());
 }

 @Test
 public void testParse_expireAfterAccess() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=30s");
     assertEquals(new CacheBuilderSpec("expireAfterAccess=30s"), spec);
     assertEquals(30, TimeUnit.SECONDS.toMillis(spec.getAccessExpirationDuration()));
     assertEquals(TimeUnit.SECONDS, spec.getAccessExpirationTimeUnit());
 }

 @Test
 public void testParse_expireAfterAccess_previousSet() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=30s, expireAfterAccess=5m");
     assertEquals(new CacheBuilderSpec("expireAfterAccess=30s"), spec);
     assertEquals(30, TimeUnit.SECONDS.toMillis(spec.getAccessExpirationDuration()));
     assertEquals(TimeUnit.SECONDS, spec.getAccessExpirationTimeUnit());
 }

 @Test
 public void testParse_expireAfterWrite() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=30s");
     assertEquals(new CacheBuilderSpec("expireAfterWrite=30s"), spec);
     assertEquals(30, TimeUnit.SECONDS.toMillis(spec.getWriteExpirationDuration()));
     assertEquals(TimeUnit.SECONDS, spec.getWriteExpirationTimeUnit());
 }

 @Test
 public void testParse_expireAfterWrite_previousSet() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=30s, expireAfterWrite=5m");
     assertEquals(new CacheBuilderSpec("expireAfterWrite=30s"), spec);
     assertEquals(30, TimeUnit.SECONDS.toMillis(spec.getWriteExpirationDuration()));
     assertEquals(TimeUnit.SECONDS, spec.getWriteExpirationTimeUnit());
 }

 @Test
 public void testParse_refreshAfterWrite() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=30s");
     assertEquals(new CacheBuilderSpec("refreshAfterWrite=30s"), spec);
     assertEquals(30, TimeUnit.SECONDS.toMillis(spec.getRefreshDuration()));
     assertEquals(TimeUnit.SECONDS, spec.getRefreshTimeUnit());
 }

 @Test
 public void testParse_refreshAfterWrite_previousSet() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=30s, refreshAfterWrite=5m");
     assertEquals(new CacheBuilderSpec("refreshAfterWrite=30s"), spec);
     assertEquals(30, TimeUnit.SECONDS.toMillis(spec.getRefreshDuration()));
     assertEquals(TimeUnit.SECONDS, spec.getRefreshTimeUnit());
 }

 @Test
 public void testParse_unknownKey() {
     assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("unknownKey=value"));
 }

 @Test
 public void testParse_unknownValueType() {
     assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("initialCapacity=-1d"));
 }

 @Test
 public void testParse_invalidDurationFormat() {
     assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("expireAfterAccess=30sx"));
 }

 @Test
 public void testParse_invalidDurationUnit() {
     assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("expireAfterAccess=30xy"));
 }

 @Test
 public void testToCacheBuilder_noSpecifiedParameters() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("");
     assertEquals(CacheBuilder.newBuilder(), spec.toCacheBuilder());
 }

 @Test
 public void testToCacheBuilder_allSpecifiedParameters() {
     CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10\n" +
                                                   "maximumSize=500\n" +
                                                   "maximumWeight=50000\n" +
                                                   "concurrencyLevel=10\n" +
                                                   "weakKeys\n" +
                                                   "softValues\n" +
                                                   "recordStats\n" +
                                                   "expireAfterAccess=30s\n" +
                                                   "expireAfterWrite=5m\n" +
                                                   "refreshAfterWrite=1h");
     CacheBuilder<Object, Object> expectedCacheBuilder = CacheBuilder.newBuilder()
             .initialCapacity(10)
             .maximumSize(500)
             .maximumWeight(50000)
             .concurrencyLevel(10)
             .weakKeys()
             .softValues()
             .recordStats()
             .expireAfterAccess(30, TimeUnit.SECONDS)
             .expireAfterWrite(5, TimeUnit.MINUTES)
             .refreshAfterWrite(1, TimeUnit.HOURS);
     assertEquals(expectedCacheBuilder, spec.toCacheBuilder());
 }

}