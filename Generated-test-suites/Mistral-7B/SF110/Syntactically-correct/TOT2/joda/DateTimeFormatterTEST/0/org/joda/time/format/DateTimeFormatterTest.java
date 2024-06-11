import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DateTimeFormatterTest {
    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateFormatDateTimePrinter(), new SimpleDateFormatDateTimeParser());
        assertTrue(formatter.isPrinter());

        formatter = new DateTimeFormatter(null, null);
        assertFalse(formatter.isPrinter());
    }
}