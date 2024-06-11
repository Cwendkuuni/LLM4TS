package com.google.common.cache;

import com.google.common.cache.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

 @Test
 public void testParseValidSpecification() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, concurrencyLevel=5, weakKeys(), weakValues(), expireAfterWrite=1h, expireAfterAccess=30m, recordStats=true");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 Cache<Object, Object> cache = cacheBuilder.build();

 assertEquals(5, cacheBuilder.initialCapacity());
 assertEquals(10, cacheBuilder.maximumSize());
 assertEquals(5, cacheBuilder.concurrencyLevel());
 assertTrue(cacheBuilder.recordStats());
 assertEquals(Cache.ExpirePolicy.ACCESS, cache.expirePolicy());
 assertEquals(CacheExpiryPolicy.ACCESS, cacheBuilder.accessExpirationPolicy());
 assertEquals(CacheExpiryPolicy.WRITE, cacheBuilder.writeExpirationPolicy());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecification() {
 try {
 CacheBuilderSpec.parse("invalidKey=10");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("key invalidKey does not take values", e.getMessage());
 }
 }

 @Test
 public void testParseEmptySpecification() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("");
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
 public void testParseDisableCaching() {
 CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
 assertEquals(CacheBuilder.newBuilder(), spec.toCacheBuilder());
 }
}