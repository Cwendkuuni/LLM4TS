package com.google.common.cache;

import static org.junit.Assert.*;

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
import com.google.common.cache.CacheBuilderSpec.ValueStrengthParser;
import com.google.common.cache.CacheBuilderSpec.WriteDurationParser;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Splitter;
import com.google.common.collect.Values;
import com.google.common.primitives.Longs;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=200, maximumWeight=300, concurrencyLevel=5, weakKeys(), softValues(), recordStats(), expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1m";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertEquals("Initial capacity was not set correctly", 10, spec.getInitialCapacity());
        assertEquals("Maximum size was not set correctly", 200, spec.getMaximumSize());
        assertEquals("Maximum weight was not set correctly", 300, spec.getMaximumWeight());
        assertEquals("Concurrency level was not set correctly", 5, spec.getConcurrencyLevel());
        assertEquals("Key strength was not set correctly", LocalCache.Strength.WEAK, spec.getKeyStrength());
        assertEquals("Value strength was not set correctly", LocalCache.Strength.SOFT, spec.getValueStrength());
        assertEquals("Record stats was not set correctly", true, spec.isRecordStats());
        assertEquals("Write expiration duration was not set correctly", 1 * 60 * 60 * 1000L, spec.getWriteExpirationDuration());
        assertEquals("Write expiration time unit was not set correctly", TimeUnit.HOURS, spec.getWriteExpirationTimeUnit());
        assertEquals("Access expiration duration was not set correctly", 1 * 60 * 60 * 1000L, spec.getAccessExpirationDuration());
        assertEquals("Access expiration time unit was not set correctly", TimeUnit.HOURS, spec.getAccessExpirationTimeUnit());
        assertEquals("Refresh duration was not set correctly", 1 * 60 * 1000L, spec.getRefreshDuration());
        assertEquals("Refresh time unit was not set correctly", TimeUnit.MINUTES, spec.getRefreshTimeUnit());
        assertEquals("Maximum size and maximum weight were not set correctly", 200, spec.getMaximumSize());
        assertEquals("Maximum weight and maximum size were not set correctly", 300, spec.getMaximumWeight());

        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();

        // You can add more tests here to validate the cache builder.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidSpecification() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=200, maximumWeight=300, concurrencyLevel=5, weakKeys,, softValues(), recordStats(), expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1m, invalidKey=value";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidKeyValuePair() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=200, maximumWeight=300, concurrencyLevel=5, weakKeys=invalidValue, softValues(), recordStats(), expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1m";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
    }

    @Test
    public void testToParsableString() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=200, maximumWeight=300, concurrencyLevel=5, weakKeys(), softValues(), recordStats(), expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1m";
        CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
        assertEquals("The parsed string does not match the original", cacheBuilderSpecification, spec.toParsableString());
    }

    @Test
    public void testToString() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=200, maximumWeight=300, concurrencyLevel=5, weakKeys(), softValues(), recordStats(), expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1m";
        CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
        assertEquals("The toString method does not match the original specification", cacheBuilderSpecification, spec.toString());
    }

    @Test
    public void testHashCodeAndEquals() {
        String cacheBuilderSpecification1 = "initialCapacity=10, maximumSize=200, maximumWeight=300, concurrencyLevel=5, weakKeys(), softValues(), recordStats(), expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1m";
        String cacheBuilderSpecification2 = "initialCapacity=10, maximumSize=200, maximumWeight=300, concurrencyLevel=5, weakKeys(), softValues(), recordStats(), expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1m";

        CacheBuilderSpec spec1 = new CacheBuilderSpec(cacheBuilderSpecification1);
        CacheBuilderSpec spec2 = new CacheBuilderSpec(cacheBuilderSpecification2);

        assertEquals("The hashcodes are not equal", spec1.hashCode(), spec2.hashCode());
        assertTrue("The equals method does not match", spec1.equals(spec2));

        String cacheBuilderSpecification3 = "initialCapacity=11, maximumSize=200, maximumWeight=300, concurrencyLevel=5, weakKeys(), softValues(), recordStats(), expireAfterAccess=1h, expireAfterWrite=1d, refreshAfterWrite=1m";

        CacheBuilderSpec spec3 = new CacheBuilderSpec(cacheBuilderSpecification3);

        assertFalse("The equals method does not match", spec1.equals(spec3));
        assertFalse("The hashcodes are not different", spec1.hashCode(), spec3.hashCode());
    }

    @Test
    public void testValueParsers() {
        List<CacheBuilderSpec.ValueParser> valueParsers = Lists.newArrayList(
                new InitialCapacityParser(),
                new MaximumSizeParser(),
                new MaximumWeightParser(),
                new ConcurrencyLevelParser(),
                new KeyStrengthParser(LocalCache.Strength.WEAK),
                new ValueStrengthParser(LocalCache.Strength.SOFT),
                new RecordStatsParser(),
                new AccessDurationParser(),
                new WriteDurationParser(),
                new RefreshDurationParser());

        for (CacheBuilderSpec.ValueParser valueParser : valueParsers) {
            assertEquals("The parser did not parse the given values correctly", 10, valueParser.parse(null, "10").getValue());
            assertEquals("The parser did not parse the given values correctly", 200L, valueParser.parse(null, "200").getValue());
            assertEquals("The parser did not parse the given values correctly", 5, valueParser.parse(null, "5").getValue());
            assertEquals("The parser did not parse the given values correctly", LocalCache.Strength.WEAK, valueParser.parse(null, "weakKeys").getKey());
            assertEquals("The parser did not parse the given values correctly", LocalCache.Strength.SOFT, valueParser.parse(null, "softValues").getKey());
            assertEquals("The parser did not parse the given values correctly", true, valueParser.parse(null, "recordStats").getValue());
            assertEquals("The parser did not parse the given values correctly", 1 * 60 * 60 * 1000L, valueParser.parse(null, "1h").getValue());
            assertEquals("The parser did not parse the given values correctly", TimeUnit.HOURS, valueParser.parse(null, "1h").getTimeUnit());
            assertEquals("The parser did not parse the given values correctly", 1 * 60 * 60 * 1000L, valueParser.parse(null, "1h").getValue());
            assertEquals("The parser did not parse the given values correctly", 1 * 60 * 1000L, valueParser.parse(null, "1m").getValue());
            assertEquals("The parser did not parse the given values correctly", TimeUnit.MINUTES, valueParser.parse(null, "1m").getTimeUnit());
            assertEquals("The parser did not parse the given values correctly", 1 * 60 * 1000L, valueParser.parse(null, "1m").getValue());
        }
    }

    static class ValueHolder<T> {
        T value;
        TimeUnit timeUnit;

        ValueHolder(T value, TimeUnit timeUnit) {
            this.value = value;
            this.timeUnit = timeUnit;
        }

        long getValue() {
            return timeUnit.toNanos(value);
        }

        TimeUnit getTimeUnit() {
            return timeUnit;
        }
    }

    static class InitialCapacityParser extends CacheBuilderSpec.IntegerParser {
        @Override
        protected void parseInteger(CacheBuilderSpec spec, int value) {
            spec.initialCapacity = value;
        }
    }

    static class MaximumSizeParser extends CacheBuilderSpec.LongParser {
        @Override
        protected void parseLong(CacheBuilderSpec spec, long value) {
            spec.maximumSize = value;
            spec.maximumWeight = null;
        }
    }

    static class MaximumWeightParser extends CacheBuilderSpec.LongParser {
        @Override
        protected void parseLong(CacheBuilderSpec spec, long value) {
            spec.maximumWeight = value;
            spec.maximumSize = null;
        }
    }

    static class ConcurrencyLevelParser extends CacheBuilderSpec.IntegerParser {
        @Override
        protected void parseInteger(CacheBuilderSpec spec, int value) {
            spec.concurrencyLevel = value;
        }
    }

    static class KeyStrengthParser implements CacheBuilderSpec.ValueParser {
        private final LocalCache.Strength strength;

        public KeyStrengthParser(LocalCache.Strength strength) {
            this.strength = strength;
        }

        @Override
        public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
            Preconditions.checkArgument(value == null, (Object)"Key %s does not take values", key);
            Preconditions.checkArgument(spec.keyStrength == null, "%s was already set to %s", key, spec.keyStrength);
            spec.keyStrength = this.strength;
        }
    }

    static class ValueStrengthParser implements CacheBuilderSpec.ValueParser {
        private final LocalCache.Strength strength;

        public ValueStrengthParser(LocalCache.Strength strength) {
            this.strength = strength;
        }

        @Override
        public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
            Preconditions.checkArgument(value == null, (Object)"Key %s does not take values", key);
            Preconditions.checkArgument(spec.valueStrength == null, "%s was already set to %s", key, spec.valueStrength);
            spec.valueStrength = this.strength;
        }
    }

    static class RecordStatsParser implements CacheBuilderSpec.ValueParser {
        @Override
        public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
            Preconditions.checkArgument(value == null, (Object)"recordStats does not take values");
            Preconditions.checkArgument(spec.recordStats == null, (Object)"recordStats already set");
            spec.recordStats = true;
        }
    }

    static class AccessDurationParser extends CacheBuilderSpec.DurationParser {
        @Override
        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            spec.accessExpirationDuration = duration;
            spec.accessExpirationTimeUnit = unit;
        }
    }

    static class WriteDurationParser extends CacheBuilderSpec.DurationParser {
        @Override
        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            spec.writeExpirationDuration = duration;
            spec.writeExpirationTimeUnit = unit;
        }
    }

    static class RefreshDurationParser extends CacheBuilderSpec.DurationParser {
        @Override
        protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
            spec.refreshDuration = duration;
            spec.refreshTimeUnit = unit;
        }
    }

    static class DurationParser implements CacheBuilderSpec.ValueParser {
        protected abstract void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit);

        @Override
        public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
            Preconditions.checkArgument(value != null && !value.isEmpty(), "value of key %s omitted", key);
            try {
                final char lastChar = value.charAt(value.length() - 1);
                TimeUnit timeUnit = null;
                switch (lastChar) {
                    case 'd': {
                        timeUnit = TimeUnit.DAYS;
                        break;
                    }
                    case 'h': {
                        timeUnit = TimeUnit.HOURS;
                        break;
                    }
                    case 'm': {
                        timeUnit = TimeUnit.MINUTES;
                        break;
                    }
                    case 's': {
                        timeUnit = TimeUnit.SECONDS;
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException(String.format("key %s invalid format.  was %s, must end with one of [dDhHmMsS]", key, value));
                    }
                }
                final long duration = Long.parseLong(value.substring(0, value.length() - 1));
                this.parseDuration(spec, duration, timeUnit);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", key, value));
            }
        }
    }
}

class SplitterDemo {
    public static void main(String[] args) {
        Splitter splitter = Splitter.on(',').trimResults();
        List<String> list = ImmutableList.copyOf(splitter.split("key1=value1,key2=value2"));
        for (String element : list) {
            System.out.println(element);
        }
    }
}