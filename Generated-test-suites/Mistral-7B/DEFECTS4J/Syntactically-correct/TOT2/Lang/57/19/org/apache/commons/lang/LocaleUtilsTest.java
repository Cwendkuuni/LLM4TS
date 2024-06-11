package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test for valid locale
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        // Test for language only locale
        expected = new Locale("en", "");
        actual = LocaleUtils.toLocale("en");
        assertEquals(expected, actual);

        // Test for country only locale
        expected = new Locale("", "US");
        actual = LocaleUtils.toLocale("US");
        assertEquals(expected, actual);

        // Test for locale with variant
        expected = new Locale("en", "US", "en_US_WIN");
        actual = LocaleUtils.toLocale("en_US_WIN");
        assertEquals(expected, actual);

        // Test for invalid locale
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("invalid"));
        assertNull(LocaleUtils.toLocale("en_invalid"));
        assertNull(LocaleUtils.toLocale("en_US_invalid"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test for empty locale list
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);

        // Test for locale with no variants
        Locale locale = new Locale("en", "US");
        expected = Arrays.asList(locale);
        actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        // Test for locale with variants
        Locale variantLocale = new Locale("en", "US", "en_US_WIN");
        expected = Arrays.asList(locale, variantLocale);
        actual = LocaleUtils.localeLookupList(locale, variantLocale);
        assertEquals(expected, actual);

        // Test for locale with no match in the list
        Locale noMatchLocale = new Locale("es", "ES");
        expected = Arrays.asList(locale, variantLocale, noMatchLocale);
        actual = LocaleUtils.localeLookupList(locale, variantLocale, noMatchLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test for empty available locales
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);

        // Test for available locales
        List<Locale> availableLocales = Arrays.asList(new Locale("en", "US"), new Locale("fr", "FR"));
        expected = availableLocales;
        actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat() {
        LocaleUtils.toLocale("enUS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLanguage() {
        LocaleUtils.toLocale("en_INVALID");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidCountry() {
        LocaleUtils.toLocale("INVALID_en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidSeparator() {
        LocaleUtils.toLocale("en_US:INVALID");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidLength() {
        LocaleUtils.toLocale("en_US_INVALID_INVALID");
    }
}