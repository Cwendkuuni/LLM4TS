import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.field.LenientDateTimeField;
import org.joda.time.field.StrictDateTimeField;
import org.joda.time.field.fieldcalculators.DateTimeFields;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class LenientDateTimeFieldTest {

    @Test
    public void testLenientDateTimeField() {
        // Creating a lenient DateTimeField using a Chronology
        Chronology isoChronology = org.joda.time.chrono.ISOChronology.getInstance();
        DateTimeField field = LenientDateTimeField.getInstance(
                new StrictDateTimeField(DateTimeFieldType.DAY_OF_MONTH, isoChronology), isoChronology);

        // Testing basic get and set methods
        LocalDateTime dateTime = new LocalDateTime(2015, 11, 30, 12, 30, 0, 0, isoChronology);
        assertEquals(30, field.get(dateTime.toMillis()));
        long newValue = 31;
        long newDateTime = field.set(dateTime.toMillis(), newValue);
        LocalDateTime newDate = new BaseDateTime(newValue, getType(), isoChronology);
        assertEquals(newDate.getYear(), 2015);
        assertEquals(newDate.getMonthOfYear(), 12);
        assertEquals(newDate.getDayOfMonth(), 31);
        assertEquals(newDate.getHourOfDay(), 12);
        assertEquals(newDate.getMinuteOfHour(), 30);
        assertEquals(newDate.getSecondOfMinute(), 0);
        assertEquals(newDate.getMillisecondOfSecond(), 0);

        // Testing get and set methods with different Chronology
        Chronology gregorian = org.joda.time.chrono.GregorianChronology.getInstance();
        DateTimeField lenientField = LenientDateTimeField.getInstance(field, gregorian);
        LocalDateTime gregorianDateTime = new LocalDateTime(2015, 11, 30, 12, 30, 0, 0, gregorian);
        assertEquals(30, lenientField.get(gregorianDateTime.toMillis()));
        newValue = 32;
        newDateTime = lenientField.set(gregorianDateTime.toMillis(), newValue);
        LocalDateTime newGregorianDate = new BaseDateTime(newValue, getType(), gregorian);
        assertEquals(newGregorianDate.getYear(), 2015);
        assertEquals(newGregorianDate.getMonthOfYear(), 12);
        assertEquals(newGregorianDate.getDayOfMonth(), 31); // Even though the value is out of bounds, Joda-Time adds the difference to the previous value
        assertEquals(newGregorianDate.getHourOfDay(), 12);
        assertEquals(newGregorianDate.getMinuteOfHour(), 30);
        assertEquals(newGregorianDate.getSecondOfMinute(), 0);
        assertEquals(newGregorianDate.getMillisecondOfSecond(), 0);

        // Testing isLenient method
        assertEquals(true, field.isLenient());
        assertNotSame(field, lenientField);
        assertEquals(true, lenientField.isLenient());

        // Testing set method with out of bounds value for different Chronology
        DateTimeField lenientField2 = LenientDateTimeField.getInstance(field, gregorian);
        long outOfBoundsValue = Long.MAX_VALUE;
        long newDateTime3 = lenientField2.set(dateTime.toMillis(), outOfBoundsValue);
        LocalDateTime outOfBoundsDate = new BaseDateTime(newDateTime3, getType(), gregorian);
        assertEquals(2147483647, outOfBoundsDate.getDayOfMonth()); // This value exceeds the maximum possible day of the month for the given year and month
        assertEquals(12, outOfBoundsDate.getMonthOfYear());
        assertEquals(12, outOfBoundsDate.getYear());

        // Testing set method with negative value for different Chronology
        long negativeValue = -1;
        long newDateTime4 = lenientField2.set(dateTime.toMillis(), negativeValue);
        LocalDateTime negativeDate = new BaseDateTime(newDateTime4, getType(), gregorian);
        assertEquals(-1, negativeDate.getDayOfMonth());
        assertEquals(11, negativeDate.getMonthOfYear());
        assertEquals(2015, negativeDate.getYear());

        // Testing set method with same value for different Chronology
        long sameValue = 30;
        long newDateTime5 = lenientField2.set(newDateTime3, sameValue);
        LocalDateTime sameValueDate = new BaseDateTime(newDateTime5, getType(), gregorian);
        assertEquals(30, sameValueDate.getDayOfMonth());
        assertEquals(12, sameValueDate.getMonthOfYear());
        assertEquals(2015, sameValueDate.getYear());

        // Testing addition and subtraction of lenient DateTimeFields
        DateTimeField lenientField3 = LenientDateTimeField.getInstance(field, isoChronology);
        DateTimeField lenientField4 = LenientDateTimeField.getInstance(field, gregorian);
        LenientDateTimeField addField = (LenientDateTimeField) lenientField3.plus(lenientField4);
        assertEquals(true, addField.isLenient());
        LenientDateTimeField subField = (LenientDateTimeField) lenientField3.minus(lenientField4);
        assertEquals(true, subField.isLenient());

        // Testing add and subtract methods with different Chronology
        DateTimeField addLenientField = LenientDateTimeField.getInstance(field, gregorian);
        DateTimeField subLenientField = LenientDateTimeField.getInstance(field, isoChronology);
        LenientDateTimeField addLenientDateTimeField = (LenientDateTimeField) addLenientField.plus(subLenientField);
        LenientDateTimeField subLenientDateTimeField = (LenientDateTimeField) addLenientDateTimeField.minus(subLenientField);
        assertEquals(true, addLenientDateTimeField.isLenient());
        assertEquals(true, subLenientDateTimeField.isLenient());

        // Testing add and subtract methods with DateTimeFieldType.weeks, months, and years
        DateTimeField weeksField = new StrictDateTimeField(DateTimeFieldType.WEEK_OF_YEAR, isoChronology);
        DateTimeField monthsField = new StrictDateTimeField(DateTimeFieldType.MONTH_OF_YEAR, isoChronology);
        DateTimeField yearsField = new StrictDateTimeField(DateTimeFieldType.YEAR, isoChronology);
        DateTimeField plusWeeksField = (LenientDateTimeField) lenientField3.plus(new Period(7, 0, 0).getFields(DateTimeFields.getFields(weeksField)));
        DateTimeField plusMonthsField = (LenientDateTimeField) lenientField3.plus(new Period(1, 0, 0).getFields(DateTimeFields.getFields(monthsField)));
        DateTimeField plusYearsField = (LenientDateTimeField) lenientField3.plus(new Period(1, 0, 0).getFields(DateTimeFields.getFields(yearsField)));
        DateTimeField minusWeeksField = (LenientDateTimeField) lenientField3.minus(new Period(7, 0, 0).getFields(DateTimeFields.getFields(weeksField)));
        DateTimeField minusMonthsField = (LenientDateTimeField) lenientField3.minus(new Period(1, 0, 0).getFields(DateTimeFields.getFields(monthsField)));
        DateTimeField minusYearsField = (LenientDateTimeField) lenientField3.minus(new Period(1, 0, 0).getFields(DateTimeFields.getFields(yearsField)));
        assertEquals(true, plusWeeksField.isLenient());
        assertEquals(true, plusMonthsField.isLenient());
        assertEquals(true, plusYearsField.isLenient());
        assertEquals(true, minusWeeksField.isLenient());
        assertEquals(true, minusMonthsField.isLenient());
        assertEquals(true, minusYearsField.isLenient());

        // Testing add and subtract methods with DurationFieldType
        DurationFieldType days = DurationFieldType.DAYS;
        DurationFieldType hours = DurationFieldType.HOURS;
        DurationFieldType minutes = DurationFieldType.MINUTES;
        DurationFieldType seconds = DurationFieldType.SECONDS;
        DateTimeField addDurationField = (LenientDateTimeField) lenientField3.plus(new Period(7, 12, 30, 0, 0, 0, 0).getFields(DateTimeFields.getFields(days)));
        DateTimeField addHoursField = (LenientDateTimeField) lenientField3.plus(new Period(7, 0, 0, 0, 0).getFields(DateTimeFields.getFields(hours)));
        DateTimeField addMinutesField = (LenientDateTimeField) lenientField3.plus(new Period(7, 0, 30, 0, 0, 0, 0).getFields(DateTimeFields.getFields(minutes)));
        DateTimeField addSecondsField = (LenientDateTimeField) lenientField3.plus(new Period(7, 0, 30, 0, 0, 30, 0).getFields(DateTimeFields.getFields(seconds)));
        DateTimeField subDurationField = (LenientDateTimeField) lenientField3.minus(new Period(7, 12, 30, 0, 0, 0, 0).getFields(DateTimeFields.getFields(days)));
        DateTimeField subHoursField = (LenientDateTimeField) lenientField3.minus(new Period(7, 0, 0, 0, 0).getFields(DateTimeFields.getFields(hours)));
        DateTimeField subMinutesField = (LenientDateTimeField) lenientField3.minus(new Period(7, 0, 30, 0, 0, 0, 0).getFields(DateTimeFields.getFields(minutes)));
        DateTimeField subSecondsField = (LenientDateTimeField) lenientField3.minus(new Period(7, 0, 30, 0, 0, 30, 0).getFields(DateTimeFields.getFields(seconds)));
        assertEquals(true, addDurationField.isLenient());
        assertEquals(true, addHoursField.isLenient());
        assertEquals(true, addMinutesField.isLenient());
        assertEquals(true, addSecondsField.isLenient());
        assertEquals(true, subDurationField.isLenient());
        assertEquals(true, subHoursField.isLenient());
        assertEquals(true, subMinutesField.isLenient());
        assertEquals(true, subSecondsField.isLenient());

        // Testing set methods with out of bounds values and different Chronology
        DateTimeField lenientField5 = LenientDateTimeField.getInstance(field, gregorian);
        long maxValue = Long.MAX_VALUE;
        long newDateTime6 = lenientField5.set(dateTime.toMillis(), maxValue);
        LocalDateTime maxValueDate = new BaseDateTime(newDateTime6, getType(), gregorian);
        assertEquals(Long.MAX_VALUE, maxValueDate.getDayOfMonth()); // This value exceeds the maximum possible day of the month for the given year and month
        assertEquals(12, maxValueDate.getMonthOfYear());
        assertEquals(2015, maxValueDate.getYear());

        long minValue = Long.MIN_VALUE;
        long newDateTime7 = lenientField5.set(dateTime.toMillis(), minValue);
        LocalDateTime minValueDate = new BaseDateTime(newDateTime7, getType(), gregorian);
        assertEquals(Long.MIN_VALUE, minValueDate.getDayOfMonth()); // This value is below the minimum possible day of the month for the given year and month
        assertEquals(11, minValueDate.getMonthOfYear());
        assertEquals(2015, minValueDate.getYear());
    }

    private DateTimeType getType() {
        return DateTimeType.forLayout(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }
}