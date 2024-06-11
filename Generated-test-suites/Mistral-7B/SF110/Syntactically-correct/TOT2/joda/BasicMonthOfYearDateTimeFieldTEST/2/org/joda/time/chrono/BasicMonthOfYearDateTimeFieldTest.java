import org.joda.time.DateTimeConstants;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMethod() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(new ISOChronology(), 1);
        long testDate = DateTimeConstants.MILLIS_IN_DAY * DateTimeConstants.DAYS_PER_WEEK * 5;
        int expectedMonth = 6; // June
        int actualMonth = field.get(testDate);
        assertEquals(expectedMonth, actualMonth);
    }
}