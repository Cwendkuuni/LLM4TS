import org.joda.time.BaseDateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.JodaTimeTestCase;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.MonthLeniency;
import org.joda.time.field.MonthOfYearDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest extends JodaTimeTestCase {

    public void testBasicMonthOfYearDateTimeField() {
        BasicChronology gregorian = new BasicGregorianChronology();
        MonthOfYearDateTimeField field = new MonthOfYearDateTimeField(gregorian, 2); // February

        // Test isLenient()
        assertFalse(field.isLenient());

        // Test get()
        long instant = BaseDateTime.UNIX_EPOCH;
        int month = field.get(instant);
        assertEquals(1, month); // January
        instant += DAYS_PER_MONTH;
        month = field.get(instant);
        assertEquals(2, month); // February
        instant += DAYS_PER_MONTH;
        month = field.get(instant);
        assertEquals(3, month); // March

        // Test add()
        instant += DAYS_PER_MONTH;
        long newInstant = field.add(instant, 1);
        assertEquals(4, field.get(newInstant)); // April

        // Test addWrapField()
        newInstant += DAYS_PER_MONTH;
        newInstant = field.addWrapField(newInstant, 1);
        assertEquals(5, field.get(newInstant)); // May

        instant += DAYS_PER_MONTH;
        newInstant = field.addWrapField(newInstant, 12);
        assertEquals(6, field.get(newInstant)); // June

        // Test getDifferenceAsLong()
        long difference = field.getDifferenceAsLong(instant, newInstant);
        assertEquals(DAYS_PER_MONTH, difference);

        // Test set()
        long setInstant = field.set(instant, 13); // Invalid month
        assertEquals(instant, setInstant);
        setInstant = field.set(instant, 0); // January
        assertEquals(instant - DAYS_PER_MONTH, setInstant);

        // Test getRangeDurationField()
        assertEquals(FieldUtils.years(1), field.getRangeDurationField());

        // Test isLeap() and getLeapAmount()
        long leapInstant = instant + DAYS_PER_YEAR;
        assertTrue(field.isLeap(leapInstant));
        assertEquals(1, field.getLeapAmount(leapInstant));

        // Test getLeapDurationField()
        assertEquals(FieldUtils.days(1), field.getLeapDurationField());

        // Test getMinimumValue() and getMaximumValue()
        assertNotNull(field.getMinimumValue());
        assertEquals(1, field.getMinimumValue());
        assertNotNull(field.getMaximumValue());
        assertEquals(12, field.getMaximumValue());

        // Test roundFloor() and remainder()
        long roundFloorInstant = field.roundFloor(instant);
        assertEquals(instant - (instant % DAYS_PER_MONTH), roundFloorInstant);
        long remainderInstant = field.remainder(instant);
        assertEquals(instant % DAYS_PER_MONTH, remainderInstant);

        // Test serialization
        field = (MonthOfYearDateTimeField) super.readObject(new ByteArrayInputStream(
                new ByteArrayResource("BasicMonthOfYearDateTimeFieldTest-serial.out").readAllBytes()));
        assertEquals(2, field.getLeapMonth());
        assertEquals(gregorian, field.getChronology());
    }

    @Override
    protected byte[] getSerialData() {
        return new ByteArrayResource("BasicMonthOfYearDateTimeFieldTest-serial.out").readAllBytes();
    }

    @Override
    protected Class<?> getTestedClass() {
        return BasicMonthOfYearDateTimeField.class;
    }

    @Override
    protected String getTestedClassName() {
        return "org.joda.time.chrono.BasicMonthOfYearDateTimeField";
    }

    @Override
    protected MonthLeniency getLeniency() {
        return MonthLeniency.STRICT;
    }
}