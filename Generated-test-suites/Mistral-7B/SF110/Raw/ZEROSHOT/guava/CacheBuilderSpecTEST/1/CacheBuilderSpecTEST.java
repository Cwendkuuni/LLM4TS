package com.example.cache; // replace with your package name

import static org.junit.Assert.*;
import com.google.common.cache.*;
import org.junit.Test;

public class CacheBuilderSpecTest {

 private static final String VALID_SPEC_INITIAL_CAPACITY = "initialCapacity=10";
 private static final String VALID_SPEC_MAXIMUM_SIZE = "maximumSize=20";
 private static final String VALID_SPEC_MAXIMUM_WEIGHT = "maximumWeight=30";
 private static final String VALID_SPEC_CONCURRENCY_LEVEL = "concurrencyLevel=5";
 private static final String VALID_SPEC_KEY_STRENGTH = "weakKeys";
 private static final String VALID_SPEC_VALUE_STRENGTH = "weakValues";
 private static final String VALID_SPEC_RECORD_STATS = "recordStats=true";
 private static final String VALID_SPEC_WRITE_EXPIRATION = "expireAfterWrite=30s";
 private static final String VALID_SPEC_ACCESS_EXPIRATION = "expireAfterAccess=15m";
 private static final String VALID_SPEC_REFRESH_DURATION = "refreshAfterWrite=1h";

 @Test
 public void testParseValidSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(VALID_SPEC_INITIAL_CAPACITY);
 assertEquals("initialCapacity=10", spec.toParsableString());

 spec = CacheBuilderSpec.parse(VALID_SPEC_MAXIMUM_SIZE);
 assertEquals("maximumSize=20", spec.toParsableString());

 spec = CacheBuilderSpec.parse(VALID_SPEC_MAXIMUM_WEIGHT);
 assertEquals("maximumWeight=30", spec.toParsableString());

 spec = CacheBuilderSpec.parse(VALID_SPEC_CONCURRENCY_LEVEL);
 assertEquals("concurrencyLevel=5", spec.toParsableString());

