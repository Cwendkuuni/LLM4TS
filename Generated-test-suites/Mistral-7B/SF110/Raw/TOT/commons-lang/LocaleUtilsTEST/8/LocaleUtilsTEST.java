import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_ValidFormat() {
 String localeString = "en_US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(localeString);
 Assert.assertEquals(expectedLocale, actualLocale);
 }

 @Test
 public void testToLocale_EmptyString() {
 Locale actualLocale = LocaleUtils.toLocale("");
 Assert.assertNull(actualLocale);
 }

 @Test
 public void testToLocale_NullInput() {
 Assert.assertNull(LocaleUtils.toLocale(null));
 }

 @Test
 public void testToLocale_InvalidFormat_NoBaseLanguage() {
 String invalidLocaleString = "_US";
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testToLocale_InvalidFormat_WrongBaseLanguageLength() {
 String invalidLocaleString = "_AB";
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testToLocale_InvalidFormat_MissingCountry() {
 String invalidLocaleString = "en";
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testToLocale_InvalidFormat_MissingLanguage() {
 String invalidLocaleString = "_US";
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidCountryCodeLength() {
 String invalidLocaleString = "en__US";
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidLanguageCodeLength() {
 String invalidLocaleString = "en__US_";
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidCountryCode() {
 String invalidLocaleString = "en_XX";
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidLanguageCode() {
 String invalidLocaleString = "en_US_XX";
 Assert.assertThrows(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("fr", "CA");
 List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", ""), new Locale("", "CA"), new Locale("", ""));
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);
 Assert.assertEquals(expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_NullInput() {
 Locale locale = null;
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);
 Assert.assertNotNull(actualList);
 Assert.assertTrue(actualList.isEmpty());
 }

 @Test
 public void testLocaleLookupList_EmptyInput() {
 Locale locale = new Locale("");
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);
 Assert.assertEquals(Collections.singletonList(locale), actualList);
 }

 @Test
 public void testLocaleLookupList_EmptyInput_WithDefaultLocale() {
 Locale locale = new Locale("");
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
 Assert.assertEquals(Arrays.asList(locale, defaultLocale), actualList);
 }

 @Test
 public void testLocaleLookupList_NullDefaultInput() {
 Locale locale = new Locale("fr", "CA");
 Locale defaultLocale = null;
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
 Assert.assertEquals(Arrays.asList(locale), actualList);
 }

 @Test
 public void testLocaleLookupList_EmptyDefaultInput() {
 Locale locale = new Locale("fr", "CA");
 Locale defaultLocale = new Locale("");
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
 Assert.assertEquals(Arrays.asList(locale, defaultLocale), actualList);
 }

 @Test
 public void testLocaleLookupList_DifferentOrder() {
 Locale locale = new Locale("fr", "CA");
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> expectedList = Arrays.asList(defaultLocale, locale);
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
 Assert.assertEquals(expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_NullLocaleInput() {
 Locale locale = null;
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
 Assert.assertEquals(Arrays.asList(defaultLocale), actualList);
 }

 @Test
 public void testLocaleLookupList_EmptyDefaultLocale() {
 Locale locale = new Locale("fr", "CA");
 Locale defaultLocale = new Locale("");
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, defaultLocale);
 Assert.assertEquals(Arrays.asList(locale), actualList);
 }

 @Test
 public void testLocaleLookupList_EmptyLocale() {
 Locale locale = new Locale("");
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);
 Assert.assertEquals(Collections.singletonList(locale), actualList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedList = LocaleUtils.availableLocaleList();
 List<Locale> actualList = Arrays.asList(Locale.getAvailableLocales());
 Assert.assertEquals(expectedList, actualList);
 }

 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> expectedSet = LocaleUtils.availableLocaleSet();
 Set<Locale> actualSet = new HashSet<>(Locale.getAvailableLocales());
 Assert.assertEquals(expectedSet, actualSet);
 }

 @Test
 public void testIsAvailableLocale_ValidLocale() {
 Locale locale = new Locale("en", "US");
 boolean expectedResult = true;
 boolean actualResult = LocaleUtils.isAvailableLocale(locale);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
 Locale locale = new Locale("en", "XX");
 boolean expectedResult = false;
 boolean actualResult = LocaleUtils.isAvailableLocale(locale);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "US-POSIX"), new Locale("en", "US-vue"), new Locale("en-US-POSIX"), new Locale("en-US-Vue"), new Locale("en_US"));
 List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
 Assert.assertEquals(expectedList, actualList);
 }

 @Test
 public void testLanguagesByCountry_EmptyInput() {
 String countryCode = "";
 List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
 Assert.assertEquals(Collections.emptyList(), actualList);
 }

 @Test
 public void testLanguagesByCountry_NullInput() {
 String countryCode = null;
 List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
 Assert.assertEquals(Collections.emptyList(), actualList);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "CA"), new Locale("en", "IE"), new Locale("en-GB"), new Locale("en-IE"), new Locale("en-CA"), new Locale("en-ZA"), new Locale("en_US"), new Locale("en_GB"), new Locale("en_CA"), new Locale("en_ZA"));
 List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
 Assert.assertEquals(expectedList, actualList);
 }

 @Test
 public void testCountriesByLanguage_EmptyInput() {
 String languageCode = "";
 List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
 Assert.assertEquals(Collections.emptyList(), actualList);
 }

 @Test
 public void testCountriesByLanguage_NullInput() {
 String languageCode = null;
 List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
 Assert.assertEquals(Collections.emptyList(), actualList);
 }
}