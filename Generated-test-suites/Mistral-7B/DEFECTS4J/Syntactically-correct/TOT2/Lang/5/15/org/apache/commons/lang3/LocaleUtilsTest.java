import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid locale strings
        Assert.assertEquals("en_US", LocaleUtils.toLocale("en_US").toString());
        Assert.assertEquals("fr_CA", LocaleUtils.toLocale("fr_CA").toString());
        Assert.assertEquals("de_DE", LocaleUtils.toLocale("de_DE").toString());

        // Test invalid locale strings
        Assert.assertNull(LocaleUtils.toLocale(null));
        Assert.assertNull(LocaleUtils.toLocale(""));
        Assert.assertNull(LocaleUtils.toLocale("en"));
        Assert.assertNull(LocaleUtils.toLocale("en_"));
        Assert.assertNull(LocaleUtils.toLocale("en_US_"));
        Assert.assertNull(LocaleUtils.toLocale("en_US_XXX"));
        Assert.assertNull(LocaleUtils.toLocale("en_US_XXX_"));

        // Test empty country code
        Assert.assertEquals("en", LocaleUtils.toLocale("en_").toString());
        Assert.assertEquals("en", LocaleUtils.toLocale("en").toString());

        // Test empty language code
        Assert.assertEquals("", LocaleUtils.toLocale("_XX").toString());
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);

        Assert.assertEquals(4, lookupList.size());
        Assert.assertEquals(locale, lookupList.get(0));
        Assert.assertEquals(new Locale("fr", "CA"), lookupList.get(1));
        Assert.assertEquals(new Locale("fr"), lookupList.get(2));
        Assert.assertEquals(Locale.getDefault(), lookupList.get(3));

        lookupList = LocaleUtils.localeLookupList(null);
        Assert.assertEquals(0, lookupList.size());

        lookupList = LocaleUtils.localeLookupList(Locale.getDefault());
        Assert.assertEquals(1, lookupList.size());
        Assert.assertEquals(Locale.getDefault(), lookupList.get(0));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        Assert.assertFalse(availableLocales.isEmpty());

        // Test specific locales
        Assert.assertTrue(availableLocales.contains(Locale.ENGLISH));
        Assert.assertTrue(availableLocales.contains(Locale.FRENCH));
        Assert.assertTrue(availableLocales.contains(Locale.GERMAN));

        // Test country specific locales
        Assert.assertTrue(availableLocales.contains(new Locale("en", "US")));
        Assert.assertTrue(availableLocales.contains(new Locale("fr", "CA")));
        Assert.assertTrue(availableLocales.contains(new Locale("de", "DE")));

        // Test variant locales
        Assert.assertTrue(availableLocales.contains(new Locale("en", "US", "POSIX")));
        Assert.assertTrue(availableLocales.contains(new Locale("fr", "CA", "POSIX")));
        Assert.assertTrue(availableLocales.contains(new Locale("de", "DE", "POSIX")));

        // Test empty locales
        Assert.assertTrue(availableLocales.contains(Locale.ROOT));
        Assert.assertTrue(availableLocales.contains(Locale.EMPTY));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
        Assert.assertFalse(languagesByCountry.isEmpty());

        // Test specific languages
        Assert.assertTrue(languagesByCountry.contains(new Locale("en", "CA")));
        Assert.assertTrue(languagesByCountry.contains(new Locale("fr", "CA")));

        // Test empty country
        List<Locale> languagesByEmptyCountry = LocaleUtils.languagesByCountry(null);
        Assert.assertEquals(0, languagesByEmptyCountry.size());

        // Test non-existent country
        List<Locale> languagesByNonExistentCountry = LocaleUtils.languagesByCountry("XY");
        Assert.assertEquals(0, languagesByNonExistentCountry.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("en_GB_XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidLanguage() {
        LocaleUtils.toLocale("xy_GB");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidCountry() {
        LocaleUtils.toLocale("en_XY");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidSeparator() {
        LocaleUtils.toLocale("en_GB:XXX");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithEmptyLanguage() {
        LocaleUtils.toLocale("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithEmptyCountry() {
        LocaleUtils.toLocale("_XX");
    }
}