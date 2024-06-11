package com.example.cachebuilder; // replace with your test package name

import static org.junit.Assert.*;
import com.google.common.cache.*;
import com.google.common.cache.CacheBuilderSpec.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Test
 public void testParseEmptySpec() {
 spec = CacheBuilderSpec.parse("");
 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertNull(spec.recordStats);
 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParseValidSpec() {
 spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,"
         + "weakKeys(),softValues(),recordStats(),expireAfterWrite=5d,expireAfterAccess=1h,refreshAfterWrite=1m");
 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(20, spec.maximumWeight);
 assertEquals(CacheBuilder.WeakKeys, spec.keyStrength);
 assertEquals(CacheBuilder.SoftValues, spec.valueStrength);
 assertTrue(spec.recordStats);
 assertEquals(5L, spec.writeExpirationDuration);
 assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
 assertEquals(1L, spec.accessExpirationDuration);
 assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
 assertEquals(1L, spec.refreshDuration);
 assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidFormat() {
 spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,"
         + "weakKeys(),softValues(),recordStats(),expireAfterWrite=5d,expireAfterAccess=1h,refreshAfterWrite=1m,"
         + "invalidKey=value");
 fail(); // the test should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidIntegerValue() {
 spec = CacheBuilderSpec.parse("initialCapacity=invalid");
 fail(); // the test should throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidDurationFormat() {
 spec = CacheBuilderSpec.parse("expireAfterWrite=invalidFormat");
 fail(); // the test should throw an exception
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 // add assertions for cacheBuilder properties based on your requirements
 }

 @Test
 public void testToString() {
 String specString = spec.toParsableString();
 String expected = "initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=5,"
         + "weakKeys(),softValues(),recordStats(),expireAfterWrite=5d,expireAfterAccess=1h,refreshAfterWrite=1m";
 assertEquals(expected, specString);
 }

 @Test
 public void testHashCodeAndEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=20,"
         + "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=5d,expireAfterAccess=1h,"
         + "refreshAfterWrite=1m");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=20,"
         + "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=5d,expireAfterAccess=1h,"
         + "refreshAfterWrite=1m");
 CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=20,"
         + "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=5d,expireAfterAccess=1h,"
         + "refreshAfterWrite=2m");
 assertEquals(spec1, spec2);
 assertNotEquals(spec1, spec3);
 }
}