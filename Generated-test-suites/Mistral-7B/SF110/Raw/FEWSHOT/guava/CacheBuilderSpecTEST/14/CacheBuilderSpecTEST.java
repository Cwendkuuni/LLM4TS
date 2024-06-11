package com.google.common.cache;

import static org.junit.Assert.*;

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.AccessDurationParser;
import com.google.common.cache.CacheBuilderSpec.ConcurrencyLevelParser;
import com.google.common.cache.CacheBuilderSpec.InitialCapacityParser;
import com.google.common.cache.CacheBuilderSpec.KeyStrengthParser;
import com.google.common.cache.CacheBuilderSpec.MaximumSizeParser;
import com.google.common.cache.CacheBuilderSpec.MaximumWeightParser;
import com.google.common.cache.CacheBuilderSpec.RecordStatsParser;
import com.google.common.cache.CacheBuilderSpec.RefreshDurationParser;
import com.google.common.cache.CacheBuilderSpec.ValueStrengthParser;
import com.google.common.cache.CacheBuilderSpec.WriteDurationParser;
import com.google.common.collect.ImmutableMap;
import java.util.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_emptySpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertEquals(0, spec.getInitialCapacity());
        assertEquals(null, spec.getMaximumSize());
        assertEquals(null, spec.getMaximumWeight());
        assertEquals(null, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.STRONG, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.STRONG, spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertEquals(0, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(0, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(0, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_initialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5");
        assertEquals(5, spec.getInitialCapacity());
        assertEquals(null, spec.getMaximumSize());
        assertEquals(null, spec.getMaximumWeight());
        assertEquals(null, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.STRONG, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.STRONG, spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertEquals(0, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(0, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(0, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_maximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=10");
        assertEquals(0, spec.getInitialCapacity());
        assertEquals(10, spec.getMaximumSize());
        assertEquals(null, spec.getMaximumWeight());
        assertEquals(null, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.STRONG, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.STRONG, spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertEquals(0, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(0, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(0, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_maximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=20");
        assertEquals(0, spec.getInitialCapacity());
        assertEquals(null, spec.getMaximumSize());
        assertEquals(20, spec.getMaximumWeight());
        assertEquals(null, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.STRONG, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.STRONG, spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertEquals(0, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(0, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(0, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_concurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=3");
        assertEquals(0, spec.getInitialCapacity());
        assertEquals(null, spec.getMaximumSize());
        assertEquals(null, spec.getMaximumWeight());
        assertEquals(3, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.STRONG, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.STRONG, spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertEquals(0, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(0, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(0, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_weakKeys() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(0, spec.getInitialCapacity());
        assertEquals(null, spec.getMaximumSize());
        assertEquals(null, spec.getMaximumWeight());
        assertEquals(null, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.STRONG, spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertEquals(0, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(0, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(0, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_softValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        assertEquals(0, spec.getInitialCapacity());
        assertEquals(null, spec.getMaximumSize());
        assertEquals(null, spec.getMaximumWeight());
        assertEquals(null, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.STRONG, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertEquals(0, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(0, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(0, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_weakValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues");
        assertEquals(0, spec.getInitialCapacity());
        assertEquals(null, spec.getMaximumSize());
        assertEquals(null, spec.getMaximumWeight());
        assertEquals(null, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.STRONG, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.WEAK, spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertEquals(0, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(0, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(0, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_recordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats=true");
        assertEquals(0, spec.getInitialCapacity());
        assertEquals(null, spec.getMaximumSize());
        assertEquals(null, spec.getMaximumWeight());
        assertEquals(null, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.STRONG, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.STRONG, spec.getValueStrength());
        assertTrue(spec.isRecordStats());
        assertEquals(0, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(0, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(0, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_writeExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=1d");
        assertEquals(0, spec.getInitialCapacity());
        assertEquals(null, spec.getMaximumSize());
        assertEquals(null, spec.getMaximumWeight());
        assertEquals(null, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.STRONG, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.STRONG, spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertEquals(TimeUnit.DAYS.toMillis(1), spec.getWriteExpirationDuration());
        assertEquals(TimeUnit.DAYS, spec.getWriteExpirationTimeUnit());
        assertEquals(0, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(0, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_accessExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1h");
        assertEquals(0, spec.getInitialCapacity());
        assertEquals(null, spec.getMaximumSize());
        assertEquals(null, spec.getMaximumWeight());
        assertEquals(null, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.STRONG, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.STRONG, spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertEquals(0, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(TimeUnit.HOURS.toMillis(1), spec.getAccessExpirationDuration());
        assertEquals(TimeUnit.HOURS, spec.getAccessExpirationTimeUnit());
        assertEquals(0, spec.getRefreshDuration());
        assertEquals(null, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_refreshExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=1h");
        assertEquals(0, spec.getInitialCapacity());
        assertEquals(null, spec.getMaximumSize());
        assertEquals(null, spec.getMaximumWeight());
        assertEquals(null, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.STRONG, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.STRONG, spec.getValueStrength());
        assertFalse(spec.isRecordStats());
        assertEquals(0, spec.getWriteExpirationDuration());
        assertEquals(null, spec.getWriteExpirationTimeUnit());
        assertEquals(0, spec.getAccessExpirationDuration());
        assertEquals(null, spec.getAccessExpirationTimeUnit());
        assertEquals(TimeUnit.HOURS.toMillis(1), spec.getRefreshDuration());
        assertEquals(TimeUnit.HOURS, spec.getRefreshTimeUnit());
    }

    @Test
    public void testParse_combined() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5" +
                " maximumSize=10" +
                " maximumWeight=20" +
                " concurrencyLevel=3" +
                " weakKeys" +
                " softValues" +
                " recordStats=true" +
                " expireAfterWrite=1d" +
                " expireAfterAccess=1h" +
                " refreshAfterWrite=1h");
        assertEquals(5, spec.getInitialCapacity());
        assertEquals(10, spec.getMaximumSize());
        assertEquals(20, spec.getMaximumWeight());
        assertEquals(3, spec.getConcurrencyLevel());
        assertEquals(LocalCache.Strength.WEAK, spec.getKeyStrength());
        assertEquals(LocalCache.Strength.SOFT, spec.getValueStrength());
        assertTrue(spec.isRecordStats());
        assertEquals(TimeUnit.DAYS.toMillis(1), spec.getWriteExpirationDuration());
        assertEquals(TimeUnit.DAYS, spec.getWriteExpirationTimeUnit());
        assertEquals(TimeUnit.HOURS.toMillis(1), spec.getAccessExpirationDuration());
        assertEquals(TimeUnit.HOURS, spec.getAccessExpirationTimeUnit());
        assertEquals(TimeUnit.HOURS.toMillis(1), spec.getRefreshDuration());
        assertEquals(TimeUnit.HOURS, spec.getRefreshTimeUnit());
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=5" +
                " maximumSize=10" +
                " maximumWeight=20" +
                " concurrencyLevel=3" +
                " weakKeys" +
                " softValues" +
                " recordStats=true" +
                " expireAfterWrite=1d" +
                " expireAfterAccess=1h" +
                " refreshAfterWrite=1h");
        assertEquals("initialCapacity=5" +
                " maximumSize=10" +
                " maximumWeight=20" +
                " concurrencyLevel=3" +
                " weakKeys" +
                " softValues" +
                " recordStats=true" +
                " expireAfterWrite=1d" +
                " expireAfterAccess=1h" +
                " refreshAfterWrite=1h", spec.toParsableString());
    }
}