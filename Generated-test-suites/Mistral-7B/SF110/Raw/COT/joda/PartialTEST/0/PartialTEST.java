package org.joda.time.test;

import org.junit.Test;
import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.field.FieldUtils;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.joda.time.DateTimeFieldType.*;
import static org.joda.time.DurationFieldType.*;

public class PartialTest {

    @Test
    public void testNullArgument() {
        try {
            new Partial(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        try {
            new Partial(DateTimeFieldType.dayOfMonth(), -1);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testEmptyPartial() {
        Partial partial = new Partial();
        Assert.assertEquals(0, partial.size());
    }

    @Test
    public void testPartialWithSingleField() {
        Partial partial = new Partial(DAY_OF_MONTH, 2);
        Assert.assertEquals(1, partial.size());
        Assert.assertEquals(DAY_OF_MONTH, partial.getFieldTypes()[0]);
        Assert.assertEquals(2, partial.getValue(0));
    }

    @Test
    public void testPartialWithMultipleFields() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        Assert.assertEquals(3, partial.size());
        Assert.assertEquals(DAY_OF_MONTH, partial.getFieldTypes()[0]);
        Assert.assertEquals(2, partial.getValue(0));
        Assert.assertEquals(MONTH_OF_YEAR, partial.getFieldTypes()[1]);
        Assert.assertEquals(3, partial.getValue(1));
    }

    @Test
    public void testPartialWithInvalidFieldOrder() {
        try {
            new Partial(HOUR_OF_DAY, MINUTES, Chronology.getInstanceUTC());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Types array must be in order largest-smallest: HOUR_OF_DAY < MINUTES", e.getMessage());
        }
    }

    @Test
    public void testPartialWithDuplicateFields() {
        try {
            new Partial(HOUR_OF_DAY, HOUR_OF_DAY, Chronology.getInstanceUTC());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Types array must not contain duplicate: HOUR_OF_DAY", e.getMessage());
        }
    }

    @Test
    public void testPartialWithInvalidRangeDurationType() {
        try {
            new Partial(HOUR_OF_DAY, HOUR_OF_DAY, Chronology.getInstanceUTC(), new DurationFieldType[] { MINUTES, HOURS });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Types array must be in order largest-smallest: HOUR_OF_DAY < MINUTES", e.getMessage());
        }
    }

    @Test
    public void testPartialWithDuplicateRangeDurationTypes() {
        try {
            new Partial(HOUR_OF_DAY, HOUR_OF_DAY, Chronology.getInstanceUTC(), new DurationFieldType[] { MINUTES, HOURS, MINUTES });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Types array must not contain duplicate: MINUTES", e.getMessage());
        }
    }

    @Test
    public void testPartialWithMismatchedRangeDurationTypes() {
        try {
            new Partial(HOUR_OF_DAY, MINUTES, Chronology.getInstanceUTC(), new DurationFieldType[] { MINUTES, HOURS });
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Types array must be in order largest-smallest: HOUR_OF_DAY < MINUTES", e.getMessage());
        }
    }

    @Test
    public void testPartialWithNullFieldTypes() {
        try {
            new Partial(null, 1, Chronology.getInstanceUTC());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Types array must not be null", e.getMessage());
        }
    }

    @Test
    public void testPartialWithNullValues() {
        try {
            new Partial(DAY_OF_MONTH, null, Chronology.getInstanceUTC());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Values array must not be null", e.getMessage());
        }
    }

    @Test
    public void testPartialWithMismatchedValuesLength() {
        try {
            new Partial(DAY_OF_MONTH, 1, MONTH_OF_YEAR, 2, Chronology.getInstanceUTC());
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Values array must be the same length as the types array", e.getMessage());
        }
    }

    @Test
    public void testPartialWithReadOnlyFields() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        try {
            partial.with(HOUR_OF_DAY, 4);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            partial.without(DAY_OF_MONTH);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            partial.withField(HOUR_OF_DAY, 4);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        try {
            partial.withFieldAdded(MINUTES, 10);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testPartialWithSerializable() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(partial);
        objectOutputStream.close();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Partial partial2 = (Partial) objectInputStream.readObject();
        objectInputStream.close();
        Assert.assertEquals(partial, partial2);
    }

    @Test
    public void testPartialToString() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        Assert.assertEquals("[DAY_OF_MONTH=2, MONTH_OF_YEAR=3]", partial.toString());
    }

    @Test
    public void testPartialToStringFormat() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy[/MM/dd]");
        Assert.assertEquals("02/03/2000[/03/02]", partial.toString(formatter));
    }

    @Test
    public void testPartialToStringLocale() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy[/MM/dd]").withLocale(Locale.FRENCH);
        Assert.assertEquals("02/03/2000[/03/02]", partial.toString(formatter));
    }

    @Test
    public void testPartialParseFormat() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy[/MM/dd]");
        ReadablePartial readablePartial = formatter.parsePartial("02/03/2000[/03/02]");
        Assert.assertEquals(partial, readablePartial);
    }

