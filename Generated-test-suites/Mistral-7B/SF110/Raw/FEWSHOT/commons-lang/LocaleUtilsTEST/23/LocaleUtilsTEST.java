package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Arrays;
import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_Empty() {
        assertNull(LocaleUtils.toLocale(""));
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
    public void testToLocale_InvalidFormat_MultipleUnderlines() {
        LocaleUtils.toLocale("en_fr_US");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidCountryCodeLength() {
        LocaleUtils.toLocale("en_FR");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_InvalidLanguageCodeLength() {
        LocaleUtils.toLocale("en_fr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat_MixedCase() {
        LocaleUtils.toLocale("En_Fr");
    }

    @Test
    public void testToLocale_ValidFormat_NoLanguageOrCountry() {
        assertEquals(Locale.ROOT, LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_ValidFormat_SingleLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("en", ""));
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));
    }

    @Test
    public void testToLocale_ValidFormat_SingleLanguage_MultipleCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "IN"));
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.US);
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));

        expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"));
        actual = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.GB);
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));

        expected = Arrays.asList(new Locale("en", "IN"), new Locale("en", "US"));
        actual = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.US, Locale.IN);
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));
    }

    @Test
    public void testToLocale_ValidFormat_MultipleLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("fr_FR"), new Locale("fr", "FR"), new Locale("fr", ""));
        List<Locale> actual = LocaleUtils.localeLookupList(new Locale("fr", "FR"));
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));
    }

    @Test
    public void testLocaleLookupList_Null() {
        assertNotNull(LocaleUtils.localeLookupList(null, null));
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale() {
        List<Locale> actual = LocaleUtils.localeLookupList(null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testLocaleLookupList_EmptyLocale() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.ROOT);
        assertEquals(expected, actual);
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "IN"));
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.US);
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));
    }

    @Test
    public void testLocaleLookupList_ValidLocale_MultipleLocale() {
        List<Locale> expected = Arrays.asList(Locale.ENGLISH, new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "IN"), Locale.FRENCH, new Locale("fr", "FR"));
        List<Locale> actual = LocaleUtils.localeLookupList(Locale.ENGLISH, Locale.US, Locale.FRENCH);
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        List<Locale> actual = new ArrayList<Locale>(set);
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));
    }

    @Test
    public void testIsAvailableLocale_Null() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_NotAvailableLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("es_AR", "AR")));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "IN"));
        List<Locale> actual = LocaleUtils.languagesByCountry("US");
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("fr", "FR"), new Locale("fr", "BE"), new Locale("fr", "CA"));
        List<Locale> actual = LocaleUtils.countriesByLanguage("fr");
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(new Locale[expected.size()]), actual.toArray(new Locale[actual.size()]));
    }

    @Test
    public void testCountriesByLanguage_EmptyList() {
        List<Locale> expected = Collections.emptyList();
        List<Locale> actual = LocaleUtils.countriesByLanguage(null);
        assertEquals(expected, actual);
    }
}