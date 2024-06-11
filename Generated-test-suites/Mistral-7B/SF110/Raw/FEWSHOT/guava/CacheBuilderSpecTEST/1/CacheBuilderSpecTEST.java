package com.google.common.cache;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import com.google.common.annotations.*;
import com.google.common.base.*;
import com.google.common.cache.*;
import com.google.common.collect.*;
import java.util.*;
import javax.annotation.*;

@Beta
public class CacheBuilderSpecTest {

 static final String DEFAULT_SPEC = "initialCapacity=10, maximumSize=100, maximumWeight=1000, concurrencyLevel=16, keyStrength=weak, valueStrength=weak, recordStats=false, expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1h";
 static final CacheBuilderSpec DEFAULT_BUILDER = CacheBuilderSpec.parse(DEFAULT_SPEC);

 @Test
 public void testParseDefaultSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(DEFAULT_SPEC);
 assertEquals(DEFAULT_BUILDER, spec);
 }

 @Test
 public void testParseInvalidSpec() {
 String invalidSpec = "initialCapacity=10, maximumSize=100, maximumWeight=1000, concurrencyLevel=16, keyStrength=weak, valueStrength=strong, recordStats=false, expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1h, invalid=5";
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(invalidSpec);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertNotNull(e.getMessage());
 }
 }

 @Test
 public void testParseKeyValuePair() {
 List<String> keysValuePairList = ImmutableList.of("initialCapacity=10", "maximumSize=50", "concurrencyLevel=16");
 for (String keyValuePair : keysValuePairList) {
 List<String> keyAndValue = (List<String>) ImmutableList.copyOf((Iterable<?>) CacheBuilderSpec.KEY_VALUE_SPLITTER.split(keyValuePair));
 String key = keyAndValue.get(0);
 String value = (keyAndValue.size() > 1) ? keyAndValue.get(1) : null;

 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.parse(key, value);

 switch (key) {
 case "initialCapacity":
 assertEquals(10, spec.initialCapacity);
 break;
 case "maximumSize":
 assertEquals(50, spec.maximumSize);
 break;
 case "concurrencyLevel":
 assertEquals(16, spec.concurrencyLevel);
 break;
 default:
 fail("Unexpected key: " + key);
 }
 }
 }

 @Test
 public void testParseStringValue() {
 List<String> keysValuePairList = ImmutableList.of("keyStrength=weak", "recordStats=true");
 for (String keyValuePair : keysValuePairList) {
 List<String> keyAndValue = (List<String>) ImmutableList.copyOf((Iterable<?>) CacheBuilderSpec.KEY_VALUE_SPLITTER.split(keyValuePair));
 String key = keyAndValue.get(0);
 String value = keyAndValue.get(1);

 CacheBuilderSpec spec = new CacheBuilderSpec("");
 ValueParser valueParser = CacheBuilderSpec.VALUE_PARSERS.get(key);
 valueParser.parse(spec, key, value);

 switch (key) {
 case "keyStrength":
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 break;
 case "recordStats":
 assertTrue(spec.recordStats);
 break;
 default:
 fail("Unexpected key: " + key);
 }
 }
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = DEFAULT_BUILDER.toCacheBuilder();
 assertEquals(DEFAULT_BUILDER.maximumSize, cacheBuilder.maximumSize());
 assertEquals(DEFAULT_BUILDER.maximumWeight, cacheBuilder.maximumWeight());
 assertEquals(DEFAULT_BUILDER.concurrencyLevel, cacheBuilder.concurrencyLevel());
 assertEquals(DEFAULT_BUILDER.keyStrength, cacheBuilder.weakKeys());
 assertEquals(DEFAULT_BUILDER.valueStrength, cacheBuilder.softValues());
 assertEquals(DEFAULT_BUILDER.recordStats, cacheBuilder.recordStats());
 assertEquals(DEFAULT_BUILDER.writeExpirationDuration, cacheBuilder.expireAfterWrite().getDuration(), 100);
 assertEquals(DEFAULT_BUILDER.writeExpirationTimeUnit, cacheBuilder.expireAfterWrite().getUnit());
 assertEquals(DEFAULT_BUILDER.accessExpirationDuration, cacheBuilder.expireAfterAccess().getDuration(), 100);
 assertEquals(DEFAULT_BUILDER.accessExpirationTimeUnit, cacheBuilder.expireAfterAccess().getUnit());
 assertEquals(DEFAULT_BUILDER.refreshDuration, cacheBuilder.refreshAfterWrite().getDuration(), 100);
 assertEquals(DEFAULT_BUILDER.refreshTimeUnit, cacheBuilder.refreshAfterWrite().getUnit());
 }

 @Test
 public void testToParsableString() {
 String parsedString = DEFAULT_BUILDER.toParsableString();
 assertEquals(DEFAULT_SPEC, parsedString);
 }

 @Test
 public void testToString() {
 String toString = DEFAULT_BUILDER.toString();
 assertEquals(MoreObjects.toStringHelper(DEFAULT_BUILDER).addValue(DEFAULT_BUILDER.toParsableString()).toString(), toString);
 }

 @Test
 public void testHashCodeAndEquals() {
 CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=1000, concurrencyLevel=16, keyStrength=weak, valueStrength=weak, recordStats=false, expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1h");
 CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=1000, concurrencyLevel=16, keyStrength=weak, valueStrength=weak, recordStats=false, expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1h");
 assertEquals(spec1, spec2);

 CacheBuilderSpec spec3 = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=50, maximumWeight=1000, concurrencyLevel=16, keyStrength=weak, valueStrength=weak, recordStats=false, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=1h");
 assertNotEquals(spec1, spec3);
 }
}