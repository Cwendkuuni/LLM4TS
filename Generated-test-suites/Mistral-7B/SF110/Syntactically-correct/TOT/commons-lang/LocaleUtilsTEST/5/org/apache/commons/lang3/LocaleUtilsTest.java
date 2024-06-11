import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidLocaleFormat() {
        List<String> testCases = Arrays.asList("en_US", "fr_FR", "de_DE", "es_ES", "it_IT");
        for (String localeString : testCases) {
            Locale expectedLocale = new Locale(localeString.split("_")[0], localeString.split("_")[1]);
            Locale actualLocale = LocaleUtils.toLocale(localeString);
            Assert.assertEquals("Incorrect locale for input: " + localeString, expectedLocale, actualLocale);
        }
    }

    @Test
    public void testToLocale_InvalidLocaleFormat_EmptyString() {
        Assert.assertNull(LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidLocaleFormat_LessThanTwoChars() {
        Assert.assertNull(LocaleUtils.toLocale("ab"));
    }

    @Test
    public void testToLocale_InvalidLocaleFormat_ContainsHash() {
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_US#"));
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        Locale locale = new Locale("es", "ES");
        List<Locale> expectedList = Arrays.asList(locale, new Locale("es", ""), new Locale("", "ES"));
        List<Locale> actualList = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals("Incorrect locale lookup list for input: " + locale, expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.localeLookupList(null);
        Assert.assertEquals("Incorrect locale lookup list for null input", expectedList, actualList);
    }

    @Test
    public void testLocaleLookupList_DefaultLocale() {
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("", "en"), defaultLocale);
        List<Locale> actualList = LocaleUtils.localeLookupList(null, defaultLocale);
        Assert.assertEquals("Incorrect default locale lookup list", expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actualList = LocaleUtils.availableLocaleList();
        List<Locale> expectedList = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
        Assert.assertEquals("Incorrect available locale list", expectedList, actualList);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> actualSet = LocaleUtils.availableLocaleSet();
        Set<Locale> expectedSet = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
        Assert.assertEquals("Incorrect available locale set", expectedSet, actualSet);
    }

    @Test
    public void testIsAvailableLocale_ValidLocale() {
        Locale locale = new Locale("es", "ES");
        Assert.assertTrue("Locale should be available: " + locale, LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        Assert.assertFalse("Null locale should not be available", LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        List<Locale> expectedList = Arrays.asList(new Locale("es", "ES"), new Locale("es", "MX"), new Locale("es", "AR"));
        List<Locale> actualList = LocaleUtils.languagesByCountry("ES");
        Assert.assertEquals("Incorrect languages by country list for country code: ES", expectedList, actualList);
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.languagesByCountry("");
        Assert.assertEquals("Incorrect languages by country list for empty country code", expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        List<Locale> expectedList = Arrays.asList(new Locale("es", "ES"), new Locale("es", "MX"), new Locale("es", "AR"));
        List<Locale> actualList = LocaleUtils.countriesByLanguage("es");
        Assert.assertEquals("Incorrect countries by language list for language code: es", expectedList, actualList);
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        List<Locale> expectedList = Collections.emptyList();
        List<Locale> actualList = LocaleUtils.countriesByLanguage("");
        Assert.assertEquals("Incorrect countries by language list for empty language code", expectedList, actualList);
    }
}