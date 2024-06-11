package com.example.cachebuilder; // replace with your test package name

import static org.junit.Assert.*;
import com.google.common.cache.*;
import com.google.common.collect.*;
import com.google.common.base.*;
import org.junit.Test;
import java.util.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, weakValues, recordStats=true, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");

        assertEquals(5, spec.initialCapacity);
        assertEquals(10, spec.maximumSize);
        assertEquals(20, spec.maximumWeight);
        assertEquals(3, spec.concurrencyLevel);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(1, spec.accessExpirationTimeUnit.toHours(spec.accessExpirationDuration));
        assertEquals(2, spec.writeExpirationTimeUnit.toDays(spec.writeExpirationDuration));
        assertEquals(30, spec.refreshTimeUnit.toSeconds(spec.refreshDuration));

        assertNull(spec.toCacheBuilder().maximumSize());
        assertEquals(5, spec.toCacheBuilder().initialCapacity());
        assertEquals(3, spec.toCacheBuilder().concurrencyLevel());
        assertTrue(spec.toCacheBuilder().recordStats());
        assertNull(spec.toCacheBuilder().weakKeys());
        assertNull(spec.toCacheBuilder().weakValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFormat() {
        CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, weakValues, recordStats=true, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s, invalidKey");
    }

    @Test
    public void testParseWithoutSpecification() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
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
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, weakValues, recordStats=true, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
        assertEquals(spec.toParsableString(), "initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, weakValues, recordStats=true, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
    }

    @Test
    public void testToString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, weakValues, recordStats=true, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
        assertEquals(spec.toString(), MoreObjects.toStringHelper(spec).addValue(spec.toParsableString()).toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, weakValues, recordStats=true, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, weakValues, recordStats=true, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=30s");

        assertEquals(spec1, spec2);

        CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=5, maximumSize=10, maximumWeight=20, concurrencyLevel=3, weakKeys, weakValues, recordStats=true, expireAfterAccess=1h, expireAfterWrite=2d, refreshAfterWrite=31s");

        assertNotEquals(spec1, spec3);
    }
}