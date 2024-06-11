import org.junit.Assert;
import org.joda.time.format.*;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new StaticDateTimePrinter(), new StaticDateTimeParser());
        assertTrue(formatter.isPrinter());
    }

    @Test
    public void testIsParserWithoutPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, new StaticDateTimeParser());
        assertFalse(formatter.isPrinter());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullPrinterAndParser() {
        new DateTimeFormatter(null, null);
    }

    // Other test cases for other methods
}