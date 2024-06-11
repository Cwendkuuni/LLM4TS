package com.example.junit4;

import static org.junit.Assert.*;
import com.google.common.cache.*;

import java.util.*;
import java.util.concurrent.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

 private static final String VALID_SPEC = "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=2, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h";
 private static final String INVALID_SPEC = "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=2, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h, invalidKey=invalidValue";
 private static final String INVALID_SPEC_MISSING_VALUE = "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=2, weakKeys, softValues, recordStats, expireAfterAccess=1h";
 private static final String EMPTY_SPEC = "";
 private static final String MISSING_KEY_SPEC = "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=2, ";

 @Test
 public void testParseValidSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(VALID_SPEC);
 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(20, spec.maximumWeight);
 assertEquals(2, spec.concurrencyLevel);
 assertTrue(spec.keyStrength == LocalCache.Strength.WEAK);
 assertTrue(spec.valueStrength == LocalCache.Strength.SOFT);
 assertTrue(spec.recordStats);
 assertEquals(1, TimeUnit.HOURS);
 assertEquals(2, TimeUnit.DAYS);
 assertEquals(3, TimeUnit.HOURS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpec() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(INVALID_SPEC);
 } catch (IllegalArgumentException e) {
 assertEquals("key invalidKey does not take values", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingValueSpec() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(INVALID_SPEC_MISSING_VALUE);
 } catch (IllegalArgumentException e) {
 assertEquals("key expireAfterWrite does not take values", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseEmptySpec() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(EMPTY_SPEC);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingKeySpec() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(MISSING_KEY_SPEC);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(VALID_SPEC);
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 // perform assertions on cacheBuilder if needed
 }
}