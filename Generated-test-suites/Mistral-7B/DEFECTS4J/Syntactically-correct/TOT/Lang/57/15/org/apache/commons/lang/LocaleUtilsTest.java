package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        assertEquals("de", LocaleUtils.toLocale("de").toString());

        // Test invalid input - empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test invalid input - language code not lowercase
        try {
            LocaleUtils.toLocale("EN_US");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test invalid input - country code not uppercase
        try {
            LocaleUtils.toLocale("en_fr");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test invalid input - separator not an underscore
        try {
            LocaleUtils.toLocale("en:US");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test invalid input - length less than 2
        try {
            LocaleUtils.toLocale("en");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test invalid input - length greater than 7
        try {
            LocaleUtils.toLocale("en_US_XXX_YYY");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test invalid input - country code contains separator
        try {
            LocaleUtils.toLocale("en_US_");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Collection<Locale> localeList = LocaleUtils.localeLookupList(locale);

        // Test no default locale
        assertContains(localeList, locale);
        assertContains(localeList, new Locale("fr", "CA"));
        assertContains(localeList, new Locale("fr"));

        // Test with default locale
        Locale defaultLocale = new Locale("en");
        localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertContains(localeList, locale);
        assertContains(localeList, new Locale("fr", "CA"));
        assertContains(localeList, new Locale("fr"));
        assertContains(localeList, defaultLocale);
    }

    @Test
    public void testAvailableLocaleList() {
        Collection<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(localeList.size() > 0);
        for (Locale locale : localeList) {
            assertNotNull(locale);
        }
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en_US")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr_CA")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xy_zz")));
    }

    @Test
    public void testLanguagesByCountry() {
        Collection<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assertContains("en", languages);
        assertContains("fr", languages);
        assertEquals(2, languages.size());
    }

    @Test
    public void testCountriesByLanguage() {
        Collection<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertContains(new Locale("US"), countries);
        assertContains(new Locale("CA"), countries);
        assertEquals(2, countries.size());
    }

    private void assertContains(Collection<Locale> collection, Locale locale) {
        assertTrue(collection.contains(locale));
    }
}