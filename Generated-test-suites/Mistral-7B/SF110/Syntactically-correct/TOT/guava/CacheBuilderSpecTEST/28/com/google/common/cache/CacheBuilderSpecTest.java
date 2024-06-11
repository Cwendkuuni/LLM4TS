package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

 @Test
 public void testParseEmptySpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("");
 assertTrue(spec.initialCapacity == null);
 assertTrue(spec.maximumSize == null);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 assertTrue(spec.specification.isEmpty());
 }

 @Test
 public void testParseSimpleSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10");
 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 assertEquals("initialCapacity=5,maximumSize=10", spec.toParsableString());
 }

 @Test
 public void testParseComplexSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5," +
 " maximumSize=10," +
 " maximumWeight=20," +
 " concurrencyLevel=5," +
 " weakKeys," +
 " softValues," +
 " recordStats," +
 " expireAfterAccess=1h," +
 " expireAfterWrite=2d," +
 " refreshAfterWrite=30s");
 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(20, spec.maximumWeight);
 assertEquals(5, spec.concurrencyLevel);
 assertTrue(spec.keyStrength == LocalCache.Strength.WEAK);
 assertTrue(spec.valueStrength == LocalCache.Strength.SOFT);
 assertTrue(spec.recordStats);
 assertEquals(3600000L, spec.accessExpirationDuration);
 assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
 assertEquals(2 * 24 * 60 * 60 * 1000L, spec.writeExpirationDuration);
 assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
 assertEquals(30000L, spec.refreshDuration);
 assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
 assertEquals("initialCapacity=5," +
 " maximumSize=10," +
 " maximumWeight=20," +
 " concurrencyLevel=5," +
 " weakKeys," +
 " softValues," +
 " recordStats," +
 " expireAfterAccess=1h," +
 " expireAfterWrite=2d," +
 " refreshAfterWrite=30s", spec.toParsableString());
 }

 @Test
 public void testParseInvalidSpec() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10=,maximumWeight");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertTrue(CacheBuilderSpec.KEYS_SPLITTER.split("initialCapacity=5,maximumSize=10=,maximumWeight")[1].contains("="));
 }

 @Test
 public void testParseWithNullValue() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5," +
 " maximumSize=10," +
 " maximumWeight=null," +
 " concurrencyLevel=5," +
 " weakKeys," +
 " softValues," +
 " recordStats," +
 " expireAfterAccess=1h," +
 " expireAfterWrite=2d," +
 " refreshAfterWrite=30s");
 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
assertEquals(null, spec.maximumWeight);
 assertEquals(5, spec.concurrencyLevel);
 assertTrue(spec.keyStrength == LocalCache.Strength.WEAK);
 assertTrue(spec.valueStrength == LocalCache.Strength.SOFT);
 assertTrue(spec.recordStats);
 assertEquals(3600000L, spec.accessExpirationDuration);
 assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
 assertEquals(2 * 24 * 60 * 60 * 1000L, spec.writeExpirationDuration);
 assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
 assertEquals(30000L, spec.refreshDuration);
 assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
 }

 @Test
 public void testParseUnknownKey() {
 try {
 CacheBuilderSpec.parse("unknownKey=10");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testParseUnknownValueParser() {
 try {
 CacheBuilderSpec.parse("maximumWeight=invalidParser");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}