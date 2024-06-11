import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.Period;
import org.joda.time.chrono.ISOChronology;
import org.junit.Assert;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        final Chronology chronology = new ISOChronology();
        final BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, 2);

        // isLenient()
        Assert.assertFalse(field.isLenient());

        // get()
        long date = new LocalDate(2022, 2, 28).toDate().getTime();
        Assert.assertEquals(2, field.get(date));

        // add() with int
        long newDate = field.add(date, 1);
        Assert.assertEquals(2022, getYear(newDate));
        Assert.assertEquals(3, field.get(newDate));
        Assert.assertEquals(29, getDayOfMonth(newDate));

        // add() with long
        newDate = field.add(date, 365);
        Assert.assertEquals(2023, getYear(newDate));
        Assert.assertEquals(1, field.get(newDate));

        // addWrapField()
        long wrappedDate = field.addWrapField(newDate, 6);
        Assert.assertEquals(2, field.get(wrappedDate));

        // getDifferenceAsLong()
        long otherDate = new LocalDate(2022, 1, 1).toDate().getTime();
        Assert.assertEquals(364, field.getDifferenceAsLong(date, otherDate));

        // set()
        long newDate2 = field.set(date, 5);
        Assert.assertEquals(2022, getYear(newDate2));
        Assert.assertEquals(5, field.get(newDate2));
        Assert.assertEquals(31, getDayOfMonth(newDate2));

        // getRangeDurationField()
        DurationField rangeDurationField = field.getRangeDurationField();
        Assert.assertEquals(DateTimeFieldType.years(), rangeDurationField.getType());
        Assert.assertEquals(0, rangeDurationField.getSize());

        // isLeap()
        Assert.assertFalse(field.isLeap(date));

        // getLeapAmount()
        Assert.assertEquals(0, field.getLeapAmount(date));

        // getLeapDurationField()
        DurationField leapDurationField = field.getLeapDurationField();
        Assert.assertEquals(DateTimeFieldType.days(), leapDurationField.getType());
        Assert.assertEquals(0, leapDurationField.getSize());

        // getMinimumValue()
        Assert.assertEquals(1, field.getMinimumValue());

        // getMaximumValue()
        Assert.assertEquals(12, field.getMaximumValue());

        // roundFloor()
        long floor = field.roundFloor(date);
        Assert.assertEquals(2022, getYear(floor));
        Assert.assertEquals(2, field.get(floor));
        Assert.assertEquals(28, getDayOfMonth(floor));

        // remainder()
        long remainder = field.remainder(date);
        Assert.assertEquals(0, remainder);
    }

    private int getDayOfMonth(long date) {
        LocalDateTime localDateTime = new LocalDateTime(date);
        return localDateTime.getDayOfMonth();
    }

    private int getYear(long date) {
        LocalDateTime localDateTime = new LocalDateTime(date);
        return localDateTime.getYear();
    }
}