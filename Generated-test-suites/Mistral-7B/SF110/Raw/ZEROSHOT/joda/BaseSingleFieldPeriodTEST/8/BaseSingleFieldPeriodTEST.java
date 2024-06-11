package org.joda.time.base;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.test.*;

public class BaseSingleFieldPeriodTest {

 private BaseSingleFieldPeriod testPeriod;
 private ReadablePartial testReadablePartial;
 private ReadableInstant testReadableInstant;
 private ReadablePeriod testReadablePeriod;

 @Before
 public void setUp() {
 testPeriod = new BaseSingleFieldPeriod(1);
 testReadablePartial = new GeneralBasePartial(ISOChronology.getInstanceUTC(), new DurationFieldType[] { Minutes.getInstance() }, new int[] { 1 });
 testReadableInstant = new GregorianInstant(1972, 1, 1, 0, 0, 0, 0, 0, TimeZone.getDefault());
 testReadablePeriod = new GenericPeriod(ISOChronology.getInstanceUTC(), new DurationFieldType[] { Minutes.getInstance() }, new int[] { 2 });
 }

 @Test
 public void testGetFieldType() {
 assertSame(Minutes.getInstance(), testPeriod.getFieldType());
 }

 @Test
 public void testGetPeriodType() {
 assertSame(PeriodType.getInstance(), testPeriod.getPeriodType());
 }

 @Test
 public void testSize() {
 assertEquals(1, testPeriod.size());
 }

 @Test
 public void testGetFieldTypeAt() {
 assertSame(Minutes.getInstance(), testPeriod.getFieldType(0));
 }

 @Test
 public void testGetValueAt() {
 assertEquals(1, testPeriod.getValue(0));
 }

 @Test
 public void testGetValue() {
 assertEquals(1, testPeriod.getValue());
 }

 @Test
 public void testSetValue() {
 testPeriod.setValue(2);
 assertEquals(2, testPeriod.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueOutOfBounds() {
 testPeriod.getValue(2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadableInstant() {
 BaseSingleFieldPeriod.between(null, testReadableInstant, Minutes.getInstance());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenNullReadableInstant2() {
 BaseSingleFieldPeriod.between(testReadableInstant, null, Minutes.getInstance());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenDifferentChronologies() {
 testPeriod = new BaseSingleFieldPeriod(1);
 testReadablePartial = new GeneralBasePartial(ISOChronology.getInstance("America/New_York"), new DurationFieldType[] { Minutes.getInstance() }, new int[] { 1 });
 BaseSingleFieldPeriod.between(testReadableInstant, testReadablePartial, Minutes.getInstance());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBetweenDifferentFields() {
 testReadablePartial = new GeneralBasePartial(ISOChronology.getInstanceUTC(), new DurationFieldType[] { Minutes.getInstance() }, new int[] { 1 });
 testReadablePartial2 = new GeneralBasePartial(ISOChronology.getInstanceUTC(), new DurationFieldType[] { Hours.getInstance() }, new int[] { 1 });
 BaseSingleFieldPeriod.between(testReadablePartial, testReadablePartial2, Minutes.getInstance());
 }

 @Test
 public void testBetween() {
 testPeriod = new BaseSingleFieldPeriod(60);
 long instantMillis = testReadableInstant.getMillis();
 long partialMillis = testReadablePartial.getMillis();
 assertEquals(60, BaseSingleFieldPeriod.between(testReadableInstant, testReadablePartial, Minutes.getInstance()));
 assertEquals(60, BaseSingleFieldPeriod.between(testReadableInstant, testReadablePartial, Minutes.getInstance()).getValue());
 assertEquals(testPeriod, Period.getInstance(60, Minutes.getInstance()));
 assertEquals(testPeriod, new BaseSingleFieldPeriod(60));
 }

 @Test
 public void testStandardPeriodIn() {
 assertEquals(1, BaseSingleFieldPeriod.standardPeriodIn(testReadablePeriod, 60));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInNullPeriod() {
 BaseSingleFieldPeriod.standardPeriodIn(null, 60);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStandardPeriodInZeroPeriod() {
 BaseSingleFieldPeriod.standardPeriodIn(Period.ZERO, 60);
 }
}