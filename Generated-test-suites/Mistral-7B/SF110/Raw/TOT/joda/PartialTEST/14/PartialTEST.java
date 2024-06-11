package org.joda.time.test;

import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeException;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

 @Test
 public void testEmptyPartial() {
 Partial emptyPartial = new Partial();
 assertEquals(0, emptyPartial.size());
 assertEquals("[]", emptyPartial.toString());
 }

 @Test
 public void testPartialWithOneField() {
 Chronology chronology = LocalDateTime.now().getChronology();
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year() }, new int[] { 2019 }, chronology);
 assertEquals(1, partial.size());
 assertEquals(chronology, partial.getChronology());
 assertEquals(DateTimeFieldType.year(), partial.getFieldTypes()[0]);
 assertEquals(2019, partial.getValue(0));
 }

 @Test
 public void testPartialWithMultipleFields() {
 Chronology chronology = LocalDateTime.now().getChronology();
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() },
 new int[] { 2019, 11, 30 }, chronology);
 assertEquals(3, partial.size());
 assertEquals(chronology, partial.getChronology());
 assertEquals(DateTimeFieldType.year(), partial.getFieldTypes()[0]);
 assertEquals(2019, partial.getValue(0));
 assertEquals(DateTimeFieldType.monthOfYear(), partial.getFieldTypes()[1]);
 assertEquals(11, partial.getValue(1));
 assertEquals(DateTimeFieldType.dayOfMonth(), partial.getFieldTypes()[2]);
 assertEquals(30, partial.getValue(2));
 }

 @Test
 public void testPartialWithInvalidFieldType() {
 Chronology chronology = LocalDateTime.now().getChronology();
 try {
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), null }, new int[] { 2019, 11 }, chronology);
 fail("should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPartialWithInvalidValues() {
 Chronology chronology = LocalDateTime.now().getChronology();
 try {
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.minuteOfHour(), DateTimeFieldType.secondOfMinute() }, new int[] { 60, 60 }, chronology);
 fail("should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPartialWithDuplicateFieldTypes() {
 Chronology chronology = LocalDateTime.now().getChronology();
 try {
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.minuteOfHour(), DateTimeFieldType.minuteOfHour() }, new int[] { 60, 60 }, chronology);
 fail("should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPartialWithOutOfOrderFieldTypes() {
 Chronology chronology = LocalDateTime.now().getChronology();
 try {
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.secondOfMinute(), DateTimeFieldType.minuteOfHour() }, new int[] { 60, 60 }, chronology);
 fail("should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPartialWithInvalidChronology() {
 try {
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() }, new int[] { 2019, 11 }, new BaseDateTime(0, DateTimeConstants.MILLIS_PER_DAY, BaseDateTime.UTC));
 fail("should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testPartialWithSetters() {
 Chronology chronology = LocalDateTime.now().getChronology();
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() }, new int[] { 2019, 11, 30 }, chronology);
 Partial partialWithFieldAdded = partial.withFieldAdded(DateTimeFieldType.hourOfDay(), 12);
 Partial partialWithFieldAddWrapped = partial.withFieldAddWrapped(DateTimeFieldType.hourOfDay(), 24);
 Partial partialWithPeriodAdded = partial.withPeriodAdded(new LocalDateTime().withTimeAtSet(12, 30, 0).toLocalDate().with(DateTimeConstants.MILLIS_PER_DAY), 5);
 assertEquals(4, partialWithFieldAdded.size());
 assertEquals(4, partialWithFieldAddWrapped.size());
 assertEquals(4, partialWithPeriodAdded.size());
 assertEquals(12, partialWithFieldAdded.getValue(3));
 assertEquals(13, partialWithFieldAddWrapped.getValue(3));
 assertEquals(2019, partialWithPeriodAdded.getValue(0));
 assertEquals(11, partialWithPeriodAdded.getValue(1));
 assertEquals(30, partialWithPeriodAdded.getValue(2));
 assertEquals(35, partialWithPeriodAdded.getValue(3));
 }

 @Test
 public void testPartialWithToString() {
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() }, new int[] { 2019, 11, 30 }, LocalDateTime.now().getChronology());
 assertEquals("[year=2019, monthOfYear=11, dayOfMonth=30]", partial.toString());
 }

 @Test
 public void testPartialWithGetFormatter() {
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() }, new int[] { 2019, 11, 30 }, LocalDateTime.now().getChronology());
 DateTimeFormatter formatter = partial.getFormatter();
 assertEquals("[year=2019, monthOfYear=11, dayOfMonth=30]", formatter.print(partial));
 }

 @Test
 public void testPartialWithIsMatch() {
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() }, new int[] { 2019, 11, 30 }, LocalDateTime.now().getChronology());
 ReadableInstant instant = new BaseDateTime(2019, 11, 30, 12, 30, 0, 0, LocalDateTime.now().getZone().toInstant()).toInstant();
 assertTrue(partial.isMatch(instant));
 }

 @Test
 public void testPartialWithIsMatchWithDifferentChronology() {
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() }, new int[] { 2019, 11, 30 }, LocalDateTime.now().getChronology());
 ReadableInstant instant = new BaseDateTime(2019, 11, 30, 12, 30, 0, 0, DateTimeZone.UTC).toInstant();
 assertFalse(partial.isMatch(instant));
 }

 @Test
 public void testPartialWithIsMatchWithDifferentValues() {
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() }, new int[] { 2019, 11, 30 }, LocalDateTime.now().getChronology());
 ReadableInstant instant = new BaseDateTime(2019, 12, 31, 12, 30, 0, 0, LocalDateTime.now().getZone().toInstant()).toInstant();
 assertFalse(partial.isMatch(instant));
 }

 @Test
 public void testPartialWithClone() {
 Partial partial = new Partial(new DateTimeFieldType[] { DateTimeFieldType.year(), DateTimeFieldType.monthOfYear(), DateTimeFieldType.dayOfMonth() }, new int[] { 2019, 11, 30 }, LocalDateTime.now().getChronology());
 Partial clonedPartial = partial.clone();
 assertEquals(partial, clonedPartial);
 }
}