package com.google.common.cache;

import com.google.common.cache.*;
import com.google.common.cache.CacheBuilderSpec.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

 @BeforeClass
 public static void setUp() {
 System.out.println("--- CacheBuilderSpec tests ---");
 }

 @AfterClass
 public static void tearDown() {
 System.out.println("--- CacheBuilderSpec tests completed ---");
 }

 @Test
 public void testParseEmptySpec() {
 final String emptySpec = "";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(emptySpec);
 final CacheBuilder<Object, Object> builder = parsedSpec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder(), builder);
 }

 @Test
 public void testParseBasicSpec() {
 final String basicSpec = "initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, softValues, expireAfterWrite=1h, expireAfterAccess=30m";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(basicSpec);
 final CacheBuilder<Object, Object> builder = parsedSpec.toCacheBuilder();

 assertEquals(10, builder.initialCapacity());
 assertEquals(20, builder.maximumSize());
 assertEquals(5, builder.concurrencyLevel());
 assertEquals(AccessMode.WEAK, builder.keyStrength());
 assertEquals(AccessMode.SOFT, builder.valueStrength());
 assertEquals(Duration.ONE_HOUR, builder.expireAfterWrite());
 assertEquals(Duration.THIRTY_MINUTES, builder.expireAfterAccess());
 }

 @Test
 public void testParseInvalidSpec() {
 final String invalidSpec = "invalid=key, maximumSize=invalid";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(invalidSpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseMissingValueSpec() {
 final String missingValueSpec = "initialCapacity=10, maximumSize=";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(missingValueSpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseMissingKeySpec() {
 final String missingKeySpec = "maximumSize=20,";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(missingKeySpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseUnknownKeySpec() {
 final String unknownKeySpec = "unknownKey=value";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(unknownKeySpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseInvalidValueSpec() {
 final String invalidValueSpec = "invalidKey=invalidValue";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(invalidValueSpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseInvalidDurationSpec() {
 final String invalidDurationSpec = "expireAfterWrite=invalidDuration";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(invalidDurationSpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseNegativeInitialCapacity() {
 final String negativeInitialCapacitySpec = "initialCapacity=-1";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(negativeInitialCapacitySpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseNegativeMaximumSize() {
 final String negativeMaximumSizeSpec = "maximumSize=-1";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(negativeMaximumSizeSpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseNegativeConcurrencyLevel() {
 final String negativeConcurrencyLevelSpec = "concurrencyLevel=-1";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(negativeConcurrencyLevelSpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseUnknownDurationUnit() {
 final String unknownDurationUnitSpec = "expireAfterWrite=1hm";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(unknownDurationUnitSpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseZeroInitialCapacity() {
 final String zeroInitialCapacitySpec = "initialCapacity=0";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(zeroInitialCapacitySpec);
 final CacheBuilder<Object, Object> builder = parsedSpec.toCacheBuilder();
 assertEquals(0, builder.initialCapacity());
 }

 @Test
 public void testParseZeroMaximumSize() {
 final String zeroMaximumSizeSpec = "maximumSize=0";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(zeroMaximumSizeSpec);
 final CacheBuilder<Object, Object> builder = parsedSpec.toCacheBuilder();
 assertEquals(0, builder.maximumSize());
 }

 @Test
 public void testParseZeroConcurrencyLevel() {
 final String zeroConcurrencyLevelSpec = "concurrencyLevel=0";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(zeroConcurrencyLevelSpec);
 final CacheBuilder<Object, Object> builder = parsedSpec.toCacheBuilder();
 assertEquals(0, builder.concurrencyLevel());
 }

 @Test
 public void testParseMaximumSizeGreaterThanMaximumWeight() {
 final String maxSizeGreaterThanWeightSpec = "maximumSize=10, maximumWeight=5";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(maxSizeGreaterThanWeightSpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseMaximumSizeEqualToMaximumWeight() {
 final String maxSizeEqualWeightSpec = "maximumSize=5, maximumWeight=5";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(maxSizeEqualWeightSpec);
 final CacheBuilder<Object, Object> builder = parsedSpec.toCacheBuilder();
 assertEquals(5, builder.maximumSize());
 assertEquals(5, builder.maximumWeight());
 }

 @Test
 public void testParseConcurrencyLevelGreaterThanInitialCapacity() {
 final String concurrencyLevelGreaterThanInitialCapacitySpec = "concurrencyLevel=10, initialCapacity=5";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(concurrencyLevelGreaterThanInitialCapacitySpec);
 assertNull(parsedSpec);
 }

 @Test
 public void testParseSingleKeysOrValuesParser() {
 final String singleKeysParserSpec = "weakKeys";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(singleKeysParserSpec);
 assertEquals(AccessMode.WEAK, parsedSpec.keyStrength());
 }

 @Test
 public void testParseSingleValuesParser() {
 final String singleValuesParserSpec = "softValues";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(singleValuesParserSpec);
 assertEquals(AccessMode.SOFT, parsedSpec.valueStrength());
 }

 @Test
 public void testParseRecordStatsParser() {
 final String recordStatsParserSpec = "recordStats";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(recordStatsParserSpec);
 assertTrue(parsedSpec.recordStats());
 }

 @Test
 public void testParseExpireAfterWriteParser() {
 final String expireAfterWriteParserSpec = "expireAfterWrite=1h";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(expireAfterWriteParserSpec);
 final Duration expireAfterWrite = parsedSpec.writeExpirationTimeUnit() != null ? parsedSpec.writeExpirationDuration() : null;
 assertEquals(Duration.ONE_HOUR, expireAfterWrite);
 }

 @Test
 public void testParseExpireAfterAccessParser() {
 final String expireAfterAccessParserSpec = "expireAfterAccess=30m";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(expireAfterAccessParserSpec);
 final Duration expireAfterAccess = parsedSpec.accessExpirationTimeUnit() != null ? parsedSpec.accessExpirationDuration() : null;
 assertEquals(Duration.THIRTY_MINUTES, expireAfterAccess);
 }

 @Test
 public void testParseRefreshAfterWriteParser() {
 final String refreshAfterWriteParserSpec = "refreshAfterWrite=1h";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(refreshAfterWriteParserSpec);
 final Duration refreshAfterWrite = parsedSpec.refreshTimeUnit() != null ? parsedSpec.refreshDuration() : null;
 assertEquals(Duration.ONE_HOUR, refreshAfterWrite);
 }

 @Test
 public void testParseCacheBuilder() {
 final String cacheBuilderSpec = "initialCapacity=10, maximumSize=20, concurrencyLevel=5, weakKeys, softValues, expireAfterWrite=1h, expireAfterAccess=30m, recordStats, refreshAfterWrite=1m";
 final CacheBuilderSpec parsedSpec = CacheBuilderSpec.parse(cacheBuilderSpec);
 final CacheBuilder<Object, Object> builder = parsedSpec.toCacheBuilder();

 assertEquals(10, builder.initialCapacity());
 assertEquals(20, builder.maximumSize());
 assertEquals(5, builder.concurrencyLevel());
 assertEquals(AccessMode.WEAK, builder.keyStrength());
 assertEquals(AccessMode.SOFT, builder.valueStrength());
 assertEquals(Duration.ONE_HOUR, builder.expireAfterWrite());
 assertEquals(Duration.THIRTY_MINUTES, builder.expireAfterAccess());
 assertEquals(true, builder.recordStats());
 assertEquals(Duration.ONE_MINUTE, builder.refreshAfterWrite());
 }

}