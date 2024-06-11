package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;
import static com.google.common.base.Preconditions.*;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setup() {
 spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20," +
 "concurrencyLevel=5, weakKeys, weakValues, recordStats, expireAfterAccess=1h," +
 "expireAfterWrite=2d, refreshAfterWrite=3h");
 }

 @Test
 public void testParseValidSpec() {
 assertNotNull(spec);
 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(20, spec.maximumWeight);
 assertEquals(5, spec.concurrencyLevel);
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
 assertTrue(spec.recordStats);
 assertEquals(Duration.ofHours(1), spec.accessExpirationDuration, 1);
 assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
 assertEquals(Duration.ofDays(2), spec.writeExpirationDuration, 1);
 assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
 assertEquals(Duration.ofHours(3), spec.refreshDuration, 1);
 assertEquals(TimeUnit.HOURS, spec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecNoKeyValuePair() {
 CacheBuilderSpec.parse("invalid");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecMissingEqualSign() {
 CacheBuilderSpec.parse("initialCapacity=5, maximumSize 10");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecUnknownKey() {
 CacheBuilderSpec.parse("unknownKey=1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecUnparseableValue() {
 CacheBuilderSpec.parse("initialCapacity=invalid");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecUnsupportedKey() {
 CacheBuilderSpec.parse("invalidKey=1");
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidSpecUnsupportedKeyStrength() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("keyStrength=unknownValue");
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidSpecUnsupportedValueStrength() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("valueStrength=unknownValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecMissingValue() {
 CacheBuilderSpec.ValueParser valueParser = new ValueParser() {
 @Override
 public void parse(CacheBuilderSpec spec, String key, String value) {
 checkArgument(value == null, "value of key %s omitted", key);
 spec.keyStrength = LocalCache.Strength.WEAK;
 }
 };
 CacheBuilderSpec.VALUE_PARSERS.put("keyWithoutValue", valueParser);
 CacheBuilderSpec spec = CacheBuilderSpec.parse("keyWithoutValue=1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecInvalidValueType() {
 CacheBuilderSpec.ValueParser valueParser = new ValueParser() {
 @Override
 public void parse(CacheBuilderSpec spec, String key, String value) {
 checkArgument(!value.isEmpty(), (Object)"value of key %s omitted", key);
 try {
 int i = Integer.parseInt(value);
 } catch (NumberFormatException e) {
 throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", key, value));
 }
 }
 };
 CacheBuilderSpec.VALUE_PARSERS.put("invalidValue", valueParser);
 CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidValue=invalid");
 }
}