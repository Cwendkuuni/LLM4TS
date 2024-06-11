package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for LocaleUtils
 */
public class LocaleUtilsTest {

    /**
     * Test toLocale method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        // Test with valid language code and empty country code
        Locale expected = new Locale("en", "");
        Locale actual = LocaleUtils.toLocale("en");
        Assert.assertEquals(expected.getLanguage(), actual.getLanguage());
        Assert.assertEquals(expected.getCountry(), actual.getCountry());

        // Test with valid language code, country code and variant
        expected = new Locale("en", "GB", "London");
        actual = LocaleUtils.toLocale("en_GB_London");
        Assert.assertEquals(expected.getLanguage(), actual.getLanguage());
        Assert.assertEquals(expected.getCountry(), actual.getCountry());
        Assert.assertEquals(expected.getVariant(), actual.getVariant());
    }

    /**
     * Test toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        // Test with null input
        LocaleUtils.toLocale(null);

        // Test with invalid language code and empty country code
        LocaleUtils.toLocale("AB");

        // Test with invalid language code, country code and invalid separator
        LocaleUtils.toLocale("en_GB_xx");

        // Test with invalid language code, country code and variant with invalid separator
        LocaleUtils.toLocale("en_GB_London_xx");

        // Test with invalid language code, country code and variant with invalid length
        LocaleUtils.toLocale("en_GB_London_xxxx");
    }

    /**
     * Test localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        Locale baseLocale = new Locale("fr", "CA", "xxx");
        List expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List actual = LocaleUtils.localeLookupList(baseLocale, new Locale("en"));
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test localeLookupList method with null and empty input.
     */
    @Test
    public void testLocaleLookupListNullAndEmptyInput() {
        // Test with null input
        List actual = LocaleUtils.localeLookupList(null, null);
        Assert.assertEquals(Collections.emptyList(), actual);

        // Test with empty input
        actual = LocaleUtils.localeLookupList(new Locale(""), null);
        Assert.assertEquals(Collections.emptyList(), actual);
    }

    /**
     * Test availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List expected = LocaleUtils.availableLocaleList();
        List actual = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test availableLocaleSet method.
     */
    @Test
    public void testAvailableLocaleSet() {
        Set expected = LocaleUtils.availableLocaleSet();
        Set actual = new HashSet(Locale.getAvailableLocales());
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test isAvailableLocale method with valid input.
     */
    @Test
    public void testIsAvailableLocaleValidInput() {
        Locale locale = new Locale("en", "GB");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test isAvailableLocale method with invalid input.
     */
    @Test
    public void testIsAvailableLocaleInvalidInput() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
        Locale locale = new Locale("en", "GB", "London");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    /**
     * Test languagesByCountry method with valid input.
     */
    @Test
    public void testLanguagesByCountryValidInput() {
        List expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en", "CA")
        );
        List actual = LocaleUtils.languagesByCountry("CA");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test languagesByCountry method with null input.
     */
    @Test
    public void testLanguagesByCountryNullInput() {
        List actual = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(Collections.emptyList(), actual);
    }

    /**
     * Test countriesByLanguage method with valid input.
     */
    @Test
    public void testCountriesByLanguageValidInput() {
        List expected = Arrays.asList(
                new Locale("CA", "en"),
                new Locale("US", "en")
        );
        List actual = LocaleUtils.countriesByLanguage("en");
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test countriesByLanguage method with null input.
     */
    @Test
    public void testCountriesByLanguageNullInput() {
        List actual = LocaleUtils.countriesByLanguage(null);
        Assert.assertEquals(Collections.emptyList(), actual);
    }
}