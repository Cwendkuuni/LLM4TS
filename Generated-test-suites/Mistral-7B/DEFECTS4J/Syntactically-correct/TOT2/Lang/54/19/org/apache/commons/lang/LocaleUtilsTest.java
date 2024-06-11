import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Locale;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        assertEquals(expected, actual);

        expected = new Locale("fr", "CA");
        actual = LocaleUtils.toLocale("fr_CA");
        assertEquals(expected, actual);

        expected = new Locale("es", "");
        actual = LocaleUtils.toLocale("es");
        assertEquals(expected, actual);

        // Test invalid locale strings
        try {
            LocaleUtils.toLocale(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("AB_CD");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            LocaleUtils.toLocale("12_34");
            fail("NumberFormatException expected");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = LocaleUtils.localeLookupList(locale);
        List<Locale> actual = LocaleUtils.localeLookupList(locale, null);

        List<Locale> expectedList = Arrays.asList(
                locale,
                new Locale("fr", "CA"),
                new Locale("fr", ""),
                null
        );

        assertEquals(expectedList, expected);
        assertEquals(expectedList, actual);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> expected = LocaleUtils.availableLocaleList();
        List<Locale> actual = Locale.getAvailableLocales();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        Locale localeWithVariant = new Locale("fr", "CA", "Canada");
        assertFalse(LocaleUtils.isAvailableLocale(localeWithVariant));

        Locale invalidLocale = new Locale("ab", "cd");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));

        Locale emptyLocale = new Locale("");
        assertFalse(LocaleUtils.isAvailableLocale(emptyLocale));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> expected = LocaleUtils.languagesByCountry("US");
        List<Locale> actual = LocaleUtils.languagesByCountry("US");

        List<Locale> expectedList = Arrays.asList(
                new Locale("en", "US"),
                new Locale("es", "US"),
                new Locale("fr", "US")
        );

        assertEquals(expectedList, expected);
        assertEquals(expectedList, actual);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> expected = LocaleUtils.countriesByLanguage("en");
        List<Locale> actual = LocaleUtils.countriesByLanguage("en");

        List<Locale> expectedList = Arrays.asList(
                new Locale("US", "en"),
                new Locale("GB", "en"),
                new Locale("CA", "en"),
                new Locale("AU", "en")
        );

        assertEquals(expectedList, expected);
        assertEquals(expectedList, actual);
    }
}