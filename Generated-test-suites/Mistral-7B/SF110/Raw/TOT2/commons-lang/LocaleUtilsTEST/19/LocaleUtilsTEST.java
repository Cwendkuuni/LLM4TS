import org.junit.Test;
import org.junit.Assert.*;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        String validLocale = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(validLocale);
        assertEquals(expectedLocale, actualLocale);
    }
}