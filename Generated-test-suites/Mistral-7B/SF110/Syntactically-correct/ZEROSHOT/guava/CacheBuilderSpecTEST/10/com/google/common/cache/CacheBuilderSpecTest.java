package com.google.common.cache;

import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.*;
import com.google.common.collect.*;
import com.google.common.base.*;
import javax.annotation.*;

public class CacheBuilderSpecTest {

 private static final String SPECIFICATION_WITH_VALID_KEY_VALUE_PAIRS = "initialCapacity=10, maximumSize=100, maximumWeight=50, concurrencyLevel=10, keyStrength=weak, valueStrength=soft, recordStats=true, writeExpirationDuration=5m, accessExpirationDuration=1h, refreshDuration=30s";
 private static final String SPECIFICATION_WITH_MISSING_VALUE = "initialCapacity=10, maximumSize=100, maximumWeight=50, concurrencyLevel=10, keyStrength=weak, valueStrength=soft, recordStats, writeExpirationDuration=5m, accessExpirationDuration=1h, refreshDuration=30s";
 private static final String SPECIFICATION_WITH_INVALID_KEY = "invalidKey=value";
 private static final String SPECIFICATION_WITH_INVALID_VALUE = "initialCapacity=10, maximumSize=100, maximumWeight=50, concurrencyLevel=10, keyStrength=weak, valueStrength=soft, recordStats=true, writeExpirationDuration=5m, accessExpirationDuration=1h, refreshDuration=30s, expireAfterWrite=-1s";
 private static final String SPECIFICATION_WITH_INVALID_TIME_UNIT = "initialCapacity=10, maximumSize=100, maximumWeight=50, concurrencyLevel=10, keyStrength=weak, valueStrength=soft, recordStats=true, writeExpirationDuration=5a, accessExpirationDuration=1h, refreshDuration=30s";
 private static final String SPECIFICATION_WITH_INVALID_DUPLICATE_KEY = "initialCapacity=10, maximumSize=100, maximumWeight=50, concurrencyLevel=10, keyStrength=weak, valueStrength=soft, recordStats=true, writeExpirationDuration=5m, accessExpirationDuration=1h, writeExpirationDuration=1m, refreshDuration=30s";

 @Test
 public void testParseValidSpecification() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(SPECIFICATION_WITH_VALID_KEY_VALUE_PAIRS);
 assertTrue(spec.initialCapacity != null);
 assertEquals(10, spec.initialCapacity);
 assertTrue(spec.maximumSize != null);
 assertEquals(100, spec.maximumSize);
 assertTrue(spec.maximumWeight != null);
 assertEquals(50, spec.maximumWeight);
 assertTrue(spec.concurrencyLevel != null);
 assertEquals(10, spec.concurrencyLevel);
 assertNotNull(spec.keyStrength);
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 assertNotNull(spec.valueStrength);
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 assertTrue(spec.recordStats != null);
 assertEquals(true, spec.recordStats);
 assertNotNull(spec.writeExpirationTimeUnit);
 assertEquals(TimeUnit.MINUTES, spec.writeExpirationTimeUnit);
 assertEquals(5, spec.writeExpirationDuration);
 assertNotNull(spec.accessExpirationTimeUnit);
 assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
 assertEquals(1, spec.accessExpirationDuration);
 assertNotNull(spec.refreshTimeUnit);
 assertEquals(TimeUnit.SECONDS, spec.refreshTimeUnit);
 assertEquals(30, spec.refreshDuration);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingValue() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(SPECIFICATION_WITH_MISSING_VALUE);
 fail("An IllegalArgumentException should have been thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidKey() {
 CacheBuilderSpec spec = null;
 try {
 spec = CacheBuilderSpec.parse(SPECIFICATION_WITH_INVALID_KEY);
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(spec);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseInvalidValue() {
 CacheBuilderSpec spec = null;
 try {
 spec = CacheBuilderSpec.parse(SPECIFICATION_WITH_INVALID_VALUE);
 } catch (IllegalStateException e) {
 // expected, as parsing the invalid string will throw a NumberFormatException before reaching the ValueParser
 }
 assertNull(spec);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidTimeUnit() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(SPECIFICATION_WITH_INVALID_TIME_UNIT);
 fail("An IllegalArgumentException should have been thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidDuplicateKey() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(SPECIFICATION_WITH_INVALID_DUPLICATE_KEY);
 fail("An IllegalArgumentException should have been thrown");
 }
}