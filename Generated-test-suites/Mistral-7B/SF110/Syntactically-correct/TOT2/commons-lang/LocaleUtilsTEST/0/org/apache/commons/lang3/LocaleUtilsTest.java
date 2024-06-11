import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocaleEmpty() {
        assertInstanceOf(Locale.class, LocaleUtils.toLocale(""));
        assertEquals("", LocaleUtils.toLocale("").getCountry());
        assertEquals("", LocaleUtils.toLocale("").getLanguage());
    }

    @Test
    public void testToLocaleValidLocale() {
        assertInstanceOf(Locale.class, LocaleUtils.toLocale("en_US"));
        assertEquals("en", LocaleUtils.toLocale("en_US").getLanguage());
        assertEquals("US", LocaleUtils.toLocale("en_US").getCountry());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_US#");
    }

    // Add test cases for other methods
}