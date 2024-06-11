package com.google.common.cache;

import static org.junit.Assert.*;
import static com.google.common.cache.CacheBuilderSpec.*;

import java.util.concurrent.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

 @Test
 public void testParseValidSpec() {
 CacheBuilderSpec spec = parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3m");
 assertEquals("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3m", spec.toParsableString());
 }

 @Test
 public void testParseEmptySpec() {
 CacheBuilderSpec spec = parse("");
 assertNull(spec);
 }

 @Test
 public void testParseInvalidSpec() {
 try {
 CacheBuilderSpec spec = parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3m, invalidKey=");
 fail("Expected IllegalArgumentException");
 }
 catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testDisableCaching() {
 CacheBuilderSpec spec = disableCaching();
 assertEquals("maximumSize=0", spec.toParsableString());
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3m");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(10, cacheBuilder.initialCapacity());
 assertEquals(20, cacheBuilder.maximumSize());
 assertEquals(30, cacheBuilder.maximumWeight());
 assertEquals(5, cacheBuilder.concurrencyLevel());
 assertTrue(cacheBuilder.weakKeys());
 assertTrue(cacheBuilder.softValues());
 assertTrue(cacheBuilder.recordStats());
 assertEquals(1, cacheBuilder.expireAfterAccessInSeconds());
 assertEquals(2, cacheBuilder.expireAfterWrite(TimeUnit.DAYS).toSeconds());
 assertEquals(3, cacheBuilder.refreshAfterWrite(TimeUnit.MINUTES).toSeconds());
 }

 @Test
 public void testEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3m");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3m");
 assertEquals(spec1, spec2);
 }

 @Test
 public void testNotEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3m");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=4m");
 assertNotEquals(spec1, spec2);
 }

 @Test
 public void testHashCode() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3m");
 int hashCode1 = spec.hashCode();
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3m");
 int hashCode2 = spec2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }
}