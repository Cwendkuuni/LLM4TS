import org.junit.Test;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.joda.time.BasicMonthOfYearDateTimeField;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMethod() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeChronology.getInstanceUTC(), DateTimeConstants.JANUARY);
        long instant = LocalDateTime.now().toDate().getTime();
        int expectedMonth = DateTimeConstants.DECEMBER; // Any valid month value
        int actualMonth = field.get(instant);

        // Check if the extracted month matches the expected value
        assertEquals(expectedMonth, actualMonth);
    }
}