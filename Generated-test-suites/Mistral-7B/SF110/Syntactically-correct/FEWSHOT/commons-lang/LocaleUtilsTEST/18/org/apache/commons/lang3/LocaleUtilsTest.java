package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.isInstanceOf;

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
    public void testToLocale_EmptyString() {
        assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoLanguage() {
        LocaleUtils.toLocale("_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_NoCountry() {
        LocaleUtils.toLocale("en");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_EmptyLanguage() {
        LocaleUtils.toLocale("_fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_EmptyCountry() {
        LocaleUtils.toLocale("en_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidCountryCode() {
        LocaleUtils.toLocale("en_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidLanguageCode() {
        LocaleUtils.toLocale("zz_fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_LeadingAndTrailingHyphens() {
        LocaleUtils.toLocale("--en--fr--");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_TrailingHyphens() {
        LocaleUtils.toLocale("en-_fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_MultipleHyphens() {
        LocaleUtils.toLocale("en--fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_MultipleLanguageTags() {
        LocaleUtils.toLocale("en_fr_de");
    }

    @Test
    public void testToLocale_ValidFormat() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en-US"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("es", "ES"), LocaleUtils.toLocale("es"));
        assertEquals(new Locale("zh_CN", ""), LocaleUtils.toLocale("zh_CN"));
        assertEquals(new Locale("en", "", "GB"), LocaleUtils.toLocale("en-GB"));
        assertEquals(new Locale("fr", "", "FR"), LocaleUtils.toLocale("fr"));
        assertEquals(new Locale("", "zh_CN"), LocaleUtils.toLocale("zh_CN"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> locales = LocaleUtils.localeLookupList(Locale.US);
        assertEquals(4, locales.size());
        assertTrue(locales.contains(Locale.US));
        assertTrue(locales.contains(new Locale("en", "US")));
        assertTrue(locales.contains(new Locale("en", "")));
        assertTrue(locales.contains(Locale.getDefault()));

        locales = LocaleUtils.localeLookupList(Locale.FRENCH);
        assertEquals(3, locales.size());
        assertTrue(locales.contains(Locale.FRENCH));
        assertTrue(locales.contains(new Locale("fr", "CA")));
        assertTrue(locales.contains(Locale.getDefault()));

        locales = LocaleUtils.localeLookupList(Locale.getDefault());
        assertEquals(1, locales.size());
        assertTrue(locales.contains(Locale.getDefault()));
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertEquals(0, LocaleUtils.localeLookupList(null).size());
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> locales = LocaleUtils.localeLookupList(Locale.getDefault());
        assertEquals(1, locales.size());
        assertTrue(locales.contains(Locale.getDefault()));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> locales = LocaleUtils.availableLocaleList();
        assertTrue(locales.size() > 0);
        assertTrue(locales.get(0).getDisplayName().length() > 0);
        assertFalse(locales.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> locales = LocaleUtils.availableLocaleSet();
        assertTrue(locales.size() > 0);
        assertTrue(locales.contains(Locale.US));
        assertTrue(locales.contains(Locale.getDefault()));
        assertFalse(locales.isEmpty());
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.getDefault()));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en", "GB"), new Locale("en", "US"));
        assertEquals(expected, LocaleUtils.languagesByCountry("GB"));

        expected = Collections.emptyList();
        assertEquals(expected, LocaleUtils.languagesByCountry(null));

        expected = Arrays.asList(new Locale("es"), new Locale("es", "ES"));
        assertEquals(expected, LocaleUtils.languagesByCountry("es"));

        expected = Arrays.asList(new Locale("zh", "CN"), new Locale("zh", "TW"), new Locale("zh", "HK"));
        assertEquals(expected, LocaleUtils.languagesByCountry("zh_CN"));

        expected = Collections.singletonList(new Locale("fr"));
        assertEquals(expected, LocaleUtils.languagesByCountry("fr_FR"));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("US"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("en"));

        expected = Arrays.asList(new Locale("ES"), new Locale("ES", "ES"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("es"));

        expected = Arrays.asList(new Locale("CN"), new Locale("CN", "Zhonghua"), new Locale("TW"), new Locale("HK"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("zh"));

        expected = Collections.singletonList(new Locale("FR"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("fr"));
    }

    @Test
    public void testLocaleUtilsStaticInitialization() {
        assertNotNull(LocaleUtils.cLanguagesByCountry);
        assertNotNull(LocaleUtils.cCountriesByLanguage);
    }
}