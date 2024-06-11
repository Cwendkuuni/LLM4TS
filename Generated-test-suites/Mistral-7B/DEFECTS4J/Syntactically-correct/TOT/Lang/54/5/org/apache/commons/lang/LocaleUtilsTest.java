package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        Locale expected = new Locale("en", "GB");
        Locale actual = LocaleUtils.toLocale("en_GB");
        assertEquals(expected, actual);

        expected = new Locale("fr", "");
        actual = LocaleUtils.toLocale("fr");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA", "xxx");
        actual = LocaleUtils.toLocale("fr_CA_xxx");
        assertEquals(expected, actual);

        // Test invalid input
        try {
            Locale actual = LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("ab_AB_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_GB_XXX");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_GB_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("en_ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Locale actual = LocaleUtils.toLocale("ab");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        Locale defaultLocale = Locale.getDefault();
        List<Locale> expected = new ArrayList<>();
        List<Locale> actual = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expected, actual);

        // Test valid input
        Locale locale = new Locale("fr", "CA", "xxx");
        expected = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(expected, actual);

        // Test invalid input
        try {
            Locale actual = LocaleUtils.localeLookupList(null, null);
            fail("Expected empty list");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        Locale invalidLocale = new Locale("ab", "AB", "XXX");
        try {
            Locale actual = LocaleUtils.localeLookupList(invalidLocale, defaultLocale);
            fail("Expected empty list");
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
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<>(Locale.getAvailableLocales());
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test valid locales
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));

        // Test invalid locales
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("ab", "AB")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = LocaleUtils.languagesByCountry("CA");
        List<Locale> actual = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        assertEquals(expected, actual);

        expected = Collections.emptyList();
        actual = LocaleUtils.languagesByCountry(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = LocaleUtils.countriesByLanguage("en");
        List<Locale> actual = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"));
        assertEquals(expected, actual);

        expected = Collections.emptyList();
        actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected, actual);
    }
}