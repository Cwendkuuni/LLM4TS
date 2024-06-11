package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.JulianDate;
import org.joda.time.field.DateTimeFieldType;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class BasicMonthOfYearDateTimeFieldTest {

 private BasicMonthOfYearDateTimeField field;
 private BasicChronology chronology;

 @Before
 public void setUp() {
 chronology = new JulianDate();
 field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
 }

 @Test
 public void testIsLenient() {
 assertFalse(field.isLenient());
 }

 @Test
 public void testGet_simple() {
 assertEquals(1, field.get(0));
 }

 @Test
 public void testGet_largePositive() {
 assertEquals(13, field.get(121312000000L));
 }

 @Test
 public void testGet_largeNegative() {
 assertEquals(12, field.get(-121312000000L));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAdd_largeNegative() {
 field.add(-1, Long.MAX_VALUE);
 }

 @Test
 public void testAdd_smallPositive() {
 long n = 0;
 long result = field.add(n, 1);
 assertEquals(1, field.get(result));
 }

 @Test
 public void testAdd_mediumPositive() {
 long n = 121312000000L;
 long result = field.add(n, 1);
 assertEquals(2, field.get(result));
 }

 @Test
 public void testAdd_mediumNegative() {
 long n = 121312000000L;
 long result = field.add(n, -1);
 assertEquals(1, field.get(result));
 }

 @Test
 public void testAdd_largePositive() {
 long n = 121312000000L;
 long result = field.add(n, 13);
 assertEquals(14, field.get(result));
 }

 @Test
 public void testAdd_largeNegative() {
 long n = 121312000000L;
 long result = field.add(n, -13);
 assertEquals(12, field.get(result));
 }

 @Test
 public void testAddWrapField_largePositive() {
 long n = 121312000000L;
 long result = field.addWrapField(n, 13);
 assertEquals(14, field.get(result));
 }

 @Test
 public void testAddWrapField_largeNegative() {
 long n = 121312000000L;
 long result = field.addWrapField(n, -13);
 assertEquals(12, field.get(result));
 }

 @Test
 public void testAdd_largeDifference() {
 long n1 = 121312000000L;
 long n2 = 345623456000L;
 long result = field.add(n1, n2);
 assertEquals(12 + 34, field.get(result));
 }

 @Test
 public void testSet_simple() {
 long n = 0;
 long result = field.set(n, 1);
 assertEquals(1, field.get(result));
 }

 @Test
 public void testSet_largePositive() {
 long n = 121312000000L;
 long result = field.set(n, 13);
 assertEquals(13, field.get(result));
 }

 @Test
 public void testSet_largeNegative() {
 long n = 121312000000L;
 long result = field.set(n, 12);
 assertEquals(12, field.get(result));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSet_outOfBounds() {
 long n = 121312000000L;
 long result = field.set(n, 13 + 1);
 }

 @Test
 public void testAdd_contiguousFields() {
 BaseDateTime date = new BaseDateTime(0, DateTimeZone.UTC);
 long[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
 long[] results = new long[values.length];
 ReadablePartial readablePartial = new BaseDateTime(values, DateTimeFieldType.monthOfYear(), null);
 for (int i = 0; i < values.length; i++) {
 results[i] = field.add(date.toLong(), readablePartial.getLongValue(), values[i], 1)[0];
 }
 assertEquals(values, results);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAdd_mixedFields() {
 BaseDateTime date = new BaseDateTime(0, DateTimeZone.UTC);
 long[] values = { 1, 2, 3, 4, 13, 14, 15, 16, 17, 18, 19, 20 };
 ReadablePartial readablePartial = new BaseDateTime(values, DateTimeFieldType.monthOfYear(), null);
 field.add(date.toLong(), readablePartial, 0, 1);
 }
}