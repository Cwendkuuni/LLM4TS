import org.apache.commons.lang.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Locale;
import java.util.Arrays;

public class LocaleUtilsTest {

    // Test toLocale method
    @Test
    public void testToLocale() {
        // Test valid input
        String input1 = "en";
        Locale expected1 = new Locale("en", "");
        Locale result1 = LocaleUtils.toLocale(input1);
        Assert.assertEquals(expected1, result1);

        String input2 = "en_GB";
        Locale expected2 = new Locale("en", "GB");
        Locale result2 = LocaleUtils.toLocale(input2);
        Assert.assertEquals(expected2, result2);

        String input3 = "en_GB_XXX";
        Locale expected3 = new Locale("en", "GB", "XXX");
        Locale result3 = LocaleUtils.toLocale(input3);
        Assert.assertEquals(expected3, result3);

        // Test invalid input
        String input4 = "";
        Assert.assertNull(LocaleUtils.toLocale(input4));

        String input5 = "fr_CA_Invalid";
        Assert.assertNull(LocaleUtils.toLocale(input5));

        String input6 = "en_Invalid";
        Assert.assertNull(LocaleUtils.toLocale(input6));

        String input7 = "en_GB_Invalid_";
        Assert.assertNull(LocaleUtils.toLocale(input7));
    }

    // Test localeLookupList method
    @Test
    public void testLocaleLookupList() {
        // Test valid input
        Locale locale1 = new Locale("fr", "CA", "xxx");
        List<Locale> expected1 = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> result1 = LocaleUtils.localeLookupList(locale1, new Locale("en"));
        Assert.assertEquals(expected1, result1);

        Locale locale2 = new Locale("fr");
        List<Locale> expected2 = Arrays.asList(
                new Locale("fr"),
                new Locale("en")
        );
        List<Locale> result2 = LocaleUtils.localeLookupList(locale2, new Locale("en"));
        Assert.assertEquals(expected2, result2);

        // Test null input
        Assert.assertTrue(LocaleUtils.localeLookupList(null, new Locale("en")).isEmpty());
    }

    // Test availableLocaleList method
    @Test
    public void testAvailableLocaleList() {
        List<Locale> result = LocaleUtils.availableLocaleList();
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        Assert.assertEquals(expected, result);
    }

    // Test availableLocaleSet method
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> result = LocaleUtils.availableLocaleSet();
        Set<Locale> expected = Collections.unmodifiableSet(new HashSet<>(Locale.getAvailableLocales()));
        Assert.assertEquals(expected, result);
    }

    // Test isAvailableLocale method
    @Test
    public void testIsAvailableLocale() {
        Locale locale1 = new Locale("fr", "CA");
        Assert.assertTrue(LocaleUtils.isAvailableLocale(locale1));

        Locale locale2 = new Locale("fr", "InvalidCountry");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale2));

        Locale locale3 = new Locale("InvalidLanguage", "FR");
        Assert.assertFalse(LocaleUtils.isAvailableLocale(locale3));
    }

    // Test languagesByCountry method
    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
        List<Locale> expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA")
        );
        Assert.assertEquals(expected, result);

        String countryCode2 = "ZZ";
        List<Locale> result2 = LocaleUtils.languagesByCountry(countryCode2);
        List<Locale> expected2 = Collections.emptyList();
        Assert.assertEquals(expected2, result2);
    }

    // Test countriesByLanguage method
    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
        List<Locale> expected = Arrays.asList(
                new Locale("CA"),
                new Locale("GB"),
                new Locale("US")
        );
        Assert.assertEquals(expected, result);

        String languageCode2 = "ZZ";
        List<Locale> result2 = LocaleUtils.countriesByLanguage(languageCode2);
        List<Locale> expected2 = Collections.emptyList();
        Assert.assertEquals(expected2, result2);
    }
}