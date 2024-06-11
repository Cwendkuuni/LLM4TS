package com.google.common.cache; // make sure to import all necessary classes

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class CacheBuilderSpecTest {

  private CacheBuilderSpec spec;

  @Before
  public void setUp() {
    this.spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
        + "keyStrength=STRONG,valueStrength=STRONG,recordStats=true,"
        + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
        + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
        + "refreshDuration=10s,refreshTimeUnit=SECONDS");
  }

  @Test
  public void testParse() {
    assertEquals(10, this.spec.initialCapacity);
    assertEquals(20, this.spec.maximumSize);
    assertEquals(30, this.spec.maximumWeight);
    assertEquals(5, this.spec.concurrencyLevel);
    assertEquals(LocalCache.Strength.STRONG, this.spec.keyStrength);
    assertEquals(LocalCache.Strength.STRONG, this.spec.valueStrength);
    assertTrue(this.spec.recordStats);
    assertEquals(1, TimeUnit.HOURS.toNanos(1));
    assertEquals(5, TimeUnit.MINUTES.toNanos(5));
    assertEquals(10, TimeUnit.SECONDS.toNanos(10));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSpecificationFormat() {
    CacheBuilderSpec spec = null;
    try {
      spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
          + "keyStrength=STRONG,valueStrength=STRONG,recordStats=true,"
          + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
          + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
          + "refreshDuration=10s,refreshInterval=SECONDS"); // missing 'TimeUnit'
    } catch (IllegalArgumentException e) {
      assertEquals("key refreshInterval invalid format.  was refreshInterval already set", e.getMessage());
    }
    assertNull(spec);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSpecificationValue() {
    CacheBuilderSpec spec = null;
    try {
      spec = CacheBuilderSpec.parse("initialCapacity=-1,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
          + "keyStrength=SOFT,valueStrength=STRONG,recordStats=false,"
          + "writeExpirationDuration=abc,writeExpirationTimeUnit=HOURS,"
          + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
          + "refreshDuration=10s,refreshTimeUnit=SECONDS"); // invalid duration value
    } catch (IllegalArgumentException e) {
      assertEquals("key writeExpirationDuration value set to abc, must be integer", e.getMessage());
    }
    assertNull(spec);
  }

  @Test(expected = AssertionError.class)
  public void testUnknownKeyStrength() {
    CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
        + "keyStrength=WEAK_INVALID,valueStrength=STRONG,recordStats=true,"
        + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
        + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
        + "refreshDuration=10s,refreshTimeUnit=SECONDS");
  }

  @Test(expected = AssertionError.class)
  public void testUnknownValueStrength() {
    CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
        + "keyStrength=STRONG,valueStrength=WEAK_INVALID,recordStats=true,"
        + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
        + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
        + "refreshDuration=10s,refreshTimeUnit=SECONDS");
  }

  @Test
  public void testToCacheBuilder() {
    CacheBuilder<Object, Object> cacheBuilder = this.spec.toCacheBuilder();
    assertEquals(10, cacheBuilder.initialCapacity());
    assertEquals(20, cacheBuilder.maximumSize());
    assertEquals(30, cacheBuilder.maximumWeight());
    assertEquals(5, cacheBuilder.concurrencyLevel());
    assertTrue(cacheBuilder.recordStats());
    assertEquals(1, cacheBuilder.expireAfterWrite(TimeUnit.HOURS));
    assertEquals(5, cacheBuilder.expireAfterAccess(TimeUnit.MINUTES));
    assertEquals(10, cacheBuilder.refreshAfterWrite(TimeUnit.SECONDS));
  }

  @Test
  public void testToParsableString() {
    assertEquals("initialCapacity=10,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
        + "keyStrength=STRONG,valueStrength=STRONG,recordStats=true,"
        + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
        + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
        + "refreshDuration=10s,refreshTimeUnit=SECONDS", this.spec.toParsableString());
  }

  @Test
  public void testToString() {
    assertEquals(String.format("CacheBuilderSpec{specification='%s'}", this.spec.toParsableString()), this.spec.toString());
  }

  @Test
  public void testHashCode() {
    CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
        + "keyStrength=STRONG,valueStrength=STRONG,recordStats=true,"
        + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
        + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
        + "refreshDuration=10s,refreshTimeUnit=SECONDS");

    CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
        + "keyStrength=STRONG,valueStrength=STRONG,recordStats=true,"
        + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
        + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
        + "refreshDuration=10s,refreshTimeUnit=SECONDS");

    CacheBuilderSpec spec3 = CacheBuilderSpec.parse("initialCapacity=11,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
        + "keyStrength=STRONG,valueStrength=STRONG,recordStats=true,"
        + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
        + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
        + "refreshDuration=10s,refreshTimeUnit=SECONDS");

    assertEquals(spec1.hashCode(), spec2.hashCode());
    assertFalse(spec1.hashCode() == spec3.hashCode());
  }

  @Test
  public void testEquals() {
    CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
        + "keyStrength=STRONG,valueStrength=STRONG,recordStats=true,"
        + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
        + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
        + "refreshDuration=10s,refreshTimeUnit=SECONDS");

    CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
        + "keyStrength=STRONG,valueStrength=STRONG,recordStats=true,"
        + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
        + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
        + "refreshDuration=10s,refreshTimeUnit=SECONDS");

    CacheBuilderSpec spec3 = CacheBuilderSpec.parse("initialCapacity=11,maximumSize=20,maximumWeight=30,concurrencyLevel=5,"
        + "keyStrength=STRONG,valueStrength=STRONG,recordStats=true,"
        + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
        + "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES,"
        + "refreshDuration=10s,refreshTimeUnit=SECONDS");

    assertTrue(spec1.equals(spec2));
    assertFalse(spec1.equals(spec3));
  }

}