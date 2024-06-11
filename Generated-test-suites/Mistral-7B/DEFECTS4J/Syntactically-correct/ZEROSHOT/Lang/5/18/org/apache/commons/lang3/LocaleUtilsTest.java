import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test empty string
        assertNull(LocaleUtils.toLocale(""));

        // Test valid locales
        assertEquals(new Locale("en"), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Test invalid locales
        assertNull(LocaleUtils.toLocale("en_GB_xx")); // missing country code separator
        assertNull(LocaleUtils.toLocale("en_GB_xxx_")); // leading underscore
        assertNull(LocaleUtils.toLocale("en_GB_xxxZ")); // invalid country code character
        assertNull(LocaleUtils.toLocale("en_GB_xxx_Z")); // trailing underscore and invalid country code character
        assertNull(LocaleUtils.toLocale("en_GB_xxx_Za")); // invalid country code character
        assertNull(LocaleUtils.toLocale("en_GB_xxx_Za_")); // trailing underscore and invalid country code character
        assertNull(LocaleUtils.toLocale("en_GB_xxx_Za_Bb")); // invalid country code character
        assertNull(LocaleUtils.toLocale("en_GB_xxx_Za_Bb_")); // trailing underscore and invalid country code character

        // Test invalid language codes
        assertNull(LocaleUtils.toLocale("en_GB_xxx_aa")); // invalid language code
        assertNull(LocaleUtils.toLocale("en_GB_xxx_aa_")); // trailing underscore and invalid language code

        // Test invalid separators
        assertNull(LocaleUtils.toLocale("en_GB_xxx-xx")); // invalid separator
        assertNull(LocaleUtils.toLocale("en_GB_xxx_xx")); // invalid separator
        assertNull(LocaleUtils.toLocale("en_GB_xxx_xx_")); // trailing separator and invalid country code character
        assertNull(LocaleUtils.toLocale("en_GB_xxx_xx_Z")); // trailing separator and invalid country code character
        assertNull(LocaleUtils.toLocale("en_GB_xxx_xx_Za")); // trailing separator and invalid country code character
        assertNull(LocaleUtils.toLocale("en_GB_xxx_xx_Za_Bb")); // trailing separator and invalid country code character
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr"));
        assertEquals(expectedList, localeList);

        locale = new Locale("fr");
        localeList = LocaleUtils.localeLookupList(locale);
        expectedList = Arrays.asList(locale, new Locale("fr", ""), new Locale("en"));
        assertEquals(expectedList, localeList);

        locale = null;
        localeList = LocaleUtils.localeLookupList(locale);
        assertEquals(Collections.emptyList(), localeList);
    }

    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr", "CA");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("fr");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en");
        assertTrue(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("zz", "ZZ");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "ZZ");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("en", "");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = new Locale("", "");
        assertFalse(LocaleUtils.isAvailableLocale(locale));

        locale = null;
        assertFalse(LocaleUtils.isAvailableLocale(locale));
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "CA";
        List<Locale> expectedList = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode), new Locale("es", countryCode));
        List<Locale> langs = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, langs);

        countryCode = "US";
        expectedList = Arrays.asList(new Locale("en", countryCode), new Locale("es", countryCode));
        langs = LocaleUtils.languagesByCountry(countryCode);
        assertEquals(expectedList, langs);

        countryCode = null;
        assertNull(LocaleUtils.languagesByCountry(countryCode));
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "fr";
        List<Locale> expectedList = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "CH"));
        List<Locale> countries = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, countries);

        languageCode = "en";
        expectedList = Arrays.asList(new Locale(languageCode, "US"), new Locale(languageCode, "GB"));
        countries = LocaleUtils.countriesByLanguage(languageCode);
        assertEquals(expectedList, countries);

        languageCode = null;
        assertNull(LocaleUtils.countriesByLanguage(languageCode));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertNotNull(localeList);
        assertEquals(Locale.getAvailableLocales().length, localeList.size());

        for (Locale locale : localeList) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertNotNull(localeSet);
        assertEquals(Locale.getAvailableLocales().length, localeSet.size());

        for (Locale locale : localeSet) {
            assertTrue(LocaleUtils.isAvailableLocale(locale));
        }
    }
}