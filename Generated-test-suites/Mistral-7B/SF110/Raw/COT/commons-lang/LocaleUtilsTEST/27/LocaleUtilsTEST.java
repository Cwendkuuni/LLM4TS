import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        Assert.assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        Assert.assertEquals(new Locale("", ""), LocaleUtils.toLocale(""));
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        String[] testCases = {"", "_abc", "_abc_", "_abc_def", "_abc_def_", "_123", "_123_", "_123_def", "_123_def_"};
        for (String input : testCases) {
            try {
                LocaleUtils.toLocale(input);
                Assert.fail("Expected IllegalArgumentException for input: " + input);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testToLocale_InvalidFormat_InvalidLanguageCode() {
        String[] testCases = {"_abc", "_abc_", "_abc_def_", "_123", "_123_", "_123_def", "_123_def_", "123"};
        for (String input : testCases) {
            try {
                LocaleUtils.toLocale(input);
                Assert.fail("Expected IllegalArgumentException for input: " + input);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testToLocale_ValidFormat_OneSegment() {
        String[] testCases = {"en", "fr", "es", "de", "zh_CN", "ja"};
        for (String input : testCases) {
            Locale expected = new Locale(input);
            Locale actual = LocaleUtils.toLocale(input);
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void testToLocale_ValidFormat_TwoSegments() {
        String[] testCases = {"en_US", "fr_FR", "es_ES", "de_DE", "zh_CN_Hans", "ja_JP"};
        for (String input : testCases) {
            Locale expected = new Locale(input.split("_")[0], input.split("_")[1]);
            Locale actual = LocaleUtils.toLocale(input);
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void testToLocale_ValidFormat_ThreeSegments() {
        String[] testCases = {"en_US_POSIX", "fr_FR_POSIX", "es_ES_POSIX", "de_DE_POSIX", "zh_CN_Hans_POSIX", "ja_JP_POSIX"};
        for (String input : testCases) {
            Locale expected = new Locale("", input.substring(1, input.length() - 3));
            expected.setVariants(new String[]{input.substring(input.length() - 3)});
            Locale actual = LocaleUtils.toLocale(input);
            Assert.assertEquals(expected, actual);
        }
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        List<Locale> result = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testLocaleLookupList_EmptyLocale() {
        Locale locale = new Locale("");
        List<Locale> result = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(Collections.singletonList(locale), result);
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale() {
        Locale locale = new Locale("en_US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, null);
        List<Locale> expected = new ArrayList<Locale>(3);
        expected.add(locale);
        expected.add(new Locale("en", ""));
        expected.add(new Locale("", ""));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_EmptyDefaultLocale() {
        Locale locale = new Locale("en_US");
        List<Locale> result = LocaleUtils.localeLookupList(locale, new Locale(""));
        List<Locale> expected = new ArrayList<Locale>(3);
        expected.add(locale);
        expected.add(new Locale("en", ""));
        expected.add(new Locale("", ""));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLocaleLookupList_LocaleWithVariant() {
        Locale locale = new Locale("fr_FR_frFR");
        List<Locale> result = LocaleUtils.localeLookupList(locale, new Locale(""));
        List<Locale> expected = new ArrayList<Locale>(4);
        expected.add(locale);
        expected.add(new Locale("fr", ""));
        expected.add(new Locale("fr", "FR"));
        expected.add(new Locale("", ""));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = new HashSet<Locale>(Locale.getAvailableLocales());
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsAvailableLocale_NullLocale() {
        Assert.assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_NotAvailableLocale() {
        Locale locale = new Locale("invalid-locale");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale locale = new Locale("en_US");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Arrays.asList(new Locale("en", countryCode), new Locale("en", ""));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Arrays.asList(new Locale(languageCode, "US"), new Locale(languageCode, "GB"), new Locale(languageCode, "CA"), new Locale(languageCode, "AU"));
        Assert.assertEquals(expected, result);
    }
}