package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.BasePeriod;
import org.joda.time.Chronology;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDate;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.test.BaseTestCase;

public class BaseSingleFieldPeriodTest extends BaseTestCase {

 private BaseSingleFieldPeriod testPeriod;
 private ReadablePeriod testPeriod2;
 private ReadablePeriod testPeriod3;
 private ReadablePeriod testPeriod4;
 private ReadablePartial testPartial;
 private ReadablePartial testPartial2;
 private ReadableInstant testInstant;
 private ReadableInstant testInstant2;
 private Chronology chronology;

 @Before
 public void setUp() {
 testPeriod = new MySingleFieldPeriod(1);
 testPeriod2 = new MySingleFieldPeriod(2);
 testPeriod3 = new MySingleFieldPeriod(3);
 testPeriod4 = new MySingleFieldPeriod(4);
 testPartial = new LocalDate();
 testPartial2 = new LocalDate();
 testInstant = new Instant(System.currentTimeMillis());
 testInstant2 = new Instant(System.currentTimeMillis() - 1000L);
 chronology = DateTimeUtils.getLocalDateTimeChronology();
 }

 @Test
 public void testBetween() {
 // Test with valid ReadableInstant objects
 int result = BaseSingleFieldPeriod.between(testInstant, testInstant2, DurationFieldType.MILLISECONDS);
 assertEquals("Incorrect difference between Instant objects", -1000, result);

 // Test with null ReadableInstant objects
 try {
 BaseSingleFieldPeriod.between(null, testInstant, DurationFieldType.MILLISECONDS);
 fail("IllegalArgumentException should be thrown when 'readableInstant' is null");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(testInstant, null, DurationFieldType.MILLISECONDS);
 fail("IllegalArgumentException should be thrown when 'readableInstant2' is null");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testBetweenPartial() {
 // Test with valid ReadablePartial and ReadablePeriod objects
 LocalDate date1 = new LocalDate();
 LocalDate date2 = new LocalDate(date1.getYear(), date1.getMonthOfYear(), date1.getDayOfMonth().plus(1).getValue());
 Chronology dateChronology = DateTimeUtils.getLocalDateChronology();
 int result = BaseSingleFieldPeriod.between(testPartial, testPartial2, testPeriod);
 assertEquals("Incorrect difference between Partial objects", 0, result);

 int result2 = BaseSingleFieldPeriod.between(testPartial, testPartial2, testPeriod2);
 assertEquals("Incorrect difference between Partial objects", 0, result2);

 int result3 = BaseSingleFieldPeriod.between(testPartial, date2.toLocalDate(), testPeriod);
 assertEquals("Incorrect difference between LocalDate and Partial object", 1, result3);

 int result4 = BaseSingleFieldPeriod.between(date2.toLocalDate(), testPartial2, testPeriod);
 assertEquals("Incorrect difference between LocalDate and Partial object", -1, result4);

 // Test with null or invalid ReadablePartial objects
 try {
 BaseSingleFieldPeriod.between(null, testPartial, testPeriod);
 fail("IllegalArgumentException should be thrown when 'readablePartial' is null");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(testPartial, null, testPeriod);
 fail("IllegalArgumentException should be thrown when 'readablePartial2' is null");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(testPartial, testPartial, null);
 fail("IllegalArgumentException should be thrown when 'readablePeriod' is null");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(testPartial, testPartial2, testPeriod2.withSize(2));
 fail("IllegalArgumentException should be thrown when 'readablePeriod' has size greater than 1");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(testPartial, testPartial2, new Period(1, DurationFieldType.DAYS, Chronology.getInstanceUTC()));
 fail("IllegalArgumentException should be thrown when 'readablePeriod' is not an instance of BaseSingleFieldPeriod");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testStandardPeriodIn() {
 // Test with valid ReadablePeriod object and long value
 Period testPeriod = new Period(1, DurationFieldType.DAYS, chronology);
 long n = 2;
 int result = BaseSingleFieldPeriod.standardPeriodIn(testPeriod, n);
 assertEquals("Incorrect result of standardPeriodIn method", 2, result);

 // Test with null ReadablePeriod object
 try {
 BaseSingleFieldPeriod.standardPeriodIn(null, n);
 fail("IllegalArgumentException should be thrown when 'readablePeriod' is null");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testConstructor() {
 // Test with valid int value
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 assertEquals("Incorrect value of iPeriod after constructor call", 1, testPeriod.getValue());

 // Test with invalid int value
 try {
 new MySingleFieldPeriod(-1);
 fail("IllegalArgumentException should be thrown when 'iPeriod' is negative");
 } catch (IllegalArgumentException e) {
 // Expected
 }
}

 @Test
 public void testGetValue() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 assertEquals("Incorrect value of iPeriod after getValue call", 1, testPeriod.getValue());
}

 @Test
 public void testSetValue() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 testPeriod.setValue(2);
 assertEquals("Incorrect value of iPeriod after setValue call", 2, testPeriod.getValue());
}

 @Test
 public void testGetFieldType() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 DurationFieldType durationFieldType = testPeriod.getFieldType();
 assertEquals("Incorrect field type", DurationFieldType.MILLISECONDS, durationFieldType);
}

 @Test
 public void testGetPeriodType() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 PeriodType periodType = testPeriod.getPeriodType();
 assertEquals("Incorrect period type", PeriodType.MILLISECONDS, periodType);
}

 @Test
 public void testSize() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 assertEquals("Incorrect size of Period object", 1, testPeriod.size());
}

 @Test
 public void testGetFieldTypeAt() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 DurationFieldType durationFieldType = testPeriod.getFieldType(0);
 assertEquals("Incorrect field type", DurationFieldType.MILLISECONDS, durationFieldType);
}

