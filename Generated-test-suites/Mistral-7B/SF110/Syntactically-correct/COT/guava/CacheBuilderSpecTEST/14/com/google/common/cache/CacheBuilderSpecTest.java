package com.example.cachebuilder; // replace with your test package name

import com.google.common.cache.CacheBuilderSpec;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=10, weakKeys, expireAfterAccess=1d, expireAfterWrite=2h";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        Assert.assertEquals(10, cacheBuilderSpec.getInitialCapacity());
        Assert.assertEquals(50, cacheBuilderSpec.getMaximumSize());
        Assert.assertEquals(10, cacheBuilderSpec.getConcurrencyLevel());
        Assert.assertEquals(LocalCache.Strength.WEAK, cacheBuilderSpec.getKeyStrength());
        Assert.assertEquals(LocalCache.Strength.NOT_PRESENT, cacheBuilderSpec.getValueStrength());
        Assert.assertTrue(cacheBuilderSpec.isRecordStats());
        Assert.assertEquals(1, cacheBuilderSpec.getWriteExpirationTimeUnit().toMinutes(cacheBuilderSpec.getWriteExpirationDuration())); // 2 hours
        Assert.assertEquals(1, cacheBuilderSpec.getAccessExpirationTimeUnit().toDays(cacheBuilderSpec.getAccessExpirationDuration())); // 1 day
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidSpecification() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=10, weakKeys, expireAfterAccess=1d, expireAfterWrite=2h, invalidKey=value";
        CacheBuilderSpec.parse(cacheBuilderSpecification);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.disableCaching();
        Assert.assertEquals(0, cacheBuilderSpec.getMaximumSize());
    }

    @Test
    public void testToCacheBuilder() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=50, concurrencyLevel=10, weakKeys, expireAfterAccess=1d, expireAfterWrite=2h";
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();

        // You can add more assertions here to test the cacheBuilder's properties
    }

    // Add more test cases for other methods and edge cases
}