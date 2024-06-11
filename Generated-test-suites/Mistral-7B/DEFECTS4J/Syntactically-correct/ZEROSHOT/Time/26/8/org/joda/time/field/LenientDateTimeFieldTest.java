package org.joda.time.field;

import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeField;
import org.joda.time.LocalDateTimeFieldType;
import org.joda.time.ZoneId;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LenientDateTimeFieldTest {

 private static final Chronology ENGLISH_PROLEPTIC = DateTimeUtils.getUTC();

 @Test
 public void testGetInstance() {
 DateTimeField field = LocalDateTimeField.createLocalDateOfWeekFields(LocalDateTimeFieldType.DAY_OF_MONTH, LocalDateTimeFieldType.DAY_OF_WEEK);
 DateTimeField lenientField = LenientDateTimeField.getInstance(field, ENGLISH_PROLEPTIC);
 assertNotNull(lenientField);
 assertEquals(field, lenientField.getBase());
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testGetInstanceNull() {
 DateTimeField field = null;
 Chronology base = ENGLISH_PROLEPTIC;
 DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);
 assertNull(lenientField);
 }

 @Test
 public void testGetInstanceAlreadyLenient() {
 DateTimeField field = LenientDateTimeField.createLenientLocalDateTimeField(LocalDateTimeFieldType.DAY_OF_MONTH, LocalDateTimeFieldType.DAY_OF_WEEK);
 DateTimeField lenientField = LenientDateTimeField.getInstance(field, ENGLISH_PROLEPTIC);
 assertNotNull(lenientField);
 assertEquals(field, lenientField);
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testSetInbounds() {
 DateTimeField field = LocalDateTimeField.createLocalDateOfWeekFields(LocalDateTimeFieldType.DAY_OF_MONTH, LocalDateTimeFieldType.DAY_OF_WEEK);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ENGLISH_PROLEPTIC);
 LocalDateTime dateTime = new LocalDateTime(DateTimeUtils.CURRENT_MILLIS, ENGLISH_PROLEPTIC);
 long currentValue = lenientField.get(dateTime.toLong());
 long newValue = currentValue;
 long result = lenientField.set(dateTime.toLong(), newValue);
 assertEquals(result, dateTime.toLong());
 }

 @Test
 public void testSetInboundsPlusOne() {
 DateTimeField field = LocalDateTimeField.createLocalDateOfWeekFields(LocalDateTimeFieldType.DAY_OF_MONTH, LocalDateTimeFieldType.DAY_OF_WEEK);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ENGLISH_PROLEPTIC);
 LocalDateTime dateTime = new LocalDateTime(DateTimeUtils.CURRENT_MILLIS, ENGLISH_PROLEPTIC);
 long currentValue = lenientField.get(dateTime.toLong());
 long newValue = currentValue + 1;
 long result = lenientField.set(dateTime.toLong(), newValue);
 assertEquals(result, dateTime.plusDays(1).toLong());
 }

 @Test
 public void testSetOutOfBoundsMinusOne() {
 DateTimeField field = LocalDateTimeField.createLocalDateOfWeekFields(LocalDateTimeFieldType.DAY_OF_MONTH, LocalDateTimeFieldType.DAY_OF_WEEK);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ENGLISH_PROLEPTIC);
 LocalDateTime dateTime = new LocalDateTime(DateTimeUtils.CURRENT_MILLIS, ENGLISH_PROLEPTIC);
 long currentValue = lenientField.get(dateTime.toLong());
 long newValue = currentValue - 1;
 long result = lenientField.set(dateTime.toLong(), newValue);
 assertEquals(result, dateTime.minusDays(1).toLong());
 }

 @Test
 public void testSetOutOfBoundsPlusOneInWrongChronology() {
 DateTimeField field = LocalDateTimeField.createLocalDateOfWeekFields(LocalDateTimeFieldType.DAY_OF_MONTH, LocalDateTimeFieldType.DAY_OF_WEEK);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ENGLISH_PROLEPTIC);
 LocalDateTime dateTime = new LocalDateTime(DateTimeUtils.CURRENT_MILLIS, ZoneId.of("Europe/London"));
 long currentValue = lenientField.get(dateTime.toLong());
 long newValue = currentValue + 1;
 try {
 lenientField.set(dateTime.toLong(), newValue);
 fail();
 } catch (IllegalArgumentException expected) {
 // expected
 }
 }

 @Test
 public void testIsLenient() {
 DateTimeField field = LocalDateTimeField.createLocalDateOfWeekFields(LocalDateTimeFieldType.DAY_OF_MONTH, LocalDateTimeFieldType.DAY_OF_WEEK);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ENGLISH_PROLEPTIC);
 assertTrue(lenientField.isLenient());
 }

 @Test
 public void testGetBase() {
 DateTimeField field = LocalDateTimeField.createLocalDateOfWeekFields(LocalDateTimeFieldType.DAY_OF_MONTH, LocalDateTimeFieldType.DAY_OF_WEEK);
 LenientDateTimeField lenientField = new LenientDateTimeField(field, ENGLISH_PROLEPTIC);
 assertEquals(field, lenientField.getBase());
 }
}