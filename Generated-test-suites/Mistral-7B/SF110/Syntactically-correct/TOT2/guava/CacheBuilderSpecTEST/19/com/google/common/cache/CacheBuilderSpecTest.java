package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setup() {
 spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, " +
 "weakKeys, recordStats, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m");
 }

 @Test
 public void testParse() {
 CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(spec.toParsableString());
 assertEquals(spec.initialCapacity, parsedSpec.initialCapacity);
 assertEquals(spec.maximumSize, parsedSpec.maximumSize);
 assertEquals(spec.maximumWeight, parsedSpec.maximumWeight);
 assertEquals(spec.concurrencyLevel, parsedSpec.concurrencyLevel);
 assertEquals(spec.keyStrength, parsedSpec.keyStrength);
 assertEquals(spec.valueStrength, parsedSpec.valueStrength);
 assertEquals(spec.recordStats, parsedSpec.recordStats);
 assertEquals(spec.writeExpirationDuration, parsedSpec.writeExpirationDuration);
 assertEquals(spec.writeExpirationTimeUnit, parsedSpec.writeExpirationTimeUnit);
 assertEquals(spec.accessExpirationDuration, parsedSpec.accessExpirationDuration);
 assertEquals(spec.accessExpirationTimeUnit, parsedSpec.accessExpirationTimeUnit);
 assertEquals(spec.refreshDuration, parsedSpec.refreshDuration);
 assertEquals(spec.refreshTimeUnit, parsedSpec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCacheSpec() {
 CacheBuilderSpec.parse("invalidKey=invalidValue");
 }

 @Test
 public void testToParsableString() {
 assertEquals(spec.toParsableString(), "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, " +
 "weakKeys, recordStats, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m");
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(spec.initialCapacity, cacheBuilder.initialCapacity());
 assertEquals(spec.maximumSize, cacheBuilder.maximumSize());
 assertEquals(spec.maximumWeight, cacheBuilder.maximumWeight());
 assertEquals(spec.concurrencyLevel, cacheBuilder.concurrencyLevel());
 assertEquals(spec.keyStrength, cacheBuilder.weakKeys());
 assertEquals(spec.valueStrength, cacheBuilder.weakValues());
 assertEquals(spec.recordStats, cacheBuilder.recordStats());
 assertEquals(spec.writeExpirationDuration, cacheBuilder.expireAfterWrite(TimeUnit.MILLISECONDS).toMillis() / 1000L, 0.01d);
 assertEquals(spec.writeExpirationTimeUnit, cacheBuilder.expireAfterWrite(TimeUnit.MILLISECONDS).getUnit());
 assertEquals(spec.accessExpirationDuration, cacheBuilder.expireAfterAccess(TimeUnit.MILLISECONDS).toMillis() / 1000L, 0.01d);
 assertEquals(spec.accessExpirationTimeUnit, cacheBuilder.expireAfterAccess(TimeUnit.MILLISECONDS).getUnit());
 assertEquals(spec.refreshDuration, cacheBuilder.refreshAfterWrite(TimeUnit.MILLISECONDS).toMillis() / 1000L, 0.01d);
 assertEquals(spec.refreshTimeUnit, cacheBuilder.refreshAfterWrite(TimeUnit.MILLISECONDS).getUnit());
 }

 @Test
 public void testEquals() {
 CacheBuilderSpec equalSpec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, " +
 "weakKeys, recordStats, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m");
 CacheBuilderSpec anotherSpec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, " +
 "weakKeys, recordStats, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m");
 CacheBuilderSpec nullSpec = null;
 CacheBuilderSpec emptySpec = new CacheBuilderSpec("");

 assertEquals(spec, equalSpec);
 assertNotEquals(spec, anotherSpec);
 assertNull(spec.hashCode());
 assertNull(spec.equals(nullSpec));
 assertFalse(spec.equals(emptySpec));
 }
}