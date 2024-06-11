package com.example.junit;

import static org.junit.Assert.*;
import com.google.common.cache.*;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setUp() {
 spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=1h, writeExpirationTimeUnit=HOURS, accessExpirationDuration=30s, accessExpirationTimeUnit=SECONDS, refreshDuration=1d, refreshTimeUnit=DAYS");
 }

 @Test
 public void testParse() {
 assertEquals("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=3600, writeExpirationTimeUnit=HOURS, accessExpirationDuration=30000, accessExpirationTimeUnit=SECONDS, refreshDuration=86400000, refreshTimeUnit=DAYS", spec.toParsableString());
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(5, cacheBuilder.initialCapacity());
 assertEquals(10, cacheBuilder.maximumSize());
 assertEquals(20, cacheBuilder.maximumWeight());
 assertEquals(5, cacheBuilder.concurrencyLevel());
 cacheBuilder.weakKeys();
 cacheBuilder.softValues();
 assertTrue(cacheBuilder.recordStats());
 assertEquals(1, cacheBuilder.expireAfterWrite(1, TimeUnit.HOURS).getExpirationTimeUnit().toHours(1));
 assertEquals(30, cacheBuilder.expireAfterAccess(30, TimeUnit.SECONDS).getExpirationTimeUnit().toSeconds(1));
 assertEquals(1, cacheBuilder.refreshAfterWrite(1, TimeUnit.DAYS).getExpirationTimeUnit().toDays(1));
 }

 @Test(expected = AssertionError.class)
 public void testUnknownKey() {
 CacheBuilderSpec unknownSpec = CacheBuilderSpec.parse("unknownKey=10");
 fail("Should throw AssertionError");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntegerValue() {
 CacheBuilderSpec invalidSpec = CacheBuilderSpec.parse("initialCapacity=invalid");
 fail("Should throw NumberFormatException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDurationFormat() {
 CacheBuilderSpec durationSpec = CacheBuilderSpec.parse("accessExpirationDuration=1sday");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testEqualSpecifications() {
 CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=1h, writeExpirationTimeUnit=HOURS, accessExpirationDuration=30s, accessExpirationTimeUnit=SECONDS, refreshDuration=1d, refreshTimeUnit=DAYS");
 CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=3600, writeExpirationTimeUnit=HOURS, accessExpirationDuration=30000, accessExpirationTimeUnit=SECONDS, refreshDuration=86400000, refreshTimeUnit=DAYS");
 assertEquals(spec1, spec2);
 }

 @Test
 public void testNotEqualSpecifications() {
 CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=1h, writeExpirationTimeUnit=HOURS, accessExpirationDuration=30s, accessExpirationTimeUnit=SECONDS, refreshDuration=1d, refreshTimeUnit=DAYS");
 CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=false, writeExpirationDuration=1h, writeExpirationTimeUnit=HOURS, accessExpirationDuration=30s, accessExpirationTimeUnit=SECONDS, refreshDuration=1d, refreshTimeUnit=DAYS");
 assertNotEquals(spec1, spec2);
 }
}