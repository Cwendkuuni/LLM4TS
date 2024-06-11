package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import com.google.common.base.*;
import com.google.common.cache.*;
import com.google.common.collect.*;
import java.util.*;
import java.util.concurrent.*;
import javax.annotation.*;
import org.junit.*;

public class CacheBuilderSpecTest {

 @Beta
 @Test
 public void testParse() {
  // Test parsing an empty specification
  CacheBuilderSpec emptySpec = CacheBuilderSpec.parse("");
  assertEquals(0, emptySpec.initialCapacity);
  assertEquals(0L, emptySpec.maximumSize);
  assertEquals(0L, emptySpec.maximumWeight);
  assertEquals(0, emptySpec.concurrencyLevel);
  assertEquals(LocalCache.Strength.STRONG, emptySpec.keyStrength);
  assertEquals(LocalCache.Strength.STRONG, emptySpec.valueStrength);
  assertFalse(emptySpec.recordStats);
  assertEquals(0L, emptySpec.writeExpirationDuration);
  assertEquals(null, emptySpec.writeExpirationTimeUnit);
  assertEquals(0L, emptySpec.accessExpirationDuration);
  assertEquals(null, emptySpec.accessExpirationTimeUnit);
  assertEquals(0L, emptySpec.refreshDuration);
  assertEquals(null, emptySpec.refreshTimeUnit);
  assertEquals("", emptySpec.specification);

  // Test parsing a simple specification
  CacheBuilderSpec simpleSpec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5");
  assertEquals(10, simpleSpec.initialCapacity);
  assertEquals(200L, simpleSpec.maximumSize);
  assertEquals(500L, simpleSpec.maximumWeight);
  assertEquals(5, simpleSpec.concurrencyLevel);
  assertEquals(LocalCache.Strength.STRONG, simpleSpec.keyStrength);
  assertEquals(LocalCache.Strength.STRONG, simpleSpec.valueStrength);
  assertFalse(simpleSpec.recordStats);
  assertEquals(0L, simpleSpec.writeExpirationDuration);
  assertEquals(null, simpleSpec.writeExpirationTimeUnit);
  assertEquals(0L, simpleSpec.accessExpirationDuration);
  assertEquals(null, simpleSpec.accessExpirationTimeUnit);
  assertEquals(0L, simpleSpec.refreshDuration);
  assertEquals(null, simpleSpec.refreshTimeUnit);
  assertEquals("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5", simpleSpec.specification);

  // Test parsing a specification with recordStats set
  CacheBuilderSpec recordStatsSpec = CacheBuilderSpec.parse("maximumSize=10,recordStats=true");
  assertEquals(0, recordStatsSpec.initialCapacity);
  assertEquals(10L, recordStatsSpec.maximumSize);
  assertEquals(0L, recordStatsSpec.maximumWeight);
  assertEquals(0, recordStatsSpec.concurrencyLevel);
  assertEquals(LocalCache.Strength.STRONG, recordStatsSpec.keyStrength);
  assertEquals(LocalCache.Strength.STRONG, recordStatsSpec.valueStrength);
  assertTrue(recordStatsSpec.recordStats);
  assertEquals(0L, recordStatsSpec.writeExpirationDuration);
  assertEquals(null, recordStatsSpec.writeExpirationTimeUnit);
  assertEquals(0L, recordStatsSpec.accessExpirationDuration);
  assertEquals(null, recordStatsSpec.accessExpirationTimeUnit);
  assertEquals(0L, recordStatsSpec.refreshDuration);
  assertEquals(null, recordStatsSpec.refreshTimeUnit);
  assertEquals("maximumSize=10,recordStats=true", recordStatsSpec.specification);

  // Test parsing a specification with expireAfterAccess set
  CacheBuilderSpec accessExpirationSpec = CacheBuilderSpec.parse("accessExpirationDuration=10m");
  assertEquals(0, accessExpirationSpec.initialCapacity);
  assertEquals(0L, accessExpirationSpec.maximumSize);
  assertEquals(0L, accessExpirationSpec.maximumWeight);
  assertEquals(0, accessExpirationSpec.concurrencyLevel);
  assertEquals(LocalCache.Strength.STRONG, accessExpirationSpec.keyStrength);
  assertEquals(LocalCache.Strength.STRONG, accessExpirationSpec.valueStrength);
  assertFalse(accessExpirationSpec.recordStats);
  assertEquals(0L, accessExpirationSpec.writeExpirationDuration);
  assertEquals(null, accessExpirationSpec.writeExpirationTimeUnit);
  assertEquals(10L, accessExpirationSpec.accessExpirationDuration);
  assertEquals(TimeUnit.MINUTES, accessExpirationSpec.accessExpirationTimeUnit);
  assertEquals(0L, accessExpirationSpec.refreshDuration);
  assertEquals(null, accessExpirationSpec.refreshTimeUnit);
  assertEquals("accessExpirationDuration=10m", accessExpirationSpec.specification);

  // Test parsing a specification with expireAfterWrite set
  CacheBuilderSpec writeExpirationSpec = CacheBuilderSpec.parse("writeExpirationDuration=1h");
  assertEquals(0, writeExpirationSpec.initialCapacity);
  assertEquals(0L, writeExpirationSpec.maximumSize);
  assertEquals(0L, writeExpirationSpec.maximumWeight);
  assertEquals(0, writeExpirationSpec.concurrencyLevel);
  assertEquals(LocalCache.Strength.STRONG, writeExpirationSpec.keyStrength);
  assertEquals(LocalCache.Strength.STRONG, writeExpirationSpec.valueStrength);
  assertFalse(writeExpirationSpec.recordStats);
  assertEquals(1L, writeExpirationSpec.writeExpirationDuration);
  assertEquals(TimeUnit.HOURS, writeExpirationSpec.writeExpirationTimeUnit);
  assertEquals(0L, writeExpirationSpec.accessExpirationDuration);
  assertEquals(null, writeExpirationSpec.accessExpirationTimeUnit);
  assertEquals(0L, writeExpirationSpec.refreshDuration);
  assertEquals(null, writeExpirationSpec.refreshTimeUnit);
  assertEquals("writeExpirationDuration=1h", writeExpirationSpec.specification);

  // Test parsing a specification with refreshAfterWrite set
  CacheBuilderSpec refreshSpec = CacheBuilderSpec.parse("refreshAfterWrite=1h");
  assertEquals(0, refreshSpec.initialCapacity);
  assertEquals(0L, refreshSpec.maximumSize);
  assertEquals(0L, refreshSpec.maximumWeight);
  assertEquals(0, refreshSpec.concurrencyLevel);
  assertEquals(LocalCache.Strength.STRONG, refreshSpec.keyStrength);
  assertEquals(LocalCache.Strength.STRONG, refreshSpec.valueStrength);
  assertFalse(refreshSpec.recordStats);
  assertEquals(0L, refreshSpec.writeExpirationDuration);
  assertEquals(null, refreshSpec.writeExpirationTimeUnit);
  assertEquals(0L, refreshSpec.accessExpirationDuration);
  assertEquals(null, refreshSpec.accessExpirationTimeUnit);
  assertEquals(1L, refreshSpec.refreshDuration);
  assertEquals(TimeUnit.HOURS, refreshSpec.refreshTimeUnit);
  assertEquals("refreshAfterWrite=1h", refreshSpec.specification);
 }

