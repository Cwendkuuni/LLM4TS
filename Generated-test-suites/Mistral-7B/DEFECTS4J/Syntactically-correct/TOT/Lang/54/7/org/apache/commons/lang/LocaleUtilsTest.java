import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "FR");
        actual = LocaleUtils.toLocale("fr_FR");
        assertEquals(expected, actual);

        expected = new Locale("de", "DE_AT");
        actual = LocaleUtils.toLocale("de_AT_ Wien");
        assertEquals(expected, actual);

        // Test invalid inputs
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB_London_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            LocaleUtils.toLocale("en_GB-London-xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr", ""),
                new Locale("en")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        locale = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.localeLookupList(locale, new Locale("en"));
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actual = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(
                new Locale("zh", "CN"),
                new Locale("zh", "HK"),
                new Locale("zh", "TW"),
                // ... other locales ...
                new Locale("en", "US"),
                new Locale("en", "GB"),
                new Locale("en", "CA")
                // ... other locales ...
        );
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "FR");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA_xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(
                new Locale("en", "US"),
                new Locale("es", "US"),
                new Locale("fr", "CA"),
                // ... other locales ...
                new Locale("zh", "CN")
                // ... other locales ...
        );
        assertEquals(expected, actual);

        actual = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(
                new Locale("en", "US"),
                new Locale("en", "GB"),
                // ... other locales ...
                new Locale("en", "ZA")
                // ... other locales ...
        );
        assertEquals(expected, actual);

        actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), actual);
    }
}