import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleSimple() {
        Locale locale = LocaleUtils.toLocale("en");
        assertEquals("en", locale.getLanguage());
        assertEquals("", locale.getCountry());
        assertNull(locale.getVariant());
    }

    @Test
    public void testToLocaleWithCountry() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertNull(locale.getVariant());
    }

    @Test
    public void testToLocaleWithCountryAndVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals("en", locale.getLanguage());
        assertEquals("GB", locale.getCountry());
        assertEquals("xxx", locale.getVariant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleEmptyString() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("xx_yy");
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(3, localeList.size());
        assertEquals(baseLocale, localeList.get(0));
        assertEquals(new Locale("fr", "CA"), localeList.get(1));
        assertEquals(new Locale("fr"), localeList.get(2));
    }

    @Test
    public void testLocaleLookupListDefaultLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null, new Locale("en"));
        assertEquals(2, localeList.size());
        assertEquals(new Locale("en"), localeList.get(0));
        assertNull(localeList.get(1));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertFalse(localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertFalse(localeSet.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("es_ES");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en_GB_xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> languageList = LocaleUtils.languagesByCountry(countryCode);
        assertFalse(languageList.isEmpty());
        for (Locale locale : languageList) {
            assertTrue(locale.getCountry().equals(countryCode));
            assertTrue(locale.getVariant().isEmpty());
        }
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> countryList = LocaleUtils.countriesByLanguage(languageCode);
        assertFalse(countryList.isEmpty());
        for (Locale locale : countryList) {
            assertTrue(locale.getLanguage().equals(languageCode));
            assertTrue(locale.getCountry().length() != 0);
            assertTrue(locale.getVariant().isEmpty());
        }
    }
}