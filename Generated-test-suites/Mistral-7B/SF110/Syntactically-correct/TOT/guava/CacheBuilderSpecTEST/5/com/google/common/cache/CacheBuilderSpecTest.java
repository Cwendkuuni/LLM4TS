package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setup() {
 spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=5, concurrencyLevel=3, recordStats=true," +
 "weakKeys, expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1s");
 }

 @Test
 public void testParse() {
 CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=5, concurrencyLevel=3," +
 "recordStats=true,weakKeys,expireAfterWrite=1h,expireAfterAccess=1m,refreshAfterWrite=1s");
 assertEquals(spec, parsedSpec);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownKey() {
 try {
 CacheBuilderSpec.parse("unknownKey=value");
 fail("should have thrown IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntegerValue() {
 try {
 CacheBuilderSpec.parse("initialCapacity=abcd");
 fail("should have thrown NumberFormatException");
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidLongValue() {
 try {
 CacheBuilderSpec.parse("maximumSize=-1");
 fail("should have thrown NumberFormatException");
 } catch (NumberFormatException e) {
 // expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testUnknownValueStrength() {
 try {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=5, concurrencyLevel=3," +
 "unknownValueStrength=true,weakKeys,expireAfterWrite=1h,expireAfterAccess=1m,refreshAfterWrite=1s");
 fail("should have thrown AssertionError");
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testToParsableString() {
 assertEquals("initialCapacity=10, maximumSize=5, concurrencyLevel=3, recordStats=true," +
 "weakKeys, expireAfterWrite=1h, expireAfterAccess=1m, refreshAfterWrite=1s", spec.toParsableString());
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(10, cacheBuilder.initialCapacity());
 assertEquals(5, cacheBuilder.maximumSize());
 assertEquals(3, cacheBuilder.concurrencyLevel());
 assertTrue(cacheBuilder.recordStats());
 assertEquals(Cache.Strength.WEAK, cacheBuilder.keyStrength());
 assertEquals(Cache.Strength.WEAK, cacheBuilder.valueStrength());
 assertEquals(1, cacheBuilder.expireAfterWriteInMillis());
 assertEquals(TimeUnit.HOURS, cacheBuilder.expireAfterWriteTimeUnit());
 assertEquals(1, cacheBuilder.expireAfterAccessInMillis());
 assertEquals(TimeUnit.MINUTES, cacheBuilder.expireAfterAccessTimeUnit());
 assertEquals(1, cacheBuilder.refreshAfterWriteInMillis());
 assertEquals(TimeUnit.SECONDS, cacheBuilder.refreshAfterWriteTimeUnit());
 }
}