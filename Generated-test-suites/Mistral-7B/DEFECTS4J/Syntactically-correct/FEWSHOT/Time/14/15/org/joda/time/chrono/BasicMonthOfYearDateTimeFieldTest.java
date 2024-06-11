package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationField;
import org.joda.time.ReadablePartial;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.ImpreciseDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for the BasicMonthOfYearDateTimeField class.
 */
public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasic() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        // Get methods
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(chronology.getAverageMillisPerMonth(), field.getMillisPerStep());
        assertEquals(chronology, field.getChronology());

        // Set methods
        long instant = DateTimeUtils.currentTimeMillis();
        long set = field.set(instant, DateTimeConstants.DECEMBER);
        assertEquals(DateTimeConstants.DECEMBER, field.get(set));
        assertEquals(instant + (DateTimeConstants.DECEMBER * chronology.getAverageMillisPerMonth()), set);

        // Add methods
        long add = field.add(instant, 2);
        assertEquals(DateTimeConstants.FEBRUARY, field.get(add));
        assertEquals(instant + (2 * chronology.getAverageMillisPerMonth()), add);

        add = field.add(instant, -2);
        assertEquals(DateTimeConstants.NOVEMBER, field.get(add));
        assertEquals(instant + ((DateTimeConstants.NOVEMBER - DateTimeConstants.DECEMBER) * chronology.getAverageMillisPerMonth()), add);

        // Add wrap methods
        add = field.addWrapField(instant, 13);
        assertEquals(DateTimeConstants.JANUARY, field.get(add));
        assertEquals(instant + ((DateTimeConstants.JANUARY - DateTimeConstants.DECEMBER) * chronology.getAverageMillisPerMonth()), add);

        // Get difference methods
        long instant1 = DateTimeUtils.currentTimeMillis();
        long instant2 = instant1 + (DateTimeConstants.DECEMBER * chronology.getAverageMillisPerMonth());
        long difference = field.getDifferenceAsLong(instant1, instant2);
        assertEquals(-1, difference);
        assertEquals(instant2 - instant1, difference * chronology.getAverageMillisPerMonth());

        difference = field.getDifferenceAsLong(instant2, instant1);
        assertEquals(1, difference);
        assertEquals(instant1 - instant2, difference * chronology.getAverageMillisPerMonth());

        // Check if leap year
        instant = DateTimeUtils.currentTimeMillis();
        assertTrue(field.isLeap(instant));
        assertEquals(DateTimeConstants.DECEMBER, field.get(instant));
        assertEquals(DateTimeConstants.DECEMBER + 1, field.get(field.add(instant, 1)));

        instant -= DateTimeConstants.DAYS_PER_YEAR;
        assertFalse(field.isLeap(instant));
        assertEquals(DateTimeConstants.DECEMBER, field.get(instant));
        assertEquals(DateTimeConstants.DECEMBER, field.get(field.add(instant, 1)));
    }

    @Test
    public void testMinMaxValues() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        // Minimum value
        long instant = field.getMinimumValue();
        assertEquals(DateTimeFieldType.monthOfYear().getMinimumValue(), field.get(instant));
        assertEquals(instant, field.roundFloor(instant));

        // Maximum value
        instant = field.getMaximumValue();
        assertEquals(DateTimeFieldType.monthOfYear().getMaximumValue(), field.get(instant));
        assertEquals(instant, field.roundFloor(instant));
    }

    @Test
    public void testAddWrappedDates() {
        BasicChronology chronology = new GregorianChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);

        long instant = DateTimeUtils.currentTimeMillis();
        long[] values = {instant, instant + (DateTimeConstants.DECEMBER * chronology.getAverageMillisPerMonth())};
        int[] expected = {DateTimeConstants.JANUARY, DateTimeConstants.DECEMBER};

        for (int i = 0; i < 2; i++) {
            int month = field.get(values[i]);
            int[] result = field.add(ReadablePartial.partial(values[i], field), field.getFieldIndex(), new int[]{month}, 1);
            assertEquals(expected[i % 2], result[0]);
            assertEquals(expected[i % 2] + 1, result[1]);
        }
    }
}