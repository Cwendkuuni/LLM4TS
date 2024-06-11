package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidInput() {
        // Test valid input of length 2
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        // Test valid input of length 3
        expected = new Locale("fr_CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        // Test valid input of length 4 with country code
        expected = new Locale("fr_CA", "qc");
        actual = LocaleUtils.toLocale("fr_CA_qc");
        assertEquals(expected, actual);

        // Test valid input of length 5 with _ as separator
        expected = new Locale("fr_CA", "", "qc");
        actual = LocaleUtils.toLocale("fr_CA____qc");
        assertEquals(expected, actual);

        // Test valid input of length 5 with _ as separator and empty language code
        expected = new Locale("", "US");
        actual = LocaleUtils.toLocale("_US");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_EmptyString() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_SingleCharacter() {
        LocaleUtils.toLocale("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_NoSeparator() {
        LocaleUtils.toLocale("enUS");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidInput_WrongFormat() {
        LocaleUtils.toLocale("en_US#");
    }

    @Test
    public void testLocaleLookupList_ValidInput() {
        Locale locale = new Locale("en", "US");
        List<Locale> expected = LocaleUtils.localeLookupList(locale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, null);
        assertEquals(expected, actual);

        assertEquals(2, expected.size());
        assertEquals(locale, expected.get(0));
        assertEquals(new Locale("en", "US"), expected.get(1));

        locale = new Locale("fr", "CA");
        expected = LocaleUtils.localeLookupList(locale);
        actual = LocaleUtils.localeLookupList(locale, null);
        assertEquals(3, expected.size());
        assertEquals(locale, expected.get(0));
        assertEquals(new Locale("fr", "CA"), expected.get(1));
        assertEquals(new Locale("fr", ""), expected.get(2));
    }

    @Test
    public void testLocaleLookupList_ValidInputWithDefaultLocale() {
        Locale locale = new Locale("en", "US");
        Locale defaultLocale = new Locale("es", "ES");
        List<Locale> expected = LocaleUtils.localeLookupList(locale, defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);

        assertEquals(3, expected.size());
        assertEquals(locale, expected.get(0));
        assertEquals(new Locale("en", "US"), expected.get(1));
        assertEquals(defaultLocale, expected.get(2));
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        Locale locale = null;
        List<Locale> expected = LocaleUtils.localeLookupList(locale, null);
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(Collections.emptyList(), expected);
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    public void testLocaleLookupList_EmptyStringInput() {
        List<Locale> expected = LocaleUtils.localeLookupList(null);
        List<Locale> actual = LocaleUtils.localeLookupList("");
        assertEquals(Collections.emptyList(), expected);
        assertEquals(Collections.emptyList(), actual);
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
    public void testIsAvailableLocale_ValidInput() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        Locale locale = new Locale("invalid", "invalid");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale(null, null);
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> expected = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> actual = Locale.getLocales(new Locale(countryCode, ""));
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expected = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> actual = Locale.getLocales(new Locale("", languageCode));
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountry_EmptyString() {
        String countryCode = "";
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguage_NullInput() {
        String languageCode = null;
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }
}