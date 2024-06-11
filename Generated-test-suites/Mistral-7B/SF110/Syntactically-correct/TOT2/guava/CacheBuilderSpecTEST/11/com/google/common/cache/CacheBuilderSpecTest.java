package com.google.common.cache;

import static org.junit.Assert.*;

import org.junit.Test;

public class CacheBuilderSpecTest {

  @Test
  public void testParse() {
    // Given
    String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100, concurrencyLevel=5, weakKeys, recordStats, expireAfterWrite=1h, expireAfterAccess=30m, refreshAfterWrite=15m";

    // When
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

    // Then
    CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
    assertEquals(10, cacheBuilder.initialCapacity());
    assertEquals(100, cacheBuilder.maximumSize());
    assertEquals(5, cacheBuilder.concurrencyLevel());
    assertTrue(cacheBuilder.weakKeys());
    assertTrue(cacheBuilder.recordStats());
    assertEquals(1, cacheBuilder.expireAfterWrite());
    assertEquals(TimeUnit.HOURS, cacheBuilder.expireAfterWriteTimeUnit());
    assertEquals(30, cacheBuilder.expireAfterAccess());
    assertEquals(TimeUnit.MINUTES, cacheBuilder.expireAfterAccessTimeUnit());
    assertEquals(15, cacheBuilder.refreshAfterWrite());
    assertEquals(TimeUnit.MINUTES, cacheBuilder.refreshAfterWriteTimeUnit());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseEmptySpecification() {
    String cacheBuilderSpecification = "";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseInvalidSpecification() {
    String cacheBuilderSpecification = "invalidKey=value";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseDuplicateKeys() {
    String cacheBuilderSpecification = "initialCapacity=10, maximumSize=10, initialCapacity=20";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
  }

  @Test(expected = NumberFormatException.class)
  public void testParseInvalidIntegerValue() {
    String cacheBuilderSpecification = "initialCapacity=invalid";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseInvalidTimeUnit() {
    String cacheBuilderSpecification = "expireAfterWrite=10dD";
    CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
  }
}