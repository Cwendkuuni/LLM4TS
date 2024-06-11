import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test case
        String localeString = "en_US";
        Locale expectedLocale = new Locale("en", "US");
        Locale actualLocale = LocaleUtils.toLocale(localeString);
        Assert.assertEquals(expectedLocale, actualLocale);

        // Edge case: empty string
        Assert.assertNull(LocaleUtils.toLocale(""));

        // Edge case: invalid format
        String invalidLocaleString = "en_GB_xxx";
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));

        // Error case: null input
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test case
        String baseLocaleString = "fr_CA";
        Locale baseLocale = new Locale("fr", "CA");
        Locale expectedLocale1 = new Locale("fr", "CA", "xxx");
        Locale expectedLocale2 = new Locale("fr", "CA");
        Locale expectedLocale3 = new Locale("fr", "");
        List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale);
        Assert.assertEquals(Arrays.asList(expectedLocale1, expectedLocale2, expectedLocale3), localeList);

        // Edge case: empty list
        Locale emptyLocale = null;
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(emptyLocale));

        // Error case: null input
        Assert.assertThrows(NullPointerException.class, () -> LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test case
        List<Locale> expectedLocaleList = Locale.getAvailableLocales();
        List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case: empty list
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.availableLocaleList());

        // Error case: null input
        Assert.assertThrows(NullPointerException.class, () -> LocaleUtils.availableLocaleList(null));
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test case
        String availableLocaleString = "en_US";
        Locale availableLocale = new Locale("en", "US");
        boolean expectedResult = true;
        boolean actualResult = LocaleUtils.isAvailableLocale(availableLocale);
        Assert.assertEquals(expectedResult, actualResult);

        // Edge case: invalid locale
        String invalidLocaleString = "en_US_xxx";
        Locale invalidLocale = new Locale("en", "US", "xxx");
        expectedResult = false;
        actualResult = LocaleUtils.isAvailableLocale(invalidLocale);
        Assert.assertEquals(expectedResult, actualResult);

        // Error case: null input
        Assert.assertThrows(NullPointerException.class, () -> LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Basic test case
        String countryCode = "CA";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));
        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case: empty list
        String emptyCountryCode = null;
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(emptyCountryCode));

        // Error case: null input
        Assert.assertThrows(NullPointerException.class, () -> LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test case
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "GB"), new Locale("en", "US"));
        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expectedLocaleList, actualLocaleList);

        // Edge case: empty list
        String emptyLanguageCode = null;
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(emptyLanguageCode));

        // Error case: null input
        Assert.assertThrows(NullPointerException.class, () -> LocaleUtils.countriesByLanguage(null));
    }
}