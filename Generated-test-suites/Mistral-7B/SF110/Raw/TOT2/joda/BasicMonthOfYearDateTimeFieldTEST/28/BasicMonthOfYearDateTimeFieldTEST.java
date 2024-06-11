import org.joda.time.BaseDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class BasicMonthOfYearDateTimeFieldTest {
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField testInstance = new BasicMonthOfYearDateTimeField(BaseChronology.UTC, 2);
        assertFalse(testInstance.isLenient());
    }
}