import org.apache.commons.lang3.Locale;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_NullInput() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocale_EmptyInput() {
        assertInstanceOf(Locale.class, LocaleUtils.toLocale(""));
        assertEquals("", LocaleUtils.toLocale("").getLanguage());
        assertEquals("", LocaleUtils.toLocale("").getCountry());
        assertEquals("", LocaleUtils.toLocale("").getVariant());
    }

    @Test
    public void testToLocale_InvalidFormat_NoLanguage() {
        String[] invalidLocales = new String[]{"#", "_#", "_AB_"};
        for (String invalidLocale : invalidLocales) {
            try {
                LocaleUtils.toLocale(invalidLocale);
                fail("Expected IllegalArgumentException for invalid locale format: " + invalidLocale);
            } catch (IllegalArgumentException e) {
                assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
            }
        }
    }

    @Test
    public void testToLocale_InvalidFormat_NoCountry() {
        String[] invalidLocales = new String[]{"_AB", "_AB_CD", "_AB_CD_"};
        for (String invalidLocale : invalidLocales) {
            try {
                LocaleUtils.toLocale(invalidLocale);
                fail("Expected IllegalArgumentException for invalid locale format: " + invalidLocale);
            } catch (IllegalArgumentException e) {
                assertEquals("Invalid locale format: " + invalidLocale, e.getMessage());
            }
        }
    }

    @Test
    public void testToLocale_ValidFormat() {
        List<String> validLocales = new ArrayList<>();
        validLocales.add("en");
        validLocales.add("en_US");
        validLocales.add("es_ES");
        validLocales.add("fr_FR");
        validLocales.add("de_DE");
        for (String validLocale : validLocales) {
            Locale expectedLocale = new Locale(validLocale);
            Locale actualLocale = LocaleUtils.toLocale(validLocale);
            assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
            assertEquals(expectedLocale.getVariant(), actualLocale.getVariant());
        }
    }

    @Test
    public void testLocaleLookupList_NullInput() {
        List<Locale> localeList = LocaleUtils.localeLookupList(null);
        assertNotNull(localeList);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testLocaleLookupList_EmptyInput() {
        Locale locale = new Locale("");
        List<Locale> localeList = LocaleUtils.localeLookupList(locale);
        assertNotNull(localeList);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testLocaleLookupList_InputWithVariants() {
        Locale inputLocale = new Locale("fr_FR_CH");
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", ""), new Locale("fr", "CH"), inputLocale);
        List<Locale> localeList = LocaleUtils.localeLookupList(inputLocale);
        assertArrayEquals(expectedLocaleList.toArray(new Locale[0]), localeList.toArray(new Locale[0]));
    }

    @Test
    public void testLocaleLookupList_InputWithoutVariants() {
        Locale inputLocale = new Locale("fr_FR");
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", ""), inputLocale);
        List<Locale> localeList = LocaleUtils.localeLookupList(inputLocale);
        assertArrayEquals(expectedLocaleList.toArray(new Locale[0]), localeList.toArray(new Locale[0]));
    }

    @Test
    public void testLocaleLookupList_InputWithDefaultLocale() {
        Locale inputLocale = new Locale("fr_FR");
        Locale defaultLocale = new Locale("en_US");
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", ""), inputLocale, defaultLocale);
        List<Locale> localeList = LocaleUtils.localeLookupList(inputLocale, defaultLocale);
        assertArrayEquals(expectedLocaleList.toArray(new Locale[0]), localeList.toArray(new Locale[0]));
    }

    @Test
    public void testLocaleLookupList_InputWithoutDefaultLocale() {
        Locale inputLocale = new Locale("fr_FR");
        List<Locale> localeList = LocaleUtils.localeLookupList(inputLocale);
        assertArrayEquals(new Locale[]{inputLocale}, localeList.toArray(new Locale[0]));
    }

    @Test
    public void testAvailableLocaleList_EmptyList() {
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testAvailableLocaleList_NonEmptyList() {
        List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertArrayEquals(expectedLocaleList.toArray(new Locale[0]), localeList.toArray(new Locale[0]));
    }

    @Test
    public void testAvailableLocaleSet_EmptySet() {
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertTrue(localeSet.isEmpty());
    }

    @Test
    public void testAvailableLocaleSet_NonEmptySet() {
        Set<Locale> expectedLocaleSet = new HashSet<>(Locale.getAvailableLocales());
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertEquals(expectedLocaleSet, localeSet);
    }

    @Test
    public void testIsAvailableLocale_NullInput() {
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testIsAvailableLocale_InvalidInput() {
        Locale invalidLocale = new Locale("InvalidLocale");
        assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
    }

    @Test
    public void testIsAvailableLocale_AvailableLocale() {
        Locale availableLocale = Locale.SIMPLIFIED_CHINESE;
        assertTrue(LocaleUtils.isAvailableLocale(availableLocale));
    }

    @Test
    public void testLanguagesByCountry_NullInput() {
        List<Locale> localeList = LocaleUtils.languagesByCountry(null);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_EmptyCountryCode() {
        List<Locale> localeList = LocaleUtils.languagesByCountry("");
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testLanguagesByCountry_AvailableLocales() {
        String countryCode = "US";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("es", "US"), new Locale("es", "MX"), new Locale("fr", "CA"));
        List<Locale> localeList = LocaleUtils.languagesByCountry(countryCode);
        assertArrayEquals(expectedLocaleList.toArray(new Locale[0]), localeList.toArray(new Locale[0]));
    }

    @Test
    public void testCountriesByLanguage_NullInput() {
        List<Locale> localeList = LocaleUtils.countriesByLanguage(null);
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_EmptyLanguageCode() {
        List<Locale> localeList = LocaleUtils.countriesByLanguage("");
        assertTrue(localeList.isEmpty());
    }

    @Test
    public void testCountriesByLanguage_AvailableLocales() {
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("US"), new Locale("CA"), new Locale("GB"), new Locale("AU"), new Locale("ZA"));
        List<Locale> localeList = LocaleUtils.countriesByLanguage(languageCode);
        assertArrayEquals(expectedLocaleList.toArray(new Locale[0]), localeList.toArray(new Locale[0]));
    }
}