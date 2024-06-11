package com.google.common.cache;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setUp() {
 spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, concurrencyLevel=5, keyStrength=weak, valueStrength=weak, recordStats=true, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m");
 }

 @Test
 public void testParse() {
 CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(spec.toParsableString());
 assertEquals(spec, parsedSpec);
 }

 @Test
 public void testParseInvalidSpecification() {
 assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("invalid=specification"));
 }

 @Test
 public void testParseMissingKey() {
 assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("initialCapacity=5"));
 }

 @Test
 public void testParseMissingValue() {
 assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("keyStrength=weak"));
 }

 @Test
 public void testParseInvalidValue() {
 assertThrows(NumberFormatException.class, () -> CacheBuilderSpec.parse("maximumSize=-10"));
 }

 @Test
 public void testParseInvalidTimeUnit() {
 assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse("expireAfterAccess=1w"));
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(5, cacheBuilder.initialCapacity());
 assertEquals(10, cacheBuilder.maximumSize());
 assertEquals(5, cacheBuilder.concurrencyLevel());
 assertTrue(cacheBuilder.recordStats());
 assertEquals(CacheBuilder.ExpirationPolicy.ACCESS, cacheBuilder.expireAfterAccess());
 assertEquals(CacheBuilder.ExpirationPolicy.ACCESS, cacheBuilder.expireAfterWrite());
 assertEquals(CacheBuilder.ExpirationPolicy.ACCESS, cacheBuilder.refreshAfterWrite());
 }

 @Test
 public void testToParsableString() {
 assertEquals(spec.toParsableString(), "initialCapacity=5, maximumSize=10, concurrencyLevel=5, keyStrength=weak, valueStrength=weak, recordStats=true, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m");
 }

 @Test
 public void testHashCodeAndEquals() {
 CacheBuilderSpec anotherSpec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, concurrencyLevel=5, keyStrength=weak, valueStrength=weak, recordStats=true, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m");
 assertEquals(spec, anotherSpec);
 }

 @Test
 public void testHashCodeAndEqualsDifferentValues() {
 CacheBuilderSpec anotherSpec = new CacheBuilderSpec("initialCapacity=5, maximumSize=11, concurrencyLevel=5, keyStrength=weak, valueStrength=weak, recordStats=true, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m");
 assertNotEquals(spec, anotherSpec);
 }

 @Test
 public void testHashCodeAndEqualsSameObject() {
 CacheBuilderSpec specInstance = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, concurrencyLevel=5, keyStrength=weak, valueStrength=weak, recordStats=true, expireAfterAccess=5m, expireAfterWrite=1h, refreshAfterWrite=15m");
 assertEquals(specInstance, specInstance);
 }
}