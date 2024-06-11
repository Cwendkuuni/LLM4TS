import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleWithValidLocaleString() {
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithEmptyString() {
        String localeString = "";
        Locale actualLocale = LocaleUtils.toLocale(localeString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidLocaleString() {
        String localeString = "invalidLocale";
        Locale actualLocale = LocaleUtils.toLocale(localeString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithShortLocaleString() {
        String localeString = "en";
        Locale actualLocale = LocaleUtils.toLocale(localeString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithLongLocaleString() {
        String localeString = "en_US_US";
        Locale actualLocale = LocaleUtils.toLocale(localeString);
    }
}