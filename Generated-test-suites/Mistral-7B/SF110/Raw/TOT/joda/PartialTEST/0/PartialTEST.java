package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.DurationFieldType.Field;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.Partial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class PartialTest {

 private Partial partial;
 private Chronology chronology;

 @Before
 public void setUp() {
 chronology = DateTimeUtils.getCaledarInstances().getCalendarInstance("America/New_York");
 partial = new Partial(chronology, new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE}, new int[]{25, 13, 55});
 }

 @Test
 public void testSizeAndGetters() {
 assertEquals(3, partial.size());
 assertEquals(chronology, partial.getChronology());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, partial.getFieldType(0));
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, partial.getFieldType(1));
 assertEquals(DateTimeFieldType.MINUTE, partial.getFieldType(2));
 assertEquals(25, partial.getValue(0));
 assertEquals(13, partial.getValue(1));
 assertEquals(55, partial.getValue(2));
 }

 @Test
 public void testInvalidConstructorArguments() {
 try {
 new Partial(null);
 fail("Should not allow null chronology");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 new Partial(new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH}, new int[]{25, 13, 55, 100});
 fail("Should not allow invalid value length");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testToString() {
 assertEquals("[DAY_OF_MONTH=25, HOUR_OF_DAY=13, MINUTE=55]", partial.toString());
 }

 @Test
 public void testToStringWithFormat() {
 final DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
 assertEquals("25-12-2018 13:55:00", formatter.print(new BaseDateTime(partial.getMillis(), chronology)));
 }

 @Test
 public void testWithChronologyRetainFields() {
 Chronology newChronology = DateTimeUtils.getCaledarInstances().getCalendarInstance("Europe/Moscow");
 Partial newPartial = partial.withChronologyRetainFields(newChronology);
 assertEquals(partial, newPartial);
 assertNotSame(chronology, newPartial.getChronology());
 }

 @Test
 public void testWith() {
 Partial newPartial = partial.with(DateTimeFieldType.HOUR_OF_DAY, 14);
 assertEquals(partial, newPartial);
 assertEquals(14, newPartial.getValue(1));
 }

 @Test
 public void testWithout() {
 Partial newPartial = partial.without(DateTimeFieldType.DAY_OF_MONTH);
 assertEquals(2, newPartial.size());
 assertNotEquals(DateTimeFieldType.DAY_OF_MONTH, newPartial.getFieldType(0));
 }

 @Test
 public void testWithField() {
 Partial newPartial = partial.withField(DateTimeFieldType.HOUR_OF_DAY, 15);
 assertEquals(partial, newPartial);
 assertEquals(15, newPartial.getValue(1));
 }

 @Test
 public void testWithFieldAdded() {
 Partial newPartial = new Partial(chronology, new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE, DateTimeFieldType.SECOND}, new int[]{25, 13, 55, 10});
 Partial newPartial2 = newPartial.withFieldAdded(DateTimeFieldType.MILLISECOND, 250);
 assertEquals(4, newPartial2.size());
 assertEquals(DateTimeFieldType.MILLISECOND, newPartial2.getFieldType(3));
 assertEquals(250, newPartial2.getValue(3));
 }

 @Test
 public void testWithFieldAddWrapped() {
 Partial newPartial = new Partial(chronology, new DateTimeFieldType[]{DateTimeFieldType.DAY_OF_MONTH, DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE, DateTimeFieldType.SECOND}, new int[]{25, 13, 55, 60});
 try {
 Partial newPartial2 = newPartial.withFieldAddWrapped(DurationFieldType.MILLISECOND, 1000);
 fail("Should not allow negative value");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 Partial newPartial3 = newPartial.withFieldAddWrapped(DurationFieldType.MILLISECOND, -1000);
 fail("Should not allow negative value");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 Partial newPartial4 = newPartial.withFieldAddWrapped(DurationFieldType.MILLISECOND, 10000);
 fail("Should not allow value greater than maximum");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 try {
 Partial newPartial5 = newPartial.withFieldAddWrapped(DurationFieldType.MILLISECOND, 999);
 assertEquals(newPartial, newPartial5);
 assertEquals(60001, newPartial5.getValue(3));
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWithPeriodAdded() {
 final LocalDateTime start = new LocalDateTime(2018, 12, 25, 13, 55, 0);
 final ReadablePeriod period = new LocalDateTime(2018, 12, 25, 1, 45, 0).toPeriod();
 Partial newPartial = partial.withPeriodAdded(period, 3);
 assertEquals(3, newPartial.size());
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, newPartial.getFieldType(2));
 assertEquals(16, newPartial.getValue(2));
 }

 @Test
 public void testPlus() {
 final LocalDateTime start = new LocalDateTime(2018, 12, 25, 13, 55, 0);
 Partial newPartial = partial.plus(periodOf(1, DateTimeFieldType.DAY_OF_MONTH));
 assertEquals(3, newPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, newPartial.getFieldType(0));
 assertEquals(26, newPartial.getValue(0));
 }

 @Test
 public void testMinus() {
 final LocalDateTime start = new LocalDateTime(2018, 12, 25, 13, 55, 0);
 Partial newPartial = partial.minus(periodOf(1, DateTimeFieldType.DAY_OF_MONTH));
 assertEquals(3, newPartial.size());
 assertEquals(DateTimeFieldType.DAY_OF_MONTH, newPartial.getFieldType(0));
 assertEquals(24, newPartial.getValue(0));
 }

 @Test
 public void testProperty() {
 final Property property = new Property(partial, 1);
 assertEquals(DateTimeFieldType.HOUR_OF_DAY, property.getField().getFieldType());
 assertEquals(13, property.get());
 }

 @Test
 public void testIsMatch() {
 final Instant instant = new DateTime(2018, 12, 25, 13, 55, 0, 0).toInstant();
 MutableDateTime mutableDateTime = new MutableDateTime(instant, chronology);
 assertTrue(partial.isMatch(mutableDateTime));
 }

 @Test
 public void testToStringList() {
 assertEquals("[DAY_OF_MONTH=25, HOUR_OF_DAY=13, MINUTE=55]", partial.toStringList());
 }

 @Test
 public void testIsMatchWithReadablePartial() {
 final Partial readablePartial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE}, new int[]{13, 55});
 assertTrue(partial.isMatch(readablePartial));
 }

 @Test
 public void testGetFormatter() {
 DateTimeFormatter formatter = partial.getFormatter();
 assertEquals("[DD-MM-YYYY HH:mm:ss][dd-MM-yyyy HH:mm:ss]", formatter.pattern());
 }

 @Test
 public void testIsMatchWithReadableInstant() {
 final Instant instant = new DateTime(2018, 12, 25, 13, 55, 0, 0).toInstant();
 final ReadableInstant readableInstant = Instant.now();
 assertFalse(partial.isMatch(readableInstant));
 }

 @Test
 public void testIsMatchWithInvalidReadableInstant() {
 final Instant instant = new DateTime(2018, 12, 25, 13, 55, 0, 0).toInstant();
 final ReadableInstant readableInstant = new DateTime(2018, 12, 25, 13, 55, 0, 0).plus(Period.days(1));
 assertFalse(partial.isMatch(readableInstant));
 }
}