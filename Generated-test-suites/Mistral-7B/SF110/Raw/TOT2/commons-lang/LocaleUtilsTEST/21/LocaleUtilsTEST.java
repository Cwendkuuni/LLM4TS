import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String testString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(testString);
        assertEquals(expectedLocale, actualLocale);
    }
}