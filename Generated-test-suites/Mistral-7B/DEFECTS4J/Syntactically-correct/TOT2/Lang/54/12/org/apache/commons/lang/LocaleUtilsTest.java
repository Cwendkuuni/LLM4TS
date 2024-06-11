import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    // Test for toLocale method
    @Test
    public void testToLocale() {
        // Basic test case
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        assertEquals(expectedLocale, actualLocale);

        // Edge case 1: Empty string
        String emptyString = "";
        Locale emptyLocale = Locale.getDefault();
        Locale testEmptyLocale = LocaleUtils.toLocale(emptyString);
        assertEquals(emptyLocale, testEmptyLocale);

        // Edge case 2: Invalid format
        String invalidLocaleString = "xy_ZW";
        Locale invalidLocale = null;
        try {
            invalidLocale = LocaleUtils.toLocale(invalidLocaleString);
        } catch (IllegalArgumentException e) {
            // Expected
        }
        assertNull(invalidLocale);
    }

    // Test for localeLookupList method
    @Test
    public void testLocaleLookupList() {
        // Basic test case
        Locale baseLocale = Locale.forLanguageTag("fr-CA");
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA", "CA"),
                new Locale("fr", "CA"),
                new Locale("fr", ""),
                Locale.getDefault()
        );
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case 1: Null baseLocale
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> actualEmptyLocaleList = LocaleUtils.localeLookupList(null);
        assertEquals(emptyLocaleList, actualEmptyLocaleList);

        // Edge case 2: Null defaultLocale
        List<Locale> defaultLocaleNullList = Arrays.asList(Locale.getDefault());
        List<Locale> actualDefaultLocaleNullList = LocaleUtils.localeLookupList(baseLocale, null);
        assertEquals(expectedLocaleList, actualDefaultLocaleNullList);
    }

    // Test for availableLocaleList method
    @Test
    public void testAvailableLocaleList() {
        // Basic test case
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> actualLocaleList = Collections.emptyList();
        assertTrue(expectedLocaleList.size() > 0);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case: Empty list
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> actualEmptyLocaleList = LocaleUtils.availableLocaleList();
        assertEquals(emptyLocaleList, actualEmptyLocaleList);
    }

    // Test for isAvailableLocale method
    @Test
    public void testIsAvailableLocale() {
        // Basic test case
        Locale locale = new Locale("en", "US");
        List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
        assertTrue(LocaleUtils.isAvailableLocale(locale));
        assertTrue(availableLocaleList.contains(locale));

        // Edge case 1: Null locale
        assertFalse(LocaleUtils.isAvailableLocale(null));

        // Edge case 2: Invalid locale
        Locale invalidLocale = new Locale("xy", "ZW");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
        assertFalse(LocaleUtils.availableLocaleList().contains(invalidLocale));
    }

    // Test for languagesByCountry method
    @Test
    public void testLanguagesByCountry() {
        // Basic test case
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en", "CA")
        );
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case 1: Null countryCode
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> actualEmptyLocaleList = LocaleUtils.languagesByCountry(null);
        assertEquals(emptyLocaleList, actualEmptyLocaleList);

        // Edge case 2: Invalid countryCode
        String invalidCountryCode = "XY";
        List<Locale> invalidLocaleList = Collections.emptyList();
        List<Locale> actualInvalidLocaleList = LocaleUtils.languagesByCountry(invalidCountryCode);
        assertEquals(invalidLocaleList, actualInvalidLocaleList);
    }

    // Test for countriesByLanguage method
    @Test
    public void testCountriesByLanguage() {
        // Basic test case
        String languageCode = "fr";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr", "FR")
        );
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case 1: Null languageCode
        List<Locale> emptyLocaleList = Collections.emptyList();
        List<Locale> actualEmptyLocaleList = LocaleUtils.countriesByLanguage(null);
        assertEquals(emptyLocaleList, actualEmptyLocaleList);

        // Edge case 2: Invalid languageCode
        String invalidLanguageCode = "XY";
        List<Locale> invalidLocaleList = Collections.emptyList();
        List<Locale> actualInvalidLocaleList = LocaleUtils.countriesByLanguage(invalidLanguageCode);
        assertEquals(invalidLocaleList, actualInvalidLocaleList);
    }
}