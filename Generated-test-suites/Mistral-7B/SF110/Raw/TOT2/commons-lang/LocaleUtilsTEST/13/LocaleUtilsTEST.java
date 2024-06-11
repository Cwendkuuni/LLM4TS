import org.apache.commons.lang3.LocaleUtils;
import org.junit.*;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocaleEmpty() {
        assertInstanceOf(Locale.class, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocaleValidLocale() {
        assertInstanceOf(Locale.class, LocaleUtils.toLocale("en_US"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("en_US#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLength() {
        LocaleUtils.toLocale("en");
    }

    @Test
    public void testLocaleLookupListNull() {
        assertEquals(0, LocaleUtils.localeLookupList(null).size());
    }

    @Test
    public void testLocaleLookupListValidLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("en", ""), result.get(1));
        assertEquals(new Locale("", "US"), result.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatWithHash() {
        LocaleUtils.toLocale("en_US#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLength() {
        LocaleUtils.toLocale("en");
    }

    @Test
    public void testLocaleLookupListWithNullLocale() {
        assertEquals(0, LocaleUtils.localeLookupList(null).size());
    }

    @Test
    public void testAvailableLocaleListEmpty() {
        assertEquals(0, LocaleUtils.availableLocaleList().size());
    }

    @Test
    public void testAvailableLocaleSetEmpty() {
        assertEquals(0, LocaleUtils.availableLocaleSet().size());
    }

    @Test
    public void testAvailableLocaleListNotEmpty() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(true, availableLocaleList.contains(new Locale("en", "US")));
    }

    @Test
    public void testIsAvailableLocaleTrue() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
    }

    @Test
    public void testIsAvailableLocaleFalse() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es", "AR")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountryNull() {
        LocaleUtils.languagesByCountry(null);
    }

    @Test
    public void testLanguagesByCountryValidCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(2, result.size());
        assertEquals(new Locale("en", "US"), result.get(0));
        assertEquals(new Locale("es", "US"), result.get(1));
    }

    // Add similar tests for other methods

}