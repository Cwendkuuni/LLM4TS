package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.annotations.Beta;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.LocalCache;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.*;
import org.junit.Test;

@Beta
public class CacheBuilderSpecTest {

 private static final String DEFAULT_SPEC = "initialCapacity=10, maximumSize=100, concurrencyLevel=16, weakKeys(), softValues(), recordStats(), expireAfterAccess=5m, expireAfterWrite=10m, refreshAfterWrite=1h";
 private static final String NO_ACCESS_EXPIRATION = "initialCapacity=10, maximumSize=100, concurrencyLevel=16, weakKeys(), softValues(), recordStats(), expireAfterAccess=0, expireAfterWrite=10m, refreshAfterWrite=1h";
 private static final String NO_WRITE_EXPIRATION = "initialCapacity=10, maximumSize=100, concurrencyLevel=16, weakKeys(), softValues(), recordStats(), expireAfterAccess=5m, expireAfterWrite=0, refreshAfterWrite=1h";
 private static final String MAX_SIZE_ZERO = "initialCapacity=10, maximumSize=0, concurrencyLevel=16, weakKeys(), softValues(), recordStats(), expireAfterAccess=5m, expireAfterWrite=10m, refreshAfterWrite=1h";
 private static final String MAX_WEIGHT_ZERO = "initialCapacity=10, maximumSize=100, concurrencyLevel=16, weakKeys(), softValues(), recordStats(), maximumWeight=0, expireAfterAccess=5m, expireAfterWrite=10m, refreshAfterWrite=1h";

 @Test
 public void testParse() {
 CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(DEFAULT_SPEC);
 assertEquals(DEFAULT_SPEC, cacheBuilderSpec.toParsableString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecification() {
 try {
 CacheBuilderSpec.parse("invalidSpecification");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("invalidSpecification", e.getMessage());
 }
 }

 @Test
 public void testParseWithEmptySpecification() {
 try {
 CacheBuilderSpec.parse("");
 fail();
 } catch (IllegalArgumentException e) {
 // Empty string is an invalid cache builder specification
 }
 }

 @Test
 public void testParseWithNullSpecification() {
 try {
 CacheBuilderSpec.parse(null);
 fail();
 } catch (NullPointerException e) {
 // Null is not a valid cache builder specification
 }
 }

 @Test
 public void testParseWithMaxSizeZero() {
 CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(MAX_SIZE_ZERO);
 assertEquals(MAX_SIZE_ZERO, cacheBuilderSpec.toParsableString());
 assertNull(cacheBuilderSpec.maximumSize);
 }

 @Test
 public void testParseWithMaxWeightZero() {
 CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(MAX_WEIGHT_ZERO);
 assertEquals(MAX_WEIGHT_ZERO, cacheBuilderSpec.toParsableString());
 assertNull(cacheBuilderSpec.maximumWeight);
 }

 @Test
 public void testParseWithNoAccessExpiration() {
 CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(NO_ACCESS_EXPIRATION);
 assertEquals(NO_ACCESS_EXPIRATION, cacheBuilderSpec.toParsableString());
 assertNull(cacheBuilderSpec.accessExpirationDuration);
 assertNull(cacheBuilderSpec.accessExpirationTimeUnit);
 }

 @Test
 public void testParseWithNoWriteExpiration() {
 CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(NO_WRITE_EXPIRATION);
 assertEquals(NO_WRITE_EXPIRATION, cacheBuilderSpec.toParsableString());
 assertNull(cacheBuilderSpec.writeExpirationDuration);
 assertNull(cacheBuilderSpec.writeExpirationTimeUnit);
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(DEFAULT_SPEC);
 CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
 assertEquals(10, cacheBuilder.initialCapacity());
 assertEquals(100, cacheBuilder.maximumSize());
 assertEquals(16, cacheBuilder.concurrencyLevel());
 assertEquals(LocalCache.Strength.WEAK, cacheBuilder.weakKeys());
 assertEquals(LocalCache.Strength.SOFT, cacheBuilder.softValues());
 assertTrue(cacheBuilder.recordStats());
 assertEquals(5, cacheBuilder.expireAfterAccess(TimeUnit.MINUTES));
 assertEquals(10, cacheBuilder.expireAfterWrite(TimeUnit.MINUTES));
 assertEquals(1, cacheBuilder.refreshAfterWrite(TimeUnit.HOURS));
 }

 @Test
 public void testToCacheBuilderWithInvalidSpecification() {
 try {
 CacheBuilderSpec.parse("invalidSpecification").toCacheBuilder();
 fail();
 } catch (IllegalArgumentException e) {
 // An invalid cache builder specification should not be able to create a CacheBuilder instance
 }
 }

 @Test
 public void testToParsableString() {
 CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(DEFAULT_SPEC);
 assertEquals(DEFAULT_SPEC, cacheBuilderSpec.toParsableString());
 }

 @Test
 public void testToString() {
 CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(DEFAULT_SPEC);
 assertEquals(DEFAULT_SPEC, cacheBuilderSpec.toString());
 }

 @Test
 public void testHashCode() {
 CacheBuilderSpec cacheBuilderSpec1 = CacheBuilderSpec.parse(DEFAULT_SPEC);
 CacheBuilderSpec cacheBuilderSpec2 = CacheBuilderSpec.parse(DEFAULT_SPEC);
 assertEquals(cacheBuilderSpec1.hashCode(), cacheBuilderSpec2.hashCode());
 }

 @Test
 public void testEquals() {
 CacheBuilderSpec cacheBuilderSpec1 = CacheBuilderSpec.parse(DEFAULT_SPEC);
 CacheBuilderSpec cacheBuilderSpec2 = CacheBuilderSpec.parse(DEFAULT_SPEC);
 assertTrue(cacheBuilderSpec1.equals(cacheBuilderSpec2));
 }

 @Test
 public void testEqualsDifferentObjects() {
 CacheBuilderSpec cacheBuilderSpec1 = CacheBuilderSpec.parse(DEFAULT_SPEC);
 Object anotherObject = new Object();
 assertFalse(cacheBuilderSpec1.equals(anotherObject));
 }
}