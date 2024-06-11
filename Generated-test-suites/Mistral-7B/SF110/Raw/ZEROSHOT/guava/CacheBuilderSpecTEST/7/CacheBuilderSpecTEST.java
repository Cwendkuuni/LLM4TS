package com.google.common.cache;

import static org.junit.Assert.*;
import com.google.common.cache.*;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.Arrays;
import java.util.List;
import java.time.Duration;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_EmptySpecification_ReturnsDefaultSpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse(" ");
        assertEquals(CacheBuilder.newBuilder(), spec.toCacheBuilder());
    }

    @Test
    public void testParse_SimpleSpecification_SetsCorrectProperties() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5, maximumSize=10, concurrencyLevel=3");
        assertEquals(5, spec.initialCapacity);
        assertEquals(10, spec.maximumSize);
        assertEquals(3, spec.concurrencyLevel);
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
    public void testParse_ComplexSpecification_SetsCorrectProperties() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100, maximumWeight=500, initialCapacity=25," +
                "weakKeys(), concurrencyLevel=5, expireAfterAccess=2d, expireAfterWrite=30m, recordStats, refreshAfterWrite=1h");
        assertEquals(100, spec.maximumSize);
        assertEquals(500, spec.maximumWeight);
        assertEquals(25, spec.initialCapacity);
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
        assertTrue(spec.recordStats);
        assertEquals(Duration.ofDays(2), spec.accessExpirationDuration, spec.accessExpirationTimeUnit);
        assertEquals(Duration.ofMinutes(30), spec.writeExpirationDuration, spec.writeExpirationTimeUnit);
        assertEquals(Duration.ofHours(1), spec.refreshDuration, spec.refreshTimeUnit);
        assertEquals(CacheBuilder.newBuilder().maximumSize(100).maximumWeight(500).initialCapacity(25)
                .weakKeys().concurrencyLevel(5)
                .expireAfterAccess(Duration.ofDays(2))
                .expireAfterWrite(Duration.ofMinutes(30))
                .recordStats()
                .refreshAfterWrite(Duration.ofHours(1)), spec.toCacheBuilder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidSpecification_ThrowsException() {
        CacheBuilderSpec.parse("invalid=key");
    }
}