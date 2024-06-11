import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new SimpleDateTimeFormat(), new BasicDateTimeParser());
        assertTrue(formatter.isPrinter());
    }
}