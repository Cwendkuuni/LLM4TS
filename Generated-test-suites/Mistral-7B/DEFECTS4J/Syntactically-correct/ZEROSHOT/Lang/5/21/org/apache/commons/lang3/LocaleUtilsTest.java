import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        String[] validInputs = new String[]{"en", "en_GB", "en_GB_US", "es_ES", "fr_FR"};
        Locale[] expectedLocales = new Locale[] {new Locale("en"), new Locale("en", "GB"), new Locale("en", "GB", "US"), new Locale("es", "ES"), new Locale("fr", "FR")};
        for (int i = 0; i < validInputs.length; i++) {
            Locale actualLocale = LocaleUtils.toLocale(validInputs[i]);
            assertEquals(expectedLocales[i].getLanguage(), actualLocale.getLanguage());
            assertEquals(expectedLocales[i].getCountry(), actualLocale.getCountry());
            assertEquals(expectedLocales[i].getVariant(), actualLocale.getVariant());
        }

        // Test null input
        assertNull(LocaleUtils.toLocale(null));

        // Test invalid inputs
        String[] invalidInputs = new String[]{"en_GB_US_invalid", "en_GB_", "en_GB__", "en_GB_US__", "en_GB_US_invalid"};
        for (String invalidInput : invalidInputs) {
            try {
                LocaleUtils.toLocale(invalidInput);
                fail("Expected IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        Locale emptyLocale = null;
        List<Locale> localeList = LocaleUtils.localeLookupList(emptyLocale);
        assertTrue(localeList.isEmpty());

        // Test valid inputs
        String[] validInputs = new String[]{"fr", "fr_CA", "fr_CA_xxx", "en", "en_GB", "en_GB_US", "en_GB_US_IN"};
        Locale[] expectedFirstLocale = new Locale[] {new Locale("fr"), new Locale("fr", "CA"), new Locale("fr", "CA", "xxx"), new Locale("en"), new Locale("en", "GB"), new Locale("en", "GB", "US"), new Locale("en", "GB", "US", "IN")};
        Locale[] expectedOtherLocales = new Locale[] {new Locale("fr"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en"), new Locale("en"), new Locale("en"), new Locale("en")};
        for (int i = 0; i < validInputs.length; i++) {
            Locale firstLocale = LocaleUtils.toLocale(validInputs[i]);
            List<Locale> localeList1 = LocaleUtils.localeLookupList(firstLocale);
            assertEquals(Arrays.asList(expectedFirstLocale[i], expectedOtherLocales[i].withVariant("")), localeList1);
            List<Locale> localeList2 = LocaleUtils.localeLookupList(Locale.getDefault());
            assertTrue(localeList1.containsAll(localeList2));
        }
    }

    @Test
    public void testAvailableLocaleList() {
        // Test empty list
        List<Locale> localeList = LocaleUtils.availableLocaleList();
        assertTrue(localeList.isEmpty());

        // Test valid list
        List<Locale> expectedLocales = Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.KOREAN, Locale.JAPANESE, Locale.TAIWAN, Locale.SIMPLIFIED_CHINESE_HONG_KONG, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN);
        List<Locale> localeList1 = LocaleUtils.availableLocaleList();
        assertEquals(expectedLocales, localeList1);

        // Test null input
        assertNull(LocaleUtils.availableLocaleList(null));
    }

    @Test
    public void testAvailableLocaleSet() {
        // Test empty set
        Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
        assertTrue(localeSet.isEmpty());

        // Test valid set
        Set<Locale> expectedLocales = new HashSet<>(Arrays.asList(Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, Locale.KOREAN, Locale.JAPANESE, Locale.TAIWAN, Locale.SIMPLIFIED_CHINESE_HONG_KONG, Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN));
        Set<Locale> localeSet1 = LocaleUtils.availableLocaleSet();
        assertEquals(expectedLocales, localeSet1);

        // Test null input
        assertNull(LocaleUtils.availableLocaleSet(null));
    }

    @Test
    public void testLanguagesByCountry() {
        // Test empty list
        String emptyCountryCode = null;
        List<Locale> localeList = LocaleUtils.languagesByCountry(emptyCountryCode);
        assertTrue(localeList.isEmpty());

        // Test valid country code
        List<Locale> expectedLocales = Arrays.asList(new Locale("es", "ES"), new Locale("fr", "FR"), new Locale("it", "IT"));
        List<Locale> localeList1 = LocaleUtils.languagesByCountry("ES");
        assertEquals(expectedLocales, localeList1);

        // Test null input
        assertNull(LocaleUtils.languagesByCountry(null));
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty list
        String emptyLanguageCode = null;
        List<Locale> localeList = LocaleUtils.countriesByLanguage(emptyLanguageCode);
        assertTrue(localeList.isEmpty());

        // Test valid language code
        List<Locale> expectedLocales = Arrays.asList(new Locale("ES", "ES"), new Locale("FR", "FR"), new Locale("IT", "IT"));
        List<Locale> localeList1 = LocaleUtils.countriesByLanguage("ES");
        assertEquals(expectedLocales, localeList1);

        // Test null input
        assertNull(LocaleUtils.countriesByLanguage(null));
    }
}