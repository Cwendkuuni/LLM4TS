package org.joda.time.test;

import static org.junit.Assert.*;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.format.*;
import org.joda.time.field.*;
import org.junit.*;

public class PartialTest {

    
    
    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertTrue(emptyPartial.isMatch(DateTime.now()));
        assertNull(emptyPartial.getFormatter());
        assertNull(emptyPartial.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullChronology() {
        new Partial(null);
    }

    @Test
    public void testSingleFieldPartial() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial singleFieldPartial = new Partial(dateTimeFieldType, 2022);
        assertEquals(1, singleFieldPartial.size());
        assertEquals(dateTimeFieldType, singleFieldPartial.getFieldTypes()[0]);
        assertEquals(2022, singleFieldPartial.getValue(0));
        assertEquals(2022, singleFieldPartial.get(DateTimeFieldType.year()));
        assertTrue(singleFieldPartial.isMatch(new DateTime(2022, 1, 1)));
        assertEquals("year=2022", singleFieldPartial.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidChronology() {
        new Partial(ChronoUtils.ISO_INSTANT, new DateTimeFieldType[]{ DateTimeFieldType.year(), DateTimeFieldType.monthOfYear() }, new int[]{2022, 12 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMismatchFieldTypesAndValues() {
        new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.getDefault());
        new Partial(DateTimeFieldType.monthOfYear(), 2022, DateTimeUtils.getDefault());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOrderFieldTypes() {
        new Partial(new DateTimeFieldType[]{ DateTimeFieldType.monthOfYear(), DateTimeFieldType.year() }, new int[]{1, 2 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateFieldTypes() {
        new Partial(new DateTimeFieldType[]{ DateTimeFieldType.year(), DateTimeFieldType.year() }, new int[]{2022, 2022 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullDateTimeFieldType() {
        new Partial(null, 2022, DateTimeUtils.getDefault());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        new Partial(DateTimeFieldType.year(), (short) 0, DateTimeUtils.getDefault());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValue() {
        new Partial(DateTimeFieldType.year(), -1, DateTimeUtils.getDefault());
    }

    @Test
    public void testWithChronologyRetainFields() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.getDefault());
        Partial partialWithChronologyRetainFields = partial.withChronologyRetainFields(DateTimeUtils.getUTC());
        assertEquals(partial, partialWithChronologyRetainFields);
    }

    @Test
    public void testWith() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.getDefault());
        Partial partialWith = new Partial(partial.with(dateTimeFieldType, 2023));
        assertEquals(2, partialWith.size());
        assertEquals(partial.getFieldTypes()[0], partialWith.getFieldTypes()[0]);
        assertEquals(dateTimeFieldType, partialWith.getFieldTypes()[1]);
        assertEquals(2023, partialWith.getValue(1));
        assertTrue(partialWith.isMatch(new DateTime(2023, 1, 1)));
        assertEquals("year=2022, year=2023", partialWith.toString());
    }

    @Test
    public void testWithout() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.getDefault());
        Partial partialWithout = new Partial(partial.without(dateTimeFieldType));
        assertEquals(0, partialWithout.size());
        assertFalse(partialWithout.isMatch(new DateTime(2022, 1, 1)));
        assertNull(partialWithout.getFormatter());
        assertNull(partialWithout.toString());
    }

    @Test
    public void testWithField() {
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.year();
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.getDefault());
        Partial partialWithField = new Partial(partial.withField(dateTimeFieldType, 2023));
        assertEquals(2, partialWithField.size());
        assertEquals(partial.getFieldTypes()[0], partialWithField.getFieldTypes()[0]);
        assertEquals(dateTimeFieldType, partialWithField.getFieldTypes()[1]);
        assertEquals(2023, partialWithField.getValue(1));
        assertTrue(partialWithField.isMatch(new DateTime(2023, 1, 1)));
        assertEquals("year=2022, year=2023", partialWithField.toString());
    }

    @Test
    public void testWithFieldAdded() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.getDefault());
        Partial partialWithFieldAdded = new Partial(partial.withFieldAdded(durationFieldType, 1));
        assertEquals(2, partialWithFieldAdded.size());
        assertEquals(DateTimeFieldType.year(), partialWithFieldAdded.getFieldTypes()[0]);
        assertEquals(durationFieldType, partialWithFieldAdded.getFieldTypes()[1]);
        assertEquals(2022, partialWithFieldAdded.getValue(0));
        assertEquals(1, partialWithFieldAdded.getValue(1));
        assertTrue(partialWithFieldAdded.isMatch(new DateTime(2022, 1, 2)));
        assertEquals("year=2022, days=1", partialWithFieldAdded.toString());
    }

    @Test
    public void testWithFieldAddWrapped() {
        DurationFieldType durationFieldType = DurationFieldType.days();
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.getDefault());
        Partial partialWithFieldAddWrapped = new Partial(partial.withFieldAddWrapped(durationFieldType, 1));
        assertEquals(2, partialWithFieldAddWrapped.size());
        assertEquals(DateTimeFieldType.year(), partialWithFieldAddWrapped.getFieldTypes()[0]);
        assertEquals(durationFieldType, partialWithFieldAddWrapped.getFieldTypes()[1]);
        assertEquals(2022, partialWithFieldAddWrapped.getValue(0));
        assertEquals(1, partialWithFieldAddWrapped.getValue(1));
        assertTrue(partialWithFieldAddWrapped.isMatch(new DateTime(2022, 1, 3)));
        assertEquals("year=2022, days=1", partialWithFieldAddWrapped.toString());
    }

    @Test
    public void testWithPeriodAdded() {
        ReadablePeriod readablePeriod = new GeneralPeriod("P1D", new DateTime(2022, 1, 1), new DateTime(2022, 1, 2));
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.getDefault());
        Partial partialWithPeriodAdded = new Partial(partial.withPeriodAdded(readablePeriod, 1));
        assertEquals(2, partialWithPeriodAdded.size());
        assertEquals(DateTimeFieldType.year(), partialWithPeriodAdded.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.days(), partialWithPeriodAdded.getFieldTypes()[1]);
        assertEquals(2022, partialWithPeriodAdded.getValue(0));
        assertEquals(1, partialWithPeriodAdded.getValue(1));
        assertTrue(partialWithPeriodAdded.isMatch(new DateTime(2022, 1, 3)));
        assertEquals("year=2022, days=1", partialWithPeriodAdded.toString());
    }

    @Test
    public void testPlus() {
        ReadablePeriod readablePeriod = new GeneralPeriod("P1D", new DateTime(2022, 1, 1), new DateTime(2022, 1, 2));
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.getDefault());
        Partial partialPlus = partial.plus(readablePeriod);
        assertEquals(2, partialPlus.size());
        assertEquals(DateTimeFieldType.year(), partialPlus.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.days(), partialPlus.getFieldTypes()[1]);
        assertEquals(2022, partialPlus.getValue(0));
        assertEquals(1, partialPlus.getValue(1));
        assertTrue(partialPlus.isMatch(new DateTime(2022, 1, 3)));
        assertEquals("year=2022, days=1", partialPlus.toString());
    }

    @Test
    public void testMinus() {
        ReadablePeriod readablePeriod = new GeneralPeriod("P1D", new DateTime(2022, 1, 3), new DateTime(2022, 1, 2));
        Partial partial = new Partial(DateTimeFieldType.year(), 2022, DateTimeUtils.getDefault());
        Partial partialMinus = partial.minus(readablePeriod);
        assertEquals(2, partialMinus.size());
        assertEquals(DateTimeFieldType.year(), partialMinus.getFieldTypes()[0]);
        assertEquals(DateTimeFieldType.days(), partialMinus.getFieldTypes()[1]);
        assertEquals(2022, partialMinus.getValue(0));
        assertEquals(-1, partialMinus.getValue(1));
        assertTrue(partialMinus.isMatch(new DateTime(2022, 1, 1)));
        assertEquals("year=2022, days=-1", partialMinus.toString());
    }

    
}