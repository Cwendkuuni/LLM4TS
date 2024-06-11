package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

 @Test
 public void testParseValidSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=50,concurrencyLevel=5," +
 "weakKeys(),softValues(),recordStats(),expireAfterWrite=1d,expireAfterAccess=2h,refreshAfterWrite=3m");

 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(50, spec.maximumWeight);
 assertEquals(5, spec.concurrencyLevel);
 assertTrue(spec.keyStrength == LocalCache.Strength.WEAK);
 assertTrue(spec.valueStrength == LocalCache.Strength.SOFT);
 assertTrue(spec.recordStats);
 assertEquals(1, spec.writeExpirationDuration);
 assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
 assertEquals(2, spec.accessExpirationDuration);
 assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
 assertEquals(3, spec.refreshDuration);
 assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpec() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidKey=value");
 fail("expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingValue() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=");
 fail("expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidIntegerValue() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=abc");
 fail("expected NumberFormatException");
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testParseUnknownStrength() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("unknownStrength=weakKeys");
 fail("expected AssertionError");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testParseDuplicateStrength() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues=weakKeys");
 fail("expected AssertionError");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testToParsableString() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=50," +
 "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1d," +
 "expireAfterAccess=2h,refreshAfterWrite=3m");
 assertEquals("initialCapacity=5,maximumSize=10,maximumWeight=50," +
 "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1d," +
 "expireAfterAccess=2h,refreshAfterWrite=3m", spec.toParsableString());
 }

 @Test
 public void testToParsableStringInvalidSpec() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=50," +
 "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1d," +
 "expireAfterAccess=2h,refreshAfterWrite=3m,unknownStrength=weakKeys");
 assertEquals("initialCapacity=5,maximumSize=10,maximumWeight=50," +
 "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1d," +
 "expireAfterAccess=2h", spec.toParsableString());
 }

 @Test
 public void testEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=50," +
 "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1d," +
 "expireAfterAccess=2h,refreshAfterWrite=3m");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=50," +
 "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1d," +
 "expireAfterAccess=2h,refreshAfterWrite=3m");
 CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=50," +
 "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1d," +
 "expireAfterAccess=2h,refreshAfterWrite=3m,unknownStrength=weakKeys");

 assertEquals(spec1, spec2);
 assertNotEquals(spec1, spec3);
 }

 @Test
 public void testHashCode() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=50," +
 "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1d," +
 "expireAfterAccess=2h,refreshAfterWrite=3m");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=50," +
 "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1d," +
 "expireAfterAccess=2h,refreshAfterWrite=3m");
 CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=50," +
 "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1d," +
 "expireAfterAccess=2h,refreshAfterWrite=3m,unknownStrength=weakKeys");

 assertEquals(spec1.hashCode(), spec2.hashCode());
 assertNotEquals(spec1.hashCode(), spec3.hashCode());
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5,maximumSize=10,maximumWeight=50," +
 "concurrencyLevel=5,weakKeys(),softValues(),recordStats(),expireAfterWrite=1d," +
 "expireAfterAccess=2h,refreshAfterWrite=3m");

 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

 assertEquals(5, cacheBuilder.initialCapacity());
 assertEquals(10, cacheBuilder.maximumSize());
 assertEquals(50, cacheBuilder.maximumWeight());
 assertEquals(5, cacheBuilder.concurrencyLevel());
 assertTrue(cacheBuilder.weakKeys());
 assertTrue(cacheBuilder.softValues());
 assertTrue(cacheBuilder.recordStats());
 assertEquals(1, cacheBuilder.expireAfterWrite(TimeUnit.DAYS));
 assertEquals(2, cacheBuilder.expireAfterAccess(TimeUnit.HOURS));
 assertEquals(3, cacheBuilder.refreshAfterWrite(TimeUnit.MINUTES));
 }

 @Test
 public void testDisableCaching() {
 CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();

 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

 assertEquals(0, cacheBuilder.maximumSize());
 }
}