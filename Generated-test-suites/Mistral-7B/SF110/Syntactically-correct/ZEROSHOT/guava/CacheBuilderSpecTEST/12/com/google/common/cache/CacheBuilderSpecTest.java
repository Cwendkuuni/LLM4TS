package com.example.test;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import com.google.common.cache.*;
import com.google.common.cache.CacheBuilderSpec.*;
import org.junit.Before;
import org.junit.Test;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setUp() {
 spec = ParseCacheBuilderSpec.parse("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5,"
 + " keyStrength=STRONG, valueStrength=STRONG, recordStats=true,"
 + " writeExpirationDuration=1h, writeExpirationTimeUnit=HOURS,"
 + " accessExpirationDuration=30s, accessExpirationTimeUnit=SECONDS,"
 + " refreshDuration=1m, refreshTimeUnit=MINUTES");
 }

 @Test
 public void testParse() {
 assertEquals("initialCapacity=10, maximumSize=20, maximumWeight=30, concurrencyLevel=5,"
 + " keyStrength=STRONG, valueStrength=STRONG, recordStats=true,"
 + " writeExpirationDuration=3600000, writeExpirationTimeUnit=HOURS,"
 + " accessExpirationDuration=30000, accessExpirationTimeUnit=SECONDS,"
 + " refreshDuration=60000, refreshTimeUnit=MINUTES", spec.toParsableString());
 }

 @Test
 public void testToCacheBuilder() {
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(10, cacheBuilder.initialCapacity());
 assertEquals(20, cacheBuilder.maximumSize());
 assertEquals(30, cacheBuilder.maximumWeight());
 assertEquals(5, cacheBuilder.concurrencyLevel());
 cacheBuilder.weakKeys();
 cacheBuilder.weakValues();
 assertEquals(true, cacheBuilder.recordStats());
 assertEquals(TimeUnit.HOURS, cacheBuilder.expireAfterWriteUnit());
 assertEquals(1, cacheBuilder.expireAfterWriteInHours());
 assertEquals(TimeUnit.MINUTES, cacheBuilder.expireAfterAccessUnit());
 assertEquals(30, cacheBuilder.expireAfterAccessInSeconds());
 assertEquals(TimeUnit.MINUTES, cacheBuilder.refreshAfterWriteUnit());
 assertEquals(1, cacheBuilder.refreshAfterWriteInMinutes());
 }

}

class ParseCacheBuilderSpec {
 static CacheBuilderSpec parse(String cacheBuilderSpecification) {
 return CacheBuilderSpec.parse(cacheBuilderSpecification);
 }
}