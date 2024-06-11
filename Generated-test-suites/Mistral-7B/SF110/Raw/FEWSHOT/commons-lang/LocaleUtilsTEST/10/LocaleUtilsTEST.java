package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleEmpty() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleNull() {
        LocaleUtils.toLocale(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat1() {
        LocaleUtils.toLocale("A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidFormat2() {
        LocaleUtils.toLocale("en_FR_fr_FR");
    }

    @Test
    public void testToLocaleValidFormat1() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocaleValidFormat2() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
    }

    @Test
    public void testToLocaleValidFormat3() {
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
    }

    @Test
    public void testToLocaleValidFormat4() {
        assertEquals(new Locale("de", "DE", "CH"), LocaleUtils.toLocale("de_DE_CH"));
    }

    @Test
    public void testLocaleLookupListEmptyLocale() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupListNullLocale() {
        List<Locale> locales = Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("de"));
        assertEquals(Collections.unmodifiableList(locales), LocaleUtils.localeLookupList(null, new Locale("en")));
    }

    @Test
    public void testLocaleLookupListEmptyCountry() {
        List<Locale> locales = Arrays.asList(new Locale("en_US"), new Locale("fr_FR"), new Locale("de_DE"));
        assertEquals(Collections.unmodifiableList(Arrays.asList(new Locale("en_US"), new Locale("en"), new Locale("fr_FR"), new Locale("fr"), new Locale("de_DE"), new Locale("de"))), LocaleUtils.localeLookupList(new Locale("en"), null));
    }

    @Test
    public void testLocaleLookupListDefaultLocale() {
        List<Locale> locales = Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("de"));
        assertEquals(Collections.unmodifiableList(Arrays.asList(new Locale("en"), new Locale("fr"), new Locale("de"), new Locale("en"))), LocaleUtils.localeLookupList(null, new Locale("en")));
    }

    @Test
    public void testLocaleLookupListValidLocale() {
        List<Locale> locales = Arrays.asList(new Locale("en_US"), new Locale("fr_FR"), new Locale("de_DE"));
        assertEquals(Collections.unmodifiableList(Arrays.asList(new Locale("en_US"), new Locale("en"), new Locale("fr_FR"), new Locale("fr"), new Locale("de_DE"), new Locale("de"))), LocaleUtils.localeLookupList(new Locale("en_US"), new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        Set<Locale> locales = Collections.unmodifiableSet(Locale.getAvailableLocales());
        assertEquals(locales, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> frLocales = Arrays.asList(new Locale("fr_FR"), new Locale("fr_BE"), new Locale("fr_CA"));
        assertEquals(Collections.unmodifiableList(frLocales), LocaleUtils.languagesByCountry("FR"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> enLocales = Arrays.asList(new Locale("en_US"), new Locale("en_GB"), new Locale("en_IE"));
        assertEquals(Collections.unmodifiableList(enLocales), LocaleUtils.countriesByLanguage("en"));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en_US")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("invalid_locale")));
    }
}