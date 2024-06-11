package org.apache.commons.lang3;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_EmptyString() {
        assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        assertNull(LocaleUtils.toLocale("fr"));
        assertNull(LocaleUtils.toLocale("fr_"));
        assertNull(LocaleUtils.toLocale("fr_FR"));
        assertNull(LocaleUtils.toLocale("fr_FR_"));
        assertNull(LocaleUtils.toLocale("fr_FR_xxx"));
        assertNull(LocaleUtils.toLocale("fr_FR_xx_"));
        assertNull(LocaleUtils.toLocale("fr_FR_xx_xx"));
        assertNull(LocaleUtils.toLocale("fr_FR_xx_xx_"));
        assertNull(LocaleUtils.toLocale("fr_FR_xx_xx_xx"));
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        assertNull(LocaleUtils.toLocale("__"));
        assertNull(LocaleUtils.toLocale("__xx"));
        assertNull(LocaleUtils.toLocale("__xx_"));
        assertNull(LocaleUtils.toLocale("__xx_xx"));
        assertNull(LocaleUtils.toLocale("__xx_xx_"));
        assertNull(LocaleUtils.toLocale("__xx_xx_xx"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguage() {
        assertNull(LocaleUtils.toLocale("xx_XX"));
        assertNull(LocaleUtils.toLocale("xx_XX_"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx_"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx_xx"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx_xx_"));
        assertNull(LocaleUtils.toLocale("xx_XX_xx_xx_xx"));
    }

    @Test
    public void testToLocale_ValidFormat() {
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("fr", "", "qc"), LocaleUtils.toLocale("fr_", "_qc"));
        assertEquals(new Locale("fr", "CA", "qc"), LocaleUtils.toLocale("fr_CA_qc"));
        assertEquals(new Locale("fr", "CA", "qc_CA"), LocaleUtils.toLocale("fr_CA_qc_CA"));
        assertEquals(new Locale("fr", "CA", "qc_CA_xx"), LocaleUtils.toLocale("fr_CA_qc_CA_xx"));
        assertEquals(new Locale("fr", "CA", "qc_CA_xx_xx"), LocaleUtils.toLocale("fr_CA_qc_CA_xx_xx"));
        assertEquals(new Locale("fr", "CA", "qc_CA_xx_xx_xx"), LocaleUtils.toLocale("fr_CA_qc_CA_xx_xx_xx"));
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant() {
        List<Locale> locales = Arrays.asList(new Locale("fr", "CA"), new Locale("fr_CA_fr_CA"));
        assertEquals(locales, LocaleUtils.localeLookupList(new Locale("fr_CA", null)));
    }

    @Test
    public void testLocaleLookupList_LocaleWithoutVariant() {
        List<Locale> locales = Arrays.asList(new Locale("fr"), new Locale("fr_CA"), new Locale("en_US"));
        assertEquals(locales, LocaleUtils.localeLookupList(new Locale("fr", "CA"), null));
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> locales = Arrays.asList(new Locale("fr"), new Locale("fr_CA"), new Locale("en_US"));
        assertEquals(Arrays.asList(new Locale("en_US")), LocaleUtils.localeLookupList(null, new Locale("en_US")));
    }

    @Test
    public void testAvailableLocaleList_Empty() {
        assertTrue(LocaleUtils.availableLocaleList().isEmpty());
    }

    @Test
    public void testAvailableLocaleList_NotEmpty() {
        List<Locale> availableLocales = Arrays.asList(new Locale("fr"), new Locale("en_US"));
        assertEquals(availableLocales, LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet_Empty() {
        assertTrue(LocaleUtils.availableLocaleSet().isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_NotEmpty() {
        Set<Locale> availableLocales = Collections.newHashSet(Arrays.asList(new Locale("fr"), new Locale("en_US")));
        assertEquals(availableLocales, LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en_US")));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx_XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr_XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("fr_XX_")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("fr"), new Locale("fr_CA"), new Locale("fr_BE"), new Locale("fr_CH"));
        assertEquals(expected, LocaleUtils.languagesByCountry("CA"));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("CA_FR"), new Locale("BE"), new Locale("BE_FR"), new Locale("CH"), new Locale("CH_FR"));
        assertEquals(expected, LocaleUtils.countriesByLanguage("fr"));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
    }

}