 @Test
 public void testGetValueAt() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 int value = testPeriod.getValue(0);
 assertEquals("Incorrect value of iPeriod at index 0", 1, value);
}

 @Test
 public void testGetValueAtInvalidIndex() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 try {
 testPeriod.getValue(1);
 fail("IndexOutOfBoundsException should be thrown");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
}

 @Test
 public void testGetAt() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 int result = testPeriod.get(DurationFieldType.MILLISECONDS);
 assertEquals("Incorrect result of get method", 1, result);
}

 @Test
 public void testIsSupported() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 assertTrue("Incorrect result of isSupported method", testPeriod.isSupported(DurationFieldType.MILLISECONDS));
}

 @Test
 public void testToPeriod() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 Period result = testPeriod.toPeriod();
 assertEquals("Incorrect result of toPeriod method", new Period(1, DurationFieldType.MILLISECONDS, chronology), result);
}

 @Test
 public void testToMutablePeriod() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 MutablePeriod result = testPeriod.toMutablePeriod();
 assertEquals("Incorrect result of toMutablePeriod method", new MutablePeriod(1, DurationFieldType.MILLISECONDS, chronology), result);
}

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod testPeriod1 = new MySingleFieldPeriod(1);
 BaseSingleFieldPeriod testPeriod2 = new MySingleFieldPeriod(1);
 assertTrue("Incorrect result of equals method", testPeriod1.equals(testPeriod2));
}

 @Test
 public void testHashCode() {
 BaseSingleFieldPeriod testPeriod1 = new MySingleFieldPeriod(1);
 BaseSingleFieldPeriod testPeriod2 = new MySingleFieldPeriod(1);
 int result1 = testPeriod1.hashCode();
 int result2 = testPeriod2.hashCode();
 assertEquals("Incorrect result of hashCode method", result1, result2);
}

 @Test
 public void testCompareTo() {
 BaseSingleFieldPeriod testPeriod1 = new MySingleFieldPeriod(1);
 BaseSingleFieldPeriod testPeriod2 = new MySingleFieldPeriod(2);
 int result = testPeriod1.compareTo(testPeriod2);
 assertEquals("Incorrect result of compareTo method", -1, result);

 BaseSingleFieldPeriod testPeriod3 = new MySingleFieldPeriod(1);
 BaseSingleFieldPeriod testPeriod4 = new MySingleFieldPeriod(1);
 int result2 = testPeriod3.compareTo(testPeriod4);
 assertEquals("Incorrect result of compareTo method", 0, result2);
}

 @Test
 public void testToString() {
 BaseSingleFieldPeriod testPeriod = new MySingleFieldPeriod(1);
 String result = testPeriod.toString();
 assertEquals("Incorrect result of toString method", "P1M", result);
}

}

 public class MySingleFieldPeriod extends BaseSingleFieldPeriod {
 @Override
 public DurationFieldType getFieldType() {
 return DurationFieldType.MILLISECONDS;
 }
 }