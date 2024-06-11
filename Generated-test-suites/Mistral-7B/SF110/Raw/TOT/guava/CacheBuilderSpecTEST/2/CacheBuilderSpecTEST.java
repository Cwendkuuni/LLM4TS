package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setUp() {
 spec = new CacheBuilderSpec("initialCapacity=1,maximumSize=5,concurrencyLevel=3," +
 "keyStrength=weak,valueStrength=soft,recordStats=true," +
 "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS," +
 "accessExpirationDuration=30s,accessExpirationTimeUnit=SECONDS," +
 "refreshDuration=5m,refreshTimeUnit=MINUTES");
 }

 @Test
 public void testParse() {
 Assert.assertEquals(1, spec.getInitialCapacity());
 Assert.assertEquals(5L, spec.getMaximumSize());
 Assert.assertEquals(3, spec.getConcurrencyLevel());
 Assert.assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
 Assert.assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
 Assert.assertTrue(spec.isRecordStats());
 Assert.assertEquals(1L, spec.getWriteExpirationDuration(), 0.1);
 Assert.assertEquals(TimeUnit.HOURS, spec.getWriteExpirationTimeUnit());
 Assert.assertEquals(30L, spec.getAccessExpirationDuration(), 0.1);
 Assert.assertEquals(TimeUnit.SECONDS, spec.getAccessExpirationTimeUnit());
 Assert.assertEquals(5L, spec.getRefreshDuration(), 0.1);
 Assert.assertEquals(TimeUnit.MINUTES, spec.getRefreshTimeUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownKey() {
 try {
 CacheBuilderSpec.parse("unknownKey=value");
 } catch (IllegalArgumentException e) {
 Assert.assertEquals("unknownKey value set to value, must be one of [initialCapacity, maximumSize, maximumWeight, concurrencyLevel, keyStrength, valueStrength, recordStats, writeExpirationDuration, writeExpirationTimeUnit, accessExpirationDuration, accessExpirationTimeUnit, refreshDuration, refreshInterval]", e.getMessage());
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidDuration() {
 try {
 CacheBuilderSpec.parse("accessExpirationDuration=invalid");
 } catch (NumberFormatException e) {
 // expected
 } catch (IllegalArgumentException e) {
 Assert.fail();
 }
 }

 @Test
 public void testToParsableString() {
 String specString = spec.toParsableString();
 String expectedSpecString = "initialCapacity=1,maximumSize=5,concurrencyLevel=3," +
 "keyStrength=weak,valueStrength=soft,recordStats=true," +
 "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS," +
 "accessExpirationDuration=30s,accessExpirationTimeUnit=SECONDS," +
 "refreshDuration=5m,refreshTimeUnit=MINUTES";
 Assert.assertEquals(expectedSpecString, specString);
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 Assert.assertEquals(1, cacheBuilder.initialCapacity());
 Assert.assertEquals(5, cacheBuilder.maximumSize());
 Assert.assertEquals(3, cacheBuilder.concurrencyLevel());
 Assert.assertTrue(cacheBuilder.recordStats());
 Assert.assertEquals(TimeUnit.HOURS, cacheBuilder.expireAfterWrite());
 Assert.assertEquals(TimeUnit.MINUTES, cacheBuilder.refreshAfterWrite());
 }

 @Test
 public void testEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=1,maximumSize=5,concurrencyLevel=3," +
 "keyStrength=weak,valueStrength=soft,recordStats=true," +
 "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS," +
 "accessExpirationDuration=30s,accessExpirationTimeUnit=SECONDS," +
 "refreshDuration=5m,refreshTimeUnit=MINUTES");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=1,maximumSize=5,concurrencyLevel=3," +
 "keyStrength=weak,valueStrength=soft,recordStats=true," +
 "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS," +
 "accessExpirationDuration=30s,accessExpirationTimeUnit=SECONDS," +
 "refreshDuration=5m,refreshTimeUnit=MINUTES");
 Assert.assertEquals(spec1, spec2);
 }

 @Test
 public void testNotEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=1,maximumSize=5,concurrencyLevel=3," +
 "keyStrength=weak,valueStrength=soft,recordStats=true," +
 "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS," +
 "accessExpirationDuration=30s,accessExpirationTimeUnit=SECONDS," +
 "refreshDuration=5m,refreshTimeUnit=MINUTES");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=1,maximumSize=5,concurrencyLevel=3," +
 "keyStrength=weak,valueStrength=soft,recordStats=false," +
 "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS," +
 "accessExpirationDuration=30s,accessExpirationTimeUnit=SECONDS," +
 "refreshDuration=5m,refreshTimeUnit=MINUTES");
 Assert.assertNotEquals(spec1, spec2);
 }
}