 spec = CacheBuilderSpec.parse(VALID_SPEC_KEY_STRENGTH);
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);

 spec = CacheBuilderSpec.parse(VALID_SPEC_VALUE_STRENGTH);
 assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);

 spec = CacheBuilderSpec.parse(VALID_SPEC_RECORD_STATS);
 assertTrue(spec.recordStats);

 spec = CacheBuilderSpec.parse(VALID_SPEC_WRITE_EXPIRATION);
 assertEquals(30, spec.writeExpirationDuration);
 assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);

 spec = CacheBuilderSpec.parse(VALID_SPEC_ACCESS_EXPIRATION);
 assertEquals(15, spec.accessExpirationDuration);
 assertEquals(TimeUnit.MINUTES, spec.accessExpirationTimeUnit);

 spec = CacheBuilderSpec.parse(VALID_SPEC_REFRESH_DURATION);
 assertEquals(1, spec.refreshDuration); // assuming hours = 1000 * 60 * 60
 assertEquals(TimeUnit.HOURS, spec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecFormat() {
 try {
 CacheBuilderSpec.parse("initialCapacity=10s"); // invalid format
 fail();
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidKey() {
 try {
 CacheBuilderSpec.parse("invalidKey=10");
 fail();
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidValue() {
 try {
 CacheBuilderSpec.parse("initialCapacity=abc");
 fail();
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidKeyStrength() {
 try {
 CacheBuilderSpec.parse("unknownKeyStrength");
 fail();
 } catch (AssertionError e) {
 // expected exception
 }
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidValueStrength() {
 try {
 CacheBuilderSpec.parse("unknownValueStrength");
 fail();
 } catch (AssertionError e) {
 // expected exception
 }
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(VALID_SPEC_INITIAL_CAPACITY);
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(10, cacheBuilder.initialCapacity());

 spec = CacheBuilderSpec.parse(VALID_SPEC_MAXIMUM_SIZE);
 cacheBuilder = spec.toCacheBuilder();
 assertEquals(20, cacheBuilder.maximumSize());

 spec = CacheBuilderSpec.parse(VALID_SPEC_MAXIMUM_WEIGHT);
 cacheBuilder = spec.toCacheBuilder();
 assertEquals(30, cacheBuilder.maximumWeight());

 spec = CacheBuilderSpec.parse(VALID_SPEC_CONCURRENCY_LEVEL);
 cacheBuilder = spec.toCacheBuilder();
 assertEquals(5, cacheBuilder.concurrencyLevel());

 spec = CacheBuilderSpec.parse(VALID_SPEC_KEY_STRENGTH);
 cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.weakKeys());

 spec = CacheBuilderSpec.parse(VALID_SPEC_VALUE_STRENGTH);
 cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.weakValues());

 spec = CacheBuilderSpec.parse(VALID_SPEC_RECORD_STATS);
 cacheBuilder = spec.toCacheBuilder();
 assertTrue(cacheBuilder.recordStats());

 spec = CacheBuilderSpec.parse(VALID_SPEC_WRITE_EXPIRATION);
 cacheBuilder = spec.toCacheBuilder();
 assertEquals(30, cacheBuilder.expireAfterWrite(TimeUnit.SECONDS));

 spec = CacheBuilderSpec.parse(VALID_SPEC_ACCESS_EXPIRATION);
 cacheBuilder = spec.toCacheBuilder();
 assertEquals(15 * 60, cacheBuilder.expireAfterAccess(TimeUnit.MINUTES));

 spec = CacheBuilderSpec.parse(VALID_SPEC_REFRESH_DURATION);
 cacheBuilder = spec.toCacheBuilder();
 assertEquals(1, cacheBuilder.refreshAfterWrite(TimeUnit.HOURS));
 }

 @Test
 public void testToParsableString() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, recordStats, expireAfterWrite=30s, expireAfterAccess=15m, refreshAfterWrite=1h");
 assertEquals("initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, recordStats, expireAfterWrite=30s, expireAfterAccess=15m, refreshAfterWrite=1h", spec.toParsableString());
 }

 @Test
 public void testEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, recordStats, expireAfterWrite=30s, expireAfterAccess=15m, refreshAfterWrite=1h");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, recordStats, expireAfterWrite=30s, expireAfterAccess=15m, refreshAfterWrite=1h");
 CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, recordStats, expireAfterWrite=30s, expireAfterAccess=15m, refreshAfterWrite=1h");
 CacheBuilderSpec spec4 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, concurrencyLevel=6, weakKeys, recordStats, expireAfterWrite=30s, expireAfterAccess=15m, refreshAfterWrite=1h");

 assertEquals(spec1, spec2);
 assertEquals(spec1, spec3);
 assertNotEquals(spec1, spec4);
 }

 @Test
 public void testHashCode() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, recordStats, expireAfterWrite=30s, expireAfterAccess=15m, refreshAfterWrite=1h");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, recordStats, expireAfterWrite=30s, expireAfterAccess=15m, refreshAfterWrite=1h");
 CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, recordStats, expireAfterWrite=30s, expireAfterAccess=15m, refreshAfterWrite=1h");
 CacheBuilderSpec spec4 = new CacheBuilderSpec("initialCapacity=10, maximumSize=20, concurrencyLevel=6, weakKeys, recordStats, expireAfterWrite=30s, expireAfterAccess=15m, refreshAfterWrite=1h");

 int hashCode1 = spec1.hashCode();
 int hashCode2 = spec2.hashCode();
 int hashCode3 = spec3.hashCode();
 int hashCode4 = spec4.hashCode();

 assertEquals(hashCode1, hashCode2);
 assertEquals(hashCode1, hashCode3);
 assertNotEquals(hashCode1, hashCode4);
 }
}