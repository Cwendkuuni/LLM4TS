import org.joda.time.format.DateTimeFormatter;
import static org.junit.Assert.assertTrue;

public class DateTimeFormatterTest {
    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
        assertTrue(formatter.isPrinter());
    }
}