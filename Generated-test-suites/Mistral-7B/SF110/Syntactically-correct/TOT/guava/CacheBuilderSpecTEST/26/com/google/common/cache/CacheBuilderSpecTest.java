package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setup() {
 spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterWrite=1h, expireAfterAccess=2d, refreshAfterWrite=3m");
 }

 @Test
 public void testParse() {
 CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(spec.toParsableString());
 assertEquals(spec.toParsableString(), parsedSpec.toParsableString());
 }

 @Test
 public void testParseInvalidSpecification() {
 assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("invalidSpecification"));
 }

 @Test
 public void testParseMissingKey() {
 assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("concurrencyLevel=5"));
 }

 @Test
 public void testParseInvalidValue() {
 assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("initialCapacity=five"));
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
 cacheBuilder.recordStats();
 assertNull(cacheBuilder.expireAfterWrite());
 assertNull(cacheBuilder.expireAfterAccess());
 assertNull(cacheBuilder.refreshAfterWrite());
 }

 @Test
 public void testToParsableString() {
 assertEquals("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterWrite=1h, expireAfterAccess=2d, refreshAfterWrite=3m", spec.toParsableString());
 }

 @Test
 public void testEquals() {
 CacheBuilderSpec equalSpec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterWrite=1h, expireAfterAccess=2d, refreshAfterWrite=3m");
 CacheBuilderSpec notEqualSpec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterWrite=1h, expireAfterAccess=2d, refreshAfterWrite=4m");
 assertNotEquals(spec, notEqualSpec);
 }

 @Test
 public void testHashCode() {
 CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterWrite=1h, expireAfterAccess=2d, refreshAfterWrite=3m");
 CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterWrite=1h, expireAfterAccess=2d, refreshAfterWrite=3m");
 assertEquals(spec1.hashCode(), spec2.hashCode());
 }

 @Test
 public void testToString() {
 assertEquals("CacheBuilderSpec{initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=3600000, writeExpirationTimeUnit=HOURS, accessExpirationDuration=259200000, accessExpirationTimeUnit=DAYS, refreshDuration=180000, refreshTimeUnit=MINUTES}", spec.toString());
 }
}