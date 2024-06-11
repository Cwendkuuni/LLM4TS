package com.google.common.cache;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;

public class CacheBuilderSpecTest {

 @Test
 public void testParse() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");

 assertEquals(5, spec.getInitialCapacity());
 assertEquals(10, spec.getMaximumSize());
 assertEquals(15, spec.getMaximumWeight());
 assertEquals(10, spec.getConcurrencyLevel());
 assertTrue(spec.getKeyStrength() == LocalCache.Strength.WEAK);
 assertTrue(spec.getValueStrength() == LocalCache.Strength.SOFT);
 assertTrue(spec.isRecordStats());
 assertEquals(1, spec.getAccessExpirationDuration());
 assertEquals(TimeUnit.DAYS, spec.getAccessExpirationTimeUnit());
 assertEquals(2, spec.getWriteExpirationDuration());
 assertEquals(TimeUnit.HOURS, spec.getWriteExpirationTimeUnit());
 assertEquals(3, spec.getRefreshDuration());
 assertEquals(TimeUnit.MINUTES, spec.getRefreshTimeUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m, invalidKey=invalidValue");
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testToParsableString() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 assertEquals("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m", spec.toParsableString());
 }

 @Test
 public void testToString() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 assertEquals("CacheBuilderSpec{specification='initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=0, writeExpirationTimeUnit=null, accessExpirationDuration=0, accessExpirationTimeUnit=null, refreshDuration=0, refreshTimeUnit=null, toParsableString=initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=0, writeExpirationTimeUnit=null, accessExpirationDuration=0, accessExpirationTimeUnit=null, refreshDuration=0, refreshTimeUnit=null'}", spec.toString());
 }

 @Test
 public void testHashCode() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=5, maximumSize=11, maximumWeight=15, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");

 assertEquals(spec1.hashCode(), spec2.hashCode());
 assertNotEquals(spec1.hashCode(), spec3.hashCode());
 }

 @Test
 public void testEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 CacheBuilderSpec spec4 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=14, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");

 assertTrue(spec1.equals(spec2));
 assertTrue(spec1.equals(spec3));
 assertFalse(spec1.equals(spec4));
 }
}