package com.google.common.cache;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.AccessDurationParser;
import com.google.common.cache.CacheBuilderSpec.ConcurrencyLevelParser;
import com.google.common.cache.CacheBuilderSpec.InitialCapacityParser;
import com.google.common.cache.CacheBuilderSpec.KeyStrengthParser;
import com.google.common.cache.CacheBuilderSpec.MaximumSizeParser;
import com.google.common.cache.CacheBuilderSpec.MaximumWeightParser;
import com.google.common.cache.CacheBuilderSpec.RecordStatsParser;
import com.google.common.cache.CacheBuilderSpec.RefreshDurationParser;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import com.google.common.cache.CacheBuilderSpec.WriteDurationParser;
import com.google.common.collect.ImmutableMap;

public class CacheBuilderSpecTest {

	@Test
	public void testParse() {
		String cacheSpec = "initialCapacity=10,maximumSize=500,maximumWeight=1000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m";
		CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheSpec);

		assertEquals(10, spec.getInitialCapacity());
		assertEquals(500, spec.getMaximumSize());
		assertEquals(1000, spec.getMaximumWeight());
		assertEquals(10, spec.getConcurrencyLevel());
		assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
		assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
		assertTrue(spec.isRecordStats());
		assertEquals(1, TimeUnit.HOURS.toMinutes(1));
		assertEquals(30, TimeUnit.SECONDS.toMillis(30));
		assertEquals(1, TimeUnit.MINUTES.toSeconds(1));
	}

	@Test
	public void testParsingEmptySpec() {
		String cacheSpec = "";
		try {
			CacheBuilderSpec.parse(cacheSpec);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("blank cacheBuilderSpecification", e.getMessage());
		}
	}

	@Test
	public void testParsingInvalidSpec() {
		String cacheSpec = "invalidKey=value";
		try {
			CacheBuilderSpec.parse(cacheSpec);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("unknown key invalidKey", e.getMessage());
		}
	}

	@Test
	public void testToCacheBuilder() {
		String cacheSpec = "initialCapacity=10,maximumSize=500,maximumWeight=1000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m";
		CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheSpec);
		CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

		assertEquals(10, cacheBuilder.initialCapacity());
		assertEquals(500, cacheBuilder.maximumSize());
		assertEquals(1000, cacheBuilder.maximumWeight());
		assertEquals(10, cacheBuilder.concurrencyLevel());
		assertTrue(cacheBuilder.weakKeys());
		assertTrue(cacheBuilder.softValues());
		assertTrue(cacheBuilder.recordStats());
		assertEquals(1, TimeUnit.HOURS.toMinutes(1));
		assertEquals(30, TimeUnit.SECONDS.toMillis(30));
		assertEquals(1, TimeUnit.MINUTES.toSeconds(1));
		assertEquals(1, TimeUnit.HOURS.toMinutes(1));
	}

	@Test
	public void testDisableCaching() {
		CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
		CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
		assertEquals(0, cacheBuilder.maximumSize());
	}

	@Test
	public void testToParsableString() {
		String cacheSpec = "initialCapacity=10,maximumSize=500,maximumWeight=1000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m";
		CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheSpec);
		assertEquals(cacheSpec, spec.toParsableString());
	}

	@Test
	public void testToString() {
		String cacheSpec = "initialCapacity=10,maximumSize=500,maximumWeight=1000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m";
		CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheSpec);
		assertEquals(cacheSpec, spec.toString());
	}

	@Test
	public void testHashCode() {
		String cacheSpec1 = "initialCapacity=10,maximumSize=500,maximumWeight=1000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m";
		String cacheSpec2 = "initialCapacity=10,maximumSize=500,maximumWeight=1000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m";
		String cacheSpec3 = "initialCapacity=11,maximumSize=500,maximumWeight=1000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m";

		CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheSpec1);
		CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheSpec2);
		CacheBuilderSpec spec3 = CacheBuilderSpec.parse(cacheSpec3);

		assertEquals(spec1.hashCode(), spec1.hashCode());
		assertEquals(spec1.hashCode(), spec2.hashCode());
		assertNotEquals(spec1.hashCode(), spec3.hashCode());
	}

	@Test
	public void testEquals() {
		String cacheSpec1 = "initialCapacity=10,maximumSize=500,maximumWeight=1000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m";
		String cacheSpec2 = "initialCapacity=10,maximumSize=500,maximumWeight=1000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m";
		String cacheSpec3 = "initialCapacity=10,maximumSize=500,maximumWeight=1000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterWrite=1h,expireAfterAccess=31s,refreshAfterWrite=1m";

		CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheSpec1);
		CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheSpec2);
		CacheBuilderSpec spec3 = CacheBuilderSpec.parse(cacheSpec3);

		assertTrue(spec1.equals(spec1));
		assertTrue(spec1.equals(spec2));
		assertFalse(spec1.equals(spec3));
	}

	@Test
	public void testCreateInstance() throws Exception {
		Constructor<CacheBuilderSpec> constructor = CacheBuilderSpec.class.getDeclaredConstructor(String.class);
		constructor.setAccessible(true);

		String cacheSpec = "initialCapacity=10,maximumSize=500,maximumWeight=1000,concurrencyLevel=10,weakKeys,softValues,recordStats,expireAfterWrite=1h,expireAfterAccess=30s,refreshAfterWrite=1m";
		CacheBuilderSpec spec = constructor.newInstance(cacheSpec);

		assertEquals(cacheSpec, spec.toParsableString());
		assertEquals(10, spec.getInitialCapacity());
		assertEquals(500, spec.getMaximumSize());
		assertEquals(1000, spec.getMaximumWeight());
		assertEquals(10, spec.getConcurrencyLevel());
		assertTrue(spec.isKeyStrengthPresent());
		assertTrue(spec.isValueStrengthPresent());
		assertTrue(spec.isRecordStatsPresent());
		assertEquals(1, TimeUnit.HOURS.toMinutes(1));
		assertEquals(30, TimeUnit.SECONDS.toMillis(30));
		assertEquals(1, TimeUnit.MINUTES.toSeconds(1));
		assertEquals(1, TimeUnit.HOURS.toMinutes(1));
	}

	@Test
	public void testValueParsers() {
		assertEquals(ImmutableMap.of(
				"initialCapacity", new InitialCapacityParser(),
				"maximumSize", new MaximumSizeParser(),
				"maximumWeight", new MaximumWeightParser(),
				"concurrencyLevel", new ConcurrencyLevelParser(),
				"weakKeys", new KeyStrengthParser(LocalCache.Strength.WEAK),
				"softValues", new ValueStrengthParser(LocalCache.Strength.SOFT),
				"recordStats", new RecordStatsParser(),
				"expireAfterAccess", new AccessDurationParser(),
				"expireAfterWrite", new WriteDurationParser(),
				"refreshAfterWrite", new RefreshDurationParser()),
				CacheBuilderSpec.VALUE_PARSERS);
	}
}