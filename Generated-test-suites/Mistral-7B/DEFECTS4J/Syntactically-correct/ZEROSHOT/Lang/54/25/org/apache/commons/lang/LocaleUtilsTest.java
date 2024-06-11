import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("es", LocaleUtils.toLocale("es").toString());

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_GB_xx"));
        assertNull(LocaleUtils.toLocale("en_GB_xx_xx"));
        assertNull(LocaleUtils.toLocale("en_GB_xx_xx_xx"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale defaultLocale = Locale.getDefault();

        // Test empty list
        List<Locale> emptyList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertTrue(emptyList.isEmpty());

        // Test single locale
        Locale locale1 = LocaleUtils.toLocale("en_GB");
        List<Locale> list1 = LocaleUtils.localeLookupList(locale1, defaultLocale);
        assertEquals(3, list1.size());
        assertEquals(locale1, list1.get(0));
        assertEquals("en", list1.get(1).getLanguage());
        assertEquals("", list1.get(1).getCountry());
        assertEquals("en_GB", list1.get(2).toString());

        // Test multiple locales
        Locale locale2 = LocaleUtils.toLocale("fr_CA_XX");
        List<Locale> list2 = LocaleUtils.localeLookupList(locale2, defaultLocale);
        assertEquals(5, list2.size());
        assertEquals(locale2, list2.get(0));
        assertEquals("fr", list2.get(1).getLanguage());
        assertEquals("CA", list2.get(1).getCountry());
        assertEquals("fr_CA", list2.get(2).toString());
        assertEquals("", list2.get(3).getCountry());
        assertEquals("fr", list2.get(4).getLanguage());
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assertEquals(172, availableLocales.size());

        // Test valid country codes
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.CANADA));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMANY));

        // Test invalid country codes
        assertFalse(LocaleUtils.isAvailableLocale(Locale.CHINA));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.JAPANESE));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.KOREAN));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountryFR = LocaleUtils.languagesByCountry("FR");
        assertEquals(11, languagesByCountryFR.size());
        assertTrue(languagesByCountryFR.contains(Locale.FRENCH));
        assertTrue(languagesByCountryFR.contains(Locale.FRENCH_CANADA));
        assertTrue(languagesByCountryFR.contains(Locale.FRENCH_BELGIUM));
        assertTrue(languagesByCountryFR.contains(Locale.FRENCH_SWITZERLAND));
        assertTrue(languagesByCountryFR.contains(Locale.FRENCH_LUXEMBOURG));
        assertFalse(languagesByCountryFR.contains(Locale.GERMAN));

        List<Locale> languagesByCountryDE = LocaleUtils.languagesByCountry("DE");
        assertEquals(10, languagesByCountryDE.size());
        assertTrue(languagesByCountryDE.contains(Locale.GERMAN));
        assertTrue(languagesByCountryDE.contains(Locale.GERMAN_AUSTRIA));
        assertTrue(languagesByCountryDE.contains(Locale.GERMAN_SWITZERLAND));
        assertFalse(languagesByCountryDE.contains(Locale.FRENCH));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> countriesByLanguageEN = LocaleUtils.countriesByLanguage("en");
        assertEquals(26, countriesByLanguageEN.size());
        assertTrue(countriesByLanguageEN.contains(Locale.ENGLISH));
        assertTrue(countriesByLanguageEN.contains(Locale.ENGLISH_AUSTRALIA));
        assertTrue(countriesByLanguageEN.contains(Locale.ENGLISH_CANADA));
        assertTrue(countriesByLanguageEN.contains(Locale.ENGLISH_NEWZEALAND));
        assertTrue(countriesByLanguageEN.contains(Locale.ENGLISH_SOUTH_AFRICA));
        assertFalse(countriesByLanguageEN.contains(Locale.GERMAN));

        List<Locale> countriesByLanguageDE = LocaleUtils.countriesByLanguage("de");
        assertEquals(13, countriesByLanguageDE.size());
        assertTrue(countriesByLanguageDE.contains(Locale.GERMAN));
        assertTrue(countriesByLanguageDE.contains(Locale.GERMAN_AUSTRIA));
        assertTrue(countriesByLanguageDE.contains(Locale.GERMAN_GERMANY));
        assertTrue(countriesByLanguageDE.contains(Locale.GERMAN_SWITZERLAND));
        assertFalse(countriesByLanguageDE.contains(Locale.ENGLISH));
    }
}