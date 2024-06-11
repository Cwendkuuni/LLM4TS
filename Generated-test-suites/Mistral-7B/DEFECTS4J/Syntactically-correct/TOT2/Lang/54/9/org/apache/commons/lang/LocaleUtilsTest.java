import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 // Test case for toLocale method
 @Test
 public void testToLocale() {
 String[] testCases = new String[]{"en", "en_GB", "en_GB_US", "fr_FR", "fr_CA", "es_ES"};
 for (String localeString : testCases) {
 Locale expectedLocale = new Locale(localeString.substring(0, 2), localeString.substring(3));
 if (localeString.length() > 5) {
 expectedLocale = new Locale(localeString.substring(0, 2), localeString.substring(3, 5), localeString.substring(6));
 }
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale.getDisplayName(), actualLocale.getDisplayName());
 assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
 assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
 }
 }

 // Test case for localeLookupList method with valid locale
 @Test
 public void testLocaleLookupListWithValidLocale() {
 Locale locale = new Locale("fr", "CA");
 List expectedLocales = LocaleUtils.localeLookupList(locale);
 List actualLocales = LocaleUtils.localeLookupList(locale, null);
 assertEquals(expectedLocales.size(), actualLocales.size());
 for (int i = 0; i < expectedLocales.size(); i++) {
 Locale expectedLocale = (Locale) expectedLocales.get(i);
 Locale actualLocale = (Locale) actualLocales.get(i);
 assertEquals(expectedLocale.getDisplayName(), actualLocale.getDisplayName());
 assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
 assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
 }
 }

 // Test case for localeLookupList method with null locale
 @Test
 public void testLocaleLookupListWithNullLocale() {
 List expectedLocales = Collections.emptyList();
 List actualLocales = LocaleUtils.localeLookupList(null);
 assertEquals(expectedLocales, actualLocales);
 }

 // Test case for isAvailableLocale method with valid locale
 @Test
 public void testIsAvailableLocaleWithValidLocale() {
 Locale locale = new Locale("fr", "CA");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 // Test case for isAvailableLocale method with invalid locale
 @Test(expected = IllegalArgumentException.class)
 public void testIsAvailableLocaleWithInvalidLocale() {
 Locale locale = new Locale("fr", "CA", "xxx");
 LocaleUtils.isAvailableLocale(locale);
 }

 // Test case for languagesByCountry method with valid country code
 @Test
 public void testLanguagesByCountryWithValidCountryCode() {
 List expectedLocales = LocaleUtils.languagesByCountry("CA");
 List actualLocales = LocaleUtils.languagesByCountry("CA");
 assertEquals(expectedLocales.size(), actualLocales.size());
 for (int i = 0; i < expectedLocales.size(); i++) {
 Locale expectedLocale = (Locale) expectedLocales.get(i);
 Locale actualLocale = (Locale) actualLocales.get(i);
 assertEquals(expectedLocale.getDisplayName(), actualLocale.getDisplayName());
 assertEquals(expectedLocale.getLanguage(), actualLocale.getLanguage());
 assertEquals(expectedLocale.getCountry(), actualLocale.getCountry());
 }
 }

 // Test case for languagesByCountry method with null country code
 @Test
 public void testLanguagesByCountryWithNullCountryCode() {
 List expectedLocales = Collections.emptyList();
 List actualLocales = LocaleUtils.languagesByCountry(null);
 assertEquals(expectedLocales, actualLocales);
 }

}