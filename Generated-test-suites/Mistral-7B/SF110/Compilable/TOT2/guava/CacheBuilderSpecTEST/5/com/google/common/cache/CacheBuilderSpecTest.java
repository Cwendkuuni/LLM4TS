package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.InitialCapacityParser;
import com.google.common.cache.CacheBuilderSpec.MaximumSizeParser;
import com.google.common.cache.CacheBuilderSpec.MaximumWeightParser;
import com.google.common.cache.CacheBuilderSpec.ConcurrencyLevelParser;
import com.google.common.cache.CacheBuilderSpec.KeyStrengthParser;
import com.google.common.cache.CacheBuilderSpec.ValueStrengthParser;
import com.google.common.cache.CacheBuilderSpec.RecordStatsParser;
import com.google.common.cache.CacheBuilderSpec.AccessDurationParser;
import com.google.common.cache.CacheBuilderSpec.WriteDurationParser;
import com.google.common.cache.CacheBuilderSpec.RefreshDurationParser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.*;

public class CacheBuilderSpecTest {

 private final static String basicSpec = "initialCapacity=5, maximumSize=10";
 private final static String invalidSpec = "initialCapacity=5, maximumSize=10=15";

 @Test
 public void testParseBasicSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(basicSpec);
 CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().initialCapacity(5).maximumSize(10), builder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(invalidSpec);
 }

 @Test
 public void testParseWithInitialCapacity() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
 assertEquals(new CacheBuilderSpec("initialCapacity=10"), spec);
 }

 @Test
 public void testParseWithMaximumSize() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=15");
 assertEquals(new CacheBuilderSpec("maximumSize=15"), spec);
 }

 @Test
 public void testParseWithMaximumWeight() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=20");
 assertEquals(new CacheBuilderSpec("maximumWeight=20"), spec);
 }

 @Test
 public void testParseWithConcurrencyLevel() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=5");
 assertEquals(new CacheBuilderSpec("concurrencyLevel=5"), spec);
 }

 @Test
 public void testParseWithKeyStrength() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
 assertEquals(new CacheBuilderSpec("keyStrength=WEAK"), spec);
 }

 @Test
 public void testParseWithValueStrength() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
 assertEquals(new CacheBuilderSpec("valueStrength=SOFT"), spec);
 }

 @Test
 public void testParseWithRecordStats() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats");
 assertEquals(new CacheBuilderSpec("recordStats=true"), spec);
 }

 @Test
 public void testParseWithAccessExpiration() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=30s");
 assertEquals(new CacheBuilderSpec("accessExpirationDuration=30, accessExpirationTimeUnit=SECONDS"), spec);
 }

 @Test
 public void testParseWithWriteExpiration() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=5m");
 assertEquals(new CacheBuilderSpec("writeExpirationDuration=300, writeExpirationTimeUnit=MINUTES"), spec);
 }

 @Test
 public void testParseWithRefreshExpiration() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=1h");
 assertEquals(new CacheBuilderSpec("refreshDuration=3600, refreshTimeUnit=HOURS"), spec);
 }

 @Test
 public void testParseWithAllProperties() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20," +
 " concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=30s, expireAfterWrite=5m," +
 " refreshAfterWrite=1h");
 assertEquals(new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20," +
 " concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true," +
 " writeExpirationDuration=300, writeExpirationTimeUnit=MINUTES, accessExpirationDuration=30," +
 " accessExpirationTimeUnit=SECONDS, refreshDuration=3600, refreshTimeUnit=HOURS"), spec);
 }
}