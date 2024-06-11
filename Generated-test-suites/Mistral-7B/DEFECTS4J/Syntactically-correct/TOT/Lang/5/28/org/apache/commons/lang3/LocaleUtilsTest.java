package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class LocaleUtilsTest {

    private final String strLocale;
    private final String expectedLanguage;
    private final String expectedCountry;
    private final String expectedVariant;
    private final Locale expectedLocale;

    public LocaleUtilsTest(String strLocale, String expectedLanguage, String expectedCountry, String expectedVariant, Locale expectedLocale) {
        this.strLocale = strLocale;
        this.expectedLanguage = expectedLanguage;
        this.expectedCountry = expectedCountry;
        this.expectedVariant = expectedVariant;
        this.expectedLocale = expectedLocale;
    }

    @Parameterized.Parameters(name = "{index}: toLocale({0}) -> {1}, {2}, {3}, {4}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"en", "en", "", "", new Locale("en")},
                {"en_US", "en", "US", "", new Locale("en", "US")},
                {"en_US_POSIX", "en", "US", "", new Locale("en", "US", "POSIX")},
                {"en_GB", "en", "GB", "", new Locale("en", "GB")},
                {"en_GB_POSIX", "en", "GB", "", new Locale("en", "GB", "POSIX")},
                {"en_GB_x", "en", "GB", "x", new Locale("en", "GB", "x")},
                {"en_GB_x_POSIX", "en", "GB", "x", new Locale("en", "GB", "x")},
                {"fr", "fr", "", "", new Locale("fr")},
                {null, null, null, null, null},
                {"a_Z", "a", "Z", "", new Locale("a", "Z")},
                {"zxx_ZZ", "", "", "zz", new Locale("", "zz")},
                {"zz_XX", null, null, "XX", new Locale("zz", "XX")},
                {"xx_XX", null, null, null, new Locale("xx")},
                {"", "", "", "", null},
                {" ", "", "", "", null},
                {"en_US_POSIX.UTF-8", "en", "US", "UTF-8", new Locale("en", "US", "UTF-8")},
                {"en_US_POSIX.UTF-8_varying", "en", "US", "UTF-8_varying", new Locale("en", "US", "UTF-8_varying")},
                {"en_US_POSIX.en_US.UTF-8", "en", "US", "UTF-8", new Locale("en", "US")},
                {"en_US_POSIX.en_US.UTF-8_varying", "en", "US", "UTF-8_varying", new Locale("en", "US")},
                {"en_US_POSIX.en_US.utf-8", "en", "US", "utf-8", new Locale("en", "US")},
                {"en_US_POSIX.en_US.utf-8_varying", "en", "US", "utf-8_varying", new Locale("en", "US")}
        });
    }

    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale(strLocale);
        assertEquals(expectedLanguage, locale.getLanguage());
        assertEquals(expectedCountry, locale.getCountry());
        assertEquals(expectedVariant, locale.getVariant());
        assertNotSame(strLocale, locale.toString());
        assertNotSame(this, locale);
        assertEquals(expectedLocale, locale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("xx_xx_xx");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        assertEquals(expectedLocaleList, localeList);
    }

    @Test(expected = NullPointerException.class)
    public void testLocaleLookupList_NullLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
    }

    @Test(expected = NullPointerException.class)
    public void testLocaleLookupList_NullDefaultLocale() {
        List<Locale> localeList = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), null);
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "FR";
        List<Locale> langs = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expectedLangs = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr", "BE"),
                new Locale("fr", "CH"),
                new Locale("fr", "FR"),
                new Locale("fr", "LU"),
                new Locale("fr", "MC")
        );
        assertEquals(expectedLangs, langs);
    }

    @Test(expected = NullPointerException.class)
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> langs = LocaleUtils.languagesByCountry(null);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "EN";
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expectedCountries = Arrays.asList(
                new Locale("EN", "AU"),
                new Locale("EN", "BZ"),
                new Locale("EN", "CA"),
                new Locale("EN", "GB"),
                new Locale("EN", "IE"),
                new Locale("EN", "JE"),
                new Locale("EN", "NG"),
                new Locale("EN", "NZ"),
                new Locale("EN", "PH"),
                new Locale("EN", "SG"),
                new Locale("EN", "TR"),
                new Locale("EN", "US"),
                new Locale("EN", "ZA"),
                new Locale("EN", "ZW")
        );
        assertEquals(expectedCountries, countries);
    }

    @Test(expected = NullPointerException.class)
    public void testCountriesByLanguage_NullLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage(null);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        locale = new Locale("fr", "XX", "xxx");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test(expected = NullPointerException.class)
    public void testIsAvailableLocale_NullLocale() {
        boolean available = LocaleUtils.isAvailableLocale(null);
    }
}