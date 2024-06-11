package com.google.common.cache;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import org.junit.*;

public class CacheBuilderSpecTest {

  @Test
  public void testParseEmptySpec() {
    CacheBuilderSpec spec = CacheBuilderSpec.parse("");
    assertNull(spec);
  }

  @Test
  public void testParseSingleKeyValuePair() {
    String cacheBuilderSpec = "initialCapacity=10,maximumSize=20";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);
    assertEquals(spec.initialCapacity, 10);
    assertEquals(spec.maximumSize, 20);
    assertEquals(spec.concurrencyLevel, null);
    assertEquals(spec.keyStrength, null);
    assertEquals(spec.valueStrength, null);
    assertEquals(spec.recordStats, null);
    assertEquals(spec.writeExpirationDuration, 0);
    assertEquals(spec.writeExpirationTimeUnit, null);
    assertEquals(spec.accessExpirationDuration, 0);
    assertEquals(spec.accessExpirationTimeUnit, null);
    assertEquals(spec.refreshDuration, 0);
    assertEquals(spec.refreshTimeUnit, null);
  }

  @Test
  public void testParseMultipleKeyValuePairs() {
    String cacheBuilderSpec = "initialCapacity=10,maximumSize=20,concurrencyLevel=5,recordStats=true,expireAfterAccess=1h,expireAfterWrite=30s,refreshAfterWrite=1m";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);
    assertEquals(spec.initialCapacity, 10);
    assertEquals(spec.maximumSize, 20);
    assertEquals(spec.maximumWeight, null);
    assertEquals(spec.concurrencyLevel, 5);
    assertEquals(spec.keyStrength, null);
    assertEquals(spec.valueStrength, null);
    assertEquals(spec.recordStats, true);
    assertEquals(spec.writeExpirationDuration, 30);
    assertEquals(spec.writeExpirationTimeUnit, TimeUnit.SECONDS);
    assertEquals(spec.accessExpirationDuration, 3600);
    assertEquals(spec.accessExpirationTimeUnit, TimeUnit.HOURS);
    assertEquals(spec.refreshDuration, 60);
    assertEquals(spec.refreshTimeUnit, TimeUnit.MINUTES);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseInvalidKeyValuePair() {
    String cacheBuilderSpec = "initialCapacity=10,maximumSize=apple";
    CacheBuilderSpec.parse(cacheBuilderSpec);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseMissingValueForKey() {
    String cacheBuilderSpec = "initialCapacity=10,";
    CacheBuilderSpec.parse(cacheBuilderSpec);
  }

  @Test(expected = AssertionError.class)
  public void testParseUnknownKeyStrength() {
    String cacheBuilderSpec = "keyStrength=unknown";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);
  }

  @Test(expected = AssertionError.class)
  public void testParseUnknownValueStrength() {
    String cacheBuilderSpec = "valueStrength=unknown";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpec);
  }

  @Test
  public void testToCacheBuilder() {
    CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,concurrencyLevel=5,recordStats=true,expireAfterAccess=1h,expireAfterWrite=30s,refreshAfterWrite=1m");
    CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

    assertTrue(cacheBuilder.maximumSize() > 0);
    assertEquals(cacheBuilder.concurrencyLevel(), 5);
    assertTrue(cacheBuilder.recordStats());
    assertNull(cacheBuilder.expireAfterWrite());
    assertNull(cacheBuilder.expireAfterAccess());
    assertNull(cacheBuilder.refreshAfterWrite());

    CacheBuilder<Object, Object> emptyCacheBuilder = CacheBuilderSpec.disableCaching().toCacheBuilder();
    assertFalse(emptyCacheBuilder.maximumSize() > 0);
  }

  @Test
  public void testToString() {
    CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,concurrencyLevel=5,recordStats=true,expireAfterAccess=1h,expireAfterWrite=30s,refreshAfterWrite=1m");
    String toString = spec.toString();
    assertEquals(toString, "CacheBuilderSpec{toParsableString='initialCapacity=10,maximumSize=20,concurrencyLevel=5,recordStats=true,expireAfterAccess=1h,expireAfterWrite=30s,refreshAfterWrite=1m'}");
  }

  @Test
  public void testHashCodeAndEquals() {
    CacheBuilderSpec spec1 =
        CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,concurrencyLevel=5,recordStats=true,expireAfterAccess=1h,expireAfterWrite=30s,refreshAfterWrite=1m");
    CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,concurrencyLevel=5,recordStats=true,expireAfterAccess=1h,expireAfterWrite=30s,refreshAfterWrite=1m");
    CacheBuilderSpec spec3 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,concurrencyLevel=6,recordStats=true,expireAfterAccess=1h,expireAfterWrite=30s,refreshAfterWrite=1m");

    assertEquals(spec1.hashCode(), spec2.hashCode());
    assertEquals(spec1, spec2);
    assertNotEquals(spec1, spec3);
  }
}