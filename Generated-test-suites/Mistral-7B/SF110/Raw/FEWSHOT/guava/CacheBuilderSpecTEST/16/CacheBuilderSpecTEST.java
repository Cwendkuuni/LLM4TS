package com.google.common.cache;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CacheBuilderSpecTest {

 private final static String TEST_SPEC = "initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m";
 private CacheBuilderSpec cacheBuilderSpec;

 @Before
 public void setUp() {
 cacheBuilderSpec = CacheBuilderSpec.parse(TEST_SPEC);
 }

 @Test
 public void testInitialCapacity() {
 assertEquals(10, cacheBuilderSpec.getInitialCapacity());
 }

 @Test
 public void testMaximumSize() {
 assertEquals(20, cacheBuilderSpec.getMaximumSize());
 }

 @Test
 public void testMaximumWeight() {
 assertEquals(30, cacheBuilderSpec.getMaximumWeight());
 }

 @Test
 public void testConcurrencyLevel() {
 assertEquals(5, cacheBuilderSpec.getConcurrencyLevel());
 }

 @Test
 public void testKeyStrength() {
 assertEquals(LocalCache.Strength.WEAK, cacheBuilderSpec.getKeyStrength());
 }

 @Test
 public void testValueStrength() {
 assertEquals(LocalCache.Strength.SOFT, cacheBuilderSpec.getValueStrength());
 }

 @Test
 public void testRecordStats() {
 assertTrue(cacheBuilderSpec.isRecordStats());
 }

 @Test
 public void testWriteExpirationDuration() {
 assertEquals(1 * 24 * 60 * 60 * 1000, cacheBuilderSpec.getWriteExpirationDuration());
 }

 @Test
 public void testWriteExpirationTimeUnit() {
 assertEquals(TimeUnit.DAYS, cacheBuilderSpec.getWriteExpirationTimeUnit());
 }

 @Test
 public void testAccessExpirationDuration() {
 assertEquals(1 * 24 * 60 * 60 * 1000, cacheBuilderSpec.getAccessExpirationDuration());
 }

 @Test
 public void testAccessExpirationTimeUnit() {
 assertEquals(TimeUnit.DAYS, cacheBuilderSpec.getAccessExpirationTimeUnit());
 }

 @Test
 public void testRefreshDuration() {
 assertEquals(3 * 60 * 1000, cacheBuilderSpec.getRefreshDuration());
 }

 @Test
 public void testRefreshTimeUnit() {
 assertEquals(TimeUnit.MINUTES, cacheBuilderSpec.getRefreshTimeUnit());
 }

 @Test
 public void testNullSpec() {
 assertNull(CacheBuilderSpec.parse(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptySpec() {
 CacheBuilderSpec.parse("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidSpec() {
 String invalidSpec = "initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m, invalidKey=value";
 CacheBuilderSpec.parse(invalidSpec);
 }
}