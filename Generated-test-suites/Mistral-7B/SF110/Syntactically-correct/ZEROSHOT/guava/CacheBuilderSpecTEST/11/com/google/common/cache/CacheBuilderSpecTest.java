package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.cache.*;
import com.google.common.annotations.*;
import java.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;

@Beta
@org.junit.Test
public class CacheBuilderSpecTest {

 private final static String validSpec = "initialCapacity=10, maximumSize=200, maximumWeight=500, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m";
 private final static String invalidSpec = "invalidKey=10, invalidValue=true";

 @VisibleForTesting
 @Autowired
 private CacheBuilder<Object, Object> cacheBuilder;

 @Before
 public void setup() {
 this.cacheBuilder = CacheBuilder.newBuilder();
 }

 @Test
 public void testParseValidSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse(validSpec);
 assertEquals(10, spec.getInitialCapacity());
 assertEquals(200, spec.getMaximumSize());
 assertEquals(500, spec.getMaximumWeight());
 assertEquals(10, spec.getConcurrencyLevel());
 assertTrue(spec.isKeyWeak());
 assertTrue(spec.isValueSoft());
 assertTrue(spec.isRecordStats());
 assertEquals(1, spec.getAccessExpirationTimeUnit().toMinutes());
 assertEquals(2, spec.getWriteExpirationTimeUnit().toHours());
 assertEquals(3, spec.getRefreshTimeUnit().toMinutes());
 }

 @Test
 public void testParseEmptySpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("");
 assertNull(spec.getInitialCapacity());
 assertNull(spec.getMaximumSize());
 assertNull(spec.getMaximumWeight());
 assertNull(spec.getConcurrencyLevel());
 assertFalse(spec.isKeyWeak());
 assertFalse(spec.isValueSoft());
 assertFalse(spec.isRecordStats());
 assertNull(spec.getAccessExpirationTimeUnit());
 assertNull(spec.getWriteExpirationTimeUnit());
 assertNull(spec.getRefreshTimeUnit());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpec() {
 try {
 CacheBuilderSpec.parse(invalidSpec);
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("key invalidKey does not take values", e.getMessage());
 }
 }

 @Test
 public void testToParsableString() {
 CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=200, maximumWeight=500, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 assertEquals(validSpec, spec.toParsableString());
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilderSpec spec = new CacheBuilderSpec(validSpec);
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().initialCapacity(10).maximumSize(200).maximumWeight(500).concurrencyLevel(10).weakKeys().softValues().recordStats().expireAfterAccess(1, TimeUnit.DAYS).expireAfterWrite(2, TimeUnit.HOURS).refreshAfterWrite(3, TimeUnit.MINUTES), actual);
 }

 @Test
 public void testDisableCaching() {
 CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
 CacheBuilder<Object, Object> actual = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().maximumSize(0), actual);
 }

 @Test
 public void testHashCodeAndEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=200, maximumWeight=500, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=200, maximumWeight=500, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 assertEquals(spec1, spec2);
 CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=10, maximumSize=200, maximumWeight=500, concurrencyLevel=11, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 assertNotEquals(spec1, spec3);
 }
}