package org.apache.commons.lang.test;

import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("fr", "CA"), LocaleUtils.toLocale("fr_CA"));
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("fr"), LocaleUtils.toLocale("fr"));

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_US_"));
        assertNull(LocaleUtils.toLocale("en_US_xx"));
        assertNull(LocaleUtils.toLocale("en_US_xx_"));
        assertNull(LocaleUtils.toLocale("en_US_xx_xx"));
        assertNull(LocaleUtils.toLocale("en_US_xx_xx_"));
        assertNull(LocaleUtils.toLocale("en_US_xx_xx_xx"));
        assertNull(LocaleUtils.toLocale("en_US_xx_xx_xx_xx"));

        // Test borderline cases
        assertEquals(new Locale("fr", ""), LocaleUtils.toLocale("fr"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("en", "GB", ""), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "", ""), LocaleUtils.toLocale("en"));

        // Test variants
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US"));
        assertEquals(new Locale("en", "US_POSIX"), LocaleUtils.toLocale("en_US_POSIX"));
        assertEquals(new Locale("en", "US_POSIX"), LocaleUtils.toLocale("en_US.POSIX"));
        assertEquals(new Locale("en", "US_POSIX"), LocaleUtils.toLocale("en_US_POSIX.LE_ACTUAL"));
        assertEquals(new Locale("en", "US_POSIX"), LocaleUtils.toLocale("en_US_POSIX.LE_JAPANese"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US_JP"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US_JP_JP"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US_JP_ja"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US_JP_ja_JP"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US_JP_ja_JP_JP"));
        assertEquals(new Locale("en", "US"), LocaleUtils.toLocale("en_US_JP_ja_JP_JP_JP"));
    }

    @Test
    public void testLocaleLookupList() {
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en"));
        List<Locale> result = LocaleUtils.localeLookupList(Locale.FRENCH, Locale.ENGLISH);
        assertEquals(expected, result);

        result = LocaleUtils.localeLookupList(Locale.FRENCH);
        expected = Arrays.asList(new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> availableLocales = Arrays.asList(
                new Locale("en", "US"),
                new Locale("fr", "CA"),
                new Locale("es", "MX"),
                new Locale("it", "IT"),
                new Locale("de", "DE"));
        List<Locale> unavailableLocales = Arrays.asList(
                new Locale("zh", "CN"),
                new Locale("ko", "KR"),
                new Locale("ja", "JP"),
                new Locale("ru", "RU"),
                new Locale("pt", "BR"));

        for (Locale locale : availableLocales) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }

        for (Locale locale : unavailableLocales) {
            assertFalse(LocaleUtils.isAvailableLocale(locale));
        }
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = Arrays.asList(new Locale("en"), new Locale("fr"));
        List<Locale> result = LocaleUtils.languagesByCountry("CA");
        assertEquals(expected, result);

        result = LocaleUtils.languagesByCountry(null);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = Arrays.asList(new Locale("CA"), new Locale("US"));
        List<Locale> result = LocaleUtils.countriesByLanguage("en");
        assertEquals(expected, result);

        result = LocaleUtils.countriesByLanguage(null);
        assertEquals(Collections.emptyList(), result);
    }
}