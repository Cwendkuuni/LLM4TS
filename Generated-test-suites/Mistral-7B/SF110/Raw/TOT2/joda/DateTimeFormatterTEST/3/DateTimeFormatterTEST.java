import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimePrinter;
import org.junit.Assert;
import org.junit.Test;

public class DateTimeFormatterTest {

    @Test
    public void testIsPrinter() {
        DateTimeFormatter formatter = new DateTimeFormatter(new StubDateTimePrinter(), null);
        Assert.assertTrue(formatter.isPrinter());
        
        formatter = new DateTimeFormatter(null, new StubDateTimeParser());
        Assert.assertFalse(formatter.isPrinter());
    }
}