import org.apache.commons.lang3.LocaleUtils;
import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocaleUtilsTest {

 @Test
 public void testToLocale_ValidLocale() {
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
 public void testToLocale_NullString() {
 Locale actualLocale = LocaleUtils.toLocale(null);

 Assert.assertNull(actualLocale);
 }

 @Test
 public void testToLocale_InvalidFormat_MissingLanguage() {
 String invalidLocaleString = "_US";

 Assert.throwsException(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testToLocale_InvalidFormat_MissingCountry() {
 String invalidLocaleString = "en_";

 Assert.throwsException(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidLanguageLength() {
 String invalidLocaleString = "en_fr_es";

 Assert.throwsException(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testToLocale_InvalidFormat_InvalidCountryLength() {
 String invalidLocaleString = "en_fr";

 Assert.throwsException(IllegalArgumentException.class, () -> LocaleUtils.toLocale(invalidLocaleString));
 }

 @Test
 public void testLocaleLookupList_ValidLocale() {
 Locale locale = new Locale("fr", "CA");
 List<Locale> expectedList = Arrays.asList(new Locale("fr", "CA"), new Locale("fr", ""), new Locale("", "CA"), new Locale("", ""));
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);

 Assert.assertEquals(expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_NullLocale() {
 List<Locale> actualList = LocaleUtils.localeLookupList(null);

 Assert.assertEquals(Collections.emptyList(), actualList);
 }

 @Test
 public void testLocaleLookupList_EmptyLocale() {
 Locale locale = new Locale("");
 List<Locale> expectedList = Arrays.asList(new Locale("", ""), new Locale("", ""));
 List<Locale> actualList = LocaleUtils.localeLookupList(locale);

 Assert.assertEquals(expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 List<Locale> expectedList = Arrays.asList(new Locale("", ""), new Locale("en", ""));
 List<Locale> actualList = LocaleUtils.localeLookupList(null, new Locale("en", "US"));

 Assert.assertEquals(expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_InvalidLocale() {
 Locale invalidLocale = new Locale("fr", "US-AL");
 List<Locale> expectedList = Arrays.asList(new Locale("fr", ""), new Locale("fr", ""), new Locale("", ""), new Locale("en", "US"));
 List<Locale> actualList = LocaleUtils.localeLookupList(invalidLocale);

 Assert.assertEquals(expectedList, actualList);
 }

 @Test
 public void testAvailableLocaleList_ExpectedLocalesPresent() {
 List<Locale> availableLocales = LocaleUtils.availableLocaleList();

 Assert.assertTrue(availableLocales.contains(new Locale("en", "US")));
 Assert.assertTrue(availableLocales.contains(new Locale("fr", "FR")));
 }

 @Test
 public void testAvailableLocaleList_ExpectedLocalesAbsent() {
 List<Locale> availableLocales = LocaleUtils.availableLocaleList();

 Assert.assertFalse(availableLocales.contains(new Locale("es", "ES")));
 Assert.assertFalse(availableLocales.contains(new Locale("de", "DE")));
 }

 @Test
 public void testIsAvailableLocale_ExpectedLocalePresent() {
 Locale locale = new Locale("es", "ES");

 Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testIsAvailableLocale_ExpectedLocaleAbsent() {
 Locale locale = new Locale("de", "DE");

 Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
 }

 @Test
 public void testLanguagesByCountry_ValidCountryCode() {
 String countryCode = "US";
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "US-POSIX"), new Locale("en", "US-virgin islands"), new Locale("en", "US-minnesota"));
 List<Locale> actualLocales = LocaleUtils.languagesByCountry(countryCode);

 Assert.assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testLanguagesByCountry_EmptyCountryCode() {
 List<Locale> actualLocales = LocaleUtils.languagesByCountry(null);

 Assert.assertEquals(Collections.emptyList(), actualLocales);
 }

 @Test
 public void testLanguagesByCountry_InvalidCountryCode() {
 String invalidCountryCode = "xx";
 List<Locale> actualLocales = LocaleUtils.languagesByCountry(invalidCountryCode);

 Assert.assertEquals(Collections.emptyList(), actualLocales);
 }

 @Test
 public void testCountriesByLanguage_ValidLanguageCode() {
 String languageCode = "en";
 List<Locale> expectedLocales = Arrays.asList(new Locale("en", "US"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "CA"));
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage(languageCode);

 Assert.assertEquals(expectedLocales, actualLocales);
 }

 @Test
 public void testCountriesByLanguage_EmptyLanguageCode() {
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage(null);

 Assert.assertEquals(Collections.emptyList(), actualLocales);
 }

 @Test
 public void testCountriesByLanguage_InvalidLanguageCode() {
 String invalidLanguageCode = "xx";
 List<Locale> actualLocales = LocaleUtils.countriesByLanguage(invalidLanguageCode);

 Assert.assertEquals(Collections.emptyList(), actualLocales);
 }
}