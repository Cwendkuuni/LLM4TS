package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.DecimalFields;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.Minutes;
import org.junit.*;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 @BeforeClass
 public static void setUp() {
 // Set up any static variables or initialize any static methods here
 }

 @AfterClass
 public static void tearDown() {
 // Clean up any static variables or resources here
 }

 @Before
 public void setup() {
 // Set up any instance variables or initialize any instance methods here
 }

 @After
 public void tearDown() {
 // Clean up any instance variables or resources here
 }

 @Test
 public void testBetweenMethodWithValidArguments() {
 ReadableInstant readableInstant = new Instant(1357041600000L); // Thu Jan 01 2013 00:00:00.000 UTC
 ReadableInstant readableInstant2 = new Instant(1357044760000L); // Thu Jan 01 2013 00:12:00.000 UTC
 DurationFieldType durationFieldType = Minutes.INSTANCE;

 int result = BaseSingleFieldPeriod.between(readableInstant, readableInstant2, durationFieldType);

 assertEquals(12, result); // The difference between the two instants is 12 minutes
 }

 @Test
 public void testBetweenMethodWithNullArguments() {
 // Test the exception when the arguments are null
 try {
 BaseSingleFieldPeriod.between(null, null, Minutes.INSTANCE);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testBetweenMethodWithNonContiguousReadablePartial() {
 ReadablePartial readablePartial = new GeneralBaseCalendar(ISOChronology.getInstanceUTC()).newDateTime(1357041600000L).withFieldAdded(Months.MINUTES_1, 30).toReadOnlyDateTime();
 ReadablePartial readablePartial2 = new GeneralBaseCalendar(ISOChronology.getInstanceUTC()).newDateTime(1357041600000L).withFieldAdded(Months.MINUTES_2, 15).toReadOnlyDateTime();
 ReadablePeriod readablePeriod = Duration.standardHours(1);

 try {
 BaseSingleFieldPeriod.between(readablePartial, readablePartial2, readablePeriod);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testStandardPeriodInMethodWithValidArguments() {
 ReadablePeriod readablePeriod = Duration.standardDays(2);
 long n = 10;

 int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);

 assertEquals(12 * 24 * 60 * 10, result); // The difference between 2 days and 10 is 2 days * 24 hours * 60 minutes * 10
 }

 @Test
 public void testStandardPeriodInMethodWithNullArguments() {
 // Test the exception when the arguments are null
 try {
 BaseSingleFieldPeriod.standardPeriodIn(null, 10);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testStandardPeriodInMethodWithNonPreciseField() {
 ReadablePeriod readablePeriod = Duration.standardDays(1).withFieldAdded(Decimals.HOURS_1.getFieldType(), DecimalFields.microseconds(500));
 long n = 1000;

 try {
 BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testConstructors() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12); // Create an instance with an int value
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(new BaseSingleFieldPeriod(12)); // Create an instance with another instance

 assertEquals(12, baseSingleFieldPeriod.getValue());
 assertEquals(12, baseSingleFieldPeriod2.getValue());
 }

 @Test
 public void testGetFieldTypeMethod() {
 // Test the getFieldType method of an abstract class
 try {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12);
 DurationFieldType fieldType = baseSingleFieldPeriod.getFieldType();
 assertEquals(Minutes.INSTANCE, fieldType);
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testGetPeriodTypeMethod() {
 // Test the getPeriodType method of an abstract class
 try {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12);
 PeriodType periodType = baseSingleFieldPeriod.getPeriodType();
 assertEquals(PeriodType.MINUTES, periodType);
 } catch (Exception e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test
 public void testSizeMethod() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12);

 int size = baseSingleFieldPeriod.size();

 assertEquals(1, size);
 }

 @Test
 public void testGetValueMethod() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12);

 int value = baseSingleFieldPeriod.getValue();

 assertEquals(12, value);
 }

 @Test
 public void testSetValueMethod() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12);

 baseSingleFieldPeriod.setValue(15);

 assertEquals(15, baseSingleFieldPeriod.getValue());
 }

 @Test
 public void testGetFieldTypeMethodWithInvalidIndex() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12);

 try {
 baseSingleFieldPeriod.getFieldType(2);
 fail("IndexOutOfBoundsException expected");
 } catch (IndexOutOfBoundsException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetValueMethodWithInvalidIndex() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12);

 try {
 baseSingleFieldPeriod.getValue(2);
 fail("IndexOutOfBoundsException expected");
 } catch (IndexOutOfBoundsException e) {
 // Expected exception
 }
 }

 @Test
 public void testGetMethodWithInvalidDurationFieldType() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12);

 try {
 baseSingleFieldPeriod.get(DurationFieldType.HOURS);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testIsSupportedMethod() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12);

 assertTrue(baseSingleFieldPeriod.isSupported(Minutes.INSTANCE));
 assertFalse(baseSingleFieldPeriod.isSupported(Hours.INSTANCE));
 }

 @Test
 public void testToPeriodMethod() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12);
 Period expected = Duration.standardMinutes(12);

 Period actual = baseSingleFieldPeriod.toPeriod();

 assertEquals(expected, actual);
 }

 @Test
 public void testToMutablePeriodMethod() {
 BaseSingleFieldPeriod baseSingleFieldPeriod = new BaseSingleFieldPeriod(12);
 MutablePeriod expected = new MutablePeriod();
 expected.set(Minutes.MINUTES, 12);
 MutablePeriod actual = baseSingleFieldPeriod.toMutablePeriod();

 assertEquals(expected, actual);
 }

 @Test
 public void testEqualsMethod() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(12);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(12);
 BaseSingleFieldPeriod baseSingleFieldPeriod3 = new BaseSingleFieldPeriod(15);

 assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod1));
 assertTrue(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod2));
 assertFalse(baseSingleFieldPeriod1.equals(baseSingleFieldPeriod3));
 }

 @Test
 public void testHashCodeMethod() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(12);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(12);
 BaseSingleFieldPeriod baseSingleFieldPeriod3 = new BaseSingleFieldPeriod(15);

 int baseSingleFieldPeriod1HashCode = baseSingleFieldPeriod1.hashCode();
 int baseSingleFieldPeriod2HashCode = baseSingleFieldPeriod2.hashCode();
 int baseSingleFieldPeriod3HashCode = baseSingleFieldPeriod3.hashCode();

 assertNotEquals(baseSingleFieldPeriod1HashCode, baseSingleFieldPeriod2HashCode);
 assertNotEquals(baseSingleFieldPeriod1HashCode, baseSingleFieldPeriod3HashCode);
 assertNotEquals(baseSingleFieldPeriod2HashCode, baseSingleFieldPeriod3HashCode);
 }

 @Test
 public void testCompareToMethodWithEqualObjects() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(12);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(12);

 assertEquals(0, baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2));
 }

 @Test
 public void testCompareToMethodWithDifferentObjects() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(12);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(15);

 assertEquals(-3, baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2));
 }

 @Test
 public void testCompareToMethodWithSameClassDifferentValues() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(12);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriod(15);

 assertEquals(-3, baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2));
 assertEquals(3, baseSingleFieldPeriod2.compareTo(baseSingleFieldPeriod1));
 }

 @Test
 public void testCompareToMethodWithDifferentClasses() {
 BaseSingleFieldPeriod baseSingleFieldPeriod1 = new BaseSingleFieldPeriod(12);
 BaseSingleFieldPeriod baseSingleFieldPeriod2 = new BaseSingleFieldPeriodWithSeconds(12);

 assertFalse(baseSingleFieldPeriod1.compareTo(baseSingleFieldPeriod2) instanceof Number);
 }
}