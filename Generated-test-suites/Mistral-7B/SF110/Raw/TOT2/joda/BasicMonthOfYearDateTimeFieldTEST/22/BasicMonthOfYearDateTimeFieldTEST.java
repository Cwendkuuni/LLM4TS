import org.junit.Test;
import static org.junit.Assert.assertFalse;

public class BasicMonthOfYearDateTimeFieldTest {
    @Test
    public void testIsLenient() {
        BasicMonthOfYearDateTimeField field = new BasicMonthOfYearDateTimeField(Mockito.mock(BasicChronology.class), 1);
        assertFalse(field.isLenient());
    }
}