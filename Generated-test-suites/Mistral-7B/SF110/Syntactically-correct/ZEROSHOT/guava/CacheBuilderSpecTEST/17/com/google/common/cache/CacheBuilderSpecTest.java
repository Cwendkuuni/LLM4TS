package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import java.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;
import static org.mockito.Mockito.*;

@Beta
@org.junit.runner.RunWith(org.junit.runners.JUnit4.class)
public class CacheBuilderSpecTest {

 private static final String VALID_SPEC = "initialCapacity=10, maximumSize=200, maximumWeight=300, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=1m";
 private static final String INVALID_SPEC = "initialCapacity=abc, maximumSize=200, maximumWeight=300, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=1m, invalidKey=invalidValue";
 private static final String MISSING_VALUE_SPEC = "initialCapacity=10, maximumSize=200, maximumWeight=300, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=";
 private static final String MISSING_KEY_SPEC = "initialCapacity=10, maximumSize=200, maximumWeight=300, concurrencyLevel=5, , softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=1m";

 @VisibleForTesting
 static CacheBuilderSpec parseValidSpec() {
 return CacheBuilderSpec.parse(VALID_SPEC);
 }

 @VisibleForTesting
 static CacheBuilderSpec parseInvalidSpec() {
 return CacheBuilderSpec.parse(INVALID_SPEC);
 }

 @VisibleForTesting
 static CacheBuilderSpec parseMissingValueSpec() {
 return CacheBuilderSpec.parse(MISSING_VALUE_SPEC);
 }

 @VisibleForTesting
 static CacheBuilderSpec parseMissingKeySpec() {
 return CacheBuilderSpec.parse(MISSING_KEY_SPEC);
 }

 @VisibleForTesting
 static CacheBuilderSpec noCachingSpec() {
 return CacheBuilderSpec.disableCaching();
 }

 @VisibleForTesting
 static CacheBuilder buildCacheBuilder(CacheBuilderSpec spec) {
 return spec.toCacheBuilder();
 }

 @Before
 public void setup() {
 // Initialize mocks
 }

 @Test
 public void testParseValidSpec() {
 CacheBuilderSpec spec = parseValidSpec();
 assertEquals("Valid cache builder specification", VALID_SPEC, spec.toParsableString());
 assertNotNull("Initial capacity should not be null", spec.initialCapacity);
 assertEquals("Initial capacity should be 10", 10, spec.initialCapacity);
 assertNotNull("Maximum size should not be null", spec.maximumSize);
 assertEquals("Maximum size should be 200", 200, spec.maximumSize);
 assertNotNull("Maximum weight should not be null", spec.maximumWeight);
 assertEquals("Maximum weight should be 300", 300, spec.maximumWeight);
 assertNotNull("Concurrency level should not be null", spec.concurrencyLevel);
 assertEquals("Concurrency level should be 5", 5, spec.concurrencyLevel);
 assertNotNull("Key strength should not be null", spec.keyStrength);
 assertEquals("Key strength should be weakKeys", LocalCache.Strength.WEAK, spec.keyStrength);
 assertNotNull("Value strength should not be null", spec.valueStrength);
 assertEquals("Value strength should be softValues", LocalCache.Strength.SOFT, spec.valueStrength);
 assertNotNull("Record stats should not be null", spec.recordStats);
 assertTrue("Record stats should be set to true", spec.recordStats);
 assertNotNull("Write expiration time unit should not be null", spec.writeExpirationTimeUnit);
 assertEquals("Write expiration time unit should be SECONDS", TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
 assertNotNull("Access expiration time unit should not be null", spec.accessExpirationTimeUnit);
 assertEquals("Access expiration time unit should be DAYS", TimeUnit.DAYS, spec.accessExpirationTimeUnit);
 assertNotNull("Refresh duration should not be null", spec.refreshDuration);
 assertNotNull("Refresh time unit should not be null", spec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpec() {
 try {
 CacheBuilderSpec spec = parseInvalidSpec();
 fail("An exception should have been thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid cache builder specification", "key initialCapacity value set to abc, must be integer", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingValueSpec() {
 try {
 CacheBuilderSpec spec = parseMissingValueSpec();
 fail("An exception should have been thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid cache builder specification", "key refreshAfterWrite missing value", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseMissingKeySpec() {
 try {
 CacheBuilderSpec spec = parseMissingKeySpec();
 fail("An exception should have been thrown");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid cache builder specification", "key missing, comma separator expected", e.getMessage());
 }
 }

 @Test
 public void testNoCachingSpec() {
 CacheBuilderSpec spec = noCachingSpec();
 assertEquals("No caching specification", "maximumSize=0", spec.toParsableString());
 assertNull("Initial capacity should be null", spec.initialCapacity);
 assertNull("Maximum size should be null", spec.maximumSize);
 assertNull("Maximum weight should be null", spec.maximumWeight);
 assertNull("Concurrency level should be null", spec.concurrencyLevel);
 assertNull("Key strength should be null", spec.keyStrength);
 assertNull("Value strength should be null", spec.valueStrength);
 assertNull("Record stats should be null", spec.recordStats);
 assertNull("Write expiration time unit should be null", spec.writeExpirationTimeUnit);
 assertNull("Access expiration time unit should be null", spec.accessExpirationTimeUnit);
 assertNull("Refresh duration should be null", spec.refreshDuration);
 assertNull("Refresh time unit should be null", spec.refreshTimeUnit);
 }

 @Test
 public void testBuildCacheBuilder() {
 CacheBuilderSpec spec = parseValidSpec();
 CacheBuilder<Object, Object> cacheBuilder = buildCacheBuilder(spec);
 assertNotNull("Cache builder should not be null", cacheBuilder);
 }

}