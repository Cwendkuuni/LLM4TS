import org.joda.time.BasicDateTime;
import org.joda.time.BasicFieldUtils;
import org.joda.time.BasicStableInstant;
import org.joda.time.Chronology;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ImmutableDateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalDateTimeFieldVerifier;
import org.joda.time.LocalDateTimeFieldType;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.StableInstant;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthOfYearDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearField() {
        Chronology chrono = ISOChronology.INSTANCE;
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(chrono, null);
        DurationField df = field.getRangeDurationField();
        int maxYear = 2050, minYear = -1000;
        for (int i = minYear; i <= maxYear; i++) {
            int maxMonth = chrono.getMaximumValue(field, i);
            int minMonth = chrono.getMinimumValue(field, i);
            assertEquals("Minimum value for year " + i, minMonth, 1);
            assertEquals("Maximum value for year " + i, maxMonth, 12);
            // Test get methods
            assertEquals("Get field value for year " + i + ", month 1", 1, field.get(i, new BasicDateTime(i, 1, 1, 0, 0, 0, 0).getMillis()));
            assertEquals("Get field value for year " + i + ", month 12", 12, field.get(i, new BasicDateTime(i, 12, 31, 0, 0, 0, 0).getMillis()));
            // Test set methods
            long setValue = field.set(i, 5, new BasicDateTime(i, 2, 29, 0, 0, 0, 0).getMillis());
            assertEquals("Set method for year " + i + ", month 5, day 29", 1, field.get(setValue));
            // Test add methods
            long addValue = field.add(new BasicDateTime(i, 1, 1, 0, 0, 0, 0).getMillis(), 11);
            long addWrapValue = field.addWrapField(new BasicDateTime(i, 1, 1, 0, 0, 0, 0).getMillis(), 13);
            assertEquals("Add methods for year " + i + ", month 11", 12, field.get(addValue));
            assertEquals("AddWrapField method for year " + i + ", month 13", 1, field.get(addWrapValue));
            // Test DurationField method
            Period p = new Period(df.getAmount(new BasicDateTime(i, 1, 1, 0, 0, 0, 0).getMillis()), df.getAmount(new BasicDateTime(i, 12, 31, 0, 0, 0, 0).getMillis()));
            assertEquals("DurationField method for year " + i, 354, p.getStandardDays());
        }
    }

    @Test
    public void testBasicMonthOfYearFieldAddMonths() {
        Chronology chrono = ISOChronology.INSTANCE;
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(chrono, null);
        DurationField df = field.getRangeDurationField();
        int maxYear = 2050, minYear = -1000;
        for (int i = minYear; i <= maxYear; i++) {
            long testValue = new BasicDateTime(i, 1, 1, 0, 0, 0, 0).getMillis();
            long testAddValue = testValue + 12 * Months.MONTHS;
            int testMonth = field.get(testAddValue);
            assertEquals("Add months method for year " + i, 13, testMonth);
            long testAddWrapValue = testValue + 13 * Months.MONTHS;
            int testAddWrapMonth = field.get(testAddWrapValue);
            assertEquals("AddWrapMonths method for year " + i, 1, testAddWrapMonth);
        }
    }

    @Test
    public void testBasicMonthOfYearFieldAddWrapField() {
        Chronology chrono = ISOChronology.INSTANCE;
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(chrono, null);
        int maxYear = 2050, minYear = -1000;
        for (int i = minYear; i <= maxYear; i++) {
            long testValue = new BasicDateTime(i, 1, 1, 0, 0, 0, 0).getMillis();
            long testAddWrapValue = testValue + 13 * Months.MONTHS;
            long testAddWrapValue2 = testValue + 14 * Months.MONTHS;
            int testAddWrapMonth = field.get(testAddWrapValue);
            int testAddWrapMonth2 = field.get(testAddWrapValue2);
            assertEquals("AddWrapMonths method for year " + i + ", month 14", 1, testAddWrapMonth2);
            assertTrue("AddWrapMonths method for year " + i + ", month 14 should be wrapped", testAddWrapMonth == 1 || testAddWrapMonth == 13);
            assertTrue("AddWrapMonths method for year " + i + ", month 14 should be wrapped", testAddWrapMonth2 == 1 || testAddWrapMonth2 == 13);
        }
    }

    @Test
    public void testBasicMonthOfYearFieldSet() {
        Chronology chrono = ISOChronology.INSTANCE;
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(chrono, null);
        int maxYear = 2050, minYear = -1000;
        for (int i = minYear; i <= maxYear; i++) {
            long testValue = new BasicDateTime(i, 1, 1, 0, 0, 0, 0).getMillis();
            int testMonth = 2;
            long testSetValue = field.set(testValue, testMonth);
            assertEquals("Set method for year " + i + ", month 2", testValue, testSetValue);
            assertEquals("Get method for year " + i + ", month 2", testMonth, field.get(testSetValue));
            long testSetValue2 = field.set(testValue, 13);
            assertEquals("Set method for year " + i + ", month 13", testValue + 11 * Months.MONTHS, testSetValue2);
            assertEquals("Get method for year " + i + ", month 13", 13, field.get(testSetValue2));
        }
    }

    @Test
    public void testBasicMonthOfYearFieldGetDifference() {
        Chronology chrono = ISOChronology.INSTANCE;
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(chrono, null);
        int maxYear = 2050, minYear = -1000;
        for (int i = minYear; i <= maxYear; i++) {
            LocalDateTimeFieldType fieldType = LocalDateTimeFieldType.monthOfYear();
            LocalDateTimeFieldVerifier verifier = new LocalDateTimeFieldVerifier(fieldType);
            LocalDateTime dateMin = LocalDateTime.of(i, 1, 1, 0, 0);
            LocalDateTime dateMax = LocalDateTime.of(i, 12, 31, 0, 0);
            long testValueMin = dateMin.toInstant().toEpochMilli();
            long testValueMax = dateMax.toInstant().toEpochMilli();
            long testDifference = field.getDifferenceAsLong(testValueMin, testValueMax);
            int expectedDifference = (field.getMaximumValue(i) - field.getMinimumValue(i)) + 1;
            assertEquals("GetDifference method for year " + i, expectedDifference, (int) testDifference);
        }
    }

    @Test
    public void testBasicMonthOfYearFieldGetLeapAmount() {
        Chronology chrono = ISOChronology.INSTANCE;
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(chrono, null);
        int maxYear = 2050, minYear = -1000;
        for (int i = minYear; i <= maxYear; i++) {
            int leapMonth = 2;
            if (chrono.isLeapYear(i)) {
                int testLeapAmount = field.getLeapAmount(new BasicDateTime(i, 1, 1, 0, 0, 0, 0).getMillis());
                assertEquals("GetLeapAmount method for year " + i + ", month " + leapMonth, 1, testLeapAmount);
            } else {
                int testLeapAmount = field.getLeapAmount(new BasicDateTime(i, 1, 1, 0, 0, 0, 0).getMillis());
                assertEquals("GetLeapAmount method for year " + i + ", month " + leapMonth, 0, testLeapAmount);
            }
        }
    }

    @Test
    public void testBasicMonthOfYearFieldIsLeap() {
        Chronology chrono = ISOChronology.INSTANCE;
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(chrono, null);
        int maxYear = 2050, minYear = -1000;
        for (int i = minYear; i <= maxYear; i++) {
            int leapMonth = 2;
            long testValue = new BasicDateTime(i, 1, 1, 0, 0, 0, 0).getMillis();
            boolean testIsLeap = field.isLeap(testValue);
            if (chrono.isLeapYear(i)) {
                assertTrue("IsLeap method for year " + i + ", month " + leapMonth, testIsLeap);
            } else {
                assertFalse("IsLeap method for year " + i + ", month " + leapMonth, testIsLeap);
            }
        }
    }

    @Test
    public void testBasicMonthOfYearFieldGetLeapDurationField() {
        Chronology chrono = ISOChronology.INSTANCE;
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(chrono, null);
        int maxYear = 2050, minYear = -1000;
        for (int i = minYear; i <= maxYear; i++) {
            int leapMonth = 2;
            if (chrono.isLeapYear(i)) {
                DurationField testLeapDurationField = field.getLeapDurationField();
                assertEquals("GetLeapDurationField method for year " + i, DurationFieldType.days(), testLeapDurationField.getType());
                assertEquals("GetLeapDurationField method for year " + i, 1, testLeapDurationField.getAmount(new BasicDateTime(i, 1, leapMonth, 0, 0, 0, 0).getMillis()));
            }
        }
    }
}