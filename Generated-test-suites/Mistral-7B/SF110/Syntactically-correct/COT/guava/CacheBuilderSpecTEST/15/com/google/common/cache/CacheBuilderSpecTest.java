package com.google.common.cache; // Assuming you have the necessary imports and helper classes

import static org.junit.Assert.*;
import static com.google.common.cache.CacheBuilderSpec.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CacheBuilderSpecTest {

    

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100, maximumWeight=100";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals("10", spec.initialCapacity.toString());
        assertEquals("100", spec.maximumSize.toString());
        assertEquals("100", spec.maximumWeight.toString());
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

        String emptyCacheBuilderSpecification = "";
        try {
            CacheBuilderSpec emptySpec = CacheBuilderSpec.parse(emptyCacheBuilderSpecification);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        String invalidCacheBuilderSpecification = "invalidKey=invalidValue";
        try {
            CacheBuilderSpec invalidSpec = CacheBuilderSpec.parse(invalidCacheBuilderSpecification);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToCacheBuilder() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100, maximumWeight=100";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        assertNotNull(cacheBuilder);

        assertFalse(spec.initialCapacity.isPresent());
        assertEquals("10", cacheBuilder.initialCapacity());

        assertFalse(spec.maximumSize.isPresent());
        assertEquals("100", cacheBuilder.maximumSize());

        assertFalse(spec.maximumWeight.isPresent());
        assertEquals("100", cacheBuilder.maximumWeight());

        assertFalse(spec.concurrencyLevel.isPresent());
        assertEquals(16, cacheBuilder.concurrencyLevel()); // Default value

        assertFalse(spec.keyStrength.isPresent());
        assertFalse(spec.valueStrength.isPresent());

        assertFalse(spec.recordStats.isPresent());
        assertFalse(cacheBuilder.recordStats());

        assertFalse(spec.writeExpirationTimeUnit.isPresent());
        assertFalse(spec.accessExpirationTimeUnit.isPresent());
        assertFalse(spec.refreshTimeUnit.isPresent());

        // Test expireAfterWrite
        spec = new CacheBuilderSpec("expireAfterWrite=1h");
        cacheBuilder = spec.toCacheBuilder();
        assertTrue(cacheBuilder.expireAfterWrite(1, TimeUnit.HOURS));

        // Test expireAfterAccess
        spec = new CacheBuilderSpec("expireAfterAccess=5m");
        cacheBuilder = spec.toCacheBuilder();
        assertTrue(cacheBuilder.expireAfterAccess(5, TimeUnit.MINUTES));

        // Test refreshAfterWrite
        spec = new CacheBuilderSpec("refreshAfterWrite=10s");
        cacheBuilder = spec.toCacheBuilder();
        assertTrue(cacheBuilder.refreshAfterWrite(10, TimeUnit.SECONDS));

        // Test weakKeys, softValues, weakValues
        spec = new CacheBuilderSpec("weakKeys");
        cacheBuilder = spec.toCacheBuilder();
        assertTrue(cacheBuilder.weakKeys());

        spec = new CacheBuilderSpec("softValues");
        cacheBuilder = spec.toCacheBuilder();
        assertTrue(cacheBuilder.softValues());

        spec = new CacheBuilderSpec("weakValues");
        cacheBuilder = spec.toCacheBuilder();
        assertTrue(cacheBuilder.weakValues());

        // Test recordStats
        spec = new CacheBuilderSpec("recordStats=true");
        cacheBuilder = spec.toCacheBuilder();
        assertTrue(cacheBuilder.recordStats());
    }

    @Test
    public void testToString() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100, maximumWeight=100";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        String expected = "CacheBuilderSpec{toParsableString='initialCapacity=10, maximumSize=100, maximumWeight=100'}";
        String actual = spec.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        String cacheBuilderSpecification1 = "initialCapacity=10, maximumSize=100, maximumWeight=100";
        String cacheBuilderSpecification2 = "initialCapacity=10, maximumSize=100, maximumWeight=100";
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheBuilderSpecification1);
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheBuilderSpecification2);
        assertEquals(spec1.hashCode(), spec2.hashCode());

        String cacheBuilderSpecification3 = "initialCapacity=10, maximumSize=100, maximumWeight=101";
        CacheBuilderSpec spec3 = CacheBuilderSpec.parse(cacheBuilderSpecification3);
        assertNotEquals(spec1.hashCode(), spec3.hashCode());
    }

    @Test
    public void testEquals() {
        String cacheBuilderSpecification1 = "initialCapacity=10, maximumSize=100, maximumWeight=100";
        String cacheBuilderSpecification2 = "initialCapacity=10, maximumSize=100, maximumWeight=100";
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheBuilderSpecification1);
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheBuilderSpecification2);
        assertTrue(spec1.equals(spec2));

        String cacheBuilderSpecification3 = "initialCapacity=10, maximumSize=101, maximumWeight=100";
        CacheBuilderSpec spec3 = CacheBuilderSpec.parse(cacheBuilderSpecification3);
        assertFalse(spec1.equals(spec3));
    }

    
}