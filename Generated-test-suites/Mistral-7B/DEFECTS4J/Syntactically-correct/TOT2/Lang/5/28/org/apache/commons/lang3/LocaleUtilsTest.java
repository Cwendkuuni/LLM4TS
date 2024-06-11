import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Test class for LocaleUtils.
 */
public class LocaleUtilsTest {

    /**
     * Test for toLocale method with valid input.
     */
    @Test
    public void testToLocaleValidInput() {
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        Assert.assertEquals("Expected locale to match", expectedLocale, actualLocale);
    }

    /**
     * Test for toLocale method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleInvalidInput() {
        String invalidLocaleString = "enUS";
        LocaleUtils.toLocale(invalidLocaleString);
    }

    /**
     * Test for localeLookupList method with valid input.
     */
    @Test
    public void testLocaleLookupListValidInput() {
        String baseLocale = "fr_CA";
        String defaultLocale = "en";

        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale(baseLocale),
                new Locale(baseLocale.substring(0, 2), baseLocale.substring(3)),
                new Locale(baseLocale.substring(0, 2)),
                new Locale(defaultLocale)
        );

        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(Locale.parse(baseLocale), Locale.parse(defaultLocale));
        Assert.assertEquals("Expected locale list to match", expectedLocaleList, actualLocaleList);
    }

    /**
     * Test for localeLookupList method with null input.
     */
    @Test
    public void testLocaleLookupListNullInput() {
        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(null);
        Assert.assertTrue("Expected empty list", actualLocaleList.isEmpty());
    }

    /**
     * Test for availableLocaleList method.
     */
    @Test
    public void testAvailableLocaleList() {
        List<Locale> expectedLocaleList = LocaleUtils.availableLocaleList();
        int expectedSize = expectedLocaleList.size();
        List<Locale> actualLocaleList = Locale.getAvailableLocales();
        int actualSize = actualLocaleList.size();

        Assert.assertEquals("Expected size of available locales to match", expectedSize, actualSize);
        for (int i = 0; i < expectedSize; i++) {
            Assert.assertEquals("Expected locale to match at index " + i, expectedLocaleList.get(i), actualLocaleList.get(i));
        }
    }

    /**
     * Test for languagesByCountry method.
     */
    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA"),
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );

        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals("Expected locale list to match", expectedLocaleList, actualLocaleList);
    }

    /**
     * Test for countriesByLanguage method.
     */
    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(
                new Locale("CA", "en"),
                new Locale("US", "en"),
                new Locale("GB", "en"),
                new Locale("GB", "en"),
                new Locale("JM", "en"),
                new Locale("IE", "en")
        );

        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals("Expected locale list to match", expectedLocaleList, actualLocaleList);
    }
}