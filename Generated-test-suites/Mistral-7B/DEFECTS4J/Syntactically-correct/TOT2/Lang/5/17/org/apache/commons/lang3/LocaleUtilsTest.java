import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import org.junit.Assert;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Basic test
        Locale expected = new Locale("en", "US");
        Locale actual = LocaleUtils.toLocale("en_US");
        Assert.assertEquals(expected, actual);

        // Edge case: empty string
        Assert.assertNull(LocaleUtils.toLocale(""));

        // Edge case: no separator
        Assert.assertNull(LocaleUtils.toLocale("enUS"));

        // Error case: invalid format
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_GB_xx"));

        // Edge case: invalid language code
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("xx_US"));

        // Edge case: invalid country code
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale("en_XX"));
    }

    @Test
    public void testLocaleLookupList() {
        // Basic test
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA", "xxx"),
                new Locale("fr", "CA"),
                new Locale("fr")
        );
        List<Locale> actual = LocaleUtils.localeLookupList(locale);
        Assert.assertEquals(expected, actual);

        // Edge case: empty list
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.localeLookupList(null));

        // Error case: invalid locale
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.localeLookupList(null));
    }

    @Test
    public void testAvailableLocaleList() {
        // Basic test
        List<Locale> expected = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> actual = LocaleUtils.availableLocaleList();
        Assert.assertEquals(expected, actual);

        // Edge case: empty list
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.availableLocaleList());
    }

    @Test
    public void testIsAvailableLocale() {
        // Basic test
        Locale locale = new Locale("en", "US");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        // Edge case: not available locale
        Locale locale2 = new Locale("en", "XX");
        assertFalse(LocaleUtils.isAvailableLocale(locale2));

        // Error case: null locale
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    // Add other test methods for other methods in the LocaleUtils class

    @Test
    public void testLanguagesByCountry() {
        // Basic test
        String countryCode = "CA";
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("en", "CA")
        );
        List<Locale> actual = LocaleUtils.languagesByCountry(countryCode);
        Assert.assertEquals(expected, actual);

        // Edge case: null country code
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));

        // Error case: invalid country code
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.languagesByCountry("XX"));
    }

    @Test
    public void testCountriesByLanguage() {
        // Basic test
        String languageCode = "fr";
        List<Locale> expected = Arrays.asList(
                new Locale("fr", "CA"),
                new Locale("fr", "FR")
        );
        List<Locale> actual = LocaleUtils.countriesByLanguage(languageCode);
        Assert.assertEquals(expected, actual);

        // Edge case: null language code
        Assert.assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));

        // Error case: invalid language code
        Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.countriesByLanguage("XX"));
    }
}