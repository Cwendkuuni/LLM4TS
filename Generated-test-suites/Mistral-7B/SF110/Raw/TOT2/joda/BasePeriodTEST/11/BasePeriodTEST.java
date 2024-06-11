import org.junit.Test;
import org.joda.time.BasePeriod;
import static org.junit.Assert.assertEquals;

public class BasePeriodTest {

    @Test
    public void testBasePeriodWithIntConstructor() {
        // Given
        int number = 10;
        BasePeriod period = new BasePeriod(number);

        // When
        PeriodType periodType = period.getPeriodType();
        int[] values = period.getValues();

        // Then
        assertEquals(PeriodType.standard(), periodType);
        assertEquals(number, values[0]);
        assertEquals(0, values[1]);
        assertEquals(0, values[2]);
        assertEquals(0, values[3]);
        assertEquals(0, values[4]);
        assertEquals(0, values[5]);
        assertEquals(0, values[6]);
        assertEquals(0, values[7]);
    }
}