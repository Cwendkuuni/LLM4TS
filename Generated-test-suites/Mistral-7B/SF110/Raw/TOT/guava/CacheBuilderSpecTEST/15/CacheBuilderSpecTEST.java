package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.AccessDurationParser;
import com.google.common.cache.CacheBuilderSpec.ConcurrencyLevelParser;
import com.google.common.cache.CacheBuilderSpec.InitialCapacityParser;
import com.google.common.cache.CacheBuilderSpec.KeyStrengthParser;
import com.google.common.cache.CacheBuilderSpec.MaximumSizeParser;
import com.google.common.cache.CacheBuilderSpec.MaximumWeightParser;
import com.google.common.cache.CacheBuilderSpec.RecordStatsParser;
import com.google.common.cache.CacheBuilderSpec.ValueStrengthParser;
import com.google.common.cache.CacheBuilderSpec.WriteDurationParser;
import com.google.common.cache.CacheBuilderSpec.DurationParser;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

 @Test
 public void testParseWithValidSpecification() {
 String cacheSpec = "initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h";
 CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheSpec);

 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(15, spec.maximumWeight);
 assertEquals(3, spec.concurrencyLevel);
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 assertTrue(spec.recordStats);
 assertEquals(1, spec.writeExpirationTimeUnit.toHours(spec.writeExpirationDuration));
 assertEquals(2, spec.writeExpirationTimeUnit.toDays(spec.writeExpirationDuration));
 assertEquals(3, spec.refreshTimeUnit.toHours(spec.refreshDuration));

 List<String> keys = Lists.newArrayList("key1", "key2", "key3");
 ImmutableList<Object> values = ImmutableList.of("value1", "value2", "value3");
 Map<String, Object> map = new LinkedHashMap<>();
 map.putAll(keys.stream().collect(Collectors.toMap(Function.identity(), index -> values.get(index))));

 CacheBuilder<String, Object> builder = spec.toCacheBuilder();
 Cache<String, Object> cache = builder.build();

 for (Map.Entry<String, Object> entry : map.entrySet()) {
 String key = entry.getKey();
 Object value = entry.getValue();
 cache.put(key, value);
 }

 for (Map.Entry<String, Object> entry : map.entrySet()) {
 String key = entry.getKey();
 Object value = entry.getValue();
 assertNotNull(cache.get(key));
 assertEquals(value, cache.get(key));
 }
 }

 @Test
 public void testParseWithInvalidSpecification() {
 String cacheSpec = "initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h, invalidKey=invalidValue";
 CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheSpec);

 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(15, spec.maximumWeight);
 assertEquals(3, spec.concurrencyLevel);
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 assertTrue(spec.recordStats);
 assertEquals(1, spec.writeExpirationTimeUnit.toHours(spec.writeExpirationDuration));
 assertEquals(2, spec.writeExpirationTimeUnit.toDays(spec.writeExpirationDuration));
 assertEquals(3, spec.refreshTimeUnit.toHours(spec.refreshDuration));

 List<String> keys = Lists.newArrayList("key1", "key2", "key3");
 ImmutableList<Object> values = ImmutableList.of("value1", "value2", "value3");
 Map<String, Object> map = new LinkedHashMap<>();
 map.putAll(keys.stream().collect(Collectors.toMap(Function.identity(), index -> values.get(index))));

 CacheBuilder<String, Object> builder = spec.toCacheBuilder();
 try {
 builder.build();
 fail("Expected exception: IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithInvalidDurationFormat() {
 String cacheSpec = "initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=invalidWriteDuration, refreshAfterWrite=3h";
 CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheSpec);

 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(15, spec.maximumWeight);
 assertEquals(3, spec.concurrencyLevel);
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 assertTrue(spec.recordStats);

 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testParseWithEmptySpecification() {
 String cacheSpec = "";
 CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheSpec);

 assertNull(spec.initialCapacity);
 assertNull(spec.maximumSize);
 assertNull(spec.maximumWeight);
 assertNull(spec.concurrencyLevel);
 assertNull(spec.keyStrength);
 assertNull(spec.valueStrength);
 assertFalse(spec.recordStats);
 assertNull(spec.writeExpirationDuration);
 assertNull(spec.writeExpirationTimeUnit);
 assertNull(spec.accessExpirationDuration);
 assertNull(spec.accessExpirationTimeUnit);
 assertNull(spec.refreshDuration);
 assertNull(spec.refreshTimeUnit);
 }

 @Test
 public void testToParsableString() {
 String cacheSpec = "initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h";
 CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheSpec);

 assertEquals(cacheSpec, spec.toParsableString());
 }

 @Test(expected = AssertionError.class)
 public void testEqualityWithDifferentObjects() {
 CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 assertNotEquals(spec1, spec2);
 }

 @Test
 public void testEqualityWithSameObjects() {
 CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 CacheBuilderSpec spec2 = spec1;
 assertEquals(spec1, spec2);
 }

 @Test
 public void testEqualityWithNullObject() {
 CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 assertNull(spec1);
 }

 @Test
 public void testHashCode() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=15, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 long expectedHashCode = Objects.hash(5, 10, 15, 3, LocalCache.Strength.WEAK, LocalCache.Strength.SOFT, true, 2L, TimeUnit.DAYS, 1L, TimeUnit.HOURS, 3L, TimeUnit.HOURS);
 assertEquals(expectedHashCode, spec.hashCode());
 }
}