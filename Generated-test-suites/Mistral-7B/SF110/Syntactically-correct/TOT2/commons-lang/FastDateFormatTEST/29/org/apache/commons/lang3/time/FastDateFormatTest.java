import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static org.junit.Assert.*;

public class FastDateFormatTest {

    @Test
    public void testFormatDate() throws Exception {
        FastDateFormat fastDateFormat = new FastDateFormat("dd/MM/yyyy hh:mm a zzzz", Locale.ENGLISH);
        Calendar calendar = new GregorianCalendar(2022, 10, 12);
        Date date = calendar.getTime();
        String expected = "12/11/2022 10:30 AM PST";
        String actual = fastDateFormat.format(date);
        assertEquals(expected, actual);
    }
}