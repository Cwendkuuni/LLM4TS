import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleWithValidInput() {
        String str = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(str);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithEmptyString() {
        String str = "";
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithNullString() {
        String str = null;
        LocaleUtils.toLocale(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        String str = "en_US#";
        LocaleUtils.toLocale(str);
    }

    @Test
    public void testToLocaleWithShortFormat() {
        String str = "en";
        Locale expectedLocale = new Locale("en", "");
        Locale actualLocale = LocaleUtils.toLocale(str);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test
    public void testToLocaleWithLongFormat() {
        String str = "en_US_CA";
        Locale expectedLocale = new Locale("en", "US", "CA");
        Locale actualLocale = LocaleUtils.toLocale(str);
        assertEquals(expectedLocale, actualLocale);
    }
}