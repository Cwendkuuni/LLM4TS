import org.joda.time.BaseDateTime;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.TestUtils;
import org.joda.time.chrono.BasicChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.JulianField;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BasicMonthOfYearDateTimeFieldTest {

    private static final Chronology CHRONOLOGY = new BasicChronology("test", "TestCalendar") {
        @Override
        public int getMaxYear() {
            return 3000;
        }

        @Override
        public int getMinYear() {
            return -3000;
        }

        @Override
        public int getDaysInYearMonth(int year, int month) {
            return (month % 2 == 0) ? 30 : 31;
        }

        @Override
        public int getMonthsInYear(int year) {
            return 12;
        }

        @Override
        public int getLeapMonth() {
            return 2;
        }
    };

    @Test
    public void testBasicMonthOfYearDateTimeField() {
        DateTimeFieldType fieldType = DateTimeFieldType.monthOfYear();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(CHRONOLOGY, 2);

        // isLenient
        assertFalse(field.isLenient());

        // get
        LocalDateTime dt = new LocalDateTime(Instant.now());
        assertEquals(1, field.get(dt.getMillis()));

        LocalDateTime dt2 = new LocalDateTime(2023, 2, 29, 12, 0, 0, 0);
        assertEquals(2, field.get(dt2.getMillis()));

        LocalDateTime dt3 = new LocalDateTime(2023, 1, 31, 12, 0, 0, 0);
        assertEquals(1, field.get(dt3.getMillis()));

        // add
        LocalDateTime dt4 = new LocalDateTime(2023, 1, 1, 12, 0, 0, 0);
        LocalDateTime dt5 = field.add(dt4.getMillis(), 10);
        assertEquals(11, field.get(dt5.getMillis()));

        LocalDateTime dt6 = new LocalDateTime(2023, 12, 31, 12, 0, 0, 0);
        LocalDateTime dt7 = field.add(dt6.getMillis(), -5);
        assertEquals(7, field.get(dt7.getMillis()));

        // add(long, long)
        LocalDateTime dt8 = new LocalDateTime(2023, 1, 1, 12, 0, 0, 0);
        LocalDateTime dt9 = field.add(dt8.getMillis(), 10L);
        assertEquals(11, field.get(dt9.getMillis()));

        LocalDateTime dt10 = new LocalDateTime(2023, 12, 31, 12, 0, 0, 0);
        LocalDateTime dt11 = field.add(dt10.getMillis(), -5L);
        assertEquals(7, field.get(dt11.getMillis()));

        // add(ReadablePartial, int[], int)
        LocalDate date = new LocalDate(2023, 1, 1);
        ReadablePartial partial = date.toLocalDateTime().toDateTimeMutable().getField(fieldType).asReadOnlyPartial();
        int[] values = {1, 2};
        int valueToAdd = 3;
        LocalDateTime dt12 = field.add(partial, fieldType.getIndex(), values, valueToAdd);
        assertEquals(4, field.get(dt12.getMillis()));

        // addWrapField
        LocalDateTime dt13 = new LocalDateTime(2023, 1, 1, 12, 0, 0, 0);
        LocalDateTime dt14 = field.addWrapField(dt13.getMillis(), 13);
        assertEquals(14, field.get(dt14.getMillis()));

        // getDifferenceAsLong
        LocalDateTime dt15 = new LocalDateTime(2023, 1, 1, 12, 0, 0, 0);
        LocalDateTime dt16 = new LocalDateTime(2023, 11, 30, 12, 0, 0, 0);
        assertEquals(10, field.getDifferenceAsLong(dt15.getMillis(), dt16.getMillis()));

        // set
        LocalDateTime dt17 = new LocalDateTime(2023, 1, 1, 12, 0, 0, 0);
        LocalDateTime dt18 = field.set(dt17.getMillis(), 12);
        assertEquals(12, field.get(dt18.getMillis()));

        // getRangeDurationField
        DurationField rangeField = field.getRangeDurationField();
        assertEquals(DurationFieldType.years(), rangeField.getFieldType());

        // isLeap
        LocalDateTime dt19 = new LocalDateTime(2023, 2, 28, 12, 0, 0, 0);
        assertTrue(field.isLeap(dt19.getMillis()));

        // getLeapAmount
        LocalDateTime dt20 = new LocalDateTime(2023, 2, 29, 12, 0, 0, 0);
        assertEquals(1, field.getLeapAmount(dt20.getMillis()));

        // getLeapDurationField
        DurationField leapField = field.getLeapDurationField();
        assertEquals(DurationFieldType.days(), leapField.getFieldType());

        // getMinimumValue
        assertEquals(1, field.getMinimumValue());

        // getMaximumValue
        assertEquals(12, field.getMaximumValue());

        // roundFloor
        LocalDateTime dt21 = new LocalDateTime(2023, 1, 1, 12, 0, 0, 0);
        assertEquals(TestUtils.getMillisForTest(2023, 1, 1), field.roundFloor(dt21.getMillis()));

        // remainder
        LocalDateTime dt22 = new LocalDateTime(2023, 1, 1, 12, 0, 0, 0);
        assertEquals(0, field.remainder(dt22.getMillis()));

        // readResolve
        Object obj = new Serializable() {
            private static final long serialVersionUID = 1L;
            private Object readResolve() {
                return CHRONOLOGY.monthOfYear();
            }
        };
        assertEquals(field, (BasicMonthOfYearDateTimeField) obj);
    }
}