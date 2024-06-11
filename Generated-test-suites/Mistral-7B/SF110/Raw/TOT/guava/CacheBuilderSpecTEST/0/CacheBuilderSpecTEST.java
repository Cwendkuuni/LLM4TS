package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;
import static org.mockito.Mockito.*;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;
 private List<String> keys;

 @Before
 public void setUp() {
 spec = new CacheBuilderSpec("maximumSize=10,initialCapacity=5,weakKeys,softValues,writeExpirationDuration=1h,recordStats");
 keys = Arrays.asList("key1", "key2", "key3");
 }

 @Test
 public void testParseValidSpec() {
 assertEquals("maximumSize=10,initialCapacity=5,weakKeys,softValues,writeExpirationDuration=1h,recordStats", spec.toParsableString());
 assertEquals(10, spec.maximumSize);
 assertEquals(5, spec.initialCapacity);
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 assertEquals(true, spec.recordStats);
 assertEquals(1, spec.writeExpirationTimeUnit.toHours(spec.writeExpirationDuration));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecEmpty() {
 spec = new CacheBuilderSpec("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecMultipleKeys() {
 spec = new CacheBuilderSpec("initialCapacity=5,maximumSize=10=key1=value1,weakKeys");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecUnknownKey() {
 spec = new CacheBuilderSpec("unknownKey=value1,maximumSize=10");
 }

 @Test
 public void testParseIntegerValue() {
 ValueParser parser = new CacheBuilderSpec.InitialCapacityParser();
 spec = new CacheBuilderSpec("");
 parser.parse(spec, "initialCapacity", "5");
 assertEquals(5, spec.initialCapacity);
 }

 @Test
 public void testParseLongValue() {
 ValueParser parser = new CacheBuilderSpec.MaximumSizeParser();
 spec = new CacheBuilderSpec("");
 parser.parse(spec, "maximumSize", "10");
 parser.parse(spec, "maximumSize", "20");
 assertEquals(20, spec.maximumSize);
 }

 @Test
 public void testParseBooleanValue() {
 ValueParser parser = new CacheBuilderSpec.RecordStatsParser();
 spec = new CacheBuilderSpec("");
 parser.parse(spec, "recordStats", null);
 assertTrue(spec.recordStats);
 parser.parse(spec, "recordStats", "false");
 assertFalse(spec.recordStats);
 }

 @Test
 public void testParseDurationValue() {
 ValueParser parser = new CacheBuilderSpec.AccessDurationParser();
 spec = new CacheBuilderSpec("");
 parser.parse(spec, "expireAfterAccess", "1h");
 assertEquals(3600L, spec.accessExpirationDuration);
 assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);

 parser = new CacheBuilderSpec.WriteDurationParser();
 spec = new CacheBuilderSpec("");
 parser.parse(spec, "expireAfterWrite", "2m30s");
 assertEquals(150L, spec.writeExpirationDuration);
 assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
 }

 @Test
 public void testParseDurationValueWithInvalidUnit() {
 ValueParser parser = new CacheBuilderSpec.AccessDurationParser();
 spec = new CacheBuilderSpec("");
 try {
 parser.parse(spec, "expireAfterAccess", "1w");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(5, cacheBuilder.initialCapacity());
 assertEquals(10, cacheBuilder.maximumSize());
 assertEquals(LocalCache.Strength.WEAK, cacheBuilder.weakKeys());
 assertEquals(LocalCache.Strength.SOFT, cacheBuilder.softValues());
 assertTrue(cacheBuilder.recordStats());
 assertEquals(1, cacheBuilder.expireAfterWrite(1, TimeUnit.HOURS).toHours(1));
 }

 @Test
 public void testToParsableString() {
 assertEquals("maximumSize=10,initialCapacity=5,weakKeys,softValues,writeExpirationDuration=1h,recordStats", spec.toParsableString());
 }

 @Test
 public void testToString() {
 assertEquals("CacheBuilderSpec{maximumSize=10, initialCapacity=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=1h, writeExpirationTimeUnit=HOURS, accessExpirationDuration=0, accessExpirationTimeUnit=NONE, refreshDuration=0, refreshTimeUnit=NONE, specification=maximumSize=10,initialCapacity=5,weakKeys,softValues,writeExpirationDuration=1h,recordStats}", spec.toString());
 }

 @Test
 public void testHashCodeEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=10,initialCapacity=5,weakKeys,softValues,writeExpirationDuration=1h,recordStats");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=10,initialCapacity=5,weakKeys,softValues,writeExpirationDuration=1h,recordStats");
 CacheBuilderSpec spec3 = new CacheBuilderSpec("maximumSize=10,initialCapacity=5,weakKeys,softValues,writeExpirationDuration=2h,recordStats");

 assertEquals(spec1, spec1);
 assertEquals(spec1, spec2);
 assertNotEquals(spec1, spec3);
 }
}