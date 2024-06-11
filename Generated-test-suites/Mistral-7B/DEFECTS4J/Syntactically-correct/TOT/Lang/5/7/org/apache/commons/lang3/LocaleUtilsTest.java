package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        String[] inputs = new String[]{"en", "en_GB", "en_GB_US", "fr_CA"};
        Locale[] expected = new Locale[]{new Locale("en"), new Locale("en", "GB"), new Locale("en", "GB", "US"), new Locale("fr", "CA")};
        for (int i = 0; i < inputs.length; i++) {
            Locale actual = LocaleUtils.toLocale(inputs[i]);
            Assert.assertEquals("Unexpected Locale for input: " + inputs[i], expected[i], actual);
        }

        // Test null input
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid input
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals("Unexpected list of locales for input: " + locale, expected, actual);

        // Test null input
        Assert.assertEquals("Unexpected empty list for null input", Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        // Test size
        int size = LocaleUtils.availableLocaleList().size();
        Assert.assertEquals("Unexpected size of available locales", 144, size);

        // Test contains
        Locale locale = new Locale("fr", "CA");
        Assert.assertTrue("Locale not available: " + locale, LocaleUtils.isAvailableLocale(locale));

        // Test empty input
        Assert.assertFalse("Empty list contains locale", LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test valid input
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals("Unexpected list of languages for country: " + countryCode, expected, actual);

        // Test null input
        Assert.assertEquals("Empty list for null input", Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Test valid input
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", "BE"), new Locale("fr", "CH"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals("Unexpected list of countries for language: " + languageCode, expected, actual);

        // Test null input
        Assert.assertEquals("Empty list for null input", Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }
}