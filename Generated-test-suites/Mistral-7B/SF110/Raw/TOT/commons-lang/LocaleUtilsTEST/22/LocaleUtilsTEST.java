import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

 // Test case for toLocale method with valid arguments
 @Test
 public void testToLocaleValidArguments() {
 String testString = "en_US";
 Locale expectedLocale = new Locale("en", "US");
 Locale actualLocale = LocaleUtils.toLocale(testString);
 assertEquals(expectedLocale, actualLocale);
 }

 // Test case for toLocale method with invalid arguments - null
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleNull() {
 LocaleUtils.toLocale(null);
 }

 // Test case for toLocale method with invalid arguments - empty string
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleEmptyString() {
 LocaleUtils.toLocale("");
 }

 // Test case for toLocale method with invalid arguments - invalid format
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleInvalidFormat() {
 String testString = "en_US#";
 LocaleUtils.toLocale(testString);
 }

 // Test case for toLocale method with invalid arguments - length less than 2
 @Test(expected = IllegalArgumentException.class)
 public void testToLocaleLengthLessThanTwo() {
 String testString = "en";
 LocaleUtils.toLocale(testString);
 }

 // Test case for localeLookupList method with null Locale
 @Test
 public void testLocaleLookupListNullLocale() {
 List<Locale> result = LocaleUtils.localeLookupList(null);
 assertEquals(0, result.size());
 }

 // Test case for localeLookupList method with valid Locale
 @Test
 public void testLocaleLookupListValidLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> result = LocaleUtils.localeLookupList(locale);
 List<Locale> expectedList = Arrays.asList(locale, new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
 assertEquals(expectedList, result);
 }

 // Test case for localeLookupList method with null defaultLocale
 @Test
 public void testLocaleLookupListNullDefaultLocale() {
 Locale locale = new Locale("en", "US");
 List<Locale> result = LocaleUtils.localeLookupList(locale, null);
 List<Locale> expectedList = Arrays.asList(locale, new Locale("en", "US"), new Locale("en", ""), new Locale("", "US"), new Locale("", ""));
 assertEquals(expectedList, result);
 }

 // Test case for localeLookupList method with valid Locale and defaultLocale
 @Test
 public void testLocaleLookupListBothValid() {
 Locale locale = new Locale("fr", "CA");
 Locale defaultLocale = new Locale("en", "US");
 List<Locale> result = LocaleUtils.localeLookupList(locale, defaultLocale);
 List<Locale> expectedList = Arrays.asList(locale, new Locale("fr", "CA"), new Locale("fr", ""), new Locale("", "CA"), new Locale("en", "US"), new Locale("en", ""));
 assertEquals(expectedList, result);
 }

 // Test case for availableLocaleList method
 @Test
 public void testAvailableLocaleList() {
 List<Locale> result = LocaleUtils.availableLocaleList();
 List<Locale> expectedList = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
 assertEquals(expectedList, result);
 }

 // Test case for availableLocaleSet method
 @Test
 public void testAvailableLocaleSet() {
 Set<Locale> result = LocaleUtils.availableLocaleSet();
 Set<Locale> expectedSet = LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
 assertEquals(expectedSet, result);
 }

 // Test case for isAvailableLocale method with available Locale
 @Test
 public void testIsAvailableLocaleAvailable() {
 Locale locale = new Locale("en", "US");
 assertTrue(LocaleUtils.isAvailableLocale(locale));
 }

 // Test case for isAvailableLocale method with invalid Locale
 @Test
 public void testIsAvailableLocaleInvalid() {
Locale locale = new Locale("invalid", "US");
 assertFalse(LocaleUtils.isAvailableLocale(locale));
}

 // Test case for languagesByCountry method with valid countryCode
 @Test
 public void testLanguagesByCountryValidCountryCode() {
 String countryCode = "US";
 List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
 List<Locale> expectedList = Collections.singletonList(new Locale("en", "US"));
 assertEquals(expectedList, result);
 }

 // Test case for languagesByCountry method with invalid countryCode
 @Test
 public void testLanguagesByCountryInvalidCountryCode() {
 String countryCode = "invalid";
 List<Locale> result = LocaleUtils.languagesByCountry(countryCode);
 assertEquals(0, result.size());
 }

 // Test case for countriesByLanguage method with valid languageCode
 @Test
 public void testCountriesByLanguageValidLanguageCode() {
 String languageCode = "en";
 List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
 List<Locale> expectedList = Arrays.asList(new Locale("en", "US"), new Locale("en", "CA"), new Locale("en", "GB"), new Locale("en", "IE"));
 assertEquals(expectedList, result);
 }

 // Test case for countriesByLanguage method with invalid languageCode
 @Test
 public void testCountriesByLanguageInvalidLanguageCode() {
 String languageCode = "invalid";
 List<Locale> result = LocaleUtils.countriesByLanguage(languageCode);
 assertEquals(0, result.size());
 }
}