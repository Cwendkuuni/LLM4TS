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
 public void setup() {
 spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5," +
 " keyStrength=STRONG, valueStrength=STRONG, recordStats=true," +
 " writeExpirationDuration=30s, writeExpirationTimeUnit=SECONDS," +
 " accessExpirationDuration=1h, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=2d, refreshTimeUnit=DAYS");
 }

 @Test
 public void testParse() {
 CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=20, maximumWeight=30," +
 " concurrencyLevel=5, keyStrength=STRONG, valueStrength=STRONG, recordStats=true," +
 " writeExpirationDuration=30s, writeExpirationTimeUnit=SECONDS," +
 " accessExpirationDuration=1h, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=2d, refreshTimeUnit=DAYS");

 assertEquals(spec.initialCapacity, parsedSpec.initialCapacity);
 assertEquals(spec.maximumSize, parsedSpec.maximumSize);
 assertEquals(spec.maximumWeight, parsedSpec.maximumWeight);
 assertEquals(spec.concurrencyLevel, parsedSpec.concurrencyLevel);
 assertEquals(spec.keyStrength, parsedSpec.keyStrength);
 assertEquals(spec.valueStrength, parsedSpec.valueStrength);
 assertEquals(spec.recordStats, parsedSpec.recordStats);
 assertEquals(spec.writeExpirationDuration, parsedSpec.writeExpirationDuration);
 assertEquals(spec.writeExpirationTimeUnit, parsedSpec.writeExpirationTimeUnit);
 assertEquals(spec.accessExpirationDuration, parsedSpec.accessExpirationDuration);
 assertEquals(spec.accessExpirationTimeUnit, parsedSpec.accessExpirationTimeUnit);
 assertEquals(spec.refreshDuration, parsedSpec.refreshDuration);
 assertEquals(spec.refreshTimeUnit, parsedSpec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownKey() {
 try {
 CacheBuilderSpec.parse("unknownKey=value");
 fail("Should throw IllegalArgumentException for unknown key");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValue() {
 try {
 CacheBuilderSpec.parse("maximumSize=invalidValue");
 fail("Should throw IllegalArgumentException for invalid value");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = AssertionError.class)
 public void testUnsupportedKeyStrength() {
 try {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, maximumWeight=30," +
 " concurrencyLevel=5, keyStrength=UNSUPPORTED, valueStrength=STRONG, recordStats=true," +
 " writeExpirationDuration=30s, writeExpirationTimeUnit=SECONDS," +
 " accessExpirationDuration=1h, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=2d, refreshTimeUnit=DAYS");
 fail("Should throw AssertionError for unsupported keyStrength");
 } catch (AssertionError e) {
 // Expected exception
 }
 }

 @Test(expected = AssertionError.class)
 public void testUnsupportedValueStrength() {
 try {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, maximumWeight=30," +
 " concurrencyLevel=5, keyStrength=STRONG, valueStrength=UNSUPPORTED, recordStats=true," +
 " writeExpirationDuration=30s, writeExpirationTimeUnit=SECONDS," +
 " accessExpirationDuration=1h, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=2d, refreshTimeUnit=DAYS");
 fail("Should throw AssertionError for unsupported valueStrength");
 } catch (AssertionError e) {
 // Expected exception
 }
 }

 @Test
 public void testToParsableString() {
 assertEquals("initialCapacity=10, maximumSize=20, maximumWeight=30," +
 " concurrencyLevel=5, keyStrength=STRONG, valueStrength=STRONG," +
 " recordStats=true," +
 " writeExpirationDuration=30000, writeExpirationTimeUnit=SECONDS," +
 " accessExpirationDuration=3600000, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=2days, refreshTimeUnit=DAYS", spec.toParsableString());
 }

 @Test
 public void testToString() {
 assertEquals("CacheBuilderSpec{specification='initialCapacity=10, maximumSize=20, maximumWeight=30," +
 " concurrencyLevel=5, keyStrength=STRONG, valueStrength=STRONG, recordStats=true," +
 " writeExpirationDuration=30s, writeExpirationTimeUnit=SECONDS," +
 " accessExpirationDuration=1h, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=2d, refreshTimeUnit=DAYS'}", spec.toString());
 }

 @Test
 public void testHashCode() {
 long initialCapacity = 10;
 long maximumSize = 20;
 long maximumWeight = 30;
 int concurrencyLevel = 5;
 LocalCache.Strength keyStrength = LocalCache.Strength.STRONG;
 LocalCache.Strength valueStrength = LocalCache.Strength.STRONG;
 boolean recordStats = true;
 long writeExpirationDuration = 30000L;
 TimeUnit writeExpirationTimeUnit = TimeUnit.SECONDS;
 long accessExpirationDuration = 3600000L;
 TimeUnit accessExpirationTimeUnit = TimeUnit.HOURS;
 long refreshDuration = 2L * 24 * 60 * 60 * 1000L; // 2 days in milliseconds
 TimeUnit refreshTimeUnit = TimeUnit.DAYS;

 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=" + initialCapacity + ", maximumSize=" + maximumSize +
 ", maximumWeight=" + maximumWeight + ", concurrencyLevel=" + concurrencyLevel +
 ", keyStrength=" + keyStrength + ", valueStrength=" + valueStrength + ", recordStats=" + recordStats +
 ", writeExpirationDuration=" + writeExpirationDuration + ", writeExpirationTimeUnit=" + writeExpirationTimeUnit.name() +
 ", accessExpirationDuration=" + accessExpirationDuration + ", accessExpirationTimeUnit=" + accessExpirationTimeUnit.name() +
 ", refreshDuration=" + refreshDuration + ", refreshTimeUnit=" + refreshTimeUnit.name());

 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=" + initialCapacity + ", maximumSize=" + maximumSize +
 ", maximumWeight=" + maximumWeight + ", concurrencyLevel=" + concurrencyLevel +
 ", keyStrength=" + keyStrength + ", valueStrength=" + valueStrength + ", recordStats=" + recordStats +
 ", writeExpirationDuration=" + writeExpirationDuration + ", writeExpirationTimeUnit=" + writeExpirationTimeUnit.name() +
 ", accessExpirationDuration=" + accessExpirationDuration + ", accessExpirationTimeUnit=" + accessExpirationTimeUnit.name() +
 ", refreshDuration=" + refreshDuration + ", refreshTimeUnit=" + refreshTimeUnit.name());

 assertEquals(spec1.hashCode(), spec2.hashCode());
 }

 @Test
 public void testEquals() {
 long initialCapacity = 10;
 long maximumSize = 20;
 long maximumWeight = 30;
 int concurrencyLevel = 5;
 LocalCache.Strength keyStrength = LocalCache.Strength.STRONG;
 LocalCache.Strength valueStrength = LocalCache.Strength.STRONG;
 boolean recordStats = true;
 long writeExpirationDuration = 30000L;
 TimeUnit writeExpirationTimeUnit = TimeUnit.SECONDS;
 long accessExpirationDuration = 3600000L;
 TimeUnit accessExpirationTimeUnit = TimeUnit.HOURS;
 long refreshDuration = 2L * 24 * 60 * 60 * 1000L; // 2 days in milliseconds
 TimeUnit refreshTimeUnit = TimeUnit.DAYS;

 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=" + initialCapacity + ", maximumSize=" + maximumSize +
 ", maximumWeight=" + maximumWeight + ", concurrencyLevel=" + concurrencyLevel +
 ", keyStrength=" + keyStrength + ", valueStrength=" + valueStrength + ", recordStats=" + recordStats +
 ", writeExpirationDuration=" + writeExpirationDuration + ", writeExpirationTimeUnit=" + writeExpirationTimeUnit.name() +
 ", accessExpirationDuration=" + accessExpirationDuration + ", accessExpirationTimeUnit=" + accessExpirationTimeUnit.name() +
 ", refreshDuration=" + refreshDuration + ", refreshTimeUnit=" + refreshTimeUnit.name());

 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=" + initialCapacity + ", maximumSize=" + maximumSize +
 ", maximumWeight=" + maximumWeight + ", concurrencyLevel=" + concurrencyLevel +
 ", keyStrength=" + keyStrength + ", valueStrength=" + valueStrength + ", recordStats=" + recordStats +
 ", writeExpirationDuration=" + writeExpirationDuration + ", writeExpirationTimeUnit=" + writeExpirationTimeUnit.name() +
 ", accessExpirationDuration=" + accessExpirationDuration + ", accessExpirationTimeUnit=" + accessExpirationTimeUnit.name() +
 ", refreshDuration=" + refreshDuration + ", refreshTimeUnit=" + refreshTimeUnit.name());

 CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=" + initialCapacity + ", maximumSize=" + maximumSize +
 ", maximumWeight=" + maximumWeight + ", concurrencyLevel=" + concurrencyLevel +
 ", keyStrength=" + (LocalCache.Strength.values().length + 1) + ", valueStrength=" + valueStrength +
 ", recordStats=" + recordStats + ", writeExpirationDuration=" + writeExpirationDuration +
 ", writeExpirationTimeUnit=" + writeExpirationTimeUnit.name() + ", accessExpirationDuration=" + accessExpirationDuration +
 ", accessExpirationTimeUnit=" + accessExpirationTimeUnit.name() + ", refreshDuration=" + refreshDuration +
 ", refreshTimeUnit=" + refreshTimeUnit.name());

 assertTrue(spec1.equals(spec2));
 assertTrue(spec1.hashCode() == spec2.hashCode());
 assertFalse(spec1.equals(spec3));
 assertFalse(spec1.hashCode() == spec3.hashCode());
 }
}