    @Test
    public void testPartialParseFormatWithLocale() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy[/MM/dd]").withLocale(Locale.FRENCH);
        ReadablePartial readablePartial = formatter.parsePartial("02/03/2000[/03/02]");
        Assert.assertEquals(partial, readablePartial);
    }

    @Test
    public void testPartialIsMatch() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        LocalDateTime localDateTime = new LocalDateTime(2000, 3, 2, 12, 0, 0, 0, Chronology.getInstanceUTC());
        Assert.assertTrue(partial.isMatch(localDateTime));

        partial = new Partial(DAY_OF_MONTH, 3, MONTH_OF_YEAR, 2, Chronology.getInstanceUTC());
        Assert.assertFalse(partial.isMatch(localDateTime));
    }

    @Test
    public void testPartialIsMatchWithDifferentChronology() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        LocalDate localDate = new LocalDate(2000, 3, 2, Chronology.getInstanceOfLocale(Locale.FRENCH));
        Assert.assertFalse(partial.isMatch(localDate));

        partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceOfLocale(Locale.FRENCH));
        Assert.assertTrue(partial.isMatch(localDate));
    }

    @Test
    public void testPartialWithPeriodAdded() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        LocalDate date = new LocalDate(2000, 3, 2, Chronology.getInstanceUTC());
        LocalDate dateAdded = date.plus(DurationFieldType.days(10));
        Partial partialAdded = partial.withPeriodAdded(new LocalDate(dateAdded).toLocalDateTime(), 1);
        Assert.assertEquals(10, partialAdded.getValue(0));

        partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        LocalDateTime localDateTime = new LocalDateTime(2000, 3, 2, 12, 0, 0, 0, Chronology.getInstanceUTC());
        LocalDateTime localDateTimeAdded = localDateTime.plus(Duration.standardDays(10));
        Partial partialAdded2 = partial.withPeriodAdded(localDateTimeAdded, 1);
        Assert.assertEquals(10, partialAdded2.getValue(0));

        partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        LocalDateTime localDateTime3 = new LocalDateTime(2000, 3, 2, 12, 0, 0, 0, Chronology.getInstanceUTC());
        ReadablePeriod period = new BaseDateTime(2000, 3, 4, 12, 0, 0, 0, Chronology.getInstanceUTC()).plus(Duration.standardDays(10));
        Partial partialAdded3 = partial.withPeriodAdded(period, 1);
        Assert.assertEquals(10, partialAdded3.getValue(0));
    }

    @Test
    public void testPartialWithChronologyRetainFields() {
        Partial partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        Chronology chronology = DateTimeUtils.getChronology(Chronology.getInstanceUTC());
        Partial partialWithChronology = partial.withChronologyRetainFields(chronology);
        Assert.assertEquals(partial, partialWithChronology);

        partial = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        Chronology chronology2 = DateTimeUtils.getChronology(DateTimeConstants.ISO_DATE_TIME);
        Partial partialWithChronology2 = partial.withChronologyRetainFields(chronology2);
        Assert.assertNotSame(partial, partialWithChronology2);

        Partial partialWithChronology3 = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, Chronology.getInstanceUTC());
        Partial partial3 = new Partial(DAY_OF_MONTH, 2, MONTH_OF_YEAR, 3, chronology2);
        Assert.assertNotSame(partialWithChronology3, partial3);
    }
}