package org.joda.time.base;

import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.list.BaseListField;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod testPeriod;
 private DurationFieldType testFieldType;

 @Before
 public void setUp() {
 testFieldType = BaseListField.YEARS;
 testPeriod = new TestSingleFieldPeriod(5);
 }

 @Test
 public void testGetPeriodType() {
 PeriodType expected = PeriodType.standardInstance();
 PeriodType actual = testPeriod.getPeriodType();
 assertEquals(expected, actual);
 }

 @Test
 public void testSize() {
 int expected = 1;
 int actual = testPeriod.size();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetFieldType() {
 DurationFieldType expected = testFieldType;
 DurationFieldType actual = testPeriod.getFieldType();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetFieldTypeWithWrongIndex() {
 try {
 testPeriod.getFieldType(1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testGetValue() {
 int expected = 5;
 int actual = testPeriod.getValue();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetValueWithWrongIndex() {
 try {
 testPeriod.getValue(1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testGet() {
 int expected = 5;
 int actual = testPeriod.get(testFieldType);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsSupported() {
 assertTrue(testPeriod.isSupported(testFieldType));
 }

 @Test
 public void testIsSupportedWithWrongFieldType() {
 assertFalse(testPeriod.isSupported(BaseListField.DAYS));
 }

 @Test
 public void testToPeriod() {
 Period expected = new Period(5, PeriodType.standardInstance());
 Period actual = testPeriod.toPeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testToMutablePeriod() {
 MutablePeriod expected = new MutablePeriod();
 expected.set(testFieldType, 5);
 MutablePeriod actual = testPeriod.toMutablePeriod();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 BaseSingleFieldPeriod testPeriod2 = new TestSingleFieldPeriod(5);
 assertTrue(testPeriod.equals(testPeriod2));
 }

 @Test
 public void testEqualsWithDifferentType() {
 BaseSingleFieldPeriod testPeriod2 = new TestSingleFieldPeriod(6);
 BaseSingleFieldPeriod testPeriod3 = new TestDoubleFieldPeriod(5, 3);
 assertFalse(testPeriod.equals(testPeriod2));
 assertFalse(testPeriod.equals(testPeriod3));
 }

 @Test
 public void testHashCode() {
 int expected = 27 * (27 * 17 + 5) + testFieldType.hashCode();
 int actual = testPeriod.hashCode();
 assertEquals(expected, actual);
 }

 @Test
 public void testCompareToSameInstance() {
 int expected = 0;
 int actual = testPeriod.compareTo(testPeriod);
 assertEquals(expected, actual);
 }

 @Test
 public void testCompareToDifferentInstanceWithSameValue() {
 BaseSingleFieldPeriod testPeriod2 = new TestSingleFieldPeriod(5);
 int expected = 0;
 int actual = testPeriod.compareTo(testPeriod2);
 assertEquals(expected, actual);
 }

 @Test
 public void testCompareToDifferentInstanceWithDifferentValue() {
 BaseSingleFieldPeriod testPeriod2 = new TestSingleFieldPeriod(6);
 int expected = -1;
 int actual = testPeriod.compareTo(testPeriod2);
 assertEquals(expected, actual);
 }

 @Test
 public void testCompareToDifferentClass() {
 TestDoubleFieldPeriod testPeriod2 = new TestDoubleFieldPeriod(5, 3);
 int expected = -1;
 int actual = testPeriod.compareTo(testPeriod2);
 assertEquals(expected, actual);
 }

 @Test
 public void testBetween() {
 LocalDateTime start = new LocalDateTime(new LocalDate(2015, 1, 1), new LocalTime(0, 0, 0, 0));
 LocalDateTime end = new LocalDateTime(new LocalDate(2015, 1, 2), new LocalTime(12, 30, 0, 0));
 int expected = 16800000;
 int actual = BaseSingleFieldPeriod.between(start.toInstant(), end.toInstant(), testFieldType);
 assertEquals(expected, actual);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testBetweenWithNullInstant() {
 Instant nullInstant = null;
 // exception expected
 BaseSingleFieldPeriod.between(nullInstant, end.toInstant(), testFieldType);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testBetweenWithNullEndInstant() {
 Instant nullEndInstant = null;
 // exception expected
 BaseSingleFieldPeriod.between(start.toInstant(), nullEndInstant, testFieldType);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testBetweenWithDifferentChronology() {
 LocalDateTime start = new LocalDateTime(new LocalDate(2015, 1, 1), new LocalTime(0, 0, 0, 0), DateTimeUtils.forDate(new Date()));
 LocalDateTime end = new LocalDateTime(new LocalDate(2015, 1, 2), new LocalTime(12, 30, 0, 0), DateTimeUtils.forDate(new Date()));
 // exception expected
 BaseSingleFieldPeriod.between(start.toInstant(), end.toInstant(), testFieldType);
 }

 @Test
 public void testStandardPeriodIn() {
 long expected = 5 * 3153600000L;
 long actual = BaseSingleFieldPeriod.standardPeriodIn(testPeriod, 31536000000L);
 assertEquals(expected, actual);
 }

 @Test
 public void testStandardPeriodInWithNullPeriod() {
 long expected = 0;
 long actual = BaseSingleFieldPeriod.standardPeriodIn(null, 31536000000L);
 assertEquals(expected, actual);
 }
}

class TestSingleFieldPeriod extends BaseSingleFieldPeriod {
 TestSingleFieldPeriod(final int iPeriod) {
 super(iPeriod);
 }
}

class TestDoubleFieldPeriod extends BaseSingleFieldPeriod {
 TestDoubleFieldPeriod(final int iPeriod1, final int iPeriod2) {
 super(iPeriod1 + iPeriod2 * 2);
 }
}