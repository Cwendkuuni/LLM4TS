import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test case for toLocale() method
 @Test
 public void testToLocale() {
 String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr_FR", "fr_FR_CA"};
 for (String testCase : testCases) {
 Locale expectedLocale = new Locale(testCase.substring(0, 2), testCase.substring(3, 5));
 Locale actualLocale = LocaleUtils.toLocale(testCase);
 assertEquals(expectedLocale, actualLocale);
 }
 }

 // Test case for localeLookupList() method (normal case)
 @Test
 public void testLocaleLookupListNormalCase() {
 Locale baseLocale = new Locale("en", "US");
 List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale);
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("en"));
 assertEquals(expectedLocaleList, localeList);
 }

 // Test case for localeLookupList() method (with defaultLocale)
 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale baseLocale = new Locale("en", "US");
 Locale defaultLocale = new Locale("fr", "FR");
 List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("en"), new Locale("fr", "FR"));
 assertEquals(expectedLocaleList, localeList);
 }

 // Test case for isAvailableLocale() method (valid locale)
 @Test
 public void testIsAvailableLocaleValidLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 // Test case for isAvailableLocale() method (invalid locale)
 @Test(expected = IllegalArgumentException.class)
 public void testIsAvailableLocaleInvalidLocale() {
 Locale locale = null; // or new Locale("xx", "xx")
 LocaleUtils.isAvailableLocale(locale);
 }

 // Test case for languagesByCountry() method (valid country)
 @Test
 public void testLanguagesByCountryValidCountry() {
 String countryCode = "US";
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "AU"));
 List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 // Test case for languagesByCountry() method (invalid country)
 @Test
 public void testLanguagesByCountryInvalidCountry() {
 String countryCode = "xx";
 List<Locale> actualLocaleList = LocaleUtils.languagesByCountry(countryCode);
 assertTrue(actualLocaleList.isEmpty());
 }

 // Test case for countriesByLanguage() method (valid language)
 @Test
 public void testCountriesByLanguageValidLanguage() {
 String languageCode = "en";
 List<Locale> expectedLocaleList = Arrays.asList(new Locale("US", "en"), new Locale("CA", "en"), new Locale("GB", "en"), new Locate("IE", "en"), new Locale("AU", "en"));
 List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedLocaleList, actualLocaleList);
 }

 // Test case for countriesByLanguage() method (invalid language)
 @Test
 public void testCountriesByLanguageInvalidLanguage() {
 String languageCode = "xx";
 List<Locale> actualLocaleList = LocaleUtils.countriesByLanguage(languageCode);
 assertTrue(actualLocaleList.isEmpty());
 }
}