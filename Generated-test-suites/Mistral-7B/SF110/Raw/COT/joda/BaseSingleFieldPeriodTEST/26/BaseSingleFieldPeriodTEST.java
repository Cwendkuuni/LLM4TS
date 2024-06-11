import org.junit.Test;
import org.joda.time.BaseSingleFieldPeriod;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.DateTimeUtils;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod createTestInstance(int iPeriod) {
 return new BaseSingleFieldPeriod(iPeriod);
 }

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 assertSame(DurationFieldType.instance(1), testInstance.getFieldType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFieldTypeWhenInstanceIsNull() {
 BaseSingleFieldPeriod testInstance = null;
 testInstance.getFieldType();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFieldTypeWhenFieldTypeIsInvalid() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 try {
 testInstance.getFieldType(2);
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 testInstance.getFieldType(DurationFieldType.DAYS);
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToWhenInstancesHaveDifferentClasses() {
 BaseSingleFieldPeriod testInstance1 = createTestInstance(1);
 BaseSingleFieldPeriod testInstance2 = new AnotherTestClass();
 int comparisonResult = testInstance1.compareTo(testInstance2);
 }

 @Test
 public void testSize() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 assertEquals(1, testInstance.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWhenIndexIsInvalid() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 testInstance.getValue(2);
 }

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 assertEquals(1, testInstance.getValue());
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod testInstance1 = createTestInstance(1);
 BaseSingleFieldPeriod testInstance2 = createTestInstance(1);
 BaseSingleFieldPeriod testInstance3 = createTestInstance(2);
 assertTrue(testInstance1.equals(testInstance1));
 assertTrue(testInstance1.equals(testInstance2));
 assertFalse(testInstance1.equals(testInstance3));
 }

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod testInstance1 = createTestInstance(1);
 BaseSingleFieldPeriod testInstance2 = createTestInstance(1);
 assertEquals(testInstance1.hashCode(), testInstance2.hashCode());
 }

 @Test
 public void testCompareToWhenBothInstancesHaveSameValue() {
 BaseSingleFieldPeriod testInstance1 = createTestInstance(1);
 BaseSingleFieldPeriod testInstance2 = createTestInstance(1);
 assertEquals(0, testInstance1.compareTo(testInstance2));
 }

 @Test
 public void testCompareToWhenFirstInstanceHasLargerValue() {
 BaseSingleFieldPeriod testInstance1 = createTestInstance(2);
 BaseSingleFieldPeriod testInstance2 = createTestInstance(1);
 assertEquals(1, testInstance1.compareTo(testInstance2));
 }

 @Test
 public void testCompareToWhenFirstInstanceHasSmallerValue() {
 BaseSingleFieldPeriod testInstance1 = createTestInstance(1);
 BaseSingleFieldPeriod testInstance2 = createTestInstance(2);
 assertEquals(-1, testInstance1.compareTo(testInstance2));
 }

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 Period expected = new Period(1, 0, 0, 0, 0, 0, 0);
 Period actual = testInstance.toPeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 MutablePeriod expected = new MutablePeriod().withValue(1, testInstance.getFieldType());
 MutablePeriod actual = testInstance.toMutablePeriod();
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWhenFirstReadableInstantIsNull() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 ReadableInstant firstReadableInstant = null;
 try {
 BaseSingleFieldPeriod.between(null, firstReadableInstant, DurationFieldType.DAYS);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWhenSecondReadableInstantIsNull() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 ReadableInstant secondReadableInstant = null;
 try {
 BaseSingleFieldPeriod.between(firstReadableInstant, null, DurationFieldType.DAYS);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testBetweenWithValidArguments() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 ReadableInstant firstReadableInstant = DateTimeUtils.currentTime().withFieldAdded(DurationFieldType.DAYS, 1);
 ReadableInstant secondReadableInstant = DateTimeUtils.currentTime();
 int result = BaseSingleFieldPeriod.between(firstReadableInstant, secondReadableInstant, DurationFieldType.DAYS);
 assertEquals(1, result);
 }

 @Test
 public void testBetweenWithDifferentChronologies() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 ReadableInstant firstReadableInstant = DateTimeUtils.currentTime().withZone(DateTimeZone.forID("Asia/Kolkata"));
 ReadableInstant secondReadableInstant = DateTimeUtils.currentTime().withZone(DateTimeZone.UTC);
 int result = BaseSingleFieldPeriod.between(firstReadableInstant, secondReadableInstant, DurationFieldType.DAYS);
 assertEquals(-4.5, result / 0.5D); // Assuming a 30-minute time difference between the two zones
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWhenPartialObjectsAreInvalid() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 ReadablePartial readablePartial1 = new ReadablePartial() {
 @Override
 public int size() {
 return 2;
 }
 };
 ReadablePartial readablePartial2 = new ReadablePartial() {
 @Override
 public int size() {
 return 1;
 }
 };
 try {
 BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, DurationFieldType.DAYS);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWhenPartialObjectsHaveDifferentSizes() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 ReadablePartial readablePartial1 = new ReadablePartial() {
 @Override
 public int size() {
 return 1;
 }
 };
 ReadablePartial readablePartial2 = new ReadablePartial() {
 @Override
 public int size() {
 return 2;
 }
 };
 try {
 BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, DurationFieldType.DAYS);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWhenPartialObjectsHaveDifferentFieldTypes() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 ReadablePartial readablePartial1 = new ReadablePartial() {
 @Override
 public int size() {
 return 1;
 }

 @Override
 public DurationFieldType getFieldType(int index) {
 return DurationFieldType.DAYS;
 }
 };
 ReadablePartial readablePartial2 = new ReadablePartial() {
 @Override
 public int size() {
 return 1;
 }

 @Override
 public DurationFieldType getFieldType(int index) {
 return DurationFieldType.HOURS;
 }
 };
 try {
 BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, DurationFieldType.DAYS);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenWhenPartialObjectsAreNotContiguous() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 ReadablePartial readablePartial1 = new ReadablePartial() {
 @Override
 public int size() {
 return 1;
 }

 @Override
 public DurationFieldType getFieldType(int index) {
 return DurationFieldType.DAYS;
 }
 };
 ReadablePartial readablePartial2 = new ReadablePartial() {
 @Override
 public int size() {
 return 1;
 }

 @Override
 public DurationFieldType getFieldType(int index) {
 return DurationFieldType.HOURS;
 }
 };
 try {
 BaseSingleFieldPeriod.between(readablePartial1, readablePartial2, DurationFieldType.DAYS);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testStandardPeriodIn() {
 BaseSingleFieldPeriod testInstance = createTestInstance(7);
 long n = 2;
 int result = BaseSingleFieldPeriod.standardPeriodIn(testInstance, n);
 assertEquals(3, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInWhenPeriodIsNull() {
 BaseSingleFieldPeriod testInstance = null;
 long n = 2;
 BaseSingleFieldPeriod.standardPeriodIn(testInstance, n);
 }

 @Test
 public void testStandardPeriodInWhenPeriodHasZeroFields() {
 BaseSingleFieldPeriod testInstance = createTestInstance(0);
 long n = 2;
 int result = BaseSingleFieldPeriod.standardPeriodIn(testInstance, n);
 assertEquals(0, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInWhenNIsZero() {
 BaseSingleFieldPeriod testInstance = createTestInstance(7);
 long n = 0;
 BaseSingleFieldPeriod.standardPeriodIn(testInstance, n);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInWhenPeriodHasNonPreciseField() {
 BaseSingleFieldPeriod testInstance = createTestInstance(7);
 long n = 2;
 try {
 BaseSingleFieldPeriod.standardPeriodIn(testInstance, n);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testToString() {
 BaseSingleFieldPeriod testInstance = createTestInstance(1);
 String expected = testInstance.getClass().getSimpleName() + "[value=" + testInstance.getValue() + "]";
 String actual = testInstance.toString();
 assertEquals(expected, actual);
 }
}