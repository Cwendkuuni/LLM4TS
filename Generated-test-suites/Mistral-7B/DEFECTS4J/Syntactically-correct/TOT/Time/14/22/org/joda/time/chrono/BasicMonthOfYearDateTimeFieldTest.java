import org.joda.time.BasicDateTime;
import org.joda.time.BasicMonthOfYearDateTimeField;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.ImpreciseDateTimeField;
import org.joda.time.LocalDate;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        Chronology gregorianChronology = DateTimeUtils.gregorianUTC();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(gregorianChronology, DateTimeConstants.JANUARY);

        // Test getter methods
        assertEquals(DateTimeFieldType.monthOfYear(), field.getFieldType());
        assertEquals(gregorianChronology.getAverageMillisPerMonth(), field.getPrecision());

        // Test isLenient() returns false
        assertFalse(field.isLenient());

        // Test get(long) method
        long instant = new BasicDateTime(1546304000000L, gregorianChronology).getMillis();
        int month = field.get(instant);
        assertEquals(5, month); // May

        // Test add(long, int) method
        long newInstant = field.add(instant, 2);
        LocalDate date = new LocalDate(newInstant, gregorianChronology);
        assertEquals(new Date(newInstant), date.toDate());
        assertEquals(7, date.getMonthOfYear()); // July

        // Test add(long, long) method
        long leapYearInstant = new BasicDateTime(1556932800000L, gregorianChronology).getMillis();
        long newLeapInstant = field.add(leapYearInstant, 12);
        LocalDate leapDate = new LocalDate(newLeapInstant, gregorianChronology);
        assertEquals(new Date(newLeapInstant), leapDate.toDate());
        assertEquals(1, leapDate.getMonthOfYear()); // January

        // Test addWrapField(long, int) method
        long wrapAroundInstant = new BasicDateTime(1362998400000L, gregorianChronology).getMillis();
        long newWrapAroundInstant = field.addWrapField(wrapAroundInstant, 3);
        assertEquals(new Date(wrapAroundInstant), new Date(newWrapAroundInstant)); // Same instant
        assertEquals(12, field.get(newWrapAroundInstant)); // December

        // Test getDifferenceAsLong(long, long) method
        long difference = field.getDifferenceAsLong(wrapAroundInstant, newWrapAroundInstant);
        assertEquals(2, difference);

        // Test set(long, int) method
        long newSetInstant = field.set(instant, 7);
        LocalDate setDate = new LocalDate(newSetInstant, gregorianChronology);
        assertEquals(new Date(newSetInstant), setDate.toDate());
        assertEquals(7, setDate.getMonthOfYear()); // July

        // Test getRangeDurationField() method
        DurationField rangeDurationField = field.getRangeDurationField();
        assertEquals(DateTimeFieldType.years(), rangeDurationField.getFieldType());

        // Test isLeap(long) method
        assertTrue(field.isLeap(leapYearInstant));

        // Test getLeapAmount(long) method
        assertEquals(1, field.getLeapAmount(leapYearInstant));

        // Test getLeapDurationField() method
        DurationField leapDurationField = field.getLeapDurationField();
        assertEquals(DateTimeFieldType.days(), leapDurationField.getFieldType());

        // Test getMinimumValue() method
        assertEquals(DateTimeConstants.JANUARY, field.getMinimumValue());

        // Test getMaximumValue() method
        assertEquals(DateTimeConstants.DECEMBER, field.getMaximumValue());

        // Test roundFloor(long) method
        long roundedFloorInstant = field.roundFloor(instant);
        LocalDate roundedFloorDate = new LocalDate(roundedFloorInstant, gregorianChronology);
        assertEquals(new Date(roundedFloorInstant), roundedFloorDate.toDate());
        assertEquals(5, roundedFloorDate.getMonthOfYear()); // May

        // Test remainder(long) method
        long remainderInstant = field.remainder(instant);
        assertEquals(0, remainderInstant);

        // Test serialization
        field = (BasicMonthOfYearDateTimeField) new BasicMonthOfYearDateTimeField(gregorianChronology, DateTimeConstants.JANUARY).clone();
        assertEquals(field.getClass(), BasicMonthOfYearDateTimeField.class);
    }
}