 @Test
 public void testToParsableString() {
  // Test toParsableString for an empty specification
  CacheBuilderSpec emptySpec = CacheBuilderSpec.parse("");
  assertEquals("", emptySpec.toParsableString());

  // Test toParsableString for a simple specification
  CacheBuilderSpec simpleSpec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5");
  assertEquals("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5", simpleSpec.toParsableString());

  // Test toParsableString for a specification with recordStats set
  CacheBuilderSpec recordStatsSpec = CacheBuilderSpec.parse("maximumSize=10,recordStats=true");
  assertEquals("maximumSize=10,recordStats=true", recordStatsSpec.toParsableString());

  // Test toParsableString for a specification with expireAfterAccess set
  CacheBuilderSpec accessExpirationSpec = CacheBuilderSpec.parse("accessExpirationDuration=10m");
  assertEquals("accessExpirationDuration=10m", accessExpirationSpec.toParsableString());

  // Test toParsableString for a specification with expireAfterWrite set
  CacheBuilderSpec writeExpirationSpec = CacheBuilderSpec.parse("writeExpirationDuration=1h");
  assertEquals("writeExpirationDuration=1h", writeExpirationSpec.toParsableString());

  // Test toParsableString for a specification with refreshAfterWrite set
  CacheBuilderSpec refreshSpec = CacheBuilderSpec.parse("refreshAfterWrite=1h");
  assertEquals("refreshAfterWrite=1h", refreshSpec.toParsableString());
 }

