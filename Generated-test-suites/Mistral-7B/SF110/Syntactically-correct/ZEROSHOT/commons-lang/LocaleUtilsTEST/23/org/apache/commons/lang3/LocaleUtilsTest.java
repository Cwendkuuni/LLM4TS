import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

 private List<Locale> getAvailableLocales() {
 return LocaleUtils.availableLocaleList();
 }

 @Test
 public void testToLocale_ValidLocaleFormat() {
 // Test for cases with only language
 String language = "en";
 Locale expectedLocale = new Locale(language);
 Locale actualLocale = LocaleUtils.toLocale(language);
 assertEquals(expectedLocale, actualLocale);

 // Test for cases with language and country
 String languageCountry = "en_US";
 expectedLocale = new Locale(language, "US");
 actualLocale = LocaleUtils.toLocale(languageCountry);
 assertEquals(expectedLocale, actualLocale);

 // Test for cases with language, country and variant
 String languageCountryVariant = "en_US_Win";
 expectedLocale = new Locale(language, "US", "Win");
 actualLocale = LocaleUtils.toLocale(languageCountryVariant);
 assertEquals(expectedLocale, actualLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_MissingLanguage() {
 String invalidLocale = "_US";
 LocaleUtil.toLocale(invalidLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_MissingLanguageAndCountry() {
 String invalidLocale = "_";
 LocaleUtil.toLocale(invalidLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_MissingCountry() {
 String invalidLocale = "en_";
 LocaleUtil.toLocale(invalidLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_InvalidLanguageLength() {
 String invalidLocale = "enxx";
 LocaleUtil.toLocale(invalidLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_InvalidCountryLength() {
 String invalidLocale = "en_xx";
 LocaleUtil.toLocale(invalidLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_InvalidCountryCharacter() {
 String invalidLocale = "en_#";
 LocaleUtil.toLocale(invalidLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_MissingUpperCaseCountryLetter() {
 String invalidLocale = "en_fr";
 LocaleUtil.toLocale(invalidLocale);
 }

 @Test
 public void testToLocale_InvalidLocaleFormat_EmptyString() {
 String emptyString = "";
 Locale actualLocale = LocaleUtils.toLocale(emptyString);
 Locale expectedLocale = new Locale("");
 assertEquals(expectedLocale, actualLocale);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToLocale_InvalidLocaleFormat_ContainsHash() {
 String invalidLocale = "en_US#";
 LocaleUtil.toLocale(invalidLocale);
 }

 @Test
 public void testLocaleLookupList() {
 Locale locale = new Locale("en", "US");
 List<Locale> expectedList = LocaleUtils.localeLookupList(locale);
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, null);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testLocaleLookupList_DefaultLocale() {
 Locale locale = new Locale("es");
 List<Locale> expectedList = Arrays.asList(new Locale("es"), new Locale("es", "ES"), new Locale("en"), new Locale("en", "US"));
 List<Locale> actualList = LocaleUtils.localeLookupList(locale, Locale.US);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testAvailableLocaleList() {
 List<Locale> expectedList = LocaleUtils.availableLocaleList();
 List<Locale> actualList = getAvailableLocales();
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testIsAvailableLocale() {
 Locale locale = new Locale("en", "US");
 boolean expectedResult = true;
 boolean actualResult = LocaleUtils.isAvailableLocale(locale);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testIsAvailableLocale_InvalidLocale() {
 Locale locale = new Locale("", "");
 boolean expectedResult = false;
 boolean actualResult = LocaleUtils.isAvailableLocale(locale);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testLanguagesByCountry() {
 String countryCode = "US";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "JM"), new Locale("en", "HK"), new Locale("en", "ZA"));
 List<Locale> actualList = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testCountriesByLanguage() {
 String languageCode = "en";
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"), new Locale("en", "JM"), new Locale("en", "HK"), new Locale("en", "ZA"), new Locale("en", "AU"), new Locale("en", "NZ"), new Locale("en", "TT"), new Locale("en", "ZW"), new Locale("en", "SG"), new Locale("en", "MY"), new Locale("en", "PH"), new Locale("en", "ID"), new Locale("en", "SG"), new Locale("en", "LK"), new Locale("en", "BN"), new Locale("en", "BM"));
 List<Locale> actualList = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(expectedList, actualList);
 }
}