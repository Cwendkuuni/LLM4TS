package com.google.common.cache;

import com.google.common.collect.ImmutableMap;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import com.google.common.base.Preconditions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class CacheBuilderSpecTest {

 private static final String VALID_SPEC = "initialCapacity=5, maximumSize=10, maximumWeight=50, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterWrite=1h, expireAfterAccess=2m, refreshAfterWrite=3s";
 private static final String INVALID_SPEC = "initialCapacity=5, maximumSize=10, maximumWeight=50, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterWrite=1h, expireAfterWrite=2s, refreshAfterWrite=3s"; // duplicate key
 private static final String MISSING_KEY_SPEC = "initialCapacity=5, maximumSize=10, maximumWeight=50, concurrencyLevel=10, expireAfterWrite=1h, expireAfterAccess=2m"; // missing key
 private static final String INVALID_VALUE_SPEC = "initialCapacity=5, maximumSize=10, maximumWeight=50, concurrencyLevel=10, weakKeys=invalidValue, expireAfterWrite=1h"; // invalid value for key "weakKeys"
 private static final String NEGATIVE_VALUE_SPEC = "initialCapacity=-1, maximumSize=-1, maximumWeight=-1, concurrencyLevel=-1, weakKeys"; // negative values

 @Test
 public void testValidSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(VALID_SPEC);
 assertNotNull(spec);
 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(50, spec.maximumWeight);
 assertEquals(10, spec.concurrencyLevel);
 assertNotNull(spec.keyStrength);
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 assertNotNull(spec.valueStrength);
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 assertNotNull(spec.recordStats);
 assertEquals(1, spec.writeExpirationTimeUnit.toHours(spec.writeExpirationDuration));
 assertEquals(2, spec.accessExpirationTimeUnit.toMinutes(spec.accessExpirationDuration));
 assertEquals(3, spec.refreshTimeUnit.toSeconds(spec.refreshDuration));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSpec() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(INVALID_SPEC);
 } catch (IllegalArgumentException e) {
 assertEquals("key expireAfterWrite already set", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingKeySpec() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(MISSING_KEY_SPEC);
 } catch (IllegalArgumentException e) {
 assertEquals("unknown key", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValueSpec() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(INVALID_VALUE_SPEC);
 } catch (IllegalArgumentException e) {
 assertEquals("invalid value for key weakKeys", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeValueSpec() {
 try {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(NEGATIVE_VALUE_SPEC);
 } catch (IllegalArgumentException e) {
 assertEquals("value cannot be negative", e.getMessage());
 }
 }
}