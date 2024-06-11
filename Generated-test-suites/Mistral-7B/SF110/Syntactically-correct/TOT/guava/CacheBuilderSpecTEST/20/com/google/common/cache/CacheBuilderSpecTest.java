package com.google.common.cache;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Splitter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CacheBuilderSpecTest {

 private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
 private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
 private static final Map<String, ValueParser> VALUE_PARSERS = CacheBuilderSpec.VALUE_PARSERS;

 private CacheBuilderSpec spec;

 @Before
 public void setup() {
 spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,maximumWeight=200," +
 "concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterAccess=1h," +
 "expireAfterWrite=2d,refreshAfterWrite=3h");
 }

 @Test
 public void testParseInitialCapacity() {
 assertEquals(10, spec.initialCapacity);
 }

 @Test
 public void testParseMaximumSize() {
 assertEquals(100, spec.maximumSize.longValue());
 assertEquals(100, spec.maximumSize.intValue());
 }

 @Test
 public void testParseMaximumWeight() {
 assertEquals(200, spec.maximumWeight.longValue());
 assertEquals(200, spec.maximumWeight.intValue());
 }

 @Test
 public void testParseConcurrencyLevel() {
 assertEquals(10, spec.concurrencyLevel.intValue());
 }

 @Test
 public void testParseKeyStrength() {
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 }

 @Test
 public void testParseValueStrength() {
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 }

 @Test
 public void testParseRecordStats() {
 assertTrue(spec.recordStats);
 }

 @Test
 public void testParseAccessExpiration() {
 assertEquals(1, TimeUnit.HOURS.toMinutes(spec.accessExpirationDuration.longValue()));
 assertEquals(spec.accessExpirationDuration, spec.toCacheBuilder().expireAfterAccess(1, TimeUnit.HOURS));
 }

 @Test
 public void testParseWriteExpiration() {
 assertEquals(2, TimeUnit.DAYS.toHours(spec.writeExpirationDuration.longValue()));
 assertEquals(spec.writeExpirationDuration, spec.toCacheBuilder().expireAfterWrite(2, TimeUnit.DAYS));
 }

 @Test
 public void testParseRefreshDuration() {
 assertEquals(3, TimeUnit.HOURS.toMinutes(spec.refreshDuration.longValue()));
 assertEquals(spec.refreshDuration, spec.toCacheBuilder().refreshAfterWrite(3, TimeUnit.HOURS));
 }

 @Test(expected = AssertionError.class)
 public void testUnknownKey() {
 spec = CacheBuilderSpec.parse("unknownKey=value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingValue() {
 spec = CacheBuilderSpec.parse("initialCapacity=");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntegerValue() {
 spec = CacheBuilderSpec.parse("initialCapacity=invalid");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDurationFormat() {
 spec = CacheBuilderSpec.parse("expireAfterAccess=1h,invalid");
 }

 @Test
 public void testToParsableString() {
 assertEquals("initialCapacity=10,maximumSize=100,maximumWeight=200," +
 "concurrencyLevel=10,weakKeys,softValues,recordStats," +
 "expireAfterAccess=1h,expireAfterWrite=2d,refreshAfterWrite=3h", spec.toParsableString());
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Integer, String> builder = spec.toCacheBuilder();
 assertEquals(10, builder.initialCapacity());
 assertEquals(100, builder.maximumSize());
 assertEquals(200, builder.maximumWeight());
 assertEquals(10, builder.concurrencyLevel());
 assertEquals(LocalCache.Strength.WEAK, builder.weakKeys());
 assertEquals(LocalCache.Strength.SOFT, builder.softValues());
 assertTrue(builder.recordStats());
 assertEquals(1, TimeUnit.HOURS.toMinutes(builder.expireAfterAccess().get(TimeUnit.MILLISECONDS)), 0);
 assertEquals(2, TimeUnit.DAYS.toHours(builder.expireAfterWrite().get(TimeUnit.MILLISECONDS)), 0);
 assertEquals(3, TimeUnit.HOURS.toMinutes(builder.refreshAfterWrite().get(TimeUnit.MILLISECONDS)), 0);
 }

 @Test
 public void testToCacheBuilderEmptySpec() {
 CacheBuilderSpec emptySpec = CacheBuilderSpec.parse("");
 assertNull(emptySpec.toCacheBuilder());
 }
}