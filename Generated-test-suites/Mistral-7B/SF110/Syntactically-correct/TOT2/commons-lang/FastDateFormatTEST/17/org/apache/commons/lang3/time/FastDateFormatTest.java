import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FastDateFormatTest {

    @Test
    public void testGetInstance() {
        FastDateFormat dateFormat = FastDateFormat.getInstance();
        assertEquals(FastDateFormat.FULL, dateFormat.getDateStyle());
        assertEquals(FastDateFormat.FULL, dateFormat.getTimeStyle());
        assertEquals(Locale.getDefault(), dateFormat.getLocale());
        assertEquals(TimeZone.getDefault(), dateFormat.getTimeZone());
    }
}