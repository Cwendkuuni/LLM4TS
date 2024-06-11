package org.apache.commons.lang3.test;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Locale;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_Empty() {
        assertNotNull(LocaleUtils.toLocale(""));
        assertEquals("", LocaleUtils.toLocale("").getLanguage());
        assertEquals("", LocaleUtils.toLocale("").getCountry());
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("XX"));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguage() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("xx_XX"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidCountry() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_xx"));
    }

    @Test
    public void testToLocale_InvalidFormat_MissingLanguageOrCountry() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("_XX"));
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("XX_"));
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguageAndCountry() {
        assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("xx_xx"));
    }

    @Test
    public void testToLocale_ValidFormat_SinglePart() {
        assertEquals("en", LocaleUtils.toLocale("en").getLanguage());
    }

    @Test
    public void testToLocale_ValidFormat_TwoParts() {
        assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
    }

    @Test
    public void testToLocale_ValidFormat_ThreeParts() {
        assertEquals("en_US_POSIX", LocaleUtils.toLocale("en_US_POSIX").toString());
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testLocaleLookupList_EmptyLocale() {
        Locale locale = new Locale("");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("en_US");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(locale, result.get(0));
        assertEquals(new Locale("en", ""), result.get(1));
        assertEquals(new Locale("en", "US"), result.get(2));
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        List<Locale> result = LocaleUtils.localeLookupList((Locale)null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0).getCountry());
        assertEquals(Locale.getDefault().getLanguage(), result.get(0).getLanguage());
    }

    @Test
    public void testAvailableLocaleList_Empty() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testAvailableLocaleList_NonEmpty() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet_Empty() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testAvailableLocaleSet_NonEmpty() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidLocale() {
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xxxx")));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry(null);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testLanguagesByCountry_EmptyList() {
        List<Locale> result = LocaleUtils.languagesByCountry("XX");
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> result = LocaleUtils.languagesByCountry("US");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}