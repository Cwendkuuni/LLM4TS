package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_emptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_invalidFormat() {
        String[] invalidFormats = new String[]{"en_GB_xyz", "en_gb_xyz", "en_GB_xyz_"};
        for (String invalidFormat : invalidFormats) {
            IllegalArgumentException exception = null;
            try {
                LocaleUtils.toLocale(invalidFormat);
            } catch (IllegalArgumentException e) {
                exception = e;
            }
            assertNotNull(exception);
            assertEquals("Invalid locale format: " + invalidFormat, exception.getMessage());
        }
    }

    @Test
    public void testToLocale_validFormat() {
        Locale expectedLocale;
        String str;
        Locale locale;

        str = "en";
        expectedLocale = new Locale("en", "");
        locale = LocaleUtils.toLocale(str);
        assertEquals(expectedLocale.getLanguage(), locale.getLanguage());
        assertEquals(expectedLocale.getCountry(), locale.getCountry());

        str = "en_GB";
        expectedLocale = new Locale("en", "GB");
        locale = LocaleUtils.toLocale(str);
        assertEquals(expectedLocale.getLanguage(), locale.getLanguage());
        assertEquals(expectedLocale.getCountry(), locale.getCountry());

        str = "en_GB_xxx";
        expectedLocale = new Locale("en", "GB", "xxx");
        locale = LocaleUtils.toLocale(str);
        assertEquals(expectedLocale.getLanguage(), locale.getLanguage());
        assertEquals(expectedLocale.getCountry(), locale.getCountry());
        assertEquals(expectedLocale.getVariant(), locale.getVariant());
    }

    @Test
    public void testLocaleLookupList_emptyLocale() {
        Locale locale = null;
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assertTrue(lookupList.isEmpty());
    }

    @Test
    public void testLocaleLookupList_locale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedList, lookupList);
    }

    @Test
    public void testLocaleLookupList_defaultLocale() {
        Locale locale = new Locale("fr", "CA");
        Locale defaultLocale = new Locale("en");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"), defaultLocale);
        assertEquals(expectedList, lookupList);
    }

    @Test
    public void testLocaleLookupList_nullLocale() {
        Locale defaultLocale = new Locale("en");
        List<Locale> lookupList = LocaleUtils.localeLookupList(null, defaultLocale);
        assertTrue(lookupList.isEmpty());
    }

    @Test
    public void testLocaleLookupList_defaultLocale_null() {
        List<Locale> lookupList = LocaleUtils.localeLookupList(new Locale("fr", "CA"));
        assertTrue(lookupList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList_isEmpty() {
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertTrue(availableLocaleList.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_isEmpty() {
        Set<Locale> availableLocaleSet = LocaleUtils.availableLocaleSet();
        assertTrue(availableLocaleSet.isEmpty());
    }

    @Test
    public void testIsAvailableLocale_availableLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_invalidLocale() {
        Locale locale = new Locale("fr", "CA", "xyz");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry_validCountryCode() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        List<Locale> expectedLanguagesByCountry = Arrays.asList(new Locale("en", "CA"), new Locale("fr", "CA"));
        assertEquals(expectedLanguagesByCountry, languagesByCountry);
    }

    @Test
    public void testLanguagesByCountry_emptyCountryCode() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry(null);
        assertTrue(languagesByCountry.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_validLanguageCode() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedCountriesByLanguage = Arrays.asList(new Locale("CA"), new Locale("GB"), new Locale("US"));
        assertEquals(expectedCountriesByLanguage, countriesByLanguage);
    }

    @Test
    public void testCountriesByLanguage_emptyLanguageCode() {
        List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage(null);
        assertTrue(countriesByLanguage.isEmpty());
    }
}