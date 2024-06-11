package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("ja", "");
        actual = LocaleUtils.toLocale("ja");
        assertEquals(expected, actual);

        // Test invalid inputs
        try {
            actual = LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            actual = LocaleUtils.toLocale("xx_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            actual = LocaleUtils.toLocale("en_XX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            actual = LocaleUtils.toLocale("en_US_xxx");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid inputs
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = LocaleUtils.localeLookupList(locale);
        List<Locale> actual = new ArrayList<>();
        actual.add(locale);
        actual.add(new Locale("fr", "CA"));
        actual.add(new Locale("fr", ""));
        actual.add(Locale.getDefault());
        assertEquals(expected, actual);

        locale = new Locale("fr", "CA");
        expected = LocaleUtils.localeLookupList(locale, Locale.getDefault());
        actual.clear();
        actual.add(locale);
        actual.add(new Locale("fr", "CA"));
        actual.add(new Locale("fr", ""));
        actual.add(new Locale("en")); // Default locale
        assertEquals(expected, actual);

        // Test invalid inputs
        try {
            LocaleUtils.localeLookupList(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Locale.getAvailableLocales();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA", "xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expected = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> actual = Locale.getLocales(new String[]{countryCode});
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expected = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> actual = Locale.getLocales(new String[]{languageCode});
        assertEquals(expected, actual);
    }
}