import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new StaticDateTimePrinter(), new StaticDateTimeParser());
        Assert.assertTrue(formatter.isPrinter());
    }
}