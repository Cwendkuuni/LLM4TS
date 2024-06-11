package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        Assert.assertNotNull(LocaleUtils.toLocale("en"));
        Assert.assertNotNull(LocaleUtils.toLocale("en_US"));
        Assert.assertNotNull(LocaleUtils.toLocale("fr_CA"));
        Assert.assertNotNull(LocaleUtils.toLocale("fr_CA_Paris"));

        // Test invalid input
        Assert.assertNull(LocaleUtils.toLocale(null));
        Assert.assertNull(LocaleUtils.toLocale(""));
        Assert.assertNull(LocaleUtils.toLocale("  "));
        Assert.assertNull(LocaleUtils.toLocale("en_"));
        Assert.assertNull(LocaleUtils.toLocale("en_US_"));
        Assert.assertNull(LocaleUtils.toLocale("en_US_xxx"));
        Assert.assertNull(LocaleUtils.toLocale("en_US_Paris_xxx"));
        Assert.assertNull(LocaleUtils.toLocale("fr_CA_Paris_xxx"));

        // Test edge cases
        Assert.assertEquals("en", LocaleUtils.toLocale("en").getLanguage());
        Assert.assertEquals("en_US", LocaleUtils.toLocale("en_US").toLanguageTag());
        Assert.assertEquals("fr", LocaleUtils.toLocale("fr").getLanguage());
        Assert.assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toLanguageTag());
        Assert.assertEquals("fr_CA_Paris", LocaleUtils.toLocale("fr_CA_Paris").toLanguageTag());
    }

    @Test
    public void testLocaleLookupList() {
        // Test valid input
        Locale locale = new Locale("fr", "CA", "Paris");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, actualList);

        locale = new Locale("en");
        expectedList = Arrays.asList(locale, new Locale("en"));
        actualList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expectedList, actualList);

        // Test null input
        Assert.assertNull(LocaleUtils.localeLookupList(null));

        // Test empty list
        Locale emptyLocale = new Locale("");
        expectedList = Collections.emptyList();
        actualList = LocaleUtils.localeLookupList(emptyLocale);
        Assert.assertEquals(expectedList, actualList);

        // Test invalid locale
        Locale invalidLocale = new Locale("en_US_xxx");
        expectedList = Collections.singletonList(Locale.ENGLISH);
        actualList = LocaleUtils.localeLookupList(invalidLocale);
        Assert.assertEquals(expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        // Test valid input
        Collection<Locale> expectedLocales = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH);
        Collection<Locale> actualLocales = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedLocales, actualLocales);

        // Test empty list
        List<Locale> emptyLocaleList = Arrays.asList(new Locale(""));
        expectedLocales = emptyLocaleList;
        actualLocales = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test valid input
        Collection<Locale> expectedLocales = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.ENGLISH);
        Collection<Locale> actualLocales = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expectedLocales, actualLocales);

        // Test empty set
        Collection<Locale> emptyLocaleSet = Collections.emptySet();
        expectedLocales = emptyLocaleSet;
        actualLocales = LocaleUtils.availableLocaleSet();
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testIsAvailableLocale() {
        // Test valid input
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.SIMPLIFIED_CHINESE));
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.TRADITIONAL_CHINESE));
        Assert.assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));

        // Test invalid input
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
        Assert.assertFalse(LocaleUtils.isAvailableLocale(""));

        // Test empty locale
        Locale emptyLocale = new Locale("");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(emptyLocale));

        // Test unknown locale
        Locale unknownLocale = new Locale("unknown");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(unknownLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test valid input
        String countryCode = "CA";
        Collection<Locale> expectedLocales = Arrays.asList(new Locale("fr"), new Locale("en"));
        Collection<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedLocales, actualLocales);

        // Test null input
        Assert.assertNull(LocaleUtils.languagesByCountry(null));

        // Test empty list
        String emptyCountryCode = "";
        expectedLocales = Collections.emptyList();
        actualLocales = LocaleUtils.languagesByCountry(emptyCountryCode);
        Assert.assertEquals(expectedLocales, actualLocales);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test valid input
        String languageCode = "en";
        Collection<Locale> expectedLocales = Arrays.asList(new Locale("EN", "US"), new Locale("EN", "GB"), new Locale("EN", "CA"));
        Collection<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedLocales, actualLocales);

        // Test null input
        Assert.assertNull(LocaleUtils.countriesByLanguage(null));

        // Test empty list
        String emptyLanguageCode = "";
        expectedLocales = Collections.emptyList();
        actualLocales = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        Assert.assertEquals(expectedLocales, actualLocales);
    }
}