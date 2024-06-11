import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.List;
import java.util.Locale;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 String[] testCases = new String[]{"en", "en_GB", "en_GB_XXX", "fr_CA", "fr_CA_XXX"};
 for (String testCase : testCases) {
 Locale expectedLocale = new Locale(testCase.substring(0, 2), testCase.substring(3, testCase.length()));
 Locale actualLocale = LocaleUtils.toLocale(testCase);
 assertEquals(expectedLocale.getDisplayName(), actualLocale.getDisplayName());
 assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
 assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
 }
 }

 @Test
 public void testLocaleLookupList() {
 Locale baseLocale = new Locale("en", "US", "en_US");
 Locale variantLocale = new Locale("en", "US", "en_US_Medical");
 Locale defaultLocale = new Locale("fr", "CA");
 List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale);
 List<Locale> expectedLocaleList = Arrays.asList(baseLocale, variantLocale, new Locale("en", "US"), defaultLocale);
 assertEquals(expectedLocaleList, localeList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocales = LocaleUtils.availableLocaleList();
 int numAvailableLocales = availableLocales.size();
 assertTrue(numAvailableLocales > 0);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale enUS = new Locale("en", "US");
 Locale frCA = new Locale("fr", "CA");
 Locale invalidLocale = new Locale("xx", "yy");
 assertTrue(LocaleUtils.isAvailableLocale(enUS));
 assertTrue(LocaleUtils.isAvailableLocale(frCA));
 assertFalse(LocaleUtils.isAvailableLocale(invalidLocale));
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "CA";
 List<Locale> expectedLanguageList = Arrays.asList(new Locale("fr", countryCode), new Locale("en", countryCode), new Locale("en", ""));
 List<Locale> actualLanguageList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedLanguageList, actualLanguageList);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expectedCountryList = Arrays.asList(new Locale(languageCode, "CA"), new Locale(languageCode, "US"), new Locale(languageCode, ""));
 List<Locale> actualCountryList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedCountryList, actualCountryList);
 }
}