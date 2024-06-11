package com.google.common.cache;

import com.google.common.annotations.Beta;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;

@Beta
public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;
 private static final String CACHE_BUILDER_SPECIFICATION = "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=5, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=30s, refreshAfterWrite=2m";

 @Before
 public void setUp() {
 spec = CacheBuilderSpec.parse(CACHE_BUILDER_SPECIFICATION);
 }

 @Test
 public void testParseInitialCapacity() {
 assertEquals(5, spec.initialCapacity);
 }

 @Test
 public void testParseMaximumSize() {
 assertEquals(10, spec.maximumSize);
 }

 @Test
 public void testParseMaximumWeight() {
 assertEquals(20, spec.maximumWeight);
 }

 @Test
 public void testParseConcurrencyLevel() {
 assertEquals(5, spec.concurrencyLevel);
 }

 @Test
 public void testParseKeyStrengthWeak() {
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 }

 @Test
 public void testParseValueStrengthSoft() {
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 }

 @Test
 public void testParseRecordStats() {
 assertTrue(spec.recordStats);
 }

 @Test
 public void testParseAccessExpirationDuration() {
 assertEquals(3600000, spec.accessExpirationDuration);
 assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
 }

 @Test
 public void testParseWriteExpirationDuration() {
 assertEquals(30000, spec.writeExpirationDuration);
 assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
 }

 @Test
 public void testParseRefreshDuration() {
 assertEquals(2 * 60 * 1000, spec.refreshDuration);
 assertEquals(TimeUnit.MILLISECONDS, spec.refreshTimeUnit);
 }

 @Test
 public void testToParsableString() {
 assertEquals(CACHE_BUILDER_SPECIFICATION, spec.toParsableString());
 }

 @Test
 public void testToCacheBuilder() {
 final CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

 assertEquals(CacheBuilder.newBuilder(), cacheBuilder);
 assertEquals(5, cacheBuilder.initialCapacity());
 assertEquals(10, cacheBuilder.maximumSize());
 assertEquals(20, cacheBuilder.maximumWeight());
 assertEquals(5, cacheBuilder.concurrencyLevel());
 cacheBuilder.weakKeys();
 cacheBuilder.softValues();
 cacheBuilder.recordStats();
 cacheBuilder.expireAfterAccess(1, TimeUnit.HOURS);
 cacheBuilder.expireAfterWrite(30, TimeUnit.SECONDS);
 cacheBuilder.refreshAfterWrite(2, TimeUnit.MINUTES);
 }
}