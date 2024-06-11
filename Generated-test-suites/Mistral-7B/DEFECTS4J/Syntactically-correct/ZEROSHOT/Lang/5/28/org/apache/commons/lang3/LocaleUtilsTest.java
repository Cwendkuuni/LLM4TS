import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("de", LocaleUtils.toLocale("de").toString());

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("en"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_GB_"));
        assertNull(LocaleUtils.toLocale("en_GB_xx"));
        assertNull(LocaleUtils.toLocale("en_GB_xx_"));
        assertNull(LocaleUtils.toLocale("en_GB_xx_xx"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, localeList.get(0));
        assertEquals("fr_CA", localeList.get(1).toString());
        assertEquals("fr", localeList.get(2).toString());
        assertEquals("en", localeList.get(3).toString());

        locale = new Locale("en");
        localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(locale, localeList.get(0));
        assertEquals("en_US", localeList.get(1).toString());
        assertEquals("en_GB", localeList.get(2).toString());
        assertEquals("fr", localeList.get(3).toString());

        locale = null;
        localeList = LocaleUtils.localeLookupList(locale);
        assertTrue(localeList.isEmpty());

        locale = new Locale("en", "US", "en_US");
        localeList = LocaleUtils.localeLookupList(locale, new Locale("en", "CA"));
        assertEquals(locale, localeList.get(0));
        assertEquals("en_US", localeList.get(1).toString());
        assertEquals("en_CA", localeList.get(2).toString());
        assertEquals("en", localeList.get(3).toString());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertFalse(localeList.isEmpty());

        for (Locale locale : localeList) {
            if (locale.getLanguage().equals("en")) {
                assertEquals("en_US", locale.toString());
                assertEquals("United States", locale.getDisplayCountry());
                assertEquals("English", locale.getDisplayLanguage());
            } else if (locale.getLanguage().equals("fr")) {
                assertEquals("fr_CA", locale.toString());
                assertEquals("Canada", locale.getDisplayCountry());
                assertEquals("French", locale.getDisplayLanguage());
            }
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> localeList = LocaleUtils.languagesByCountry("CA");
        assertFalse(localeList.isEmpty());
        assertEquals("fr_CA", localeList.get(0).toString());
        assertEquals("en_CA", localeList.get(1).toString());
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> localeList = LocaleUtils.countriesByLanguage("fr");
        assertFalse(localeList.isEmpty());
        assertEquals("fr_CA", localeList.get(0).toString());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en_US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr_CA")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en_US_xxx")));
    }
}