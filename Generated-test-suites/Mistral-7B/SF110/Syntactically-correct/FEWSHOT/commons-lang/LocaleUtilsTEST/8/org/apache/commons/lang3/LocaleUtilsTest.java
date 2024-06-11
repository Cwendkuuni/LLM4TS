package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_Empty() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        throwException(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_"));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        throwException(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_xx"));
    }

    @Test
    public void testToLocale_InvalidFormat_Malformed() {
        throwException(IllegalArgumentException.class, () -> LocaleUtils.toLocale("xx_xxxx"));
    }

    @Test
    public void testToLocale_ValidFormat() {
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageOnly() {
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
    }

    @Test
    public void testToLocale_ValidFormat_CountryOnly() {
        assertEquals(new Locale("FR"), LocaleUtils.toLocale("FR"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageAndCountry() {
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageAndCountryAndVariant() {
        assertEquals(new Locale("fr", "FR", "Latn"), LocaleUtils.toLocale("fr_FR_Latn"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageAndCountryAndVariant_Empty() {
        assertEquals(new Locale("fr", "FR"), LocaleUtils.toLocale("fr_FR"));
    }

    @Test
    public void testToLocale_ValidFormat_LanguageAndCountryAndVariant_NonEmpty() {
        assertEquals(new Locale("fr", "FR", "Latn"), LocaleUtils.toLocale("fr_FR_Latn"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        Locale locale = new Locale("fr", "CA");
        List<Locale> expected = Arrays.asList(locale, new Locale("fr", "FR"), new Locale("fr", "BE"), new Locale("fr", "CH"), new Locale("fr", "LU"));
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> result = LocaleUtils.localeLookupList(defaultLocale);
        List<Locale> expected = Arrays.asList(defaultLocale, new Locale("en"), new Locale(""));
        assertEquals(expected, result);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("es"), new Locale("fr"), new Locale("hi"), new Locale("pt"));
        assertEquals(expected, result);
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry("");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"), new Locale("IE"), new Locale("AU"));
        assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        List<Locale> result = LocaleUtils.countriesByLanguage("");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("non-existent", "non-existent")));
    }

    private void throwException(Class<? extends Throwable> expectedExceptionClass, Runnable runnable) {
        try {
            runnable.run();
            fail("Expected exception of type " + expectedExceptionClass.getName() + " was not thrown.");
        } catch (Throwable e) {
            if (expectedExceptionClass.isInstance(e)) {
                return;
            }
            throw new RuntimeException("Unexpected exception: " + e.getClass().getName(), e);
        }
    }
}