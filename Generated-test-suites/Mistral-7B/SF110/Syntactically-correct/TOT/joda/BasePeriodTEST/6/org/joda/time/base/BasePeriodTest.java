package org.joda.time.base;

import org.joda.time.*;
import org.joda.time.base.BasePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.AbstractDuration;
import org.joda.time.convert.DurationConverter;
import org.joda.time.convert.PeriodConverter;
import org.joda.time.field.DurationFieldType;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasePeriodTest {

 @Test
 public void testBasePeriodConstruction() {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertEquals(3, period.getValue(DurationFieldType.years()));
 assertEquals(2, period.getValue(DurationFieldType.months()));
 // ... add other fields assertions
 }

 @Test
 public void testBasePeriodSerialization() throws IOException, ClassNotFoundException {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
 ObjectOutputStream objOS = new ObjectOutputStream(byteArrayOS);
 objOS.writeObject(period);
 objOS.close();
 ByteArrayInputStream byteArrayIS = new ByteArrayInputStream(byteArrayOS.toByteArray());
 ObjectInputStream objIS = new ObjectInputStream(byteArrayIS);
 BasePeriod deserializedPeriod = (BasePeriod)objIS.readObject();
 objIS.close();
 assertEquals(period, deserializedPeriod);
 }

 @Test
 public void testBasePeriodFromInstant() {
 BasePeriod period = new BasePeriod(new DateTime(2000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()), new DateTime(2000, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC()), PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertEquals(1, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriodFromReadableDuration() {
 BasePeriod period = new BasePeriod(new Duration(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()), null, PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertEquals(1, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriodFromReadableInstantAndReadableDuration() {
 BasePeriod period = new BasePeriod(new DateTime(2000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()), new Duration(2, 3, 4, 5, 6, 7, 8, 9, PeriodType.standard()), PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertEquals(2, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriodFromReadablePartial() {
 BasePeriod period = new BasePeriod(new DateTime(2000, 1, 1, 1, 1, 1, 1, ISOChronology.getInstanceUTC()), new DateTime(2000, 1, 1, 2, 2, 2, 2, ISOChronology.getInstanceUTC()), PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertEquals(1, period.getValue(DurationFieldType.hours()));
 assertEquals(1, period.getValue(DurationFieldType.minutes()));
 assertEquals(1, period.getValue(DurationFieldType.seconds()));
 }

 @Test
 public void testBasePeriodFromReadableDurationAndReadableInstant() {
 BasePeriod period = new BasePeriod(null, new Duration(1, 2, 3, 4, 5, 6, 7, 8, PeriodType.standard()), new DateTime(2000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()));
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertEquals(1, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriodFromObject() throws IOException, ClassNotFoundException {
 BasePeriod period = new BasePeriod(new DateTime(2000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()), (ReadablePeriod)Duration.standardDays(1).toPeriod(), PeriodType.standard());
 assertEquals(PeriodType.standard(), period.getPeriodType());
 assertEquals(1, period.getValue(DurationFieldType.days()));
 }

 @Test
 public void testBasePeriodToDuration() {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 Duration duration = period.toDurationFrom(new DateTime(2000, 1, 1, 0, 0, 0, 0, ISOChronology.getInstanceUTC()));
 assertEquals(new Duration(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard()), duration);
 }

 @Test
 public void testBasePeriodToDurationTo() {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 Duration duration = period.toDurationTo(new DateTime(2000, 1, 2, 0, 0, 0, 0, ISOChronology.getInstanceUTC()));
 assertEquals(new Duration(-3, -2, -1, -4, -5, -6, -7, -8, PeriodType.standard()), duration);
 }

 @Test
 public void testPeriodTypeConversion() throws IOException, ClassNotFoundException {
 BasePeriod period = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 PeriodType periodType = PeriodType.milledisseconds();
 BasePeriod deserializedPeriod = (BasePeriod) new ObjectInputStream(new ByteArrayInputStream(period.toByteArray())).readObject();
 assertEquals(period, deserializedPeriod);
 assertEquals(periodType, deserializedPeriod.getPeriodType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstruction() {
 new BasePeriod(null, null, null);
 }

 @Test
 public void testSetPeriod() {
 BasePeriod period = new BasePeriod();
 period.setPeriod(new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard()));
 assertEquals(3, period.getValue(DurationFieldType.years()));
 // ... add other fields assertions
 }

 @Test
 public void testMergePeriod() {
 BasePeriod period1 = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(1, 1, 1, 1, 1, 1, 1, 1, PeriodType.standard());
 period1.mergePeriod(period2);
 assertEquals(4, period1.getValue(DurationFieldType.years()));
 // ... add other fields assertions
 }

 @Test
 public void testAddPeriod() {
 BasePeriod period1 = new BasePeriod(3, 2, 1, 4, 5, 6, 7, 8, PeriodType.standard());
 BasePeriod period2 = new BasePeriod(1, 1, 1, 1, 1, 1, 1, 1, PeriodType.standard());
 period1.addPeriod(period2);
 assertEquals(4, period1.getValue(DurationFieldType.years()));
 // ... add other fields assertions
 }
}