import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeConstants;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableLocalDateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.FieldUtils;
import org.joda.time.chrono.GregorianChronology;
import org.junit.Assert;
import org.junit.Test;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        Chronology gregorianChronology = GregorianChronology.getInstance();
        BasicMonthOfYearDateTimeField basicMonthOfYearDateTimeField = new BasicMonthOfYearDateTimeField(gregorianChronology, DateTimeConstants.JANUARY);

        // Testing getters
        Assert.assertFalse(basicMonthOfYearDateTimeField.isLenient());
        Assert.assertEquals(DateTimeFieldType.monthOfYear(), basicMonthOfYearDateTimeField.getType());
        Assert.assertEquals(gregorianChronology.getAverageMillisPerMonth(), basicMonthOfYearDateTimeField.getPrecision());
        Assert.assertEquals(gregorianChronology, basicMonthOfYearDateTimeField.getChronology());
        Assert.assertEquals(DateTimeFieldType.monthOfYear(), basicMonthOfYearDateTimeField.getFieldType());
        Assert.assertEquals(1, basicMonthOfYearDateTimeField.getMaximumValue());

        // Testing get method
        MutableDateTime mutableDateTime = new MutableDateTime(0, 0, 1, 0, 0, 0, 0, gregorianChronology);
        int monthOfYear = basicMonthOfYearDateTimeField.get(mutableDateTime.getMillis());
        Assert.assertEquals(1, monthOfYear);

        // Testing set method
        mutableDateTime.setField(DateTimeFieldType.monthOfYear(), 2);
        long set = mutableDateTime.getMillis();
        Assert.assertEquals(60, basicMonthOfYearDateTimeField.set(set, 2));
        Assert.assertEquals(2, basicMonthOfYearDateTimeField.get(mutableDateTime.getMillis()));

        // Testing add method with int
        mutableDateTime.setField(DateTimeFieldType.year(), 2022);
        mutableDateTime.setField(DateTimeFieldType.monthOfYear(), 1);
        mutableDateTime.setField(DateTimeFieldType.dayOfMonth(), 31);
        long n = mutableDateTime.getMillis();

        long add = basicMonthOfYearDateTimeField.add(n, 12);
        Assert.assertEquals(3240, add);
        Assert.assertEquals(2023, new MutableDateTime(add).getFieldValue(DateTimeFieldType.year()));
        Assert.assertEquals(2, new MutableDateTime(add).getFieldValue(DateTimeFieldType.monthOfYear()));

        // Testing add method with long
        long addLong = basicMonthOfYearDateTimeField.add(n, 24 * 60 * 60 * 1000L * 30);
        Assert.assertEquals(3307200000L, addLong);
        Assert.assertEquals(2023, new MutableDateTime(addLong).getFieldValue(DateTimeFieldType.year()));
        Assert.assertEquals(5, new MutableDateTime(addLong).getFieldValue(DateTimeFieldType.monthOfYear()));

        // Testing addWrapField method
        long addWrapField = basicMonthOfYearDateTimeField.addWrapField(n + 365243200000L, 12);
        Assert.assertEquals(3240, addWrapField);

        // Testing getDifferenceAsLong method
        long set2 = basicMonthOfYearDateTimeField.set(n, 5);
        long difference = basicMonthOfYearDateTimeField.getDifferenceAsLong(set, set2);
        Assert.assertEquals(11, difference);

        // Testing set method with out of bounds values
        long set3 = basicMonthOfYearDateTimeField.set(set2, 13);
        Assert.assertEquals(12, basicMonthOfYearDateTimeField.get(set3));

        // Testing isLeap method
        mutableDateTime.setField(DateTimeFieldType.year(), 2024);
        mutableDateTime.setField(DateTimeFieldType.monthOfYear(), 2);
        boolean isLeap = basicMonthOfYearDateTimeField.isLeap(mutableDateTime.getMillis());
        Assert.assertTrue(isLeap);

        // Testing getLeapAmount method
        int leapAmount = basicMonthOfYearDateTimeField.getLeapAmount(mutableDateTime.getMillis());
        Assert.assertEquals(1, leapAmount);

        // Testing getLeapDurationField method
        DurationField leapDurationField = basicMonthOfYearDateTimeField.getLeapDurationField();
        Assert.assertEquals(DateTimeFieldType.days(), leapDurationField.getType());

        // Testing getMinimumValue method
        int minimumValue = basicMonthOfYearDateTimeField.getMinimumValue();
        Assert.assertEquals(1, minimumValue);

        // Testing getMaximumValue method
        int maximumValue = basicMonthOfYearDateTimeField.getMaximumValue();
        Assert.assertEquals(12, maximumValue);

        // Testing roundFloor method
        long roundFloor = basicMonthOfYearDateTimeField.roundFloor(n);
        Assert.assertEquals(1125300000000L, roundFloor);

        // Testing remainder method
        long remainder = basicMonthOfYearDateTimeField.remainder(n);
        Assert.assertEquals(1508400000L, remainder);

        // Testing readResolve method
        Object readResolve = basicMonthOfYearDateTimeField.readResolve();
        Assert.assertTrue(readResolve instanceof DateTimeField);

        // Testing add method with ReadablePartial
        LocalDateTime localDateTime = new LocalDateTime(2022, 1, 1, 0, 0);
        long[] newMillis = basicMonthOfYearDateTimeField.add(localDateTime.toDate(), 12, new long[3], 0);
        Assert.assertEquals(3240, newMillis[0]);
        Assert.assertEquals(2, newMillis[1]);
        Assert.assertEquals(1, newMillis[2]);
    }
}