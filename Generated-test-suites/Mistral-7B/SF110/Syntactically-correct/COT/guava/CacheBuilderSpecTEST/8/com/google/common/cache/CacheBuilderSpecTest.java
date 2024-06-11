package com.example.cachebuilder; // replace with your package name

import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.LocalCache;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        final String cacheBuilderSpecification = "initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, expireAfterAccess=1d, expireAfterWrite=2h";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        Assert.assertEquals(10, spec.initialCapacity);
        Assert.assertEquals(20, spec.maximumSize);
        Assert.assertEquals(5, spec.concurrencyLevel);
        Assert.assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
        Assert.assertEquals(null, spec.valueStrength);
        Assert.assertEquals(true, spec.recordStats);
        Assert.assertEquals(1, spec.writeExpirationDuration);
        Assert.assertEquals(TimeUnit.DAYS, spec.writeExpirationTimeUnit);
        Assert.assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
        Assert.assertEquals(1, spec.accessExpirationDuration);
        Assert.assertNull(spec.refreshDuration);
        Assert.assertNull(spec.refreshTimeUnit);

        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().initialCapacity(10).maximumSize(20).concurrencyLevel(5).weakKeys().recordStats().expireAfterAccess(1, TimeUnit.DAYS).expireAfterWrite(2, TimeUnit.HOURS).build(), cacheBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidSpecification() {
        final String cacheBuilderSpecification = "invalidKey=invalidValue";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
    }

    @Test
    public void testToParsableString() {
        final String cacheBuilderSpecification = "initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, expireAfterAccess=1d, expireAfterWrite=2h, refreshAfterWrite=3m";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        String parsedSpec = spec.toParsableString();
        Assert.assertEquals(cacheBuilderSpecification, parsedSpec);
    }

    @Test
    public void testToCacheBuilderWithNoConfigurations() {
        final CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().build(), cacheBuilder);
    }

    @Test
    public void testToCacheBuilderWithInitialCapacity() {
        final String cacheBuilderSpecification = "initialCapacity=10";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().initialCapacity(10).build(), cacheBuilder);
    }

    @Test
    public void testToCacheBuilderWithMaximumSize() {
        final String cacheBuilderSpecification = "maximumSize=10";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().maximumSize(10).build(), cacheBuilder);
    }

    @Test
    public void testToCacheBuilderWithMaximumWeight() {
        final String cacheBuilderSpecification = "maximumWeight=10";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().maximumWeight(10).build(), cacheBuilder);
    }

    @Test
    public void testToCacheBuilderWithConcurrencyLevel() {
        final String cacheBuilderSpecification = "concurrencyLevel=10";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().concurrencyLevel(10).build(), cacheBuilder);
    }

    @Test
    public void testToCacheBuilderWithWeakKeys() {
        final String cacheBuilderSpecification = "weakKeys";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().weakKeys().build(), cacheBuilder);
    }

    @Test
    public void testToCacheBuilderWithSoftValues() {
        final String cacheBuilderSpecification = "softValues";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().softValues().build(), cacheBuilder);
    }

    @Test
    public void testToCacheBuilderWithWeakValues() {
        final String cacheBuilderSpecification = "weakValues";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().weakValues().build(), cacheBuilder);
    }

    @Test
    public void testToCacheBuilderWithRecordStats() {
        final String cacheBuilderSpecification = "recordStats";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().recordStats().build(), cacheBuilder);
    }

    @Test
    public void testToCacheBuilderWithWriteExpiration() {
        final String cacheBuilderSpecification = "expireAfterWrite=1d";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(), cacheBuilder);
    }

    @Test
    public void testToCacheBuilderWithAccessExpiration() {
        final String cacheBuilderSpecification = "expireAfterAccess=1h";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.HOURS).build(), cacheBuilder);
    }

    @Test
    public void testToCacheBuilderWithRefreshExpiration() {
        final String cacheBuilderSpecification = "refreshAfterWrite=1m";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
        Assert.assertEquals(CacheBuilder.newBuilder().refreshAfterWrite(1, TimeUnit.MINUTES).build(), cacheBuilder);
    }
}