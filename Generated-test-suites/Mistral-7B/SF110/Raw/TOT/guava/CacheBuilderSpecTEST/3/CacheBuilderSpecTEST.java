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
 spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5," +
 " keyStrength=weak, valueStrength=soft, recordStats=true," +
 " writeExpirationDuration=5m, writeExpirationTimeUnit=MINUTES, accessExpirationDuration=10s," +
 " accessExpirationTimeUnit=SECONDS, refreshDuration=1h, refreshTimeUnit=HOURS");
 }

 @Test
 public void testParseCorrectly() {
 assertEquals("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5," +
 " keyStrength=weak, valueStrength=soft, recordStats=true," +
 " writeExpirationDuration=300000, writeExpirationTimeUnit=MINUTES, accessExpirationDuration=10000," +
 " accessExpirationTimeUnit=SECONDS, refreshDuration=3600000, refreshTimeUnit=HOURS", spec.toParsableString());
 }

 @Test(expected = IllegalStateException.class)
 public void testMissingKey() {
 try {
 CacheBuilderSpec.parse("missingKey=value");
 fail("Expected an exception");
 } catch (IllegalArgumentException e) {
 assertEquals("unknown key missingKey", e.getMessage());
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntegerValue() {
 try {
 CacheBuilderSpec.parse("invalidCapacity=-5");
 fail("Expected an exception");
 } catch (NumberFormatException e) {
 assertEquals("invalid integer for initialCapacity", e.getMessage());
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLongValue() {
 try {
 CacheBuilderSpec.parse("invalidMaximumSize=-5L");
 fail("Expected an exception");
 } catch (NumberFormatException e) {
 assertEquals("invalid long for maximumSize", e.getMessage());
 }
 }

 @Test(expected = AssertionError.class)
 public void testUnknownKeyStrength() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("keyStrength=unknown");
 fail("Expected an exception");
 } catch (AssertionError e) {
 // Expected
 }
 }

 @Test
 public void testParseDifferentKeys() {
 CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=20, maximumWeight=30," +
 " concurrencyLevel=5, keyStrength=weak, valueStrength=soft, recordStats=true," +
 " writeExpirationDuration=5m, writeExpirationTimeUnit=MINUTES, accessExpirationDuration=10s," +
 " accessExpirationTimeUnit=SECONDS, refreshDuration=1h, refreshTimeUnit=HOURS");
 CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20," +
 " concurrencyLevel=3, keyStrength=soft, valueStrength=weak, recordStats=false," +
 " writeExpirationDuration=3m, writeExpirationTimeUnit=MINUTES, accessExpirationDuration=5s," +
 " accessExpirationTimeUnit=SECONDS, refreshDuration=30m, refreshTimeUnit=MINUTES");
 assertNotEquals(spec1, spec2);
 }
}