import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "FR");
        actual = LocaleUtils.toLocale("fr_FR");
        assertEquals(expected, actual);

        // Test empty locale string
        actual = LocaleUtils.toLocale("");
        assertNull(actual);

        // Test invalid locale string
        try {
            actual = LocaleUtils.toLocale("xyz_AB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty list
        List<Locale> expectedEmpty = Arrays.asList();
        List<Locale> actualEmpty = LocaleUtils.localeLookupList(null);
        assertEquals(expectedEmpty, actualEmpty);

        // Test valid locale and empty defaultLocale
        Locale locale = new Locale("en", "GB");
        expectedEmpty = Arrays.asList(locale);
        actualEmpty = LocaleUtils.localeLookupList(locale);
        assertEquals(expectedEmpty, actualEmpty);

        // Test valid locale and defaultLocale
        Locale defaultLocale = new Locale("en", "US");
        expectedEmpty.add(defaultLocale);
        actualEmpty = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expectedEmpty, actualEmpty);

        // Test null locale and defaultLocale
        Locale nullLocale = null;
        expectedEmpty = Arrays.asList(defaultLocale);
        actualEmpty = LocaleUtils.localeLookupList(nullLocale, defaultLocale);
        assertEquals(expectedEmpty, actualEmpty);

        // Test invalid locale and defaultLocale
        String invalidLocale = "xyz_AB";
        try {
            Locale invalidLocaleLocale = LocaleUtils.toLocale(invalidLocale);
            Locale[] invalidLocaleLocales = {invalidLocaleLocale};
            expectedEmpty = Arrays.asList(defaultLocale);
            actualEmpty = LocaleUtils.localeLookupList(invalidLocaleLocale, defaultLocale);
            assertEquals(expectedEmpty, actualEmpty);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        Collection<Locale> expected = LocaleUtils.availableLocaleSet();
        Collection<Locale> actual = Locale.getAvailableLocales();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("xyz", "AB");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("es", "US"), new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        countryCode = "CA";
        expected = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);

        countryCode = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        languageCode = "fr";
        expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "FR"));
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);

        languageCode = null;
        expected = Collections.emptyList();
        actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

}