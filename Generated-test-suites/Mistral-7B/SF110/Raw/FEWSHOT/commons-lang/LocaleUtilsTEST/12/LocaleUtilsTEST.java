package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

    @Test
    public void testToLocaleInvalidFormatEmpty() {
        try {
            LocaleUtils.toLocale("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToLocaleInvalidFormatNoLanguage() {
        try {
            LocaleUtils.toLocale("_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToLocaleInvalidFormatNoCountry() {
        try {
            LocaleUtils.toLocale("en");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToLocaleInvalidFormatInvalidCountry() {
        try {
            LocaleUtils.toLocale("en_abc");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testToLocaleValidFormat() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en-US"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr-CA"));
        assertEquals(new Locale("it_IT"), LocaleUtils.toLocale("it_IT"));
    }

    @Test
    public void testLocaleLookupListNullLocale() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupListEmptyLocale() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(new Locale("")));
    }

    @Test
    public void testLocaleLookupListValidLocale() {
        Locale locale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(locale, new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testLocaleLookupListConcurrentModification() {
        Locale locale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        list.add(new Locale("fr", "FR"));
    }

    @Test
    public void testLocaleLookupListDefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"), defaultLocale);
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("en", ""), defaultLocale);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupListEmptyAvailableLocales() {
        List<Locale> availableLocales = Collections.emptyList();
        List<Locale> expected = Arrays.asList(new Locale(""), new Locale(""));
        List<Locale> result = LocaleUtils.localeLookupList(new Locale(""), new Locale(""));
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupListValidAvailableLocales() {
        List<Locale> availableLocales = Arrays.asList(new Locale("en", "US"), new Locale("fr", "CA"), new Locale("it_IT"));
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
        List<Locale> result = LocaleUtils.localeLookupList(new Locale("en", ""), new Locale(""));
        assertEquals(expected, result);

        List<Locale> expected2 = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", ""), new Locale("", "CA"), new Locale("", ""));
        List<Locale> result2 = LocaleUtils.localeLookupList(new Locale("fr", ""), new Locale(""));
        assertEquals(expected2, result2);

        List<Locale> expected3 = Arrays.asList(new Locale("it_IT"), new Locale("it_IT"), new Locale("it", ""), new Locale("it", ""));
        List<Locale> result3 = LocaleUtils.localeLookupList(new Locale("it_IT"), new Locale("it_IT"));
        assertEquals(expected3, result3);
    }

    @Test
    public void testAvailableLocaleListEmpty() {
        assertEquals(Collections.emptyList(), LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleListValid() {
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSetEmpty() {
        assertEquals(Collections.emptySet(), LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testAvailableLocaleSetValid() {
        Set<Locale> expected = Collections.unmodifiableSet(Arrays.asList(Locale.getAvailableLocales()));
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocaleNullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocaleInvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "US_INVALID")));
    }

    @Test
    public void testIsAvailableLocaleAvailableLocale() {
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocaleUnavailableLocale() {
        Locale locale = new Locale("en", "US_UNAVAILABLE");
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountryNullCountryCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testLanguagesByCountryEmptyAvailableLocales() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(""));
    }

    @Test
    public void testLanguagesByCountryValidCountryCode() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("en", "US"));
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguageNullLanguageCode() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

    @Test
    public void testCountriesByLanguageEmptyAvailableLocales() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(""));
    }

    @Test
    public void testCountriesByLanguageValidLanguageCode() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "CA"));
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, result);
    }
}