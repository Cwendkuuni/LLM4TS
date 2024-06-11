import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeUtils.getCurrentCalendar().getTimeZone().getCalendarInstance(), DateTimeConstants.JANUARY);
        assertFalse(field.isLenient());
    }
}