 @Test
 public void testEquals() {
  // Test equals for an empty specification
  CacheBuilderSpec emptySpec1 = CacheBuilderSpec.parse("");
  CacheBuilderSpec emptySpec2 = CacheBuilderSpec.parse("");
  assertTrue(emptySpec1.equals(emptySpec2));

  // Test equals for two equal specifications
  CacheBuilderSpec simpleSpec1 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5");
  CacheBuilderSpec simpleSpec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5");
  assertTrue(simpleSpec1.equals(simpleSpec2));

  // Test equals for two unequal specifications
  CacheBuilderSpec simpleSpec3 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5");
  CacheBuilderSpec simpleSpec4 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=501,concurrencyLevel=5");
  assertFalse(simpleSpec3.equals(simpleSpec4));

  // Test equals for two specifications with different classes
  CacheBuilderSpec simpleSpec5 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5");
  String specString = "initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5";
  CacheBuilderSpec simpleSpec6 = new CacheBuilderSpec(specString);
  assertFalse(simpleSpec5.equals(simpleSpec6));
 }

 @Test
 public void testHashCode() {
  // Test hashCode for an empty specification
  CacheBuilderSpec emptySpec = CacheBuilderSpec.parse("");
  int emptySpecHashCode = emptySpec.hashCode();
  CacheBuilderSpec emptySpec2 = CacheBuilderSpec.parse("");
  int emptySpec2HashCode = emptySpec2.hashCode();
  assertEquals(emptySpecHashCode, emptySpec2HashCode);

  // Test hashCode for a simple specification
  CacheBuilderSpec simpleSpec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5");
  int simpleSpecHashCode = simpleSpec.hashCode();
  CacheBuilderSpec simpleSpec2 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5");
  int simpleSpec2HashCode = simpleSpec2.hashCode();
  assertEquals(simpleSpecHashCode, simpleSpec2HashCode);

  // Test hashCode for two unequal specifications
  CacheBuilderSpec simpleSpec3 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5");
  CacheBuilderSpec simpleSpec4 = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=501,concurrencyLevel=5");
  int simpleSpec3HashCode = simpleSpec3.hashCode();
  int simpleSpec4HashCode = simpleSpec4.hashCode();
  assertNotEquals(simpleSpec3HashCode, simpleSpec4HashCode);
 }

 @Test
 public void testToString() {
  // Test toString for an empty specification
  CacheBuilderSpec emptySpec = CacheBuilderSpec.parse("");
  assertEquals("CacheBuilderSpec{}", emptySpec.toString());

  // Test toString for a simple specification
  CacheBuilderSpec simpleSpec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5");
  assertEquals("CacheBuilderSpec{initialCapacity=10, maximumSize=200, maximumWeight=500, concurrencyLevel=5, keyStrength=STRONG, valueStrength=STRONG, recordStats=false, writeExpirationDuration=0, writeExpirationTimeUnit=null, accessExpirationDuration=0, accessExpirationTimeUnit=null, refreshDuration=0, refreshTimeUnit=null, specification=initialCapacity=10,maximumSize=200,maximumWeight=500,concurrencyLevel=5}", simpleSpec.toString());
 }
}