import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        Locale expectedLocale = new Locale("en", "GB");
        Locale actualLocale = LocaleUtils.toLocale("en_GB");
        assertEquals(expectedLocale, actualLocale);

        expectedLocale = new Locale("es", "ES");
        actualLocale = LocaleUtils.toLocale("es_ES");
        assertEquals(expectedLocale, actualLocale);

        expectedLocale = new Locale("fr", "CA");
        actualLocale = LocaleUtils.toLocale("fr_CA");
        assertEquals(expectedLocale, actualLocale);

        expectedLocale = new Locale("en");
        actualLocale = LocaleUtils.toLocale("en");
        assertEquals(expectedLocale, actualLocale);

        String invalidLocaleString = "en_GB_invalid";
        try {
            Locale locale = LocaleUtils.toLocale(invalidLocaleString);
            fail("Expected IllegalArgumentException but got a valid Locale instance.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        String emptyLocaleString = "";
        try {
            Locale locale = LocaleUtils.toLocale(emptyLocaleString);
            fail("Expected IllegalArgumentException but got a valid Locale instance.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale defaultLocale = new Locale("en");
        Locale targetLocale = new Locale("fr", "CA");

        List<Locale> localeList = LocaleUtils.localeLookupList(targetLocale);
        List<Locale> expectedLocaleList = Arrays.asList(targetLocale, new Locale("fr"), new Locale("fr", ""), defaultLocale);
        assertEquals(expectedLocaleList, localeList);

        expectedLocaleList = Arrays.asList(new Locale("en"), defaultLocale);
        localeList = LocaleUtils.localeLookupList(null);
        assertEquals(expectedLocaleList, localeList);
    }

    @Test
    public void testAvailableLocales() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocales = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.SPANISH, Locale.ITALIAN, Locale.JAPANESE, Locale.KOREAN, Locale.RUSSIAN, Locale.POLISH, Locale.PORTUGuese, Locale.DUTCH, Locale.SWEDISH, Locale.FINNISH, Locale.HUNGARIAN, Locale.CZECH, Locale.SLOVAK, Locale.Danish, Locale.NOREWGIAN, Locale.GREEK, Locale.ROMANIAN, Locale.CROATIAN, Locale.SLovene, Locale.ALBANIAN, Locale.BOSNIAN, Locale.SERBIAN, Locale.ESTONIAN, Locale.LATVIAN, Locale.LITHUANIAN, Locale.UKRAINIAN, Locale.BELARUSIAN);
        assertEquals(expectedLocales, locales);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("en", "US");
        Locale locale2 = new Locale("fr", "FR");
        Locale locale3 = new Locale("es", "ES");
        Locale locale4 = new Locale("jp", "JP");

        assertTrue(LocaleUtils.isAvailableLocale(locale1));
        assertTrue(LocaleUtils.isAvailableLocale(locale2));
        assertTrue(LocaleUtils.isAvailableLocale(locale3));
        assertTrue(LocaleUtils.isAvailableLocale(locale4));

        Locale locale5 = new Locale("non-existent-locale");
        assertFalse(LocaleUtils.isAvailableLocale(locale5));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expectedLocales = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "FR"), new Locale("en", "CA"), new Locale("en", "US"));
        List<Locale> locales = LocaleUtils.languagesByCountry("CA");
        assertEquals(expectedLocales, locales);

        locales = LocaleUtils.languagesByCountry("US");
        expectedLocales = Arrays.asList(new Locale("en", "US"));
        assertEquals(expectedLocales, locales);

        locales = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.EMPTY_LIST, locales);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expectedLocales = Arrays.asList(new Locale("ca", "CA"), new Locale("ca", "ES"), new Locale("fr", "CA"), new Locale("fr", "FR"));
        List<Locale> locales = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expectedLocales, locales);

        locales = LocaleUtils.countriesByLanguage("en");
        expectedLocales = Arrays.asList(new Locale("CA"), new Locale("US"));
        assertEquals(expectedLocales, locales);

        locales = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.EMPTY_LIST, locales);
    }
}