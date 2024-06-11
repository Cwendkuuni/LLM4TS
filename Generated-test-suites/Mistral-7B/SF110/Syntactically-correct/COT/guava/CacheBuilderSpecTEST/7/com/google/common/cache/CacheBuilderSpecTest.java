package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=5,maximumSize=10,maximumWeight=15,concurrencyLevel=2,weakKeys,softValues,recordStats,expireAfterAccess=5m,expireAfterWrite=1h,refreshAfterWrite=2d";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals(5, spec.getInitialCapacity());
        assertEquals(10, spec.getMaximumSize());
        assertEquals(15, spec.getMaximumWeight());
        assertEquals(2, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
        assertTrue(spec.isRecordStats());
        assertEquals(5, spec.getAccessExpirationDuration());
        assertEquals(TimeUnit.MINUTES, spec.getAccessExpirationTimeUnit());
        assertEquals(3600, spec.getWriteExpirationDuration());
        assertEquals(TimeUnit.HOURS, spec.getWriteExpirationTimeUnit());
        assertEquals(2 * 24 * 60 * 60, spec.getRefreshDuration());
        assertEquals(TimeUnit.DAYS, spec.getRefreshTimeUnit());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidSpecification() {
        String cacheBuilderSpecification = "initialCapacity=5,maximumSize=10,concurrencyLevel=2,weakKeys,softValues,recordStats,expireAfterAccess=5m,expireAfterWrite=1h,refreshAfterWrite=2d,invalid";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
        fail("Should throw IllegalArgumentException for invalid key-value pair");
    }

    @Test
    public void testToParsableString() {
        String cacheBuilderSpecification = "initialCapacity=5,maximumSize=10,maximumWeight=15,concurrencyLevel=2,weakKeys,softValues,recordStats,expireAfterAccess=5m,expireAfterWrite=1h,refreshAfterWrite=2d";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals(cacheBuilderSpecification, spec.toParsableString());
    }

    @Test
    public void testParseEmptySpecification() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertNull(spec);
    }

    @Test
    public void testParseNullSpecification() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse(null);
        assertNull(spec);
    }

    @Test
    public void testToCacheBuilder() {
        String cacheBuilderSpecification = "initialCapacity=5,maximumSize=10,maximumWeight=15,concurrencyLevel=2,weakKeys,softValues,recordStats,expireAfterAccess=5m,expireAfterWrite=1h,refreshAfterWrite=2d";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        assertNotNull(cacheBuilder);

        assertEquals(5, cacheBuilder.initialCapacity());
        assertEquals(10, cacheBuilder.maximumSize());
        assertEquals(15, cacheBuilder.maximumWeight());
        assertEquals(2, cacheBuilder.concurrencyLevel());
        assertEquals(Cache.Builder.KeyStrength.WEAK, cacheBuilder.weakKeys());
        assertEquals(Cache.Builder.ValueStrength.SOFT, cacheBuilder.softValues());
        assertTrue(cacheBuilder.recordStats());
        assertEquals(5, cacheBuilder.expireAfterAccess(TimeUnit.MINUTES));
        assertEquals(1, cacheBuilder.expireAfterWrite(1, TimeUnit.HOURS).getExpireAfterWriteMultplier());
        assertEquals(2, cacheBuilder.refreshAfterWrite(2, TimeUnit.DAYS).getRefreshAfterWritePeriod());
    }

    @Test
    public void testParseWithInvalidValueParser() {
        String cacheBuilderSpecification = "invalidKey=invalidValue";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpecification);
            fail("Should throw IllegalArgumentException for invalid key-value pair");
        } catch (IllegalArgumentException e) {
            assertEquals("unknown key invalidKey", e.getMessage());
        }
    }

    @Test
    public void testParseNegativeValues() {
        String cacheBuilderSpecification = "maximumSize=-5";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpecification);
            fail("Should throw IllegalArgumentException for negative maximumSize");
        } catch (IllegalArgumentException e) {
            assertEquals("maximumSize must be non-negative", e.getMessage());
        }

        cacheBuilderSpecification = "maximumWeight=-15";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpecification);
            fail("Should throw IllegalArgumentException for negative maximumWeight");
        } catch (IllegalArgumentException e) {
            assertEquals("maximumWeight must be non-negative", e.getMessage());
        }

        cacheBuilderSpecification = "concurrencyLevel=-2";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpecification);
            fail("Should throw IllegalArgumentException for negative concurrencyLevel");
        } catch (IllegalArgumentException e) {
            assertEquals("concurrencyLevel must be non-negative", e.getMessage());
        }
    }
}