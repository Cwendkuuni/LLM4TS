package org.example.lenientdatetimefieldtest;

import org.junit.jupiter.api.Assertions;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.DateTimeConstants;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;

public class LenientDateTimeFieldTest {

    @Test
    public void testGetInstance() {
        DateTimeField field = DateTimeFieldType.DAY_OF_MONTH;
        Chronology base = DateTimeConstants.UTC;
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, base);

        Assertions.assertNotNull(lenientField);
        Assertions.assertTrue(lenientField.isLenient());
    }

    LenientDateTimeField lenientField;

    @BeforeEach
    public void setUp() {
        lenientField = new LenientDateTimeField(DateTimeFieldType.DAY_OF_MONTH, DateTimeConstants.UTC);
    }

    @Test
    public void testIsLenient() {
        Assertions.assertTrue(lenientField.isLenient());
    }

    @Test
    public void testSet() {
        long instant = DateTimeConstants.MILLIS_IN_DAY;
        int value = DateTimeConstants.DAYS_PER_MONTH + 1;

        long result = lenientField.set(instant, value);

        LocalDate date = new LocalDate(result);
        int expectedDayOfMonth = DateTimeConstants.DAYS_PER_MONTH + 1;
        Assertions.assertEquals(expectedDayOfMonth, date.getDayOfMonth());
    }

    @Test
    public void testSetOutsideBounds() {
        long instant = DateTimeConstants.MILLIS_IN_DAY;
        int value = DateTimeConstants.DAYS_PER_MONTH * 2;

        long result = lenientField.set(instant, value);

        LocalDate date = new LocalDate(result);
        int expectedDayOfMonth = DateTimeConstants.DAYS_PER_MONTH * 2;
        Assertions.assertEquals(expectedDayOfMonth, date.getDayOfMonth());
    }

    @Test
    public void testSetMinMaxBounds() {
        long instant = DateTimeConstants.MILLIS_IN_DAY;
        int value = DateTimeConstants.DAYS_PER_MONTH;

        long result = lenientField.set(instant, value);

        LocalDate date = new LocalDate(result);
        int expectedDayOfMonth = DateTimeConstants.DAYS_PER_MONTH;
        Assertions.assertEquals(expectedDayOfMonth, date.getDayOfMonth());

        value = 0;
        result = lenientField.set(instant, value);

        date = new LocalDate(result);
        Assertions.assertEquals(DateTimeConstants.MIN_DAY_OF_MONTH, date.getDayOfMonth());
    }

    @Test
    public void testSetDateRollover() {
        long instant = DateTimeConstants.MILLIS_IN_DAY;
        int value = -DateTimeConstants.DAYS_PER_MONTH;

        long result = lenientField.set(instant, value);

        LocalDate date = new LocalDate(result);
        int expectedDayOfMonth = DateTimeConstants.DAYS_PER_MONTH + Math.abs(value);
        Assertions.assertEquals(expectedDayOfMonth, date.getDayOfMonth());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        DateTimeField field = null;
        Chronology base = DateTimeConstants.UTC;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
    }

    @Test(expected = NullPointerException.class)
    public void testNullChronology() {
        DateTimeField field = DateTimeFieldType.DAY_OF_MONTH;
        Chronology base = null;
        LenientDateTimeField lenientField = new LenientDateTimeField(field, base);
    }
}