import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheBuilderSpec.ValueParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

 private CacheBuilderSpec spec;

 @Before
 public void setUp() {
 spec = new CacheBuilderSpec("initialCapacity=10, maximumSize=15, maximumWeight=20, concurrencyLevel=5," +
 " keyStrength=SOFT, valueStrength=SOFT, recordStats=true," +
 " writeExpirationDuration=5m, writeExpirationTimeUnit=MINUTES," +
 " accessExpirationDuration=2h, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=30s, refreshTimeUnit=SECONDS");
 }

 @Test
 public void testParse() {
 assertEquals("initialCapacity=10, maximumSize=15, maximumWeight=20, concurrencyLevel=5," +
 " keyStrength=SOFT, valueStrength=SOFT, recordStats=true," +
 " writeExpirationDuration=300000, writeExpirationTimeUnit=MINUTES," +
 " accessExpirationDuration=7200000, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=30000, refreshTimeUnit=SECONDS", spec.toParsableString());

 assertEquals(10, spec.initialCapacity);
 assertEquals(15, spec.maximumSize);
 assertEquals(20, spec.maximumWeight);
 assertEquals(5, spec.concurrencyLevel);
 assertEquals(LocalCache.Strength.SOFT, spec.keyStrength);
 assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
 assertTrue(spec.recordStats);
 assertEquals(5, spec.writeExpirationTimeUnit.toMinutes(spec.writeExpirationDuration));
 assertEquals(2, spec.accessExpirationTimeUnit.toHours(spec.accessExpirationDuration));
 assertEquals(30, spec.refreshTimeUnit.toSeconds(spec.refreshDuration));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownKey() {
 try {
 new CacheBuilderSpec("key=value");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("unknown key key", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidValue() {
 try {
 new CacheBuilderSpec("initialCapacity=abc");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("key initialCapacity does not take string values", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingValue() {
 try {
 new CacheBuilderSpec("initialCapacity=");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("value of key initialCapacity omitted", e.getMessage());
 }
 }

 @Test(expected = AssertionError.class)
 public void testUnknownKeyStrength() {
 try {
 new CacheBuilderSpec("keyStrength=UNKNOWN");
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test(expected = AssertionError.class)
 public void testUnknownValueStrength() {
 try {
 new CacheBuilderSpec("valueStrength=UNKNOWN");
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDurationFormat() {
 try {
 new CacheBuilderSpec("writeExpirationDuration=5s1h");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("key writeExpirationDuration invalid format.  was 5s1h, must end with one of [dDhHmMsS]", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDurationUnit() {
 try {
 new CacheBuilderSpec("accessExpirationDuration=5x");
 fail();
 } catch (IllegalArgumentException e) {
 assertEquals("key accessExpirationDuration invalid format.  was 5x, must end with one of [dDhHmMsS]", e.getMessage());
 }
 }

 @Test
 public void testToParsableString() {
 assertEquals("initialCapacity=10, maximumSize=15, maximumWeight=20, concurrencyLevel=5," +
 " keyStrength=SOFT, valueStrength=SOFT, recordStats=true," +
 " writeExpirationDuration=300000, writeExpirationTimeUnit=MINUTES," +
 " accessExpirationDuration=7200000, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=30000, refreshTimeUnit=SECONDS", spec.toParsableString());
 }

 @Test
 public void testHashCodeAndEquals() {
 CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10, maximumSize=15, maximumWeight=20," +
 " concurrencyLevel=5, keyStrength=SOFT, valueStrength=SOFT, recordStats=true," +
 " writeExpirationDuration=5m, writeExpirationTimeUnit=MINUTES," +
 " accessExpirationDuration=2h, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=30s, refreshTimeUnit=SECONDS");
 CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10, maximumSize=15, maximumWeight=20," +
 " concurrencyLevel=5, keyStrength=SOFT, valueStrength=SOFT, recordStats=true," +
 " writeExpirationDuration=5m, writeExpirationTimeUnit=MINUTES," +
 " accessExpirationDuration=2h, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=30s, refreshTimeUnit=SECONDS");
 CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=10, maximumSize=15, maximumWeight=20," +
 " concurrencyLevel=5, keyStrength=SOFT, valueStrength=SOFT, recordStats=true," +
 " writeExpirationDuration=5m, writeExpirationTimeUnit=MINUTES," +
 " accessExpirationDuration=2h, accessExpirationTimeUnit=HOURS," +
 " refreshDuration=30s, refreshTimeUnit=SECONDS");

 assertEquals(spec1, spec2);
 assertEquals(spec1, spec3);
 assertEquals(spec1.hashCode(), spec2.hashCode());
 assertEquals(spec1.hashCode(), spec3.hashCode());
 }
}