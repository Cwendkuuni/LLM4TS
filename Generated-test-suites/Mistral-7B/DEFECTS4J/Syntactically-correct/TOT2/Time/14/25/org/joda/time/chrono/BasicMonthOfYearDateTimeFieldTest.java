import static org.junit.Assert.*;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.BasicMonthOfYearDateTimeField;
import org.joda.time.DateTime;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMethod() {
        BasicChronology chronology = DateTimeUtils.forDate(2010, 1, 1).getChronology();
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(chronology, DateTimeConstants.JANUARY);
        DateTime testDate = new DateTime(2010, 1, 31, 0, 0);

        int expectedMonth = 1;
        int actualMonth = field.get(testDate.getMillis());

        assertEquals(expectedMonth, actualMonth);
    }
}