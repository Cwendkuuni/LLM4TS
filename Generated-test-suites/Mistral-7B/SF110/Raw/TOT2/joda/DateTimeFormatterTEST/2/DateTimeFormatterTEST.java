import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DateTimeFormatterTest {

    @Test
    public void test1() {
        DateTimePrinter printer = Mockito.mock(DateTimePrinter.class);
        DateTimeFormatter formatter = new DateTimeFormatter(printer, null);
        assertTrue(formatter.isPrinter());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test2() {
        DateTimeFormatter formatter = new DateTimeFormatter(null, null);
    }
}