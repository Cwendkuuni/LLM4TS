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
import com.google.common.cache.CacheBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

 @Test
 public void testParseWithValidSpec() {
 CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=3," +
 "keyStrength=weak,valueStrength=soft,recordStats=true," +
 "writeExpirationDuration=30s,writeExpirationTimeUnit=SECONDS," +
 "accessExpirationDuration=1h,accessExpirationTimeUnit=HOURS," +
 "refreshDuration=2d,refreshTimeUnit=DAYS");

 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder(), cacheBuilder); // Base case: no options set

 assertEquals(5, spec.initialCapacity);
 assertEquals(10, spec.maximumSize);
 assertEquals(20, spec.maximumWeight);
 assertEquals(3, spec.concurrencyLevel);
 assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 assertTrue(spec.recordStats);
 assertEquals(30, spec.writeExpirationDuration);
 assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
 assertEquals(3600, spec.accessExpirationDuration);
 assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
 assertEquals(2 * 24 * 3600, spec.refreshDuration);
 assertEquals(TimeUnit.DAYS, spec.refreshTimeUnit);
 }

 @Test
 public void testParseWithMissingSpec() {
 try {
 CacheBuilderSpec.parse("");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithInvalidSpec() {
 try {
 CacheBuilderSpec.parse("initialCapacity=ABC");
 fail("Expected NumberFormatException");
 } catch (NumberFormatException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithDuplicateKeys() {
 try {
 CacheBuilderSpec.parse("initialCapacity=5,maximumSize=10,maximumSize=20," +
 "keyStrength=weak,keyStrength=soft,recordStats=true");
 fail("Expected AssertionError");
 } catch (AssertionError e) {
 // Expected
 }
 }

 @Test
 public void testToParsableString() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.initialCapacity = 5;
 spec.maximumSize = 10;
 spec.maximumWeight = 20;
 spec.concurrencyLevel = 3;
 spec.keyStrength = LocalCache.Strength.WEAK;
 spec.valueStrength = LocalCache.Strength.SOFT;
 spec.recordStats = true;
 spec.writeExpirationDuration = 30;
 spec.writeExpirationTimeUnit = TimeUnit.SECONDS;
 spec.accessExpirationDuration = 3600;
 spec.accessExpirationTimeUnit = TimeUnit.HOURS;
 spec.refreshDuration = 2 * 24 * 3600;
 spec.refreshTimeUnit = TimeUnit.DAYS;

 String specString = spec.toParsableString();
 assertEquals("initialCapacity=5,maximumSize=10,maximumWeight=20,concurrencyLevel=3," +
 "keyStrength=weak,valueStrength=soft,recordStats=true," +
 "writeExpirationDuration=30s,writeExpirationTimeUnit=SECONDS," +
 "accessExpirationDuration=1h,accessExpirationTimeUnit=HOURS," +
 "refreshDuration=2d,refreshTimeUnit=DAYS", specString);
 }

 @Test
 public void testToCacheBuilder_NoOptionsSet() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder(), cacheBuilder);
 }

 @Test
 public void testToCacheBuilder_InitialCapacity() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.initialCapacity = 5;
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().initialCapacity(5), cacheBuilder);
 }

 @Test
 public void testToCacheBuilder_MaximumSize() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.maximumSize = 10;
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().maximumSize(10), cacheBuilder);
 }

 @Test
 public void testToCacheBuilder_MaximumWeight() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.maximumWeight = 20;
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().maximumWeight(20), cacheBuilder);
 }

 @Test
 public void testToCacheBuilder_ConcurrencyLevel() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.concurrencyLevel = 3;
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().concurrencyLevel(3), cacheBuilder);
 }

 @Test
 public void testToCacheBuilder_KeyStrength() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.keyStrength = LocalCache.Strength.WEAK;
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().weakKeys(), cacheBuilder);
 }

 @Test
 public void testToCacheBuilder_ValueStrength() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.valueStrength = LocalCache.Strength.SOFT;
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 switch (spec.valueStrength) {
 case SOFT:
 case WEAK:
 case STRONG:
 assertEquals(CacheBuilder.newBuilder().softValues(), cacheBuilder);
 break;
 default:
 fail("Unexpected valueStrength: " + spec.valueStrength);
 }
 }

 @Test
 public void testToCacheBuilder_RecordStats() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.recordStats = true;
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().recordStats(), cacheBuilder);
 }

 @Test
 public void testToCacheBuilder_WriteExpiration() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.writeExpirationDuration = 30;
 spec.writeExpirationTimeUnit = TimeUnit.SECONDS;
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS), cacheBuilder);
 }

 @Test
 public void testToCacheBuilder_AccessExpiration() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.accessExpirationDuration = 3600;
 spec.accessExpirationTimeUnit = TimeUnit.HOURS;
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().expireAfterAccess(3600, TimeUnit.HOURS), cacheBuilder);
 }

 @Test
 public void testToCacheBuilder_RefreshDuration() {
 CacheBuilderSpec spec = new CacheBuilderSpec("");
 spec.refreshDuration = 2 * 24 * 3600;
 spec.refreshTimeUnit = TimeUnit.DAYS;
 CacheBuilder<Object, Object> cacheBuilder = spec.toCacheBuilder();
 assertEquals(CacheBuilder.newBuilder().refreshAfterWrite(2 * 24 * 3600, TimeUnit.DAYS), cacheBuilder);
 }
}