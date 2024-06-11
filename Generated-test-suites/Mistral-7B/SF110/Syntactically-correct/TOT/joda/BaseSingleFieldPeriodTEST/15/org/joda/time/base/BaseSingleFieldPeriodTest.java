import org.junit.Test;
import org.joda.time.BaseCalendar;
import org.joda.time.DateTimeUtils;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.PeriodType;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.field.Lists;
import org.joda.time.field.PeriodFields;
import static org.junit.Assert.assertEquals;

public class BaseSingleFieldPeriodTest {
    // 

    @Test
    public void testBetweenMethod() {
        final BaseCalendar baseCalendar = new BaseCalendar();
        final Instant instant1 = baseCalendar.getInstants().get(0, 2022, 1, 1, 12, 30, 0, 0, ISOChronology.getInstanceUTC());
        final Instant instant2 = baseCalendar.getInstants().get(1, 2022, 1, 1, 13, 45, 0, 0, ISOChronology.getInstanceUTC());
        final ReadablePeriod readablePeriod = Period.parse("PT1H", PeriodType.standard());

        final int result = BaseSingleFieldPeriod.between(instant1, instant2, PeriodFields.HOURS);
        final int expected = 1;

        assertEquals(expected, result);
    }

    // 
}