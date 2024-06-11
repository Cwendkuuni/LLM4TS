import org.joda.time.BasicDateTime;
import org.joda.time.BasicMonthOfYearDateTimeField;
import org.joda.time.DateTimeConstants;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {

    @Test
    public void testGetMethod() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(DateTimeConstants.ISO_CHRONOLOGY, DateTimeConstants.JANUARY);
        long instant = new BasicDateTime(2022, 1, 1, 0, 0, 0, 0, DateTimeConstants.UTC).getMillis();
        int expectedMonth = 1; // January
        int actualMonth = field.get(instant);
        assertEquals("Expected month is incorrect.", expectedMonth, actualMonth);
    }
}