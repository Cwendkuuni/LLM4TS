package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.junit.*;

import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 @BeforeClass
 public static void setupClass() {
 // Set up any static variables or objects here
 }

 @AfterClass
 public static void tearDownClass() {
 // Clean up any static variables or objects here
 }

 @Before
 public void setup() {
 // Set up any instance variables or objects here
 }

 @After
 public void tearDown() {
 // Clean up any instance variables or objects here
 }

 @Test
 public void testBetween() {
 // Test case for between method
 Instant start = new DateTime(1972, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
 Instant end = new DateTime(1972, 1, 3, 0, 0, 0, 0, DateTimeZone.UTC).toInstant();
 DurationFieldType durationFieldType = ISODateFieldType.DAY_OF_MONTH;
 int result = BaseSingleFieldPeriod.between(start, end, durationFieldType);
 assertEquals(2, result);

 // Test case for null arguments
 try {
 BaseSingleFieldPeriod.between(null, end, durationFieldType);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(start, null, durationFieldType);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 try {
 BaseSingleFieldPeriod.between(start, end, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testStandardPeriodIn() {
 // Test case for standardPeriodIn method
 ReadablePeriod readablePeriod = Period.days(2);
 long n = 4;
 int result = BaseSingleFieldPeriod.standardPeriodIn(readablePeriod, n);
 assertEquals(1, result);

 // Test case for null argument
 try {
 BaseSingleFieldPeriod.standardPeriodIn(null, n);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testConstructors() {
 // Test case for constructor with int argument
 BaseSingleFieldPeriod testObject1 = new BaseSingleFieldPeriod(1);

 // Test case for constructor with invalid argument
 try {
 BaseSingleFieldPeriod testObject2 = new BaseSingleFieldPeriod(-1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGetValue() {
 // Test case for getValue method
 BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(3);
 int result = testObject.getValue();
 assertEquals(3, result);
 }

 @Test
 public void testSetValue() {
 // Test case for setValue method
 BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(2);
 testObject.setValue(4);
 int result = testObject.getValue();
 assertEquals(4, result);
 }

 @Test
 public void testGetFieldType() {
 // Test case for getFieldType method
 BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(1);
 DurationFieldType result = testObject.getFieldType();
 assertEquals(ISODateFieldType.DAY_OF_MONTH, result);
 }

 @Test
 public void testGetPeriodType() {
 // Test case for getPeriodType method
 BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(1);
 PeriodType result = testObject.getPeriodType();
 assertEquals(PeriodType.instance(), result);
 }

 @Test
 public void testSize() {
 // Test case for size method
 BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(1);
 int result = testObject.size();
 assertEquals(1, result);
 }

 @Test
 public void testGetFieldTypeAt() {
 // Test case for getFieldTypeAt method
 BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(1);
 DurationFieldType result = testObject.getFieldType(0);
 assertEquals(ISODateFieldType.DAY_OF_MONTH, result);
 }

 @Test
 public void testGetValueAt() {
 // Test case for getValueAt method
 BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(3);
 int result = testObject.getValue(0);
 assertEquals(3, result);
 }

 @Test
 public void testGet() {
 // Test case for get method
 BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(2);
 DurationFieldType durationFieldType = ISODateFieldType.DAY_OF_MONTH;
 int result = testObject.get(durationFieldType);
 assertEquals(2, result);
 }

 @Test
 public void testIsSupported() {
 // Test case for isSupported method
 BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(1);
 DurationFieldType durationFieldType = ISODateFieldType.DAY_OF_MONTH;
 boolean result = testObject.isSupported(durationFieldType);
 assertTrue(result);

 DurationFieldType durationFieldType2 = ISODateFieldType.HOUR_OF_DAY;
 boolean result2 = testObject.isSupported(durationFieldType2);
 assertFalse(result2);
 }

 @Test
 public void testToPeriod() {
 // Test case for toPeriod method
 BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(1);
 Period result = testObject.toPeriod();
 assertEquals(Period.days(1), result);
 }

 @Test
 public void testToMutablePeriod() {
 // Test case for toMutablePeriod method
 BaseSingleFieldPeriod testObject = new BaseSingleFieldPeriod(1);
 MutablePeriod result = testObject.toMutablePeriod();
 assertEquals(Period.days(1), result.toPeriod());
 }

 @Test
 public void testEquals() {
 // Test case for equals method
 BaseSingleFieldPeriod testObject1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod testObject2 = new BaseSingleFieldPeriod(1);
 assertTrue(testObject1.equals(testObject2));

 BaseSingleFieldPeriod testObject3 = new BaseSingleFieldPeriod(2);
 assertFalse(testObject1.equals(testObject3));
 }

 @Test
 public void testHashCode() {
 // Test case for hashCode method
 BaseSingleFieldPeriod testObject1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod testObject2 = new BaseSingleFieldPeriod(1);
 assertEquals(testObject1.hashCode(), testObject2.hashCode());

 BaseSingleFieldPeriod testObject3 = new BaseSingleFieldPeriod(2);
 assertNotEquals(testObject1.hashCode(), testObject3.hashCode());
 }

 @Test
 public void testCompareTo() {
 // Test case for compareTo method
 BaseSingleFieldPeriod testObject1 = new BaseSingleFieldPeriod(1);
 BaseSingleFieldPeriod testObject2 = new BaseSingleFieldPeriod(2);
 int result = testObject1.compareTo(testObject2);
 assertEquals(-1, result);

 BaseSingleFieldPeriod testObject3 = new BaseSingleFieldPeriod(1);
 int result2 = testObject1.compareTo(testObject3);
 assertEquals(0, result2);
 }
}