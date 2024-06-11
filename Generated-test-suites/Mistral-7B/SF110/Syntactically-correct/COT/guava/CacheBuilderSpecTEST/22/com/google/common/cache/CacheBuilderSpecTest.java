package com.example.tests;

import static org.junit.Assert.*;

import com.google.common.cache.*;
import com.google.common.collect.*;
import com.google.common.base.*;
import org.junit.*;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec cacheSpec;
 private CacheBuilder<Object, Object> cacheBuilder;

 @Before
 public void setUp() {
 cacheSpec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 cacheBuilder = cacheSpec.toCacheBuilder();
 }

 @Test
 public void testParse() {
 CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 assertEquals(cacheSpec, parsedSpec);
 }

 @Test
 public void testToCacheBuilder_initialCapacity() {
 assertEquals(5, cacheBuilder.initialCapacity());
 }

 @Test
 public void testToCacheBuilder_maximumSize() {
 assertEquals(10, cacheBuilder.maximumSize());
 }

 @Test
 public void testToCacheBuilder_maximumWeight() {
 assertEquals(20, cacheBuilder.maximumWeight());
 }

 @Test
 public void testToCacheBuilder_concurrencyLevel() {
 assertEquals(3, cacheBuilder.concurrencyLevel());
 }

 @Test
 public void testToCacheBuilder_weakKeys() {
 assertEquals(LocalCache.Strength.WEAK, cacheBuilder.strength());
 }

 @Test
 public void testToCacheBuilder_softValues() {
 assertEquals(LocalCache.Strength.SOFT, cacheBuilder.weakValues());
 }

 @Test
 public void testToCacheBuilder_recordStats() {
 assertTrue(cacheBuilder.recordStats());
 }

 @Test
 public void testToCacheBuilder_accessExpiration() {
 assertEquals(1 * 60 * 60 * 1000, cacheBuilder.expireAfterAccess(TimeUnit.HOURS).toMillis());
 }

 @Test
 public void testToCacheBuilder_writeExpiration() {
 assertEquals(2 * 24 * 60 * 60 * 1000, cacheBuilder.expireAfterWrite(2, TimeUnit.DAYS).toMillis());
 }

 @Test
 public void testToCacheBuilder_refreshExpiration() {
 assertEquals(3 * 60 * 60 * 1000, cacheBuilder.refreshAfterWrite(3, TimeUnit.HOURS).toMillis());
 }

 @Test(expected = AssertionError.class)
 public void testUnsupportedKeyStrength() {
 CacheBuilderSpec unsupportedCacheSpec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, unknownKey=value");
 }

 @Test(expected = AssertionError.class)
 public void testUnsupportedValueStrength() {
 CacheBuilderSpec unsupportedCacheSpec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, keyStrength=unknownValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDurationFormat() {
 CacheBuilderSpec invalidSpec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, expireAfterAccess=abcd");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntegerValue() {
 CacheBuilderSpec invalidSpec = new CacheBuilderSpec("initialCapacity=a, maximumSize=10, maximumWeight=20, concurrencyLevel=3, expireAfterAccess=1h");
 }

 @Test(expected = NullPointerException.class)
 public void testNullSpec() {
 CacheBuilderSpec nullSpec = null;
 nullSpec.toCacheBuilder();
 }
}