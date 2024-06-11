package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.contains;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class LocaleUtilsTest {

    private static LocaleUtils utils;

    @BeforeClass
    public static void setUpBeforeClass() {
        utils = new LocaleUtils();
    }

    // test toLocale() method
    @Test
    public void testToLocale() {
        // test empty String
        assertNull(utils.toLocale(""));

        // test invalid format - no language and country
        assertNull(utils.toLocale("XX"));

        // test invalid format - only language
        assertEquals(new Locale("en"), utils.toLocale("en"));

        // test invalid format - language and country with invalid characters
        assertNull(utils.toLocale("en_ää"));

        // test valid format - language and country
        assertEquals(new Locale("fr", "FR"), utils.toLocale("fr_FR"));
        assertEquals(new Locale("en", "US"), utils.toLocale("en_US"));

        // test valid format - language, country and variant
        assertEquals(new Locale("es", "ES", "Castilian"), utils.toLocale("es_ES_Castilian"));

        // test invalid format - language, country and invalid variant
        assertNull(utils.toLocale("es_ES_Castilian_invalid"));
    }

    // test localeLookupList() method
    @Test
    public void testLocaleLookupList() {
        // test null locale
        assertEquals(Collections.emptyList(), utils.localeLookupList(null));

        // test locale with language and country
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", ""));
        assertEquals(expected, utils.localeLookupList(new Locale("en", "US")));

        // test locale with language, country and variant
        expected = Arrays.asList(new Locale("es", "ES"), new Locale("es", ""), new Locale("es", ""));
        assertEquals(expected, utils.localeLookupList(new Locale("es", "ES", "Castilian")));

        // test locale with only language
        expected = Arrays.asList(new Locale("en"), new Locale("en"), new Locale(""));
        assertEquals(expected, utils.localeLookupList(new Locale("en")));

        // test locale with only country
        expected = Arrays.asList(new Locale("", "US"), new Locale("en", "US"), new Locale(""));
        assertEquals(expected, utils.localeLookupList(new Locale("", "US")));
    }

    // test availableLocaleList() method
    @Test
    public void testAvailableLocaleList() {
        // test empty list
        assertTrue(utils.availableLocaleList().isEmpty());

        // test non-empty list
        List<Locale> availableLocales = Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("es"));
        assertEquals(availableLocales, utils.availableLocaleList());
    }

    // test availableLocaleSet() method
    @Test
    public void testAvailableLocaleSet() {
        // test empty set
        assertTrue(utils.availableLocaleSet().isEmpty());

        // test non-empty set
        Set<Locale> availableLocales = new HashSet<Locale>(Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("es")));
        assertEquals(availableLocales, utils.availableLocaleSet());
    }

    // test isAvailableLocale() method
    @Test
    public void testIsAvailableLocale() {
        // test null locale
        assertFalse(utils.isAvailableLocale(null));

        // test available locale
        assertTrue(utils.isAvailableLocale(new Locale("en")));

        // test unavailable locale
        assertFalse(utils.isAvailableLocale(new Locale("en", "XX")));
    }

    // test languagesByCountry() method
    @Test
    public void testLanguagesByCountry() {
        // test null country code
        assertTrue(utils.languagesByCountry(null).isEmpty());

        // test empty list
        assertTrue(utils.languagesByCountry("").isEmpty());

        // test available languages for a country
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("es"));
        assertEquals(expected, utils.languagesByCountry("US"));

        // test unavailable languages for a country
        assertTrue(utils.languagesByCountry("XX").isEmpty());
    }

    // test countriesByLanguage() method
    @Test
    public void testCountriesByLanguage() {
        // test null language code
        assertTrue(utils.countriesByLanguage(null).isEmpty());

        // test empty list
        assertTrue(utils.countriesByLanguage("").isEmpty());

        // test available countries for a language
        List<Locale> expected = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("MX"));
        assertEquals(expected, utils.countriesByLanguage("en"));

        // test unavailable countries for a language
        assertTrue(utils.countriesByLanguage("XX").isEmpty());
    }
}