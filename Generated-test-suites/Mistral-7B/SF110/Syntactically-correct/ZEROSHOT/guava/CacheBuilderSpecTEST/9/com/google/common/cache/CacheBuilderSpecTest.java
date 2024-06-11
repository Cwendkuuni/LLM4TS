package com.google.common.cache;

import com.google.common.annotations.*;
import org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.cache.CacheBuilderSpec.*;
import static com.google.common.cache.CacheBuilderSpec.KEYS_SPLITTER;
import static com.google.common.cache.CacheBuilderSpec.KEY_VALUE_SPLITTER;
import static com.google.common.cache.CacheBuilderSpec.VALUE_PARSERS;

@Beta
public class CacheBuilderSpecTest {

 @VisibleForTesting
 static CacheBuilderSpec parse(String cacheBuilderSpecification) {
 return CacheBuilderSpec.parse(cacheBuilderSpecification);
 }

 @Test
 public void testParse_EmptySpecification() {
 CacheBuilderSpec spec = parse("");
 assertTrue(spec.initialCapacity == null);
 assertTrue(spec.maximumSize == null);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 }

 @Test
 public void testParse_InitialCapacity() {
 CacheBuilderSpec spec = parse("initialCapacity=10");
 assertEquals(10, spec.initialCapacity.intValue());
 assertTrue(spec.maximumSize == null);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 }

 @Test
 public void testParse_MaximumSize() {
 CacheBuilderSpec spec = parse("maximumSize=50");
 assertEquals(50, spec.maximumSize.longValue());
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 }

 @Test
 public void testParse_MaximumWeight() {
 CacheBuilderSpec spec = parse("maximumWeight=100");
 assertEquals(100, spec.maximumWeight.longValue());
 assertEquals(null, spec.maximumSize);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 }

 @Test
 public void testParse_ConcurrencyLevel() {
 CacheBuilderSpec spec = parse("concurrencyLevel=15");
 assertEquals(15, spec.concurrencyLevel.intValue());
 assertTrue(spec.initialCapacity == null);
 assertTrue(spec.maximumSize == null);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 }

 @Test
 public void testParse_WeakKeys() {
 CacheBuilderSpec spec = parse("weakKeys");
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 assertTrue(spec.initialCapacity == null);
 assertTrue(spec.maximumSize == null);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 }

 @Test
 public void testParse_SoftValues() {
 CacheBuilderSpec spec = parse("softValues");
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 assertTrue(spec.initialCapacity == null);
 assertTrue(spec.maximumSize == null);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 }

 @Test
 public void testParse_WeakValues() {
 CacheBuilderSpec spec = parse("weakValues");
 assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
 assertTrue(spec.initialCapacity == null);
 assertTrue(spec.maximumSize == null);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 }

 @Test
 public void testParse_RecordStats() {
 CacheBuilderSpec spec = parse("recordStats=true");
 assertTrue(spec.recordStats);
 assertTrue(spec.initialCapacity == null);
 assertTrue(spec.maximumSize == null);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 }

 @Test
 public void testParse_WriteExpiration() {
 CacheBuilderSpec spec = parse("expireAfterWrite=1h");
 assertEquals(1 * 60 * 60 * 1000, spec.writeExpirationDuration);
 assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
 assertTrue(spec.initialCapacity == null);
 assertTrue(spec.maximumSize == null);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 }

