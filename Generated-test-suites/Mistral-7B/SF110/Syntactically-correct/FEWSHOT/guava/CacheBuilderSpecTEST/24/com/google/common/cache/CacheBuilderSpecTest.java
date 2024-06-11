package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.annotations.*;
import java.util.concurrent.*;
import com.google.common.collect.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;

@Beta
public class CacheBuilderSpecTest {

    @Test
    public void testParseInitialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5");
        assertEquals(5, spec.getInitialCapacity());
    }

    @Test
    public void testParseMaximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10");
        assertEquals(10, spec.getMaximumSize());
    }

    @Test
    public void testParseMaximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=20");
        assertEquals(20, spec.getMaximumWeight());
    }

    @Test
    public void testParseConcurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=3");
        assertEquals(3, spec.getConcurrencyLevel());
    }

    @Test
    public void testParseWeakKeys() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
    }

    @Test
    public void testParseSoftValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
    }

    @Test
    public void testParseWeakValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues");
        assertEquals(LocalCache.Strength.WEAK, spec.getValueStrength());
    }

    @Test
    public void testParseRecordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats");
        assertTrue(spec.isRecordStats());
    }

    @Test
    public void testParseExpireAfterAccess() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=5s");
        assertEquals(5, spec.getAccessExpirationDuration());
        assertEquals(TimeUnit.SECONDS, spec.getAccessExpirationTimeUnit());
    }

    @Test
    public void testParseExpireAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=10m");
        assertEquals(10, spec.getWriteExpirationDuration());
        assertEquals(TimeUnit.MINUTES, spec.getWriteExpirationTimeUnit());
    }

    @Test
    public void testParseRefreshAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=3h");
        assertEquals(3, spec.getRefreshDuration());
        assertEquals(TimeUnit.HOURS, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParseInvalidSpec() {
        String invalidSpec = "invalidSpec=5,invalidDuration=10s";
        try {
            CacheBuilderSpec.parse(invalidSpec);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseNullSpec() {
        try {
            CacheBuilderSpec.parse(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testParseEmptySpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertEquals(0, spec.getInitialCapacity());
        assertNull(spec.getMaximumSize());
        assertNull(spec.getMaximumWeight());
        assertNull(spec.getConcurrencyLevel());
        assertNull(spec.getKeyStrength());
        assertNull(spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertNull(spec.getWriteExpirationDuration());
        assertNull(spec.getWriteExpirationTimeUnit());
        assertNull(spec.getAccessExpirationDuration());
        assertNull(spec.getAccessExpirationTimeUnit());
        assertNull(spec.getRefreshDuration());
        assertNull(spec.getRefreshTimeUnit());
    }
}