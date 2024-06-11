import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        String[] testCases = new String[]{"en", "en_GB", "en_GB_xxx", "fr", "fr_CA", "fr_CA_xxx"};
        Locale[] expectedLocales = new Locale[]{new Locale("en", ""), new Locale("en", "GB"), new Locale("en", "GB", "xxx"), new Locale("fr", ""), new Locale("fr", "CA"), new Locale("fr", "CA", "xxx")};

        for (int i = 0; i < testCases.length; i++) {
            Locale actualLocale = LocaleUtils.toLocale(testCases[i]);
            assertEquals("Test case #" + i + " failed.", expectedLocales[i], actualLocale);
        }
    }

    @Test
    public void testLocaleLookupList() {
        Locale baseLocale = new Locale("en", "US", "en_US");
        List<Locale> expectedLocaleList = LocaleUtils.localeLookupList(baseLocale, new Locale("fr", "FR"));

        List<Locale> actualLocaleList = LocaleUtils.localeLookupList(baseLocale);
        List<Locale> actualLocaleListWithDefault = LocaleUtils.localeLookupList(baseLocale, new Locale("fr", "FR"));

        assertEquals("Test case failed: size.", expectedLocaleList.size(), actualLocaleList.size());
        assertEquals("Test case failed.", expectedLocaleList.get(0), actualLocaleList.get(0));
        assertEquals("Test case failed.", expectedLocaleList.get(1), actualLocaleList.get(1));
        assertEquals("Test case failed.", expectedLocaleList.get(2), actualLocaleList.get(2));
        assertEquals("Test case failed.", expectedLocaleList.get(3), actualLocaleListWithDefault.get(3));

        // Ensure that the base locale is not present in the list and that the default locale is present
        assertFalse("Test case failed: base locale present in the list.", actualLocaleList.contains(baseLocale));
        assertTrue("Test case failed: default locale not present in the list.", actualLocaleListWithDefault.contains(new Locale("fr", "FR")));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> actualLocaleList = LocaleUtils.availableLocaleList();
        List<Locale> expectedLocaleList = Arrays.asList(Locale.getAvailableLocales());

        assertEquals("Test case failed: size.", expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals("Test case failed: locale #" + i + ".", expectedLocaleList.get(i), actualLocaleList.get(i));
        }
    }

    @Test
    public void testIsAvailableLocale() {
        List<Locale> locales = new ArrayList<>();
        locales.add(new Locale("en", "US"));
        locales.add(new Locale("fr", "FR"));
        locales.add(new Locale("es", "ES"));

        for (Locale locale : locales) {
            boolean result = LocaleUtils.isAvailableLocale(locale);
            if (locale.equals(new Locale("fr", "FR"))) {
                assertTrue("Test case failed: locale fr_FR should be available.", result);
            } else {
                assertTrue("Test case failed: locale " + locale + " should not be available.", !result);
            }
        }
    }

    @Test
    public void testLanguagesByCountry() {
        String countryCode = "US";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("es", "PR"));

        List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);

        assertEquals("Test case failed: size.", expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals("Test case failed: locale #" + i + ".", expectedLocaleList.get(i), actualLocaleList.get(i));
        }
    }

    @Test
    public void testCountriesByLanguage() {
        String languageCode = "en";
        List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"));

        List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);

        assertEquals("Test case failed: size.", expectedLocaleList.size(), actualLocaleList.size());
        for (int i = 0; i < expectedLocaleList.size(); i++) {
            assertEquals("Test case failed: locale #" + i + ".", expectedLocaleList.get(i), actualLocaleList.get(i));
        }
    }
}