 @Test
 public void testParse_AccessExpiration() {
 CacheBuilderSpec spec = parse("expireAfterAccess=30s");
 assertEquals(30 * 1000, spec.accessExpirationDuration);
 assertEquals(TimeUnit.SECONDS, spec.accessExpirationTimeUnit);
 assertTrue(spec.initialCapacity == null);
 assertTrue(spec.maximumSize == null);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.refreshDuration == 0);
 assertTrue(spec.refreshTimeUnit == null);
 }

 @Test
 public void testParse_RefreshAfterWrite() {
 CacheBuilderSpec spec = parse("refreshAfterWrite=5m");
 assertEquals(5 * 60 * 1000, spec.refreshDuration);
 assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
 assertTrue(spec.initialCapacity == null);
 assertTrue(spec.maximumSize == null);
 assertTrue(spec.maximumWeight == null);
 assertTrue(spec.concurrencyLevel == null);
 assertTrue(spec.keyStrength == null);
 assertTrue(spec.valueStrength == null);
 assertTrue(spec.recordStats == null);
 assertTrue(spec.writeExpirationDuration == 0);
 assertTrue(spec.writeExpirationTimeUnit == null);
 assertTrue(spec.accessExpirationDuration == 0);
 assertTrue(spec.accessExpirationTimeUnit == null);
 }

 @Test
 public void testParse_InvalidSpecification() {
 String specification = "maximumSize=50, initialCapacity=10";
 try {
 CacheBuilderSpec spec = parse(specification);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 assertTrue(VALUE_PARSERS.get("maximumSize") instanceof LongParser);
 assertTrue(VALUE_PARSERS.get("initialCapacity") instanceof IntegerParser);
 }

 @Test
 public void testParse_InvalidDurationFormat() {
 String specification = "expireAfterAccess=5d invalid";
 try {
 CacheBuilderSpec spec = parse(specification);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilderSpec spec = parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats=true, expireAfterWrite=5m, expireAfterAccess=1h, refreshAfterWrite=30s");

 CacheBuilder<Integer, String> builder = spec.toCacheBuilder();

 assertEquals(10, builder.initialCapacity());
 assertEquals(20, builder.maximumSize());
 assertEquals(30, builder.maximumWeight());
 assertEquals(5, builder.concurrencyLevel());
 assertEquals(LocalCache.Strength.WEAK, builder.weakKeys());
 assertEquals(LocalCache.Strength.SOFT, builder.softValues());
 assertTrue(builder.recordStats());
 assertEquals(5, TimeUnit.MINUTES.toSeconds(builder.expireAfterWrite()));
 assertEquals(1, TimeUnit.HOURS.toSeconds(builder.expireAfterAccess()));
 assertEquals(30, TimeUnit.SECONDS.toSeconds(builder.refreshAfterWrite()));
 }

 @Test
 public void testToParsableString() {
 CacheBuilderSpec spec = parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats=true, expireAfterWrite=5m, expireAfterAccess=1h, refreshAfterWrite=30s");
 String parsedSpec = spec.toParsableString();
 assertEquals("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats=true, expireAfterWrite=5m, expireAfterAccess=1h, refreshAfterWrite=30s", parsedSpec);
 }

 @Test
 public void testToString() {
 CacheBuilderSpec spec = parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats=true, expireAfterWrite=5m, expireAfterAccess=1h, refreshAfterWrite=30s");
 String stringRepresentation = spec.toString();
 assertEquals("CacheBuilderSpec{specification='initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=300000, writeExpirationTimeUnit=MINUTES, accessExpirationDuration=3600000, accessExpirationTimeUnit=HOURS, refreshDuration=1800000, refreshTimeUnit=MINUTES}'}", stringRepresentation);
 }

 @Test
 public void testHashCodeEquals() {
 CacheBuilderSpec spec1 = parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=300000, writeExpirationTimeUnit=MINUTES, accessExpirationDuration=3600000, accessExpirationTimeUnit=HOURS, refreshDuration=1800000, refreshTimeUnit=MINUTES");
 CacheBuilderSpec spec2 = parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=300000, writeExpirationTimeUnit=MINUTES, accessExpirationDuration=3600000, accessExpirationTimeUnit=HOURS, refreshDuration=1800000, refreshTimeUnit=MINUTES");
 assertEquals(spec1.hashCode(), spec2.hashCode());
 assertTrue(spec1.equals(spec2));
 }

 @Test
 public void testHashCodeEqualsDifferentSpecs() {
 CacheBuilderSpec spec1 = parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, keyStrength=WEAK, valueStrength=SOFT, recordStats=true, writeExpirationDuration=300000, writeExpirationTimeUnit=MINUTES, accessExpirationDuration=3600000, accessExpirationTimeUnit=HOURS, refreshDuration=1800000, refreshTimeUnit=MINUTES");
 CacheBuilderSpec spec2 = parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, keyStrength=STRONG, valueStrength=STRONG, recordStats=false, writeExpirationDuration=150000, writeExpirationTimeUnit=MINUTES, accessExpirationDuration=4200000, accessExpirationTimeUnit=HOURS, refreshDuration=1200000, refreshTimeUnit=MINUTES");
 assertFalse(spec1.equals(spec2));
 assertFalse(spec1.hashCode() == spec2.hashCode());
 }

}