package org.joda.time.test;

import org.joda.time.*;
import org.joda.time.base.*;
import org.joda.time.chrono.*;
import org.joda.time.field.*;
import org.joda.time.format.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {

    @Test
    public void testEmptyPartial() {
        Partial emptyPartial = new Partial();
        assertEquals(0, emptyPartial.size());
        assertNull(emptyPartial.getChronology());
        assertArrayEquals(new int[0], emptyPartial.getValues());
    }

    @Test
    public void testPartialWithSingleField() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = ISODate.YEAR;
        Partial partial = new Partial(dateTimeFieldType, 2022, chronology);
        assertEquals(1, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(dateTimeFieldType, partial.getFieldType(0));
        assertEquals(2022, partial.getValue(0));
        assertEquals(2022, partial.getValues()[0]);
    }

    @Test
    public void testPartialWithMultipleFields() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = ISODate.YEAR;
        DateTimeFieldType dateTimeFieldType2 = ISODate.MONTH_OF_YEAR;
        int[] values = new int[]{2022, 11};
        Partial partial = new Partial(dateTimeFieldType1, dateTimeFieldType2, values, chronology);
        assertEquals(2, partial.size());
        assertEquals(chronology, partial.getChronology());
        assertEquals(dateTimeFieldType1, partial.getFieldType(0));
        assertEquals(2022, partial.getValue(0));
        assertEquals(2022, partial.getValues()[0]);
        assertEquals(dateTimeFieldType2, partial.getFieldType(1));
        assertEquals(11, partial.getValue(1));
        assertEquals(11, partial.getValues()[1]);
    }

    @Test
    public void testPartialWithNullFieldType() {
        try {
            new Partial(null, 2022, GregorianChronology.getInstance());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithNullValues() {
        try {
            new Partial(ISODate.YEAR, null, GregorianChronology.getInstance());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithInvalidValues() {
        try {
            Chronology chronology = GregorianChronology.getInstance();
            DateTimeFieldType dateTimeFieldType = ISODate.YEAR;
            int[] values = new int[]{2022, 32};
            new Partial(dateTimeFieldType, values, chronology);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithUnsupportedFieldType() {
        try {
            Chronology chronology = GregorianChronology.getInstance();
            DateTimeFieldType dateTimeFieldType = ISODate.YEAR;
            DateTimeFieldType unsupportedFieldType = new BaseDateTimeFieldType("UnsupportedFieldType") {
                @Override
                public DurationField getDurationType() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            new Partial(new DateTimeFieldType[]{dateTimeFieldType, unsupportedFieldType}, new int[2], chronology);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithDuplicateFields() {
        try {
            Chronology chronology = GregorianChronology.getInstance();
            DateTimeFieldType dateTimeFieldType = ISODate.YEAR;
            new Partial(new DateTimeFieldType[]{dateTimeFieldType, dateTimeFieldType}, new int[2], chronology);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithInvalidFieldOrder() {
        try {
            Chronology chronology = GregorianChronology.getInstance();
            DateTimeFieldType dateTimeFieldType1 = ISODate.YEAR;
            DateTimeFieldType dateTimeFieldType2 = ISODate.MONTH_OF_YEAR;
            new Partial(new DateTimeFieldType[]{dateTimeFieldType2, dateTimeFieldType1}, new int[2], chronology);
            fail("Should have throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithNullChronology() {
        try {
            new Partial(null);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithInvalidChronology() {
        try {
            new Partial(new CJKChronology());
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPartialWithReadablePartial() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = ISODate.YEAR;
        DateTimeFieldType dateTimeFieldType2 = ISODate.MONTH_OF_YEAR;
        Partial partial1 = new Partial(dateTimeFieldType1, 2022, chronology);
        Partial partial2 = new Partial(dateTimeFieldType2, 11, chronology);
        Partial partialCopy = new Partial(partial1, partial2.getValues());
        assertEquals(2, partialCopy.size());
        assertEquals(2022, partialCopy.getValue(0));
        assertEquals(11, partialCopy.getValue(1));
    }

    @Test
    public void testPartialWithClone() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType dateTimeFieldType = ISODate.YEAR;
        Partial partial = new Partial(dateTimeFieldType, 2022, chronology);
        Partial partialCopy = partial.clone();
        assertEquals(partial, partialCopy);
        partialCopy.setValue(0, 2023);
        assertNotEquals(partial, partialCopy);
    }

    @Test
    public void testPartialWithValidValues() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = ISODate.YEAR;
        DateTimeFieldType dateTimeFieldType2 = ISODate.MONTH_OF_YEAR;
        DateTimeFieldType dateTimeFieldType3 = ISODate.DAY_OF_MONTH;
        int[] values = new int[]{2022, 11, 31};
        Partial partial = new Partial(dateTimeFieldType1, dateTimeFieldType2, dateTimeFieldType3, values, chronology);
        assertEquals(3, partial.size());
        assertEquals(2022, partial.getValue(0));
        assertEquals(11, partial.getValue(1));
        assertEquals(31, partial.getValue(2));
        assertEquals(Arrays.asList(dateTimeFieldType1, dateTimeFieldType2, dateTimeFieldType3), Arrays.asList(partial.getFieldTypes()));
    }

    @Test
    public void testPartialWithValidValuesWithUTC() {
        Chronology chronology = GregorianChronology.getInstance().withUTC();
        DateTimeFieldType dateTimeFieldType1 = ISODate.YEAR;
        DateTimeFieldType dateTimeFieldType2 = ISODate.MONTH_OF_YEAR;
        DateTimeFieldType dateTimeFieldType3 = ISODate.DAY_OF_MONTH;
        int[] values = new int[]{2022, 11, 31};
        Partial partial = new Partial(dateTimeFieldType1, dateTimeFieldType2, dateTimeFieldType3, values, chronology);
        assertEquals(3, partial.size());
        assertEquals(2022, partial.getValue(0));
        assertEquals(11, partial.getValue(1));
        assertEquals(31, partial.getValue(2));
        assertEquals(Arrays.asList(dateTimeFieldType1, dateTimeFieldType2, dateTimeFieldType3), Arrays.asList(partial.getFieldTypes()));
    }

    @Test
    public void testPartialWithValidValuesWithCustomChronology() {
        Chronology customChronology = new ISOChronology();
        DateTimeFieldType dateTimeFieldType1 = ISODate.YEAR;
        DateTimeFieldType dateTimeFieldType2 = ISODate.MONTH_OF_YEAR;
        DateTimeFieldType dateTimeFieldType3 = ISODate.DAY_OF_MONTH;
        int[] values = new int[]{2022, 11, 31};
        Partial partial = new Partial(dateTimeFieldType1, dateTimeFieldType2, dateTimeFieldType3, values, customChronology);
        assertEquals(3, partial.size());
        assertEquals(2022, partial.getValue(0));
        assertEquals(11, partial.getValue(1));
        assertEquals(31, partial.getValue(2));
        assertEquals(Arrays.asList(dateTimeFieldType1, dateTimeFieldType2, dateTimeFieldType3), Arrays.asList(partial.getFieldTypes()));
    }

    @Test
    public void testGetFormatter() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = ISODate.YEAR;
        DateTimeFieldType dateTimeFieldType2 = ISODate.MONTH_OF_YEAR;
        int[] values = new int[]{2022, 11};
        Partial partial = new Partial(dateTimeFieldType1, dateTimeFieldType2, values, chronology);
        DateTimeFormatter formatter = partial.getFormatter();
        assertNotNull(formatter);
    }

    @Test
    public void testToString() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = ISODate.YEAR;
        DateTimeFieldType dateTimeFieldType2 = ISODate.MONTH_OF_YEAR;
        int[] values = new int[]{2022, 11};
        Partial partial = new Partial(dateTimeFieldType1, dateTimeFieldType2, values, chronology);
        assertEquals("[year=2022, monthOfYear=11]", partial.toString());
    }

    @Test
    public void testToStringList() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = ISODate.YEAR;
        DateTimeFieldType dateTimeFieldType2 = ISODate.MONTH_OF_YEAR;
        int[] values = new int[]{2022, 11};
        Partial partial = new Partial(dateTimeFieldType1, dateTimeFieldType2, values, chronology);
        assertEquals("[year=2022, monthOfYear=11]", partial.toStringList());
    }

    @Test
    public void testToStringWithFormat() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = ISODate.YEAR;
        DateTimeFieldType dateTimeFieldType2 = ISODate.MONTH_OF_YEAR;
        int[] values = new int[]{2022, 11};
        Partial partial = new Partial(dateTimeFieldType1, dateTimeFieldType2, values, chronology);
        assertEquals("yyyy-MM", partial.toString("yyyy-MM"));
    }

    @Test
    public void testToStringWithLocale() {
        Chronology chronology = GregorianChronology.getInstance();
        DateTimeFieldType dateTimeFieldType1 = ISODate.YEAR;
        DateTimeFieldType dateTimeFieldType2 = ISODate.MONTH_OF_YEAR;
        int[] values = new int[]{2022, 11};
        Partial partial = new Partial(dateTimeFieldType1, dateTimeFieldType2, values, chronology);
        assertEquals("2022-11", partial.toString("yyyy-MM", Locale.US));
    }
}