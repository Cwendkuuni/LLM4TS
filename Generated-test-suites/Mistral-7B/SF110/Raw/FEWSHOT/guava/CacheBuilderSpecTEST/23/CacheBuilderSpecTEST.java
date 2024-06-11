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
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=10");
        assertEquals(10, spec.getInitialCapacity());
    }

    @Test
    public void testParseMaximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=50");
        assertEquals(50, spec.getMaximumSize());
        assertNull(spec.getMaximumWeight());
    }

    @Test
    public void testParseMaximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=100");
        assertEquals(100, spec.getMaximumWeight());
        assertEquals(null, spec.getMaximumSize());
    }

    @Test
    public void testParseConcurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=5");
        assertEquals(5, spec.getConcurrencyLevel());
    }

    @Test
    public void testParseKeyStrengthWeak() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
        assertNull(spec.getValueStrength());
    }

    @Test
    public void testParseValueStrengthSoft() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
        assertNull(spec.getKeyStrength());
    }

    @Test
    public void testParseValueStrengthWeak() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues");
        assertEquals(LocalCache.Strength.WEAK, spec.getValueStrength());
        assertNull(spec.getKeyStrength());
    }

    @Test
    public void testParseRecordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats=true");
        assertTrue(spec.isRecordStats());
    }

    @Test
    public void testParseAccessExpirationDuration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1h");
        assertEquals(1L, spec.getAccessExpirationDuration().toHours());
        assertEquals(TimeUnit.HOURS, spec.getAccessExpirationTimeUnit());
    }

    @Test
    public void testParseAccessExpirationDurationWithSeconds() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1m30s");
        assertEquals(1L, spec.getAccessExpirationDuration().toMinutes());
        assertEquals(TimeUnit.MINUTES, spec.getAccessExpirationTimeUnit());
    }

    @Test
    public void testParseWriteExpirationDuration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=2h30m");
        assertEquals(2L, spec.getWriteExpirationDuration().toHours());
        assertEquals(TimeUnit.HOURS, spec.getWriteExpirationTimeUnit());
        assertEquals(30L, spec.getWriteExpirationDuration().toMinutes() % 60);
    }

    @Test
    public void testParseWriteExpirationDurationWithSecondsAndMilliseconds() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=1d1h45m30s");
        assertEquals(1L * 24 * 60 * 60 + 2L * 60 * 60 + 45L * 60 + 30L, spec.getWriteExpirationDuration().toSeconds());
        assertEquals(TimeUnit.SECONDS, spec.getWriteExpirationTimeUnit());
    }

    @Test
    public void testParseRefreshDuration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=5m");
        assertEquals(5L, spec.getRefreshDuration().toMinutes());
        assertEquals(TimeUnit.MINUTES, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParseInvalidSpec() {
        String invalidSpec = "invalidKey=invalidValue";
        try {
            CacheBuilderSpec.parse(invalidSpec);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("key invalidKey does not exist", e.getMessage());
        }
    }

    @Test
    public void testParseEmptySpec() {
        try {
            CacheBuilderSpec.parse("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("CacheBuilderSpec requires at least one key-value pair", e.getMessage());
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
    public void testParseBasicSpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5" +
                ", maximumSize=100" +
                ", maximumWeight=50" +
                ", concurrencyLevel=15" +
                ", weakKeys" +
                ", softValues" +
                ", expireAfterAccess=1h" +
                ", expireAfterWrite=30s" +
                ", refreshAfterWrite=1m");
        assertEquals(5, spec.getInitialCapacity());
        assertEquals(100, spec.getMaximumSize());
        assertEquals(50, spec.getMaximumWeight());
        assertEquals(15, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
        assertEquals(1L, spec.getAccessExpirationDuration().toHours());
        assertEquals(TimeUnit.HOURS, spec.getAccessExpirationTimeUnit());
        assertEquals(0L, spec.getWriteExpirationDuration().toSeconds());
        assertEquals(TimeUnit.SECONDS, spec.getWriteExpirationTimeUnit());
        assertEquals(1L, spec.getRefreshDuration().toMinutes());
        assertEquals(TimeUnit.MINUTES, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParseComplexSpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5" +
                ", maximumSize=100" +
                ", maximumWeight=50" +
                ", concurrencyLevel=15" +
                ", weakKeys" +
                ", softValues" +
                ", expireAfterAccess=3h" +
                ", expireAfterWrite=1d" +
                ", refreshAfterWrite=2h" +
                ", recordStats=true");
        assertEquals(5, spec.getInitialCapacity());
        assertEquals(100, spec.getMaximumSize());
        assertEquals(50, spec.getMaximumWeight());
        assertEquals(15, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
        assertEquals(3L * 60 * 60, spec.getAccessExpirationDuration().toSeconds());
        assertEquals(TimeUnit.SECONDS, spec.getAccessExpirationTimeUnit());
        assertEquals(24L * 60 * 60 * 60 + 1L * 24 * 60 * 60 + 24L * 60 * 60, spec.getWriteExpirationDuration().toSeconds());
        assertEquals(TimeUnit.SECONDS, spec.getWriteExpirationTimeUnit());
        assertEquals(2L * 60 * 60, spec.getRefreshDuration().toSeconds());
        assertEquals(TimeUnit.SECONDS, spec.getRefreshTimeUnit());
        assertTrue(spec.isRecordStats());
    }
}