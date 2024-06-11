import org.junit.Assert;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.StaticDateTimePrinter;
import org.joda.time.format.StaticDateTimeParser;

public class DateTimeFormatterTest1 {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new StaticDateTimePrinter(), new StaticDateTimeParser());
        assertTrue(formatter.isPrinter());

        DateTimeFormatter formatter2 = new DateTimeFormatter(null, new StaticDateTimeParser());
        assertFalse(formatter2.isPrinter());
    }

    @Test
    public void testIsParser() {
        DateTimeFormatter formatter = new DateTimeFormatter(new StaticDateTimePrinter(), new StaticDateTimeParser());
        assertTrue(formatter.isParser());

        DateTimeFormatter formatter2 = new DateTimeFormatter(new StaticDateTimePrinter(), null);
        assertFalse(formatter2.isParser());
    }

    // Test other methods similarly
}