package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setUp() {
 spec = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,concurrencyLevel=5," +
 "keyStrength=LocalCache.Strength.STRONG,valueStrength=LocalCache.Strength.STRONG," +
 "recordStats=true,expireAfterWrite=1d,expireAfterAccess=2h,refreshAfterWrite=3s");
 }

 @Test
 public void testParseInitialCapacity() {
 assertEquals(5, spec.initialCapacity);
 }

 @Test
 public void testParseMaximumSize() {
 assertEquals(10, spec.maximumSize);
 }

 @Test
 public void testParseMaximumWeight() {
 assertNull(spec.maximumWeight);
 }

 @Test
 public void testParseConcurrencyLevel() {
 assertEquals(5, spec.concurrencyLevel);
 }

 @Test
 public void testParseKeyStrength() {
 assertEquals(LocalCache.Strength.STRONG, spec.keyStrength);
 }

 @Test
 public void testParseValueStrength() {
 assertEquals(LocalCache.Strength.STRONG, spec.valueStrength);
 }

 @Test
 public void testParseRecordStats() {
 assertTrue(spec.recordStats);
 }

 @Test
 public void testParseAccessDuration() {
 assertEquals(TimeUnit.DAYS, spec.accessExpirationTimeUnit);
 assertEquals(1, spec.accessExpirationDuration.longValue());
 }

 @Test
 public void testParseWriteDuration() {
 assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
 assertEquals(1, spec.writeExpirationDuration.longValue());
 }

 @Test
 public void testParseRefreshDuration() {
 assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
 assertEquals(3, spec.refreshDuration.longValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownKey() {
 final CacheBuilderSpec unknownKeySpec = new CacheBuilderSpec("unknownKey=1");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntegerValue() {
 final CacheBuilderSpec invalidSpec = new CacheBuilderSpec("initialCapacity=-5");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingValue() {
 final CacheBuilderSpec missingValueSpec = new CacheBuilderSpec("initialCapacity=");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTimeUnit() {
 final CacheBuilderSpec invalidTimeUnitSpec = new CacheBuilderSpec("expireAfterWrite=1x");
 }

 @Test(expected = AssertionError.class)
 public void testUnknownKeyStrength() {
 final CacheBuilderSpec unknownKeyStrengthSpec = new CacheBuilderSpec("keyStrength=UNDEFINED");
 }

 @Test(expected = AssertionError.class)
 public void testUnknownValueStrength() {
 final CacheBuilderSpec unknownValueStrengthSpec = new CacheBuilderSpec("valueStrength=UNDEFINED");
 }
}