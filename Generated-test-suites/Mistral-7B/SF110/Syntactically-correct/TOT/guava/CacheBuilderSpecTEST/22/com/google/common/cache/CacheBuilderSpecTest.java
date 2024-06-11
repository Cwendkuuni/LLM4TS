package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setUp() {
 spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=5, maximumWeight=10, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterWrite=1h, expireAfterAccess=30m, refreshAfterWrite=1h");
 }

 @Test
 public void testParseInitialCapacity() {
 assertEquals(10, spec.initialCapacity);
 }

 @Test
 public void testParseMaximumSize() {
 assertEquals(5, spec.maximumSize.intValue());
 }

 @Test
 public void testParseMaximumWeight() {
 assertEquals(10, spec.maximumWeight.longValue());
 }

 @Test
 public void testParseConcurrencyLevel() {
 assertEquals(5, spec.concurrencyLevel.intValue());
 }

 @Test
 public void testParseKeyStrength_WEAK() {
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 }

 @Test
 public void testParseValueStrength_SOFT() {
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 }

 @Test
 public void testParseRecordStats() {
 assertTrue(spec.recordStats);
 }

 @Test
 public void testParseWriteExpirationDuration() {
 assertEquals(3600000L, spec.writeExpirationDuration);
 }

 @Test
 public void testParseWriteExpirationTimeUnit() {
 assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
 }

 @Test
 public void testParseAccessExpirationDuration() {
 assertEquals(1800000L, spec.accessExpirationDuration);
 }

 @Test
 public void testParseAccessExpirationTimeUnit() {
 assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);
 }

 @Test
 public void testParseRefreshDuration() {
 assertEquals(3600000L, spec.refreshDuration);
 }

 @Test
 public void testParseRefreshTimeUnit() {
 assertEquals(TimeUnit.HOURS, spec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownKey() {
 try {
 new CacheBuilderSpec("unknownKey=value");
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("unknownKey value set to value, must be one of [initialCapacity, maximumSize, maximumWeight, concurrencyLevel, keyStrength, valueStrength, recordStats, expireAfterWrite, expireAfterAccess, refreshAfterWrite]", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValueType() {
 try {
 new CacheBuilderSpec("maximumSize=abc");
 fail("Should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("key maximumSize value set to abc, must be integer", e.getMessage());
 }
 }

 @Test(expected = AssertionError.class)
 public void testUnknownKeyStrength() {
 try {
 new CacheBuilderSpec("keyStrength=STRONG");
 fail("Should have thrown AssertionError");
 } catch (AssertionError e) {
 // Expected
 }
 }
}