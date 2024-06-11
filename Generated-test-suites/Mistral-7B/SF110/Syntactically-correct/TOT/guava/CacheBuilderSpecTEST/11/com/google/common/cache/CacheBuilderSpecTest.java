package com.google.common.cache;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.MockitoAnnotations;

@RunWith(MockitoJUnitRunner.class)
public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;
 private CacheBuilder<Object, Object> cacheBuilder;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 this.spec = new CacheBuilderSpec("initialCapacity=1,maximumSize=10,maximumWeight=20,concurrencyLevel=10,"
 + "keyStrength=weak,valueStrength=soft,recordStats=true,"
 + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
 + "accessExpirationDuration=30s,accessExpirationTimeUnit=SECONDS,"
 + "refreshDuration=1m,refreshTimeUnit=MINUTES");
 this.cacheBuilder = this.spec.toCacheBuilder();
 }

 @Test
 public void testParseInitialCapacity() {
 assertEquals(1, this.spec.initialCapacity);
 }

 @Test
 public void testParseMaximumSize() {
 assertEquals(10, this.spec.maximumSize);
 }

 @Test
 public void testParseMaximumWeight() {
 assertEquals(20, this.spec.maximumWeight);
 }

 @Test
 public void testParseConcurrencyLevel() {
 assertEquals(10, this.spec.concurrencyLevel);
 }

 @Test
 public void testParseKeyStrength() {
 assertEquals(LocalCache.Strength.WEAK, this.spec.keyStrength);
 }

 @Test
 public void testParseValueStrength() {
 assertEquals(LocalCache.Strength.SOFT, this.spec.valueStrength);
 }

 @Test
 public void testParseRecordStats() {
 assertTrue(this.spec.recordStats);
 }

 @Test
 public void testParseWriteExpirationDuration() {
 assertEquals(3600000, this.spec.writeExpirationDuration);
 }

 @Test
 public void testParseWriteExpirationTimeUnit() {
 assertEquals(TimeUnit.HOURS, this.spec.writeExpirationTimeUnit);
 }

 @Test
 public void testParseAccessExpirationDuration() {
 assertEquals(30000, this.spec.accessExpirationDuration);
 }

 @Test
 public void testParseAccessExpirationTimeUnit() {
 assertEquals(TimeUnit.SECONDS, this.spec.accessExpirationTimeUnit);
 }

 @Test
 public void testParseRefreshDuration() {
 assertEquals(60000, this.spec.refreshDuration);
 }

 @Test
 public void testParseRefreshTimeUnit() {
 assertEquals(TimeUnit.MINUTES, this.spec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCacheBuilderSpecification() {
 new CacheBuilderSpec("initialCapacity=1,maximumSize=10,maximumWeight=20,concurrencyLevel=10,"
 + "keyStrength=weak,valueStrength=soft,recordStats=true,"
 + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
 + "accessExpirationDuration=30s,accessExpirationTimeUnit=SECONDS,"
 + "refreshDuration=1m,refreshTimeUnit=MINUTES, invalidKey=invalidValue");
 }

 @Test(expected = AssertionError.class)
 public void testUnknownKeyStrength() {
 new CacheBuilderSpec("initialCapacity=1,maximumSize=10,maximumWeight=20,concurrencyLevel=10,"
 + "keyStrength=UNKNOWN,valueStrength=soft,recordStats=true,"
 + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
 + "accessExpirationDuration=30s,accessExpirationTimeUnit=SECONDS,"
 + "refreshDuration=1m,refreshTimeUnit=MINUTES");
 }

 @Test(expected = AssertionError.class)
 public void testUnknownValueStrength() {
 new CacheBuilderSpec("initialCapacity=1,maximumSize=10,maximumWeight=20,concurrencyLevel=10,"
 + "keyStrength=weak,valueStrength=UNKNOWN,recordStats=true,"
 + "writeExpirationDuration=1h,writeExpirationTimeUnit=HOURS,"
 + "accessExpirationDuration=30s,accessExpirationTimeUnit=SECONDS,"
 + "refreshDuration=1m,refreshTimeUnit=MINUTES");
 }
}