package org.joda.time.test;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.Partial;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        Assert.assertEquals(0, emptyPartial.size());
        Assert.assertNull(emptyPartial.getChronology());
        Assert.assertArrayEquals(new int[0], emptyPartial.getValues());
        Assert.assertEquals(0, emptyPartial.getValue(0));
    }

    @Test
    public void testSinglePartial() {
        Chronology chrono = DateTimeUtils.forLocale(Locale.US);
        Partial singlePartial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12, chrono);
        Assert.assertEquals(1, singlePartial.size());
        Assert.assertEquals(chrono, singlePartial.getChronology());
        Assert.assertArrayEquals(new int[]{12}, singlePartial.getValues());
        Assert.assertEquals(12, singlePartial.getValue(0));
    }

    @Test
    public void testMultipleFieldsSingleChronology() {
        Chronology chrono = DateTimeUtils.forLocale(Locale.US);
        Partial multipleFields = new Partial(new DateTimeFieldType[]{DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE},
                new int[]{12, 30}, chrono);
        Assert.assertEquals(2, multipleFields.size());
        Assert.assertEquals(chrono, multipleFields.getChronology());
        Assert.assertArrayEquals(new int[]{12, 30}, multipleFields.getValues());
        Assert.assertEquals(12, multipleFields.getValue(0));
        Assert.assertEquals(30, multipleFields.getValue(1));
    }

    @Test
    public void testMultipleFieldsMultipleChronologies() {
        Partial multipleFields = new Partial(new DateTimeFieldType[]{DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE},
                new int[]{12, 30}, null);
        Chronology gregorian = DateTimeUtils.forLocale(Locale.US);
        Chronology iso = DateTimeUtils.forISO();
        multipleFields = multipleFields.withChronologyRetainFields(gregorian);
        Assert.assertEquals(gregorian, multipleFields.getChronology());
        multipleFields = multipleFields.withChronologyRetainFields(iso);
        Assert.assertEquals(iso, multipleFields.getChronology());
        Assert.assertArrayEquals(new int[]{12, 30}, multipleFields.getValues());
        Assert.assertEquals(12, multipleFields.getValue(0));
        Assert.assertEquals(30, multipleFields.getValue(1));
    }

    @Test
    public void testInvalidFieldsOrder() {
        Chronology chrono = DateTimeUtils.forLocale(Locale.US);
        try {
            new Partial(new DateTimeFieldType[]{DateTimeFieldType.MINUTE, DateTimeFieldType.HOUR_OF_DAY},
                    new int[]{12, 30}, chrono);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInvalidFieldType() {
        Chronology chrono = DateTimeUtils.forLocale(Locale.US);
        try {
            new Partial(new DateTimeFieldType[]{DurationFieldType.MILLIS_PER_MINUTE, DateTimeFieldType.HOUR_OF_DAY},
                    new int[]{12, 30}, chrono);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInvalidFieldValues() {
        Chronology chrono = DateTimeUtils.forLocale(Locale.US);
        try {
            new Partial(DateTimeFieldType.HOUR_OF_DAY, 25, chrono);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            new Partial(new DateTimeFieldType[]{DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE},
                    new int[]{12, 61}, chrono);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseString() throws ParseException {
        DateTimeFormatter formatter = ISODateTimeFormat.forFields(Arrays.asList(new DateTimeFieldType[]{DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE}),
                true, false);
        Partial partial = new Partial(formatter.parse("12:30"));
        Assert.assertEquals(2, partial.size());
        Assert.assertEquals(DateTimeFieldType.HOUR_OF_DAY, partial.getFieldType(0));
        Assert.assertEquals(DateTimeFieldType.MINUTE, partial.getFieldType(1));
        Assert.assertEquals(12, partial.getValue(0));
        Assert.assertEquals(30, partial.getValue(1));
    }

    @Test
    public void testToString() {
        Partial partial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
        Assert.assertEquals("[[HOUR_OF_DAY=12]]", partial.toString());
        partial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12, null);
        Assert.assertEquals("[HOUR_OF_DAY=12]", partial.toString());
        partial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE}, new int[]{12, 30});
        Assert.assertEquals("[[HOUR_OF_DAY=12],[MINUTE=30]]", partial.toString());
    }

    @Test
    public void testIsMatch() {
        Partial partial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
        LocalDateTime dateTime = new LocalDateTime(2012, 1, 1, 12, 0, 0, 0, DateTimeUtils.UTC);
        Assert.assertTrue(partial.isMatch(dateTime));
        partial = new Partial(DateTimeFieldType.MINUTE, 30);
        dateTime = new LocalDateTime(2012, 1, 1, 12, 30, 0, 0, DateTimeUtils.UTC);
        Assert.assertTrue(partial.isMatch(dateTime));
        dateTime = new LocalDateTime(2012, 1, 1, 13, 0, 0, 0, DateTimeUtils.UTC);
        Assert.assertFalse(partial.isMatch(dateTime));
        dateTime = new LocalDateTime(2012, 1, 1, 12, 31, 0, 0, DateTimeUtils.UTC);
        Assert.assertFalse(partial.isMatch(dateTime));
    }

    @Test
    public void testWith() {
        Partial partial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
        Partial newPartial = partial.with(DateTimeFieldType.HOUR_OF_DAY, 13);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(13, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
        partial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE}, new int[]{12, 30});
        newPartial = partial.with(DateTimeFieldType.MINUTE, 31);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(31, newPartial.getValue(1));
        Assert.assertNotSame(partial, newPartial);
    }

    @Test
    public void testWithout() {
        Partial partial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
        Partial newPartial = partial.without(DateTimeFieldType.HOUR_OF_DAY);
        Assert.assertEquals(0, newPartial.sizeDifference(partial));
        Assert.assertEquals(0, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
        partial = new Partial(new DateTimeFieldType[]{DateTimeFieldType.HOUR_OF_DAY, DateTimeFieldType.MINUTE}, new int[]{12, 30});
        newPartial = partial.without(DateTimeFieldType.MINUTE);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(12, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
    }

    @Test
    public void testWithField() {
        Partial partial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
        Partial newPartial = partial.withField(DateTimeFieldType.HOUR_OF_DAY, 13);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(13, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
        partial = new Partial(DateTimeFieldType.MINUTE, 30);
        newPartial = partial.withField(DateTimeFieldType.MINUTE, 31);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(31, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
    }

    @Test
    public void testWithFieldAdded() {
        Partial partial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
        Partial newPartial = partial.withFieldAdded(DurationFieldType.MILLIS_PER_HOUR, 1);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(13, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
        partial = new Partial(DateTimeFieldType.MINUTE, 30);
        newPartial = partial.withFieldAdded(DurationFieldType.MILLIS_PER_MINUTE, 60 * 1000);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(31, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
    }

    @Test
    public void testWithFieldAddWrapped() {
        Partial partial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
        Partial newPartial = partial.withFieldAddWrapped(DurationFieldType.MILLIS_PER_HOUR, 1);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(13, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
        partial = new Partial(DateTimeFieldType.MINUTE, 30);
        newPartial = partial.withFieldAddWrapped(DurationFieldType.MILLIS_PER_MINUTE, 60 * 1000);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(31, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
    }

    @Test
    public void testWithPeriodAdded() {
        Partial partial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
        ReadablePeriod period = new Period(1, 30, 0);
        Partial newPartial = partial.withPeriodAdded(period, 1);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(13, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
        partial = new Partial(DateTimeFieldType.MINUTE, 30);
        period = new Period(0, 30, 0);
        newPartial = partial.withPeriodAdded(period, 1);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(31, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
    }

    @Test
    public void testPlus() {
        Partial partial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 12);
        ReadablePeriod period = new Period(1, 30, 0);
        Partial newPartial = partial.plus(period);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(13, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
        partial = new Partial(DateTimeFieldType.MINUTE, 30);
        period = new Period(0, 30, 0);
        newPartial = partial.plus(period);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(31, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
    }

    @Test
    public void testMinus() {
        Partial partial = new Partial(DateTimeFieldType.HOUR_OF_DAY, 13);
        ReadablePeriod period = new Period(1, 30, 0);
        Partial newPartial = partial.minus(period);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(12, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
        partial = new Partial(DateTimeFieldType.MINUTE, 31);
        period = new Period(0, 30, 0);
        newPartial = partial.minus(period);
        Assert.assertEquals(1, newPartial.sizeDifference(partial));
        Assert.assertEquals(30, newPartial.getValue(0));
        Assert.assertNotSame(partial, newPartial);
    }
}