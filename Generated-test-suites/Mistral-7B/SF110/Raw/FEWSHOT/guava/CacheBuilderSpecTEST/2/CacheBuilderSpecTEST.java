package com.google.common.cache;

import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CacheBuilderSpecTest {

 @Test
 public void testParse_validSpecification_createsCacheBuilderSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 assertEquals("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m", spec.toParsableString());
 // Verify visible fields
 assertEquals(5, spec.initialCapacity);
 assertEquals(10L, spec.maximumSize);
 assertEquals(20L, spec.maximumWeight);
 assertEquals(5, spec.concurrencyLevel);
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 assertEquals(true, spec.recordStats);
 assertEquals(1L * TimeUnit.DAYS, spec.accessExpirationDuration);
 assertEquals(TimeUnit.DAYS, spec.accessExpirationTimeUnit);
 assertEquals(2L * TimeUnit.HOURS, spec.writeExpirationDuration);
 assertEquals(TimeUnit.HOURS, spec.writeExpirationTimeUnit);
 assertEquals(3L * TimeUnit.MINUTES, spec.refreshDuration);
 assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidSpecification_throwsIllegalArgumentException() {
 try {
 CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m, invalidKey");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_missingValue_throwsIllegalArgumentException() {
 try {
 CacheBuilderSpec.parse("initialCapacity=5, maximumSize, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_invalidValueFormat_throwsIllegalArgumentException() {
 try {
 CacheBuilderSpec.parse("initialCapacity=5, maximumSize=invalid, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParse_duplicateKey_throwsIllegalArgumentException() {
 try {
 CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumSize=20, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testToCacheBuilder_noSettings_createsDefaultCacheBuilder() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 // Default values
 assertEquals(11, cacheBuilder.initialCapacity());
 assertEquals(1000L, cacheBuilder.maximumSize());
 assertEquals(5L, cacheBuilder.maximumWeight());
 assertEquals(16, cacheBuilder.concurrencyLevel());
 assertEquals(LocalCache.Strength.STRONG, cacheBuilder.strengthOfKeys());
 assertEquals(LocalCache.Strength.STRONG, cacheBuilder.strengthOfValues());
 assertFalse(cacheBuilder.recordStats());
 assertFalse(cacheBuilder.expireAfterWrite());
 assertFalse(cacheBuilder.expireAfterAccess());
 assertFalse(cacheBuilder.refreshAfterWrite());
 }

 @Test
 public void testToCacheBuilder_validSpecification_createsCustomCacheBuilder() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 // Custom values
 assertEquals(5, cacheBuilder.initialCapacity());
 assertEquals(10L, cacheBuilder.maximumSize());
 assertEquals(20L, cacheBuilder.maximumWeight());
 assertEquals(5, cacheBuilder.concurrencyLevel());
 assertEquals(LocalCache.Strength.WEAK, cacheBuilder.strengthOfKeys());
 assertEquals(LocalCache.Strength.SOFT, cacheBuilder.strengthOfValues());
 assertEquals(true, cacheBuilder.recordStats());
 assertEquals(1L * TimeUnit.DAYS, cacheBuilder.expirationDuration());
 assertEquals(TimeUnit.DAYS, cacheBuilder.expirationTimeUnit());
 assertEquals(2L * TimeUnit.HOURS, cacheBuilder.writeExpirationDuration());
 assertEquals(TimeUnit.HOURS, cacheBuilder.writeExpirationTimeUnit());
 assertEquals(3L * TimeUnit.MINUTES, cacheBuilder.refreshExpirationDuration());
 assertEquals(TimeUnit.MINUTES, cacheBuilder.refreshExpirationTimeUnit());
 }

}