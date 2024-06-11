package com.google.common.cache;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.LocalCache;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

@Beta
public class CacheBuilderSpecTest {

    @Test
    public void testParseEmptySpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        assertThat(spec.initialCapacity).isNull();
        assertThat(spec.maximumSize).isNull();
        assertThat(spec.maximumWeight).isNull();
        assertThat(spec.concurrencyLevel).isNull();
        assertThat(spec.keyStrength).isNull();
        assertThat(spec.valueStrength).isNull();
        assertThat(spec.recordStats).isNull();
        assertThat(spec.writeExpirationDuration).isZero();
        assertThat(spec.writeExpirationTimeUnit).isNull();
        assertThat(spec.accessExpirationDuration).isZero();
        assertThat(spec.accessExpirationTimeUnit).isNull();
        assertThat(spec.refreshDuration).isZero();
        assertThat(spec.refreshTimeUnit).isNull();
    }

    @Test
    public void testParseBasicSpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=50");
        assertThat(spec.initialCapacity).isEqualTo(5);
        assertThat(spec.maximumSize).isEqualTo(10);
        assertThat(spec.maximumWeight).isEqualTo(50);
        assertThat(spec.concurrencyLevel).isNull();
        assertThat(spec.keyStrength).isNull();
        assertThat(spec.valueStrength).isNull();
        assertThat(spec.recordStats).isNull();
        assertThat(spec.writeExpirationDuration).isZero();
        assertThat(spec.writeExpirationTimeUnit).isNull();
        assertThat(spec.accessExpirationDuration).isZero();
        assertThat(spec.accessExpirationTimeUnit).isNull();
        assertThat(spec.refreshDuration).isZero();
        assertThat(spec.refreshTimeUnit).isNull();
    }

    @Test
    public void testParseWithAllValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=50," +
                "concurrencyLevel=3,keyStrength=WEAK,valueStrength=WEAK," +
                "recordStats=true,writeExpirationDuration=10s,writeExpirationTimeUnit=SECONDS," +
                "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES," +
                "refreshDuration=1h,refreshTimeUnit=HOURS");
        assertThat(spec.initialCapacity).isEqualTo(5);
        assertThat(spec.maximumSize).isEqualTo(10);
        assertThat(spec.maximumWeight).isEqualTo(50);
        assertThat(spec.concurrencyLevel).isEqualTo(3);
        assertThat(spec.keyStrength).isEqualTo(LocalCache.Strength.WEAK);
        assertThat(spec.valueStrength).isEqualTo(LocalCache.Strength.WEAK);
        assertThat(spec.recordStats).isTrue();
        assertThat(spec.writeExpirationDuration).isEqualTo(10);
        assertThat(spec.writeExpirationTimeUnit).isEqualTo(TimeUnit.SECONDS);
        assertThat(spec.accessExpirationDuration).isEqualTo(5 * 60);
        assertThat(spec.accessExpirationTimeUnit).isEqualTo(TimeUnit.MINUTES);
        assertThat(spec.refreshDuration).isEqualTo(1 * 60 * 60);
        assertThat(spec.refreshTimeUnit).isEqualTo(TimeUnit.HOURS);
    }

    @Test
    public void testParseInvalidSpec() {
        String invalidSpec = "initialCapacity=5,maximumSize=10,maximumWeight=50," +
                "concurrencyLevel=3,keyStrength=WEAK,valueStrength=WEAK," +
                "recordStats=true,writeExpirationDuration=10s,writeExpirationTimeUnit=SECONDS," +
                "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES," +
                "refreshDuration=1h,refreshTimeUnit=HOURS," +
                "expireAfterWrite=10s"; // extra key-value pair

        assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse(invalidSpec));

        String missingValueSpec = "initialCapacity=5,maximumSize=10," // missing value for maximumWeight
                + "concurrencyLevel=3,keyStrength=WEAK,valueStrength=WEAK," +
                "recordStats=true,writeExpirationDuration=10s,writeExpirationTimeUnit=SECONDS," +
                "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES," +
                "refreshDuration=1h,refreshTimeUnit=HOURS";

        assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse(missingValueSpec));

        String invalidValueTypeSpec = "initialCapacity=5,maximumSize=10,maximumWeight=apple," // invalid value type for maximumWeight
                + "concurrencyLevel=3,keyStrength=WEAK,valueStrength=WEAK," +
                "recordStats=true,writeExpirationDuration=10s,writeExpirationTimeUnit=SECONDS," +
                "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES," +
                "refreshDuration=1h,refreshTimeUnit=HOURS";

        assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse(invalidValueTypeSpec));

        String missingValueSeparatorSpec = "initialCapacity=5,maximumSize=10maximumWeight=50," // missing separator between key-value pair
                + "concurrencyLevel=3,keyStrength=WEAK,valueStrength=WEAK," +
                "recordStats=true,writeExpirationDuration=10s,writeExpirationTimeUnit=SECONDS," +
                "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES," +
                "refreshDuration=1h,refreshTimeUnit=HOURS";

        assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse(missingValueSeparatorSpec));

        String missingKeySpec = "initialCapacity=5," // missing key for maximumSize
                + "maximumSize=10maximumWeight=50,"
                + "concurrencyLevel=3,keyStrength=WEAK,valueStrength=WEAK," +
                "recordStats=true,writeExpirationDuration=10s,writeExpirationTimeUnit=SECONDS," +
                "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES," +
                "refreshDuration=1h,refreshTimeUnit=HOURS";

        assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse(missingKeySpec));

        String missingKeySpecWithComments = "initialCapacity=5," // missing key for maximumSize
                + "maximumSize=10maximumWeight=50," // valid key-value pair with a typo
                + "// this is a comment"
                + "concurrencyLevel=3,keyStrength=WEAK,valueStrength=WEAK," +
                "recordStats=true,writeExpirationDuration=10s,writeExpirationTimeUnit=SECONDS," +
                "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES," +
                "refreshDuration=1h,refreshTimeUnit=HOURS";

        assertThrows(IllegalArgumentException.class, () -> CacheBuilderSpec.parse(missingKeySpecWithComments));
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=50," +
                "concurrencyLevel=3,keyStrength=WEAK,valueStrength=WEAK," +
                "recordStats=true,writeExpirationDuration=10s,writeExpirationTimeUnit=SECONDS," +
                "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES," +
                "refreshDuration=1h,refreshTimeUnit=HOURS");

        ConcurrentMap<Object, Object> cache = spec.toCacheBuilder().build();

        assertThat(cache instanceof LocalCache).isTrue();
        assertThat(cache.getInitialCapacity()).isEqualTo(5);
        assertThat(cache.maximumSize()).isEqualTo(10);
        assertThat(cache.maximumWeight()).isEqualTo(50);
        assertThat(cache.concurrencyLevel()).isEqualTo(3);
        assertThat(cache.getOption(CacheBuilder.KeyStrengthOption.KEY_STRENGTH)).isEqualTo(LocalCache.Strength.WEAK);
        assertThat(cache.getOption(CacheBuilder.ValueStrengthOption.VALUE_STRENGTH)).isEqualTo(LocalCache.Strength.WEAK);
        assertThat(cache.recordStats()).isTrue();
        assertThat(cache.expireAfterAccess(10, TimeUnit.SECONDS)).isTrue();
        assertThat(cache.expireAfterWrite(5 * 60, TimeUnit.MINUTES)).isTrue();
        assertThat(cache.refreshAfterWrite(1, TimeUnit.HOURS)).isTrue();
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=50," +
                "concurrencyLevel=3,keyStrength=WEAK,valueStrength=WEAK," +
                "recordStats=true,writeExpirationDuration=10s,writeExpirationTimeUnit=SECONDS," +
                "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES," +
                "refreshDuration=1h,refreshTimeUnit=HOURS");
        String expectedSpec = "initialCapacity=5,maximumSize=10,maximumWeight=50," +
                "concurrencyLevel=3,keyStrength=WEAK,valueStrength=WEAK," +
                "recordStats=true,writeExpirationDuration=10s,writeExpirationTimeUnit=SECONDS," +
                "accessExpirationDuration=5m,accessExpirationTimeUnit=MINUTES," +
                "refreshDuration=1h,refreshTimeUnit=HOURS";

        String specString = spec.toParsableString();
        assertThat(specString).isEqualTo(expectedSpec);
    }
}