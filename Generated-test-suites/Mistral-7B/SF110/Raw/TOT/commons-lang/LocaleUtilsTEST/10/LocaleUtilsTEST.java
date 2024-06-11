import org.apache.commons.lang3.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_ValidFormat_TwoCharLanguage() {
 String localeString = "en";
 Locale expectedLocale = new Locale("en");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);
 }

 @Test
 public void testToLocale_ValidFormat_ThreeCharLanguage() {
 String localeString = "en-US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);
 }

 @Test
 public void testToLocale_ValidFormat_ThreeCharLanguageWithCountryEmpty() {
 String localeString = "en-";
 Locale expectedLocale = new Locale("en");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);
 }

 @Test
 public void testToLocale_ValidFormat_ThreeCharLanguageWithCountryUpperCase() {
 String localeString = "en-GB";
 Locale expectedLocale = new Locale("en", "GB");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 assertEquals(expectedLocale, actualLocale);
 }

 @Test
 public void testToLocale_InvalidFormat_EmptyString() {
 String localeString = "";
 assertNull(LocaleUtils.toLocale(localeString));
 }

 @Test
 public void testToLocale_InvalidFormat_NullString() {
 assertNull(LocaleUtils.toLocale(null));
 }

 @Test
 public void testToLocale_InvalidFormat_SingleCharString() {
 String localeString = "e";
 assertNull(LocaleUtils.toLocale(localeString));
 }

 @Test
 public void testToLocale_InvalidFormat_StringWithHash() {
 String localeString = "en#US";
 assertNull(LocaleUtils.toLocale(localeString));
 }

 @Test
 public void testLocaleLookupList_ValidLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en"), new Locale(""), new Locale(""));
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
 List<Locale> actualList = LocaleUtils.localeLookupList(null);
 assertTrue(actualList.isEmpty());
 }

 @Test
 public void testLocaleLookupList_EmptyLocale() {
 Locale locale = new Locale("");
 List<Locale> expectedList = Arrays.asList(new Locale(""), new Locale(""));
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_LocaleWithVariant() {
 Locale locale = new Locale("en", "US", "en_US");
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", ""), new Locale("en", ""), new Locale("en", ""));
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"));
 List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expectedLocales = Arrays.asList(new Locale("en-US"), new Locale("en-GB"), new Locale("en-CA"), new Locale("en-IE"));
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testIsAvailableLocale_AvailableLocale() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
 Locale locale = new Locale("en", "US", "en_US_Invalid");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
 }
}