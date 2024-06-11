import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.ReadOnlyPeriod;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createTestInstance(int periodValue, DurationFieldType fieldType) {
 return new BaseSingleFieldPeriod(periodValue) {
 @Override
 public DurationFieldType getFieldType() {
 return fieldType;
 }
 };
 }

 @Test
 public void testBetween_ReadableInstant_ReadableInstant_DurationFieldType() {
 ReadableInstant readableInstant = DateTimeUtils.currentTime();
 ReadableInstant readableInstant2 = new DateTime(System.currentTimeMillis() - 1000L);
 int expected = 1; // 1 second
 int actual = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.SECONDS);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_NullReadableInstant_ReadableInstant_DurationFieldType() {
 ReadableInstant readableInstant = null;
 ReadableInstant readableInstant2 = DateTimeUtils.currentTime();
 BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.SECONDS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadableInstant_NullReadableInstant_DurationFieldType() {
 ReadableInstant readableInstant = DateTimeUtils.currentTime();
 ReadableInstant readableInstant2 = null;
 BaseSingleFieldPeriod.between(readableInstant, readableInstant2, DurationFieldType.SECONDS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetween_ReadableInstant_ReadableInstant_NullDurationFieldType() {
 ReadableInstant readableInstant = DateTimeUtils.currentTime();
 ReadableInstant readableInstant2 = DateTimeUtils.currentTime();
 BaseSingleFieldPeriod.between(readableInstant, readableInstant2, null);
 }

 @Test
 public void testStandardPeriodIn_ReadablePeriod_Long() {
 ReadablePeriod readablePeriod = Period.parse("PT1S");
 int expected = 1;
 int actual = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, 1000L);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodIn_NullReadablePeriod_Long() {
 ReadablePeriod readablePeriod = null;
 long n = 1000L;
 BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 }

 @Test
 public void testConstructor_Int() {
 int periodValue = 1;
 BaseSingleFieldPeriod testInstance = createTestInstance(periodValue, DurationFieldType.SECONDS);
 int expected = periodValue;
 int actual = testInstance.getValue();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructor_NegativeInt() {
 BaseSingleFieldPeriod testInstance = createTestInstance(-1, DurationFieldType.SECONDS);
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 int expected = 1;
 int actual = testInstance.getValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetValue_Int() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 int newValue = 2;
 testInstance.setValue(newValue);
 int expected = newValue;
 int actual = testInstance.getValue();
 assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldType_Int_OutOfBounds() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 int index = 2;
 testInstance.getFieldType(index);
 }

 @Test
 public void testGetFieldType_() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 DurationFieldType expected = DurationFieldType.SECONDS;
 DurationFieldType actual = testInstance.getFieldType();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetPeriodType_() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 PeriodType expected = PeriodType.UNDEFINED;
 PeriodType actual = testInstance.getPeriodType();
 assertEquals(expected, actual);
 }

 @Test
 public void testSize_() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 int expected = 1;
 int actual = testInstance.size();
 assertEquals(expected, actual);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetFieldType_Int_OutOfBounds2() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 int index = 2;
 testInstance.getFieldType(index);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValue_Int_OutOfBounds() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 int index = 2;
 testInstance.getValue(index);
 }

 @Test
 public void testGet_DurationFieldType_() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 int expected = 1;
 int actual = testInstance.get(DurationFieldType.SECONDS);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsSupported_DurationFieldType_() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 boolean expected = true;
 boolean actual = testInstance.isSupported(DurationFieldType.SECONDS);
 assertEquals(expected, actual);
 }

 @Test
 public void testToPeriod_() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 Period expected = Period.parse("PT1S");
 Period actual = testInstance.toPeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testToMutablePeriod_() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 MutablePeriod expected = new MutablePeriodImpl(DurationFieldType.SECONDS, 1, null);
 MutablePeriod actual = testInstance.toMutablePeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals_Object() {
 BaseSingleFieldPeriod testInstance1 = createTestInstance(1, DurationFieldType.SECONDS);
 BaseSingleFieldPeriod testInstance2 = createTestInstance(1, DurationFieldType.SECONDS);
 boolean expected = true;
 boolean actual = testInstance1.equals(testInstance2);
 assertEquals(expected, actual);
 }

 @Test
 public void testHashCode_() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1, DurationFieldType.SECONDS);
 int expected = 27 * (27 * 17 + 1) + DurationFieldType.SECONDS.hashCode();
 int actual = testInstance.hashCode();
 assertEquals(expected, actual);
 }
}