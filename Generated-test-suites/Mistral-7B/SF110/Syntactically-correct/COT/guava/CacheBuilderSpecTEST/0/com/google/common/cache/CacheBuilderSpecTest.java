package com.example. cache;

import com.google.common.cache.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Test
 public void testParse() {
 spec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=20, concurrencyLevel=5, keyStrength=weak, valueStrength=soft, recordStats=true, expireAfterWrite=1h, expireAfterAccess=30m, refreshAfterWrite=1d");
 CacheBuilder<Object, Object> expectedBuilder = CacheBuilder.newBuilder()
 .initialCapacity(10)
 .maximumSize(20)
 .concurrencyLevel(5)
 .weakKeys()
 .softValues()
 .recordStats()
 .expireAfterWrite(1, TimeUnit.HOURS)
 .expireAfterAccess(30, TimeUnit.MINUTES)
 .refreshAfterWrite(1, TimeUnit.DAYS);
 CacheBuilder<Object, Object> actualBuilder = spec.toCacheBuilder();
 assertEquals(expectedBuilder, actualBuilder);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseInvalidSpecification() {
 spec = new CacheBuilderSpec("invalidSpecification");
 }

 @Test
 public void testParseEmptySpecification() {
 spec = new CacheBuilderSpec("");
 assertTrue(spec.initialCapacity == null && spec.maximumSize == null && spec.maximumWeight == null && spec.concurrencyLevel == null && spec.keyStrength == null && spec.valueStrength == null && spec.recordStats == null && spec.writeExpirationDuration == 0 && spec.writeExpirationTimeUnit == null && spec.accessExpirationDuration == 0 && spec.accessExpirationTimeUnit == null && spec.refreshDuration == 0 && spec.refreshTimeUnit == null);
 }

 @Test
 public void testParseInvalidKeyValuePair() {
 spec = new CacheBuilderSpec("invalidKeyValuePair=10,keyStrength=weak");
 assertFalse(spec.initialCapacity != null || spec.maximumSize != null || spec.maximumWeight != null || spec.concurrencyLevel != null || spec.keyStrength != null || spec.valueStrength != null || spec.recordStats != null || spec.writeExpirationDuration != 0 || spec.writeExpirationTimeUnit != null || spec.accessExpirationDuration != 0 || spec.accessExpirationTimeUnit != null || spec.refreshDuration != 0 || spec.refreshTimeUnit != null);
 }

 @Test
 public void testParseDuplicateKeys() {
 spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=10, keyStrength=weak");
 assertFalse(spec.initialCapacity != null && spec.maximumSize != null);
 }

 @Test
 public void testParseInvalidTimeUnit() {
 spec = new CacheBuilderSpec("expireAfterWrite=10InvalidUnit");
 assertFalse(spec.writeExpirationDuration != 0 && spec.writeExpirationTimeUnit != null);
 }
}