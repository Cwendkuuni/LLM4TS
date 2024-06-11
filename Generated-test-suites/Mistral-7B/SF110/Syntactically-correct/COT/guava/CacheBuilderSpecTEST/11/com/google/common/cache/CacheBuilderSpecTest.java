package com.google.common.cache;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.AccessDurationParser;
import com.google.common.cache.CacheBuilderSpec.ConcurrencyLevelParser;
import com.google.common.cache.CacheBuilderSpec.DurationParser;
import com.google.common.cache.CacheBuilderSpec.InitialCapacityParser;
import com.google.common.cache.CacheBuilderSpec.KeyStrengthParser;
import com.google.common.cache.CacheBuilderSpec.MaximumSizeParser;
import com.google.common.cache.CacheBuilderSpec.MaximumWeightParser;
import com.google.common.cache.CacheBuilderSpec.RecordStatsParser;
import com.google.common.cache.CacheBuilderSpec.RefreshDurationParser;
import com.google.common.cache.CacheBuilderSpec.ValueStrengthParser;
import com.google.common.cache.CacheBuilderSpec.WriteDurationParser;

public class CacheBuilderSpecTest {

    @Test
    public void testParseBasic() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        assertEquals(10, cacheBuilder.initialCapacity());
        assertEquals(500, cacheBuilder.maximumSize());
        assertEquals(10, cacheBuilder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, cacheBuilder.keyStrength());
        assertEquals(LocalCache.Strength.SOFT, cacheBuilder.valueStrength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidKeyValuePair() {
        String cacheBuilderSpecification = "invalidKey=invalidValue";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
        fail("expected IllegalArgumentException");
    }

    @Test
    public void testParseDuplicateKey() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=500, maximumSize=100, concurrencyLevel=10, weakKeys, softValues";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertEquals(10, spec.initialCapacity);
        assertEquals(500, spec.maximumSize);
        assertEquals(10, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
    }

    @Test
    public void testParseEmptySpec() {
        String cacheBuilderSpecification = "";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        assertNull(spec.initialCapacity);
        assertNull(spec.maximumSize);
        assertNull(spec.maximumWeight);
        assertNull(spec.concurrencyLevel);
        assertNull(spec.keyStrength);
        assertNull(spec.valueStrength);
        assertNull(spec.recordStats);
        assertNull(spec.writeExpirationDuration);
        assertNull(spec.writeExpirationTimeUnit);
        assertNull(spec.accessExpirationDuration);
        assertNull(spec.accessExpirationTimeUnit);
        assertNull(spec.refreshDuration);
        assertNull(spec.refreshTimeUnit);
    }

    @Test
    public void testParseWithExpiration() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=1h";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        assertEquals(10, cacheBuilder.initialCapacity());
        assertEquals(500, cacheBuilder.maximumSize());
        assertEquals(10, cacheBuilder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, cacheBuilder.keyStrength());
        assertEquals(LocalCache.Strength.SOFT, cacheBuilder.valueStrength());
        assertEquals(1, cacheBuilder.expireAfterWrite(TimeUnit.HOURS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidExpirationFormat() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=invalid";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
        fail("expected IllegalArgumentException");
    }

    @Test
    public void testParseWithAccessExpiration() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterAccess=1m";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        assertEquals(10, cacheBuilder.initialCapacity());
        assertEquals(500, cacheBuilder.maximumSize());
        assertEquals(10, cacheBuilder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, cacheBuilder.keyStrength());
        assertEquals(LocalCache.Strength.SOFT, cacheBuilder.valueStrength());
        assertEquals(1, cacheBuilder.expireAfterAccess(TimeUnit.MINUTES));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidAccessExpirationFormat() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterAccess=invalid";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
        fail("expected IllegalArgumentException");
    }

    @Test
    public void testParseWithRefreshExpiration() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, refreshAfterWrite=1h";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        assertEquals(10, cacheBuilder.initialCapacity());
        assertEquals(500, cacheBuilder.maximumSize());
        assertEquals(10, cacheBuilder.concurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, cacheBuilder.keyStrength());
        assertEquals(LocalCache.Strength.SOFT, cacheBuilder.valueStrength());
        assertEquals(1, cacheBuilder.refreshAfterWrite(TimeUnit.HOURS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidRefreshExpirationFormat() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, refreshAfterWrite=invalid";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
        fail("expected IllegalArgumentException");
    }

    @Test
    public void testParseWithRecordStats() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, recordStats";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        assertTrue(cacheBuilder.recordStats());
    }

    @Test
    public void testParseWithNoExpiration() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        assertNull(cacheBuilder.expireAfterWrite(null));
        assertNull(cacheBuilder.expireAfterAccess(null));
        assertNull(cacheBuilder.refreshAfterWrite(null));
    }

    @Test
    public void testParseWithInvalidConstraints() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=-1, concurrencyLevel=5, weakKeys, softValues";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpecification);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("maximumSize cannot be negative", e.getMessage());
        }

        String cacheBuilderSpecification2 = "initialCapacity=10, maximumSize=500, concurrencyLevel=-1, weakKeys, softValues";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpecification2);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("concurrencyLevel cannot be negative", e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidConstraintsOrder() {
        String cacheBuilderSpecification = "maximumSize=-1, initialCapacity=10, concurrencyLevel=5, weakKeys, softValues";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpecification);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("maximumSize cannot be negative", e.getMessage());
        }

        String cacheBuilderSpecification2 = "concurrencyLevel=-1, initialCapacity=10, maximumSize=500, weakKeys, softValues";
        try {
            CacheBuilderSpec.parse(cacheBuilderSpecification2);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("concurrencyLevel cannot be negative", e.getMessage());
        }
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=1h");
        assertEquals("initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=1h", spec.toParsableString());
    }

    @Test
    public void testToString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=1h");
        String expectedToString = "CacheBuilderSpec{specification='initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=1h'}";
        assertEquals(expectedToString, spec.toString());
    }

    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=1h");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=1h");
        CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=20, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=1h");
        assertEquals(spec1.hashCode(), spec2.hashCode());
        assertNotEquals(spec1.hashCode(), spec3.hashCode());
    }

    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=1h");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=1h");
        CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=20, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, expireAfterWrite=1h");
        assertTrue(spec1.equals(spec2));
        assertFalse(spec1.equals(spec3));
        assertNull(spec1.equals(null));
    }

    @Test
    public void testParseWithRecordStatsAndExpiration() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=500, concurrencyLevel=10, weakKeys, softValues, recordStats, expireAfterWrite=1h";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        assertTrue(cacheBuilder.recordStats());
        assertEquals(1, cacheBuilder.expireAfterWrite(TimeUnit.HOURS));
    }
}