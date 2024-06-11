import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale() {
 Locale expectedLocale = new Locale("en", "GB");
 Locale actualLocale = LocaleUtils.toLocale("en_GB");
 assertEquals(expectedLocale, actualLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleWithInvalidFormat() {
 LocaleUtils.toLocale("en_GB_invalid");
 }

 @Test
 public void testLocaleLookupList() {
 Locale baseLocale = new Locale("fr", "CA", "xxx");
 List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale);

 assertEquals(3, localeList.size());
 assertTrue(localeList.contains(new Locale("fr", "CA", "xxx")));
 assertTrue(localeList.contains(new Locale("fr", "CA")));
 assertTrue(localeList.contains(new Locale("fr")));
 }

 @Test
 public void testLocaleLookupListWithDefaultLocale() {
 Locale baseLocale = new Locale("fr", "CA", "xxx");
 Locale defaultLocale = new Locale("en");
 List<Locale> localeList = LocaleUtils.localeLookupList(baseLocale, defaultLocale);

 assertEquals(4, localeList.size());
 assertTrue(localeList.contains(new Locale("fr", "CA", "xxx")));
 assertTrue(localeList.contains(new Locale("fr", "CA")));
 assertTrue(localeList.contains(new Locale("fr")));
 assertTrue(localeList.contains(defaultLocale));
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> availableLocaleList = LocaleUtils.availableLocaleList();
 // Add assertions based on the available locales in your system
 assertTrue(availableLocaleList.contains(new Locale("en", "US")));
 assertTrue(availableLocaleList.contains(new Locale("fr", "FR")));
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry() {
 List<Locale> languagesByCountry = LocaleUtils.languagesByCountry("CA");
 // Add assertions based on the languages available for CA in your system
 assertTrue(languagesByCountry.contains(new Locale("en", "CA")));
 assertTrue(languagesByCountry.contains(new Locale("fr", "CA")));
 }

 @Test
 public void testCountriesByLanguage() {
 List<Locale> countriesByLanguage = LocaleUtils.countriesByLanguage("en");
 // Add assertions based on the countries available for EN in your system
 assertTrue(countriesByLanguage.contains(new Locale("US", "")));
 assertTrue(countriesByLanguage.contains(new Locale("GB", "")));
 }
}