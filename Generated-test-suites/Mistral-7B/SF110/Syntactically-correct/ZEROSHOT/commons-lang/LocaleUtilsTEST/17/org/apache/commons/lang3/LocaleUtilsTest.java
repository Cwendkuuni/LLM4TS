package org.apache.commons.lang3;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleEmpty() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocaleNull() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatEmpty() {
        LocaleUtils.toLocale(" ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatNoLanguage() {
        LocaleUtils.toLocale("_CN");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatNoCountry() {
        LocaleUtils.toLocale("en");
    }

    @Test
    public void testToLocaleValidFormat() {
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatThreePartsNoLanguage() {
        LocaleUtils.toLocale("CN_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatThreePartsNoCountry() {
        LocaleUtils.toLocale("en_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatThreePartsInvalidLanguage() {
        LocaleUtils.toLocale("ab_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatThreePartsInvalidCountry() {
        LocaleUtils.toLocale("en_XY");
    }

    @Test
    public void testToLocaleValidFormatWithVariant() {
        Locale expected = new Locale("en", "US", "Latn");
        Locale actual = LocaleUtils.toLocale("en_US_Latn");
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatWithVariantEmpty() {
        LocaleUtils.toLocale("en_US_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatWithVariantInvalidLanguage() {
        LocaleUtils.toLocale("ab_US_Latn");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormatWithVariantInvalidCountry() {
        LocaleUtils.toLocale("en_XY_Latn");
    }

    @Test
    public void testLocaleLookupListNullLocale() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(locale, new Locale("en", ""), new Locale("", "US"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupListDefaultLocale() {
        Locale defaultLocale = new Locale("fr", "FR");
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(null, defaultLocale);
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleListEmpty() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleListNonEmpty() {
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
        List<Locale> actual = LocaleUtils.availableLocaleList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSetEmpty() {
        Set<Locale> expected = Collections.emptySet();
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSetNonEmpty() {
        Set<Locale> expected = Collections.unmodifiableSet(Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN));
        Set<Locale> actual = LocaleUtils.availableLocaleSet();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocaleNull() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocaleAvailable() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testIsAvailableLocaleUnavailable() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("ab", "XY")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLanguagesByCountryEmpty() {
        String countryCode = "";
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountryNullCountryCode() {
        String countryCode = null;
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountryEmptyList() {
        String countryCode = "XX";
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLanguagesByCountryNonEmptyList() {
        String countryCode = "US";
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_US);
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguageEmpty() {
        String languageCode = "";
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguageNullLanguageCode() {
        String languageCode = null;
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguageEmptyList() {
        String languageCode = "xx";
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testCountriesByLanguageNonEmptyList() {
        String languageCode = "en";
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_AU, Locale.ENGLISH_CA, Locale.ENGLISH_GB, Locale.ENGLISH_IE, Locale.ENGLISH_NZ, Locale.ENGLISH_US);
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupListConcurrentModificationException() {
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, Locale.ENGLISH_US);
        List<Locale> actual = new ArrayList<Locale>(2);
        LocaleUtils.localeLookupList(null, new Locale("fr", "FR")).forEach(actual::add);
        try {
            LocaleUtils.localeLookupList(null, new Locale("en", "EN")).forEach(actual::add);
            fail();
        } catch (ConcurrentModificationException e) {
            // expected
        }
        assertEquals(expected, actual);
    }
}