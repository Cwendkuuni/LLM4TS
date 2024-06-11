package org.joda.time.field;

import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertSame;

public class UnsupportedDurationFieldTest {

 private static final DurationFieldType DAYS = DurationFieldType.dayOfWeek();
 private static final DurationFieldType HOURS = DurationFieldType.hours();
 private static final DurationFieldType MINUTES = DurationFieldType.minutes();
 private static final DurationFieldType SECONDS = DurationFieldType.seconds();
 private static final DurationFieldType MILLIS = DurationFieldType.millis();

 @Test
 public void testGetInstance() {
 UnsupportedDurationField udf1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField udf2 = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(DAYS, udf1.getType());
 assertSame(udf1, udf2);
 }

 @Test
 public void testGetInstanceNull() {
 assertNull(UnsupportedDurationField.getInstance(null));
 }

 @Test
 public void testGetInstanceUnknown() {
 assertThrows(IllegalArgumentException.class, () -> UnsupportedDurationField.getInstance(new DurationFieldType("UnknownType")));
 }

 @Test
 public void testIsSupported() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 assertFalse(udf.isSupported());
 }

 @Test
 public void testIsPrecise() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 assertTrue(udf.isPrecise());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValue() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getValue(0L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueAsLong() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getValueAsLong(0L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueShort() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getValue((short) 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueInt() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getValue(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueLong() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getValue(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetValueLongInstant() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getValueAsLong(0L, 0L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillis() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getMillis(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLong() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getMillis(1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisShort() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getMillis((short) 0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisInt() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getMillis(0);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMillisLongInstant() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getMillis(0L, 0L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAdd() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.add(0L, 1);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddLong() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.add(0L, 1L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifference() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getDifference(0L, 0L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetDifferenceAsLong() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getDifferenceAsLong(0L, 0L);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetUnitMillis() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 udf.getUnitMillis();
 }

 @Test
 public void testCompareTo() {
 UnsupportedDurationField udf1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField udf2 = UnsupportedDurationField.getInstance(HOURS);
 assertEquals(0, udf1.compareTo(udf2));
 }

 @Test
 public void testEquals() {
 UnsupportedDurationField udf1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField udf2 = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(udf1, udf2);
 }

 @Test
 public void testHashCode() {
 UnsupportedDurationField udf1 = UnsupportedDurationField.getInstance(DAYS);
 UnsupportedDurationField udf2 = UnsupportedDurationField.getInstance(DAYS);
 assertEquals(udf1.hashCode(), udf2.hashCode());
 }

 @Test
 public void testToString() {
 UnsupportedDurationField udf = UnsupportedDurationField.getInstance(DAYS);
 assertEquals("UnsupportedDurationField[dayOfWeek]", udf.toString());
 }
}