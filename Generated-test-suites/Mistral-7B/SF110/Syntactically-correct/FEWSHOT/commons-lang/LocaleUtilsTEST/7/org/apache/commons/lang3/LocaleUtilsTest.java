package org.apache.commons.lang3;

import static org.junit.Assert.*;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_ "));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_ foo"));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_fr"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_fr_"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_fr_ foo"));
    }

    @Test
    public void testToLocale_ValidFormat_TwoParts() {
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("fr_CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("fr_CH"), LocaleUtils.toLocale("fr_CH"));
    }

    @Test
    public void testToLocale_ValidFormat_ThreeParts() {
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("fr", "CH"), LocaleUtils.toLocale("fr_CH"));
        assertEquals(new Locale("es", "ES"), LocaleUtils.toLocale("es_ES"));
    }

    @Test
    public void testToLocale_ValidFormat_FourParts() {
        assertEquals(new Locale("es", "ES", "Castilian"), LocaleUtils.toLocale("es_ES_Castilian"));
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        assertNotNull(LocaleUtils.localeLookupList(null));
        assertFalse(LocaleUtils.localeLookupList(null).isEmpty());
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        assertNotNull(LocaleUtils.localeLookupList(new Locale("")));
        assertTrue(LocaleUtils.localeLookupList(new Locale("")).isEmpty());
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant() {
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("en_US"));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("en_US")));
    }

    @Test
    public void testLocaleLookupList_LocaleWithoutCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("en_US"), new Locale(""));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("en")));
    }

    @Test
    public void testLocaleLookupList_LocaleWithoutLanguage() {
        List<Locale> expected = Arrays.asList(new Locale(""), new Locale("en"), new Locale("en_US"));
        assertEquals(expected, LocaleUtils.localeLookupList(new Locale("")));
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("en_US"), new Locale(""));
        assertEquals(expected, LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        assertTrue(expected.size() > 0);
        for (Locale locale : expected) {
            assertNotNull(locale);
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> expected = LocaleUtils.availableLocaleSet();
        assertTrue(expected.size() > 0);
        for (Locale locale : expected) {
            assertNotNull(locale);
        }
    }

    @Test
    public void testIsAvailableLocale_NullInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("foo", "bar")));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en_US")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("es"), new Locale("es_MX"));
        assertEquals(expected, LocaleUtils.languagesByCountry("MX"));
        assertEmpty(LocaleUtils.languagesByCountry(null));
        assertEmpty(LocaleUtils.languagesByCountry(""));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("es", "MX"), new Locale("es_ES"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("es"));
        assertEmpty(LocaleUtils.countriesByLanguage(null));
        assertEmpty(LocaleUtils.countriesByLanguage(""));
    }
}