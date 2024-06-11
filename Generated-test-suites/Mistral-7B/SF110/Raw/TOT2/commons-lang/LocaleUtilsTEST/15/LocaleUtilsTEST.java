
package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String validInput = "en_US";
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale(validInput);
        Assert.assertEquals(expected, actual);

        String invalidInput = "invalid_locale";
        try {
            Locale actual = LocaleUtils.toLocale(invalidInput);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "GB");
        List<Locale> expected = Arrays.asList(locale, new Locale("en", ""), new Locale("", "GB"));
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        Locale nullLocale = null;
        List<Locale> actualNull = LocaleUtils.localeLookupList(nullLocale);
        Assert.assertTrue(actualNull.isEmpty());
    }

    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("en", "GB");
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(locale, new Locale("en", ""), new Locale("", "GB"), defaultLocale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, defaultLocale);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Arrays.asList(LocaleUtils.AVAILABLE_LOCALE_LIST.toArray(new Locale[0]));
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        Set<Locale> actual = new HashSet<>(LocaleUtils.AVAILABLE_LOCALE_LIST);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "FR");
        boolean expected = true;
        boolean actual = LocaleUtils.isAvailableLocale(locale);
        Assert.assertEquals(expected, actual);

        Locale invalidLocale = new Locale("invalid_locale", "invalid_country");
        boolean actualInvalid = LocaleUtils.isAvailableLocale(invalidLocale);
        Assert.assertFalse(actualInvalid);
    }

    @Test
    public void testLanguagesByCountry() {
        String usCountryCode = "US";
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "US-POSIX"), new Locale("en_US_POSIX"), new Locale("en_US_V Ariant"));
        List<Locale> actual = LocaleUtils.languagesByCountry(usCountryCode);
        Assert.assertArrayEquals(expected.toArray(), actual.toArray(new Locale[0]));

        String nullCountryCode = null;
        List<Locale> actualNull = LocaleUtils.languagesByCountry(nullCountryCode);
        Assert.assertTrue(actualNull.isEmpty());
    }

    @Test
    public void testCountriesByLanguage() {
        String enLanguageCode = "en";
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "ZA"));
        List<Locale> actual = LocaleUtils.countriesByLanguage(enLanguageCode);
        Assert.assertEquals(expected, actual);

        String nullLanguageCode = null;
        List<Locale> actualNull = LocaleUtils.countriesByLanguage(nullLanguageCode);
        Assert.assertTrue(actualNull.isEmpty());
    }
}