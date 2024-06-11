package com.example.junit;

import com.google.common.cache.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec cacheBuilderSpec;

 @Before
 public void setUp() {
 cacheBuilderSpec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, expireAfterAccess=1h, expireAfterWrite=30s, refreshAfterWrite=1m");
 }

 @Test
 public void testParse() {
 assertEquals("initialCapacity=5, maximumSize=10, concurrencyLevel=3, weakKeys, expireAfterAccess=1h, expireAfterWrite=30s, refreshAfterWrite=1m", cacheBuilderSpec.toParsableString());
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
 assertEquals(5, cacheBuilder.initialCapacity());
 assertEquals(10, cacheBuilder.maximumSize());
 assertEquals(3, cacheBuilder.concurrencyLevel());
 assertFalse(cacheBuilder.weakKeys());
 assertFalse(cacheBuilder.weakValues());
 assertFalse(cacheBuilder.softValues());
 assertTrue(cacheBuilder.recordStats());
 assertEquals(1, cacheBuilder.expireAfterAccessInSeconds());
 assertEquals(30, cacheBuilder.expireAfterWriteInMillis());
 assertEquals(1, cacheBuilder.refreshAfterWriteInMillis());
 }

 @Test
 public void testInvalidSpecification() {
 assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("invalidKey=value"));
 }

 @Test
 public void testMissingValue() {
 assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("maximumSize="));
 }

 @Test
 public void testInvalidValue() {
 assertThrows(NumberFormatException.class, () -> CacheBuilderSpec.parse("maximumSize=invalidValue"));
 }

 @Test
 public void testUnknownKey() {
 assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("unknownKey=value"));
 }
}