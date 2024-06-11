package com.google.common.cache;

import static org.junit.Assert.*;

import java.util.concurrent.*;
import org.junit.*;
import com.google.common.annotations.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;

@Beta
@GuavaOptions(value = { "enableAsciiPrinter" })
public class CacheBuilderSpecTest {

 private CacheBuilderSpec cacheBuilderSpec;
 private CacheBuilderSpec emptyCacheBuilderSpec;
 private CacheBuilder<Object, Object> cacheBuilder;

 @Before
 public void setUp() {
 cacheBuilderSpec = new CacheBuilderSpec("initialCapacity=10, maximumSize=15, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 emptyCacheBuilderSpec = CacheBuilderSpec.disableCaching();
 }

 @Test
 public void parseTest() {
 CacheBuilderSpec parsedCacheBuilderSpec = CacheBuilderSpec.parse("initialCapacity=10, maximumSize=15, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 assertEquals(cacheBuilderSpec, parsedCacheSpec);
 }

 @Test(expected = IllegalArgumentException.class)
 public void parseInvalidTest() {
 CacheBuilderSpec parsedCacheBuilderSpec = CacheBuilderSpec.parse("invalidSpecification");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void disableCachingTest() {
 CacheBuilderSpec disabledCacheBuilderSpec = CacheBuilderSpec.disableCaching();
 assertEquals(emptyCacheBuilderSpec, disabledCacheBuilderSpec);
 }

 @Test
 public void toCacheBuilderTest() {
 cacheBuilder = cacheBuilderSpec.toCacheBuilder();
 assertEquals(10, cacheBuilder.initialCapacity());
 assertEquals(15, cacheBuilder.maximumSize());
 assertEquals(20, cacheBuilder.maximumWeight());
 assertEquals(3, cacheBuilder.concurrencyLevel());
 cacheBuilder.weakKeys();
 cacheBuilder.softValues();
 cacheBuilder.recordStats();
 assertEquals(1L, TimeUnit.HOURS.toNanos(1L));
 assertEquals(1L, cacheBuilder.expireAfterAccess(1, TimeUnit.HOURS).timeToLive());
 assertEquals(2L * 24L * 60L * 60L * 1000L, cacheBuilder.expireAfterWrite(2, TimeUnit.DAYS).timeToIdle());
 assertEquals(3L * 24L * 60L * 60L * 1000L, cacheBuilder.refreshAfterWrite(3, TimeUnit.HOURS).refreshInterval());
 }

 @Test
 public void toParsableStringTest() {
 assertEquals("initialCapacity=10, maximumSize=15, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h", cacheBuilderSpec.toParsableString());
 }

 @Test
 public void hashCodeTest() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=15, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=15, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=10, maximumSize=14, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3h");
 Objects.assertEquals(spec1.hashCode(), spec2.hashCode());
 Objects.assertNotEquals(spec1.hashCode(), spec3.hashCode());
 }

 @Test
 public void equalsTest() {
 assertTrue(cacheBuilderSpec.equals(cacheBuilderSpec));
 assertTrue(cacheBuilderSpec.equals(cacheBuilderSpec.toCacheBuilder().asCache()));
 assertFalse(cacheBuilderSpec.equals(new Object()));
 assertFalse(cacheBuilderSpec.equals(new Object()));
 assertTrue(cacheBuilderSpec.equals(cacheBuilderSpec.toParsableString()));
 assertFalse(cacheBuilderSpec.equals(""));
 assertFalse(cacheBuilderSpec.equals(null));
 assertFalse(cacheBuilderSpec.equals(emptyCacheBuilderSpec));
 assertFalse(cacheBuilderSpec.equals(new CacheBuilderSpec("initialCapacity=10, maximumSize=15, maximumWeight=20, concurrencyLevel=3, weakKeys, softValues, recordStats, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=3s")));
 }
}